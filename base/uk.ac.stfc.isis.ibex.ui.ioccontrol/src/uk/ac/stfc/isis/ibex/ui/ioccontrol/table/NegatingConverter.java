package uk.ac.stfc.isis.ibex.ui.ioccontrol.table;

import org.eclipse.core.databinding.conversion.Converter;

public class NegatingConverter extends Converter {
	
	public NegatingConverter() {
		super(Boolean.class, Boolean.class);
	}

	@Override
	public Object convert(Object fromObject) {
		Boolean from = (Boolean) fromObject;
		if (from == null) {
			return false;
		}
		
		return !from;
	}
}
