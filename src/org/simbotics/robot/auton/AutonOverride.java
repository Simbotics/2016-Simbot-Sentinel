package org.simbotics.robot.auton;

public class AutonOverride extends AutonCommand {

	private RobotComponent overrideType;
	
	public AutonOverride(RobotComponent type) {
		super(type);
		this.overrideType = type;
	}
	
	public boolean checkAndRun() {
		AutonCommand.overrideComponent(this.overrideType);
		return true;
	}
	
	@Override
	public boolean calculate() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void override() {
		// TODO Auto-generated method stub

	}

}
