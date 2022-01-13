package org.simbotics.robot.auton.drive;

import org.simbotics.robot.auton.AutonCommand;
import org.simbotics.robot.auton.RobotComponent;

public class DriveWait extends AutonCommand{
	
	public DriveWait() {
		super(RobotComponent.DRIVE);
	}

	@Override
	public boolean calculate() {
		return true;
	}

	@Override
	public void override() {
		// TODO Auto-generated method stub
		
	}

}
