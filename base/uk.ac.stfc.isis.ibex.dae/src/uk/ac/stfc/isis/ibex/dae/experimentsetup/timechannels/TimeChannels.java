package uk.ac.stfc.isis.ibex.dae.experimentsetup.timechannels;

import java.util.ArrayList;
import java.util.List;

import uk.ac.stfc.isis.ibex.model.ModelObject;

public class TimeChannels extends ModelObject {

	private List<TimeRegime> timeRegimes = new ArrayList<>();
	private String timeChannelFile = "";
	private CalculationMethod calculationMethod = CalculationMethod.UseParametersBelow;
	private TimeUnit timeUnit = TimeUnit.MICROSECONDS;
	
	public List<TimeRegime> timeRegimes() {
		return timeRegimes;
	}

	public void setTimeRegimes(List<TimeRegime> value) {
		firePropertyChange("timeRegimes", timeRegimes, timeRegimes = value);
	}
	
	public String timeChannelFile() {
		return timeChannelFile;
	}

	public void setTimeChannelFile(String value) {
		firePropertyChange("timeChannelFile", timeChannelFile, timeChannelFile = value);
	}

	public TimeUnit timeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(TimeUnit value) {
		firePropertyChange("timeUnit", timeUnit, timeUnit = value);
	}

	public CalculationMethod calculationMethod() {
		return calculationMethod;
	}
	
	public void setCalculationMethod(CalculationMethod value) {
		firePropertyChange("calculationMethod", calculationMethod, calculationMethod = value);
	}
}
