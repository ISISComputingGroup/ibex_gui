package uk.ac.stfc.isis.ibex.experimentdetails.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import uk.ac.stfc.isis.ibex.epics.observing.ClosableCachingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.TransformingObservable;
import uk.ac.stfc.isis.ibex.experimentdetails.Parameter;

public class ParametersObservable extends TransformingObservable<Collection<String>, Collection<Parameter>> {

	private final ExperimentDetailsVariables variables;

	public ParametersObservable(
			ExperimentDetailsVariables variables, 
			ClosableCachingObservable<Collection<String>> availableParameters) {
		this.variables = variables;
		setSource(availableParameters);
	}
	
	@Override
	protected Collection<Parameter> transform(Collection<String> value) {
		List<Parameter> parameters = new ArrayList<>();
		for (String address : value) {
			parameters.add(createParameter(address));
		}
		
		return parameters;
	}
	
	protected Parameter createParameter(String address) {
		return new ObservableParameter(
				variables.parameterName(address),
				variables.parameterUnits(address),
				variables.parameterValue(address),
				variables.parameterValueSetter(address));
	}
}
