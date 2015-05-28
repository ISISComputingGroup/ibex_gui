package uk.ac.stfc.isis.ibex.product;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {
	
	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	@Override
    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new uk.ac.stfc.isis.ibex.product.ApplicationActionBarAdvisor(configurer);
    }
    
    @Override
    public void preWindowOpen() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setInitialSize(new Point(800, 600));
        configurer.setShowCoolBar(false);
        configurer.setShowStatusLine(false);
    }
    
    @Override
    public void postWindowCreate() {
    	super.postWindowCreate();
        final Shell shell = getWindowConfigurer().getWindow().getShell();
        shell.setMinimumSize(1100, 800);         
    }
    
    @Override
    public void postWindowOpen() {
    	Display display = Display.getCurrent();
    	Shell shell = display.getActiveShell();
    	
    	shell.setMaximized(true);
    	hideStatusBar();    	
    }
    
    /*
     * Hide the status bar created by CSS security
     */
    private void hideStatusBar() {
    	Display display = Display.getCurrent();
        Shell shell = display.getActiveShell();

        for (Control c: shell.getChildren()) {
            if (!isCanvasOrComposite(c) && !c.isDisposed()) {
                c.setVisible(false);
            }
        }

        shell.layout();
    }
	
    private boolean isCanvasOrComposite(Control control) {
        Class<?> cType = control.getClass();
        return cType == Canvas.class || cType == Composite.class;
    }
}
