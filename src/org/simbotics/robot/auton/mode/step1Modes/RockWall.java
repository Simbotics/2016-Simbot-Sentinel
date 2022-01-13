package org.simbotics.robot.auton.mode.step1Modes;

import org.simbotics.robot.auton.AutonCommand;
import org.simbotics.robot.auton.drive.DriveUntilLandWithheadingLockWithOutput;
import org.simbotics.robot.auton.AutonControl;
import org.simbotics.robot.auton.arm.ArmMoveTo;
import org.simbotics.robot.auton.drive.DriveShift;
import org.simbotics.robot.auton.drive.DriveStraight;
import org.simbotics.robot.auton.drive.DriveStraightOutput;
import org.simbotics.robot.auton.drive.DriveTurnToGyroAngle;
import org.simbotics.robot.auton.drive.DriveUntilLandWithheadingLockWithOutputNoDriveBack;
import org.simbotics.robot.auton.drive.DriveUntilOuterworks;
import org.simbotics.robot.auton.drive.DriveWait;
import org.simbotics.robot.auton.drive.DriveStraightAtSpeed;
import org.simbotics.robot.auton.mode.AutonBuilder;
import org.simbotics.robot.auton.mode.AutonMode;
import org.simbotics.robot.auton.shooter.ShooterSetSpeedUsingBangBang;
import org.simbotics.robot.auton.util.AutonWait;

public class RockWall implements AutonMode {


	@Override
	public void addToMode(AutonBuilder ab) {
	  ab.addCommand(new DriveShift(true));
	  ab.addCommand(new ArmMoveTo(1));
	  //ab.addCommand(new DriveUntilLandWithheadingLockWithOutput(1.0, -1));
	  //ab.addCommand(new DriveWait());
	  ab.addCommand(new ShooterSetSpeedUsingBangBang(AutonControl.CLOSE_SHOT_RPM, 0.0, 1.0));
	  ab.addCommand(new AutonWait(250));
	 // ab.addCommand(new DriveStraightOutput(1.0, 2000));
      //ab.addCommand(new DriveShift(false));
	  ab.addCommand(new DriveUntilLandWithheadingLockWithOutputNoDriveBack(1.0, 3000));
	  ab.addCommand(new DriveWait());
	  ab.addCommand(new DriveShift(false));
      ab.addCommand(new DriveTurnToGyroAngle(90,1,1500));
     // ab.addCommand(new DriveUntilOuterworks(2000));
     // ab.addCommand(new DriveWait());
      //ab.addCommand(new DriveShift(true));
      //ab.addCommand(new AutonWait(AutonControl.DRIVE_UNTIL_OUTERWORKS_TIME));
     
      
      ab.addCommand(new DriveWait());
	}

}