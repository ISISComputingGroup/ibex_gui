package uk.ac.stfc.isis.ibex.epics.adapters;

import uk.ac.stfc.isis.ibex.epics.conversion.Converter;
import uk.ac.stfc.isis.ibex.epics.observing.ConvertingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

public class ModelAdapter extends Closer {
	protected <T> InitialiseOnSubscribeObservable<String> convert(InitialiseOnSubscribeObservable<T> observable, Converter<T, String> converter) {
		ConvertingObservable<T, String> converted = new ConvertingObservable<>(observable, converter);
		return registerForClose(new InitialiseOnSubscribeObservable<>(converted));
	}
	
	protected <T> UpdatedValue<T> adapt(InitialiseOnSubscribeObservable<T> observable) {
		UpdatedObservableAdapter<T> adapted = new UpdatedObservableAdapter<>(observable);
		return registerForClose(adapted);
	}
}
