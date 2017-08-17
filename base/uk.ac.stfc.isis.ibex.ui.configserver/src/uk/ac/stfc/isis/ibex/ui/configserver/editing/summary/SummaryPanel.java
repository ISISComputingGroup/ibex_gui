
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2015 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.ui.configserver.editing.summary;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.epics.observing.Observer;
import uk.ac.stfc.isis.ibex.synoptic.Synoptic;
import uk.ac.stfc.isis.ibex.synoptic.SynopticInfo;
import uk.ac.stfc.isis.ibex.validators.BlockServerNameValidator;
import uk.ac.stfc.isis.ibex.validators.MessageDisplayer;
import uk.ac.stfc.isis.ibex.validators.SummaryDescriptionValidator;

/**
 * The panel that holds general misc information about the configuration.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class SummaryPanel extends Composite {
	private Text txtName;
	private Text txtDescription;
    private Label lblDateCreated;
	private Label lblDateCreatedField;
    private Label lblDateModified;
	private Label lblDateModifiedField;
    private Label lblSynoptic;
	private ComboViewer cmboSynoptic;
	private EditableConfiguration config;
    private final MessageDisplayer messageDisplayer;

    /**
     * Constructor for the general information about the configuration.
     * 
     * @param parent
     *            The parent composite that this panel is a part of.
     * @param style
     *            An integer giving the panel style using SWT style flags.
     * @param dialog
     *            The message displayer used to show error messages to the user.
     */
    public SummaryPanel(Composite parent, int style, MessageDisplayer dialog) {
		super(parent, style);

        messageDisplayer = dialog;
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
        Composite cmpSummary = new Composite(this, SWT.NONE);
        cmpSummary.setLayout(new GridLayout(2, false));

        Label lblName = new Label(cmpSummary, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name:");
		
        txtName = new Text(cmpSummary, SWT.BORDER);
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
        Label lblDescription = new Label(cmpSummary, SWT.NONE);
		lblDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDescription.setText("Description:");
		
        txtDescription = new Text(cmpSummary, SWT.BORDER);
		txtDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtDescription.setTextLimit(39);
		
        lblSynoptic = new Label(cmpSummary, SWT.NONE);
		lblSynoptic.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSynoptic.setText("Synoptic:");
		
        cmboSynoptic = new ComboViewer(cmpSummary, SWT.READ_ONLY);
		cmboSynoptic.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		cmboSynoptic.setContentProvider(new ArrayContentProvider());
		
        lblDateCreated = new Label(cmpSummary, SWT.NONE);
		lblDateCreated.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDateCreated.setText("Date Created:");
		
        lblDateCreatedField = new Label(cmpSummary, SWT.NONE);
				
        lblDateModified = new Label(cmpSummary, SWT.NONE);
		lblDateModified.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDateModified.setText("Date Modified:");
		
        lblDateModifiedField = new Label(cmpSummary, SWT.NONE);
	}
	
    /**
     * Sets the configuration that the panel information relates to.
     * 
     * @param config
     *            The configuration that the panel relates to.
     */
	public void setConfig(EditableConfiguration config) {
		this.config = config;
		setBindings();
	}

	private void setBindings() {
        DataBindingContext bindingContext = new DataBindingContext();
		
		UpdateValueStrategy descValidator = new UpdateValueStrategy();
        // Set validator if not saving a new config
        if (!config.getIsNew()) {
            BlockServerNameValidator configDescriptionRules =
                Configurations.getInstance().variables().configDescriptionRules.getValue();
            descValidator
                    .setBeforeSetValidator(new SummaryDescriptionValidator(messageDisplayer, configDescriptionRules));
        }
		
        UpdateValueStrategy notConverter = new UpdateValueStrategy() {
            @Override
            public Object convert(Object value) {
                return !(Boolean) value;
            }
        };
        
        bindSynopticList();
        
        bindingContext.bindValue(WidgetProperties.enabled().observe(txtName),
                BeanProperties.value("isNew").observe(config));
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(txtName), BeanProperties.value("name").observe(config));
        bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(txtDescription),
                BeanProperties.value("description").observe(config), descValidator, null);
		bindingContext.bindValue(WidgetProperties.selection().observe(cmboSynoptic.getCombo()), BeanProperties.value("synoptic").observe(config));		
        bindingContext.bindValue(WidgetProperties.visible().observe(lblDateCreated),
                BeanProperties.value("isNew").observe(config), null, notConverter);
        bindingContext.bindValue(WidgetProperties.visible().observe(lblDateModified),
                BeanProperties.value("isNew").observe(config), null, notConverter);
		bindingContext.bindValue(WidgetProperties.text().observe(lblDateCreatedField), BeanProperties.value("dateCreated").observe(config));
		bindingContext.bindValue(WidgetProperties.text().observe(lblDateModifiedField), BeanProperties.value("dateModified").observe(config));

        bindingContext.bindValue(WidgetProperties.visible().observe(lblSynoptic),
                BeanProperties.value("isComponent").observe(config), null, notConverter);
        bindingContext.bindValue(WidgetProperties.visible().observe(cmboSynoptic.getCombo()),
                BeanProperties.value("isComponent").observe(config), null, notConverter);
                
	}
	
	/**
	 * Bind the default synoptic list to the synoptic names from the list.
	 * 
	 * This means as the list is updated new values will become available to the synoptic list
	 * This behaviour is incorrect because it also select the default synoptic for the current config. There is a
	 * ticket to fix this #2527.
	 */
	private void bindSynopticList() {
		Synoptic.getInstance().availableSynopticsInfo().addObserver(new Observer<Collection<SynopticInfo>>() {
			
			@Override
			public void update(final Collection<SynopticInfo> value, Exception error, boolean isConnected) {
				updateSynopticNamesInComboBox(value);				
			}
			
			@Override
			public void onValue(Collection<SynopticInfo> value) {
				updateSynopticNamesInComboBox(value);

			}

			/**
			 * Get the new list of synoptics and the current default and update the combo box.
			 * 
			 * @param value the new value of the synoptics list
			 */
			private void updateSynopticNamesInComboBox(Collection<SynopticInfo> value) {
				final String[] names = SynopticInfo.names(value).toArray(new String[0]);
				Arrays.sort(names);
				
				final String selected = getDefaultSelection(value);
				
				
				Display.getDefault().asyncExec(new Runnable() {
					
					@Override
					public void run() {
						if (!cmboSynoptic.getControl().isDisposed()) {
							cmboSynoptic.setInput(names);
							if (selected != null) {
								config.setSynoptic(selected);
							}
						}
					}
				});
			}

			/**
			 * Based on the synoptics list get the current default.
			 * 
			 * @param value the synoptics list
			 * @return the label; empty if there is no default
			 */
			private String getDefaultSelection(Collection<SynopticInfo> value) {
				if (value == null) {
					return null;
				}
				for (SynopticInfo info : value) {
					if (info.isDefault()) {
						return info.name();
					}
				}
				return null;
			}
			
			@Override
			public void onError(Exception e) {
				// keep list as is
				
			}
			
			@Override
			public void onConnectionStatus(boolean isConnected) {
				// keep list as is				
			}
		});
	}
}
