package uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import py4j.ClientServer;
import py4j.ClientServer.ClientServerBuilder;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.scriptgenerator.ActionParameter;

public class ConfigLoader extends ModelObject {

	private final ClientServer clientServer;
	private List<Config> availableConfigs;
	private Config selectedConfig;
	private ArrayList<ActionParameter> parameters;
	
	private int getFreeSocket() {
        try (ServerSocket socket = new ServerSocket(0)) {
            socket.setReuseAddress(true);
            return socket.getLocalPort();
        } catch (IOException e) {
        	e.printStackTrace();
        	return 0;
        }
	}
	
	public ConfigLoader() {
        ClientServerBuilder clientServerBuilder = new ClientServerBuilder();
        clientServer = clientServerBuilder.pythonPort(getFreeSocket()).javaPort(getFreeSocket()).build();
        Integer javaPort = clientServer.getJavaServer().getPort();
        Integer pythonPort = clientServer.getPythonClient().getPort();
        
        ProcessBuilder builder = new ProcessBuilder().inheritIO()
        		.command("C:\\Python37\\python.exe", "C:\\Instrument\\Dev\\ibex_gui\\\\base\\uk.ac.stfc.isis.ibex.scriptgenerator\\defined_actions\\action_loader.py ", javaPort.toString(), pythonPort.toString());
        try {
			Process newProcess = builder.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        ConfigWrapper configWrapper = (ConfigWrapper) clientServer.getPythonServerEntryPoint(new Class[] { ConfigWrapper.class });
        
        availableConfigs = configWrapper.getActions();
        setConfig(availableConfigs.get(0));
	}

	public List<Config> getAvailableConfigs() {
		return availableConfigs;
	}

	public void setConfig(Config action) {
		selectedConfig = action;
		ArrayList<ActionParameter> parameters = action.getParameters().keySet().stream()
				.map(name -> new ActionParameter(name)).collect(Collectors.toCollection(ArrayList::new));
		firePropertyChange("parameters", this.parameters, this.parameters=parameters);
	}
	
	public ArrayList<ActionParameter> getParameters() {
		return parameters;
	}
	
	public Config getConfig() {
		return selectedConfig;
	}
	
}