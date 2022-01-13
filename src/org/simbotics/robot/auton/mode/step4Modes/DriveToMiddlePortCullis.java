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
import org.simbotics.robot.auton.util.AutonWait;

public class DriveToMiddlePortCullis implements AutonMode {


	@Override
	public void addToMode(AutonBuilder ab) {
		ab.addCommand(new DriveShift(false));
		ab.addCommand(new AutonOverride(RobotComponent.ARM));
		ab.addCommand(new ArmMoveTo(AutonControl.ARM_LOWEST));
		ab.addCommand(new DriveTurnToGyroAngle(270, 2, 2500));
		ab.addCommand(new DriveShift(false));
		 //ab.addCommand(new AutonWait(250));
	      
	     
		ab.addCommand(new DriveStraightAtOutputForDistance(0.7, 2000,125));
	      
	    ab.addCommand(new DriveWait());
		
		ab.addCommand(new AutonOverride(RobotComponent.ARM));
		ab.addCommand(new ArmMoveTo(AutonControl.ARM_PICKUP));
		//ab.addCommand(new DriveTurnToGyroAngle(270, 2, 1500));
		ab.addCommand(new DriveWait());
		//ab.addCommand(new DriveShift(true));
		
	}

}
