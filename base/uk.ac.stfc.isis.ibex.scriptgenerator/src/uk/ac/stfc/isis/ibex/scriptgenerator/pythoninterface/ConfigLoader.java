package uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.stream.Stream;

import py4j.ClientServer;
import py4j.ClientServer.ClientServerBuilder;
import uk.ac.stfc.isis.ibex.scriptgenerator.ActionParameter;

public class ConfigLoader {

	private final ClientServer clientServer;
	private final Stream<ActionParameter> actionParameters;
	
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
        		.command("C:\\Python37\\python.exe", "C:\\Instrument\\Dev\\ibex_gui\\\\base\\uk.ac.stfc.isis.ibex.scriptgenerator\\DummyAction.py ", javaPort.toString(), pythonPort.toString());
        try {
			Process newProcess = builder.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        ActionWrapper actionWrapper = (ActionWrapper) clientServer.getPythonServerEntryPoint(new Class[] { ActionWrapper.class });
        
        actionParameters = actionWrapper.getParameters().keySet().stream().map(name -> new ActionParameter(name));
	}

	public Stream<ActionParameter> getActionParameters() {
		return actionParameters;
	}

}