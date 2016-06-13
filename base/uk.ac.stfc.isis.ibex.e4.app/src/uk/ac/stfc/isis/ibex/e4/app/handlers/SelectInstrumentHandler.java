package uk.ac.stfc.isis.ibex.e4.app.handlers;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;

public class SelectInstrumentHandler {
	
	@CanExecute
	public boolean canExecute(EPartService partService) {
		return true;
	}

	@Execute
	public void execute(EPartService partService) {
		System.out.println("Select Instrument Called");
		
		Instrument instrument = Instrument.getInstance();
		
		InstrumentInfo info = new InstrumentInfo("DEMO");
		
		instrument.setInstrument(info);
		
	}
}
