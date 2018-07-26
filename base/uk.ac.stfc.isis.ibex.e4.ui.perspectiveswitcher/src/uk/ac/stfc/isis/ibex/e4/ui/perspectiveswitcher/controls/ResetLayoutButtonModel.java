package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls;

import uk.ac.stfc.isis.ibex.model.ModelObject;

public class ResetLayoutButtonModel extends ModelObject {
  private boolean changed = false;
  
  private static ResetLayoutButtonModel instance = null;

  public static ResetLayoutButtonModel getInstance() {
	  if (instance == null) {
		  instance = new ResetLayoutButtonModel();
	  }
	  return instance;
  }
  
  public boolean isChanged() {
    return this.changed;
  }

  public void setChanged(boolean changed)
  {
    firePropertyChange("layoutModified", null, this.changed = changed);
  }
}
