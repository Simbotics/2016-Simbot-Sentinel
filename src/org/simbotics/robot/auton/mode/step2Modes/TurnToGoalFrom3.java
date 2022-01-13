package org.simbotics.robot.auton.mode.step2Modes;

import org.simbotics.robot.auton.AutonControl;
import org.simbotics.robot.auton.AutonOverride;
import org.simbotics.robot.auton.RobotComponent;
import org.simbotics.robot.auton.arm.ArmMoveTo;
import org.simbotics.robot.auton.drive.DriveAim;
import org.simbotics.robot.auton.drive.DriveSetOutput;
import org.simbotics.robot.auton.drive.DriveShift;
import org.simbotics.robot.auton.drive.DriveStraight;
import org.simbotics.robot.auton.drive.DriveStraightAtOutputForDistance;
import org.simbotics.robot.auton.drive.DriveTurn;
import org.simbotics.robot.auton.drive.DriveTurnToGyroAngle;
import org.simbotics.robot.auton.drive.DriveWait;
import org.simbotics.robot.auton.mode.AutonBuilder;
import org.simbotics.robot.auton.mode.AutonMode;
import org.simbotics.robot.auton.shooter.ShooterSetHood;
import org.simbotics.robot.auton.shooter.ShooterSetSpeedUsingBangBang;
import org.simbotics.robot.auton.util.AutonWait;

public class TurnToGoalFrom3 implements AutonMode {
	public void addToMode(AutonBuilder ab) {
		
		ab.addCommand(new AutonOverride(RobotComponent.ARM));		
		ab.addCommand(new ArmMoveTo(AutonControl.ARM_PICKUP));
		ab.addCommand(new AutonOverride(RobotComponent.SHOOTER));
		ab.addCommand(new ShooterSetHood(true));
		
		ab.addCommand(new ShooterSetSpeedUsingBangBang(AutonControl.FAR_SHOT_RPM, 0.0, 1.0));
		ab.addCommand(new AutonWait(500));
		
		//ab.addCommand(new DriveStraight(6, 1, 1500));
		ab.addCommand(new DriveWait());
		ab.addCommand(new DriveAim(3000));
		ab.addCommand(new DriveWait());
	}
}
