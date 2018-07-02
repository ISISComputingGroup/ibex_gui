package uk.ac.stfc.isis.ibex.ui.configserver.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;

import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.configserver.configuration.ConfigInfo;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.editing.DuplicateChecker;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.ConfigSelectionDialog;


/**
 * Handler for loading recent configurations.
 */
public class RecentItemsHandler extends DisablingConfigHandler<String> {
    
    private Map<String, Configuration> configs;

    /**
     * Instantiates the handler object and adds observers on the values of all
     * configurations available.
     */
    public RecentItemsHandler() {
        super(SERVER.load());
        configs = new HashMap<String, Configuration>();
    }


    @Override
    public void safeExecute(ExecutionEvent event) {
        updateObservers();
        List<String> recentItemsNames = Configurations.getInstance().recent();
        Collection<ConfigInfo> configsInDialog = SERVER.configsInfo().getValue();
        ArrayList<ConfigInfo> recentConfigs = new ArrayList<ConfigInfo>();
        System.out.println(recentItemsNames);
        
        for (String recentItemName : recentItemsNames) {
            for (ConfigInfo config : configsInDialog) {
                if (config.name().equals(recentItemName)) {
                    recentConfigs.add(config);
                    System.out.println(config.name());
                }
            }
        }
        
        
        ConfigSelectionDialog dialog = new ConfigSelectionDialog(shell(), "Load Recent Configuration",
                recentConfigs, false, false);
        if (dialog.open() == Window.OK) {
            String config = dialog.selectedConfig();
            Map<String, Set<String>> conflicts = getConflicts(config);
            if (conflicts.isEmpty()) {
                configService.uncheckedWrite(config);
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
