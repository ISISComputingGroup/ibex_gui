package uk.ac.stfc.isis.ibex.ui.beamstatus.views;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;

public class TestMenu extends MenuManager {

    public TestMenu(String name) {
        // Actually want to do something like: new NewBlockHandler().createDialog(pvName);
        add(new Action("Testing") {
            @Override
            public void run() {
                System.out.println("Right click on " + name);
                super.run();
            }
        });
    }
    
}
