package uk.ac.stfc.isis.ibex.ui.scripting;

import org.python.pydev.ui.pythonpathconf.AbstractInterpreterProviderFactory;
import org.python.pydev.ui.pythonpathconf.AlreadyInstalledInterpreterProvider;
import org.python.pydev.ui.pythonpathconf.IInterpreterProvider;

import uk.ac.stfc.isis.ibex.preferences.PreferenceSupplier;

public class PythonInterpreterProviderFactory extends AbstractInterpreterProviderFactory {

	public static final String INTERPRETER_PROVIDER_ID = "IPC python";
	
	@Override
	public IInterpreterProvider[] getInterpreterProviders(InterpreterType type) {
		return AlreadyInstalledInterpreterProvider.create(INTERPRETER_PROVIDER_ID, PreferenceSupplier.pythonInterpreterPath());
	}
}
