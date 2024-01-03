package uk.ac.stfc.isis.ibex.ui.beamstatus.views;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.apache.logging.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.beamstatus.FacilityPV;
import uk.ac.stfc.isis.ibex.configserver.ConfigServer;
import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;
import uk.ac.stfc.isis.ibex.ui.blocks.groups.BlocksMenu;
import uk.ac.stfc.isis.ibex.ui.blocks.presentation.Presenter;
import uk.ac.stfc.isis.ibex.ui.configserver.commands.NewBlockHandler;

/**
 * Right-click menu from Beam Information.
 * 
 */
public class BeamInfoMenu extends MenuManager {

	private static final String LOG_PLOTTER_PERSPECTIVE_ID = "uk.ac.stfc.isis.ibex.client.e4.product.perspective.logplotter";
	private static final Logger LOG = IsisLog.getLogger(BeamInfoMenu.class);
	static final ConfigServer SERVER = Configurations.getInstance().server();

	/**
	 * The constructor, creates the menu for when the specific facility PV is
	 * right-clicked on.
	 *
	 * @param facilityPV the selected PV
	 */
	public BeamInfoMenu(FacilityPV facilityPV) {
		this.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				// clear previously added menu actions
				removeAll();

				// Creating right-click menu
				// Add the option to add a block to a config, if there are any configs we can
				// write to.
				add(new Action("Add block to config: " + facilityPV.pv) { // Opening configuration dialog window
					@Override
					public void run() {

						try {
							var handler = new NewBlockHandler();
							handler.setLocal(false);
							handler.createDialog(facilityPV.pv);

						} catch (TimeoutException e) {
							LoggerUtils.logErrorWithStackTrace(LOG, e.getMessage(), e);
						} catch (IOException e) {
							LoggerUtils.logErrorWithStackTrace(LOG, e.getMessage(), e);
						}
						super.run();
					}

					@Override
					public boolean isEnabled() {
						return super.isEnabled() && SERVER.writableConfigsExist();
					}
				});

				add(new Action("Open in Log Plotter: " + facilityPV.pv) { // Opening log plotter window
					public void run() {
						if (BlocksMenu.canAddPlot()) {
							switchToLogPlotter();
							Presenter.pvHistoryPresenter().newDisplay(facilityPV.pv, facilityPV.pv);
						} else {
							Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
							MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
							messageBox.setText("Failed to open in Log Plotter");
							messageBox.setMessage("Make the Log Plotter perspective visible.");
							messageBox.open();
						}
					}
				});
			}
		});
	}

	/**
	 * Switching perspective to log plotter
	 */
	private static void switchToLogPlotter() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final IPerspectiveRegistry registry = workbench.getPerspectiveRegistry();
		final IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
		page.setPerspective(registry.findPerspectiveWithId(LOG_PLOTTER_PERSPECTIVE_ID));
	}

}
