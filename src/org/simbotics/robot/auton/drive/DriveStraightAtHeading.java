package org.simbotics.robot.auton.drive;

import org.simbotics.robot.auton.AutonCommand;
import org.simbotics.robot.auton.RobotComponent;
import org.simbotics.robot.io.RobotOutput;
import org.simbotics.robot.io.SensorInput;
import org.simbotics.robot.util.SimLib;
import org.simbotics.robot.util.SimPID;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;;

public class DriveStraightAtHeading extends AutonCommand {
	
	private RobotOutput robotOut;
	private SensorInput sensorIn;
	
	private SimPID encControl;
	private SimPID gyroControl;
	
	private double gyroP;
	private double gyroI;
	private double gyroD;
	private double gyroEps;
	
	private double encP;
	private double encI;
	private double encD;
	private double encEps; 
	
	private boolean firstCycle;
	private double target;
	private double heading;
	
	public DriveStraightAtHeading(double target) {
		this(target,-1, 1.0,SmartDashboard.getNumber("Drive Enc Eps: "), -1);
	}
	
	public DriveStraightAtHeading(double target, long timeOut) {
		this(target,-1, 1.0,SmartDashboard.getNumber("Drive Enc Eps: "), timeOut);
	}
	
	public DriveStraightAtHeading(double target, double eps, long timeOut){
		this(target,-1,1.0,eps,timeOut);
	}
	
	public DriveStraightAtHeading(double target,double heading, double maxSpeed, double eps,long timeOut) {
		super(RobotComponent.DRIVE, timeOut);
	
		this.robotOut = RobotOutput.getInstance();
		this.sensorIn = SensorInput.getInstance();
		this.heading = heading;
		this.target = target;
		this.firstCycle = true;
		
		if(this.robotOut.getHighGear()){
			this.encP = SmartDashboard.getNumber("Drive P High: ");
			this.encI = SmartDashboard.getNumber("Drive I High: ");
			this.encD = SmartDashboard.getNumber("Drive D High: ");
		}else{
			this.encP = SmartDashboard.getNumber("Drive Enc P: ");
			this.encI = SmartDashboard.getNumber("Drive Enc I: ");
			this.encD = SmartDashboard.getNumber("Drive Enc D: ");
		}
		this.encEps = eps;
		this.gyroP = SmartDashboard.getNumber("Drive Gyro P: ");
		this.gyroI = SmartDashboard.getNumber("Drive Gyro I: ");
		this.gyroD = SmartDashboard.getNumber("Drive Gyro D: ");
		this.gyroEps = SmartDashboard.getNumber("Drive Gyro Eps: ");
		
		
		
		//TODO: figure out correct PID values
		this.encControl = new SimPID(this.encP,this.encI,this.encD);
		this.encControl.setDoneRange(0.5);
		this.encControl.setMaxOutput(maxSpeed);
		this.encControl.setMinDoneCycles(5);
		
		this.gyroControl = new SimPID(this.gyroP,this.gyroI,this.gyroD,this.gyroEps);
		this.gyroControl.setDoneRange(0.5);
		this.gyroControl.setMinDoneCycles(5);
	}
	

	@Override
	public boolean calculate() {
		if(this.firstCycle){
			this.firstCycle = false;
			this.encControl.setDesiredValue(this.sensorIn.getDriveInches() + this.target);
			if(this.heading == -1){
				this.gyroControl.setDesiredValue(this.sensorIn.getAngle());
			}else{
				this.gyroControl.setDesiredValue(this.heading);
			}
		}
		
		double yVal = this.encControl.calcPID(this.sensorIn.getDriveInches());
		double xVal = -this.gyroControl.calcPID(this.sensorIn.getAngle());
		
		double leftDrive = SimLib.calcLeftTankDrive(xVal, yVal);
		double rightDrive = SimLib.calcRightTankDrive(xVal, yVal);
		
		if(this.encControl.isDone()){
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
		this.robotOut.setDriveLeft(0.0);
		this.robotOut.setDriveRight(0.0);
	}

}
