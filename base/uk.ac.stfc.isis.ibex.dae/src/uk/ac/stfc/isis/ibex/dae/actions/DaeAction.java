
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2015 Science & Technology Facilities Council.
* All rights reserved.
*
* This program is distributed in the hope that it will be useful.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0 which accompanies this distribution.
* EXCEPT AS EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM 
* AND ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES 
* OR CONDITIONS OF ANY KIND.  See the Eclipse Public License v1.0 for more details.
*
* You should have received a copy of the Eclipse Public License v1.0
* along with this program; if not, you can obtain a copy from
* https://www.eclipse.org/org/documents/epl-v10.php or 
* http://opensource.org/licenses/eclipse-1.0.php
*/

package uk.ac.stfc.isis.ibex.dae.actions;

import uk.ac.stfc.isis.ibex.dae.DaeRunState;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Observer;
import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.pv.Closable;
import uk.ac.stfc.isis.ibex.epics.writing.OnCanWriteChangeListener;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.model.Action;

/**
 * An abstract class for sending actions to the DAE to move between states.
 */
public abstract class DaeAction extends Action implements Closable {
	private final OnCanWriteChangeListener canWriteListener = canWrite -> setCanWrite(canWrite);
    private final Writable<String> target;
			
    private final Observer<Boolean> transitionObserver = new BaseObserver<Boolean>() {
		@Override
		public void onConnectionStatus(boolean isConnected) {
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
            IsisLog.getLogger(this.getClass()).warn("Exception occured in transition observer.", e);
			setInTransition(true);
		}
	};

    private Observer<DaeRunState> runStateObserver = new BaseObserver<DaeRunState>() {

		@Override
		public void onValue(DaeRunState value) {
			setRunState(value);
		}

		@Override
		public void onError(Exception e) {
            IsisLog.getLogger(this.getClass()).warn("Exception occured in run state observer.", e);
			setRunState(DaeRunState.UNKNOWN);
		}

		@Override
		public void onConnectionStatus(boolean isConnected) {
            if (!isConnected) {
                setRunState(DaeRunState.UNKNOWN);
            }
		}
	};

	private Subscription runStateSubscription;
	private final Subscription transitionSubscription;
	
	private boolean inTransition;
	private boolean canWrite;
	private DaeRunState runState = DaeRunState.UNKNOWN;
	
    /**
     * Constructor for the class. Will add observers to the provided PVs.
     * 
     * @param target
     *            The PV to write to to change the state.
     * @param inStateTransition
     *            An observable to check the DAE is not transitioning.
     * @param runState
     *            An observable on the current state of the PV.
     */
	public DaeAction(
			Writable<String> target, 
			ForwardingObservable<Boolean> inStateTransition,
			ForwardingObservable<DaeRunState> runState) {    
	    this.target = target;
		target.addOnCanWriteChangeListener(canWriteListener);
		transitionSubscription = inStateTransition.subscribe(transitionObserver);
		runStateSubscription = runState.subscribe(runStateObserver);
	}

	@Override
	public synchronized void execute() {
	    target.uncheckedWrite("1");
	}

	@Override
	public synchronized void close() {
		runStateSubscription.cancelSubscription();
		transitionSubscription.cancelSubscription();
		target.removeOnCanWriteChangeListener(canWriteListener);
	}
	
    /**
     * A method to check that the transition is allowed from the specified
     * state.
     * 
     * @param runState
     *            The state we are transitioning from.
     * @return True if the transition is allowed using this action.
     */
	protected abstract boolean allowed(DaeRunState runState);
	
	private synchronized void setCanWrite(boolean canWrite) {
		this.canWrite = canWrite;
		setCanExecute(canExecute());
	}
	
	private synchronized void setInTransition(boolean inTransition) {
		this.inTransition = inTransition;
		setCanExecute(canExecute());
	}
	
	private synchronized void setRunState(DaeRunState runState) {
		this.runState = runState;
		setCanExecute(canExecute());
	}
	
	private synchronized boolean canExecute() {
		return canWrite && !inTransition && allowed(runState);
	}
}
