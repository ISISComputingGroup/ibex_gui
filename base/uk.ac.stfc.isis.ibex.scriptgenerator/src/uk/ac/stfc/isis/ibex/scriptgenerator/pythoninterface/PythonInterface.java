package uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import py4j.ClientServer;
import py4j.ClientServer.ClientServerBuilder;
import org.apache.logging.log4j.Logger;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.preferences.PreferenceSupplier;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.InvalidParamsException;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

/**
 * Sets up the py4j interface.
 *
 */
public class PythonInterface extends ModelObject {

	private ClientServer clientServer;
	private Process pythonProcess;
	private ConfigWrapper configWrapper;

	private static final String VALIDITY_ERROR_MESSAGE_PROPERTY = "validity error messages";
	private static final String PARAM_VALIDITY_PROPERTY = "parameter validity";
	private static final String GENERATED_SCRIPT_PROPERTY = "generated script";

	private static final Logger LOG = IsisLog.getLogger(PythonInterface.class);

	private static final ExecutorService THREAD = Executors
			.newSingleThreadExecutor(job -> new Thread(job, "Py4J scriptgenerator worker"));

	/**
	 * Constructor uses default action loader python script location.
	 */
	public PythonInterface() {
		this("/defined_actions/action_loader.py");
	}

	/**
	 * Constructor starts the python given python script.
	 * 
	 * @param actionLoaderPythonScript Path to the action loader python script to
	 *                                 start.
	 */
	public PythonInterface(String actionLoaderPythonScript) {
		try {
			this.setUpPythonThread(actionLoaderPythonScript);
		} catch (IOException e) {
			LOG.error("Failed to set up py4j interface");
			LOG.error(e);
		}
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
	 * @throws IOException
	 */
	private Process startPythonProcess(ClientServer clientServer, String pythonPath, String filePath)
			throws IOException {
		Integer javaPort = clientServer.getJavaServer().getPort();
		Integer pythonPort = clientServer.getPythonClient().getPort();
		String absoluteFilePath = relativePathToFull(filePath);
		ProcessBuilder builder = new ProcessBuilder().command(pythonPath, absoluteFilePath, javaPort.toString(),
				pythonPort.toString());
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
	 * Gets all available actions from the python script.
	 */
	public List<Config> getActionDefinitions() {
		return configWrapper.getActionDefinitions();
	}

	/**
	 * 
	 * Creates the py4j client/server and starts the python thread.
	 * 
	 * @param actionLoaderPythonScript Path to the script containing the Config and
	 *                                 ConfigWrapper classes.
	 * @throws IOException If actionLoaderPythonScript not found.
	 */
	public void setUpPythonThread(String actionLoaderPythonScript) throws IOException {
		clientServer = createClientServer();
		pythonProcess = startPythonProcess(clientServer, python3InterpreterPath(), actionLoaderPythonScript);
		new Thread(listenToErrors).start();
		
		this.configWrapper = (ConfigWrapper) clientServer
				.getPythonServerEntryPoint(new Class[] { ConfigWrapper.class });
	}

	/**
	 * Cleans up all resources.
	 */
	public void cleanUp() {
		pythonProcess.destroy();
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

	/**
	 * Use python to get validity errors of the current parameters and refresh the
	 * validity error message property.
	 * 
	 * @param scriptGenContent The script generator content to validate.
	 * @param config           The config to validate against.
	 * @throws ExecutionException   A failure to execute the py4j call
	 * @throws InterruptedException The Py4J call was interrupted
	 */
	public void refreshValidityErrors(List<ScriptGeneratorAction> scriptGenContent, Config config)
			throws InterruptedException, ExecutionException {
		CompletableFuture.supplyAsync(() -> { 
			var result = configWrapper.getValidityErrors(scriptGenContent, config);
			return result;
			}, THREAD)
				.thenAccept(newValidityErrors -> firePropertyChange(VALIDITY_ERROR_MESSAGE_PROPERTY, null, newValidityErrors));
	}

	/**
	 * Use python to check the validity of the parameters and refresh the parameter
	 * validity property.
	 * 
	 * @param scriptGenContent The script generator content to validate.
	 * @param config           The config to validate against.
	 * @throws ExecutionException   A failure to execute the py4j call
	 * @throws InterruptedException The Py4J call was interrupted
	 */
	public void refreshAreParamsValid(List<ScriptGeneratorAction> scriptGenContent, Config config)
			throws InterruptedException, ExecutionException {
		CompletableFuture.supplyAsync(() -> configWrapper.areParamsValid(scriptGenContent, config), THREAD)
				.thenAccept(paramValidity -> firePropertyChange(PARAM_VALIDITY_PROPERTY, null, paramValidity));
	}

	/**
	 * Generate a script in python and refresh the generated script property.
	 * 
	 * @param scriptGenContent The contents to generate the script with.
	 * @param config           The config to generate the script with.
	 * @throws InvalidParamsException Thrown if the contents (actionsTable) are
	 *                                invalid.
	 * @throws ExecutionException     A failure to execute the py4j call
	 * @throws InterruptedException   The Py4J call was interrupted
	 */
	public void refreshGeneratedScript(List<ScriptGeneratorAction> scriptGenContent, Config config)
			throws InterruptedException, ExecutionException {
		CompletableFuture.supplyAsync(() -> configWrapper.generate(scriptGenContent, config), THREAD)
				.thenAccept(generatedScript -> firePropertyChange(GENERATED_SCRIPT_PROPERTY, null, generatedScript));
	}

}
