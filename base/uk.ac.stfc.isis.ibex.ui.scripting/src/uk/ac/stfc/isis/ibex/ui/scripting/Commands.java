package uk.ac.stfc.isis.ibex.ui.scripting;

import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;

public class Commands {

	private static final String SET_INSTRUMENT = "set_instrument('%s')\n";
	
	public final static String GENIE_INITIALISATION = 
			"# Configuring GENIE PYTHON, please wait\n" +
			"import sys;sys.executable=''\n" +
			"from genie_python.genie_startup import * \n" +
			"load_script(None, globals()) \n";
	
	public static String setInstrument() {
		InstrumentInfo info = Instrument.getInstance().currentInstrument();
		String instrumentName = info.hostName().equals("localhost") ? info.pvPrefix() : info.hostName();
		
		return String.format(SET_INSTRUMENT, instrumentName);
	}
}
