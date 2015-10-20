
/**
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

package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.model.ModelObject;

public class BlockLogSettingsViewModel extends ModelObject {
    public static String PERIODIC_STRING = "Periodic";
    public static String MONITOR_STRING = "Monitor With Deadband";

    public static String DEADBAND_LABEL = "Deadband:";
    public static String SCAN_LABEL = "Scan Rate:";

    private final Block editingBlock;
    private boolean enabled = true;
    private String periodic;
    private String labelText;
    private String textBoxText;

    PropertyChangeListener textBoxListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            setTextBoxText(String.valueOf(evt.getNewValue()));
        }
    };

    private void updatePeriodic(boolean periodic) {
        editingBlock.removePropertyChangeListener(textBoxListener);
        if (periodic) {
            setLabelText(SCAN_LABEL);
            setPeriodic(PERIODIC_STRING);
            setTextBoxText(Integer.toString(editingBlock.getLogRate()));
            editingBlock.addPropertyChangeListener("log_rate", textBoxListener);
        } else {
            setLabelText(DEADBAND_LABEL);
            setPeriodic(MONITOR_STRING);
            setTextBoxText(Float.toString(editingBlock.getLogDeadband()));
            editingBlock.addPropertyChangeListener("log_deadband", textBoxListener);
        }
    }

    public BlockLogSettingsViewModel(final Block editingBlock) {
        this.editingBlock = editingBlock;

        editingBlock.addPropertyChangeListener("log_periodic", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updatePeriodic((boolean) evt.getNewValue());
            }
        });

        if (editingBlock.getLogPeriodic() && editingBlock.getLogRate() == 0) {
            setEnabled(false);
        }

        updatePeriodic(editingBlock.getLogPeriodic());
    }

    public void setEnabled(boolean enabled) {
        if (!enabled) {
            updatePeriodic(true);
            editingBlock.setLogRate(0);
        }

        firePropertyChange("enabled", this.enabled, this.enabled = enabled);

    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setLabelText(String text) {
        firePropertyChange("labelText", this.labelText, this.labelText = text);
    }

    public String getLabelText() {
        return labelText;
    }

    public void setPeriodic(String selection) {
        if (selection.equals(PERIODIC_STRING)) {
            editingBlock.setLogPeriodic(true);
        } else if (selection.equals(MONITOR_STRING)) {
            editingBlock.setLogPeriodic(false);
        }
        firePropertyChange("periodic", this.periodic, this.periodic = selection);
    }

    public String getPeriodic() {
        return periodic;
    }

    public void setTextBoxText(String text) {
        if (editingBlock.getLogPeriodic()) {
            editingBlock.setLogRate(Integer.parseInt(text));
        } else {
            editingBlock.setLogDeadband(Float.parseFloat(text));
        }
        firePropertyChange("textBoxText", this.textBoxText, this.textBoxText = text);
    }

    public String getTextBoxText() {
        return textBoxText;
    }
}
