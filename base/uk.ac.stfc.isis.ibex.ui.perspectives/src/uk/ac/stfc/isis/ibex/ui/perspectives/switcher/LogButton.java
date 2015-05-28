package uk.ac.stfc.isis.ibex.ui.perspectives.switcher;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class LogButton extends PerspectiveButton {

	private FlashingButton flash;
	private final Display display = Display.getDefault();
	private final DataBindingContext bindingContext = new DataBindingContext();
	
	private final CountViewModel model;
	
	public LogButton(Composite parent, final String perspective) {
		super(parent, perspective);
		
		flash = new FlashingButton(this, display);
		flash.setDefaultColour(this.getBackground());
	
		model = new CountViewModel(counter);
		bindingContext.bindValue(WidgetProperties.text().observe(this), BeanProperties.value("text").observe(model));
		model.addPropertyChangeListener("hasMessages", new PropertyChangeListener() {			
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				updateFlashing();
			}
		});
	}
	
	@Override
	protected void mouseEnterAction() {
		flash.stop();
		super.mouseEnterAction();
	}
	
	@Override
	protected void mouseExitAction() {
		super.mouseExitAction();
		updateFlashing();
	}
	
	@Override
	protected void mouseClickAction() {
		counter.stop();
		counter.resetCount();
	}
	
	private void updateFlashing() {
		if (model.hasMessages()) {
			flash.start();
		} else {
			flash.stop();
		}
	}
}
