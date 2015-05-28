package uk.ac.stfc.isis.ibex.configserver.displaying;

import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.instrument.Instrument;

import com.google.common.base.Strings;

public class DisplayBlock extends Block {

	private final String blockServerAlias;

	private String value;
	private String description;
	
	private final BaseObserver<String> valueAdapter = new BaseObserver<String>() {	
		@Override
		public void onValue(String value) {
			setValue(value);
		}

		@Override
		public void onError(Exception e) {
			setValue("error");
		}

		@Override
		public void onConnectionChanged(boolean isConnected) {
			if (!isConnected) {
				setValue("disconnected");
			}
		}
	};
	
	private final BaseObserver<String> descriptionAdapter = new BaseObserver<String>() {	
		@Override
		public void onValue(String value) {
			setDescription(value);
		}

		@Override
		public void onError(Exception e) {
			setDescription("No description available");
		}

		@Override
		public void onConnectionChanged(boolean isConnected) {
			if (!isConnected) {
				setDescription("No description available");
			}
		}
	};
	
	public DisplayBlock(
			Block block,
			InitialiseOnSubscribeObservable<String> valueSource,
			InitialiseOnSubscribeObservable<String> descriptionSource,
			String blockServerAlias) {
		super(block);
		this.blockServerAlias = blockServerAlias;
				
		valueSource.subscribe(valueAdapter);
		descriptionSource.subscribe(descriptionAdapter);
	}
	
	public String getValue() {
		return value;
	}
	
	public String getDescription() {
		return description;
	}

	public String blockServerAlias() {
		return Instrument.getInstance().currentInstrument().pvPrefix() + blockServerAlias;
	}
	
	private synchronized void setValue(String value) {
		firePropertyChange("value", this.value, this.value = Strings.nullToEmpty(value));
	}
	
	private synchronized void setDescription(String description) {
		firePropertyChange("description", this.description, this.description = Strings.nullToEmpty(description));
	}
}
