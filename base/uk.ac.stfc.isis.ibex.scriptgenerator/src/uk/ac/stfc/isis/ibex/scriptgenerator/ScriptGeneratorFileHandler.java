package uk.ac.stfc.isis.ibex.scriptgenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;

import org.apache.logging.log4j.Logger;

import uk.ac.stfc.isis.ibex.logger.IsisLog;

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
	 * Checks if the filename contains a ., :, / or \
	 * 
	 * @return true if filename does not contain these, or false if it does.
	 */
	private boolean isFilenameValid(String filename) {
		return !(filename.contains(".") || filename.contains(":") || filename.contains("/") || filename.contains("\\"));
	}
	
	/**
	 * Save a string to a file with the name as specified in the dialog box (a generated script generally).
	 * With a check to see if we are overwriting a file.
	 * 
	 * @param filepathPrefix The prefix to add to the file path
	 * @param filename The name of the file to write to
	 * @param toWrite The string to write to file
	 * @param fileExtension The file extension to save 
	 * @throws FileGeneratorException If there is an issue when attempting to write the script to file.
	 * @throws IOException When there is an error writing the script to file.
	 * @throws CheckForOverwriteException Thrown when we need to check with the user if they wish to overwrite.
	 */
	public void generateWithOverwriteCheck(String filepathPrefix, String filename, String toWrite, String fileExtension)
			throws FileGeneratorException, IOException, CheckForOverwriteException {
		generate(filepathPrefix, filename, toWrite, fileExtension, true);
	}
	
	/**
	 * Save a string to a file with the name as specified in the dialog box (a generated script generally).
	 * With a check to see if we are overwriting a file.
	 * 
	 * @param filepathPrefix The prefix to add to the file path
	 * @param filename The name of the file to write to
	 * @param toWrite The string to write to file
	 * @param fileExtension The file extension to save 
	 * @throws FileGeneratorException If there is an issue when attempting to write the script to file.
	 * @throws IOException When there is an error writing the script to file.
	 */
	public void generateWithoutOverwriteCheck(String filepathPrefix, String filename, String toWrite, String fileExtension)
			throws FileGeneratorException, IOException {
		try {
			generate(filepathPrefix, filename, toWrite, fileExtension, false);
		} catch (CheckForOverwriteException e) {
			LOG.error(e);
			LOG.info("We have passed false to not check for overwrite so we should not throw this");
		}
	}
	
	/**
	 * Save a string to a file with the name as specified in the dialog box (a generated script generally).
	 * With a check to see if we are overwriting a file.
	 * 
	 * @param filepathPrefix The prefix to add to the file path
	 * @param filename The name of the file to write to
	 * @param toWrite The string to write to file
	 * @param fileExtension The file extension to save 
	 * @param checkForOverwrite true if you wish to check with the user if they want to overwrite the file or not.
	 * @throws FileGeneratorException If there is an issue when attempting to write the script to file.
	 * @throws IOException When there is an error writing the script to file.
	 * @throws CheckForOverwriteException Thrown when we need to check with the user if they wish to overwrite.
	 */
	private void generate(String filepathPrefix, String filename, String toWrite, String fileExtension,
			boolean checkForOverwrite) throws FileGeneratorException, IOException, CheckForOverwriteException {
		// Don't generate if filename contains extension or file path.
		if (!isFilenameValid(filename)) {
			throw new FileGeneratorException("Cannot save: filename contains a . ; / or \\");
		}
		File scriptFile = new File(filepathPrefix + filename + fileExtension);
		try {
			if (scriptFile.createNewFile() || !checkForOverwrite) {
				// There was no file preventing creation so we are not overwriting
				writeToFile(toWrite, scriptFile);
			} else {
				// There was a file preventing creation, check to see if we wish to overwrite
				throw new CheckForOverwriteException("File already exists, would you like to overwite?");
			}
		} catch (IOException e) {
			LOG.error("Failed to write generated file");
			LOG.error(e);
			throw e;
		}
	}
	
	/**
	 * Open the file specified in this dialog by the filepathPrefix, filename and passed fileExtension in notepad++.
	 * 
	 * @param fileExtension The file extension of the file to open.
	 * @throws OpenFileException Thrown when failing to find notepad to open the file.
	 * @throws IOException Thrown when attempting to open the file.
	 */
	public void openFile(String filepathPrefix, String filename, String fileExtension) 
			throws OpenFileException, IOException {
		File file = new File(filepathPrefix + filename + fileExtension);
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
