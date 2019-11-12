package uk.ac.stfc.isis.ibex.javafx;

import java.util.concurrent.CountDownLatch;

import org.eclipse.fx.ui.workbench3.FXViewPart;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Hack is heavily inspired by cs-studio e.g.
 * https://github.com/ControlSystemStudio/cs-studio/blob/87b849003769c9e93616f11d9873e2f9337d0314/applications/diag/diag-plugins/org.csstudio.diag.epics.pvtree/src/org/csstudio/diag/epics/pvtree/StandaloneApplication.java
 */
public class FXInitializeHack extends FXViewPart {
    @Override
    protected Scene createFxScene()
    {
        return null;
    }

    @Override
    protected void setFxFocus()
    {
        // NOP
    }
    
    public static CountDownLatch initializeUI()
    {
        // Creating an FXCanvas results in a combined
        // SWT and JavaFX setup with common UI thread.
        // Shell that's created as a parent for the FXCanvas is never shown.
    	Platform.setImplicitExit(false);
    	
        final var display = Display.getDefault();
        final Shell temp_shell = new Shell(display);

        // Would like to create an FXCanvas,
        // but that requires jfxswt.jar on the classpath.
        //   new FXCanvas(temp_shell, SWT.NONE);
        // The FXViewPart creates an FXCanvas and is
        // packaged in org.eclipse.fx.ui.workbench3
        // so we can access it:
        final FXInitializeHack hack = new FXInitializeHack();
        hack.createPartControl(temp_shell);
        temp_shell.close();
        
//        final Stage stage = new Stage();
//        stage.setTitle("IBEX background JFX initialization");

        final CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(latch::countDown);
        return latch;
    }
}
