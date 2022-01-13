package org.simbotics.robot.auton.drive;

import org.simbotics.robot.auton.AutonCommand;
import org.simbotics.robot.auton.RobotComponent;
import org.simbotics.robot.io.RobotOutput;
import org.simbotics.robot.io.SensorInput;
import org.simbotics.robot.util.SimBangBang;
import org.simbotics.robot.util.SimLib;
import org.simbotics.robot.util.SimPID;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveStraightAtSpeed extends AutonCommand{

	private SensorInput sensorIn;
	private RobotOutput robotOut;
	
	private SimPID gyroControl;
	private SimBangBang encBangBang;
	
	private double speed;
	private double time;
	
	private boolean firstCycle;
	
	private double gyroP;
	private double gyroI;
	private double gyroD;
	private double eps;
	
	private double bangMin;
	private double bangMax;
	
	private double target;
	
	public DriveStraightAtSpeed(double speed, long timeOut, double target){
		super(RobotComponent.DRIVE, timeOut);
		
		this.robotOut = RobotOutput.getInstance();
		this.sensorIn = SensorInput.getInstance();
		this.firstCycle = true;
		
		this.speed = speed;
		this.target = target;
		
		this.gyroP = SmartDashboard.getNumber("Drive Gyro P: ");
		this.gyroI = SmartDashboard.getNumber("Drive Gyro I: ");
		this.gyroD = SmartDashboard.getNumber("Drive Gyro D: ");
		this.eps = SmartDashboard.getNumber("Drive Gyro Eps: ");
		
		this.bangMin = SmartDashboard.getNumber("Drive Bang Min: ");
		this.bangMax = SmartDashboard.getNumber("Drive Bang Max: ");

		this.gyroControl = new SimPID (gyroP, gyroI, gyroD, eps);

		double signCorrectBangMin = Math.signum(speed)*this.bangMin;
		double signCorrectBangMax = Math.signum(speed)*this.bangMax;
        this.encBangBang = new SimBangBang(this.bangMax, this.bangMin);
	}
    
	public DriveStraightAtSpeed(double speed, long timeOut) {
		this(speed, timeOut, -1);
	}

	public DriveStraightAtSpeed(double speed, double absTarget){
		this(speed, -1, Math.abs(absTarget));
		
	}
	
	@Override
	public boolean calculate() {
		
		if(this.firstCycle) { 
			this.gyroControl.setDesiredValue(this.sensorIn.getAngle());
			this.encBangBang.setDesiredValue(this.speed);
			this.firstCycle = false;
		}
		
		double yVal = this.encBangBang.calculate(this.sensorIn.getDriveSpeedFPS());
		double xVal = this.gyroControl.calcPID(this.sensorIn.getAngle());
		
		double leftDrive = SimLib.calcLeftTankDrive(xVal, yVal);
		double rightDrive = SimLib.calcRightTankDrive(xVal, yVal);
		
        SmartDashboard.putNumber("Drive Straight As Speed out:", yVal);

		if(target > 0 && this.sensorIn.getDriveInches() > Math.abs(this.target)){
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
