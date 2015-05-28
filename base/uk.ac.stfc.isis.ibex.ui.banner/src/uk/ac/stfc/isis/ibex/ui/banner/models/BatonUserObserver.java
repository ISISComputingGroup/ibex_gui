package uk.ac.stfc.isis.ibex.ui.banner.models;

import org.eclipse.swt.graphics.Color;

import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.InitialisableObserver;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.pv.Closable;
import uk.ac.stfc.isis.ibex.model.SettableUpdatedValue;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.ui.banner.indicators.IndicatorColours;

public class BatonUserObserver implements Closable{
	private Subscription subscription;
	
	private final InitialisableObserver<String> observer = new BaseObserver<String>() {
		@Override
		public void onValue(String value) {
			checkSelf(value);
		}

		@Override
		public void onError(Exception e) {
			setUnknown();
		}

		@Override
		public void onConnectionChanged(boolean isConnected) {
			if (!isConnected) {
				setUnknown();
			}
		}
	};	
	
	private static final String UNKNOWN = "UNKNOWN";
	private static final String NONE = "NONE";
	private final String SELF;
	
	protected final SettableUpdatedValue<String> text;
	protected final SettableUpdatedValue<Color> color;
	
	public BatonUserObserver(
			InitialiseOnSubscribeObservable<String> observable, String self) {
		text = new SettableUpdatedValue<>();
		color = new SettableUpdatedValue<>();
		subscription = observable.subscribe(observer);
		SELF = self;
	}
	
	@Override
	public void close() {
		subscription.cancel();
	}
	
	private void checkSelf(String value) 
	{
		String user = value.isEmpty() ? NONE : value;
		
		text.setValue("Current user: " +  user);
		
		if (value.equals(SELF))
		{
			color.setValue(IndicatorColours.GREEN);
		} else {
			color.setValue(IndicatorColours.BLACK);
		}
	}
	
	private void setUnknown()
	{
		text.setValue("Current user: " + UNKNOWN);
		color.setValue(IndicatorColours.RED);
	}
	
	public UpdatedValue<String> text()
	{
		return text;
	}

	public UpdatedValue<Color> color()
	{
		return color;
	}
	
}
