
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

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import com.google.common.base.Strings;

import uk.ac.stfc.isis.ibex.epics.conversion.XMLUtil;
import uk.ac.stfc.isis.ibex.epics.observing.Observable;
import uk.ac.stfc.isis.ibex.epics.writing.TransformingWriter;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.model.SettableUpdatedValue;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.SynopticDescription;


/**
 * This class is responsible for writing the synoptic data back to the BlockServer.
 *
 */
public class SynopticWriter extends TransformingWriter<SynopticDescription, String> {
	
	private SettableUpdatedValue<Boolean> canSave = new SettableUpdatedValue<>();
    private final Observable<String> schema;

    /**
     * Create a new synoptic writer that converts a synoptic to xml, checks this
     * against the supplied schema and writes it to the supplied destination.
     * 
     * @param destination
     *            The destination to write the synoptic to.
     * @param schema
     *            The schema to check the synoptic against.
     */
    public SynopticWriter(Writable<String> destination, Observable<String> schema) {
		writeTo(destination);
		canSave.setValue(destination.canWrite());
		destination.subscribe(this);
        this.schema = schema;
	}
	

	@Override
	protected String transform(SynopticDescription value) {
		// Converts the raw synoptic description into XML
        if (Strings.isNullOrEmpty(schema.getValue())) {
            Synoptic.LOG.debug("Synoptic schema not found, attempting to save anyway.");
        }
		try {
            return XMLUtil.toXml(value, SynopticDescription.class, schema.getValue());
		} catch (JAXBException | SAXException e) {
			onError(e);
		}
		
		return null;
	}

	@Override
	public void onCanWriteChanged(boolean canWrite) {
		super.onCanWriteChanged(canWrite);
		canSave.setValue(canWrite);
	}
	
    /**
     * @return An updating Boolean that specifies whether the synoptic can be
     *         written to or not.
     */
	public UpdatedValue<Boolean> canSave() {
		return canSave;
	}
}
