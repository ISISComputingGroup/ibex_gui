package uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;

import py4j.ClientServer;
import py4j.ClientServer.ClientServerBuilder;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.scriptgenerator.ActionParameter;

public class ConfigLoader extends ModelObject {
	
	private static final Logger LOG = IsisLog.getLogger(ConfigLoader.class);
	
	private ClientServer clientServer;
	private Process pythonProcess;
	private List<Config> availableConfigs;
	private ArrayList<ActionParameter> parameters = new ArrayList<ActionParameter>();
	private Config selectedConfig;
	
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
	 * Creates the connection that will be used to communicate with the Python configuration.
	 * @return The client server connection.
	 * @throws IOException if the connection could not be made.
	 */
	private ClientServer createClientServer() throws IOException {
        ClientServerBuilder clientServerBuilder = new ClientServerBuilder();
        return clientServerBuilder.pythonPort(getFreeSocket()).javaPort(getFreeSocket()).build();		
	}
	
	
	/**
	 * Spawns a python process in the operating system.
	 * @param clientServer
	 * 			Jython clientServer class.
	 * @param pythonPath
	 * 			Path to the python interpreter to be used.
	 * @param filePath
	 * 			Path to python script to be executed.
	 * @return
	 * 			The spawned python process.
	 * @throws IOException
	 */
	private Process startPythonProcess(ClientServer clientServer, String pythonPath, String filePath) throws IOException {
        Integer javaPort = clientServer.getJavaServer().getPort();
        Integer pythonPort = clientServer.getPythonClient().getPort();
        String absoluteFilePath = new ConfigUtils().relativePathToFull(filePath);
        ProcessBuilder builder = new ProcessBuilder().command(pythonPath, absoluteFilePath, javaPort.toString(), pythonPort.toString());
		return builder.start();
	}
	
	
	
	/**
	 * Constructor for the config loader, initialises the connection with python and reads the configurations.
	 */
	public ConfigLoader() {
        try {
        	get_script_generator_configurations();
            setConfig(availableConfigs.get(0));
		} catch (IOException e) {
			LOG.error("ConfigLoader could not start");
			LOG.error(e);
		}
	}


	private void get_script_generator_configurations() throws IOException {
		clientServer = createClientServer();
		pythonProcess = startPythonProcess(clientServer, python3InterpreterPath(), "/defined_actions/action_loader.py");
		new Thread(listenToErrors).start();
		
		ConfigWrapper configWrapper = (ConfigWrapper) clientServer.getPythonServerEntryPoint(new Class[] { ConfigWrapper.class });
		
		availableConfigs = configWrapper.getActions();
	}

	/**
	 * 
	 * @return The path to the python 3 interpreter. This will be bundled with the GUI later.
	 */
	private String python3InterpreterPath() {
		return "C:\\Instrument\\Apps\\Python3\\python.exe";
	}

	/**
	 * @return all found configurations.
	 */
	public List<Config> getAvailableConfigs() {
		return availableConfigs;
	}

	/**
	 * Set which configuration is currently loaded.
	 * @param config The currently loaded configuration.
	 */
	public void setConfig(Config config) {
		ArrayList<ActionParameter> parameters = config.getParameters().stream()
				.map(name -> new ActionParameter(name)).collect(Collectors.toCollection(ArrayList::new));
		firePropertyChange("parameters", this.parameters, this.parameters=parameters);
		firePropertyChange("config", this.selectedConfig, this.selectedConfig = config);
	}
	
	/**
	 * @return the currently loaded configuration.
	 */
	public Config getConfig() {
		return selectedConfig;
	}
	
	/**
	 * @return the parameters for the current configuration.
	 */
	public ArrayList<ActionParameter> getParameters() {
		return parameters;
	}
	
	/**
	 * Cleans up all resources.
	 */
	public void cleanUp() {
		pythonProcess.destroy();
	}
}
