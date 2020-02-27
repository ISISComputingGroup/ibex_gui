package uk.ac.stfc.isis.ibex.scriptgenerator.generation;
/**
 * This class is an interface for GeneratedProgrammingLanguage and GeneratedDataExchangeFormat enums.
 * It allows to distinguish whether an enum being used is of type PROGRAMMING_LANGUAGE or DATA_EXCHANGE_FORMAT,
 * and perform different operations accordingly.
 */
public interface GenerationType {

	String PROGRAMMING_LANGUAGE = "programming language";
	String DATA_EXCHANGE_FORMAT = "data exchange format";
	
	String toString();
	String getType();
}
