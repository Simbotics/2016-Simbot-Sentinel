package org.simbotics.robot.auton.drive;

import org.simbotics.robot.auton.AutonCommand;
import org.simbotics.robot.auton.RobotComponent;
import org.simbotics.robot.io.RobotOutput;
import org.simbotics.robot.io.SensorInput;
import org.simbotics.robot.util.SimLib;
import org.simbotics.robot.util.SimPID;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveUntilLandWithheadingLockWithOutput extends AutonCommand {
	
	private RobotOutput robotOut;
	private SensorInput sensorIn;
	
	private int cycle;
	private int hasLandedCycles =0;

	private SimPID gyroPID;
	private double gyroP;
	private double gyroI;
	private double gyroD;
	private double eps;
	
	private SimPID encPID;
	private double encP;
	private double encI;
	private double encD;
	private double encEps;
	
	private double output;

	private boolean firstCycle = true;
	private boolean hasGoneOver = false;
	private boolean hasLanded = false;


	public DriveUntilLandWithheadingLockWithOutput(double output, long timeOut) {
		super(RobotComponent.DRIVE, timeOut);
	
		this.robotOut = RobotOutput.getInstance();
		this.sensorIn = SensorInput.getInstance();
		this.cycle = 0;
		this.output = output;
		
		this.encP = SmartDashboard.getNumber("Drive Enc P: ");
		this.encI = SmartDashboard.getNumber("Drive Enc I: ");
		this.encD = SmartDashboard.getNumber("Drive Enc D: ");
		this.encEps = 1;
		
		this.gyroP = SmartDashboard.getNumber("Drive Gyro P: ");
		this.gyroI = SmartDashboard.getNumber("Drive Gyro I: ");
		this.gyroD = SmartDashboard.getNumber("Drive Gyro D: ");
		this.eps = SmartDashboard.getNumber("Drive Gyro Eps: ");
		this.gyroPID = new SimPID (gyroP, gyroI, gyroD, eps);
		
		this.encPID = new SimPID (this.encP, this.encI, this.encD, this.encEps);
		this.encPID.setDoneRange(0.5);
		this.encPID.setMaxOutput(1.0);
		this.encPID.setMinDoneCycles(25);
		
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
			this.hasLandedCycles++;
			if(this.hasLandedCycles > 5){
				yVal = this.encPID.calcPID(this.sensorIn.getDriveInches());
			}else{
				yVal = 0;
			}
		} else {
			yVal = this.output;			
		}
		double xVal = -this.gyroPID.calcPID(this.sensorIn.getAngle());
		
		double leftDrive = SimLib.calcLeftTankDrive(xVal, yVal);
		double rightDrive = SimLib.calcRightTankDrive(xVal, yVal);
		
		if(this.cycle > 10 && !this.hasLanded) {
			this.hasLanded = true;
			this.robotOut.setShifter(false);
			
			this.encPID.setDesiredValue(this.sensorIn.getDriveInches());
			this.gyroPID.setDesiredValue(this.sensorIn.getAngle());
		} else {
			this.robotOut.setDriveLeft(leftDrive);
			this.robotOut.setDriveRight(rightDrive);
		}
		
		if(this.encPID.isDone() && this.hasLanded) {
			this.robotOut.setDriveLeft(0);
			this.robotOut.setDriveLeft(0);
			return true;
		}
		
		return false;
	}

	@Override
	public void override() {
		this.robotOut.setDriveLeft(0.0);
		this.robotOut.setDriveRight(0.0);
	}

}
