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
import org.simbotics.robot.auton.drive.DriveTurnToGyroAngle;
import org.simbotics.robot.auton.drive.DriveWait;
import org.simbotics.robot.auton.mode.AutonBuilder;
import org.simbotics.robot.auton.mode.AutonMode;
import org.simbotics.robot.auton.shooter.ShooterSetSpeedUsingBangBang;
import org.simbotics.robot.auton.util.AutonWait;

public class DriveUpToGoalFrom4ForHighGoal implements AutonMode {
	public void addToMode(AutonBuilder ab) {
//      ab.addCommand(new ShooterSetHood(true));

		ab.addCommand(new AutonOverride(RobotComponent.ARM));		
		ab.addCommand(new ArmMoveTo(AutonControl.ARM_PICKUP));
		
		ab.addCommand(new AutonOverride(RobotComponent.SHOOTER));
		ab.addCommand(new ShooterSetSpeedUsingBangBang(AutonControl.CLOSE_SHOT_RPM, 0.0, 0.8));
		
		ab.addCommand(new DriveShift(false)); // low Gear
		
		ab.addCommand(new DriveAim(1000));
		ab.addCommand(new DriveWait());
		
		
		

		ab.addCommand(new DriveStraightAtOutputForDistance(1.0, 4000, 64)); // was 108
		ab.addCommand(new DriveWait());		
		
		ab.addCommand(new DriveTurnToGyroAngle(90, 1, 1500));
		ab.addCommand(new DriveWait());
		
		
		ab.addCommand(new DriveStraightAtOutputForDistance(0.6, 2000, 37)); // was 108
		ab.addCommand(new DriveWait());		
		
		ab.addCommand(new DriveShift(false)); // low Gear
		ab.addCommand(new DriveSetOutput(0.15)); // keep pushing against the wall
		ab.addCommand(new AutonWait(750));
	}
}
