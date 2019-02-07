package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.persistence;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.Logger;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.descriptor.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.descriptor.basic.MPartDescriptor;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.advanced.MPlaceholder;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPlaceholderResolver;
import org.eclipse.emf.ecore.resource.Resource;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectivesProvider;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;
import uk.ac.stfc.isis.ibex.ui.UnrestorableView;

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
	private final MWindow mainWindow;
	
	private final EPlaceholderResolver placeholderResolver;

	/**
	 * A PerspectiveLayoutLoader is responsible for taking a perspective ID and turning it into an
	 * MPerspective object.
	 * 
	 * @param app - the application instance
	 * @param modelService - the model service
	 * @param placeholderResolver - the placeholder resolver to use
	 */
	public PerspectiveLayoutLoader(MApplication app, EModelService modelService, EPlaceholderResolver placeholderResolver) {
		this.app = app;
		this.modelService = modelService;
		this.mainWindow = PersistenceUtils.findMainWindow(app, modelService)
				.orElseThrow(() -> new IllegalStateException("Can't find main window"));
		
		this.placeholderResolver = placeholderResolver;
	}
	
	/**
	 * Loads a perspective with a given ID. Attempts to use a save file first. If that fails,
	 * load the default layout.
	 * 
	 * @param id the ID of the perspective to load
	 * @return an MPerspective describing the loaded perspective
	 */
    public MPerspective load(String id) {
    	LOG.info("Loading perspective layout for " + id);
    	return loadLayoutFromSaveFile(id).orElseGet(() -> loadDefaultLayout(id));
    }
    
    /**
     * Loads a perspective with a given ID from a save file.
     * 
     * @param id the ID of the perspective that should be loaded
     * @return an Optional containing the perspective if it was loaded, an empty optional otherwise.
     */
    private Optional<MPerspective> loadLayoutFromSaveFile(String id) {
	    Resource resource = PersistenceUtils.getEmptyResource();
	    
	    try (FileInputStream inputStream = new FileInputStream(PersistenceUtils.getFileForPersistence(id))) { 
	    	resource.load(inputStream, null);
	    	
	    	// Print any diagnostic messages from corrupt entries.
	    	resource.getErrors().forEach(d -> LOG.info("Diagnostic error from resource: " + d.getMessage()));
	    	resource.getWarnings().forEach(d -> LOG.info("Diagnostic warning from resource: " + d.getMessage()));
	    	
	    	Optional<MPerspective> perspective = resource.getContents().stream()
	    			.filter(MPerspective.class::isInstance)
	    			.map(MPerspective.class::cast)
	    			.findFirst();
	    	
	    	perspective
		    	.map(this::getPlaceholdersRecursive)
		    	.ifPresent(placeholders -> placeholders.forEach(this::resolvePlaceholderReference));
	    	
	    	return perspective;
	    	
	    } catch (RuntimeException | IOException err) {
    		LoggerUtils.logErrorWithStackTrace(LOG, "No valid save file for perspective " + id + ", falling back to default layout", err);
    		return Optional.empty();
    	} 	
    }
    
    /**
     * Recursive descent down containers to find all placeholders from a given parent.
     * @param container the root container
     * @return a list of all of the placeholders
     */
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
    
    /**
     * Fill in the reference of a placeholder using shared parts if available.
     * 
     * Falls back to an empty view otherwise.
     * 
     * @param placeholder - the placeholder to resolve.
     */
    private void resolvePlaceholderReference(MPlaceholder placeholder) {
    	try {
		    placeholderResolver.resolvePlaceholderRef(placeholder, mainWindow);
		} catch (RuntimeException e) {
			LOG.info("Couldn't resolve placeholder reference " + placeholder.getElementId());
		}
    	
    	if (placeholder.getRef() == null) {
    		pointPlaceholderAtEmptyView(placeholder);
    	}
    }
    
    /**
     * Force a given placeholder to point at an empty view. This is the fallback
     * if we couldn't resolve a placeholder reference.
     * 
     * @param placeholder the placeholder to set to an empty view.
     */
    private void pointPlaceholderAtEmptyView(MPlaceholder placeholder) {
    	try {
			MPartDescriptor descriptor = MBasicFactory.INSTANCE.createPartDescriptor();
			
			String emptyViewPackage = UnrestorableView.class.getPackage().getName();
			
			descriptor.setContributionURI(String.format("bundleclass://%s/%s.%s", emptyViewPackage, emptyViewPackage, UnrestorableView.class.getSimpleName()));
			descriptor.setContributorURI(String.format("platform:/plugin/%s", emptyViewPackage));
			descriptor.setElementId(UnrestorableView.ID);
			descriptor.setLabel("Unrestorable view");
			descriptor.setAllowMultiple(true);
			
			MPart createdElement = modelService.createPart(descriptor);
			mainWindow.getSharedElements().add(createdElement);
			placeholder.setRef(createdElement);
			
		} catch (RuntimeException e) {
			LOG.info("Couldn't point placeholder " + placeholder.getElementId() + " at empty view: " + e.getMessage());
			LoggerUtils.logErrorWithStackTrace(LOG, e.getMessage(), e);
		}
    }
    
    private MPerspective loadDefaultLayout(String id) {
    	return app.getSnippets().stream()
    			.filter(MPerspective.class::isInstance)
    			.map(MPerspective.class::cast)
    			.filter(snippet -> PerspectivesProvider.matchPerspectivesById(snippet, id))
    			.findFirst()
    			.map(snippet -> modelService.cloneSnippet(app, snippet.getElementId(), null))
    			.map(MPerspective.class::cast)
    			.orElseThrow(() -> defaultLayoutNotFoundError(id));
    }
    
    private RuntimeException defaultLayoutNotFoundError(String id) {
    	return new IllegalArgumentException(String.format(
    			"Attempted to load default layout for perspective %s but it didn't exist.", id));
    }
}
