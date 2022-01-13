package org.simbotics.robot.auton.mode.step1Modes;

import org.simbotics.robot.auton.AutonCommand;
import org.simbotics.robot.auton.drive.DriveStraight;
import org.simbotics.robot.auton.drive.DriveStraightOutput;
import org.simbotics.robot.auton.drive.DriveTurnToGyroAngle;
import org.simbotics.robot.auton.drive.DriveWait;
import org.simbotics.robot.auton.drive.DriveStraightAtSpeed;
import org.simbotics.robot.auton.mode.AutonBuilder;
import org.simbotics.robot.auton.mode.AutonMode;

public class DriveStraightAlign implements AutonMode {


	@Override
	public void addToMode(AutonBuilder ab) {
        //ab.addCommand(new DriveStraightAtSpeed(2.0, 5000));
		ab.addCommand(new DriveStraight(36));
        //ab.addCommand(new DriveStraightOutput(1.0, 3000));
		//ab.addCommand(new DriveTurnAbsolute(90,2000));
		ab.addCommand(new DriveWait());
	}

}
