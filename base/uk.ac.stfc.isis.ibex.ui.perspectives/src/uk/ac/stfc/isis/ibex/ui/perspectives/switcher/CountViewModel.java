package uk.ac.stfc.isis.ibex.ui.perspectives.switcher;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import uk.ac.stfc.isis.ibex.log.LogCounter;
import uk.ac.stfc.isis.ibex.model.ModelObject;

public class CountViewModel extends ModelObject {
	
	private static final String IOC_LOG = "IOC Log"; 
	
	private String text;
	private boolean hasMessages;
	
	public CountViewModel(final LogCounter counter) {
		counter.addPropertyChangeListener("count", new PropertyChangeListener() {			
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				update(counter.getCount());
			}
		});
		
		update(counter.getCount());
	}
	
	public String getText() {
		return text;
	}
	
	public boolean hasMessages() {
		return hasMessages;
	}
	
	private void update(long count) {
		setText(textForCount(count));
		setHasMessages(count > 0);		
	}
	
	private void setText(String newText) {
		firePropertyChange("text", text, text = newText);
	}
	
	private void setHasMessages(boolean updated) {
		firePropertyChange("hasMessages", hasMessages, hasMessages = updated);
	}
	
	private String textForCount(long count) {
		return IOC_LOG + optionalCount(count);
	}

	private String optionalCount(Long count) {
		if (count == 0) {
			return "";
		}
		
		String displayedCount = count > 100 ? "100+" : count.toString();
		return " (" + displayedCount + ")";
	}
}
