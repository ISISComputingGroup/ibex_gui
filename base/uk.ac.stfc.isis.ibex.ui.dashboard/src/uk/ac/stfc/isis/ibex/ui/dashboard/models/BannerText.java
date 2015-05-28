package uk.ac.stfc.isis.ibex.ui.dashboard.models;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import uk.ac.stfc.isis.ibex.model.UpdatedValue;

public class BannerText extends UpdatedValue<String> {
		
	private final PropertyChangeListener update = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			setText(compose());
		}
	};

	private final UpdatedValue<String> name;
	private final UpdatedValue<String> state;
	
	public BannerText(UpdatedValue<String> name, UpdatedValue<String> state) {
		this.name = name;
		this.state = state;
		
		setText(compose());

		name.addPropertyChangeListener(update);
		state.addPropertyChangeListener(update);
	}

	private void setText(String text) {
		setValue(text);
	}
	
	private String compose() {
		return name.getValue() + "   is   " + state.getValue();
	}
}
