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

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.nicos.NicosModel;
import uk.ac.stfc.isis.ibex.nicos.ScriptSendStatus;

/**
 * View Model for queueing a script.
 */
public class QueueScriptViewModel extends ModelObject {

    private NicosModel model;
    private String script = "";
    private ScriptSendStatus scriptSendStatus;
    private String scriptSendErrorMessage;
    private DataBindingContext bindingContext = new DataBindingContext();
    /**
     * Constructor.
     * 
     * @param model
     *            the nicos model
     * @param initialScript
     *            an initial script to use
     */
    public QueueScriptViewModel(NicosModel model, String initialScript) {
        super();
        this.model = model;
        setScript(initialScript);
        
        bindingContext.bindValue(BeanProperties.value("scriptSendErrorMessage").observe(model),
                BeanProperties.value("scriptSendErrorMessage").observe(this));
        bindingContext.bindValue(BeanProperties.value("scriptSendStatus").observe(model),
                BeanProperties.value("scriptSendStatus").observe(this));
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
     *            the new script send status
     */
    public void setScriptSendStatus(ScriptSendStatus scriptSendStatus) {
        firePropertyChange("scriptSendStatus", this.scriptSendStatus, this.scriptSendStatus = scriptSendStatus);
    }

}
