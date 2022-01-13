package org.simbotics.robot.auton.mode.step2Modes;

import org.simbotics.robot.auton.AutonControl;
import org.simbotics.robot.auton.AutonOverride;
import org.simbotics.robot.auton.RobotComponent;
import org.simbotics.robot.auton.arm.ArmMoveTo;
import org.simbotics.robot.auton.drive.DriveAim;
import org.simbotics.robot.auton.drive.DriveShift;
import org.simbotics.robot.auton.drive.DriveStraight;
import org.simbotics.robot.auton.drive.DriveTurnToGyroAngle;
import org.simbotics.robot.auton.drive.DriveWait;
import org.simbotics.robot.auton.mode.AutonBuilder;
import org.simbotics.robot.auton.mode.AutonMode;
import org.simbotics.robot.auton.shooter.ShooterSetHood;
import org.simbotics.robot.auton.shooter.ShooterSetSpeedUsingBangBang;

public class TurnToGoalFrom5 implements AutonMode {
	@Override
	public void addToMode(AutonBuilder ab) {
		
		ab.addCommand(new AutonOverride(RobotComponent.ARM));		
		ab.addCommand(new ArmMoveTo(1));
		ab.addCommand(new AutonOverride(RobotComponent.SHOOTER));
		ab.addCommand(new ShooterSetHood(true));
		
		ab.addCommand(new ShooterSetSpeedUsingBangBang(AutonControl.FAR_SHOT_RPM, 0.0, 1.0));
		//ab.addCommand(new AutonWait(500));
		
		ab.addCommand(new DriveShift(false));
		//ab.addCommand(new DriveStraight(12, 1, 2000));
		//ab.addCommand(new DriveWait());
		
		ab.addCommand(new DriveTurnToGyroAngle(145, 5, 2000));
		ab.addCommand(new DriveWait());
		
		ab.addCommand(new DriveStraight(55, 4.5, 1750));
		ab.addCommand(new DriveWait());
		
		ab.addCommand(new DriveTurnToGyroAngle(100, 2.5, 2000));
		ab.addCommand(new AutonOverride(RobotComponent.ARM));		
		ab.addCommand(new ArmMoveTo(AutonControl.ARM_PICKUP));
		ab.addCommand(new DriveWait());
		
		ab.addCommand(new DriveAim(2750));
		ab.addCommand(new DriveWait());
		
		
		//ab.addCommand(new DriveAim(3000));
		//ab.addCommand(new DriveWait());
	}
}
