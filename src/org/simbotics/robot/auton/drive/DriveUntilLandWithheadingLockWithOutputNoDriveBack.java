package org.simbotics.robot.auton.drive;

import org.simbotics.robot.auton.AutonCommand;
import org.simbotics.robot.auton.RobotComponent;
import org.simbotics.robot.io.RobotOutput;
import org.simbotics.robot.io.SensorInput;
import org.simbotics.robot.util.SimLib;
import org.simbotics.robot.util.SimPID;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveUntilLandWithheadingLockWithOutputNoDriveBack extends AutonCommand {
	
	private RobotOutput robotOut;
	private SensorInput sensorIn;
	
	private int cycle;
	

	private SimPID gyroPID;
	private double gyroP;
	private double gyroI;
	private double gyroD;
	private double eps;
	
	;
	
	private double output;

	private boolean firstCycle = true;
	private boolean hasGoneOver = false;
	private boolean hasLanded = false;


	public DriveUntilLandWithheadingLockWithOutputNoDriveBack(double output, long timeOut) {
		super(RobotComponent.DRIVE, timeOut);
	
		this.robotOut = RobotOutput.getInstance();
		this.sensorIn = SensorInput.getInstance();
		this.cycle = 0;
		this.output = output;
		
		
		
		this.gyroP = SmartDashboard.getNumber("Drive Gyro P: ");
		this.gyroI = SmartDashboard.getNumber("Drive Gyro I: ");
		this.gyroD = SmartDashboard.getNumber("Drive Gyro D: ");
		this.eps = SmartDashboard.getNumber("Drive Gyro Eps: ");
		this.gyroPID = new SimPID (gyroP, gyroI, gyroD, eps);
		
		
		
	}
		
	@Override
	public boolean calculate() {		
		if(this.firstCycle) { 
			this.gyroPID.setDesiredValue(this.sensorIn.getAngle());
			this.firstCycle = false;
		}

		double zAvisValue = this.sensorIn.getZAxisValue();
		
		double yVal;
		
		
		
		
		
		
		if (zAvisValue > 15) {
			this.hasGoneOver = true;
		}
		
		if(zAvisValue < 3 && this.hasGoneOver) {
			this.cycle++;
		} else {
			this.cycle = 0;
		}
		
		
		
		if(this.hasLanded) {
			yVal = -0.2;
		} else {
			yVal = this.output;			
		}
		double xVal = -this.gyroPID.calcPID(this.sensorIn.getAngle());
		
		double leftDrive = SimLib.calcLeftTankDrive(xVal, yVal);
		double rightDrive = SimLib.calcRightTankDrive(xVal, yVal);
		
		if(this.cycle > 5 && !this.hasLanded) {
			this.hasLanded = true;
			this.robotOut.setShifter(false);
			
		
			this.gyroPID.setDesiredValue(this.sensorIn.getAngle());
		} else {
			this.robotOut.setDriveLeft(leftDrive);
			this.robotOut.setDriveRight(rightDrive);
		}
		
		if(this.hasLanded) {
			this.robotOut.setDriveLeft(leftDrive);
			this.robotOut.setDriveLeft(rightDrive);
			if(this.cycle>10){
				return true;
			}else{
				return false;
			}
			
		}
		
		return false;
	}

	@Override
	public void override() {
		this.robotOut.setDriveLeft(0.0);
		this.robotOut.setDriveRight(0.0);
	}

}
