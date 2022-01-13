package org.simbotics.robot.auton.mode.step4Modes;

import org.simbotics.robot.auton.AutonCommand;
import org.simbotics.robot.auton.AutonControl;
import org.simbotics.robot.auton.AutonOverride;
import org.simbotics.robot.auton.RobotComponent;
import org.simbotics.robot.auton.arm.ArmMoveTo;
import org.simbotics.robot.auton.drive.DriveShift;
import org.simbotics.robot.auton.drive.DriveStraightAtOutputForDistance;
import org.simbotics.robot.auton.drive.DriveToBackablePosition;
import org.simbotics.robot.auton.drive.DriveStraight;
import org.simbotics.robot.auton.drive.DriveStraightOutput;
import org.simbotics.robot.auton.drive.DriveTurnToGyroAngle;
import org.simbotics.robot.auton.drive.DriveWait;
import org.simbotics.robot.auton.drive.DriveStraightAtSpeed;
import org.simbotics.robot.auton.mode.AutonBuilder;
import org.simbotics.robot.auton.mode.AutonMode;

public class FaceOuterworks implements AutonMode {


	@Override
	public void addToMode(AutonBuilder ab) {
		ab.addCommand(new DriveShift(false));
		ab.addCommand(new AutonOverride(RobotComponent.ARM));
		ab.addCommand(new ArmMoveTo(1));
		ab.addCommand(new DriveTurnToGyroAngle(270, 1, 5000));
		
		ab.addCommand(new DriveWait());
		
		//ab.addCommand(new DriveShift(true));
		
		//ab.addCommand(new DriveStraightAtOutputForDistance(-1.0,1500));
		//ab.addCommand(new DriveWait());
		
		
		
	}

}
