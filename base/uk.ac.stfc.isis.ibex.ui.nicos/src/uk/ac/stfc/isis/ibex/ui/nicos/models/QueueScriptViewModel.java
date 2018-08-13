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
import java.util.stream.Collectors;

import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.nicos.NicosModel;
import uk.ac.stfc.isis.ibex.nicos.messages.scriptstatus.QueuedScript;

/**
 * View Model for queueing a script.
 */
public class QueueScriptViewModel extends ModelObject {

    private NicosModel model;
    private QueuedScript scriptToSend;
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
        
        this.scriptToSend = new QueuedScript();
        
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
        return scriptToSend;
    }

    /**
     * Queue the current script on the script server.
     */
    public void queueScript() {
        model.sendScript(scriptToSend);
    }
    
    /**
     * Dequeue the selected script on the script server.
     */
    public void dequeueScript() {
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

        // only move scripts if selected script is NOT first AND move direction is up
        // or selected script is NOT last AND move direction is down
        if ((sourceIndex > 0 && directionUp) || (sourceIndex < copyOfQueue.size() - 1 && !directionUp)) {
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
        List<String> scriptIDs = reorderedQueue.stream().map(s -> s.reqid).collect(Collectors.toList());

        model.sendReorderedQueue(scriptIDs);
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
        selectedScript = script;
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
     */
    private void updateDequeueButtonEnabled() {
        firePropertyChange("dequeueButtonEnabled", null, dequeueButtonEnabled = selectedScript != null);
    }
    
    /**
     * Determines if the selected script is null or in the specified position.
     * @param position
     * 			The position to check.
     * @return True if selected script is null or in the specified, false otherwise
     */
    private boolean isNullOrInPosition(int position) {
    	int scriptIdx = model.getQueuedScripts().indexOf(selectedScript);
    	return (scriptIdx == -1 || scriptIdx == position);
    }
    
    /**
     * Determine whether or not Down button can be enabled depending on position of selected script in queue.
     */
    private void updateDownButtonEnabled() {
    	boolean newButtonStatus = !isNullOrInPosition(model.getQueuedScripts().size() - 1);
        firePropertyChange("downButtonEnabled", downButtonEnabled, downButtonEnabled = newButtonStatus);
    }
    
    /**
     * Determine whether or not Up button can be enabled depending on position of selected script in queue.
     */
    private void updateUpButtonEnabled() {
    	boolean newButtonStatus = !isNullOrInPosition(0);
        firePropertyChange("upButtonEnabled", upButtonEnabled, upButtonEnabled = newButtonStatus);
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
    
    /**
     * Saves the currently selected script.
     * @param shell The shell to open up dialog boxes within.
     */
    public void saveSelected(Shell shell) {
    	(new SaveScriptAction(shell, selectedScript)).execute();
    }
}
