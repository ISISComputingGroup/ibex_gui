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
	private ConfigWrapper configWrapper;

	/**
	 * The property change to fire when the validity messages are asynchronously
	 *  received from Python.
	 */
	private static final String VALIDITY_ERROR_MESSAGE_PROPERTY = "validity error messages";
	
	/**
	 * The property change to fire when the validity of the script generator contents is
	 *  asynchronously received from Python.
	 */
	private static final String PARAM_VALIDITY_PROPERTY = "parameter validity";
	
	/**
	 * The property change to fire when the generated script is received
	 *  asynchronously from Python.
	 */
	private static final String GENERATED_SCRIPT_PROPERTY = "generated script";
	
	/**
	 * A property to notify listeners when python becomes ready or not ready.
	 */
	private static final String PYTHON_READINESS_PROPERTY = "python ready";
	
	/**
	 * Defines whether python is ready to receive Py4J calls.
	 */
	private boolean pythonReady = false;
	
	/**
	 * The default action loader script.
	 */
	private static final String DEFAULT_ACTION_LOADER_SCRIPT = "/defined_actions/action_loader.py";
	
	/**
	 * The action loader script to use.
	 */
	private String actionLoaderScript = DEFAULT_ACTION_LOADER_SCRIPT;

	private static final Logger LOG = IsisLog.getLogger(PythonInterface.class);

	/**
	 * The thread to execute python calls on.
	 */
	private static final ExecutorService THREAD = Executors
			.newSingleThreadExecutor(job -> new Thread(job, "Py4J scriptgenerator worker"));

	/**
	 * Constructor uses default action loader python script location.
	 */
	public PythonInterface() {
		this(DEFAULT_ACTION_LOADER_SCRIPT);
	}

	/**
	 * Constructor starts the python given python script.
	 * 
	 * @param actionLoaderPythonScript Path to the action loader python script to
	 *                                 start.
	 */
	public PythonInterface(String actionLoaderPythonScript) {
		actionLoaderScript = actionLoaderPythonScript;
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
				if (line.equals("Python Ready")) {
					handlePythonReadinessChange(true);
					LOG.info("Python Ready");
				} else {
					LOG.error("> " + line);
				}
			}
		} catch (IOException e) {
			LOG.warn("Could not forward process errors");
			LOG.warn(e);
		}
	};
	
	/**
	 * The readiness of python has changed. 
	 * Fire the property change and if python is no longer ready attempt to start python again.
	 * 
	 * @param ready Whether python is ready or not.
	 */
	protected void handlePythonReadinessChange(boolean ready) {
		boolean wasPythonReady = pythonReady;
		firePropertyChange(PYTHON_READINESS_PROPERTY, pythonReady, pythonReady = ready);
		if (ready == false && wasPythonReady != ready) {
			THREAD.submit(() -> {
				try {
					cleanUp();
					setUpPythonThread();
				} catch(IOException e) {
					LOG.error("Failed to load Python");
					LOG.error(e);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						LOG.error(e);
					}
					handlePythonReadinessChange(ready);
				}
			});
		}
	}

	/**
	 * 
	 * @return The path to the bundled python 3 interpreter.
	 */
	private String python3InterpreterPath() {
		return PreferenceSupplier.getBundledPythonPath();
	}

	/**
	 * Creates the connection that will be used to communicate with the Python
	 * configuration.
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
		String configSearchFolders = new PreferenceSupplier().scriptGeneratorConfigFolders();
		String absoluteFilePath = relativePathToFull(filePath);
		ProcessBuilder builder = new ProcessBuilder().command(pythonPath, absoluteFilePath, javaPort.toString(),
				pythonPort.toString(), configSearchFolders);
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
	 * Gets all actions that could not be loaded and the reason.
	 * @throws PythonNotReadyException When python is not ready to accept calls.
	 */
	public Map<String, String> getConfigLoadErrors() throws PythonNotReadyException {
		if(pythonReady) {
			try {
				return configWrapper.getConfigLoadErrors();
			} catch (Py4JException e) {
				LOG.error(e);
				handlePythonReadinessChange(false);
				throw new PythonNotReadyException("When getting config load errors");
			}
		} else {
			throw new PythonNotReadyException("When getting config load errors");
		}
	}

	/**
	 * Gets all available actions from the python script.
	 * @throws PythonNotReadyException When python is not ready to accept calls.
	 */
	public List<Config> getActionDefinitions() throws PythonNotReadyException {
		if(pythonReady) {
			try {
				return configWrapper.getActionDefinitions();
			} catch (Py4JException e) {
				LOG.error(e);
				handlePythonReadinessChange(false);
				throw new PythonNotReadyException("When getting configs");
			}
		} else {
			throw new PythonNotReadyException("When getting configs");
		}
	}

	/**
	 * 
	 * Creates the py4j client/server and starts the python thread.
	 * 
	 * @param actionLoaderPythonScript Path to the script containing the Config and
	 *                                 ConfigWrapper classes.
	 * @throws IOException If actionLoaderPythonScript not found.
	 */
	public void setUpPythonThread() throws IOException {
		firePropertyChange(PYTHON_READINESS_PROPERTY, null, pythonReady);
		clientServer = createClientServer();
		pythonProcess = startPythonProcess(clientServer, python3InterpreterPath(), actionLoaderScript);
		new Thread(listenToErrors).start();
		
		this.configWrapper = (ConfigWrapper) clientServer
				.getPythonServerEntryPoint(new Class[] { ConfigWrapper.class });
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
				.map(action -> action.getAllActionParametersAsString()).collect(Collectors.toList());
	}

	/**
	 * Use python to get validity errors of the current parameters and refresh the
	 * validity error message property.
	 * 
	 * @param scriptGenContent The script generator content to validate.
	 * @param config           The config to validate against.
	 * @throws ExecutionException   A failure to execute the py4j call
	 * @throws InterruptedException The Py4J call was interrupted
	 * @throws PythonNotReadyException When python is not ready to accept calls.
	 */
	public void refreshValidityErrors(List<ScriptGeneratorAction> scriptGenContent, Config config)
			throws InterruptedException, ExecutionException, PythonNotReadyException {
		if(pythonReady) {
			CompletableFuture.supplyAsync(() -> {
				try {
					return configWrapper.getValidityErrors(convertScriptGenContentToPython(scriptGenContent), config);
				} catch (Py4JException e) {
					LOG.error(e);
					handlePythonReadinessChange(false);
					return new HashMap<>();
				}
			}, THREAD)
				.thenAccept(newValidityErrors -> firePropertyChange(VALIDITY_ERROR_MESSAGE_PROPERTY, null, newValidityErrors));
		} else {
			handlePythonReadinessChange(false);
			throw new PythonNotReadyException("When getting validity errors");
		}
		if(!pythonReady) {
			throw new PythonNotReadyException("When getting validity errors");
		}
	}

	/**
	 * Use python to check the validity of the parameters and refresh the parameter
	 * validity property.
	 * 
	 * @param scriptGenContent The script generator content to validate.
	 * @param config           The config to validate against.
	 * @throws ExecutionException   A failure to execute the py4j call
	 * @throws InterruptedException The Py4J call was interrupted
	 * @throws PythonNotReadyException When python is not ready to accept calls.
	 */
	public void refreshAreParamsValid(List<ScriptGeneratorAction> scriptGenContent, Config config)
			throws InterruptedException, ExecutionException, PythonNotReadyException {
		if(pythonReady) {
			CompletableFuture.supplyAsync(() -> {
				try {
					return configWrapper.areParamsValid(convertScriptGenContentToPython(scriptGenContent), config);
				} catch (Py4JException e) {
					LOG.error(e);
					handlePythonReadinessChange(false);
					return false;
				}
			}, THREAD)
				.thenAccept(paramValidity -> firePropertyChange(PARAM_VALIDITY_PROPERTY, null, paramValidity));
		} else {
			handlePythonReadinessChange(false);
			throw new PythonNotReadyException("When getting parameter validity");
		}
		if(!pythonReady) {
			throw new PythonNotReadyException("When getting parameter validity");
		}
	}

	/**
	 * Generate a script in python and refresh the generated script property.
	 * 
	 * @param scriptGenContent The contents to generate the script with. An optional that is empty if parameters are invalid.
	 * @param config           The config to generate the script with.
	 * @throws ExecutionException     A failure to execute the py4j call
	 * @throws InterruptedException   The Py4J call was interrupted
	 * @throws PythonNotReadyException When python is not ready to accept calls.
	 */
	public void refreshGeneratedScript(List<ScriptGeneratorAction> scriptGenContent, Config config)
			throws InterruptedException, ExecutionException, PythonNotReadyException {
		if(pythonReady) {
			CompletableFuture.supplyAsync(() -> {
				try {
					return configWrapper.generate(convertScriptGenContentToPython(scriptGenContent), config);
				} catch (Py4JException e) {
					LOG.error(e);
					handlePythonReadinessChange(false);
					return Optional.empty();
				}
			}, THREAD)
				.thenAccept(generatedScript -> {
					firePropertyChange(GENERATED_SCRIPT_PROPERTY, null, Optional.ofNullable(generatedScript));
				});
		} else {
			handlePythonReadinessChange(false);
			throw new PythonNotReadyException("When getting generated script");
		}
		if(!pythonReady) {
			throw new PythonNotReadyException("When getting generated script");
		}
	}

}
