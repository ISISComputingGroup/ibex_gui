package uk.ac.stfc.isis.ibex.ui.tableofmotors.displayoptions;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.epics.switching.WritableFactory;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.instrument.channels.DoubleChannel;

public class AccessibilityPv {
	private static final String ACCESSIBILITY_PV = "loc://accessibility:colour_scheme";
	
	private static final WritableFactory WRITE_FACTORY = new WritableFactory(OnInstrumentSwitch.NOTHING);
	
	private final AccessibilityModel model;
	
	/**
	 * Need to use doubles as this works best with CSS local PVs.
	 */
	private static final Writable<Double> USE_ACCESSIBILITY_MODE = WRITE_FACTORY.getSwitchableWritable(new DoubleChannel(), ACCESSIBILITY_PV);
	
	private static final ExecutorService LOCAL_PV_WRITES = 
			Executors.newCachedThreadPool(job -> new Thread(job, "AccessibilityPV Local PV writes"));
	
	private static final int MAX_INITIAL_WRITE_ATTEMPTS = 30;
	private static final int MILLISECONDS_BETWEEN_INITIAL_WRITE_ATTEMPTS = 1000;
	
	public AccessibilityPv() {
		model = AccessibilityModel.getInstance();
		model.addPropertyChangeListener("accessibilityEnabled", this::onAccessibilityChange);
	}
	
	/**
	 * Attempt to write the current value of accessibility mode.
	 * 
	 * Needs to be able to retry because CSS takes some time to come up. This function runs asynchronously 
	 * to avoid blocking the UI thread with potentially blocking calls.
	 */
	public void writeInitialValue() {
		LOCAL_PV_WRITES.submit(() -> {
			for (int i=0; i<MAX_INITIAL_WRITE_ATTEMPTS; i++) {
				try {
					USE_ACCESSIBILITY_MODE.write(model.getAccessibilityEnabled() ? 1. : 0.);
					break;
				} catch (IOException e) {
					try {
						Thread.sleep(MILLISECONDS_BETWEEN_INITIAL_WRITE_ATTEMPTS);
					} catch (InterruptedException e1) {
						break;
					}
				}
			}
		});
	}
	
	/**
	 * Event handler for property change events. Asynchronous so that it does not block waiting for a timeout
	 * if CS-Studio is not up (yet).
	 * @param event the property change event
	 */
	private void onAccessibilityChange(PropertyChangeEvent event) {
		LOCAL_PV_WRITES.submit(() -> setAccessibilityPv(model.getAccessibilityEnabled()));
	}
	
	private void setAccessibilityPv(boolean enabled) {
		try {
			USE_ACCESSIBILITY_MODE.write(enabled ? 1. : 0.);
		} catch (IOException e) {
			// ignore
		}
	}
}
