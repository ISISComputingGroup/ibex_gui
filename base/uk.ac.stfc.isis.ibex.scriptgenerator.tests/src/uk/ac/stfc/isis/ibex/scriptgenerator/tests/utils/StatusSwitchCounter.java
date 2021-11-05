package uk.ac.stfc.isis.ibex.scriptgenerator.tests.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;

import uk.ac.stfc.isis.ibex.nicos.ScriptStatus;

public class StatusSwitchCounter implements PropertyChangeListener {
	
	private HashMap<ScriptStatus, HashMap<ScriptStatus, Integer>> switchMap = new HashMap<ScriptStatus, HashMap<ScriptStatus,Integer>>();

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		ScriptStatus oldStatus = (ScriptStatus) evt.getOldValue();
		ScriptStatus newStatus = (ScriptStatus) evt.getNewValue();
		if (switchMap.containsKey(oldStatus)) {
			HashMap<ScriptStatus, Integer> newStateSwitchMap = switchMap.get(oldStatus);
			if (newStateSwitchMap.containsKey(newStatus)) {
				newStateSwitchMap.put(newStatus, newStateSwitchMap.get(newStatus));
			} else {
				newStateSwitchMap.put(newStatus, 1);
			}
		} else {
			HashMap<ScriptStatus, Integer> newStateSwitchMap = new HashMap<ScriptStatus, Integer>();
			newStateSwitchMap.put(newStatus, 1);
			switchMap.put(oldStatus, newStateSwitchMap);
		}
	}
	
	public void assertNumberOfSwitches(ScriptStatus oldStatus, ScriptStatus newStatus, Integer expectedNumber) {
		if (switchMap.containsKey(oldStatus)) {
			HashMap<ScriptStatus, Integer> newStateSwitchMap = switchMap.get(oldStatus);
			if (newStateSwitchMap.containsKey(newStatus)) {
				assertThat(newStateSwitchMap.get(newStatus), is(expectedNumber));
			} else {
				assertThat(0, is(expectedNumber));
			}
		} else {
			assertThat(0, is(expectedNumber));
		}
	}

}
