
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
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.epics.observing.Observer;
import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.synoptic.Synoptic;
import uk.ac.stfc.isis.ibex.synoptic.SynopticInfo;
import uk.ac.stfc.isis.ibex.ui.UIUtils;
import uk.ac.stfc.isis.ibex.ui.widgets.buttons.IBEXButton;
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
    private Button protectedCheckBox;
    private Label protectLabel;

    private Button dynamicCheckBox;
    private Label dynamicLabel;
    
    private Label warning;
    private Observer<Collection<SynopticInfo>> synopticInfoObserver;
    private Subscription synopticSubscription;
    
    private static final String PROTECT_TOOLTIP = "Mark this configuration as protected to disable editing when manager mode is not active.";
    private static final String DYNAMIC_TOOLTIP = "Mark this configuration as dynamic, which allows it to be automatically edited in response to external events.\n\n"
    		+ "NOTE: this is advanced functionality and also requires other IBEX components to be set up in order to work. If you think you need this functionality "
    		+ "on your beamline, please contact the IBEX team for assistance.";

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

        lblSynoptic = new Label(cmpSummary, SWT.NONE);
        lblSynoptic.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblSynoptic.setText("Synoptic:");

        cmboSynoptic = new ComboViewer(cmpSummary, SWT.READ_ONLY);
        cmboSynoptic.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        cmboSynoptic.setContentProvider(new ArrayContentProvider());
        
        Composite optionsContainer = new Composite(cmpSummary, SWT.NONE);
        optionsContainer.setLayout(new GridLayout(4, false));
        optionsContainer.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 2, 1));
        
        
        
        protectLabel = new Label(optionsContainer,  SWT.NONE);
        protectLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.NONE, false, false, 1, 1));
        protectLabel.setText("Protected:");
        protectLabel.setToolTipText(PROTECT_TOOLTIP);
        
        protectedCheckBox = new IBEXButton(optionsContainer, SWT.CHECK)
        		.layoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1))
				.tooltip(PROTECT_TOOLTIP)
				.get();
        
        dynamicLabel = new Label(optionsContainer,  SWT.NONE);
        dynamicLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.NONE, false, false, 1, 1));
        dynamicLabel.setText("Dynamic:");
        dynamicLabel.setToolTipText(DYNAMIC_TOOLTIP);
        
        
        dynamicCheckBox = new IBEXButton(optionsContainer, SWT.CHECK)
        		.layoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1))
				.tooltip(DYNAMIC_TOOLTIP)
				.get();

        lblDateCreated = new Label(cmpSummary, SWT.NONE);
        lblDateCreated.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblDateCreated.setText("Date Created:");

        lblDateCreatedField = new Label(cmpSummary, SWT.NONE);
        
        lblDateModified = new Label(cmpSummary, SWT.NONE);
        lblDateModified.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblDateModified.setText("Date Modified:");

        lblDateModifiedField = new Label(cmpSummary, SWT.NONE);
        
        warning = new Label(cmpSummary, SWT.WRAP | SWT.NONE);
        GridData gd = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
        gd.widthHint = 800;
        warning.setLayoutData(gd);
        warning.setText("");
        warning.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_RED));

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

	UpdateValueStrategy<String, String> descValidator = new UpdateValueStrategy<>();
        // Set validator if not saving a new config
        if (!config.getIsNew()) {
        	 BlockServerNameValidator configDescriptionRules = Configurations.getInstance()
                     .variables().configDescriptionRules.getValue();
	    descValidator.setBeforeSetValidator(new SummaryDescriptionValidator(messageDisplayer, configDescriptionRules));
        }
        
        bindSynopticList();

        bindingContext.bindValue(WidgetProperties.enabled().observe(txtName),
                BeanProperties.value("isNew").observe(config));
        bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(txtName),
                BeanProperties.value("name").observe(config));
        bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(txtDescription),
                BeanProperties.value("description").observe(config), descValidator, null);
	bindingContext.bindValue(WidgetProperties.comboSelection().observe(cmboSynoptic.getCombo()),
                BeanProperties.value("synoptic").observe(config));
        bindingContext.bindValue(WidgetProperties.visible().observe(lblDateCreated),
		BeanProperties.value("isNew", Boolean.class).observe(config), null, UIUtils.NOT_CONVERTER);
        bindingContext.bindValue(WidgetProperties.visible().observe(lblDateModified),
		BeanProperties.value("isNew", Boolean.class).observe(config), null, UIUtils.NOT_CONVERTER);
        bindingContext.bindValue(WidgetProperties.text().observe(lblDateCreatedField),
                BeanProperties.value("dateCreated").observe(config));
        bindingContext.bindValue(WidgetProperties.text().observe(lblDateModifiedField),
                BeanProperties.value("dateModified").observe(config));
	bindingContext.bindValue(WidgetProperties.buttonSelection().observe(protectedCheckBox),
                BeanProperties.value("isProtected").observe(config));

	bindingContext.bindValue(WidgetProperties.buttonSelection().observe(dynamicCheckBox),
                BeanProperties.value("isDynamic").observe(config));

        bindingContext.bindValue(WidgetProperties.visible().observe(lblSynoptic),
		BeanProperties.value("isComponent", Boolean.class).observe(config), null, UIUtils.NOT_CONVERTER);
        
        bindingContext.bindValue(WidgetProperties.visible().observe(dynamicLabel),
        		BeanProperties.value("isComponent", Boolean.class).observe(config));
        bindingContext.bindValue(WidgetProperties.visible().observe(dynamicCheckBox),
        		BeanProperties.value("isComponent", Boolean.class).observe(config));
        
        bindingContext.bindValue(WidgetProperties.visible().observe(cmboSynoptic.getCombo()),
		BeanProperties.value("isComponent", Boolean.class).observe(config), null, UIUtils.NOT_CONVERTER);
        bindingContext.bindValue(WidgetProperties.text().observe(warning),
                BeanProperties.value("errorMessage").observe(config));


    }

    /**
     * Bind the default synoptic list to the synoptic names from the list.
     * 
     * This means as the list is updated new values will become available to the
     * synoptic list This behaviour is incorrect because it also select the
     * default synoptic for the current config. There is a ticket to fix this
     * #2527.
     */
    private void bindSynopticList() {
	synopticInfoObserver = new Observer<Collection<SynopticInfo>>() {

            @Override
            public void update(final Collection<SynopticInfo> value, Exception error, boolean isConnected) {
            	Display.getDefault().asyncExec(() -> updateSynopticNamesInComboBox(value));
            }

            @Override
            public void onValue(Collection<SynopticInfo> value) {
            	// The event comes from PV manager's threadpool but the method
            	// needs to manipulate UI elements so need to explicitly run on UI thread.
                Display.getDefault().asyncExec(() -> updateSynopticNamesInComboBox(value));
            }

            /**
             * Get the new list of synoptics and the current default and update
             * the combo box.
             * 
             * @param value
             *            the new value of the synoptics list
             */
            private void updateSynopticNamesInComboBox(Collection<SynopticInfo> value) {
                final String[] names = SynopticInfo.names(value).toArray(new String[0]);
                Arrays.sort(names);

                final String selected = getDefaultSelection(value);
                if (!cmboSynoptic.getControl().isDisposed()) {
                    cmboSynoptic.setInput(names);
                    if (selected != null) {
                        cmboSynoptic.setSelection(new StructuredSelection(selected));
                        config.setSynoptic(selected);
                    }
                }
            }

            /**
             * Based on the synoptics list get the current default.
             * 
             * @param value
             *            the synoptics list
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
	};
	synopticSubscription = Synoptic.getInstance().availableSynopticsInfo().subscribe(synopticInfoObserver);
	
	this.addDisposeListener(new DisposeListener() {
	    
	    @Override
	    public void widgetDisposed(DisposeEvent e) {
		
		synopticSubscription.cancelSubscription();
	    }
        });
    }
}
