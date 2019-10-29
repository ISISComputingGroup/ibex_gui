package uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;

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
	
	private String relativePathToString(String relativePath) throws IOException {
		URL resourcePath = getClass().getResource(relativePath);
		String fullPath = FileLocator.resolve(resourcePath).getPath();
		return Path.forWindows(fullPath).toOSString();
	}
	
	/**
	 * Forwards errors in the python process.
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
				System.out.println("> " + line);
			}
		} catch (IOException e) {
			LOG.warn("Could not forward process errors");
			LOG.warn(e);
		}
	};
	
	private ClientServer createClientServer() throws IOException {
        ClientServerBuilder clientServerBuilder = new ClientServerBuilder();
        return clientServerBuilder.pythonPort(getFreeSocket()).javaPort(getFreeSocket()).build();		
	}
	
	private Process startPythonProcess(ClientServer clientServer, String pythonPath, String filePath) throws IOException {
        Integer javaPort = clientServer.getJavaServer().getPort();
        Integer pythonPort = clientServer.getPythonClient().getPort();
        String absoluteFilePath = relativePathToString(filePath);
        ProcessBuilder builder = new ProcessBuilder().command(pythonPath, absoluteFilePath, javaPort.toString(), pythonPort.toString());
		return builder.start();
	}
	
	public ConfigLoader() {
        try {
        	clientServer = createClientServer();
        	pythonProcess = startPythonProcess(clientServer, "C:\\Instrument\\Apps\\Python3\\python.exe", "/defined_actions/action_loader.py");
            new Thread(listenToErrors).start();
            
            ConfigWrapper configWrapper = (ConfigWrapper) clientServer.getPythonServerEntryPoint(new Class[] { ConfigWrapper.class });
            
            availableConfigs = configWrapper.getActions();
            setConfig(availableConfigs.get(0));
		} catch (IOException e) {
			LOG.error("ConfigLoader could not start");
			LOG.error(e);
		}
	}

	public List<Config> getAvailableConfigs() {
		return availableConfigs;
	}

	public void setConfig(Config config) {
		ArrayList<ActionParameter> parameters = config.getParameters().stream()
				.map(name -> new ActionParameter(name)).collect(Collectors.toCollection(ArrayList::new));
		firePropertyChange("parameters", this.parameters, this.parameters=parameters);
		firePropertyChange("config", this.selectedConfig, this.selectedConfig = config);
	}
	
	public Config getConfig() {
		return selectedConfig;
	}
	
	public ArrayList<ActionParameter> getParameters() {
		return parameters;
	}
	
	public void cleanUp() {
		pythonProcess.destroy();
	}
}