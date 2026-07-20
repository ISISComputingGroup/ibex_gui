package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;

import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PerspectiveAdapter;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.model.UpdatedValue;

/**
 * A Matplotlib View targeting all current figures.
 */
public abstract class MatplotlibAllFiguresView {
	private Set<MatplotlibFigure> figures = new HashSet<>();
	private Composite parent;
	
	private PropertyChangeListener figuresChangedListener;
	private PropertyChangeListener urlListener;
	
	private IPerspectiveListener perspectiveSwitchListener;
	private String currentPerspectiveId;
	private String thisPerspectiveId;
	
	/**
	 * Construct this view.
	 * @param parent the parent
	 * @param perspective the perspective
	 */
	@PostConstruct
	public synchronized void createComposite(final Composite parent, final MPerspective perspective) {
		var layout = new GridLayout(1, true);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		parent.setLayout(layout);
		this.parent = parent;
		recreateFigures();
		figuresChangedListener = getFigures()
				.addUiThreadPropertyChangeListener(evt -> recreateFigures());
		urlListener = getConnectionUrl()
				.addUiThreadPropertyChangeListener(evt -> recreateFigures());
		
		thisPerspectiveId = perspective.getElementId();
		currentPerspectiveId = thisPerspectiveId;
		
		perspectiveSwitchListener = new PerspectiveAdapter() {
			@Override
	        public void perspectiveActivated(IWorkbenchPage page, IPerspectiveDescriptor activatedPerspective) {
				Display.getDefault().asyncExec(() -> {
					boolean relevantChange = Objects.equals(thisPerspectiveId, currentPerspectiveId)
							|| Objects.equals(thisPerspectiveId, activatedPerspective.getId());
					
		            currentPerspectiveId = activatedPerspective.getId();
		            
		            if (relevantChange) {
		            	recreateFigures();
		            }
				});
	        }
		};
		
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().addPerspectiveListener(perspectiveSwitchListener);
	}
	
	private synchronized void disposeFigures() {
		for (var f : figures) {
			f.dispose();
		}
		figures.clear();
	}
	
	private synchronized void recreateFigures() {
		disposeFigures();
		
		// Only set up figures if we're in this perspective. Otherwise they are closed so that they don't
		// use any resource while no-one is looking at them.
		// 
		// This also gets around multiple views (e.g. scripting and reflectometry) both having an active connection
		// to the backend, which then creates issues (e.g. which view should the plots take their size from?)
		// With this approach there is only ever one plotting view actually active at once - though from a user's perspective
		// it is still available in both places and will automatically resize the plot to fit whichever view is currently
		// active
		if (Objects.equals(thisPerspectiveId, currentPerspectiveId)) {
			for (int figNum : getFigures().getValue()) {
				var figure = new MatplotlibFigure(parent, SWT.NONE, getConnectionUrl().getValue(), figNum);
				figure.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
				figures.add(figure);
			}
			parent.layout();
		}
	}
	
	/**
	 * Destroy this view and it's child-views.
	 */
	@PreDestroy
	public void dispose() {
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().removePerspectiveListener(perspectiveSwitchListener);
		getFigures().removePropertyChangeListener(figuresChangedListener);
		getConnectionUrl().removePropertyChangeListener(urlListener);
		disposeFigures();
	}
	
	/**
	 * Gets the figure numbers shown by this view.
	 * @return the figure numbers
	 */
	protected abstract UpdatedValue<List<Integer>> getFigures();
	
	/**
	 * Get the connection URL of this view.
	 * @return the url
	 */
	protected abstract UpdatedValue<String> getConnectionUrl();
	
}
