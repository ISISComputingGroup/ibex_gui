package uk.ac.stfc.isis.ibex.ui.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IViewLayout;

import uk.ac.stfc.isis.ibex.ui.banner.views.BannerView;
import uk.ac.stfc.isis.ibex.ui.blocks.views.BlocksView;
import uk.ac.stfc.isis.ibex.ui.dashboard.views.DashboardView;
import uk.ac.stfc.isis.ibex.ui.perspectives.switcher.PerspectiveSwitcherView;

public abstract class BasePerspective implements IPerspectiveFactory, IsisPerspective {
	
	public static final String ID = "uk.ac.stfc.isis.ibex.ui.perspectives.base"; //$NON-NLS-1$	
		
	public void createInitialLayout(IPageLayout layout) {					
		layout.setEditorAreaVisible(false);
		layout.setFixed(true);
		
		layout.addStandaloneView(DashboardView.ID, false, IPageLayout.TOP, 0.4f, IPageLayout.ID_EDITOR_AREA);
		layout.addStandaloneView(BannerView.ID, false, IPageLayout.BOTTOM, 0.15f, DashboardView.ID);
		layout.addStandaloneView(BlocksView.ID, false, IPageLayout.RIGHT, 0.3f, DashboardView.ID);
		layout.addStandaloneView(PerspectiveSwitcherView.ID, false, IPageLayout.LEFT, 0.15f, IPageLayout.ID_EDITOR_AREA);
	}
	
	@Override
	public int compareTo(IsisPerspective other) {
		return this.name().compareTo(other.name());
	}
	
	protected void lockView(IPageLayout layout, String viewID) {
		IViewLayout view = layout.getViewLayout(viewID);
		if (view != null) {
			view.setMoveable(false);
			view.setCloseable(true);
		}
	}
}
