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
package uk.ac.stfc.isis.ibex.log;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class Log extends AbstractUIPlugin {
    private static Log instance;
	private static BundleContext context;

    public static Log getInstance() { 
    	return instance; 
    }
    
	public static Log getDefault() {
		return instance;
	}
	
    private final LogModel model;
    private LogCounter counter;
	
	public Log() {
		super();
		instance = this;
		model = new LogModel();
		counter = new LogCounter();
		model.addMessageConsumer(counter);
		model.start();
	}
    
	public ILogMessageProducer producer() {
		return model;
	}

	public LogCounter getCounter() {
		return counter;
	}
	
	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Log.context = bundleContext;
	}

	public void stop(BundleContext bundleContext) throws Exception {
		Log.context = null;
		model.stop();
	}

	public void clearMessages() {
		model.clearMessages();
	}
}
