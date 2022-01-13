package org.simbotics.robot.auton.shooter;

import org.simbotics.robot.auton.AutonCommand;
import org.simbotics.robot.auton.RobotComponent;
import org.simbotics.robot.io.SensorInput;

public class ShooterWaitForSpeed extends AutonCommand {

	private SensorInput sensorIn;
	private double speed;
	
	public ShooterWaitForSpeed(double speed) {
		super(RobotComponent.SHOOTER);
		// TODO Auto-generated constructor stub
		this.speed = speed;
		this.sensorIn = SensorInput.getInstance();
	}

	public boolean checkAndRun() {
		double curSpeed = this.sensorIn.getShooterRPM();
		
		return curSpeed > this.speed;
	}
	
	@Override
	public boolean calculate() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void override() {
		// TODO Auto-generated method stub
		
	}


}
