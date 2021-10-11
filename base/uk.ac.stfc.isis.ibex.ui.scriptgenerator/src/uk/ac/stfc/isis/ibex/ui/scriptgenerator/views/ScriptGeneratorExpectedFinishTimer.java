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
	private volatile String finishTime; 
	
	public ScriptGeneratorExpectedFinishTimer(long timeEstimateVal) {
		this.timeEstimateVal = timeEstimateVal;
		System.out.println("made class");
	}
	
	public void SetTimeEstimateVal(long timeEstimateVal) {
		this.timeEstimateVal = timeEstimateVal;
	}
	
	public String getFinishTime() {
		return finishTime;
	}
	public void run() {
		LocalDateTime currentTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		currentTime = currentTime.plusSeconds(timeEstimateVal);
		firePropertyChange("finishTimeVal", finishTime, finishTime = "Expected Finish Time: "+ currentTime.format(formatter));
	}
}