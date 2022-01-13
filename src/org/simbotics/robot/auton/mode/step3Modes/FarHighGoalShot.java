package org.simbotics.robot.auton.mode.step3Modes;

import org.simbotics.robot.auton.AutonCommand;
import org.simbotics.robot.auton.AutonControl;
import org.simbotics.robot.auton.AutonOverride;
import org.simbotics.robot.auton.RobotComponent;
import org.simbotics.robot.auton.drive.DriveStraight;
import org.simbotics.robot.auton.drive.DriveStraightOutput;
import org.simbotics.robot.auton.drive.DriveTurnToGyroAngle;
import org.simbotics.robot.auton.drive.DriveWait;
import org.simbotics.robot.auton.drive.DriveStraightAtSpeed;
import org.simbotics.robot.auton.feeder.FeederSetSpeed;
import org.simbotics.robot.auton.mode.AutonBuilder;
import org.simbotics.robot.auton.mode.AutonMode;
import org.simbotics.robot.auton.shooter.ShooterSetHood;
import org.simbotics.robot.auton.shooter.ShooterWaitForSpeed;
import org.simbotics.robot.auton.util.AutonWait;

public class FarHighGoalShot implements AutonMode {


	@Override
	public void addToMode(AutonBuilder ab) {
		ab.addCommand(new ShooterWaitForSpeed(AutonControl.FAR_SHOT_RPM));
        ab.addCommand(new FeederSetSpeed(1.0));
        ab.addCommand(new AutonWait(1500));
        ab.addCommand(new FeederSetSpeed(0));
        
        ab.addCommand(new AutonOverride(RobotComponent.SHOOTER));
        ab.addCommand(new ShooterSetHood(false));
	}

}
