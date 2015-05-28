package uk.ac.stfc.isis.ibex.ui.motor.views;

import org.eclipse.core.runtime.Path;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.opis.Opi;
import uk.ac.stfc.isis.ibex.ui.OPIView;

public class MotorOPIView extends OPIView {
		
	private String partName = "Motor view";
	
	public MotorOPIView() {
		setTitleToolTip("A more detailed view of the motor");
		setTitleImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.motor", "icons/cog.png"));
	}

	public static final String ID = "uk.ac.stfc.isis.ibex.ui.motor.views.MotorOPIView"; //$NON-NLS-1$
	
	public void setMotorName(String motorName) {
		macros().put("P", Instrument.getInstance().currentInstrument().pvPrefix());
		macros().put("MM", motorName);		
	}
	
	public void setOPITitle(String partName) {
		this.partName = partName;
	}
	
	@Override
	public void initialiseOPI() {
		super.initialiseOPI();
		super.setPartName(partName);
	}

	@Override
	protected Path opi() {
		return Opi.getDefault().provider().pathFromName("Motor/mymotor.opi");
	}
}
