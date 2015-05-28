package uk.ac.stfc.isis.ibex.ui.widgets.observable;

import uk.ac.stfc.isis.ibex.epics.adapters.TextUpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.epics.observing.CachingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.pv.Closable;
import uk.ac.stfc.isis.ibex.epics.writing.BaseWriter;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.model.SettableUpdatedValue;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;


/**
 * Links a read PV and a writable PV.
 *
 */
public class WritableObservableAdapter implements Closable {
	
	private final UpdatedValue<String> text;
	private final SettableUpdatedValue<Boolean> canSetText;
	private final BaseWriter<String, String> writer = new BaseWriter<String, String>() {
		@Override
		public void write(String value) {
			writeToWritables(value);
		}
		
		public void onCanWriteChanged(boolean canWrite) {
			canSetText.setValue(canWrite);
		}
	};

	private final Subscription writerSubscription;
	private final Subscription writableSubscription;	
	
	/**
	 * @param writable The object for writing to a PV
	 * @param observable The object for observing a PV
	 */
	public WritableObservableAdapter(Writable<String> writable, CachingObservable<String> observable) {
		text = new TextUpdatedObservableAdapter(observable);
		canSetText = new SettableUpdatedValue<>();
		canSetText.setValue(writable.canWrite());
		
		writableSubscription = writer.writeTo(writable);
		writerSubscription = writable.subscribe(writer);
	}
	
	public UpdatedValue<String> text() {
		return text;
	}
	
	public void setText(String text) {
		if (text != writer.lastWritten()){
			writer.write(text);
		}
	}
	
	public UpdatedValue<Boolean> canSetText() {
		return canSetText;
	}

	@Override
	public void close() {
		writerSubscription.cancel();
		writableSubscription.cancel();
	}
}
