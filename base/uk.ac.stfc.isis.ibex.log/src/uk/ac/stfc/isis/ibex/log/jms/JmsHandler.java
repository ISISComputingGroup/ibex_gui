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
package uk.ac.stfc.isis.ibex.log.jms;

import javax.jms.Connection;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.logging.log4j.Logger;
import org.eclipse.jface.preference.IPreferenceStore;

import uk.ac.stfc.isis.ibex.log.ILogMessageConsumer;
import uk.ac.stfc.isis.ibex.log.Log;
import uk.ac.stfc.isis.ibex.log.message.LogMessage;
import uk.ac.stfc.isis.ibex.log.preferences.PreferenceConstants;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.model.ModelObject;


/**
 * Handles the connection to a Java Message Service (JMS) Server.
 * Automatically attempts to reestablish connection if it is dropped.
 */
public class JmsHandler extends ModelObject implements Runnable {
	private static final Logger LOG = IsisLog.getLogger(JmsHandler.class);
	
	private static final String PROTOCOL = "tcp:";
	
	/** JMS Server URL */
	private String jmsUrl;
	
	/** JMS subscription Topic */
	private String jmsTopic;

	/** Server name, <code>null</code> if not connected */
	private String jmsServer = null;

	/** 'run' flag to thread */
	private volatile boolean run = true;

	/** JMS Connection */
	private Connection connection;

	/** JMS Session */
	private Session session;

	/** Indicates whether the Handler is currently connected to the JMS server */
	private boolean connectedToJms;

	/** Receives messages from the JMS server */ 
	private MessageConsumer jmsConsumer;
	
	/** Converts the XML messages received via JMS to LogMessages */
	private XmlLogMessageParser xmlParser;
	
	/** The recipient of any parsed messages */
	private ILogMessageConsumer messageLogConsumer;
	
	/** Indicates whether a warning message has been issued since last time server was connected */ 
	private boolean connectionWarnFlag;
	

	public JmsHandler() {		
		jmsUrl = getPreferenceUrl();
		jmsTopic = getPreferenceTopic();
		xmlParser = new XmlLogMessageParser();
	}
	
	public void setLogMessageConsumer(ILogMessageConsumer messageConsumer) {
		this.messageLogConsumer = messageConsumer; 
	}

	public synchronized boolean isConnected() {
		return jmsServer != null && connectedToJms;
	}
	
	public synchronized void stop() {
		run = false;
	}

	/** Establish connection with JMS server and listen for messages to forward on to consumer */
	public void run() {
		while (run) {
			// Check connection settings - if they've changed, disconnect, and connect
			//	using new settings
			String oldUrl = jmsUrl;
			String oldTopic = jmsTopic;
			jmsUrl = getPreferenceUrl();
			jmsTopic = getPreferenceTopic();
			
			if (!oldUrl.equals(jmsUrl) || !oldTopic.equals(jmsTopic)) {
				disconnect();
			}
			
			if (isConnected()) 	{
				try {
					TextMessage message = (TextMessage)jmsConsumer.receive(50);
					if (message != null) {
						messageLogConsumer.newMessage(parseLogMessage(message));
					}
				} catch (JMSException e) {
					// Do nothing; exception will be caught be exception listener (see connect())
				}
			} else {
				// If not connected or if connection is dropped, establish connection with JMS
				try {
					connect();
					
					LOG.info("Connected to JMS server: " + jmsUrl);
					// Use URL as server name.
					jmsServer = jmsUrl;
					setJmsConnectionStatus(true);
				} catch (Exception ex) {
					if (!connectionWarnFlag) {
						LOG.warn("Problem connecting to JMS server. Will auto-attempt reconnection.");
						connectionWarnFlag = true;
						setJmsConnectionStatus(false);
					}

					sleep(1000);
				}
			}
		}

		disconnect();
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) { 
			e.printStackTrace(); 
		}
	}

	private LogMessage parseLogMessage(TextMessage message) throws JMSException {
		return xmlParser.xmlToLogMessage(message.getText());
	}
	
	private synchronized void setJmsConnectionStatus(boolean connected) {
		firePropertyChange("connection", connectedToJms , connectedToJms = connected);
	}
	
	/** Connect to JMS */
	private void connect() throws JMSException {	
		createJmsConnection();	
		configureJmsConnection();
		
		// When server is unavailable, we'll hang in here
		connection.start();
		
		connectionWarnFlag = false;
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		Topic topic = session.createTopic(jmsTopic);
		jmsConsumer = session.createConsumer(topic);
	}

	private void configureJmsConnection() throws JMSException {
		connection.setExceptionListener(new ExceptionListener() {
			@Override
			public void onException(final JMSException ex) {
				LOG.warn("Lost connection to JMS server: " + jmsUrl + ". Will attempt auto reconnection.");
				setJmsConnectionStatus(false);
			}
		});		
	}

	private void createJmsConnection() throws JMSException {
		connection = getConnectionFactory(jmsUrl).createConnection();
	}

	/** Disconnect from JMS - free resources */
	private void disconnect() {
		try {
			if (jmsConsumer != null) {
				jmsConsumer.close();
				jmsConsumer = null;
			}
			
			if (session != null) {
				session.close();
				session = null;

			}
			
			if (connection != null) {
				connection.close();
				connection = null;
			}
		} catch (Exception ex) {
			LOG.error("JMS shutdown error: " + ex);
		} finally {
			LOG.info("Successfully disconnected from JMS: " + jmsServer);
			
			jmsServer = null;
			setJmsConnectionStatus(false);	
		}
	}
	
	private String getPreferenceUrl() {
		IPreferenceStore preferenceStore = Log.getDefault().getPreferenceStore();
		String address = preferenceStore.getString(PreferenceConstants.P_JMS_ADDRESS);
		String port = preferenceStore.getString(PreferenceConstants.P_JMS_PORT);
		
		if (address.indexOf("//") != 0) {
			address = "//" + address;
		}
		
		return PROTOCOL + address + ":" + port;
	}
	
	private String getPreferenceTopic() {
		IPreferenceStore preferenceStore = Log.getDefault().getPreferenceStore();
		return preferenceStore.getString(PreferenceConstants.P_JMS_TOPIC);
	}
	
	private ActiveMQConnectionFactory getConnectionFactory(String url) {
		return new ActiveMQConnectionFactory(
				ActiveMQConnection.DEFAULT_USER, 
		  		ActiveMQConnection.DEFAULT_PASSWORD, 
		  		url);
	}
}
