package uk.ac.stfc.isis.ibex.scriptgenerator.generation;

public enum GeneratedDataExchangeFormat implements GenerationType {
	JSON("Json");
	
	private final String name;
	
	private GeneratedDataExchangeFormat(String name) {
		this.name = name;
	}
	
	public String toString() {
		return name;
	}
	
	@Override
	public String getType() {
		return GenerationType.DATA_EXCHANGE_FORMAT;
	}

}
