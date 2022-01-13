package org.simbotics.robot.auton.shooter;

import org.simbotics.robot.auton.AutonCommand;
import org.simbotics.robot.auton.RobotComponent;
import org.simbotics.robot.io.RobotOutput;

public class ShooterSetHood extends AutonCommand{

	private RobotOutput robotOut;
	private boolean hoodOn;
	
	public ShooterSetHood(boolean hoodOn) {
		super(RobotComponent.SHOOTER);
		this.robotOut = RobotOutput.getInstance();
		this.hoodOn = hoodOn;
	}
	
	@Override
	public boolean calculate() {
		this.robotOut.setHood(this.hoodOn);
		return true;
	}

	@Override
	public void override() {
		// TODO Auto-generated method stub		
	}
}
