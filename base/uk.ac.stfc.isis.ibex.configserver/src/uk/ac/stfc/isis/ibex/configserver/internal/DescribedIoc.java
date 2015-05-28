package uk.ac.stfc.isis.ibex.configserver.internal;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

import com.google.common.base.Strings;

/*
 * An ioc with an updating description (which is initially blank)
 */
public class DescribedIoc extends EditableIoc {
	
	private final UpdatedValue<String> description;
	private String descriptionText;
	
	private final PropertyChangeListener updatedDesc = new PropertyChangeListener() {	
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			setDescription(Strings.nullToEmpty(description.getValue()));
		}
	};
	
	public DescribedIoc(EditableIoc ioc, UpdatedValue<String> description) {
		super(ioc);
		
		this.description = description;
		description.addPropertyChangeListener(updatedDesc, true);
	}
	
	public String getDescription() {
		return descriptionText;
	}

	private void setDescription(String text) {
		firePropertyChange("description", this.descriptionText, this.descriptionText = text);
	}
}
