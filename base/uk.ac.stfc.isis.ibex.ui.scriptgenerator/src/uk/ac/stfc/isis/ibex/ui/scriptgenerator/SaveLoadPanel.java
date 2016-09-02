package uk.ac.stfc.isis.ibex.ui.scriptgenerator;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
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

public class SaveLoadPanel extends Composite {
	public SaveLoadPanel(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		Composite saveLoad = new Composite(this, SWT.NONE);
		saveLoad.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		GridLayout glSaveLoad = new GridLayout(1, false);
		saveLoad.setLayout(glSaveLoad);
		glSaveLoad.horizontalSpacing = 10;
		glSaveLoad.verticalSpacing = 6;
		
		Button btnSaveCsv = new Button(saveLoad, SWT.NONE);
		btnSaveCsv.setText("Save as CSV");
		GridData gdButtonSaveCsv = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gdButtonSaveCsv.minimumWidth = 120;
		btnSaveCsv.setLayoutData(gdButtonSaveCsv);
		
		Button btnLoadExcel = new Button(saveLoad, SWT.NONE);
		btnLoadExcel.setText("Load Excel File");
		GridData gdButtonLoadExcel = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gdButtonLoadExcel.minimumWidth = 120;
		btnLoadExcel.setLayoutData(gdButtonLoadExcel);
		
		Composite separator = new Composite(saveLoad, SWT.NONE);
		GridData gdSeparator = new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1);
		gdSeparator.heightHint = 5;
		separator.setLayoutData(gdSeparator);
		
		Button btnExportExcel = new Button(saveLoad, SWT.NONE);
		btnExportExcel.setText("Export Table to Excel");
		GridData gdButtonExportExcel = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gdButtonExportExcel.minimumWidth = 120;
		btnExportExcel.setLayoutData(gdButtonExportExcel);
		
	}

}
