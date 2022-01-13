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

public class DriveUpToGoalFrom2ForLowGoal implements AutonMode {

	
	@Override
	public void addToMode(AutonBuilder ab) {
        //ab.addCommand(new ShooterSetHood(true));
		
		
		ab.addCommand(new AutonOverride(RobotComponent.ARM));
		ab.addCommand(new ArmMoveTo(1));
		
		ab.addCommand(new DriveShift(false)); // low Gear
		//ab.addCommand(new DriveTurnToGyroAngle(90,1.5, 1000)); // eps of 1 degree 
		//ab.addCommand(new DriveWait());
		
		//ab.addCommand(new DriveShift(true));
		ab.addCommand(new DriveStraight(90, 0.5, 2500));
		ab.addCommand(new DriveWait());
		
		ab.addCommand(new DriveShift(false));
		ab.addCommand(new DriveTurn(-50, 1.0, 1500));
		ab.addCommand(new DriveWait());
		
		ab.addCommand(new DriveAim(2000));
		ab.addCommand(new DriveWait());
		
		
		ab.addCommand(new DriveShift(true));
		ab.addCommand(new DriveStraightAtOutputForDistance(1.0,3000,86));// drive to the goal
		ab.addCommand(new DriveWait());
		
		ab.addCommand(new DriveShift(false));
		
		
		
		
		ab.addCommand(new DriveSetOutput(0.4)); // keep pushing against the wall
		ab.addCommand(new AutonWait(500));
		
		
		
	}

}
