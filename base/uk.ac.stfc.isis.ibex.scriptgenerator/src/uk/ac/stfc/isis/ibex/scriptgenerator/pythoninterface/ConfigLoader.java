package uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.util.Scanner;

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
        
	}

	public void print() {
        // We get an entry point from the Python side
		ActionWrapper hello = (ActionWrapper) clientServer.getPythonServerEntryPoint(new Class[] { ActionWrapper.class });
        // Java calls Python without ever having been called from Python
        System.out.println(hello.getParameters());
        //clientServer.shutdown();
        
	}
}