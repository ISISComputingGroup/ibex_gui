package uk.ac.stfc.isis.ibex.dae.experimentsetup.periods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.ac.stfc.isis.ibex.dae.xml.DoubleNode;
import uk.ac.stfc.isis.ibex.dae.xml.EnumNode;
import uk.ac.stfc.isis.ibex.dae.xml.IntegerNode;
import uk.ac.stfc.isis.ibex.dae.xml.StringNode;
import uk.ac.stfc.isis.ibex.dae.xml.XmlFile;
import uk.ac.stfc.isis.ibex.dae.xml.XmlNode;

public class XMLBackedPeriodSettings extends PeriodSettings {
		
	private static final int NUMBER_OF_PERIODS = 8;
	
	private final XmlFile xmlFile;
	private final List<XmlNode<?>> nodes = new ArrayList<>();
	
	private final EnumNode<PeriodSetupSource> setupSource = new EnumNode<>("/Cluster/EW[Name='Period Setup Source']/Val", PeriodSetupSource.class);
	private final StringNode periodFile = new StringNode("/Cluster/String[Name='Period File']/Val");
	private final EnumNode<PeriodControlType> periodType = new EnumNode<>("/Cluster/EW[Name='Period Type']/Val", PeriodControlType.class);
	private final IntegerNode softwarePeriods = new IntegerNode("/Cluster/I32[Name='Number Of Software Periods']/Val");
	private final DoubleNode hardwarePeriods = new DoubleNode("/Cluster/DBL[Name='Hardware Period Sequences']/Val");
	private final DoubleNode outputDelay = new DoubleNode("/Cluster/DBL[Name='Output Delay (us)']/Val");
	
	private final ArrayList<XmlBackedPeriod> periods = new ArrayList<>();
	
	public XMLBackedPeriodSettings() {
		nodes.add(setupSource);
		nodes.add(periodFile);
		nodes.add(periodType);
		nodes.add(softwarePeriods);
		nodes.add(hardwarePeriods);
		nodes.add(outputDelay);
		
		for (int i = 1; i <= NUMBER_OF_PERIODS; i++) {
			XmlBackedPeriod period = new XmlBackedPeriod(i);
			nodes.addAll(period.nodes());
			periods.add(period);
		}
		
		xmlFile = new XmlFile(nodes);
	}
	
	public void setXml(String xml) {
		xmlFile.setXml(xml);
		initialiseFromXml();
	}

	public String xml() {
		return xmlFile.toString();
	}	
	
	@Override
	public void setSetupSource(PeriodSetupSource value) {
		super.setSetupSource(value);
		setupSource.setValue(value);
	}
	
	@Override
	public void setPeriodFile(String value) {
		super.setPeriodFile(value);
		periodFile.setValue(value);
	}
	
	@Override
	public void setPeriodType(PeriodControlType value) {
		super.setPeriodType(value);
		periodType.setValue(value);
	}
	
	@Override
	public void setSoftwarePeriods(int value) {
		super.setSoftwarePeriods(value);
		softwarePeriods.setValue(value);
	}
	
	@Override
	public void setHardwarePeriods(double value) {
		super.setHardwarePeriods(value);
		hardwarePeriods.setValue(value);
	}
	
	@Override
	public void setOutputDelay(double value) {
		super.setOutputDelay(value);
		outputDelay.setValue(value);
	}
	
	private void initialiseFromXml() {
		super.setSetupSource(setupSource.value());
		super.setPeriodFile(periodFile.value());
		super.setPeriodType(periodType.value());
		super.setSoftwarePeriods(softwarePeriods.value());
		super.setHardwarePeriods(hardwarePeriods.value());
		super.setOutputDelay(outputDelay.value());
		
		for (XmlBackedPeriod period : periods) {
			period.initialiseFromNodes();
		}
		
		super.getPeriods().clear();
		super.getPeriods().addAll(periods);
		firePropertyChange("periods", Collections.<Period>emptyList(), super.getPeriods());
	}
}
