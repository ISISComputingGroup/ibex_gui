/**
 * 
 */
package uk.ac.stfc.isis.ibex.ui.logplotter;

import org.csstudio.trends.databrowser2.Messages;
import org.csstudio.trends.databrowser2.editor.DataBrowserEditor;
import org.csstudio.trends.databrowser2.model.AxisConfig;
import org.csstudio.trends.databrowser2.model.Model;
import org.csstudio.trends.databrowser2.model.PVItem;
import org.csstudio.trends.databrowser2.preferences.Preferences;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchPartConstants;
import org.eclipse.ui.internal.util.Util;
import org.eclipse.ui.part.ViewPart;


/**
 * The Class LogPlotterView which is the view which contains the log plot of individual blocks.
 */
public class LogPlotterView extends ViewPart {

	/**
	 * The ID for the view, used to import it externally.
	 */
	public static final String ID = "uk.ac.stfc.isis.ibex.ui.logplotter.LogPlotterView";
	private DataBrowserEditor editor;

	@Override
	public void createPartControl(Composite parent) {
		System.out.println("CREATE PART CTRL");
		// Create new editor
//		this.editor = DataBrowserEditor.createInstance();
//		if (editor == null) {
//			return;
//		}

		// Add received items
//		final Model model = editor.getModel();
//		final double period = Preferences.getScanPeriod();
//		try {
//			final PVItem item = new PVItem(pvAddress, period);
//			item.useDefaultArchiveDataSources();
//			// Add item to new axes
//			AxisConfig axisConfig = new AxisConfig(item.getDisplayName());
//			model.addAxis(axisConfig);
//		} catch (Exception ex) {
//			MessageDialog.openError(editor.getSite().getShell(), Messages.Error,
//					NLS.bind(Messages.ErrorFmt, ex.getMessage()));
//		}
	}

	public void setPv(String pvAddress) {
		// Add received items
		super.setPartName(getBlockName(pvAddress));
		final Model model = editor.getModel();
		final double period = Preferences.getScanPeriod();
		try {
			final PVItem item = new PVItem(pvAddress, period);
			item.useDefaultArchiveDataSources();
			// Add item to new axes
			AxisConfig axisConfig = new AxisConfig(item.getDisplayName());
			model.addAxis(axisConfig);
			
		} catch (Exception ex) {
			MessageDialog.openError(editor.getSite().getShell(), Messages.Error,
					NLS.bind(Messages.ErrorFmt, ex.getMessage()));
		}
	}
	
	private String getBlockName(String pvAddress) {
		String[] parts = pvAddress.split(":");
		return parts[parts.length - 1];
	}
	
	@Override
	public void setFocus() {

	}
}
