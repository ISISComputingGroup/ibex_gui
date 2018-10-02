package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks;

import java.util.List;
import java.util.stream.Collectors;

import uk.ac.stfc.isis.ibex.configserver.editing.DefaultName;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.model.ModelObject;


/**
 * The view model of the block editor panel.
 */
public class BlocksEditorViewModel extends ModelObject {
    EditableConfiguration config;
    
    private boolean editEnabled;
    private boolean copyDeleteEnabled;
    
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
    	List<String> blockNames = config.getAllBlocks().stream()
    			.map(b -> b.getName())
    			.collect(Collectors.toList());
    	
        DefaultName namer = new DefaultName(blockName);
        return namer.getUnique(blockNames);
    }
    
	private boolean editEnabled(List<EditableBlock> blocks) {
		boolean enabled = !blocks.isEmpty();
		
		for (EditableBlock block : blocks) {
			enabled &= block != null && block.isEditable();
		}
		return enabled;
	}
    
	/**
	 * Get whether the edit action is possible.
	 * @return True if the selected block(s) can be edited.
	 */
	public boolean getEditEnabled() {
		return editEnabled;
	}
	
	/**
	 * Set whether the edit action is possible.
	 * @param editEnabled True if the selected block(s) can be edited.
	 */
	public void setEditEnabled(boolean editEnabled) {
		firePropertyChange("editEnabled", this.editEnabled, this.editEnabled = editEnabled);
	}
	
	/**
	 * Get whether the copy/delete actions are possible.
	 * @return True if the selected block(s) can be copied/deleted.
	 */
	public boolean getCopyDeleteEnabled() {
		return copyDeleteEnabled;
	}
	
	/**
	 * Set whether the copy/delete actions are possible.
	 * @param copyDeleteEnabled True if the selected block(s) can be copied/deleted.
	 */
	public void setCopyDeleteEnabled(boolean copyDeleteEnabled) {
		firePropertyChange("copyDeleteEnabled", this.copyDeleteEnabled, this.copyDeleteEnabled = copyDeleteEnabled);
	}
	
	/**
	 * Set the blocks which have been selected.
	 * @param selected The blocks that have been selected.
	 */
	public void setSelectedBlocks(List<EditableBlock> selected) {
		if (selected.size() > 1) {
			setEditEnabled(false);
		} else {
			setEditEnabled(editEnabled(selected));
		}
		setCopyDeleteEnabled(editEnabled(selected));
	}
}

