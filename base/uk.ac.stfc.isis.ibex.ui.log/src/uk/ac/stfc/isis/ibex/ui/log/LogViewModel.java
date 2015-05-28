/*
 * Copyright (C) 2013-2014 Research Councils UK (STFC)
 *
 * This file is part of the Instrument Control Project at ISIS.
 *
 * This code and information are provided "as is" without warranty of any 
 * kind, either expressed or implied, including but not limited to the
 * implied warranties of merchantability and/or fitness for a particular 
 * purpose.
 */
package uk.ac.stfc.isis.ibex.ui.log;

import uk.ac.stfc.isis.ibex.log.ILogMessageProducer;
import uk.ac.stfc.isis.ibex.model.ModelObject;

public class LogViewModel extends ModelObject {
	private ILogMessageProducer producer;
	
	public ILogMessageProducer getMessageProducer() {
		return producer;
	}

	public void bind(ILogMessageProducer producer) {
		this.producer = producer;
	}
}
