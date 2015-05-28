package uk.ac.stfc.isis.ibex.dae.spectra;

import java.util.ArrayList;
import java.util.List;

import uk.ac.stfc.isis.ibex.dae.DaeObservables;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;

public class Spectra extends Closer {
	
	private final ArrayList<UpdatableSpectrum> spectra = new ArrayList<>();
	private final DaeObservables observables;

	public Spectra(DaeObservables observables) {
		this.observables = observables;
		
		addSpectrum(1, 1);	
		addSpectrum(2, 1);	
		addSpectrum(3, 1);	
		addSpectrum(4, 1);	
	}

	public List<? extends UpdatableSpectrum> spectra() {
		return spectra;
	}

	private void addSpectrum(int spectrum, int period) {	
		UpdatableSpectrum spec = registerForClose(new ObservedSpectrum(observables));
		spec.setNumber(spectrum);
		spec.setPeriod(period);
		spec.update();
		
		spectra.add(spec);
	}
}
