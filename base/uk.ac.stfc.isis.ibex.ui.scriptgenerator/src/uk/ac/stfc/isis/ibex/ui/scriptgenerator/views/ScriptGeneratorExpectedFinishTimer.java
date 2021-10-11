package uk.ac.stfc.isis.ibex.ui.scriptgenerator.views;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.eclipse.swt.widgets.Label;


/***
 * 
 * Helper class that calculates expected finish time.
 *
 */
public class ScriptGeneratorExpectedFinishTimer implements Runnable{
	private Label expectedFinishTime;
	private volatile long timeEstimateVal;
	
	public ScriptGeneratorExpectedFinishTimer(Label expectedFinishTime, long timeEstimateVal) {
		this.expectedFinishTime = expectedFinishTime;
		this.timeEstimateVal = timeEstimateVal;
	}
	
	public void SetTimeEstimateVal(long timeEstimateVal) {
		this.timeEstimateVal = timeEstimateVal;
	}
	public void run() {
		LocalDateTime currentTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		currentTime.plusSeconds(timeEstimateVal);
		expectedFinishTime.setText(currentTime.format(formatter));
	}
}
