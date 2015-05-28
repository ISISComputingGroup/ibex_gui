package uk.ac.stfc.isis.ibex.goniometer;

import uk.ac.stfc.isis.ibex.motor.MotorSetpoint;

public interface GoniometerModel {
	
	MotorSetpoint rUpper();
	
	MotorSetpoint rLower();
	
	MotorSetpoint cx();
	
	MotorSetpoint cy();
	
	MotorSetpoint theta();
	
	MotorSetpoint z();
}
