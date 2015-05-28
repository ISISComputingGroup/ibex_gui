package uk.ac.stfc.isis.ibex.ui.ioccontrol.table;

import org.eclipse.core.databinding.conversion.Converter;

import uk.ac.stfc.isis.ibex.ui.ioccontrol.StateLabelProvider;

public class StatusTextConverter extends Converter {

	public StatusTextConverter() {
		super(Boolean.class, String.class);
	}

	@Override
	public Object convert(Object fromBoolean) {
		Boolean from = (Boolean) fromBoolean;
		if (from == null) {
			return "Unknown";
		}
		
		return from ? StateLabelProvider.TEXT_RUNNING : StateLabelProvider.TEXT_NOT_RUNNING;
	}

}
