package org.simbotics.robot.auton.arm;

import org.simbotics.robot.auton.AutonCommand;
import org.simbotics.robot.auton.RobotComponent;

public class ArmOverride extends AutonCommand{

	public ArmOverride() {
		super(RobotComponent.ARM);
	}

	public boolean checkAndRun(){
		AutonCommand.overrideComponent(RobotComponent.ARM);
		return true;
	}
	
	@Override
	public boolean calculate() {
		return true;
	}

	@Override
	public void override() {
		
	}

}
