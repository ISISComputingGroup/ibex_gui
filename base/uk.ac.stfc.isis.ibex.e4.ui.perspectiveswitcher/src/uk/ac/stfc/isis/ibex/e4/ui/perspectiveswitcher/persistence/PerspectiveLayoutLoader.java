package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.persistence;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.logging.log4j.Logger;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.advanced.MPlaceholder;
import org.eclipse.e4.ui.model.application.ui.basic.MPartSashContainer;
import org.eclipse.e4.ui.model.application.ui.basic.MPartSashContainerElement;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.basic.MStackElement;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.emf.ecore.resource.Resource;

import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectivesProvider;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;

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
    	LOG.info("Loading perspective layout for " + id);
    	return loadLayoutFromSaveFile(id).orElseGet(() -> loadDefaultLayout(id));
    }
    
    private Optional<MPerspective> loadLayoutFromSaveFile(String id) {
	    Resource resource = E4ResourceUtils.getEmptyResource();
	    resource.setTrackingModification(false);
	    
	    try (FileInputStream inputStream = new FileInputStream(PersistenceUtils.getFileForPersistence(id))) { 
	    	resource.load(inputStream, null);
	    	
	    	// Print any diagnostic messages from corrupt entries.
	    	resource.getErrors().forEach(d -> LOG.info("Diagnostic error from resource: " + d.getMessage()));
	    	resource.getWarnings().forEach(d -> LOG.info("Diagnostic warning from resource: " + d.getMessage()));
	    	
	    	MPerspective perspective = resource.getContents().stream()
	    			.filter(obj -> obj instanceof MPerspective)
	    			.map(obj -> (MPerspective) obj)
	    			.findFirst()
	    			.orElseThrow(() -> new RuntimeException("No perspectives in save file"));
	    	
	    	List<MPlaceholder> placeholders = getPlaceholdersRecursive(perspective);
	    	LOG.info("hello");
	    	for (MPlaceholder x : placeholders) {
	    		LOG.info(x);
	    		LOG.info(x.getElementId());
	    		
//	    		MUIElement actual = Optional.ofNullable(modelService.find(x.getElementId(), app))
//	    				.orElse(modelService.find(x.getElementId(), PersistenceUtils.findMainWindow(app, modelService).get()));
	    		
	    		MUIElement actual = PersistenceUtils.findMainWindow(app, modelService)
	    			.map(MWindow::getSharedElements)
	    			.map(List::stream)
	    			.orElse(Stream.empty())
	    			.filter(elem -> elem.getElementId().equals(x.getElementId()))
	    			.findAny()
	    			.orElseThrow(() -> new RuntimeException("Could not reverse placeholder reference"));
	    		
	    		LOG.info(actual);
	    		
	    		x.setRef(actual);
	    	}
	    	
	    	return Optional.of(perspective);
	    	
	    } catch (RuntimeException | IOException err) {
    		LoggerUtils.logErrorWithStackTrace(LOG, "No valid save file for perspective " + id + ", falling back to default layout", err);
    		return Optional.empty();
    	} 	
    }
    
    @SuppressWarnings("rawtypes")
    private List<MPlaceholder> getPlaceholdersRecursive(MElementContainer container) {
    	List<MPlaceholder> placeholders = new ArrayList<>();
    	
    	for (Object element : container.getChildren()) {
    		if (element instanceof MPlaceholder) {
    			placeholders.add((MPlaceholder) element);
    		}
    		if (element instanceof MElementContainer) {
    			placeholders.addAll(getPlaceholdersRecursive((MElementContainer) element));
    		}
    	}
    	return placeholders;
    }
    
    private MPerspective loadDefaultLayout(String id) {
    	return app.getSnippets().stream()
    			.filter(snippet -> snippet instanceof MPerspective)
    			.map(snippet -> (MPerspective) snippet)
    			.filter(snippet -> PerspectivesProvider.matchPerspectivesById(snippet, id))
    			.findFirst()
    			.map(snippet -> (MPerspective) modelService.cloneSnippet(app, snippet.getElementId(), null))  // TODO: is this line needed?
    			.orElseThrow(() -> defaultLayoutNotFoundError(id));
    }
    
    private RuntimeException defaultLayoutNotFoundError(String id) {
    	return new IllegalArgumentException(String.format(
    			"Attempted to load default layout for perspective %s but it didn't exist.", id));
    }
}
