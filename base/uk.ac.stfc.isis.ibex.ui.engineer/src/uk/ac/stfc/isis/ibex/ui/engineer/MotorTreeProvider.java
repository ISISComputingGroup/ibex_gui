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
package uk.ac.stfc.isis.ibex.ui.engineer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import uk.ac.stfc.isis.ibex.motor.Controller;
import uk.ac.stfc.isis.ibex.motor.Motor;
import uk.ac.stfc.isis.ibex.motor.MotorsTable;

/**
 * Provides the content for the motor tree viewer.
 */
class MotorTreeProvider implements ITreeContentProvider, ILabelProvider {

    private static final String MOTOR_NAME_FORMAT = "%s (%s)";

    @Override
    public Object[] getChildren(Object parent) {
        if (parent instanceof Controller) {
            return ((Controller) parent).motors().toArray();
        } else if (parent instanceof MotorsTable) {
            return ((MotorsTable) parent).controllers().toArray();
        }
        return null;
    }

    @Override
    public Object getParent(Object arg0) {
        return ((Motor) arg0).getController();
    }

    @Override
    public boolean hasChildren(Object arg0) {
        return arg0 instanceof Controller;
    }

    @Override
    public void inputChanged(final Viewer viewer, Object oldInput, Object newInput) {
        PropertyChangeListener refreshViewer = new PropertyChangeListener() {

            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                Display.getDefault().asyncExec(new Runnable() {
                    @Override
                    public void run() {
                        TreeViewer view = ((TreeViewer) viewer);
                        view.update(evt.getSource(), null);
                    }
                });
            }
        };

        if (newInput instanceof MotorsTable) {
            Collection<Controller> controllers = ((MotorsTable) newInput).controllers();
            for (Controller control : controllers) {
                for (Motor mtr : control.motors()) {
                    mtr.addPropertyChangeListener("description", refreshViewer);
                    mtr.addPropertyChangeListener("name", refreshViewer);
                }
            }
        }
    }

    /**
     * @param inputElement
     * @return
     */
    @Override
    public Object[] getElements(Object inputElement) {
        return ((MotorsTable) inputElement).controllers().toArray();
    }

    @Override
    public boolean isLabelProperty(Object element, String property) {
        return true;
    }

    @Override
    public String getText(Object element) {
        if (element instanceof Motor) {
            Motor mtr = (Motor) element;
            return String.format(MOTOR_NAME_FORMAT, mtr.name(), mtr.getDescription());
        } else {
            return "TODO";
        }
    }

    @Override
    public void addListener(ILabelProviderListener listener) {
    }

    @Override
    public void removeListener(ILabelProviderListener listener) {
    }

    @Override
    public Image getImage(Object element) {
        return null;
    }

    @Override
    public void dispose() {
        // Nothing to dispose
    }
}
