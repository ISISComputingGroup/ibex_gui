package uk.ac.stfc.isis.ibex.ui.scriptgenerator.views;

import java.time.LocalDateTime;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import java.time.format.DateTimeFormatter;

/***
 * 
 * Helper class that calculates expected finish time.
 *
 */
public class ScriptGeneratorExpectedFinishTimer extends ModelObject implements Runnable{
	private volatile long timeEstimateVal;
	private String finishTime; 
	
	public ScriptGeneratorExpectedFinishTimer() {
		this.timeEstimateVal = 0;
	}
	
	public void SetTimeEstimateVal(long timeEstimateVal) {
		this.timeEstimateVal = timeEstimateVal;
	}
	
	public void run() {
		LocalDateTime currentTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		currentTime = currentTime.plusSeconds(timeEstimateVal);
		finishTime = "Expected Finish Time: "+ currentTime.format(formatter);
		firePropertyChange("finishTimeVal", null, finishTime);
	}
}