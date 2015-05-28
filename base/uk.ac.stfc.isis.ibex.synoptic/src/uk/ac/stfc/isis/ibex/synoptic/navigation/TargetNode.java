package uk.ac.stfc.isis.ibex.synoptic.navigation;

import uk.ac.stfc.isis.ibex.synoptic.model.Target;

public class TargetNode  {

	private final Target item;
	private TargetNode previous = null;
	private TargetNode up = null;
	private TargetNode next = null;
	private TargetNode down = null;
	
	public TargetNode(Target item) {
		this.item = item;
	}
	
	public TargetNode(TargetNode other) {
		this(other.item);
		previous = other.previous;
		up = other.up;
		next = other.next;
		down = other.down;
	}
	
	public Target item() {
		return item;
	}
	
	public TargetNode previous() {
		return previous;
	}

	public void setPrevious(TargetNode node) {
		previous = node;
	}
	
	public TargetNode up() {
		return up;
	}
	
	public void setUp(TargetNode node) {
		up = node;
	}
	
	public TargetNode next() {
		return next;
	}
	
	public void setNext(TargetNode node) {
		next = node;
	}
	
	public TargetNode down() {
		return down;
	}
	
	public void setDown(TargetNode node) {
		down = node;
	}
	
}
