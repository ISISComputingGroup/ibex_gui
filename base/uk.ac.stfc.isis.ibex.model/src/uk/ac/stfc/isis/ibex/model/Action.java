package uk.ac.stfc.isis.ibex.model;


public abstract class Action extends ModelObject {
	
	private boolean canExecute;
	
	public abstract void execute();
	
	public boolean getCanExecute() {
		return canExecute;
	}
	
	protected void setCanExecute(boolean canExecute) {
		firePropertyChange("canExecute", this.canExecute, this.canExecute = canExecute);
	}
}
