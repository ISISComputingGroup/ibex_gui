package uk.ac.stfc.isis.ibex.scriptgenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;

import org.apache.logging.log4j.Logger;

import uk.ac.stfc.isis.ibex.logger.IsisLog;

/**
 * A class for handling the writing to and opening of script generator files.
 */
public class ScriptGeneratorFileHandler {
	
	private static final Logger LOG = IsisLog.getLogger(ScriptGeneratorFileHandler.class);
	
	/**
	 * Save a string to a file with the name as specified in the dialog box (a generated script generally).
	 * 
	 * @param toWrite The string to write to file
	 * @param scriptFile The file to write to
	 */
	private void writeToFile(String toWrite, File scriptFile) throws IOException {
		try (BufferedWriter scriptWriter = new BufferedWriter(new FileWriter(scriptFile))) {
			scriptWriter.write(toWrite);
			scriptWriter.flush();
		}
	}
	
	/**
	 * Save a string to a file with the name as specified in the dialog box (a generated script generally).
	 * With a check to see if we are overwriting a file.
	 * 
	 * @param filepath The path to write the file to
	 * @param toWrite The string to write to file
	 * @throws IOException When there is an error writing the script to file.
	 */
	public void generate(String filepath, String toWrite) throws IOException {
		File scriptFile = new File(filepath);
		try {
			writeToFile(toWrite, scriptFile);
		} catch (IOException e) {
			LOG.error("Failed to write generated file");
			LOG.error(e);
			throw e;
		}
	}
	
	/**
	 * Open the specified file in notepad++.
	 * 
	 * @param filepath The full path to open, including extension
	 * @throws OpenFileException Thrown when failing to find notepad to open the file.
	 * @throws IOException Thrown when attempting to open the file.
	 */
	public void openFile(String filepath) 
			throws OpenFileException, IOException {
		File file = new File(filepath);
		try {
			if (file.exists()) {
				Runtime rs = Runtime.getRuntime();
				String notepadExe = findNotepadExe();
				rs.exec(String.format("%s %s", notepadExe, file));
			} else {
				String notepadLaunchWarning = "Could not launch notepad++, file does not exist";
				LOG.info(notepadLaunchWarning);
				LOG.error("Failed to open file " + file.getAbsolutePath());
				throw new OpenFileException(notepadLaunchWarning);
			}
		} catch (IOException e) {
			LOG.catching(e);
			LOG.error("Failed to open file " + file.getAbsolutePath());
			throw e;
		}
	}
	
	/**
	 * Find the notepad executable so we can launch it to open the file.
	 * 
	 * @return The location of notepad.exe
	 * @throws IOException If we fail to find notepad throw this
	 */
	private String findNotepadExe() throws IOException {
		String[] possibleLocations = {"C:\\Program Files\\Notepad++", "C:\\Program Files (x86)\\Notepad++"};
		for (String location : possibleLocations) {
			File directory = new File(location);
			if (directory.exists()) {
				File[] possibleFiles = directory.listFiles(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						return name.equals("notepad++.exe");
					}
				});
				if (possibleFiles.length > 0) {
					return possibleFiles[0].getAbsolutePath();
				}
			}
		}
		throw new IOException("Failed to find notepad to launch");
	}

}
