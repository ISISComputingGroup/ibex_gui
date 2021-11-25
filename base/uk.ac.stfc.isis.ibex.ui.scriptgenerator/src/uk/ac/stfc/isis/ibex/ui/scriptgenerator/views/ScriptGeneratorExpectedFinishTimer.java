package uk.ac.stfc.isis.ibex.ui.scriptgenerator.views;

import java.time.LocalDateTime;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import java.time.format.DateTimeFormatter;

/***
 * 
 * Helper class that calculates expected finish time.
 *
 */
public class ScriptGeneratorExpectedFinishTimer extends ModelObject implements Runnable {
	private volatile long timeEstimateVal;
	private String finishTime; 
	
	/**
	 * The default constructor.
	 *
	 */
	public ScriptGeneratorExpectedFinishTimer() {
		this.timeEstimateVal = 0;
	}
	
	/**
	 * Function to set the current time estimate.
	 * @param timeEstimateVal the current time estimate of the script.
	 */
	public void setTimeEstimateVal(long timeEstimateVal) {
		this.timeEstimateVal = timeEstimateVal;
	}
	
	/**
	 * Function to be ran by a scheduler every second.
	 * 
	 * Adds the timeEstimateVal to the current time and then alerts the gui that the property has changed.
	 */
	public void run() {
		LocalDateTime currentTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		currentTime = currentTime.plusSeconds(timeEstimateVal);
		finishTime = "Expected Finish Time: " + currentTime.format(formatter);
		firePropertyChange("finishTimeVal", null, finishTime);
	}
}