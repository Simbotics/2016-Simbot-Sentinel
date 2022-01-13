package org.simbotics.robot.auton.drive;

import org.simbotics.robot.auton.AutonCommand;
import org.simbotics.robot.auton.RobotComponent;
import org.simbotics.robot.io.RobotOutput;
import org.simbotics.robot.io.SensorInput;
import org.simbotics.robot.util.SimLib;
import org.simbotics.robot.util.SimPID;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTurnToGyroAngle extends AutonCommand {

	private RobotOutput robotOut;
	private SensorInput sensorIn;	
	private SimPID gyroControl;
	
	private boolean firstCycle = true;
	private double targetAngle;
	
	private double pConst = 0.0;
	private double iConst = 0.0;
	private double dConst = 0.0;
	private double eps = 0.0;
	
	
	
	public DriveTurnToGyroAngle(double targetAngle) {
		this(targetAngle, 1.0, SmartDashboard.getNumber("Drive Gyro Eps: "),-1);
	}
	
	public DriveTurnToGyroAngle(double targetAngle, long timeOut) {
		this(targetAngle, 1.0,SmartDashboard.getNumber("Drive Gyro Eps: ") ,timeOut);
	}
	
	public DriveTurnToGyroAngle(double targetAngle,double eps ,long timeOut) {
		this(targetAngle, 1.0,eps ,timeOut);
	}
	
	public DriveTurnToGyroAngle(double targetAngle, double maxSpeed) {
		this(targetAngle, maxSpeed,SmartDashboard.getNumber("Drive Gyro Eps: "),-1);
	}
	
	public DriveTurnToGyroAngle(double targetAngle, double maxSpeed,double eps, long timeOut){
		super(RobotComponent.DRIVE, timeOut);
		
		this.robotOut = RobotOutput.getInstance();
		this.sensorIn = SensorInput.getInstance();
		
		this.targetAngle = targetAngle;	
		
		pConst = SmartDashboard.getNumber("Drive Gyro P: ");
		iConst = SmartDashboard.getNumber("Drive Gyro I: ");
		dConst = SmartDashboard.getNumber("Drive Gyro D: ");
		this.eps = eps;
		
		this.gyroControl = new SimPID (pConst, iConst, dConst, eps);
		this.gyroControl.setMaxOutput(maxSpeed);
	}
	
	
	@Override
	public boolean calculate() {
		if(this.firstCycle){
			this.firstCycle = false;

            double angle = this.sensorIn.getAngle();
            double offset = angle % 360;
            
            if (this.targetAngle - offset < -180){
                this.gyroControl.setDesiredValue(angle + 360 + this.targetAngle - offset);         
            } else if (this.targetAngle - offset < 180) {
                this.gyroControl.setDesiredValue(angle + this.targetAngle - offset);
            } else {
                this.gyroControl.setDesiredValue(angle - 360 + this.targetAngle - offset);
            }
		}
        
		this.gyroControl.setConstants(pConst, iConst, dConst);
		
		double xVal = -this.gyroControl.calcPID(this.sensorIn.getAngle());
		SmartDashboard.putNumber("Output ", (xVal));
		double leftDrive = SimLib.calcLeftTankDrive(xVal, 0.0);
		double rightDrive = SimLib.calcRightTankDrive(xVal, 0.0);
				
		if(this.gyroControl.isDone()){
			this.robotOut.setDriveLeft(0.0);
			this.robotOut.setDriveRight(0.0);
			return true;
		}else{
			this.robotOut.setDriveLeft(leftDrive);
			this.robotOut.setDriveRight(rightDrive);
		}
		
		return false;
	}

	@Override
	public void override() {
		this.robotOut.setDriveLeft(0.0);
		this.robotOut.setDriveRight(0.0);		
	}

}
