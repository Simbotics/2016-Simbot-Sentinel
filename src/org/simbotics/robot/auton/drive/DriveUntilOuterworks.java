package org.simbotics.robot.auton.drive;

import org.simbotics.robot.auton.AutonCommand;
import org.simbotics.robot.auton.RobotComponent;
import org.simbotics.robot.io.RobotOutput;
import org.simbotics.robot.io.SensorInput;
import org.simbotics.robot.util.SimLib;
import org.simbotics.robot.util.SimPID;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;;

public class DriveUntilOuterworks extends AutonCommand {
	
	private RobotOutput robotOut;
	private SensorInput sensorIn;
	
	private int cycle;

	private double initialZAxisVaue;
	private boolean done = false;
	
	public DriveUntilOuterworks(long timeOut) {
		super(RobotComponent.DRIVE, timeOut);
	
		this.robotOut = RobotOutput.getInstance();
		this.sensorIn = SensorInput.getInstance();
		this.cycle = 0;
		
		this.initialZAxisVaue = this.sensorIn.getZAxisAutoInit();// this.sensorIn.getZAxisValue();
	}
	
	
	@Override
	public boolean calculate() {
		double valueDiff = Math.abs(this.sensorIn.getZAxisValue() - this.initialZAxisVaue);
		
		if(valueDiff > 2.5) {
			this.cycle++;
		} else {
			this.cycle = 0;
		}
		
		if(this.cycle > 25) {
			this.robotOut.setDriveLeft(0);
			this.robotOut.setDriveRight(0);
			return true;
		} else {
			this.robotOut.setDriveLeft(-0.5);
			this.robotOut.setDriveRight(-0.5);
			return false;
		}

	}

	@Override
	public void override() {
		this.robotOut.setDriveLeft(0.0);
		this.robotOut.setDriveRight(0.0);
	}

}
