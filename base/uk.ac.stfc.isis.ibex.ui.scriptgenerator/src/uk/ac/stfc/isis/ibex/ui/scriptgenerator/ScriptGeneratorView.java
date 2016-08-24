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
	 
	public ScriptGeneratorView() {
		super();
	}

	@SuppressWarnings("unused")
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, true));
		
		// Top panel to hold three panels - settingsPanel, estimatePanel and buttonsPanel
		Composite topPanel = new Composite(parent, SWT.NONE);
		topPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		topPanel.setLayout(new GridLayout(2, false));
		
		SettingsPanel settingsPanel = new SettingsPanel(topPanel, SWT.NONE);
		settingsPanel.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, false, 1, 2));
		
		EstimatePanel estimatePanel = new EstimatePanel(topPanel, SWT.NONE);
		estimatePanel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1));
		
		ButtonsPanel buttonsPanel = new ButtonsPanel(topPanel, SWT.NONE);
		buttonsPanel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		TablePanel tablePanel = new TablePanel(parent, SWT.NONE);
		tablePanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		// temporary colours
		settingsPanel.setBackground(settingsPanel.getDisplay().getSystemColor(SWT.COLOR_MAGENTA));
		estimatePanel.setBackground(estimatePanel.getDisplay().getSystemColor(SWT.COLOR_BLUE)); 
		buttonsPanel.setBackground(buttonsPanel.getDisplay().getSystemColor(SWT.COLOR_GREEN)); 
		tablePanel.setBackground(tablePanel.getDisplay().getSystemColor(SWT.COLOR_YELLOW)); 
	}
	
	@Override
	public void setFocus() {
		
	}

}