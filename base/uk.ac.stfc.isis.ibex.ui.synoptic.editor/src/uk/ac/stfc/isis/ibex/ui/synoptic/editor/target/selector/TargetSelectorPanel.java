 /*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2016 Science & Technology Facilities Council.
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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.ui.synoptic.editor.target.selector;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import uk.ac.stfc.isis.ibex.ui.Utils;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.component.ComponentDetailViewModel;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.SynopticViewModel;

/**
 * The target selector panel.
 */
public class TargetSelectorPanel extends Composite {

    private Map<String, List<String>> availableOpis;
    private final TargetSelectorViewModel viewModel;
    private Text txtName;
    private final ComponentDetailViewModel compDetailsViewModel;
    private Text txtDescription;
    private ComboViewer comboType;
    private Text txtSelectedTarget;

    /**
     * @param parent
     *            the parent composite
     * @param style
     *            the style of this composite
     * @param synopticViewModel
     *            the synoptic view model
     * @param availableOpis
     *            the available opi groups
     */
    public TargetSelectorPanel(Composite parent, int style, final SynopticViewModel synopticViewModel) {
        super(parent, style);

        this.viewModel = new TargetSelectorViewModel(synopticViewModel);
        this.availableOpis = viewModel.getAvailableOpis();
        this.compDetailsViewModel = new ComponentDetailViewModel(synopticViewModel);

        setLayout(new GridLayout(1, false));
        setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        // Draw the components
        drawNameAndIconSelector();
        drawTargetTree();

        viewModel.addPropertyChangeListener("enabled", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                Utils.recursiveSetEnabled(TargetSelectorPanel.this, viewModel.isEnabled()); 
            }
        });
        
        // Have to do this here otherwise the binding overrides the default value in the model.
        Utils.recursiveSetEnabled(this, viewModel.isEnabled());
        
        bind();
    }

    @SuppressWarnings("checkstyle:magicnumber") // Gui method   
    private void drawNameAndIconSelector() {

        Composite container = new Composite(this, SWT.NONE);
        container.setLayout(new GridLayout(3, false));
        container.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

        Label lblName = new Label(container, SWT.NONE);
        lblName.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
        lblName.setText("Name: ");

        txtName = new Text(container, SWT.BORDER);
        txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        txtName.addVerifyListener(new ComponentNameVerifier());

        Label lblIcon = new Label(container, SWT.NONE);
        lblIcon.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
        lblIcon.setText("Icon: ");

        comboType = new ComboViewer(container);
        comboType.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        comboType.getCombo().setItems(viewModel.componentTypesArray);
        comboType.getCombo().addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                compDetailsViewModel.updateModelType(viewModel.componentTypesArray[comboType.getCombo().getSelectionIndex()]);
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });
        
        Label lblSelectedTarget = new Label(container, SWT.NONE);
        lblSelectedTarget.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
        lblSelectedTarget.setText("Target OPI: ");

        txtSelectedTarget = new Text(container, SWT.BORDER | SWT.READ_ONLY);
        txtSelectedTarget.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        
        Button buttonClearTarget = new Button(container, SWT.NONE);
        buttonClearTarget.setText("Clear");
        buttonClearTarget.addSelectionListener(new SelectionListener() {         
            @Override
            public void widgetSelected(SelectionEvent e) {
                viewModel.clearTarget();
            }
            
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });
    }

    @SuppressWarnings("checkstyle:magicnumber") // Gui method
    private void drawTargetTree() {

        final List<String> keyset = new ArrayList<>(new TreeSet<>(availableOpis.keySet()));
        Collections.sort(keyset);

        final Tree tree = new Tree(this, SWT.BORDER);
        tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        for (String key : keyset) {
            if (availableOpis.get(key) == null || availableOpis.get(key).isEmpty()) {
                continue;
            }

            List<String> targets = new ArrayList<>(availableOpis.get(key));
            Collections.sort(targets);

            TreeItem category = new TreeItem(tree, SWT.NONE);
            category.setText(key);
            category.setExpanded(false);
            // Use this to tell whether something is a category or an item.
            category.setData(false); 

            for (String target : targets) {
                TreeItem targetTreeItem = new TreeItem(category, SWT.NONE);
                targetTreeItem.setText(target);
                // Use this to tell whether something is a category or an item.
                targetTreeItem.setData(true);
            }
        }
        
        tree.addSelectionListener(new SelectionListener() {         
            @Override
            public void widgetSelected(SelectionEvent e) {
                TreeItem item = tree.getSelection()[0];

                // Was a category not an item.
                if ((boolean) item.getData()) {
                    viewModel.setOpi(item.getText(0));
                } else {            
                    viewModel.clearTarget();
                }
            }
            
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });
        
        txtDescription = new Text(this, SWT.BORDER | SWT.READ_ONLY | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        GridData descriptionLayoutData = new GridData(SWT.FILL, SWT.BOTTOM, true, false);
        descriptionLayoutData.heightHint = 60;
        txtDescription.setLayoutData(descriptionLayoutData);
        
    }
    
    private void bind() {
        DataBindingContext bindingContext = new DataBindingContext();
        bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(txtName),
                BeanProperties.value("name").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.text().observe(txtSelectedTarget),
                BeanProperties.value("opi").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.text().observe(txtDescription),
                BeanProperties.value("description").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.singleSelectionIndex().observe(comboType.getCombo()),
                BeanProperties.value("icon").observe(viewModel));
    }

}
