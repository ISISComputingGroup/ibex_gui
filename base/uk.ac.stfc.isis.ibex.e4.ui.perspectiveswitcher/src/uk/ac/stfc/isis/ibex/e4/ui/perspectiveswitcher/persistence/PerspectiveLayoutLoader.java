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
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.advanced.MPlaceholder;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartSashContainer;
import org.eclipse.e4.ui.model.application.ui.basic.MPartSashContainerElement;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.e4.ui.workbench.modeling.EPlaceholderResolver;
import org.eclipse.emf.ecore.resource.Resource;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectivesProvider;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;
import uk.ac.stfc.isis.ibex.ui.EmptyView;

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
	private final EPartService partService;
	
	private final EPlaceholderResolver placeholderResolver;

	public PerspectiveLayoutLoader(MApplication app, EModelService modelService, EPartService partService, EPlaceholderResolver placeholderResolver) {
		this.app = app;
		this.modelService = modelService;
		this.mainWindow = PersistenceUtils.findMainWindow(app, modelService)
				.orElseThrow(() -> new RuntimeException("Can't find main window"));
		
		this.placeholderResolver = placeholderResolver;
		this.partService = partService;
	}
	
    public MPerspective load(String id) {
    	LOG.info("Loading perspective layout for " + id);
    	return loadLayoutFromSaveFile(id).orElseGet(() -> loadDefaultLayout(id));
    }
    
    private Optional<MPerspective> loadLayoutFromSaveFile(String id) {
	    Resource resource = E4ResourceUtils.getEmptyResource();
	    
	    try (FileInputStream inputStream = new FileInputStream(PersistenceUtils.getFileForPersistence(id))) { 
	    	resource.load(inputStream, null);
	    	
	    	// Print any diagnostic messages from corrupt entries.
	    	resource.getErrors().forEach(d -> LOG.info("Diagnostic error from resource: " + d.getMessage()));
	    	resource.getWarnings().forEach(d -> LOG.info("Diagnostic warning from resource: " + d.getMessage()));
	    	
	    	MPerspective perspective = resource.getContents().stream()
	    			.filter(MPerspective.class::isInstance)
	    			.map(MPerspective.class::cast)
	    			.findFirst()
	    			.orElseThrow(() -> new RuntimeException("No perspectives in save file"));
	    	
	    	for (MPlaceholder placeholder : getPlaceholdersRecursive(perspective)) {
	    		
	    		String phid = placeholder.getElementId();
	    		if (phid.contains(":")) {
	    			placeholder.setElementId(phid.substring(0, phid.indexOf(":")));
	    		}
	    		
	    		try {
	    		    placeholderResolver.resolvePlaceholderRef(placeholder, mainWindow);
	    		} catch (RuntimeException e) {
	    			LOG.info("Couldn't resolve placeholder reference " + placeholder.getElementId());
	    		}
	    		
	    		if (placeholder.getRef() == null) {
		    		try {
		    			MPartDescriptor descriptor = MBasicFactory.INSTANCE.createPartDescriptor();
		    			
		    			String emptyViewPackage = EmptyView.class.getPackage().getName();
		    			
		    			descriptor.setContributionURI(String.format("bundleclass://%s/%s.EmptyView", emptyViewPackage, emptyViewPackage, EmptyView.class.getSimpleName()));
		    			descriptor.setContributorURI(String.format("platform:/plugin/%s", emptyViewPackage));
		    			descriptor.setElementId(EmptyView.ID);
		    			descriptor.setLabel("Empty view");
		    			descriptor.setAllowMultiple(true);
		    			
		    			MPart createdElement = modelService.createPart(descriptor);
		    			mainWindow.getSharedElements().add(createdElement);
		    			placeholder.setRef(createdElement);
		    			
		    		} catch (RuntimeException e) {
		    			LOG.info("Couldn't resolve placeholder reference " + placeholder.getElementId() + " using classloader: " + e.getMessage());
		    			LoggerUtils.logErrorWithStackTrace(LOG, e.getMessage(), e);
		    		}
	    		}
	    		
	    		if (placeholder.getRef() == null) {
		    		LOG.info("Couldn't resolve placeholder '" + placeholder.getElementId() + "'. Deleting it from restored model.");
		    		placeholder.setToBeRendered(false);
		    		placeholder.setVisible(true);
		    		
		    		MElementContainer<MUIElement> parent = placeholder.getParent();
		    		List<MUIElement> children = parent.getChildren();
		    		
		    		if (children.stream().filter(MUIElement::isToBeRendered).count() == 0) {
		    			parent.setToBeRendered(false);
		    		}
	    		}
	    	}
	    	
	    	return Optional.of(perspective);
	    	
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
