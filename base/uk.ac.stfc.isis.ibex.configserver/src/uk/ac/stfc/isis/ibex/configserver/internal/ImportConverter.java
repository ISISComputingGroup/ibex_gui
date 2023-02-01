/*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2023 Science & Technology Facilities Council.
 * All rights reserved.
 *
 * This program is distributed in the hope that it will be useful.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution.
 * EXCEPT AS EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM 
 * AND ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES 
 * OR CONDITIONS OF ANY KIND.  See the Eclipse Public License v1.0 for more details.
 *
 * You should have received a copy of the Eclipse Public License v1.0
 * along with this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or 
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.configserver.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.configuration.PVDefaultValue;
import uk.ac.stfc.isis.ibex.configserver.configuration.PVSet;
import uk.ac.stfc.isis.ibex.configserver.editing.DuplicateBlockNameException;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.configserver.internal.DisplayUtils;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;

/**
 * Handles the changes to be done when importing a component from another instrument.
 */
public final class ImportConverter {
	
	private ImportConverter() { }
	
	/**
	 * Adds the IOCs, blocks, and groups of the imported component to the local editable component.
	 * 
	 * @param source The imported component.
	 * @param destination The editable component that will be added.
	 * @param sourcePrefix The remote instrument prefix.
	 * @param destinationPrefix The local instrument prefix.
	 * @param editableIocs The local IOCs.
	 */
	public static void convert(Configuration source, EditableConfiguration destination, String sourcePrefix, String destinationPrefix, Collection<EditableIoc> editableIocs) {
		destination.setName(source.getName());
		destination.setDescription(source.description());
		destination.setIsProtected(source.isProtected());
		destination.setIsDynamic(source.isDynamic());
		
		convertIocs(source, destination, sourcePrefix, destinationPrefix, editableIocs);
		convertBlocks(source, destination, sourcePrefix, destinationPrefix);
		convertGroups(source, destination);
	}
	
	private static void convertIocs(Configuration source, EditableConfiguration destination, String sourcePrefix, String destinationPrefix, Collection<EditableIoc> editableIocs) {
		for (var ioc : source.getIocs()) {
			for (var editableIoc : editableIocs) {
				if (ioc.getName().equals(editableIoc.getName())) {
					editableIoc.setAutostart(ioc.getAutostart());
					editableIoc.setRestart(ioc.getRestart());
					
					editableIoc.setMacros(ioc.getMacros());
					
					editableIoc.setPvs(ioc.getPvs().stream()
							   .map(e -> new PVDefaultValue(e.getName().replace(sourcePrefix, destinationPrefix), e.getValue()))
							   .collect(Collectors.toList()));
					
					editableIoc.setPvSets(ioc.getPvSets().stream()
							   .map(e -> new PVSet(e.getName().replace(sourcePrefix, destinationPrefix), e.getEnabled()))
							   .collect(Collectors.toList()));
					
					destination.addIoc(editableIoc);
					break;
				}
			}
    	}
	}
	
	private static void convertBlocks(Configuration source, EditableConfiguration destination, String sourcePrefix, String destinationPrefix) {
		for (var block : source.getBlocks()) {
			var editableBlock = new EditableBlock(block);
			
			if (editableBlock.getIsLocal()) {
				editableBlock.setPV(editableBlock.getPV().replace(sourcePrefix, destinationPrefix));
			}
			
			try {
				destination.addNewBlock(editableBlock);
			} catch (DuplicateBlockNameException e) {
				LoggerUtils.logErrorWithStackTrace(IsisLog.getLogger(ImportConverter.class), e.getMessage(), e);
				new MessageDialog(Display.getCurrent().getActiveShell(), "Error", null,
	                    "Failed to add block " + block.getName() + ":\nBlock with this name already exists.",
	                    MessageDialog.ERROR, new String[] {"OK"}, 0).open();
			}
    	}
	}
	
	private static void convertGroups(Configuration source, EditableConfiguration destination) {
		// After blocks have been added to the destination configuration, add them to their respective groups.
		// We need to use the newly added Editable Blocks when we toggle the selection for each new Editable Group.
		
		var destinationBlocks = destination.getAllBlocks();
		var sourceGroups = source.getGroups()
						   		 .stream()
						   		 .filter(g -> DisplayUtils.filterNoneGroup(g.getName()))
						   		 .collect(Collectors.toList());
		
		// Create the destination groups.
		for (var sourceGroup : sourceGroups) {
    		var newGroup = destination.addNewGroup();
    		newGroup.setName(sourceGroup.getName());
    		
    		// Get the toggled blocks.
    		var blocksToToggle = new ArrayList<EditableBlock>();
    		for (var sourceBlockName : sourceGroup.getBlocks()) {
    			for (var destinationBlock : destinationBlocks) {
    				if (destinationBlock.getName().equals(sourceBlockName)) {
    					blocksToToggle.add(destinationBlock);
    					break;
    				}
    			}
    		}

    		newGroup.toggleSelection(blocksToToggle);
    	}
	}
}
