package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.ResourceManager;

public class MatplotlibToolbar {
	private final ToolBar toolBar;
	
	private final Map<String, String> buttonIcons = Map.of(
			"home", "/resources/home.png",
			"back", "/resources/left-arrow.png",
			"forward", "/resources/right-arrow.png",
			"zoom_active", "/resources/zoom_active.png",
			"zoom_inactive", "/resources/zoom_inactive.png",
			"pan_active", "/resources/pan_active.png",
			"pan_inactive", "/resources/pan_inactive.png"
	);
	//ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.graphing", 
	
	public final MatplotlibButton homeButton;
	public final MatplotlibButton backButton;
	public final MatplotlibButton forwardButton;
	public final MatplotlibButton panButton;
	public final MatplotlibButton zoomButton;
	@SuppressWarnings("unused")
	// This is only added to the GUI, so not "used" but is still technically used
	private final ToolItem separator;
	
	private final MatplotlibFigureViewModel viewModel;
	
	public static String ICON_INACTIVE = "inactive";
	public static String ICON_ACTIVE = "active";
	
	public MatplotlibToolbar(MatplotlibFigureViewModel viewModel, Composite container) {
		toolBar = new ToolBar(container, SWT.PUSH);
		toolBar.setLayoutData(new GridData(SWT.TOP, SWT.TOP, true, false));
		
		this.viewModel = viewModel;
		
		homeButton = new MatplotlibButton(toolBar, "home", buttonIcons.get("home"));

		backButton = new MatplotlibButton(toolBar, "back", buttonIcons.get("back"));
		
		forwardButton = new MatplotlibButton(toolBar, "forward", buttonIcons.get("forward"));
		
		separator = new ToolItem(toolBar, SWT.SEPARATOR);
		
		panButton = new MatplotlibButton(toolBar, "pan", buttonIcons.get("pan_active"), buttonIcons.get("pan_inactive"));
		
		zoomButton = new MatplotlibButton(toolBar, "zoom", buttonIcons.get("zoom_active"), buttonIcons.get("zoom_inactive"));
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
		
		private String navType;
		private Image activeIcon;
		private Image inactiveIcon;
		
		public MatplotlibButton(ToolBar toolBar, String navType, String iconFilePath) {
			this.navType = navType;
			this.activeIcon = ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.graphing", iconFilePath);
			this.inactiveIcon = null;
			
			button = new ToolItem(toolBar, SWT.PUSH);
			button.setImage(activeIcon);
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					viewModel.navigatePlot(navType);
				}
			});
		}
		
		public MatplotlibButton(ToolBar toolBar, String navType, String activeIconFilePath, String inactiveIconFilePath) {
			this.navType = navType;
			this.activeIcon = ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.graphing", activeIconFilePath);
			this.inactiveIcon = ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.graphing", inactiveIconFilePath);
			
			button = new ToolItem(toolBar, SWT.PUSH);
			button.setImage(inactiveIcon);
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					viewModel.navigatePlot(navType);
				}
			});
		}
		
		public void setIcon(String iconStatus) {
			if (iconStatus == ICON_ACTIVE) {
				button.setImage(activeIcon);
			} else {
				button.setImage(inactiveIcon);
			}
		}
		
		public void dispose() {
			if (!activeIcon.isDisposed()) {
				activeIcon.dispose();
			} else if (!inactiveIcon.isDisposed()) {
				inactiveIcon.dispose();
			}
			
			button.dispose();
		}
		
	}
}	


