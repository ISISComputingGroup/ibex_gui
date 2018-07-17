package uk.ac.stfc.isis.ibex.ui.configserver.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;

import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.editing.DuplicateChecker;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.RecentConfigSelectionDialog;


/**
 * Handler for loading recent configurations.
 */
public class RecentConfigsHandler extends DisablingConfigHandler<String> {

    private Map<String, Configuration> configs;

    /**
     * Instantiates the handler object and adds observers on the values of all
     * configurations available.
     */
    public RecentConfigsHandler() {
        super(SERVER.load());
        configs = new HashMap<String, Configuration>();
    }

    @Override
    public void safeExecute(ExecutionEvent event) {
        updateObservers();
        List<String> recentConfigsNames = Configurations.getInstance().getRecent();
        ArrayList<String> recentConfigs = new ArrayList<String>();
        
        for (String recentConfigName : recentConfigsNames) {
            String justName = Configurations.getInstance().getRecentName(recentConfigName);
            String currentConfigName =  SERVER.currentConfig().getValue().getName();
            if (!justName.equals(currentConfigName)) {
                recentConfigs.add(recentConfigName);
            }
        }
        
        RecentConfigSelectionDialog dialog = new RecentConfigSelectionDialog(shell(), "Load Recent Configuration",
                recentConfigs);
        if (dialog.open() == Window.OK) {
            String config = Configurations.getInstance().getRecentName(dialog.selectedConfig());
            System.out.println(config);
            Map<String, Set<String>> conflicts = getConflicts(config);
            if (conflicts.isEmpty()) {
                configService.uncheckedWrite(config);
                Configurations.getInstance().addRecent(config);
            } else {
                new MessageDialog(shell(), "Conflicts in selected configuration", null, buildWarning(conflicts),
                        MessageDialog.WARNING, new String[] {"Ok"}, 0).open();
                safeExecute(event);
            }
        }
    }

    private void updateObservers() {
        for (String name : SERVER.configNames()) {
            if (!configs.containsKey(name)) {
                ForwardingObservable<Configuration> configObs = SERVER.config(name);
                configObs.addObserver(new BaseObserver<Configuration>() {
                    @Override
                    public void onValue(Configuration value) {
                        configs.put(value.getName(), value);
                    }
                });
            }
        }
    }

    private Map<String, Set<String>> getConflicts(String name) {
        Configuration config = configs.get(name);
        DuplicateChecker duplicateChecker = new DuplicateChecker();
        duplicateChecker.setBase(config);
        return duplicateChecker.checkOnLoad();
    }

    private String buildWarning(Map<String, Set<String>> conflicts) {
        boolean multi = (conflicts.size() > 1);
        StringBuilder sb = new StringBuilder();
        sb.append("Cannot load the selected configuration as it and its components contains duplicate blocks. "
                + "Conflicts detected for the following block" + (multi ? "s" : "") + ":\n\n");

        for (String block : conflicts.keySet()) {
            sb.append("Block \"" + block + "\" contained in:\n");
            Set<String> sources = conflicts.get(block);
            for (String source : sources) {
                sb.append("\u2022 " + source + "\n");
            }
            sb.append("\n");
        }
        sb.append(
                "Please rename or remove the duplicate block" + (multi ? "s" : "")
                        + " before loading this configuration.");
        return sb.toString();
    }

}
