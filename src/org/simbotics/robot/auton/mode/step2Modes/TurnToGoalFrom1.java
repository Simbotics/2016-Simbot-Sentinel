package org.simbotics.robot.auton.mode.step2Modes;

import org.simbotics.robot.auton.AutonControl;
import org.simbotics.robot.auton.AutonOverride;
import org.simbotics.robot.auton.RobotComponent;
import org.simbotics.robot.auton.drive.DriveAim;
import org.simbotics.robot.auton.drive.DriveShift;
import org.simbotics.robot.auton.drive.DriveTurn;
import org.simbotics.robot.auton.drive.DriveWait;
import org.simbotics.robot.auton.mode.AutonBuilder;
import org.simbotics.robot.auton.mode.AutonMode;
import org.simbotics.robot.auton.shooter.ShooterSetHood;
import org.simbotics.robot.auton.shooter.ShooterSetSpeedUsingBangBang;

public class TurnToGoalFrom1 implements AutonMode {

	
	@Override
	public void addToMode(AutonBuilder ab) {
		ab.addCommand(new DriveShift(false));
		ab.addCommand(new AutonOverride(RobotComponent.SHOOTER));
        ab.addCommand(new ShooterSetHood(true));
		ab.addCommand(new ShooterSetSpeedUsingBangBang(AutonControl.FAR_SHOT_RPM, 0.0, 1.0));
		ab.addCommand(new DriveTurn(-55,2,1500));
		ab.addCommand(new DriveWait());
		
		//ab.addCommand(new DriveStraight(40, 1, 3000));
		//ab.addCommand(new DriveWait());
		
		ab.addCommand(new DriveAim(4000));
		ab.addCommand(new DriveWait());
	}

}
