
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

import uk.ac.stfc.isis.ibex.epics.writing.TransformingWriter;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.model.SettableUpdatedValue;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.SynopticDescription;
import uk.ac.stfc.isis.ibex.synoptic.xml.XMLUtil;


/**
 * This class is responsible for writing the synoptic data back to the BlockServer.
 *
 */
public class SynopticWriter extends TransformingWriter<SynopticDescription, String> {
	
	private SettableUpdatedValue<Boolean> canSave = new SettableUpdatedValue<>();

	public SynopticWriter(Writable<String> destination) {
		writeTo(destination);
		canSave.setValue(destination.canWrite());
		destination.subscribe(this);
	}
	
	@Override
	protected String transform(SynopticDescription value) {
		// Converts the raw synoptic description into XML
		try {
            return XMLUtil.toXml(value, SynopticDescription.class, null);
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
	
	public UpdatedValue<Boolean> canSave() {
		return canSave;
	}
}
