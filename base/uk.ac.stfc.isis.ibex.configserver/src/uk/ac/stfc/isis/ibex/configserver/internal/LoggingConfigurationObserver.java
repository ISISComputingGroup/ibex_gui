package uk.ac.stfc.isis.ibex.configserver.internal;

import org.apache.logging.log4j.Logger;

import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.observing.LoggingObserver;
import uk.ac.stfc.isis.ibex.json.JsonSerialisingConverter;

public class LoggingConfigurationObserver extends LoggingObserver<Configuration> {

	public LoggingConfigurationObserver(Logger log, String id) {
		super(log, id);
	}

	@Override
	public void onValue(Configuration value) {
		super.onValue(value);
		try {
			log.info(id + " " + new JsonSerialisingConverter<>(Configuration.class).convert(value).toString());
		} catch (ConversionException e) {
			log.info(id + " " + e.toString());
		}
	}
}
