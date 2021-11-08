package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.util.Optional;

public class DynamicScript {
	
	private Integer id;
	private Optional<String> code;
	
	public DynamicScript(Integer id, String code) {
		this.id = id;
		this.code = Optional.of(code);
	}
	
	public DynamicScript(Integer id) {
		this.id = id;
		this.code = Optional.empty();
	}
	
	public void setCode(String code) {
		this.code = Optional.of(code);
	}
	
	public static Integer getIdFromName(String name) throws DynamicScriptingException {
		var splitName = name.split(" ");
		if (splitName.length == 3) {
			try {
				return Integer.parseInt(splitName[2]);
			} catch(NumberFormatException e) {
				throw new DynamicScriptingException("Format should be like 'Script Generator: <Integer ID>'");
			}
		} else {
			throw new DynamicScriptingException("Format should be like 'Script Generator: <Integer ID>'");
		}
	}
	
	public String getName() {
		return String.format("Script Generator: %d", id);
	}
	
	public Integer getId() {
		return id;
	}
	
	public Optional<String> getCode() {
		return code;
	}

}
