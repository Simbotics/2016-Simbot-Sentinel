package org.simbotics.robot.auton.feeder;

import org.simbotics.robot.auton.AutonCommand;
import org.simbotics.robot.auton.RobotComponent;
import org.simbotics.robot.io.SensorInput;

public class WaitUntilFeederLight extends AutonCommand{

	private SensorInput sensorIn;
	
	public WaitUntilFeederLight() {
		super(RobotComponent.FEEDER);
		
		this.sensorIn = SensorInput.getInstance();
	}

	@Override
	public boolean calculate() {
		if(this.sensorIn.getFeederLight()) {
			return true;
		} else {
			return false;
		}
		
	}

	@Override
	public void override() {
		
		
	}

}
