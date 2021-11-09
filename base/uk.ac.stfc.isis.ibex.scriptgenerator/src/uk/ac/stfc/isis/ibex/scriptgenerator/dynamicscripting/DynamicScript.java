package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.util.Optional;

public class DynamicScript {
	
	private Integer id;
	private Optional<String> code;
	
	public DynamicScript(Integer id) {
		this.id = id;
		this.code = Optional.empty();
	}
	
	public static Integer getIdFromName(String name) throws DynamicScriptNameFormatException {
		String prefix = "Script Generator: ";
		if (name.startsWith(prefix)) {
			String idWithWhitespace = name.substring(prefix.length());
			String id = idWithWhitespace.strip();
			try {
				return Integer.parseInt(id);
			} catch(NumberFormatException e) {
				throw new DynamicScriptNameFormatException("Format should be like 'Script Generator: <Integer ID>'");
			}
		} else {
			throw new DynamicScriptNameFormatException("Format should be like 'Script Generator: <Integer ID>'");
		}
	}
	
	public String getName() {
		return String.format("Script Generator: %d", id);
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setCode(String code) {
		this.code = Optional.of(code);
	}
	
	public Optional<String> getCode() {
		if (code.isPresent()) {
			return Optional.of(code.get() + "\nrunscript()");
		}
		return code;
	}

}
