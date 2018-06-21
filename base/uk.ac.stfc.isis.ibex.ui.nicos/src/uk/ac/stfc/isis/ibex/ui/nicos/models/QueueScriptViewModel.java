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
package uk.ac.stfc.isis.ibex.ui.nicos.models;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.nicos.NicosModel;
import uk.ac.stfc.isis.ibex.nicos.ScriptSendStatus;
import uk.ac.stfc.isis.ibex.nicos.messages.scriptstatus.QueuedScript;

/**
 * View Model for queueing a script.
 */
public class QueueScriptViewModel extends ModelObject {

    private NicosModel model;
    private QueuedScript script;
    private ScriptSendStatus scriptSendStatus;
    private String scriptSendErrorMessage;
    private DataBindingContext bindingContext = new DataBindingContext();
    private QueuedScript selectedScript;
    private Boolean upButtonEnabled = false;
    private Boolean downButtonEnabled = false;
    private Boolean dequeueButtonEnabled = false;
    
    /**
     * Constructor.
     * 
     * @param model
     *            the NICOS model
     */
    public QueueScriptViewModel(NicosModel model) {
        this.model = model;
        
        this.script = new QueuedScript();
        
        bindingContext.bindValue(BeanProperties.value("scriptSendErrorMessage").observe(this),
                BeanProperties.value("scriptSendErrorMessage").observe(model));
        bindingContext.bindValue(BeanProperties.value("scriptSendStatus").observe(this),
                BeanProperties.value("scriptSendStatus").observe(model));
        
        model.addPropertyChangeListener("queuedScripts", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updateButtonEnablement();
            }
        });
    }


    /**
     * Gets the current script.
     *
     * @return the script
     */
    public QueuedScript getScript() {
        return script;
    }

    /**
     * Queue the current script on the script server.
     */
    public void queueScript() {
        model.sendScript(script);
    }


    /**
     * Get the last error message received when queueing a script.
     * 
     * Blank for no error message.
     * 
     * @return the script send error message
     */
    public String getScriptSendErrorMessage() {
        return scriptSendErrorMessage;
    }

    /**
     * Set the script error message and fire a property change.
     * 
     * @param scriptSendErrorMessage
     *            the new error message
     */
    public void setScriptSendErrorMessage(String scriptSendErrorMessage) {
        firePropertyChange("scriptSendErrorMessage", this.scriptSendErrorMessage,
                this.scriptSendErrorMessage = scriptSendErrorMessage);
    }

    /**
     * Get the status of the last script that was sent.
     * 
     * @return the status of sending a script
     */
    public ScriptSendStatus getScriptSendStatus() {
        return scriptSendStatus;
    }

    /**
     * Sets the script send status; the status of the last script that was sent.
     *
     * @param scriptSendStatus
     *            the new script send status
     */
    public void setScriptSendStatus(ScriptSendStatus scriptSendStatus) {
        firePropertyChange("scriptSendStatus", this.scriptSendStatus, this.scriptSendStatus = scriptSendStatus);
    }
    
    /**
     * Dequeue the selected script on the script server.
     */
    public void dequeueScript() {
        // dequeue command only requires ID (reqid) of script
        model.dequeueScript(selectedScript.reqid);
    }

    /**
     * Move selected script position in queue according to supplied direction. 
     *
     * @param directionUp
     *             true if direction of desired movement is UP, false if DOWN
     */
    public void moveScript(Boolean directionUp) {

        List<QueuedScript> copyOfQueue = new ArrayList<>(model.getQueuedScripts());
        int sourceIndex = copyOfQueue.indexOf(selectedScript);

        // only move scripts:
        // IF selected script is NOT first AND move direction is up
        // OR 
        // IF selected script is NOT last AND move direction is down
        
        if ((sourceIndex > 0 && directionUp) || (sourceIndex < copyOfQueue.size() && !directionUp)) {
            
            // subtract 1 if direction UP, add 1 if DOWN
            
            int targetIndex = directionUp ? sourceIndex - 1 : sourceIndex + 1;
            Collections.swap(copyOfQueue, sourceIndex, targetIndex);
            sendReorderedList(copyOfQueue);
        }
        
        updateButtonEnablement();
    }

    /**
     *  Create list of reqids in reordered queue and send to NicosModel.
     *  
     *  @param reorderedQueue
     *          queue containing the reordered scripts
     */
    public void sendReorderedList(List<QueuedScript> reorderedQueue) {
 
        List<String> listOfScriptIDs = new ArrayList<>(); 
        
        for (QueuedScript item : reorderedQueue) {
            listOfScriptIDs.add(item.reqid);
        }

        model.sendReorderedQueue(listOfScriptIDs);
        updateButtonEnablement();
    }
    
    /**
     * Call methods to update state of buttons
     * 
     */   
    private void updateButtonEnablement() {
        updateDownButtonEnabled();
        updateUpButtonEnabled();
        updateDequeueButtonEnabled();
    }
    
    /**
     * Set the selected script.
     * 
     * @param script
     *         The selected script
     */
    public void setSelectedScript(QueuedScript script) {
        this.selectedScript = script;
        
        updateButtonEnablement();
    }
    
    /**
     * Return the selected script in the queue.
     * 
     * @return selectedScript
     *             The selected script in the queue
     */
    public QueuedScript getSelectedScript() {
        return selectedScript;
    }
    
    /**
     * Determine whether or not Dequeue button can be enabled depending on whether or not script selected in queue.
     * 
     */
    private void updateDequeueButtonEnabled() {
        firePropertyChange("dequeueButtonEnabled", null, dequeueButtonEnabled = selectedScript != null);
    }

    /**
     * Determine whether or not Down button can be enabled depending on position of selected script in queue.
     * 
     */
    private void updateDownButtonEnabled() {
        QueuedScript lastScriptInQueue;
        try {
            lastScriptInQueue = model.getQueuedScripts().get(model.getQueuedScripts().size() - 1);
             
            if (selectedScript == null) {
                downButtonEnabled = false;
            } else if (selectedScript.equals(lastScriptInQueue)) {
                downButtonEnabled = false;
            } else {
                downButtonEnabled = true;
            }
        } catch (IndexOutOfBoundsException e) {
            downButtonEnabled = false;
        }
        
        firePropertyChange("downButtonEnabled", null, downButtonEnabled);
    }
    
    /**
     * Determine whether or not Up button can be enabled depending on position of selected script in queue.
     * 
     */
    private void updateUpButtonEnabled() {
        QueuedScript firstScriptInQueue;
        try {
            firstScriptInQueue = model.getQueuedScripts().get(0);
             
            if (selectedScript == null) {
                upButtonEnabled = false;
            } else if (selectedScript.equals(firstScriptInQueue)) {
                upButtonEnabled = false;
            } else {
                upButtonEnabled = true;
            }
        } catch (IndexOutOfBoundsException e) {
            upButtonEnabled = false;
        }
        
        firePropertyChange("upButtonEnabled", null, upButtonEnabled);
    }
    
    /**
     * Get enabled state of Up button.
     * 
     * @return enabled state of Up button
     */
    public boolean getUpButtonEnabled() {
        return upButtonEnabled;
    }
    
    /**
     * Get enabled state of Down button.
     * 
     * @return enabled state of Down button
     */
    public boolean getDownButtonEnabled() {
        return downButtonEnabled;
    }
    
    /**
     * Get enabled state of Dequeue button.
     * 
     * @return enabled state of Dequeue button
     */
    public boolean getDequeueButtonEnabled() {
        return dequeueButtonEnabled;
    }
}
