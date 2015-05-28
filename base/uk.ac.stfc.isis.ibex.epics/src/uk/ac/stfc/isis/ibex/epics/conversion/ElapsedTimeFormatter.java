package uk.ac.stfc.isis.ibex.epics.conversion;

import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

public class ElapsedTimeFormatter extends Converter<Long, String> {
		
	@Override
	public String convert(Long elapsedSeconds) throws ConversionException {
		Period period = new Period(1000 * elapsedSeconds);
		PeriodFormatter formatter = new PeriodFormatterBuilder()
			.appendHours().appendSuffix(" hours ")
			.appendMinutes().appendSuffix(" min ")
			.appendSeconds().appendSuffix(" s")
			.toFormatter();
		
		return formatter.print(period);
	}
}