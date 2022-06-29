package uk.ac.stfc.isis.ibex.scriptgenerator.tests.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;

import uk.ac.stfc.isis.ibex.model.HasStatus;

/**
 * When attached as a property change listener to a class that implements the HasStatus interface
 * an object of this class will tally up the amount of property changes from one status to another.
 * Provides useful assertion methods for testing property change firings.
 * 
 * @param <T> The observable class.
 * @param <K> The type of status the observable class has.
 */
public class StatusSwitchCounter<T extends HasStatus<K>, K> implements PropertyChangeListener {

	private HashMap<K, HashMap<K, Integer>> switchMap = new HashMap<K, HashMap<K, Integer>>();

	/**
	 * Tally up a new property changed from one status to another.
	 * {@inheritdoc}
	 */
	@SuppressWarnings("unchecked")
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
	
	/**
	 * Assert that the number of property change switches from the oldStatus to the newStatus is
	 * exactly the expectedNumber.
	 * 
	 * @param oldStatus The status switched from.
	 * @param newStatus The status switched to.
	 * @param expectedNumber The expected number of times the switch occurred.
	 */
	public void assertNumberOfSwitches(K oldStatus, K newStatus, Integer expectedNumber) {
		String failureMessage = getFailureMessage(oldStatus, newStatus, expectedNumber);
		if (switchMap.containsKey(oldStatus)) {
			HashMap<K, Integer> newStateSwitchMap = switchMap.get(oldStatus);
			if (newStateSwitchMap.containsKey(newStatus)) {
				assertThat(failureMessage, newStateSwitchMap.get(newStatus), is(expectedNumber));
			} else {
				assertThat(failureMessage, 0, is(expectedNumber));
			}
		} else {
			assertThat(failureMessage, 0, is(expectedNumber));
		}
	}
	
	/**
	 * Assert that no property changes have been fired.
	 */
	public void assertNoSwitches() {
		String failureMessage = "Expected no switches.\nGot: " + switchMap.toString();
		assertTrue(failureMessage, switchMap.isEmpty());
	}
	
	private String getFailureMessage(K oldStatus, K newStatus, Integer expectedNumber) {
		return String.format("Expected map to contain: {%s={%s=%d}}.\nActual map: ", 
				oldStatus, 
				newStatus,
				expectedNumber
			) + switchMap.toString();
	}

}
