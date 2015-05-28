package uk.ac.stfc.isis.ibex.ui.synoptic;

import org.apache.logging.log4j.Logger;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.synoptic.model.Target;
import uk.ac.stfc.isis.ibex.synoptic.navigation.TargetNode;

public class NavigationPresenter extends ModelObject {
	
	private static final Logger LOG = IsisLog.getLogger("NavigationPresenter");
	
	private TargetNode current;
	
	public NavigationPresenter(TargetNode node) {
		current = node;
	}
	
	public Target currentTarget() {
		return current.item();
	}
	
	public void setCurrentTarget(TargetNode target) {
		current = target;
		fireAllWithNewValues();
	}

	public TargetNode currentNode() {
		return current;
	}
	
	public boolean getHasPrevious() {
		return current != null && current.previous() != null;
	}

	public boolean getHasUp() {
		return current != null && current.up() != null;
	}
	
	public boolean getHasNext() {
		return current != null && current.next() != null;
	}
	
	public boolean getHasDown() {
		return current != null && current.down() != null;
	}
	
	public void previous() {
		if (getHasPrevious()) {
			setCurrent(current.previous());
		}
	}
	
	public String nameOfPrevious() {
		return getHasPrevious() ? display(current.previous().item().name()) : "";
	}

	public void up() {
		if (getHasUp()) {
			setCurrent(current.up());
		}
	}
	
	public String nameOfUp() {
		return getHasUp() ? display(current.up().item().name()) : "";
	}
	
	public void next() {
		if (getHasNext()) {
			setCurrent(current.next());
		}
	}
	
	public String nameOfNext() {
		return getHasNext() ? display(current.next().item().name()) : "";
	}
	
	public void down() {
		if (getHasDown()) {
			setCurrent(current.down());
		}
	}
	
	public String nameOfDown() {
		return getHasDown() ? display(current.down().item().name()) : "";
	}
	
	private String display(String name) {
		return name.trim();
	}
	
	private void setCurrent(TargetNode node) {
		LOG.info(node.item());
		current = node;
		fireAllWithNewValues();
	}
	
	private void fireAllWithNewValues() {
		// Sending an previous value of null forces an update of databound controls
		firePropertyChange("hasPrevious", null, getHasPrevious());
		firePropertyChange("hasUp", null, getHasUp());
		firePropertyChange("hasNext", null, getHasNext());
		firePropertyChange("hasDown", null, getHasDown());
		
		firePropertyChange("currentTarget", null, currentTarget());
		firePropertyChange("currentNode", null, currentNode());
	}
}
