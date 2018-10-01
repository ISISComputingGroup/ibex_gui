package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.persistence;

import org.eclipse.e4.ui.internal.workbench.E4XMIResourceFactory;
import org.eclipse.emf.ecore.resource.Resource;

@SuppressWarnings("restriction")
public class E4ResourceUtils {
	private static final E4XMIResourceFactory RESOURCE_FACTORY = new E4XMIResourceFactory();
	
    public static Resource getEmptyResource() {
		return RESOURCE_FACTORY.createResource(null);
    }
}
