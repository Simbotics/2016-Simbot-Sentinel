package org.simbotics.robot.auton.intake;

import org.simbotics.robot.auton.AutonCommand;
import org.simbotics.robot.auton.RobotComponent;
import org.simbotics.robot.io.RobotOutput;

public class IntakeRollerSetSpeed extends AutonCommand{

	private double speed; 
	private RobotOutput robotOut;
	
	public IntakeRollerSetSpeed(double speed) {
		super(RobotComponent.INTAKE);
		this.speed = speed;
		this.robotOut = RobotOutput.getInstance();
	}

	@Override
	public boolean calculate() {
		this.robotOut.setIntake(this.speed);
		return true;
	}

	@Override
	public void override() {
		this.robotOut.setIntake(0.0);
		
	}

}
