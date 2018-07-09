package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks;

import java.util.ArrayList;
import java.util.Collection;

import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.configserver.editing.DefaultName;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.model.ModelObject;


/**
 * The view model of the block editor panel.
 */
public class BlocksEditorViewModel extends ModelObject {
    EditableConfiguration config;
    
    /**
     * Constructor for the block editor panel view model.
     * 
     * @param config 
     *                  The configuration used by the block editor panel.
     */
    public BlocksEditorViewModel(EditableConfiguration config) {
        this.config = config;
    }
    
    /**
     * This method is responsible for returning a unique name to the name passed so that two blocks don't have the same name.
     * 
     * @param blockName 
     *                  The name that should be changed.
     * @return
     *          The new unique name for the block.
     */
    public String getUniqueName(String blockName) {
        Collection<String> allBlocksInConfigNames = new ArrayList<String>();
        for (Block blockInConfig : config.getAllBlocks()) {
            allBlocksInConfigNames.add(blockInConfig.getName());
        }
        DefaultName namer = new DefaultName(blockName);
        return namer.getUnique(allBlocksInConfigNames);
    }
}

