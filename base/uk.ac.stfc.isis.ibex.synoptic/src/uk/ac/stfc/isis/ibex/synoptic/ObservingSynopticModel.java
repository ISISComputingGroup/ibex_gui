/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2016
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.synoptic;

import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.Observer;
import uk.ac.stfc.isis.ibex.epics.switching.SwitchableObservable;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.synoptic.internal.Variables;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.SynopticDescription;

/**
 * A class for linking the PV observables used to define the synoptic with the
 * SynopticModel.
 *
 */
public class ObservingSynopticModel extends ModelObject {

    SynopticInfo synopticInfo;

    private final Observer<SynopticDescription> descriptionObserver = new BaseObserver<SynopticDescription>() {
        @Override
        public void onValue(SynopticDescription value) {
            model.setSynopticFromDescription(value);
        }

        @Override
        public void onError(Exception e) {
            LoggerUtils.logErrorWithStackTrace(IsisLog.getLogger(getClass()), e.getMessage(), e);
            model.setSynopticFromDescription(SynopticDescription.getEmptySynopticDescription());
        }

        @Override
        public void onConnectionStatus(boolean isConnected) {
            if (!isConnected) {
                model.setSynopticFromDescription(SynopticDescription.getEmptySynopticDescription());
            }
        }
    };

	private final SynopticModel model;
	private final Variables variables;
    private final SwitchableObservable<SynopticDescription> synopticObservable;

    /**
     * Create a link between the PV observables used to define the synoptic and the synoptic model.
     * 
     * @param variables Contains the PV observables.
     * @param model The synoptic model to link to.
     */
	public ObservingSynopticModel(Variables variables, SynopticModel model) {
		this.model = model;
		this.variables = variables;

        synopticObservable = new SwitchableObservable<SynopticDescription>(
                variables.getSynopticDescription(Variables.NONE_SYNOPTIC_PV));
        synopticObservable.subscribe(descriptionObserver);
	}

	/**
	 * Change this synoptic to use the new information given and set the details 
	 *  observable to the new description.
	 * 
	 * @param newSynoptic The information for the synoptic to switch to.
	 */
	public void switchSynoptic(SynopticInfo newSynoptic) {
        synopticInfo = newSynoptic;
        synopticObservable.setSource(variables.getSynopticDescription(newSynoptic.pv()));
	}

	/**
	 * Get an observable description of the synoptic.
	 * 
	 * @return An observable description of the synoptic.
	 */
    public SwitchableObservable<SynopticDescription> getSynopticObservable() {
        return synopticObservable;
    }

    /**
     * Get basic information about this synoptic.
     * 
     * @return Basic information about this synoptic.
     */
    public SynopticInfo getSynopticInfo() {
        return synopticInfo;
    }
}
