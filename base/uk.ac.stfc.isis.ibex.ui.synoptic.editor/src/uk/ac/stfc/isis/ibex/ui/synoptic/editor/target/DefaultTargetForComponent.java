
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2015
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.ui.synoptic.editor.target;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;

import uk.ac.stfc.isis.ibex.devicescreens.desc.ComponentType;
import uk.ac.stfc.isis.ibex.opis.Opi;
import uk.ac.stfc.isis.ibex.opis.desc.OpiDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.TargetDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.TargetType;

/**
 * Identifies the default targets for the specified component type.
 *
 */
public final class DefaultTargetForComponent {
    
    private DefaultTargetForComponent() {
    }

    public static Collection<TargetDescription> defaultTarget(final ComponentType compType) {
        Collection<TargetDescription> targetDescriptions = new ArrayList<>();

        Map<String, OpiDescription> opis = Opi.getDefault().descriptionsProvider().getDescriptions().getOpis();
        Collection<String> matchingTargetNames = Maps.filterValues(opis, typeMatches(compType)).keySet();
        for (String targetName : matchingTargetNames) {
            targetDescriptions.add(new TargetDescription(targetName, TargetType.OPI));
        }
        
        if (targetDescriptions.size() == 0) {
            targetDescriptions.add(new TargetDescription("NONE", TargetType.OPI));
        }

        return targetDescriptions;
    }
    
    private static Predicate<OpiDescription> typeMatches(final ComponentType compType) {
        return new Predicate<OpiDescription>() {
            @Override
            public boolean apply(OpiDescription opiDescription) {
                return opiDescription.getType().equals(compType.toString());
            }
        };
    }
}
