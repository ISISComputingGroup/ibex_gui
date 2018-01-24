
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

package uk.ac.stfc.isis.ibex.synoptic;

import java.util.Collection;

import org.apache.logging.log4j.Logger;

import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.synoptic.internal.ObservableSynoptic;
import uk.ac.stfc.isis.ibex.synoptic.internal.Variables;
import uk.ac.stfc.isis.ibex.synoptic.model.Synoptic;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.SynopticDescription;
import uk.ac.stfc.isis.ibex.synoptic.navigation.InstrumentNavigationGraph;

/**
 * Model for the IBEX synoptic perspective.
 */
public class SynopticModel extends ModelObject {
	
	private static final Logger LOG = IsisLog.getLogger(SynopticModel.class);
			
	private final Variables variables;

	private ObservableSynoptic instrument; 
	private SynopticWriter setCurrentSynoptic;
	
	/**
	 * @param variables Data associated with viewing and editing synoptics (e.g. PV, schemas).
	 */
	public SynopticModel(Variables variables) {
		this.variables = variables;
		instrument = getInstrument(new SynopticDescription());
		
        setCurrentSynoptic = new SynopticWriter(variables.synopticSetter, variables.synopticSchema);
	}
	
	public Synoptic instrument() {
		return instrument;
	}
	
	/**
	 * @return An object that will accept synoptic names and request their deletion.
	 */
	public Writable<Collection<String>> deleteSynoptics() {
		return variables.synopticsDeleter;
	}

	/**
	 * @return An object that will request the current synoptic is saved.
	 */
	public SynopticWriter saveSynoptic() {
		return setCurrentSynoptic;
	}
	
	/**
	 * Set the current synoptic to one that matches a given description.
	 * 
	 * @param description The description of the synoptic to be set.
	 */
	public void setSynopticFromDescription(SynopticDescription description) {
		try {
			instrument = getInstrument(description);
		} catch (Exception ex) {
			LOG.error("Creating synoptic support object from xml: " + ex.getLocalizedMessage(), ex);
			instrument = getInstrument(SynopticDescription.getEmptySynopticDescription());
			throw ex;
		}
	}
	
	public InstrumentNavigationGraph instrumentGraph() {
		return new InstrumentNavigationGraph(instrument);
	}
	
	private ObservableSynoptic getInstrument(SynopticDescription description) {
		return new ObservableSynoptic(description, variables);
	}
}
