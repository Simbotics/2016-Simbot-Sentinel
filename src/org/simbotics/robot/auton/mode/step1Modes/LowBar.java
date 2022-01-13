package org.simbotics.robot.auton.mode.step1Modes;

import org.simbotics.robot.auton.AutonControl;
import org.simbotics.robot.auton.arm.ArmMoveTo;
import org.simbotics.robot.auton.drive.DriveShift;
import org.simbotics.robot.auton.drive.DriveStraight;
import org.simbotics.robot.auton.drive.DriveWait;
import org.simbotics.robot.auton.mode.AutonBuilder;
import org.simbotics.robot.auton.mode.AutonMode;
import org.simbotics.robot.auton.shooter.ShooterSetSpeedUsingBangBang;
import org.simbotics.robot.auton.util.AutonWait;

public class LowBar implements AutonMode {


	@Override
	public void addToMode(AutonBuilder ab) {
	  ab.addCommand(new DriveShift(false));
      ab.addCommand(new ArmMoveTo(AutonControl.ARM_PICKUP));
      ab.addCommand(new AutonWait(200));
      
     
      ab.addCommand(new DriveStraight(226,3000)); // uses PID
      ab.addCommand(new DriveWait());
      ab.addCommand(new ShooterSetSpeedUsingBangBang(AutonControl.CLOSE_SHOT_RPM, 0.0, 0.8));
     
      
      
      
	}

}
