package uk.ac.stfc.isis.ibex.ui.configserver.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

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
    public void safeExecute(Shell shell) {
        updateObservers();
        List<String> recentConfigs = Configurations.getInstance()
                .getRecentlyLoadedConfigurations(SERVER.configsInfo().getValue());
        List<String> timeStamps = Configurations.getInstance()
                .getLastModifiedTimestampsOfRecentlyLoadedConfigurations(SERVER.configsInfo().getValue());
        createDialog(shell, recentConfigs, timeStamps);
    }

    private void createDialog(Shell shell, List<String> recentConfigs, List<String> timeStamps) {
        RecentConfigSelectionDialog dialog = new RecentConfigSelectionDialog(shell, "Load a Recent Configuration",
                recentConfigs, timeStamps);
        if (dialog.open() == Window.OK) {
            checksForConflicts(shell, dialog.selectedConfig());
        }
    }

    private void checksForConflicts(Shell shell, String config) {
        Map<String, Set<String>> conflicts = getConflicts(config);
        if (conflicts.isEmpty()) {
            loadsConfig(config);
        } else {
            createsErrorMessage(shell, conflicts);
        }
    }

    private void loadsConfig(String config) {
        configService.uncheckedWrite(config);
        Configurations.getInstance().addNameToRecentlyLoadedConfigList(config);
    }

    private void createsErrorMessage(Shell shell, Map<String, Set<String>> conflicts) {
        new MessageDialog(shell, "Conflicts in selected configuration", null, buildWarning(conflicts),
                MessageDialog.WARNING, new String[] {"Ok"}, 0).open();
        execute(shell);
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
        sb.append("Please rename or remove the duplicate block" + (multi ? "s" : "")
                + " before loading this configuration.");
        return sb.toString();
    }

}
