package org.simbotics.robot.auton.mode.step2Modes;

import org.simbotics.robot.auton.AutonControl;
import org.simbotics.robot.auton.AutonOverride;
import org.simbotics.robot.auton.RobotComponent;
import org.simbotics.robot.auton.arm.ArmMoveTo;
import org.simbotics.robot.auton.arm.ArmOverride;
import org.simbotics.robot.auton.drive.DriveAim;
import org.simbotics.robot.auton.drive.DriveSetOutput;
import org.simbotics.robot.auton.drive.DriveShift;
import org.simbotics.robot.auton.drive.DriveStraight;
import org.simbotics.robot.auton.drive.DriveStraightAtOutputForDistance;
import org.simbotics.robot.auton.drive.DriveTurn;
import org.simbotics.robot.auton.drive.DriveWait;
import org.simbotics.robot.auton.mode.AutonBuilder;
import org.simbotics.robot.auton.mode.AutonMode;
import org.simbotics.robot.auton.shooter.ShooterSetHood;
import org.simbotics.robot.auton.shooter.ShooterSetSpeedUsingBangBang;
import org.simbotics.robot.auton.util.AutonWait;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveUpToGoalFrom1ForHighGoal implements AutonMode {
	
	@Override
	public void addToMode(AutonBuilder ab) {
		
		//ab.addCommand(new AutonOverride(RobotComponent.ARM));
		//ab.addCommand(new ArmMoveTo(1));
		
		//ab.addCommand(new DriveStraight(12, 3,1500));
		//ab.addCommand(new DriveWait());
		
		ab.addCommand(new DriveShift(false));
		ab.addCommand(new DriveTurn(-60, 2, 2000));
		ab.addCommand(new DriveWait());
		
		//ab.addCommand(new ArmOverride());
		//ab.addCommand(new ArmMoveTo(830));
		ab.addCommand(new DriveAim(1750));
		ab.addCommand(new DriveWait());
		
		//ab.addCommand(new AutonWait(3000)); // 2056 delay
		

		ab.addCommand(new DriveShift(true));
		
		ab.addCommand(new DriveStraightAtOutputForDistance(0.8,3000,85));// drive to the goal
		ab.addCommand(new DriveWait());
		
		
		ab.addCommand(new DriveShift(false));
		ab.addCommand(new DriveStraight(27,0.5,1500)); // drive to the goal
		ab.addCommand(new DriveWait());
		
	
		ab.addCommand(new DriveSetOutput(0.25)); // keep pushing against the wall
		ab.addCommand(new AutonWait(1000));
		//ab.addCommand(new DriveSetOutput(0.25)); // keep pushing against the wall
		
		
		
		
	}

}
