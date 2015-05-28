package uk.ac.stfc.isis.ibex.ui.banner.indicators;

import org.eclipse.swt.graphics.Color;

import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.InitialisableObserver;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.pv.Closable;
import uk.ac.stfc.isis.ibex.model.SettableUpdatedValue;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

public class IndicatorStateObserver <T> implements Closable {

	private final InitialisableObserver<T> sourceObserver = new BaseObserver<T>() {
		@Override
		public void onValue(T value) {
			setState(value);
		}

		@Override
		public void onError(Exception e) {
			setState(null);
		}

		@Override
		public void onConnectionChanged(boolean isConnected) {
			if (!isConnected) {
				setState(null);
			}
		}
	};
	
	protected Subscription sourceSubscription;
	protected final SettableUpdatedValue<String> text;
	protected final SettableUpdatedValue<Color> color;
	protected final SettableUpdatedValue<Boolean> bool;	
	protected final IndicatorViewStateConverter<T> viewConverter;
	
	public IndicatorStateObserver(InitialiseOnSubscribeObservable<T> source, IndicatorViewStateConverter<T> viewConverter) {
		this.viewConverter = viewConverter;
		text = new SettableUpdatedValue<>();
		color = new SettableUpdatedValue<>();
		bool = new SettableUpdatedValue<>();
		sourceSubscription = source.subscribe(sourceObserver);
	}	
	
	public UpdatedValue<String> text() {
		return text;
	}
	
	public UpdatedValue<Color> color() {
		return color;
	}	
	
	public UpdatedValue<Boolean> bool() {
		return bool;
	}	
	
	@Override
	public void close() {
		sourceSubscription.cancel();
	}	
	
	protected void setState(T value) {
		viewConverter.setState(value);
		text.setValue(viewConverter.getName());
		color.setValue(viewConverter.color());
		bool.setValue(viewConverter.toBool());
	}
	
}
