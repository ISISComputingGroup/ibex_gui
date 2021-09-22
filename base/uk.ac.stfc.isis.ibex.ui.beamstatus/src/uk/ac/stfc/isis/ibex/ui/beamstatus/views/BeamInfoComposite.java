package uk.ac.stfc.isis.ibex.ui.beamstatus.views;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import uk.ac.stfc.isis.ibex.beamstatus.FacilityPV;

/**
 * A class to bend PV labels on menu to FacilityPV objects.
 *
 */
public class BeamInfoComposite extends Composite {

	/**
	 * Constructor.
	 * 
	 * @param parent composite where menu is displayed
	 * @param style  the style of widget to construct
	 */
	public BeamInfoComposite(Composite parent, int style) {
		super(parent, style);
	}

	/**
	 * Method to bind label with FacilityPV object.
	 * 
	 * @param facilityPV pv address to bind data to
	 * @param label      menu item that needs to be binded to a facilityPV
	 * @param object
	 */
	protected void bindAndAddMenu(FacilityPV facilityPV, Label label, Control object) {
		DataBindingContext bindingContext = new DataBindingContext();
		BeamInfoMenu beamInfo = new BeamInfoMenu(facilityPV);
		label.setMenu(beamInfo.createContextMenu(object));
		bindingContext.bindValue(WidgetProperties.text().observe(label),
				BeanProperties.value("value").observe(facilityPV.updatedValue));

	}
}
