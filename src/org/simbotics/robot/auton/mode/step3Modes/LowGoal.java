package org.simbotics.robot.auton.mode.step3Modes;

import org.simbotics.robot.auton.AutonCommand;
import org.simbotics.robot.auton.AutonOverride;
import org.simbotics.robot.auton.RobotComponent;
import org.simbotics.robot.auton.arm.ArmMoveTo;
import org.simbotics.robot.auton.arm.ArmOverride;
import org.simbotics.robot.auton.drive.DriveSetOutput;
import org.simbotics.robot.auton.drive.DriveShift;
import org.simbotics.robot.auton.drive.DriveStraight;
import org.simbotics.robot.auton.drive.DriveStraightOutput;
import org.simbotics.robot.auton.drive.DriveTurnToGyroAngle;
import org.simbotics.robot.auton.drive.DriveWait;
import org.simbotics.robot.auton.drive.DriveStraightAtSpeed;
import org.simbotics.robot.auton.feeder.FeederSetSpeed;
import org.simbotics.robot.auton.intake.IntakeRollerSetSpeed;
import org.simbotics.robot.auton.mode.AutonBuilder;
import org.simbotics.robot.auton.mode.AutonMode;
import org.simbotics.robot.auton.shooter.ShooterSetHood;
import org.simbotics.robot.auton.shooter.ShooterWaitForSpeed;
import org.simbotics.robot.auton.util.AutonWait;

public class LowGoal implements AutonMode {


	@Override
	public void addToMode(AutonBuilder ab) {
		
		ab.addCommand(new AutonOverride(RobotComponent.SHOOTER)); // turn off the shooter
		
		ab.addCommand(new FeederSetSpeed(-1.0));
		ab.addCommand(new IntakeRollerSetSpeed(-1.0));
		
		ab.addCommand(new AutonWait(3000));
		ab.addCommand(new ArmOverride());
		ab.addCommand(new ArmMoveTo(280));
		//ab.addCommand(new AutonWait(2000));
		//ab.addCommand(new ArmOverride());
		//ab.addCommand(new ArmMoveTo(1));
		//ab.addCommand(new AutonWait(500));
		//ab.addCommand(new ArmOverride());
		//ab.addCommand(new ArmMoveTo(290));
		ab.addCommand(new AutonWait(1000));
		ab.addCommand(new FeederSetSpeed(0));
		ab.addCommand(new IntakeRollerSetSpeed(0));
		ab.addCommand(new DriveStraight(-48,5,1500));
		ab.addCommand(new DriveWait());
		
		ab.addCommand(new DriveTurnToGyroAngle(270, 1, 2000));
		ab.addCommand(new DriveWait());
		
		
	}

}
