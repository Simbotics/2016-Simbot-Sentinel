package org.simbotics.robot.auton.drive;

import org.simbotics.robot.auton.AutonCommand;
import org.simbotics.robot.auton.RobotComponent;
import org.simbotics.robot.io.RobotOutput;
import org.simbotics.robot.io.SensorInput;
import org.simbotics.robot.util.SimBangBang;
import org.simbotics.robot.util.SimLib;
import org.simbotics.robot.util.SimPID;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveSetOutput extends AutonCommand{

	private SensorInput sensorIn;
	private RobotOutput robotOut;
	
	
	private double output;
	

	


	
	public DriveSetOutput(double output){
		super(RobotComponent.DRIVE);
		
		this.robotOut = RobotOutput.getInstance();
		this.sensorIn = SensorInput.getInstance();
		this.output = output;
		
	}
    
	
	
	@Override
	public boolean calculate() {
		this.robotOut.setDriveLeft(this.output);
		this.robotOut.setDriveRight(this.output);
		return true;
		
	}

	@Override
	public void override() {

		
	}

}