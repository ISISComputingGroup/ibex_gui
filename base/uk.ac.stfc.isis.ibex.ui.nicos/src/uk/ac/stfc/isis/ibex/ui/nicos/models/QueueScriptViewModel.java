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
    private String script = "";
    private ScriptSendStatus scriptSendStatus;
    private String scriptSendErrorMessage;
    private DataBindingContext bindingContext = new DataBindingContext();
    private Boolean directionUp;
    /**
     * Constructor.
     * 
     * @param model
     *            the NICOS model
     * @param initialScript
     *            an initial script to use
     */
    public QueueScriptViewModel(NicosModel model, String initialScript) {
        this.model = model;
        setScript(initialScript);
        
        bindingContext.bindValue(BeanProperties.value("scriptSendErrorMessage").observe(this),
                BeanProperties.value("scriptSendErrorMessage").observe(model));
        bindingContext.bindValue(BeanProperties.value("scriptSendStatus").observe(this),
                BeanProperties.value("scriptSendStatus").observe(model));
    }


    /**
     * Gets the current script.
     *
     * @return the script
     */
    public String getScript() {
        return script;
    }

    /**
     * Sets the script.
     *
     * @param script
     *            the new script
     */
    public void setScript(String script) {
        firePropertyChange("script", this.script, this.script = script);
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
     *			the new script send status
     */
    public void setScriptSendStatus(ScriptSendStatus scriptSendStatus) {
        firePropertyChange("scriptSendStatus", this.scriptSendStatus, this.scriptSendStatus = scriptSendStatus);
    }
    
    /**
     * Dequeue the selected script on the script server.
     * 
     * @param selected
     * 			the script to be dequeued
     */
    public void dequeueScript(QueuedScript selected) {
        // dequeue command only requires ID (reqid) of script
    	model.dequeueScript(selected.reqid);
    }

    	/** TODO:
    	 *  - get queue from NICOS (or current viewtable contents?)
    	 *  - get selected script's index in table
    	 *  - get index of script above selected (selected script's index - 1) IF >= 0
    	 *  - swap indices of both scripts
    	 *  - send reordered queue/list to NICOS
    	 */

    /**
     * Move selected script position in queue according to supplied direction. 
     *
     * @param selectedScript
     * 			the selected script in the viewtable
     * @param directionUp
     * 			true if direction of desired movement is UP, false if DOWN
     * @return queue
     * 			the reordered queue
     */
    public List<QueuedScript> moveScript(QueuedScript selectedScript, Boolean directionUp) {

    	List<QueuedScript> queue = model.getQueuedScripts();
    	int sourceIndex = queue.indexOf(selectedScript);
    	int targetIndex = 0;

    	// only move scripts:
    	// IF selected script is NOT first AND move direction is up
    	// OR 
    	// IF selected script is NOT last AND move direction is down
    	
    	// TODO: test fails because there are more elements than scripts in list (10?). extras contain null. 
    	
    	if ((sourceIndex != 0 && directionUp) || (sourceIndex != queue.size() && !directionUp)) {
	    	
	    	// subtract 1 if direction UP, add 1 if DOWN
	    	// TODO: make this more efficient
	    	if (directionUp) {
	    		targetIndex = sourceIndex - 1;
	    	}
	    	else {
	        	targetIndex = sourceIndex + 1;
	    	}
	    	
	    	Collections.swap(queue, sourceIndex, targetIndex);
    	}
    return queue;
    }

    /**
     *  Create list of reqids in reordered queue and send to NicosModel.
     *  
     *  @param reorderedQueue
     *  		queue containing the reordered scripts
     */
    public void extractReqids(QueuedScript reorderedQueue) {
    
    // TODO:
    // read reqid of each script in list and add to new list
    // pass to NICOS model to send to server

    }
}
