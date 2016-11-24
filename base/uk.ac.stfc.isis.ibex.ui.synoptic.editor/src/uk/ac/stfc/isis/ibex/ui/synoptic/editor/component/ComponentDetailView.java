
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
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.devicescreens.components.ComponentType;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;
import uk.ac.stfc.isis.ibex.ui.devicescreens.ComponentIcons;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.IComponentSelectionListener;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.IInstrumentUpdateListener;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.SynopticViewModel;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.UpdateTypes;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.pv.PVList;

/**
 * UI section that allows the user to view and edit the details of a component:
 * name, type, and PVs.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class ComponentDetailView extends Composite {
    private static final String SELECT_COMPONENT = "Select a component to view/edit details";
    private static final String UNIQUE_COMPONENT_NAME = "Component names must be unique";
    private final Color colorBlack = SWTResourceManager.getColor(0, 0, 0);
    private final Color colorRed = SWTResourceManager.getColor(255, 0, 0);
	private SynopticViewModel synopticViewModel;
	private ComponentDescription component;
	private Composite fieldsComposite;
	private Composite labelComposite;
	private Text txtName;
	private ComboViewer cmboType;
	private Label lblTypeIcon;
    private Label lblNoSelection;
    private boolean selectionCausedByMouseClick = false;
	private PVList pvList;
    private static List<String> typeList = ComponentType.componentTypeAlphaList();

    /**
     * The constructor.
     * 
     * @param parent the parent composite
     * @param synopticViewModel the view model
     */
	public ComponentDetailView(Composite parent,
			final SynopticViewModel synopticViewModel) {
		super(parent, SWT.NONE);

		this.synopticViewModel = synopticViewModel;

		synopticViewModel.addInstrumentUpdateListener(new IInstrumentUpdateListener() {
			@Override
			public void instrumentUpdated(UpdateTypes updateType) {
                if (updateType == UpdateTypes.EDIT_COMPONENT) {
                    synopticViewModel.addTargetToSelectedComponent(false);
                } else if (updateType == UpdateTypes.EDIT_COMPONENT_FINAL) {
                    synopticViewModel.addTargetToSelectedComponent(true);
                }

                if (updateType != UpdateTypes.EDIT_COMPONENT && updateType != UpdateTypes.EDIT_TARGET
                        && updateType != UpdateTypes.ADD_TARGET) {
					component = synopticViewModel.getFirstSelectedComponent();
					showComponent(component);
				}
			}
		});

		synopticViewModel
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
		
        synopticViewModel.addInstrumentUpdateListener(new IInstrumentUpdateListener() {
            @Override
            public void instrumentUpdated(UpdateTypes updateType) {
                if (updateType == UpdateTypes.EDIT_COMPONENT) {
                    if (synopticViewModel.getHasDuplicatedName()) {
                        labelComposite.setVisible(true);
                        lblNoSelection.setText(UNIQUE_COMPONENT_NAME);
                        lblNoSelection.setForeground(colorRed);
                    } else {
                        labelComposite.setVisible(false);
                    }
                }
            }
        });
		
		setLayout(new GridLayout(1, false));
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		createControls(this);
		showComponent(null);
	}

    /**
     * Create the widgets for the view.
     * 
     * @param parent the parent composite (this)
     */
	public void createControls(final Composite parent) {
		labelComposite = new Composite(parent, SWT.NONE);
		labelComposite.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true,
				false, 1, 1));
		labelComposite.setLayout(new GridLayout());

        lblNoSelection = new Label(labelComposite, SWT.NONE);
        lblNoSelection.setText(SELECT_COMPONENT);

		fieldsComposite = new Composite(parent, SWT.NONE);
		fieldsComposite.setLayout(new GridLayout(2, false));
		fieldsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false, 1, 1));

        Label lblName = new Label(fieldsComposite, SWT.NONE);
        lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblName.setText("Name");

        txtName = new Text(fieldsComposite, SWT.BORDER);
        txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        txtName.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                updateModelName();
            }
        });

        Label lblType = new Label(fieldsComposite, SWT.NONE);
        lblType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblType.setText("Type");

        cmboType = new ComboViewer(fieldsComposite, SWT.READ_ONLY);
        cmboType.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        cmboType.setContentProvider(ArrayContentProvider.getInstance());
        cmboType.setInput(typeList);
        cmboType.getCombo().select(0);
        cmboType.getCombo().addFocusListener(new FocusListener() {
            @Override
            public void focusGained(org.eclipse.swt.events.FocusEvent e) {
            }

            @Override
            public void focusLost(org.eclipse.swt.events.FocusEvent e) {
                if (!selectionCausedByMouseClick) {
                    updateModelType(true);
                    updateTypeIcon();
                }
            }
        });

        cmboType.getCombo().addMouseListener(new MouseListener() {

            @Override
            public void mouseDoubleClick(MouseEvent e) {
            }

            @Override
            public void mouseDown(MouseEvent e) {
            }

            @Override
            public void mouseUp(MouseEvent e) {
                selectionCausedByMouseClick = true;
            }
        });

        cmboType.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                boolean isFinalUpdate = selectionCausedByMouseClick;
                updateModelType(isFinalUpdate);
                updateTypeIcon();
                selectionCausedByMouseClick = false;
            }
        });

        Label lblIcon = new Label(fieldsComposite, SWT.NONE);
        lblIcon.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
        lblIcon.setText("Icon");

        lblTypeIcon = new Label(fieldsComposite, SWT.BORDER);
        GridData iconGridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        iconGridData.widthHint = 32;
        iconGridData.heightHint = 32;
        lblTypeIcon.setLayoutData(iconGridData);
        lblTypeIcon.setAlignment(SWT.CENTER);

        Label lblPvs = new Label(fieldsComposite, SWT.NONE);
        lblPvs.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
        lblPvs.setText("PVs");

        pvList = new PVList(fieldsComposite, synopticViewModel);
        GridData pvGridData = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        pvGridData.heightHint = 150;
        pvList.setLayoutData(pvGridData);
	}

    /**
     * Display a synoptic component in the view.
     * 
     * @param component the component to display
     */
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

            lblNoSelection.setText(SELECT_COMPONENT);
            lblNoSelection.setForeground(colorBlack);

			txtName.setText("None");
			cmboType.getCombo().select(0);
		}
	}

	private void updateModelName() {
		if (component != null) {
			component.setName(txtName.getText());
			synopticViewModel.broadcastInstrumentUpdate(UpdateTypes.EDIT_COMPONENT);
		}
	}

    private void updateModelType(boolean isFinalUpdate) {
		if (component != null) {
			int typeIndex = cmboType.getCombo().getSelectionIndex();
            String selection = typeList.get(typeIndex);
            ComponentType type = ComponentType.valueOf(selection);
            component.setType(type, true);

            if (isFinalUpdate) {
                synopticViewModel.broadcastInstrumentUpdate(UpdateTypes.EDIT_COMPONENT_FINAL);
            } else {
                synopticViewModel.broadcastInstrumentUpdate(UpdateTypes.EDIT_COMPONENT);
            }
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
