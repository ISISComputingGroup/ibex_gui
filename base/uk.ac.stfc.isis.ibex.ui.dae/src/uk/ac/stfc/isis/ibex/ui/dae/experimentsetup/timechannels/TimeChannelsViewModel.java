package uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.timechannels;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import uk.ac.stfc.isis.ibex.dae.experimentsetup.timechannels.CalculationMethod;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.timechannels.TimeChannels;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.timechannels.TimeRegime;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.timechannels.TimeUnit;
import uk.ac.stfc.isis.ibex.model.ModelObject;

public class TimeChannelsViewModel extends ModelObject {

	private TimeChannels model;
	
	public void setModel(TimeChannels timeChannels) {
		model = timeChannels;
		
		model.addPropertyChangeListener("timeRegimes", new PropertyChangeListener() {
		@Override
			public void propertyChange(PropertyChangeEvent e) {
				firePropertyChange(e.getPropertyName(), e.getOldValue(), e.getNewValue());
			}
		});
	}
	
	public List<TimeRegime> timeRegimes() {		
		return model.timeRegimes();
	}

	public String getTimeChannelFile() {
		return model.timeChannelFile();
	}
	
	public void setTimeChannelFile(String value) {
		model.setTimeChannelFile(value);
	}
	
	public int getCalculationMethod() {	
		return model.calculationMethod().ordinal();
	}

	public void setCalculationMethod(int index) {
		CalculationMethod value = CalculationMethod.values()[index];
		model.setCalculationMethod(value);
	}
	
	public int getTimeUnit() {
		return model.timeUnit().ordinal();
	}
	
	public void setTimeUnit(int index) {
		TimeUnit value = TimeUnit.values()[index];
		model.setTimeUnit(value);
	}
}
