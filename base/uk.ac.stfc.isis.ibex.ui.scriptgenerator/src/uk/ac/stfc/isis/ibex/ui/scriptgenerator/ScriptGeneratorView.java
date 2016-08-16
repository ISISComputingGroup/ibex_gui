package uk.ac.stfc.isis.ibex.ui.scriptgenerator;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;

import java.util.ArrayList;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;

import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorRow;
import uk.ac.stfc.isis.ibex.ui.scriptgenerator.ScriptGeneratorTable;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;

public class ScriptGeneratorView extends ViewPart {
	public static final String ID = "uk.ac.stfc.isis.ibex.ui.scriptgenerator.scriptgeneratorview";
	private ScriptGeneratorTable table;
	private Text text;
	private ArrayList<ScriptGeneratorRow> list;
	 
	public ScriptGeneratorView() {
		super();
	}

	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(10, true));
		
		list = new ArrayList<ScriptGeneratorRow>();
		list.add(new ScriptGeneratorRow("", 0, false));
		ScriptGeneratorTable table = new ScriptGeneratorTable(parent, SWT.NONE, SWT.MULTI | SWT.NO_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER, false);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 10, 1));
		table.setRows(list);
	
		Button btnPreview = new Button(parent, SWT.NONE);
		btnPreview.setText("Preview");
		btnPreview.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false, 1, 1));
		
		text = new Text(parent, SWT.BORDER);
		text.setEditable(false);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 9, 1));

		bind();
	}

	public void bind() {
		DataBindingContext bindingContext = new DataBindingContext();
        bindingContext.bindValue(WidgetProperties.text().observe(text), BeanProperties.value("script").observe(list.get(0)));
	}
	
	@Override
	public void setFocus() {
		
	}

}
