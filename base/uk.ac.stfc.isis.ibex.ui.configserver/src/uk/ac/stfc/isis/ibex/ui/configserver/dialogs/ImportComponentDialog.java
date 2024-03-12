/*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2023 Science & Technology Facilities Council.
 * All rights reserved.
 *
 * This program is distributed in the hope that it will be useful.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution.
 * EXCEPT AS EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM 
 * AND ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES 
 * OR CONDITIONS OF ANY KIND.  See the Eclipse Public License v1.0 for more details.
 *
 * You should have received a copy of the Eclipse Public License v1.0
 * along with this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or 
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.ui.configserver.dialogs;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.stream.Collectors;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.ui.LinkWrapper;
import uk.ac.stfc.isis.ibex.ui.configserver.ImportVariables;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.components.ComponentTable;
import uk.ac.stfc.isis.ibex.ui.mainmenu.instrument.InstrumentTable;
import uk.ac.stfc.isis.ibex.ui.widgets.buttons.IBEXButtonBuilder;

/**
 * Dialog for importing a component.
 */
public class ImportComponentDialog extends TitleAreaDialog  {
	
	private static final int WIDTH = 800;
	private static final int HEIGHT = 800;
	private static final String HELP_LINK = "https://shadow.nd.rl.ac.uk/ibex_user_manual/Create-and-Manage-Components#import-a-component";
	
	private final String title;
	private final String subTitle;
	private InstrumentTable instrumentTable;
	private ComponentTable componentTable;
	private Button importButton;
	
	private ImportVariables importVariables;
	private Configuration selectedComponent = null;

	
	/**
     * Constructor.
     * 
     * @param parentShell 		parent shell to run dialogue
	 * @param title         	title of dialogue
	 * @param subTitle      	action being taken
	 * @param importVariables	variables used for importing
     */
	public ImportComponentDialog(Shell parentShell, String title, String subTitle, ImportVariables importVariables) {
		super(parentShell);
		this.title = title;
		this.subTitle = subTitle;
		this.importVariables = importVariables;
	}
	
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(title);
	}
	
	@Override
	protected Point getInitialSize() {
        return new Point(WIDTH, HEIGHT);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
        setTitle(subTitle);
		Composite container = new Composite(parent, SWT.FILL);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		container.setLayout(new GridLayout(2, true));
		
		LinkWrapper link = new LinkWrapper(container, "Help", HELP_LINK);
	    link.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1));
	    
		createIntrumentGroup(container);
		createComponentGroup(container);
		createCustomGroup(container);
		
		importVariables.addUiThreadPropertyChangeListener("components", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				componentTable.setComponents(importVariables.getComponents());
			}
		});
		
		importVariables.addUiThreadPropertyChangeListener("status", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				componentTable.enabled(importVariables.connected() && importVariables.versionMatch());
				setErrorMessage(importVariables.getErrorMessage());
			}
		});
		
		return container;
	}
	
	@Override
    protected void createButtonsForButtonBar(Composite parent) {
        importButton = createButton(parent, IDialogConstants.OK_ID, "Import", true);
        createButton(parent, IDialogConstants.CANCEL_ID, "Cancel", false);
        importButton.setEnabled(false);
    }
	
	private void createIntrumentGroup(Composite parent) {
		Group group = new Group(parent, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		group.setText("Instruments");
		group.setLayout(new GridLayout(2, false));
		
		Label label = new Label(group, SWT.RIGHT);
		label.setText("Instrument:");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		
		Text searchText = new Text(group, SWT.SEARCH);
		searchText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		searchText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				instrumentTable.setSearch(searchText.getText());
			}
		});

		var currentInstrumentName = Instrument.getInstance().name().getValue();
		var instruments = Instrument.getInstance().getInstruments().stream().filter(i -> !i.name().equals(currentInstrumentName)).collect(Collectors.toList());
		instrumentTable = new InstrumentTable(group, SWT.NONE, SWT.FULL_SELECTION, instruments);
        instrumentTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
        instrumentTable.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                if (selection.size() > 0) {
                    InstrumentInfo instrument = (InstrumentInfo) selection.getFirstElement();
                    importVariables.selectInstrument(instrument.pvPrefix());
                }
            }
        });
	}
	
	private void createComponentGroup(Composite parent) {
		Group group = new Group(parent, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		group.setText("Components");
		group.setLayout(new GridLayout(2, false));
		
		Label label = new Label(group, SWT.RIGHT);
		label.setText("Component:");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		
		Text searchText = new Text(group, SWT.SEARCH);
		searchText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		searchText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				componentTable.setSearch(searchText.getText());
			}
		});

		componentTable = new ComponentTable(group, SWT.NONE, SWT.FULL_SELECTION);
        componentTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
        componentTable.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                if (selection.size() > 0) {
                	selectedComponent = (Configuration) selection.getFirstElement();
					importButton.setEnabled(true);
                } else {
					importButton.setEnabled(false);
				}
            }
        });
	}
	
	private void createCustomGroup(Composite parent) {
		final int columns = 3;
		Group group = new Group(parent, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
		group.setText("Custom Instrument Selection");
		group.setLayout(new GridLayout(columns, false));
		
		String warningString = "Configure a PV prefix for an unknown instrument. (e.g. \"IN:NAME:\")";
        Label warning = new Label(group, SWT.LEFT);
        warning.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, columns, 1));
        warning.setText(warningString);
		
		Label label = new Label(group, SWT.RIGHT);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		label.setText("PV Prefix:");
		
		Text text = new Text(group, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button button = new IBEXButtonBuilder(group, SWT.NONE)
			.setText("Select")
			.setListener(new Listener() {
					@Override
					public void handleEvent(Event event) {
						importVariables.selectInstrument(text.getText());
					}
				}).setCustomLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1)).build();

		
		
		
	}
	
	/**
	 * @return The currently selected component.
	 */
	public Configuration getSelectedComponent() {
		return selectedComponent;
	}
}
