package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;

import java.beans.PropertyChangeListener;
import java.util.Objects;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.model.SettableUpdatedValue;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

/**
 * A class to contain and manage the plot navigation buttons.
 */
public class MatplotlibToolbar {
	
	private final ToolBar toolBar;
	
	private final MatplotlibButton homeButton;
	private final MatplotlibButton backButton;
	private final MatplotlibButton forwardButton;
	private final MatplotlibButton panButton;
	private final MatplotlibButton zoomButton;
	
	private final MatplotlibFigureViewModel viewModel;

	private static final String SYMBOLIC_PATH = "uk.ac.stfc.isis.ibex.ui.graphing";
	private static final String HOME_ICON = "/resources/home.png";
	private static final String BACK_ICON = "/resources/left-arrow.png";
	private static final String FORWARD_ICON = "/resources/right-arrow.png";
	private static final String ZOOM_ACTIVE = "/resources/zoom_active.png";
	private static final String ZOOM_INACTIVE = "/resources/zoom_inactive.png";
	private static final String PAN_ACTIVE = "/resources/pan_active.png";
	private static final String PAN_INACTIVE = "/resources/pan_inactive.png";
	
	/**
	 * Constructor for the toolbar.
	 * @param viewModel 
	 * @param container the container for buttons to be added to
	 */
	public MatplotlibToolbar(MatplotlibFigureViewModel viewModel, Composite container) {
		toolBar = new ToolBar(container, SWT.PUSH);
		toolBar.setLayoutData(new GridData(SWT.TOP, SWT.TOP, true, false));
		
		this.viewModel = viewModel;
		
		
		
		homeButton = new MatplotlibButton(viewModel.getHomeButtonState(), toolBar, 
				SelectionListener.widgetSelectedAdapter(e -> viewModel.navigatePlot(MatplotlibButtonType.HOME)), HOME_ICON, "Reset original view");

		backButton = new MatplotlibButton(viewModel.getBackButtonState(), toolBar, 
				SelectionListener.widgetSelectedAdapter(e -> viewModel.navigatePlot(MatplotlibButtonType.BACK)), BACK_ICON, "Back to previous view");
		
		forwardButton = new MatplotlibButton(viewModel.getForwardButtonState(), toolBar, 
				SelectionListener.widgetSelectedAdapter(e -> viewModel.navigatePlot(MatplotlibButtonType.FORWARD)), FORWARD_ICON, "Forward to next view");
		
		// This is used, but only to be added to the toolbar.
		@SuppressWarnings("unused")
		var separator = new ToolItem(toolBar, SWT.SEPARATOR);
		
		panButton = new MatplotlibButton(viewModel.getPanButtonState(), toolBar, 
				SelectionListener.widgetSelectedAdapter(e -> viewModel.navigatePlot(MatplotlibButtonType.PAN)), PAN_ACTIVE, PAN_INACTIVE, "Pan");
		
		zoomButton = new MatplotlibButton(viewModel.getZoomButtonState(), toolBar, 
				SelectionListener.widgetSelectedAdapter(e -> viewModel.navigatePlot(MatplotlibButtonType.ZOOM)), ZOOM_ACTIVE, ZOOM_INACTIVE, "Zoom");

	}
	
	/**
	 * Disposes this button.
	 */
	public void dispose() {
		viewModel.close();
		
		homeButton.dispose();
		backButton.dispose();
		forwardButton.dispose();
		panButton.dispose();
		zoomButton.dispose();
		
		toolBar.dispose();
	}
	
	/**
	 * Private inner class for the toolbar buttons.
	 */
	private static class MatplotlibButton {
		private ToolItem button;
		private Image activeIcon;
		private Image inactiveIcon;
		private String description;
		
		private final PropertyChangeListener buttonListener;
		
		private SettableUpdatedValue<MatplotlibButtonState> buttonState = new SettableUpdatedValue<MatplotlibButtonState>(MatplotlibButtonState.DISABLED);
		private UpdatedValue<MatplotlibButtonState> viewModelState;
		
		/**
		 * Constructor for buttons with one icon.
		 * @param state
		 * @param toolBar
		 * @param selectionListener
		 * @param iconFilePath
		 */
		MatplotlibButton(UpdatedValue<MatplotlibButtonState> state, ToolBar toolBar, SelectionListener selectionListener, String iconFilePath, String description) {
			this(state, toolBar, selectionListener, iconFilePath, iconFilePath, description);
		}
		
		/**
		 * Constructor for buttons with two icons.
		 * @param viewModelState
		 * @param toolBar
		 * @param type
		 * @param activeIconFilePath
		 * @param inactiveIconFilePath
		 */
		MatplotlibButton(UpdatedValue<MatplotlibButtonState> viewModelState, ToolBar toolBar, SelectionListener selectionListener, String activeIconFilePath, String inactiveIconFilePath, String description) {
			this.activeIcon = ResourceManager.getPluginImage(SYMBOLIC_PATH, activeIconFilePath);
			this.inactiveIcon = ResourceManager.getPluginImage(SYMBOLIC_PATH, inactiveIconFilePath);
			this.viewModelState = viewModelState;
			this.description = description;
			
			this.buttonState.setValue(viewModelState.getValue());
			button = new ToolItem(toolBar, SWT.PUSH);
			button.setImage(inactiveIcon);
			button.addSelectionListener(selectionListener);
			this.setState(buttonState.getValue());
			
			buttonListener = viewModelState
					.addUiThreadPropertyChangeListener(e -> {
						if (!this.isDisposed()) {
							this.setState((MatplotlibButtonState) e.getNewValue());
						}
					});
			/*
			 * button.addListener(SWT.MouseHover, new Listener() {
			 * 
			 * @Override public void handleEvent(Event e) { // why isn't this working? :(
			 * viewModel.getModel().setPlotMessage(description); } });
			 */
		}
		
		/**
		 * Dispose of this button.
		 */
		public void dispose() {
			// don't need to dispose of icons here as they are handled by the ResourceManager class
			
			viewModelState.removePropertyChangeListener(buttonListener);
			button.dispose();
		}
		
		private Boolean isDisposed() {
			return button.isDisposed();
		}
		
		private void setState(MatplotlibButtonState state) {
			buttonState.setValue(state);
			button.setEnabled(state.getButtonState());
			
			if (Objects.equals(state, MatplotlibButtonState.ENABLED_ACTIVE)) {
				button.setImage(activeIcon);
			} else if (Objects.equals(state, MatplotlibButtonState.ENABLED_INACTIVE)) {
				button.setImage(inactiveIcon);
			}
		}
		
	}
}	


