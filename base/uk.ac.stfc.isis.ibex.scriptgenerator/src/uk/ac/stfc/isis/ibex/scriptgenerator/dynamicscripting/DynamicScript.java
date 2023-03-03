package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.util.Optional;

/**
 * A data transfer object for a dynamic script.
 */
public class DynamicScript {
	
	private Integer id;
	private Optional<String> code;
	
	/**
	 * Create the dynamic script with the id and no code.
	 * 
	 * @param id The id of the dynamic script.
	 */
	public DynamicScript(Integer id) {
		this.id = id;
		this.code = Optional.empty();
	}
	
	/**
	 * Given the name, obtain the ID of the script or an exception if it is not in the correct format.
	 * 
	 * @param name The name of the script.
	 * @return The ID of the script.
	 * @throws DynamicScriptNameFormatException Thrown if the name is not of the correct format to get the ID from.
	 */
	public static Integer getIdFromName(String name) throws DynamicScriptNameFormatException {
		String prefix = "Script Generator: ";
		if (name.startsWith(prefix)) {
			String idWithWhitespace = name.substring(prefix.length());
			String id = idWithWhitespace.strip();
			try {
				return Integer.parseInt(id);
			} catch (NumberFormatException e) {
				throw new DynamicScriptNameFormatException("Format should be like 'Script Generator: <Integer ID>'");
			}
		} else {
			throw new DynamicScriptNameFormatException("Format should be like 'Script Generator: <Integer ID>'");
		}
	}
	
	/**
	 * @return The name of the script.
	 */
	public String getName() {
		return String.format("Script Generator: %d", id);
	}
	
	/**
	 * @return The ID of the script.
	 */
	public Integer getId() {
		return id;
	}
	
	/**
	 * Set the code for this script.
	 * 
	 * @param code The code to set
	 */
	public void setCode(String code) {
		this.code = Optional.of(code);
	}
	
	/**
	 * @return An optional of the code with the runscript() call appended or an empty optional if not set.
	 */
	public Optional<String> getCode() {
		if (code.isPresent()) {
			return Optional.of(code.get() + "\nrunscript()");
		}
		return code;
	}

}
