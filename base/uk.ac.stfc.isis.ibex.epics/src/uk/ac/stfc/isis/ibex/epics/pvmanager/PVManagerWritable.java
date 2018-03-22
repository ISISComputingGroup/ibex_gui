
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

package uk.ac.stfc.isis.ibex.epics.pvmanager;

import java.io.IOException;

import org.diirt.datasource.PVManager;
import org.diirt.datasource.PVWriter;
import org.diirt.datasource.PVWriterEvent;
import org.diirt.datasource.PVWriterListener;
import org.diirt.datasource.expression.WriteExpressionImpl;

import uk.ac.stfc.isis.ibex.epics.pv.PVInfo;
import uk.ac.stfc.isis.ibex.epics.pv.WritablePV;
import uk.ac.stfc.isis.ibex.epics.writing.WriteException;

/**
 * A class for writing to a PV via PVManager.
 *
 * @param <T> the type of the PV
 */
public class PVManagerWritable<T> extends WritablePV<T> {

	private final WriteExpressionImpl<T> writeExpression;
	private final PVWriter<T> pv;
	
	private final PVWriterListener<T> observingListener = new PVWriterListener<T>() {
		@Override
		public void pvChanged(PVWriterEvent<T> evt) {
			if (pv == null) {
				return;
			}
			
			if (evt.isConnectionChanged()) {
				canWriteChanged(pv.isWriteConnected());
			}
			
			if (evt.isExceptionChanged()) {
				error(pv.lastWriteException());
			}
			
			if (evt.isWriteFailed()) {
				error(new WriteException("Write failed"));
			}
		}
	};
	
	public PVManagerWritable(PVInfo<T> info) {
		super(info);
		writeExpression = new WriteExpressionImpl<T>(info.address());		
		pv = PVManager
				.write(writeExpression)
				.writeListener(observingListener)
				.async();	
	}
	
	@Override
	public void write(T value) throws IOException {
	    
	    if (!canWrite()) {
	        String message = "Can't write to PV '" + writeExpression.getName() + "'.";
	        
	        if (pv.lastWriteException() != null) {
	            message += " Caused by: " + pv.lastWriteException().toString();
	            throw new IOException(message, pv.lastWriteException());
	        } else {
	            throw new IOException(message);
	        }
	    }
	    
		pv.write(value);
	}

	@Override
	public void close() {
		pv.close();
	}
}
