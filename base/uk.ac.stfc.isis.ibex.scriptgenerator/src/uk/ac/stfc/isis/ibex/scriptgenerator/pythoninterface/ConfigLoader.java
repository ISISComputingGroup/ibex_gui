package uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface;

import java.io.IOException;
import java.net.ServerSocket;

import py4j.ClientServer;
import py4j.ClientServer.ClientServerBuilder;

public class ConfigLoader {

	ClientServer clientServer;
	
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
        System.out.println(clientServer.getJavaServer().getPort());
        System.out.println(clientServer.getPythonClient().getPort());
	}

	public void print() {
        // We get an entry point from the Python side
		ActionWrapper hello = (ActionWrapper) clientServer.getPythonServerEntryPoint(new Class[] { ActionWrapper.class });
        // Java calls Python without ever having been called from Python
        System.out.println(hello.getSomeString());
        //clientServer.shutdown();
	}
}