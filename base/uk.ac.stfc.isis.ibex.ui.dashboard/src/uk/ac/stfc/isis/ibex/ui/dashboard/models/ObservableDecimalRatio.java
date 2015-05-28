package uk.ac.stfc.isis.ibex.ui.dashboard.models;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import uk.ac.stfc.isis.ibex.epics.observing.ClosableCachingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Pair;
import uk.ac.stfc.isis.ibex.epics.observing.TransformingObservable;

public class ObservableDecimalRatio extends TransformingObservable<Pair<Number, Number>, String> {

	private static NumberFormat FORMAT = DecimalFormat.getInstance(Locale.ENGLISH);
	static {
		FORMAT.setMaximumIntegerDigits(3);
		FORMAT.setMaximumFractionDigits(3);
	}
	
	public ObservableDecimalRatio(ClosableCachingObservable<Pair<Number, Number>> source) {
		setSource(source);
	}
	
	@Override
	protected String transform(Pair<Number, Number> value) {		
		if (value.first == null || value.second == null) {
			return "Unknown";
		}
		
		return format(value.first) + " / " + format(value.second);
	}

	private String format(Number value) {
		return FORMAT.format(value);
	}
}
