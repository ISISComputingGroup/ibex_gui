package uk.ac.stfc.isis.ibex.model;


public abstract class SetCommand<T> extends ModelObject {
		
	private boolean canSend;
	
	public abstract void send(T value);
	
	public boolean getCanSend() {
		return canSend;
	}

	protected void setCanSend(boolean canSend) {
		firePropertyChange("canSend", this.canSend, this.canSend = canSend);
	}
}
