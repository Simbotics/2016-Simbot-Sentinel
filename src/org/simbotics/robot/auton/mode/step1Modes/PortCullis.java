package org.simbotics.robot.auton.mode.step1Modes;

import org.simbotics.robot.auton.AutonCommand;
import org.simbotics.robot.auton.AutonControl;
import org.simbotics.robot.auton.arm.ArmMoveTo;
import org.simbotics.robot.auton.drive.DriveShift;
import org.simbotics.robot.auton.drive.DriveStraight;
import org.simbotics.robot.auton.drive.DriveStraightAtOutputForDistance;
import org.simbotics.robot.auton.drive.DriveStraightOutput;
import org.simbotics.robot.auton.drive.DriveTurnToGyroAngle;
import org.simbotics.robot.auton.drive.DriveUntilOuterworks;
import org.simbotics.robot.auton.drive.DriveWait;
import org.simbotics.robot.auton.drive.DriveStraightAtSpeed;
import org.simbotics.robot.auton.mode.AutonBuilder;
import org.simbotics.robot.auton.mode.AutonMode;
import org.simbotics.robot.auton.shooter.ShooterSetSpeedUsingBangBang;
import org.simbotics.robot.auton.util.AutonWait;

public class PortCullis implements AutonMode {


	@Override
	public void addToMode(AutonBuilder ab) {
	  ab.addCommand(new DriveShift(false));
	  ab.addCommand(new ShooterSetSpeedUsingBangBang(AutonControl.CLOSE_SHOT_RPM, 0.0, 0.8));
      ab.addCommand(new ArmMoveTo(AutonControl.ARM_LOWEST));
      ab.addCommand(new AutonWait(1000));
      
      //ab.addCommand(new DriveStraight(140,3000)); // uses PID
     // ab.addCommand(new DriveStraightOutput(1.0, 2500));
      ab.addCommand(new DriveStraightAtOutputForDistance(1.0, 2800,140));
      
      ab.addCommand(new DriveWait());
      
      
      //ab.addCommand(new DriveShift(true));
      //ab.addCommand(new DriveUntilOuterworks(2000));
      //ab.addCommand(new DriveWait());
      //ab.addCommand(new DriveShift(true));
      //ab.addCommand(new AutonWait(AutonControl.DRIVE_UNTIL_OUTERWORKS_TIME));
      ab.addCommand(new DriveShift(false));
      ab.addCommand(new DriveTurnToGyroAngle(90,1,1500));
      ab.addCommand(new DriveWait());
      
      
      
	}

}