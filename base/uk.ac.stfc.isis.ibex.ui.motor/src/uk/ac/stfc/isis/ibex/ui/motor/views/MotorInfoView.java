package uk.ac.stfc.isis.ibex.ui.motor.views;

import org.eclipse.swt.widgets.Composite;

public class MotorInfoView extends Composite {

	private MinimalMotorViewModel minimalMotorViewModel;
	
	public MotorInfoView(Composite parent, int style, MinimalMotorViewModel minimalMotorViewModel) {
		super(parent, style);
		
		this.minimalMotorViewModel = minimalMotorViewModel;
	}

	/**
	* Gets the MinimalMotorViewModel used by the cell.
	* 
	* @return the motor view model used by the cell.
	*/
	public MinimalMotorViewModel getViewModel() {
	    return minimalMotorViewModel;
	}
		
}
