package uk.ac.stfc.isis.ibex.dae.actions;

import uk.ac.stfc.isis.ibex.dae.DaeRunState;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.InitialisableObserver;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.pv.Closable;
import uk.ac.stfc.isis.ibex.epics.writing.BaseWriter;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.model.Action;

public abstract class DaeAction extends Action implements Closable {
		
	private final BaseWriter<String, String> actionWriter = new BaseWriter<String, String>() {

		@Override
		public void write(String value) {
			writeToWritables(value);
		}
		
		@Override
		public void onCanWriteChanged(boolean canWrite) {
			super.onCanWriteChanged(canWrite);
			setCanWrite(canWrite);
		}
	};
			
	private final InitialisableObserver<Boolean> transitionObserver = new BaseObserver<Boolean>() {
		@Override
		public void onConnectionChanged(boolean isConnected) {
			if (!isConnected) {
				setInTransition(true);
			}
		}

		@Override
		public void onValue(Boolean value) {
			setInTransition(value);
		}

		@Override
		public void onError(Exception e) {
			setInTransition(true);
		}
	};

	private InitialisableObserver<DaeRunState> runStateObserver = new BaseObserver<DaeRunState>() {

		@Override
		public void onValue(DaeRunState value) {
			setRunState(value);
		}

		@Override
		public void onError(Exception e) {
			setRunState(DaeRunState.UNKNOWN);
		}

		@Override
		public void onConnectionChanged(boolean isConnected) {
			setRunState(DaeRunState.UNKNOWN);
		}
	};

	private Subscription runStateSubscription;
	private final Subscription transitionSubscription;
	private final Subscription targetSubscribtion;
	private final Subscription writerSubscription;
	
	private boolean inTransition;
	private boolean canWrite;
	private DaeRunState runState = DaeRunState.UNKNOWN;
	
	public DaeAction(
			Writable<String> target, 
			InitialiseOnSubscribeObservable<Boolean> inStateTransition,
			InitialiseOnSubscribeObservable<DaeRunState> runState) {
		
		targetSubscribtion = target.subscribe(actionWriter);
		writerSubscription = actionWriter.writeTo(target);
		transitionSubscription = inStateTransition.subscribe(transitionObserver);
		runStateSubscription = runState.subscribe(runStateObserver);
	}

	@Override
	public void execute() {
		actionWriter.write("1");
	}

	@Override
	public void close() {
		runStateSubscription.cancel();
		transitionSubscription.cancel();
		targetSubscribtion.cancel();
		writerSubscription.cancel();
	}
	
	protected abstract boolean allowed(DaeRunState runState);
	
	private void setCanWrite(boolean canWrite) {
		this.canWrite = canWrite;
		setCanExecute(canExecute());
	}
	
	private void setInTransition(boolean inTransition) {
		this.inTransition = inTransition;
		setCanExecute(canExecute());
	}
	
	private void setRunState(DaeRunState runState) {
		this.runState = runState;
		setCanExecute(canExecute());
	}
	
	private boolean canExecute() {
		return canWrite && !inTransition && allowed(runState);
	}
}
