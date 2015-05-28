package uk.ac.stfc.isis.ibex.experimentdetails;

public enum Role {
	PI("PI"),
	USER("User"),
	CONTACT("Contact"),
	BLANK("")
	;
	
	private final String text;
	
	private Role(final String text){
		this.text = text;
	}
	
	@Override
	public String toString(){
		return text;
	}
}
