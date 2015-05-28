package uk.ac.stfc.isis.ibex.configserver.displaying;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import uk.ac.stfc.isis.ibex.configserver.ConfigServer;
import uk.ac.stfc.isis.ibex.configserver.Displaying;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.configuration.Group;
import uk.ac.stfc.isis.ibex.epics.observing.ClosableCachingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.observing.TransformingObservable;

import com.google.common.base.Strings;

public class DisplayConfiguration 
	extends TransformingObservable<Configuration, DisplayConfiguration> implements Displaying {

	private String name;
	private String description;
	private final List<DisplayGroup> groups = new ArrayList<>();
	private Configuration config;

	private final ConfigServer configServer;
	
	public DisplayConfiguration(
			ClosableCachingObservable<Configuration> config, 
			ConfigServer configServer) {
		this.configServer = configServer;
		setSource(config);
	}
	
	@Override
	protected DisplayConfiguration transform(Configuration value) {
		this.config = value;
		name = value.name();
		description = value.description();
		setGroups(value.getGroups());
		
		return this;
	}

	@Override
	public InitialiseOnSubscribeObservable<DisplayConfiguration> displayCurrentConfig() {
		return new InitialiseOnSubscribeObservable<>(this);
	}
	
	public String name() {
		return Strings.nullToEmpty(name);
	}

	public String description() {
		return Strings.nullToEmpty(description);
	}

	public Collection<DisplayGroup> groups() {
		return new ArrayList<>(groups);
	}
		
	protected void setGroups(Collection<Group> configGroups) {
		groups.clear();
		for (Group group : configGroups) {
			groups.add(new DisplayGroup(group, config.getBlocks(), configServer));
		}
	}
}
