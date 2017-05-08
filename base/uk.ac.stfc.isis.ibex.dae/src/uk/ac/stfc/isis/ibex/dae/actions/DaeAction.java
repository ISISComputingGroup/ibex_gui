
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

import java.io.IOException;

import uk.ac.stfc.isis.ibex.dae.DaeRunState;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Observer;
import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.pv.Closable;
import uk.ac.stfc.isis.ibex.epics.writing.BaseWriter;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.model.Action;

public abstract class DaeAction extends Action implements Closable {
		
	private final BaseWriter<String, String> actionWriter = new BaseWriter<String, String>() {

		@Override
		public void write(String value) throws IOException {
            writeToWritables(value);
		}
		
		@Override
		public void onCanWriteChanged(boolean canWrite) {
			super.onCanWriteChanged(canWrite);
			setCanWrite(canWrite);
		}
	};
			
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
			setRunState(DaeRunState.UNKNOWN);
		}

		@Override
		public void onConnectionStatus(boolean isConnected) {
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
			ForwardingObservable<Boolean> inStateTransition,
			ForwardingObservable<DaeRunState> runState) {
		
		targetSubscribtion = target.subscribe(actionWriter);
		writerSubscription = actionWriter.writeTo(target);
		transitionSubscription = inStateTransition.addObserver(transitionObserver);
		runStateSubscription = runState.addObserver(runStateObserver);
	}

	@Override
	public void execute() {
	    actionWriter.uncheckedWrite("1");
	}

	@Override
	public void close() {
		runStateSubscription.removeObserver();
		transitionSubscription.removeObserver();
		targetSubscribtion.removeObserver();
		writerSubscription.removeObserver();
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
