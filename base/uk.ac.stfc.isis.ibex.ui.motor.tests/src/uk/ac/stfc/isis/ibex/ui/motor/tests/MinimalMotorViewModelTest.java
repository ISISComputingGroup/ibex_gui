 /*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2016 Science & Technology Facilities Council.
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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.ui.motor.tests;

import org.eclipse.swt.graphics.Color;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import uk.ac.stfc.isis.ibex.motor.MotorEnable;
import uk.ac.stfc.isis.ibex.ui.motor.displayoptions.MotorBackgroundPalette;
import uk.ac.stfc.isis.ibex.ui.motor.views.MinimalMotorViewModel;

/**
 * This class tests MinimalMotorViewModel
 */
public class MinimalMotorViewModelTest {

    @Mock
    private MotorBackgroundPalette palette;
    private MinimalMotorViewModel viewModel;
    private TestMotor testMotor;

    @Before
    public void setUp() {
        viewModel = new MinimalMotorViewModel();

        palette = Mockito.mock(MotorBackgroundPalette.class);
        Mockito.when(palette.getDisabledColor()).thenReturn(new Color(null, 0, 0, 0));
        Mockito.when(palette.getUnnamedColor()).thenReturn(new Color(null, 1, 1, 1));
        Mockito.when(palette.getMovingColor()).thenReturn(new Color(null, 2, 2, 2));
        Mockito.when(palette.getStoppedColor()).thenReturn(new Color(null, 3, 3, 3));

        testMotor = new TestMotor();

    }

    @Test
    public void GIVEN_motor_name_is_empty_THEN_motor_colour_is_unnamed_colour() {
        // Arrange
        testMotor.name = "";
        testMotor.enabled = MotorEnable.ENABLE;
        testMotor.moving = false;

        viewModel.setPalette(palette);
        viewModel.setMotor(testMotor);

        // Act
        Color color = viewModel.getColor();

        // Assert
        Assert.assertEquals(palette.getUnnamedColor(), color);
    }

    @Test
    public void GIVEN_motor_name_is_null_THEN_motor_colour_is_unnamed_colour() {
        // Arrange
        testMotor.name = null;
        testMotor.enabled = MotorEnable.ENABLE;

        viewModel.setPalette(palette);
        viewModel.setMotor(testMotor);

        // Act
        Color color = viewModel.getColor();

        // Assert
        Assert.assertEquals(palette.getUnnamedColor(), color);
    }

    @Test
    public void GIVEN_motor_is_not_moving_THEN_motor_colour_is_stopped_colour() {
        // Arrange
        testMotor.enabled = MotorEnable.ENABLE;

        viewModel.setPalette(palette);
        viewModel.setMotor(testMotor);

        // Act
        Color color = viewModel.getColor();

        // Assert
        Assert.assertEquals(palette.getStoppedColor(), color);
    }

    @Test
    public void GIVEN_motor_is_moving_THEN_motor_colour_is_moving_colour() {
        // Arrange
        testMotor.enabled = MotorEnable.ENABLE;
        testMotor.moving = true;

        viewModel.setPalette(palette);
        viewModel.setMotor(testMotor);

        // Act
        Color color = viewModel.getColor();

        // Assert
        Assert.assertEquals(palette.getMovingColor(), color);
    }

    @Test
    public void GIVEN_motor_is_disabled_THEN_motor_colour_is_disabled_colour() {
        // Arrange
        testMotor.enabled = MotorEnable.DISABLE;

        viewModel.setPalette(palette);
        viewModel.setMotor(testMotor);

        // Act
        Color color = viewModel.getColor();

        // Assert
        Assert.assertEquals(palette.getDisabledColor(), color);
    }

    @Test
    public void GIVEN_motor_is_unknown_THEN_motor_colour_is_unknown_colour() {
        // Arrange
        testMotor.enabled = MotorEnable.UNKNOWN;

        viewModel.setPalette(palette);
        viewModel.setMotor(testMotor);


        // Act
        Color color = viewModel.getColor();

        // Assert
        Assert.assertEquals(palette.getDisabledColor(), color);
    }

    @Test
    public void GIVEN_motor_is_set_WHEN_palette_has_not_been_set_THEN_no_exception_is_thrown() {
        try{
            // Act
            viewModel.setMotor(testMotor);
        } catch(Exception e){
            // Assert that no exception was thrown
            Assert.fail("Exception shouldn't have been thrown. " + e.toString());
        }
    }

}
