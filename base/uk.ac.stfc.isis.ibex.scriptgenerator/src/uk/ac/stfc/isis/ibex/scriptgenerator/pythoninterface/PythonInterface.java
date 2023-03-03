package uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import py4j.ClientServer;
import py4j.ClientServer.ClientServerBuilder;
import py4j.Py4JException;

import org.apache.logging.log4j.Logger;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.preferences.PreferenceSupplier;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorProperties;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

/**
 * Sets up the py4j interface and acts as an interface when calling into Py4J.
 *
 */
public class PythonInterface extends ModelObject {

	/**
	 * The client-server connection to use to communicate with Python.
	 */
	private ClientServer clientServer;
	
	/**
	 * The python process executing calls to python,
	 */
	private Process pythonProcess;
	
	/**
	 * The access point to python that wraps the rest of the python functionality.
	 */
	private ScriptDefinitionsWrapper scriptDefinitionsWrapper;
	
	/**
	 * Defines whether python is ready to receive Py4J calls.
	 */
	private boolean pythonReady = false;
	
	/**
	 * The default script definition loader script.
	 */
	private static final String DEFAULT_SCRIPT_DEFINITION_LOADER_SCRIPT = "/python_support/script_definition_loader.py";
	
	
	/**
	 * The script definition loader script to use.
	 */
	private String scriptDefinitionLoaderScript = DEFAULT_SCRIPT_DEFINITION_LOADER_SCRIPT;

	/**
	 * The path to the script generator actually used.
	 */
	
	private static final Logger LOG = IsisLog.getLogger(PythonInterface.class);
	
	/**
	 * The time to wait before retrying restarting python in ms.
	 */
	private static final int TIME_TO_WAIT_BEFORE_RETRY = 1000;
	
	/**
	 * The name for the script generator worker thread.
	 */
	private static final String THREAD_NAME = "Py4J scriptgenerator worker";

	/**
	 * The thread to execute python calls on.
	 */
	private static final ExecutorService THREAD = Executors
			.newSingleThreadExecutor(job -> new Thread(job, THREAD_NAME));
	
	/**
	 * The scripts with ids as keys.
	 */
	private Map<Integer, Optional<String>> generatedScripts = new HashMap<Integer, Optional<String>>();
	
	/**
	 * The ID of the last script that's been added.
	 */
	private Integer lastScriptId = 0;

	/**
	 * Constructor uses default script definition loader python script location.
	 */
	public PythonInterface() {
		this(DEFAULT_SCRIPT_DEFINITION_LOADER_SCRIPT);
	}

	/**
	 * Constructor starts the python given python script.
	 * 
	 * @param scriptDefinitionLoaderPythonScript Path to the script definition loader python script to
	 *                                 start.
	 */
	public PythonInterface(String scriptDefinitionLoaderPythonScript) {
		scriptDefinitionLoaderScript = scriptDefinitionLoaderPythonScript;
	}

	/**
	 * Forwards errors from the python process.
	 */
	private Runnable listenToErrors = () -> {
		try {
			InputStreamReader isr = new InputStreamReader(pythonProcess.getErrorStream());
			BufferedReader br = new BufferedReader(isr);
			String line = null;

			while ((line = br.readLine()) != null) {
				if (!pythonProcess.isAlive()) {
					break;
				}
				LOG.error("> " + line);
			}
		} catch (IOException e) {
			LOG.warn("Could not forward process errors");
			LOG.warn(e);
		}
	};
	
	/**
	 * Asserts that a method is only ever called on the Py4J worker thread.
	 */
	private static void assertOnPy4jWorkerThread() {
		if (!Thread.currentThread().getName().contains(THREAD_NAME)) {
			final var errorMsg = String.format("Programming error: assertOnPy4jWorkerThread() called on thread that wasn't Py4J worker thread (actually called from %s)", Thread.currentThread().getName());
			LOG.error(errorMsg);
			throw new RuntimeException(errorMsg);
		}
	}
	
	/**
	 * When python has become not ready handle this by trying to restart it.
	 * MUST always be called in Py4J worker thread.
	 * MUST never wait for a thread to complete or return in here otherwise deadlock will occur.
	 */
	private void restartPython() {
		assertOnPy4jWorkerThread();
		
		try {
			cleanUp();
			setUpPythonThread();
		} catch (IOException e) {
			LOG.error("Failed to load Python");
			LOG.error(e);
			try {
				Thread.sleep(TIME_TO_WAIT_BEFORE_RETRY);
			} catch (InterruptedException e1) {
				LOG.error(e);
			}
			handlePythonReadinessChange(false);
		}
	}
	
	/**
	 * The readiness of python has changed. 
	 * Fire the property change and if python is no longer ready attempt to start python again.
	 * 
	 * @param ready Whether python is ready or not.
	 */
	protected void handlePythonReadinessChange(boolean ready) {
		boolean wasPythonReady = pythonReady;
		firePropertyChange(ScriptGeneratorProperties.PYTHON_READINESS_PROPERTY, pythonReady, pythonReady = ready);
		if (!ready && wasPythonReady != ready) {
			THREAD.submit(() -> restartPython());
		}
	}

	/**
	 * 
	 * @return The path to the bundled python 3 interpreter.
	 */
	private String python3InterpreterPath() {
		return PreferenceSupplier.getPythonPath();
	}

	/**
	 * Creates the connection that will be used to communicate with the Python.
	 * 
	 * @return The client server connection.
	 * @throws IOException if the connection could not be made.
	 */
	private ClientServer createClientServer() throws IOException {
		ClientServerBuilder clientServerBuilder = new ClientServerBuilder();
		return clientServerBuilder.pythonPort(getFreeSocket()).javaPort(getFreeSocket()).build();
	}

	/**
	 * Spawns a python process in the operating system.
	 * 
	 * @param clientServer Jython clientServer class.
	 * @param pythonPath   Path to the python interpreter to be used.
	 * @param filePath     Path to python script to be executed.
	 * @return The spawned python process.
	 * @throws IOException When the python process fails to start correctly.
	 */
	private Process startPythonProcess(ClientServer clientServer, String pythonPath, String filePath)
			throws IOException {
		Integer javaPort = clientServer.getJavaServer().getPort();
		Integer pythonPort = clientServer.getPythonClient().getPort();
		Optional<String> scriptDefinitionsRepo = new PreferenceSupplier().scriptGeneratorScriptDefinitionFolder();

		String absoluteScriptPath = relativePathToFull(filePath);
		ProcessBuilder builder;
		
		if (scriptDefinitionsRepo.isEmpty()) {
			builder = new ProcessBuilder().command(pythonPath, absoluteScriptPath, javaPort.toString(),
					pythonPort.toString());
		} else { 
			builder = new ProcessBuilder().command(pythonPath, absoluteScriptPath, javaPort.toString(),
					pythonPort.toString(), String.format("--repo_path=%s", scriptDefinitionsRepo.get()));
		}
		pythonProcess = builder.start();
		try {
			if (!pythonProcess.isAlive() || pythonProcess.exitValue() != 0) {
				String message = "Python process did not start correctly. Exit value: " + pythonProcess.exitValue();
				LOG.error(message);
				throw new IOException(message);
			}
		} catch (IllegalThreadStateException e) {
			// Thrown by exitValue() if python process is still running (which is good)
		}
		return pythonProcess;
	}

	/**
	 * @return The next free socket on the machine.
	 * @throws IOException if a free socket cannot be found
	 */
	private int getFreeSocket() throws IOException {
		try (ServerSocket socket = new ServerSocket(0)) {
			socket.setReuseAddress(true);
			return socket.getLocalPort();
		}
	}
	
	/**
	 * Gets all script definitions that could not be loaded and the reason.
	 * 
	 * @return Any errors when loading script definitions, the key is the script definition name,
	 *  the value is the reason it could not load.
	 * @throws PythonNotReadyException When python is not ready to accept calls.
	 */
	public Map<String, String> getScriptDefinitionLoadErrors() throws PythonNotReadyException {
		if (pythonReady) {
			try {
				return scriptDefinitionsWrapper.getScriptDefinitionLoadErrors();
			} catch (Py4JException e) {
				LOG.error(e);
				handlePythonReadinessChange(false);
				throw new PythonNotReadyException("When getting script definition load errors");
			}
		} else {
			throw new PythonNotReadyException("When getting script definition load errors");
		}
	}

	/**
	 * Gets all available script definitions from the python script.
	 * 
	 * @return A list of available script definitions.
	 * @throws PythonNotReadyException When python is not ready to accept calls.
	 */
	public List<ScriptDefinitionWrapper> getScriptDefinitions() throws PythonNotReadyException {
		if (pythonReady) {
			try {
				return scriptDefinitionsWrapper.getScriptDefinitions();
			} catch (Py4JException e) {
				LOG.error(e);
				handlePythonReadinessChange(false);
				throw new PythonNotReadyException("When getting script definitions");
			}
		} else {
			throw new PythonNotReadyException("When getting script definitions");
		}
	}
	
	/**
	 * Create the py4j client/server and starts the python thread in the Py4J worker thread. 
	 */
	public void workerSetUpPythonThread() {
		THREAD.submit(() -> {
			try {
				setUpPythonThread();
			} catch (IOException e) {
				LOG.error(e);
			}
		});
	}

	/**
	 * 
	 * Creates the py4j client/server and starts the python thread. 
	 * ALWAYS called inside the Py4J worker thread.
	 * 
	 * @throws IOException If scriptDefinitionLoaderPythonScript not found.
	 */
	private void setUpPythonThread() throws IOException {
		assertOnPy4jWorkerThread();
		
		firePropertyChange(ScriptGeneratorProperties.PYTHON_READINESS_PROPERTY, null, pythonReady);
		clientServer = createClientServer();
		pythonProcess = startPythonProcess(clientServer, python3InterpreterPath(), scriptDefinitionLoaderScript);
		new Thread(listenToErrors, "ScriptGenerator error listener").start();
		
		this.scriptDefinitionsWrapper = (ScriptDefinitionsWrapper) clientServer
				.getPythonServerEntryPoint(new Class[] {ScriptDefinitionsWrapper.class});
		
		while (true) {
			try {
				this.scriptDefinitionsWrapper.isPythonReady();
				handlePythonReadinessChange(true);
				break;
			} catch (Py4JException e) {
				// Waiting until Python is ready (no Py4JException)
				LOG.info("ScriptGenerator setUpPythonThread: waiting for python to start (last Py4j exception message was '" + e.getMessage() + "')");
				try {
					Thread.sleep(TIME_TO_WAIT_BEFORE_RETRY);
				} catch (InterruptedException ex) {
					continue;
				}
			}
		}
	}

	/**
	 * Get whether there are updates available for the git repository.
	 * @return true if there are updates available.
	 */
	public boolean updatesAvailable() {
		return this.scriptDefinitionsWrapper.updatesAvailable();
		
	}
	
	/**
	 * Cleans up all resources i.e. destroy the python process.
	 */
	public void cleanUp() {
		pythonProcess.destroyForcibly();
		clientServer.shutdown();
	}

	/**
	 * Gets the full path to a file given the path relative to this plugin.
	 * 
	 * @param relativePath The path of the file relative to this plugin.
	 * @return The full path.
	 * @throws IOException if the file could not be found.
	 */
	private String relativePathToFull(String relativePath) throws IOException {
		URL resourcePath = getClass().getResource(relativePath);

		String fullPath = FileLocator.resolve(resourcePath).getPath();

		return Path.forWindows(fullPath).toOSString();
	}
	
	private List<Map<String, String>> convertScriptGenContentToPython(List<ScriptGeneratorAction> scriptGenContent) {
		return scriptGenContent.stream()
				.map(action -> action.getActionParameterValueMapAsStrings()).collect(Collectors.toList());
	}
	
	private interface RefreshRunnable {
		Object run() throws PythonNotReadyException, InterruptedException, ExecutionException;
	}
	
	private void runRefreshRunnable(RefreshRunnable runnable, String property, String errorMsg) throws InterruptedException, ExecutionException, PythonNotReadyException {
		if (pythonReady) {
			CompletableFuture.supplyAsync(() -> {
				try {
					return runnable.run();
				} catch (Py4JException | PythonNotReadyException | InterruptedException | ExecutionException e) {
					LOG.error(e);
					handlePythonReadinessChange(false);
					return null;
				}
			}, THREAD).thenAccept(newValue -> firePropertyChange(property, null, newValue));
		} else {
			handlePythonReadinessChange(false);
			throw new PythonNotReadyException(errorMsg);
		}
	}

	/**
	 * Use python to get validity errors of the current parameters and refresh the
	 * validity error message property.
	 * 
	 * @param scriptGenContent The script generator content to validate.
	 * @param scriptDefinition           The script definition to validate against.
	 * @param globalParams The global parameters to check parameter validity with.
	 * @throws ExecutionException   A failure to execute the py4j call
	 * @throws InterruptedException The Py4J call was interrupted
	 * @throws PythonNotReadyException When python is not ready to accept calls.
	 */
	public void refreshValidityErrors(List<String> globalParams, List<ScriptGeneratorAction> scriptGenContent, ScriptDefinitionWrapper scriptDefinition)
			throws InterruptedException, ExecutionException, PythonNotReadyException {
		runRefreshRunnable(() -> {
			return scriptDefinitionsWrapper.getValidityErrors(globalParams, convertScriptGenContentToPython(scriptGenContent), scriptDefinition);
        }, ScriptGeneratorProperties.VALIDITY_ERROR_MESSAGE_PROPERTY, "When getting validity errors");
	}

	/**
	 * Use python to check the validity of the parameters and refresh the parameter
	 * validity property.
	 * 
	 * @param scriptGenContent The script generator content to validate.
	 * @param scriptDefinition           The script definition to validate against.
	 * @param globalParams The global parameters to check parameter validity with.
	 * @throws ExecutionException   A failure to execute the py4j call
	 * @throws InterruptedException The Py4J call was interrupted
	 * @throws PythonNotReadyException When python is not ready to accept calls.
	 */
	public void refreshAreParamsValid(List<ScriptGeneratorAction> scriptGenContent, List<String> globalParams, ScriptDefinitionWrapper scriptDefinition)
			throws InterruptedException, ExecutionException, PythonNotReadyException {
		runRefreshRunnable(() -> {
			return scriptDefinitionsWrapper.areParamsValid(convertScriptGenContentToPython(scriptGenContent), globalParams, scriptDefinition);
        }, ScriptGeneratorProperties.PARAM_VALIDITY_PROPERTY, "When getting parameter validity");
	}
	
    /**
     * Use python to estimate the time estimation for the current parameters and refresh the
     * time estimation property.
     * 
     * @param scriptGenContent The script generator content
     * @param scriptDefinition           The script definition
     * @param globalParams The global parameters to refresh time estimation with.
     * @throws ExecutionException   A failure to execute the py4j call
     * @throws InterruptedException The Py4J call was interrupted
     * @throws PythonNotReadyException When python is not ready to accept calls.
     */
    public void refreshTimeEstimation(List<ScriptGeneratorAction> scriptGenContent, ScriptDefinitionWrapper scriptDefinition, List<String> globalParams)
            throws InterruptedException, ExecutionException, PythonNotReadyException {
    	runRefreshRunnable(() -> {
    		return scriptDefinitionsWrapper.estimateTime(convertScriptGenContentToPython(scriptGenContent), scriptDefinition, globalParams);
        }, ScriptGeneratorProperties.TIME_ESTIMATE_PROPERTY, "When getting time estimation");
    }
    
    /**
     * Use python to calculate a custom estimation defined by the scriptDefinition for the current parameters and refresh the
     * custom estimation property.
     * 
     * @param scriptGenContent The script generator content
     * @param scriptDefinition The script definition
     * @param globalParams The global parameters to refresh custom estimation with.
     * @throws ExecutionException A failure to execute the py4j call
     * @throws InterruptedException The Py4J call was interrupted
     * @throws PythonNotReadyException When python is not ready to accept calls.
     */
    public void refreshCustomEstimation(List<ScriptGeneratorAction> scriptGenContent, ScriptDefinitionWrapper scriptDefinition, List<String> globalParams)
            throws InterruptedException, ExecutionException, PythonNotReadyException {
    	runRefreshRunnable(() -> {
    		return scriptDefinitionsWrapper.estimateCustom(convertScriptGenContentToPython(scriptGenContent), scriptDefinition, globalParams);
        }, ScriptGeneratorProperties.CUSTOM_ESTIMATE_PROPERTY, "When getting custom estimation");
    }
    
    /**
     * Only call this from a non-client thread.
     * Generate a script.
     * 
     * @param scriptGenContent The contents to generate the script with. An optional that is empty if parameters are invalid.
	 * @param jsonContent json content that will be hexed and compressed
	 * @param scriptDefinition           The script definition to generate the script with.
	 * @param globalParams The global parameters to generate the script with.
	 * @return An optional script.
     */
    private Optional<String> generateScript(List<ScriptGeneratorAction> scriptGenContent, String jsonContent, List<String> globalParams, ScriptDefinitionWrapper scriptDefinition) {
    	try {
			return Optional.of(scriptDefinitionsWrapper.generate(convertScriptGenContentToPython(scriptGenContent), jsonContent, globalParams, scriptDefinition));
		} catch (Py4JException e) {
			LOG.error(e);
			handlePythonReadinessChange(false);
			return Optional.empty();
		}
    }

	/**
	 * Generate a script in python and refresh the generated script property.
	 * 
	 * @param scriptGenContent The contents to generate the script with. An optional that is empty if parameters are invalid.
	 * @param jsonContent json content that will be hexed and compressed
	 * @param scriptDefinition           The script definition to generate the script with.
	 * @param globalParams The global parameters to refresh the generated script with.
	 * @throws ExecutionException     A failure to execute the py4j call
	 * @throws InterruptedException   The Py4J call was interrupted
	 * @throws PythonNotReadyException When python is not ready to accept calls.
	 * @return An ID for the generated script.
	 */
	public int refreshGeneratedScript(List<ScriptGeneratorAction> scriptGenContent, String jsonContent, List<String> globalParams, ScriptDefinitionWrapper scriptDefinition)
			throws InterruptedException, ExecutionException, PythonNotReadyException {
		lastScriptId += 1;
		var scriptId = lastScriptId;
		generatedScripts.put(scriptId, Optional.empty());
		if (pythonReady) {
			CompletableFuture.supplyAsync(() -> {
				return generateScript(scriptGenContent, jsonContent, globalParams, scriptDefinition);
			}, THREAD)
				.thenAccept(generatedScript -> {
					generatedScripts.put(scriptId, generatedScript);
					firePropertyChange(ScriptGeneratorProperties.GENERATED_SCRIPT_PROPERTY, null, Optional.of(scriptId));
				});
		} else {
			handlePythonReadinessChange(false);
			throw new PythonNotReadyException("When getting generated script");
		}
		return scriptId;
	}
	
	/**
	 * Get the script that is related to the given ID.
	 * 
	 * @param scriptId The ID for the script
	 * @return The script
	 */
	public Optional<String> getScriptFromId(Integer scriptId) {
		return generatedScripts.get(scriptId);
	}

	/**
	 * Get the list of git errors raised while loading.
	 * @return List of error messages from git loading.
	 */
	public List<String> getGitLoadErrors() {
		return scriptDefinitionsWrapper.getGitErrors();
	}

	/**
	 * Gets whether the remote git repo URL is accessible.
	 * @return true if the remote repo URL can be accessed.
	 */
	public boolean remoteAvailable() {
		return scriptDefinitionsWrapper.remoteAvailable();
	}

	/**
	 * Determine from the python whether the git repository is dirty.
	 * 
	 * @return true if the git repository is dirty.
	 */
	public boolean isDirty() {
		return scriptDefinitionsWrapper.isDirty();
	}

	/**
	 * Merges git repository from upstream.
	 */
	public void mergeOrigin() {
		scriptDefinitionsWrapper.mergeOrigin();
		
	}

	/**
	 * Gets the path to the script definitions repository.
	 * @return The path to the script definitions repository.
	 */
	public String getRepoPath() {
		return scriptDefinitionsWrapper.getRepoPath();
	}


}
