package org.simbotics.robot.auton.drive;

import org.simbotics.robot.auton.AutonCommand;
import org.simbotics.robot.auton.RobotComponent;
import org.simbotics.robot.io.RobotOutput;
import org.simbotics.robot.io.SensorInput;
import org.simbotics.robot.util.SimLib;
import org.simbotics.robot.util.SimPID;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;;

public class DriveStraightToTargetingDistance extends AutonCommand {
	
	private RobotOutput robotOut;
	private SensorInput sensorIn;
	
	private SimPID encControl;
	private SimPID gyroControl;
	
	private double gyroP;
	private double gyroI;
	private double gyroD;
	
	private double encP;
	private double encI;
	private double encD;
	
	private boolean firstCycle;
	private double target;
	
	public DriveStraightToTargetingDistance(double target) {
		this(target, 1.0, -1);
	}
	
	public DriveStraightToTargetingDistance(double target, long timeOut) {
		this(target, 1.0, timeOut);
	}
	
	public DriveStraightToTargetingDistance(double target, double maxSpeed, long timeOut) {
		super(RobotComponent.DRIVE, timeOut);
	
		this.robotOut = RobotOutput.getInstance();
		this.sensorIn = SensorInput.getInstance();
		this.sensorIn.startImageProcessing();
		this.target = target;
		
		this.firstCycle = true;
		
		this.encP = SmartDashboard.getNumber("Drive Enc P: ");
		this.encI = SmartDashboard.getNumber("Drive Enc I: ");
		this.encD = SmartDashboard.getNumber("Drive Enc D: ");
		
		this.gyroP = SmartDashboard.getNumber("Drive Gyro P: ");
		this.gyroI = SmartDashboard.getNumber("Drive Gyro I: ");
		this.gyroD = SmartDashboard.getNumber("Drive Gyro D: ");
		
		
		//TODO: figure out correct PID values
		this.encControl = new SimPID(this.encP, this.encI, this.encD, 0.5);
		this.encControl.setMaxOutput(maxSpeed);
		this.encControl.setMinDoneCycles(5);
		
		this.gyroControl = new SimPID(this.gyroP, this.gyroI, this.gyroD, 0.5);
		this.gyroControl.setMinDoneCycles(5);
	}
	
	@Override
	public boolean calculate() {
		if(this.firstCycle){
			this.firstCycle = false;
			
			double distanceToTarget = this.sensorIn.getParticleInfo().getDistanceToTarget();
			
			this.encControl.setDesiredValue(this.sensorIn.getDriveInches() + distanceToTarget - this.target);
			this.gyroControl.setDesiredValue(this.sensorIn.getAngle());
		}
		
		double yVal = this.encControl.calcPID(this.sensorIn.getDriveInches());
		double xVal = this.gyroControl.calcPID(this.sensorIn.getAngle());
		
		double leftDrive = SimLib.calcLeftTankDrive(xVal, yVal);
		double rightDrive = SimLib.calcRightTankDrive(xVal, yVal);
		
		if(this.encControl.isDone()){
			this.sensorIn.stopImageProcessing();
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
		this.sensorIn.stopImageProcessing();
		this.robotOut.setDriveLeft(0.0);
		this.robotOut.setDriveRight(0.0);
	}

}
