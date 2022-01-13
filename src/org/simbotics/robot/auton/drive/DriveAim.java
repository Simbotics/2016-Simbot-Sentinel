package org.simbotics.robot.auton.drive;

import org.simbotics.robot.auton.AutonCommand;
import org.simbotics.robot.auton.RobotComponent;
import org.simbotics.robot.io.RobotOutput;
import org.simbotics.robot.io.SensorInput;
import org.simbotics.robot.util.SimLib;
import org.simbotics.robot.util.SimTargeting;

public class DriveAim extends AutonCommand{

	SimTargeting simTargeting;
	RobotOutput robotOut;
	SensorInput sensorIn;
	public DriveAim(long timeout) {
		super(RobotComponent.DRIVE,timeout);
		
		this.simTargeting = new SimTargeting();
		this.robotOut = RobotOutput.getInstance();
		this.sensorIn = SensorInput.getInstance();
		this.sensorIn.startImageProcessing();
	}
	
	public DriveAim() {
		this(-1);
	}

	@Override
	public boolean calculate() {
		
		double x = this.simTargeting.calculate();
		
		double leftDrive = SimLib.calcLeftTankDrive(x, 0.0);
		double rightDrive = SimLib.calcRightTankDrive(x, 0.0);
		
		robotOut.setDriveLeft(leftDrive);
		robotOut.setDriveRight(rightDrive);
		
		if(this.simTargeting.getTargetingState() == SimTargeting.State.DONE){
			this.sensorIn.stopImageProcessing();
			return true;
		}else{
			return false;
		}
		
	}

	@Override
	public void override() {
		this.sensorIn.stopImageProcessing();
		this.robotOut.setDriveLeft(0.0);
		this.robotOut.setDriveRight(0.0);	
	}
}
