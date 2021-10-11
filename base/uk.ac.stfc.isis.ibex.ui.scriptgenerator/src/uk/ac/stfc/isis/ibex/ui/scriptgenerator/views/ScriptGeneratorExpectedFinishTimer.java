package uk.ac.stfc.isis.ibex.ui.scriptgenerator.views;

import java.time.LocalDateTime;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import java.time.format.DateTimeFormatter;

import org.eclipse.swt.widgets.Label;


/***
 * 
 * Helper class that calculates expected finish time.
 *
 */
public class ScriptGeneratorExpectedFinishTimer extends ModelObject implements Runnable{
	private Label expectedFinishTime;
	private volatile long timeEstimateVal;
	
	public ScriptGeneratorExpectedFinishTimer(Label expectedFinishTime, long timeEstimateVal) {
		this.expectedFinishTime = expectedFinishTime;
		this.timeEstimateVal = timeEstimateVal;
		System.out.println("made class");
	}
	
	public void SetTimeEstimateVal(long timeEstimateVal) {
		this.timeEstimateVal = timeEstimateVal;
	}
	public void run() {
		LocalDateTime currentTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		currentTime.plusSeconds(timeEstimateVal);
		firePropertyChange("finishTimeVal", "now", "Expected Finish Time: "+ currentTime.format(formatter));
		System.out.println("Expected Finish Time: "+ currentTime.format(formatter));
	}
}
