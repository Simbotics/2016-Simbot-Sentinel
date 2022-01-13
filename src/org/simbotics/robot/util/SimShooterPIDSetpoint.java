package org.simbotics.robot.util;

import org.simbotics.robot.io.SensorInput;

/*
 * output = predetermined output for that speed + P calculation
 */
public class SimShooterPIDSetpoint {

	private SensorInput sensorIn;
	private SimPID pid;
	
	private double desiredSpeed;
	
	public SimShooterPIDSetpoint(double desiredSpeed) {
		this.sensorIn = SensorInput.getInstance();
		this.pid = new SimPID(0.00025, 0, 0); // just using P for now. TODO: should we use I as well in case it never gets to the desired speed?
		this.desiredSpeed = desiredSpeed;
	}
	
	public double caclulate() {	
		// set value for a given speed plus PID for how far away we are from that speed
		return this.fixedOutput(this.desiredSpeed) + this.pid.calcPID(this.sensorIn.getShooterRPM());
	}
	
	public void setDesiredSpeed(double speed) {
		this.desiredSpeed = speed;
	}

	// line of best fit calculation
	private double fixedOutput(double desiredSpeed) {
		return 0.0000851282368608891 * desiredSpeed + 0.0337036630165938;
	}
	
}

