
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

package uk.ac.stfc.isis.ibex.ui.synoptic.editor.component;

import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.synoptic.model.ComponentType;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.IComponentSelectionListener;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.IInstrumentUpdateListener;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.SynopticViewModel;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.UpdateTypes;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.pv.PVList;

/**
 * UI section that allows the user to view and edit the details of a component:
 * name, type, and PVs
 * 
 */
public class ComponentDetailView extends Composite {
	private SynopticViewModel instrument;

	private ComponentDescription component;

	private Composite fieldsComposite;
	private Composite labelComposite;

	private Text txtName;
	private ComboViewer cmboType;
	private Label lblTypeIcon;

	private PVList pvList;

    private static List<String> typeList = ComponentType.componentTypeAlphaList();

	public ComponentDetailView(Composite parent,
			final SynopticViewModel instrument) {
		super(parent, SWT.NONE);

		this.instrument = instrument;

		instrument.addInstrumentUpdateListener(new IInstrumentUpdateListener() {
			@Override
			public void instrumentUpdated(UpdateTypes updateType) {
                if (updateType == UpdateTypes.EDIT_COMPONENT) {
                    instrument.addTargetToSelectedComponent();
                }
                if (updateType != UpdateTypes.EDIT_COMPONENT && updateType != UpdateTypes.EDIT_TARGET
                        && updateType != UpdateTypes.ADD_TARGET) {
					component = instrument.getSelectedComponent();
					showComponent(component);
				}
			}
		});

		instrument
				.addComponentSelectionListener(new IComponentSelectionListener() {
					@Override
					public void selectionChanged(
							List<ComponentDescription> oldSelection,
							List<ComponentDescription> newSelection) {
						if (newSelection != null && newSelection.size() == 1) {
							component = newSelection.iterator().next();
						} else {
							component = null;
						}
						showComponent(component);
					}
				});

		setLayout(new GridLayout(1, false));
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		createControls(this);
		showComponent(null);
	}

	public void createControls(final Composite parent) {
		labelComposite = new Composite(parent, SWT.NONE);
		labelComposite.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true,
				false, 1, 1));
		labelComposite.setLayout(new GridLayout());
		{
			Label lblNoSelection = new Label(labelComposite, SWT.NONE);
			lblNoSelection.setText("Select a component to view/edit details");
		}

		fieldsComposite = new Composite(parent, SWT.NONE);
		fieldsComposite.setLayout(new GridLayout(2, false));
		fieldsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false, 1, 1));
		{
			Label lblName = new Label(fieldsComposite, SWT.NONE);
			lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
					false, 1, 1));
			lblName.setText("Name");

			txtName = new Text(fieldsComposite, SWT.BORDER);
			txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
					false, 1, 1));

			txtName.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					updateModelName();
				}
			});

			Label lblType = new Label(fieldsComposite, SWT.NONE);
			lblType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
					false, 1, 1));
			lblType.setText("Type");

			cmboType = new ComboViewer(fieldsComposite, SWT.READ_ONLY);
			cmboType.getCombo().setLayoutData(
					new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

			cmboType.setContentProvider(ArrayContentProvider.getInstance());
            cmboType.setInput(typeList);
			cmboType.getCombo().select(0);
			cmboType.addSelectionChangedListener(new ISelectionChangedListener() {
				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					updateModelType();
					updateTypeIcon();
				}
			});

			Label lblIcon = new Label(fieldsComposite, SWT.NONE);
			lblIcon.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false,
					false, 1, 1));
			lblIcon.setText("Icon");

			lblTypeIcon = new Label(fieldsComposite, SWT.BORDER);
			GridData iconGridData = new GridData(SWT.LEFT, SWT.CENTER, false,
					false, 1, 1);
			iconGridData.widthHint = 32;
			iconGridData.heightHint = 32;
			lblTypeIcon.setLayoutData(iconGridData);

			Label lblPvs = new Label(fieldsComposite, SWT.NONE);
			lblPvs.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false,
					1, 1));
			lblPvs.setText("PVs");

			pvList = new PVList(fieldsComposite, instrument);
			GridData pvGridData = new GridData(SWT.FILL, SWT.CENTER, true,
					false, 1, 1);
			pvGridData.heightHint = 150;
			pvList.setLayoutData(pvGridData);
		}
	}

	public void showComponent(ComponentDescription component) {
		if (component != null) {
			fieldsComposite.setVisible(true);
			labelComposite.setVisible(false);

			txtName.setText(component.name());

            int typeIndex = typeList.indexOf(component.type().name());
            cmboType.getCombo().select(typeIndex);

			updateTypeIcon();
		} else {
			fieldsComposite.setVisible(false);
			labelComposite.setVisible(true);

			txtName.setText("None");
			cmboType.getCombo().select(0);
		}
	}

	private void updateModelName() {
		if (component != null) {
			component.setName(txtName.getText());
			instrument.broadcastInstrumentUpdate(UpdateTypes.EDIT_COMPONENT);
		}
	}

	private void updateModelType() {
		if (component != null) {
			int typeIndex = cmboType.getCombo().getSelectionIndex();
            String selection = typeList.get(typeIndex);
            ComponentType type = ComponentType.valueOf(selection);
			component.setType(type);

			instrument.broadcastInstrumentUpdate(UpdateTypes.EDIT_COMPONENT);
		}
	}

	private void updateTypeIcon() {
		int typeIndex = cmboType.getCombo().getSelectionIndex();
		Image icon = null;

		if (typeIndex < 0) {
			icon = ComponentIcons.thumbnailForType(ComponentType.UNKNOWN);
		} else {
            String selection = typeList.get(typeIndex);
            ComponentType enteredType = ComponentType.valueOf(selection);
            icon = ComponentIcons.thumbnailForType(enteredType);
		}

		lblTypeIcon.setImage(icon);
	}
}
