package uk.ac.stfc.isis.ibex.ui.alarm;

import org.csstudio.alarm.beast.ui.alarmtree.AlarmTreeView;


public class AlarmView extends AlarmTreeView {

	public static final String ID = "uk.ac.stfc.isis.ibex.ui.test.views.TestView"; //$NON-NLS-1$

	
	public AlarmView() {
		setPartName("TestView");
	}

	@Override
	public void setFocus() {
		// Set the focus
	}
}
