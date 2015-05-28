package uk.ac.stfc.isis.ibex.dae.spectra;

public class UpdatableSpectrum extends Spectrum {

	private boolean requiresUpdate;

	public boolean getRequiresUpdate() {
		return requiresUpdate;
	}
	
	public void update() {
		firePropertyChange("requiresUpdate", requiresUpdate, requiresUpdate = false);
	}
	
	@Override
	public void setNumber(int value) {
		super.setNumber(value);
		firePropertyChange("requiresUpdate", requiresUpdate, requiresUpdate = true);
	}
	
	@Override
	public void setPeriod(int value) {
		super.setPeriod(value);
		firePropertyChange("requiresUpdate", requiresUpdate, requiresUpdate = true);
	}
}
