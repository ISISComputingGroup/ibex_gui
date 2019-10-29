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
	private Config selectedConfig;
	private ArrayList<ActionParameter> parameters = new ArrayList<ActionParameter>();
	
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
	
	public ConfigLoader() {
        try {
            ClientServerBuilder clientServerBuilder = new ClientServerBuilder();
            clientServer = clientServerBuilder.pythonPort(getFreeSocket()).javaPort(getFreeSocket()).build();
            Integer javaPort = clientServer.getJavaServer().getPort();
            Integer pythonPort = clientServer.getPythonClient().getPort();
            String pythonPath = "C:\\Instrument\\Apps\\Python3\\python.exe";
            String filePath = relativePathToString("/defined_actions/action_loader.py");
            ProcessBuilder builder = new ProcessBuilder().command(pythonPath, filePath, javaPort.toString(), pythonPort.toString());
			pythonProcess = builder.start();
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

	public void setConfig(Config action) {
		selectedConfig = action;
		ArrayList<ActionParameter> parameters = action.getParameters().stream()
				.map(name -> new ActionParameter(name)).collect(Collectors.toCollection(ArrayList::new));
		firePropertyChange("parameters", this.parameters, this.parameters=parameters);
	}
	
	public ArrayList<ActionParameter> getParameters() {
		return parameters;
	}
	
	public Config getConfig() {
		return selectedConfig;
	}
	
	public void cleanUp() {
		try {
			pythonProcess.destroy();
		} catch (InterruptedException e) {
			LOG.warn(e);
		}
	}
	
}