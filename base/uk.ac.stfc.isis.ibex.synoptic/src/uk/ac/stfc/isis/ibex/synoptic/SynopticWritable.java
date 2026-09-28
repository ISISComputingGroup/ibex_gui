
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

import java.io.IOException;
import java.util.function.Function;

import com.google.common.base.Strings;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.XMLUtil;
import uk.ac.stfc.isis.ibex.epics.observing.Observable;
import uk.ac.stfc.isis.ibex.epics.writing.OnCanWriteChangeListener;
import uk.ac.stfc.isis.ibex.epics.writing.TransformingWritable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.model.SettableUpdatedValue;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.SynopticDescription;


/**
 * This class is responsible for writing the synoptic data back to the BlockServer.
 *
 */
public class SynopticWritable extends TransformingWritable<SynopticDescription, String> {
	
    private SettableUpdatedValue<Boolean> canSave = new SettableUpdatedValue<>();
    
    private OnCanWriteChangeListener canWriteListener = canWrite -> canSave.setValue(canWrite);
    
    /**
     * Create a function that will convert a synoptic structure into xml, checking it against the provided schema.
     * @param schema The schema to check against
     * @return A function that will do the conversion
     */
    private static Function<SynopticDescription, String> getConverter(String schema) {
        return value -> {
            // Converts the raw synoptic description into XML
            if (Strings.isNullOrEmpty(schema)) {
                Synoptic.LOG.debug("Synoptic schema not found, attempting to save anyway.");
            }
            try {
                return XMLUtil.toXml(value, SynopticDescription.class, schema);
            } catch (IOException e) {
                throw new ConversionException(e.getMessage());
            }
        };
    }
    
    /**
     * Create a new synoptic writer that converts a synoptic to xml, checks this
     * against the supplied schema and writes it to the supplied destination.
     * 
     * @param destination
     *            The destination to write the synoptic to.
     * @param schema
     *            The schema to check the synoptic against.
     */
    public SynopticWritable(Writable<String> destination, Observable<String> schema) {
        super(destination, getConverter(schema.getValue()));

        destination.addOnCanWriteChangeListener(canWriteListener);
	}
	
    /**
     * @return An updating Boolean that specifies whether the synoptic can be
     *         written to or not.
     */
	public UpdatedValue<Boolean> canSave() {
		return canSave;
	}


    @Override
    public void close() {
        destination.ifPresent(dest -> addOnCanWriteChangeListener(canWriteListener));
    }
}
