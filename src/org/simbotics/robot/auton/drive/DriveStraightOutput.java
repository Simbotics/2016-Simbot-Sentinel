package org.simbotics.robot.auton.drive;

import org.simbotics.robot.auton.AutonCommand;
import org.simbotics.robot.auton.RobotComponent;
import org.simbotics.robot.io.RobotOutput;
import org.simbotics.robot.io.SensorInput;
import org.simbotics.robot.util.SimBangBang;
import org.simbotics.robot.util.SimLib;
import org.simbotics.robot.util.SimPID;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveStraightOutput extends AutonCommand{

	private SensorInput sensorIn;
	private RobotOutput robotOut;
	
	
	private double output;
	private double time;

	


	
	public DriveStraightOutput(double output, long timeOut){
		super(RobotComponent.DRIVE, timeOut);
		
		this.robotOut = RobotOutput.getInstance();
		this.sensorIn = SensorInput.getInstance();
		this.output = output;
		
	}
    
	
	
	@Override
	public boolean calculate() {
		this.robotOut.setDriveLeft(this.output);
		this.robotOut.setDriveRight(this.output);
		return false;
		
	}

	@Override
	public void override() {

		this.robotOut.setDriveLeft(0);
		this.robotOut.setDriveRight(0);
	}

}
