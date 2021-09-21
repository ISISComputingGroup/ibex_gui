package uk.ac.stfc.isis.ibex.ui.beamstatus.views;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import uk.ac.stfc.isis.ibex.beamstatus.FacilityPV;
/**
 * A class to bend PV labels on menu to FacilityPV objects 
 *
 */
public class BeamInfoComposite extends Composite {

	BeamInfoComposite(Composite parent, int style) {
		super(parent, style);
	}

	protected void bindAndAddMenu(FacilityPV facilityPV, Label label, Control object) {
		DataBindingContext bindingContext = new DataBindingContext();
		BeamInfoMenu beamInfo = new BeamInfoMenu(facilityPV);
		label.setMenu(beamInfo.createContextMenu(object));
		bindingContext.bindValue(WidgetProperties.text().observe(label),
				BeanProperties.value("value").observe(facilityPV.updatedValue));
	
	}
}
