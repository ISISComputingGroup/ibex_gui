package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;

import java.beans.PropertyChangeListener;
import java.util.Objects;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.model.SettableUpdatedValue;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

public class MatplotlibToolbar {
	private final ToolBar toolBar;
	
	public final MatplotlibButton homeButton;
	public final MatplotlibButton backButton;
	public final MatplotlibButton forwardButton;
	public final MatplotlibButton panButton;
	public final MatplotlibButton zoomButton;
	
	private final MatplotlibFigureViewModel viewModel;

	private final String SYMBOLIC_PATH = "uk.ac.stfc.isis.ibex.ui.graphing";
	private final String HOME_ICON = "/resources/home.png";
	private final String BACK_ICON = "/resources/left-arrow.png";
	private final String FORWARD_ICON = "/resources/right-arrow.png";
	private final String ZOOM_ACTIVE = "/resources/zoom_active.png";
	private final String ZOOM_INACTIVE = "/resources/zoom_inactive.png";
	private final String PAN_ACTIVE = "/resources/pan_active.png";
	private final String PAN_INACTIVE = "/resources/pan_inactive.png";
	
	public static String ICON_INACTIVE = "inactive";
	public static String ICON_ACTIVE = "active";
	
	
	public MatplotlibToolbar(MatplotlibFigureViewModel viewModel, Composite container) {
		toolBar = new ToolBar(container, SWT.PUSH);
		toolBar.setLayoutData(new GridData(SWT.TOP, SWT.TOP, true, false));
		
		this.viewModel = viewModel;
		
		homeButton = new MatplotlibButton(viewModel.getHomeButtonState(), toolBar, MatplotlibButtonType.HOME, HOME_ICON);

		backButton = new MatplotlibButton(viewModel.getBackButtonState(), toolBar, MatplotlibButtonType.BACK, BACK_ICON);
		
		forwardButton = new MatplotlibButton(viewModel.getForwardButtonState(), toolBar, MatplotlibButtonType.FORWARD, FORWARD_ICON);
		
		var separator = new ToolItem(toolBar, SWT.SEPARATOR);
		
		panButton = new MatplotlibButton(viewModel.getPanButtonState(), toolBar, MatplotlibButtonType.PAN, PAN_ACTIVE, PAN_INACTIVE);
		
		zoomButton = new MatplotlibButton(viewModel.getZoomButtonState(), toolBar, MatplotlibButtonType.ZOOM, ZOOM_ACTIVE, ZOOM_INACTIVE);
	}
	
	public void dispose() {
		homeButton.dispose();
		backButton.dispose();
		forwardButton.dispose();
		panButton.dispose();
		zoomButton.dispose();
	}
	
	class MatplotlibButton {
		private ToolItem button;
		private final SettableUpdatedValue<MatplotlibButtonState> buttonState;
		private final PropertyChangeListener buttonListener;
		
		private final MatplotlibButtonType navType;
		private Image activeIcon;
		private Image inactiveIcon;
		
		public MatplotlibButton(UpdatedValue<MatplotlibButtonState> state, ToolBar toolBar, MatplotlibButtonType navType, String iconFilePath) {
			this(state, toolBar, navType, iconFilePath, iconFilePath);
		}

		public MatplotlibButton(UpdatedValue<MatplotlibButtonState> state, ToolBar toolBar, MatplotlibButtonType type, String activeIconFilePath, String inactiveIconFilePath) {
			this.navType = type;
			this.activeIcon = ResourceManager.getPluginImage(SYMBOLIC_PATH, activeIconFilePath);
			this.inactiveIcon = ResourceManager.getPluginImage(SYMBOLIC_PATH, inactiveIconFilePath);
			this.buttonState =  new SettableUpdatedValue<MatplotlibButtonState>(MatplotlibButtonState.DISABLED);
			this.buttonState.setValue(state.getValue());
			
			button = new ToolItem(toolBar, SWT.PUSH);
			button.setImage(inactiveIcon);
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					viewModel.navigatePlot(navType);
				}
			});
			this.setState(buttonState.getValue());
			
			buttonListener = state
					.addUiThreadPropertyChangeListener(e -> {
						if (!this.isDisposed()) {
							this.setState((MatplotlibButtonState) e.getNewValue());
						}
					});
		}
		
		public void dispose() {
			if (!activeIcon.isDisposed()) {
				activeIcon.dispose();
			}
			if (!inactiveIcon.isDisposed()) {
				inactiveIcon.dispose();
			}
			
			buttonState.removePropertyChangeListener(buttonListener);
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


