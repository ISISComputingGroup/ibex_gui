package uk.ac.stfc.isis.ibex.scriptgenerator.tests.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;

import uk.ac.stfc.isis.ibex.model.HasStatus;

public class StatusSwitchCounter<T extends HasStatus<K>, K> implements PropertyChangeListener {
	
	private HashMap<K, HashMap<K, Integer>> switchMap = new HashMap<K, HashMap<K,Integer>>();

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		T oldVal = (T) evt.getOldValue();
		T newVal = (T) evt.getNewValue();
		K oldStatus = oldVal.getStatus();
		K newStatus = newVal.getStatus();
		if (switchMap.containsKey(oldStatus)) {
			HashMap<K, Integer> newStateSwitchMap = switchMap.get(oldStatus);
			if (newStateSwitchMap.containsKey(newStatus)) {
				newStateSwitchMap.put(newStatus, newStateSwitchMap.get(newStatus));
			} else {
				newStateSwitchMap.put(newStatus, 1);
			}
		} else {
			HashMap<K, Integer> newStateSwitchMap = new HashMap<K, Integer>();
			newStateSwitchMap.put(newStatus, 1);
			switchMap.put(oldStatus, newStateSwitchMap);
		}
	}
	
	public void assertNumberOfSwitches(K oldStatus, K newStatus, Integer expectedNumber) {
		if (switchMap.containsKey(oldStatus)) {
			HashMap<K, Integer> newStateSwitchMap = switchMap.get(oldStatus);
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
