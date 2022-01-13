package org.simbotics.robot.auton.mode.step4Modes;

import org.simbotics.robot.auton.AutonCommand;
import org.simbotics.robot.auton.drive.DriveShift;
import org.simbotics.robot.auton.drive.DriveToBackablePosition;
import org.simbotics.robot.auton.drive.DriveStraight;
import org.simbotics.robot.auton.drive.DriveStraightOutput;
import org.simbotics.robot.auton.drive.DriveTurnToGyroAngle;
import org.simbotics.robot.auton.drive.DriveWait;
import org.simbotics.robot.auton.drive.DriveStraightAtSpeed;
import org.simbotics.robot.auton.mode.AutonBuilder;
import org.simbotics.robot.auton.mode.AutonMode;

public class DriveToMiddleFrom1 implements AutonMode {


	@Override
	public void addToMode(AutonBuilder ab) {
		ab.addCommand(new DriveTurnToGyroAngle(270, 1, 2500));
		ab.addCommand(new DriveShift(false));
		ab.addCommand(new DriveStraight(178,2, 3500));
		ab.addCommand(new DriveWait());
		
	}

}
