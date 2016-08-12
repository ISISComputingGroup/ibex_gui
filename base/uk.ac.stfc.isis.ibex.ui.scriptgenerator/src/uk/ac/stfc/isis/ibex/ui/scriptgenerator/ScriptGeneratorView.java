package uk.ac.stfc.isis.ibex.ui.scriptgenerator;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.SWT;
import uk.ac.stfc.isis.ibex.ui.scriptgenerator.ScriptGeneratorTable;

public class ScriptGeneratorView extends ViewPart {
	public static final String ID = "uk.ac.stfc.isis.ibex.ui.scriptgenerator.scriptgeneratorview";
	private ScriptGeneratorTable table;
	 
	public ScriptGeneratorView() {
		super();
	}

	public void createPartControl(Composite parent) {
		table = new ScriptGeneratorTable(parent, SWT.NONE, SWT.MULTI | SWT.NO_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER, false);
	}

	@Override
	public void setFocus() {
		
	}

}
