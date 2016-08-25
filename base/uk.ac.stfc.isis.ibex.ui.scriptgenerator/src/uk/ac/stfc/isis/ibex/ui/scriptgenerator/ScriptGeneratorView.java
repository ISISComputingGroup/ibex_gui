package uk.ac.stfc.isis.ibex.ui.scriptgenerator;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FillLayout;

import java.util.ArrayList;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;

import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorRow;
import uk.ac.stfc.isis.ibex.ui.scriptgenerator.ScriptGeneratorTable;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
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
		
		Composite topPanel = new Composite(parent, SWT.NONE);
		topPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		topPanel.setLayout(new GridLayout(3, false));
		
		Label lblSettingsTitle = new Label(topPanel, SWT.LEFT);
		lblSettingsTitle.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
		lblSettingsTitle.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1));
		lblSettingsTitle.setText(" Settings");
		
		CLabel lblSeparator = new CLabel(topPanel, SWT.NONE);
		//lblSeparator.setMargins(0, 0, 40, 0);
		
		Label lblEstimateTitle = new Label(topPanel, SWT.LEFT);
		lblEstimateTitle.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
		lblEstimateTitle.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false, 1, 1));
		lblEstimateTitle.setText(" Estimated Script Time");
		
		SettingsPanel settingsPanel = new SettingsPanel(topPanel, SWT.BORDER_SOLID);
		settingsPanel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 2));
		
		new Label(topPanel, SWT.NONE);
		
		EstimatePanel estimatePanel = new EstimatePanel(topPanel, SWT.NONE);
		estimatePanel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		
		new Label(topPanel, SWT.NONE);
		
		ButtonsPanel buttonsPanel = new ButtonsPanel(topPanel, SWT.NONE);
		buttonsPanel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		
		TablePanel tablePanel = new TablePanel(parent, SWT.NONE);
		tablePanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		// temporary colours
//		topPanel.setBackground(settingsPanel.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN));
//		settingsPanel.setBackground(settingsPanel.getDisplay().getSystemColor(SWT.COLOR_MAGENTA));
//		estimatePanel.setBackground(estimatePanel.getDisplay().getSystemColor(SWT.COLOR_BLUE)); 
//		buttonsPanel.setBackground(buttonsPanel.getDisplay().getSystemColor(SWT.COLOR_GREEN));  
	}
	
	@Override
	public void setFocus() {
		
	}

}