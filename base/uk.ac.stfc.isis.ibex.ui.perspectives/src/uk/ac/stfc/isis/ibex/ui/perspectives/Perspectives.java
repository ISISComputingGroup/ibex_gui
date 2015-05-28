package uk.ac.stfc.isis.ibex.ui.perspectives;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

public class Perspectives {

	private final List<IsisPerspective> perspectives = new ArrayList<>();
	private final Map<String, String> ids = new HashMap<>();
	
	public Perspectives() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] elements = registry.getConfigurationElementsFor("uk.ac.stfc.isis.ibex.ui.perspectives");
		for (IConfigurationElement element : elements) {
			try {
				final Object obj = element.createExecutableExtension("class");
				IsisPerspective perspective = (IsisPerspective) obj;
				perspectives.add(perspective);
				ids.put(perspective.name(), perspective.ID());
				
				Collections.sort(perspectives);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}
	
	public List<IsisPerspective> get() {		
		return perspectives;
	}
	
	public String getID(String perspectiveName) {
		return ids.get(perspectiveName);
	}
	
}
