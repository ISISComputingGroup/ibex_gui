package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.persistence;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import org.apache.logging.log4j.Logger;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.emf.ecore.resource.Resource;
import uk.ac.stfc.isis.ibex.logger.IsisLog;

/**
 * This class is responsible for loading an MPerspective instance given a perspective ID.
 * 
 * The MPerspective instance will be the saved layout if available, otherwise it will fall
 * back to a default layout.
 */
@SuppressWarnings("restriction")
public class PerspectiveLayoutLoader {
	
	private static final Logger LOG = IsisLog.getLogger(PerspectiveLayoutLoader.class);
	
	private final MApplication app;
	private final EModelService modelService;

	public PerspectiveLayoutLoader(MApplication app, EModelService modelService) {
		this.app = app;
		this.modelService = modelService;
	}
	
    public MPerspective load(String id) {
    	LOG.info("Attempting to load perspective: " + id);
    	return loadLayoutFromSaveFile(id).orElseGet(() -> loadDefaultLayout(id));
    }
    
    private Optional<MPerspective> loadLayoutFromSaveFile(String id) {
    	try {
    	    Resource resource = E4ResourceUtils.getEmptyResource();
    	    
    	    try (FileInputStream inputStream = new FileInputStream(FilenameProvider.getPath(id).toFile())) { 
    	    	resource.load(inputStream, null);
    	    	
    	    	if (resource.getContents().isEmpty()) {
    	    		LOG.error("Loaded an empty resource file for perspective with id " + id);
    	    		return Optional.empty();
    	    	}
    	    	
    	    	return resource.getContents().stream()
    	    			.filter(obj -> obj instanceof MPerspective)
    	    			.map(obj -> (MPerspective) obj)
    	    			.findFirst();
    	    }
    	} catch (RuntimeException | IOException err) {
    		LOG.info("No valid save file for perspective " + id + ", falling back to default layout");
    		return Optional.empty();
    	} 	
    }
    
    private MPerspective loadDefaultLayout(String id) {
    	return app.getSnippets().stream()
    			.filter(snippet -> snippet instanceof MPerspective)
    			.map(snippet -> (MPerspective) snippet)
    			.filter(snippet -> Objects.equals(snippet.getElementId(), id))
    			.findFirst()
    			.map(snippet -> (MPerspective) modelService.cloneSnippet(app, snippet.getElementId(), null))
    			.orElseThrow(() -> defaultLayoutNotFoundError(id));
    }
    
    private RuntimeException defaultLayoutNotFoundError(String id) {
    	return new IllegalArgumentException(String.format(
    			"Attempted to load default layout for perspective %s but it didn't exist.", id));
    }
}
