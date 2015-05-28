package uk.ac.stfc.isis.ibex.configserver.editing;

import java.util.Collection;
import java.util.Collections;

import uk.ac.stfc.isis.ibex.configserver.ConfigServer;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.internal.IocDescriber;
import uk.ac.stfc.isis.ibex.epics.adapters.TextUpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.epics.observing.CachingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ClosableCachingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.TransformingObservable;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

/*
 * Build a full configuration suitable for editing. 
 */
public class ObservableEditableConfiguration 
	extends TransformingObservable<Configuration, EditableConfiguration> implements IocDescriber {

	private final ConfigServer configServer;

	public ObservableEditableConfiguration(
			ClosableCachingObservable<Configuration> config,
			ConfigServer configServer) {
		this.configServer = configServer;
		setSource(config);
	}
	
	@Override
	protected EditableConfiguration transform(Configuration value) {
		return new EditableConfiguration(
				value, 
				valueOrEmptyCollection(configServer.iocs()), 
				valueOrEmptyCollection(configServer.components()), 
				valueOrEmptyCollection(configServer.pvs()),
				this);
	}

	@Override
	public UpdatedValue<String> getDescription(String iocName) {
		return new TextUpdatedObservableAdapter(configServer.iocDescription(iocName));
	}
	
	private static <T> Collection<T> valueOrEmptyCollection(CachingObservable<Collection<T>> collection) {
		return collection.value() != null ? collection.value() : Collections.<T>emptyList();
	}
}
