package uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.URL;
import java.util.List;


import py4j.ClientServer;
import py4j.ClientServer.ClientServerBuilder;
import org.apache.logging.log4j.Logger;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;

import uk.ac.stfc.isis.ibex.logger.IsisLog;

public class PythonInterface {

	
	private ClientServer clientServer;
	private Process pythonProcess;
	
	private static final Logger LOG = IsisLog.getLogger(PythonInterface.class);
	
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
	private String action_loader_python_script;
	private ConfigWrapper configWrapper;
	
	
	public PythonInterface() {
		this("/defined_actions/action_loader.py");
	}
	
	public PythonInterface(String action_loader_python_script) {
		this.action_loader_python_script = action_loader_python_script;
	}


	/**
	 * 
	 * @return The path to the python 3 interpreter. This will be bundled with the GUI later.
	 */
	private String python3InterpreterPath() {
		return "C:\\Instrument\\Apps\\Python3\\python.exe";
	}

	
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
        String absoluteFilePath = relativePathToFull(filePath);
        ProcessBuilder builder = new ProcessBuilder().command(pythonPath, absoluteFilePath, javaPort.toString(), pythonPort.toString());
		return builder.start();
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
	 * Initialises
	 */
 	
	public List<Config> getActions() throws IOException {
		return configWrapper.getActions();
	}
	
	/**
	 * 
	 * NB should call clean up at some point
	 * @return
	 * @throws IOException
	 */
	public void setUpPythonThread() throws IOException {
		clientServer = createClientServer();
		pythonProcess = startPythonProcess(clientServer, python3InterpreterPath(), action_loader_python_script);
		new Thread(listenToErrors).start();
		
		ConfigWrapper configWrapper = (ConfigWrapper) clientServer.getPythonServerEntryPoint(new Class[] { ConfigWrapper.class });
		
		this.configWrapper = configWrapper ;
	}

	
	/**
	 * Cleans up all resources.
	 */
	public void cleanUp() {
		pythonProcess.destroy();
	}
	
	
	/**
	 * Gets the full path to a file given the path relative to this plugin.
	 * @param relativePath The path of the file relative to this plugin.
	 * @return The full path.
	 * @throws IOException if the file could not be found.
	 */
	private String relativePathToFull(String relativePath) throws IOException {
		URL resourcePath = getClass().getResource(relativePath);
		
		String fullPath = FileLocator.resolve(resourcePath).getPath();
		//TODOD DELTEE 
		
		return Path.forWindows(fullPath).toOSString();
	}
	
}
