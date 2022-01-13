package org.simbotics.robot.auton.drive;

import org.simbotics.robot.auton.AutonCommand;
import org.simbotics.robot.auton.RobotComponent;
import org.simbotics.robot.io.RobotOutput;
import org.simbotics.robot.io.SensorInput;
import org.simbotics.robot.util.SimBangBang;
import org.simbotics.robot.util.SimLib;
import org.simbotics.robot.util.SimPID;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveStraightOutputHeadingLock extends AutonCommand{

	private SensorInput sensorIn;
	private RobotOutput robotOut;
	
	
	private double output;
	private double time;
	private SimPID gyroControl;

	private double gyroP;
	private double gyroI;
	private double gyroD;
	private double eps;
	private boolean firstCycle;


	
	public DriveStraightOutputHeadingLock(double output, long timeOut){
		super(RobotComponent.DRIVE, timeOut);
		
		this.robotOut = RobotOutput.getInstance();
		this.sensorIn = SensorInput.getInstance();
		this.output = output;
		
		this.gyroP = SmartDashboard.getNumber("Drive Gyro P: ");
		this.gyroI = SmartDashboard.getNumber("Drive Gyro I: ");
		this.gyroD = SmartDashboard.getNumber("Drive Gyro D: ");
		this.eps = SmartDashboard.getNumber("Drive Gyro Eps: ");
		this.gyroControl = new SimPID (gyroP, gyroI, gyroD, eps);
	}
    
	
	
	@Override
	public boolean calculate() {
		if(this.firstCycle) { 
			this.gyroControl.setDesiredValue(this.sensorIn.getAngle());
			this.firstCycle = false;
		}
		
		double yVal = this.output;
		double xVal = this.gyroControl.calcPID(this.sensorIn.getAngle());
		
		double leftDrive = SimLib.calcLeftTankDrive(xVal, yVal);
		double rightDrive = SimLib.calcRightTankDrive(xVal, yVal);
		
		this.robotOut.setDriveLeft(leftDrive);
		this.robotOut.setDriveRight(rightDrive);
		return false;
		
	}

	@Override
	public void override() {

		this.robotOut.setDriveLeft(0);
		this.robotOut.setDriveRight(0);
	}

}
