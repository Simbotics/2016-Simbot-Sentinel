package org.simbotics.robot.auton.drive;

import org.simbotics.robot.auton.AutonCommand;
import org.simbotics.robot.auton.RobotComponent;
import org.simbotics.robot.io.RobotOutput;
import org.simbotics.robot.io.SensorInput;
import org.simbotics.robot.util.SimBangBang;
import org.simbotics.robot.util.SimLib;
import org.simbotics.robot.util.SimPID;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveStraightAtOutputForDistance extends AutonCommand{

	private SensorInput sensorIn;
	private RobotOutput robotOut;
	
	private SimPID gyroControl;
	
	private double power;
	private double time;
	
	private boolean firstCycle;
	
	private double gyroP;
	private double gyroI;
	private double gyroD;
	private double eps;
	private double startingPosition;
	
	private double target;
	
	public DriveStraightAtOutputForDistance(double power, long timeOut, double target){
		super(RobotComponent.DRIVE, timeOut);
		
		this.robotOut = RobotOutput.getInstance();
		this.sensorIn = SensorInput.getInstance();
		this.firstCycle = true;
		
		this.power = power;
		this.target = target;
		
		this.gyroP = SmartDashboard.getNumber("Drive Gyro P: ");
		this.gyroI = SmartDashboard.getNumber("Drive Gyro I: ");
		this.gyroD = SmartDashboard.getNumber("Drive Gyro D: ");
		this.eps = SmartDashboard.getNumber("Drive Gyro Eps: ");
		
		
		

		this.gyroControl = new SimPID (gyroP, gyroI, gyroD, eps);

	
       
	}
    
	public DriveStraightAtOutputForDistance(double power, long timeOut) {
		this(power, timeOut, -1);
	}

	public DriveStraightAtOutputForDistance(double power, double absTarget){
		this(power, -1, Math.abs(absTarget));
		
	}
	
	@Override
	public boolean calculate() {
		
		if(this.firstCycle) { 
			this.gyroControl.setDesiredValue(this.sensorIn.getAngle());
			this.startingPosition = this.sensorIn.getDriveInches();
			this.firstCycle = false;
		}
		
		double yVal = this.power;
		double xVal = -this.gyroControl.calcPID(this.sensorIn.getAngle());
		
		double leftDrive = SimLib.calcLeftTankDrive(xVal, yVal);
		double rightDrive = SimLib.calcRightTankDrive(xVal, yVal);
		
     

		if(target > 0 && this.sensorIn.getDriveInches() > Math.abs(this.target+this.startingPosition)){
			this.robotOut.setDriveLeft(0.0);
			this.robotOut.setDriveRight(0.0);
			return true;
		}else{
			this.robotOut.setDriveLeft(leftDrive);
			this.robotOut.setDriveRight(rightDrive);
			return false;
		}
	}

	@Override
	public void override() {

		this.robotOut.setDriveLeft(0);
		this.robotOut.setDriveRight(0);
	}

}