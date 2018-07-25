
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

package uk.ac.stfc.isis.ibex.ui.scriptgenerator.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.annotation.PostConstruct;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.scriptgenerator.settings.ApertureSans;
import uk.ac.stfc.isis.ibex.scriptgenerator.settings.ApertureTrans;
import uk.ac.stfc.isis.ibex.scriptgenerator.settings.CollectionMode;
import uk.ac.stfc.isis.ibex.scriptgenerator.settings.Order;
import uk.ac.stfc.isis.ibex.scriptgenerator.settings.SampleGeometry;
import uk.ac.stfc.isis.ibex.scriptgenerator.settings.SansSettings;
import uk.ac.stfc.isis.ibex.validators.NumbersOnlyValidator;

/**
 * Provides settings to control the script generator.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class SettingsView {
    private ComboViewer comboOrder;
	private ComboViewer comboSampleGeometry;
	private Text txtDoSans;
	private Text txtSampleHeight;
	private Text txtDoTrans;
	private Text txtSampleWidth;
	private Button btnLoopOver;
	private ComboViewer comboCollectionMode;
	private ComboViewer comboApertureSans;
	private ComboViewer comboApertureTrans;
	
	/**
	 * The default constructor.
	 */
	public SettingsView() {
	}

	@PostConstruct
	public void createPartControl(Composite parent) {
		
		SansSettings settings = new SansSettings(1, 1, 7, 7, Order.TRANS, false, ApertureSans.MEDIUM, ApertureTrans.MEDIUM, SampleGeometry.DISC, CollectionMode.HISTOGRAM);
		
		parent.setLayout(new GridLayout(2, true));
		parent.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 2));
		
		Group grpSettings = new Group(parent, SWT.NULL);
		grpSettings.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		GridLayout glGrpSettings = new GridLayout(7, false);
		grpSettings.setLayout(glGrpSettings);
		glGrpSettings.horizontalSpacing = 10;
		glGrpSettings.verticalSpacing = 6;

		Label lblOrder = new Label(grpSettings, SWT.RIGHT);
		lblOrder.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblOrder.setText("Order:");
		lblOrder.setToolTipText("The order of the SANS and TRANS operations");
		
		comboOrder = new ComboViewer(grpSettings, SWT.NONE | SWT.READ_ONLY);
		comboOrder.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		comboOrder.setContentProvider(new ArrayContentProvider());
		comboOrder.setInput(Order.values());	
		ISelection orderSelection = new StructuredSelection(Order.TRANS);
		comboOrder.setSelection(orderSelection);
		
		CLabel lblSeparator = new CLabel(grpSettings, SWT.CENTER);
		lblSeparator.setMargins(100, 0, 0, 0);
		
		Label lblSampleGeometry = new Label(grpSettings, SWT.RIGHT);
		lblSampleGeometry.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSampleGeometry.setText("Sample Geometry:");
		lblSampleGeometry.setToolTipText("Where the sample is placed");
		
		comboSampleGeometry = new ComboViewer(grpSettings, SWT.NONE | SWT.READ_ONLY);
		comboSampleGeometry.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		comboSampleGeometry.setContentProvider(new ArrayContentProvider());
		comboSampleGeometry.setInput(SampleGeometry.values());
		ISelection selectionGeometrySelection = new StructuredSelection(SampleGeometry.DISC);
		comboSampleGeometry.setSelection(selectionGeometrySelection);
		
		Label lblDoSans = new Label(grpSettings, SWT.RIGHT);
		lblDoSans.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1));
		lblDoSans.setText("Do SANS:");
		
		txtDoSans = new Text(grpSettings, SWT.BORDER);
		GridData gdTxtDoSans = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		txtDoSans.setLayoutData(gdTxtDoSans);
		gdTxtDoSans.widthHint = 30;
		txtDoSans.setText("1");
		txtDoSans.setToolTipText("The number of times to collect all SANS data");
		
		Label lblDoSansTimes = new Label(grpSettings, SWT.LEFT);
		lblDoSansTimes.setAlignment(SWT.LEFT);
		lblDoSansTimes.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		lblDoSansTimes.setText("time(s)");
		
		new Label(grpSettings, SWT.CENTER);
		
		Label lblSampleHeight = new Label(grpSettings, SWT.RIGHT);
		lblSampleHeight.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1));
		lblSampleHeight.setText("Sample Height:");
		txtSampleHeight = new Text(grpSettings, SWT.BORDER);

		GridData gdTxtSampleHeight = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		txtSampleHeight.setLayoutData(gdTxtSampleHeight);
		gdTxtSampleHeight.widthHint = 30;
		txtSampleHeight.setText("7");
		txtSampleHeight.setToolTipText("The sample's height");
		
		Label lblSampleHeightUnits = new Label(grpSettings, SWT.RIGHT);
		lblSampleHeightUnits.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		lblSampleHeightUnits.setText("mm");
		
		Label lblDoTrans = new Label(grpSettings, SWT.RIGHT);
		lblDoTrans.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1));
		lblDoTrans.setText("Do TRANS:");
		
		txtDoTrans = new Text(grpSettings, SWT.BORDER);
		GridData gdTxtDoTrans = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		txtDoTrans.setLayoutData(gdTxtDoTrans);
		gdTxtDoTrans.widthHint = 30;
		txtDoTrans.setText("1");
		txtDoTrans.setToolTipText("The number of times to collect all TRANS data");
		
		Label lblDoTransTimes = new Label(grpSettings, SWT.LEFT);
		lblDoTransTimes.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		lblDoTransTimes.setText("time(s)");
		
		new Label(grpSettings, SWT.CENTER);
		
		Label lblSampleWidth = new Label(grpSettings, SWT.RIGHT);
		lblSampleWidth.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1));
		lblSampleWidth.setText("Sample Width:");
		
		txtSampleWidth = new Text(grpSettings, SWT.BORDER);
		GridData gdTxtSampleWidth = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		txtSampleWidth.setLayoutData(gdTxtSampleWidth);
		gdTxtSampleWidth.widthHint = 30;
		txtSampleWidth.setText("7");
		txtSampleWidth.setToolTipText("The sample's width");
        txtSampleWidth.setEnabled(false);
		
		Label lblSampleWidthUnits = new Label(grpSettings, SWT.RIGHT);
		lblSampleWidthUnits.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		lblSampleWidthUnits.setText("mm");
		
		new Label(grpSettings, SWT.CENTER);
		
		btnLoopOver = new Button(grpSettings, SWT.CHECK);
		btnLoopOver.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 2, 1));
		btnLoopOver.setText("Loop over each run?");
		
		new Label(grpSettings, SWT.CENTER);
		
		Label lblCollectionMode = new Label(grpSettings, SWT.RIGHT);
		lblCollectionMode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCollectionMode.setText("Collection Mode:");
		lblCollectionMode.setToolTipText("The method used to record data");
		
		comboCollectionMode = new ComboViewer(grpSettings, SWT.NONE | SWT.READ_ONLY);
		comboCollectionMode.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		comboCollectionMode.setContentProvider(new ArrayContentProvider());
		comboCollectionMode.setInput(CollectionMode.values());	
		ISelection selectionCollectionMode = new StructuredSelection(CollectionMode.HISTOGRAM);
		comboCollectionMode.setSelection(selectionCollectionMode);
		new Label(grpSettings, SWT.NONE);
		
		Composite separator = new Composite(grpSettings, SWT.NONE);
		GridData gdSeparator = new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1);
		gdSeparator.heightHint = 5;
		separator.setLayoutData(gdSeparator);
		
		new Label(grpSettings, SWT.CENTER);

		Label lblApertureSettings = new Label(grpSettings, SWT.RIGHT);
		lblApertureSettings.setText("A1, S1 Setting:");

		comboApertureSans = new ComboViewer(grpSettings, SWT.NONE | SWT.READ_ONLY);
		comboApertureSans.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		comboApertureSans.setContentProvider(new ArrayContentProvider());
		comboApertureSans.setInput(ApertureSans.values());	
		ISelection selectionApertureSans = new StructuredSelection(ApertureSans.MEDIUM);
		comboApertureSans.setSelection(selectionApertureSans);
		
		Label lblApertureSans = new Label(grpSettings, SWT.RIGHT);
		lblApertureSans.setText("SANS");
		lblApertureSans.setToolTipText("SANS aperture size");
		
		new Label(grpSettings, SWT.CENTER);
		new Label(grpSettings, SWT.CENTER);
		new Label(grpSettings, SWT.CENTER);
		new Label(grpSettings, SWT.NONE);
			
		comboApertureTrans = new ComboViewer(grpSettings, SWT.NONE | SWT.READ_ONLY);
		comboApertureTrans.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		comboApertureTrans.setContentProvider(new ArrayContentProvider());
		comboApertureTrans.setInput(ApertureTrans.values());	
		ISelection selectionApertureTrans = new StructuredSelection(ApertureTrans.MEDIUM);
		comboApertureTrans.setSelection(selectionApertureTrans);
		
		Label lblApertureTrans = new Label(grpSettings, SWT.RIGHT);
		lblApertureTrans.setText("TRANS");
		lblApertureTrans.setToolTipText("TRANS aperture size");
		
		new Label(grpSettings, SWT.NONE);
		new Label(grpSettings, SWT.NONE);
		new Label(grpSettings, SWT.NONE);
		new Label(grpSettings, SWT.NONE);
		
		bind(settings);
	}
	

	/**
     * The databinding of all settings between SettingsPanel and Settings.
     * 
     * @param settings the settings to be displayed
     */
    public void bind(final SansSettings settings) {
        final DataBindingContext bindingContext = new DataBindingContext();
		IValidator validator = new NumbersOnlyValidator();
		
		UpdateValueStrategy strategy = new UpdateValueStrategy();
		strategy.setAfterGetValidator(validator);
		
        IObservableValue targetOrder = ViewersObservables.observeSingleSelection(comboOrder);
        IObservableValue modelOrder = BeanProperties.value("order").observe(settings);
        bindingContext.bindValue(targetOrder, modelOrder);
		
        IObservableValue targetDoSans = WidgetProperties.text(SWT.Modify).observe(txtDoSans);
        IObservableValue modelDoSans = BeanProperties.value("doSans").observe(settings);
        Binding bindValueDoSans = bindingContext.bindValue(targetDoSans, modelDoSans, strategy, null);
        
        ControlDecorationSupport.create(bindValueDoSans, SWT.TOP | SWT.RIGHT);
        
        IObservableValue targetDoTrans = WidgetProperties.text(SWT.Modify).observe(txtDoTrans);
        IObservableValue modelDoTrans = BeanProperties.value("doTrans").observe(settings);
        Binding bindValueDoTrans = bindingContext.bindValue(targetDoTrans, modelDoTrans, strategy, null);
        
        ControlDecorationSupport.create(bindValueDoTrans, SWT.TOP | SWT.RIGHT);
		
        IObservableValue targetLoopOver = WidgetProperties.selection().observe(btnLoopOver);
        IObservableValue modelLoopOver = BeanProperties.value("loopOver").observe(settings);
        bindingContext.bindValue(targetLoopOver, modelLoopOver);
        
        IObservableValue targetApertureSans = ViewersObservables.observeSingleSelection(comboApertureSans);
        IObservableValue modelApertureSans = BeanProperties.value("sansSize").observe(settings);
        bindingContext.bindValue(targetApertureSans, modelApertureSans);
        
        IObservableValue targetApertureTrans = ViewersObservables.observeSingleSelection(comboApertureTrans);
        IObservableValue modelApertureTrans = BeanProperties.value("transSize").observe(settings);
        bindingContext.bindValue(targetApertureTrans, modelApertureTrans);
        
        IObservableValue targetGeometry = ViewersObservables.observeSingleSelection(comboSampleGeometry);
        IObservableValue modelGeometry = BeanProperties.value("geometry").observe(settings);
        bindingContext.bindValue(targetGeometry, modelGeometry);
        
        IObservableValue targetSampleHeight = WidgetProperties.text(SWT.Modify).observe(txtSampleHeight);
        IObservableValue modelSampleHeight = BeanProperties.value("sampleHeight").observe(settings);
        Binding bindValueSampleHeight = bindingContext.bindValue(targetSampleHeight, modelSampleHeight, strategy, null);
        
        ControlDecorationSupport.create(bindValueSampleHeight, SWT.TOP | SWT.RIGHT);
        
        IObservableValue targetSampleWidth = WidgetProperties.text(SWT.Modify).observe(txtSampleWidth);
        IObservableValue modelSampleWidth = BeanProperties.value("sampleWidth").observe(settings);
        Binding bindValueSampleWidth = bindingContext.bindValue(targetSampleWidth, modelSampleWidth, strategy, null);
        
        ControlDecorationSupport.create(bindValueSampleWidth, SWT.TOP | SWT.RIGHT);
        
        IObservableValue targetCollection = ViewersObservables.observeSingleSelection(comboCollectionMode);
        IObservableValue modelCollection = BeanProperties.value("collection").observe(settings);
        bindingContext.bindValue(targetCollection, modelCollection);

        settings.addPropertyChangeListener("geometry", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                txtSampleWidth.setEnabled(true);
                bindingContext.bindValue(WidgetProperties.text().observe(txtSampleWidth),
                        BeanProperties.value("sampleWidth").observe(settings));
                if (settings.getGeometry() == SampleGeometry.DISC) {
                    txtSampleWidth.setEnabled(false);
                    bindingContext.bindValue(WidgetProperties.text().observe(txtSampleWidth),
                            BeanProperties.value("sampleHeight").observe(settings));
                }
            }
        });
	}

}