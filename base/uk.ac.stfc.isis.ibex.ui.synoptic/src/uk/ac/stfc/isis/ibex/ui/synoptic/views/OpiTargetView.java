package uk.ac.stfc.isis.ibex.ui.synoptic.views;

import java.util.Map;

import org.csstudio.opibuilder.util.MacrosInput;
import org.eclipse.core.runtime.Path;

import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.opis.Opi;
import uk.ac.stfc.isis.ibex.ui.OPIView;

public final class OpiTargetView extends OPIView {

	public static final String ID = "uk.ac.stfc.isis.ibex.ui.synoptic.views.OpiTargetView"; //$NON-NLS-1$

	private final String pvPrefix = Instrument.getInstance().currentInstrument().pvPrefix();
	
	private String opiName;
	
	public void setOpi(String title, String opiName, Map<String, String> macros) {
		this.opiName = opiName;
		
		macros.put("NAME", title);
		addMacros(macros);	
		initialiseOPI();
	}
		
	@Override
	protected Path opi() {
		return Opi.getDefault().provider().pathFromName(opiName);
	}
	
	private void addMacros(Map<String, String> macros) {
		MacrosInput input = macros();
		input.put("P", pvPrefix);
		for (Map.Entry<String, String> macro : macros.entrySet()) {
			input.put(macro.getKey(), macro.getValue());
		}
	}
}
