package uk.ac.stfc.isis.ibex.configserver.internal;

import uk.ac.stfc.isis.ibex.model.UpdatedValue;

public interface IocDescriber {
	UpdatedValue<String> getDescription(String iocName);
}
