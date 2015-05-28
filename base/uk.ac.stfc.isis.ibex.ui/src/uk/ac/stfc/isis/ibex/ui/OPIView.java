package uk.ac.stfc.isis.ibex.ui;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashMap;

import org.apache.logging.log4j.Logger;
import org.csstudio.opibuilder.runmode.RunnerInput;
import org.csstudio.opibuilder.util.MacrosInput;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.PartInitException;

import uk.ac.stfc.isis.ibex.logger.IsisLog;

public abstract class OPIView extends org.csstudio.opibuilder.runmode.OPIView {
	
	private static final Logger LOG = IsisLog.getLogger(OPIView.class);
	
	private MacrosInput macros = new MacrosInput(new LinkedHashMap<String, String>(), false);
	
	public void initialiseOPI() {
		try {
			final RunnerInput input = new RunnerInput(opi(), null, macros);
			setOPIInput(input);
		} catch (PartInitException e) {
			LOG.catching(e);
		}
	}
	
	protected MacrosInput macros() {
		return macros;
	}
	
	protected abstract Path opi();
	
	protected final Path pathToFileResource(String relativePath) {	
		Path path = null;
		try {
			URL url = getClass().getResource(relativePath);
			String filePath = FileLocator.resolve(url).getPath();			
			path = new Path(filePath);
		} catch (IOException e) {
			LOG.catching(e);
		}
		
		return path;
	}
}
