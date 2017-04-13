
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

package uk.ac.stfc.isis.ibex.ui.synoptic.beamline;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.targets.Target;
import uk.ac.stfc.isis.ibex.ui.synoptic.Activator;
import uk.ac.stfc.isis.ibex.ui.synoptic.SynopticPresenter;

/**
 * The abstract class for any items shown in the synoptic.
 */
public abstract class BeamlineComposite extends Composite  {

    protected Target target;
    protected CLabel nameLabel;
    private SynopticPresenter presenter = Activator.getDefault().presenter();
    private final Cursor handCursor = SWTResourceManager.getCursor(SWT.CURSOR_HAND);

    /**
     * The default constructor for the composite.
     * 
     * @param parent
     *            The parent composite.
     * @param style
     *            The SWT style for the composite.
     */
	public BeamlineComposite(Composite parent, int style) {
		super(parent, style);
	}

    /**
     * Set the target this component is pointing to.
     * 
     * @param target
     *            The target to point to.
     */
    public void setTarget(Target target) {
        this.target = target;
        nameLabel.setToolTipText(target.name());
        addTargetListeners(nameLabel);
    }

    /**
     * Sets the name of this component.
     * 
     * @param name
     *            The name to set.
     */
    public void setName(String name) {
        nameLabel.setText(name);
    }

    /**
     * Adds the listeners to a control to modify the cursor and navigate to the target. 
     * @param c The control to add the listeners to
     */
    protected void addTargetListeners(Control c) {
        c.addListener(SWT.MouseEnter, new Listener() {
            @Override
            public void handleEvent(Event event) {
                if (presenter.isValidTarget(target)) {
                    setCursor(target.name() == null ? null : handCursor);
                }
            }
        });

        c.addListener(SWT.MouseExit, new Listener() {
            @Override
            public void handleEvent(Event event) {
                setCursor(null);
            }
        });

        c.addListener(SWT.MouseUp, new Listener() {
            @Override
            public void handleEvent(Event event) {
                if (presenter.isValidTarget(target)) {
                    presenter.navigateTo(target.name());
                }
            }
        });
    }

    /**
     * The desired height of the composite relative to its parent.
     * 
     * @return the beam height.
     */
	public abstract int beamLineHeight();
}
