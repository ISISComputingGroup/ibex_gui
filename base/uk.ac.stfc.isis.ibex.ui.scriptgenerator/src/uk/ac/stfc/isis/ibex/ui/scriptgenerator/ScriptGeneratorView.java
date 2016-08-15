package uk.ac.stfc.isis.ibex.ui.scriptgenerator;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;

import java.util.ArrayList;

import org.eclipse.swt.SWT;

import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorRow;
import uk.ac.stfc.isis.ibex.ui.scriptgenerator.ScriptGeneratorTable;

public class ScriptGeneratorView extends ViewPart {
	public static final String ID = "uk.ac.stfc.isis.ibex.ui.scriptgenerator.scriptgeneratorview";
	private ScriptGeneratorTable table;
	 
	public ScriptGeneratorView() {
		super();
	}

	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		ScriptGeneratorTable table = new ScriptGeneratorTable(parent, SWT.NONE, SWT.MULTI | SWT.NO_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER, false);
		ArrayList<ScriptGeneratorRow> list = new ArrayList<ScriptGeneratorRow>();
		list.add(new ScriptGeneratorRow("", 0, false));
		table.setRows(list);
	}

	@Override
	public void setFocus() {
		
	}

}
