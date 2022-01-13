package org.simbotics.robot.auton.mode.step2Modes;

import org.simbotics.robot.auton.AutonCommand;
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
import org.simbotics.robot.auton.mode.step3Modes.FarHighGoalShot;
import org.simbotics.robot.auton.shooter.ShooterSetHood;
import org.simbotics.robot.auton.shooter.ShooterSetSpeedUsingBangBang;
import org.simbotics.robot.auton.util.AutonWait;
import org.simbotics.robot.auton.drive.DriveUntilOuterworks;

public class DriveUpToGoalFrom5ForHighGoal implements AutonMode {

	
	@Override
	public void addToMode(AutonBuilder ab) {
//      ab.addCommand(new ShooterSetHood(true));
		ab.addCommand(new AutonOverride(RobotComponent.ARM));		
		ab.addCommand(new ArmMoveTo(AutonControl.ARM_PICKUP));
		
		ab.addCommand(new DriveShift(true)); // high gear
		ab.addCommand(new DriveStraightAtOutputForDistance(1.0, 3000,90));
		ab.addCommand(new DriveWait());
		
		ab.addCommand(new DriveShift(false)); // low Gear
		ab.addCommand(new DriveStraightAtOutputForDistance(0.4, 2000,20));
		ab.addCommand(new DriveWait());
		
		

		
//		ab.addCommand(new AutonWait(1000));

		ab.addCommand(new DriveTurn(47.0, 1.0, 2000)); //decrease the timeout on comp bot
		ab.addCommand(new DriveWait());	
		
		ab.addCommand(new DriveStraightAtOutputForDistance(1.0, 2000,22));
		ab.addCommand(new DriveWait());

		
		ab.addCommand(new DriveSetOutput(0.3)); // keep pushing against the wall
		ab.addCommand(new AutonWait(250));
		
	}
}


