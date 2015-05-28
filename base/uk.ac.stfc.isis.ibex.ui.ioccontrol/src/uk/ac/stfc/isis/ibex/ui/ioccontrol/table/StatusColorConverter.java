package uk.ac.stfc.isis.ibex.ui.ioccontrol.table;

import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.swt.graphics.Color;

import uk.ac.stfc.isis.ibex.ui.ioccontrol.StateLabelProvider;

public class StatusColorConverter extends Converter {

	public StatusColorConverter() {
		super(Boolean.class, Color.class);
	}

	@Override
	public Object convert(Object fromBoolean) {
		Boolean isRunning = (Boolean) fromBoolean;
		return isRunning ? StateLabelProvider.COLOR_RUNNING : StateLabelProvider.COLOR_STOPPED;
	}
}
