package org.simbotics.robot.auton.mode.step4Modes;

import org.simbotics.robot.auton.AutonControl;
import org.simbotics.robot.auton.AutonOverride;
import org.simbotics.robot.auton.RobotComponent;
import org.simbotics.robot.auton.arm.ArmMoveTo;
import org.simbotics.robot.auton.drive.DriveShift;
import org.simbotics.robot.auton.drive.DriveStraight;
import org.simbotics.robot.auton.drive.DriveTurnToGyroAngle;
import org.simbotics.robot.auton.drive.DriveUntilBall;
import org.simbotics.robot.auton.drive.DriveWait;
import org.simbotics.robot.auton.feeder.FeederSetSpeed;
import org.simbotics.robot.auton.feeder.WaitUntilFeederLight;
import org.simbotics.robot.auton.intake.IntakeRollerSetSpeed;
import org.simbotics.robot.auton.mode.AutonBuilder;
import org.simbotics.robot.auton.mode.AutonMode;
import org.simbotics.robot.auton.util.AutonWait;

public class PickUpFrom1 implements AutonMode {


	@Override
	public void addToMode(AutonBuilder ab) {
		ab.addCommand(new DriveTurnToGyroAngle(270, 1, 2500));
		ab.addCommand(new DriveShift(true));
		ab.addCommand(new DriveStraight(180,0.75,10,3500));
		ab.addCommand(new DriveWait());
		ab.addCommand(new DriveShift(false));
		
		ab.addCommand(new AutonOverride(RobotComponent.ARM));
		ab.addCommand(new ArmMoveTo(AutonControl.ARM_PICKUP));
		ab.addCommand(new DriveTurnToGyroAngle(290, 2, 2000));
		ab.addCommand(new DriveWait());
		
		
		
		ab.addCommand(new DriveShift(false));
		ab.addCommand(new IntakeRollerSetSpeed(1.0));
		
		ab.addCommand(new DriveUntilBall(0.7, 1500));
		ab.addCommand(new DriveWait());
		ab.addCommand(new IntakeRollerSetSpeed(0.0));
		ab.addCommand(new AutonOverride(RobotComponent.ARM));
		ab.addCommand(new ArmMoveTo(1));
		
		ab.addCommand(new AutonWait(750));
		
		ab.addCommand(new IntakeRollerSetSpeed(1.0));
		ab.addCommand(new FeederSetSpeed(0.5));
		
		ab.addCommand(new WaitUntilFeederLight());
		
		ab.addCommand(new IntakeRollerSetSpeed(0.0));
		ab.addCommand(new FeederSetSpeed(0.0));
	}

}
