package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;

import uk.ac.stfc.isis.ibex.model.ModelObject;

public class ResetLayoutButtonModel extends ModelObject {
  private boolean changed = false;
  private MPerspective currentPerspective;
  private Set<MPerspective> changedPerspectives = new HashSet<MPerspective>();

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

  public void setChanged(boolean changed) {
	  currentPerspective = getCurrentPerspective();
	  if (changed) {
		  this.changedPerspectives.add(currentPerspective);
	  } else {
		  this.changedPerspectives.remove(currentPerspective);
	  }
	  firePropertyChange("layoutModified", null, this.changed = changed);
  }

  public MPerspective getCurrentPerspective() {
	  return this.currentPerspective;
  }

  public void setCurrentPerspective(MPerspective perspective) {
	  this.currentPerspective = perspective;
	  if (this.changedPerspectives.contains(perspective)) {
		  this.setChanged(true);
	  } else {
		  this.setChanged(false);
	  }
  }
}
