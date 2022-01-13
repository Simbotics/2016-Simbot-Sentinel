package org.simbotics.robot.auton.shooter;

import org.simbotics.robot.auton.AutonCommand;
import org.simbotics.robot.auton.RobotComponent;
import org.simbotics.robot.io.RobotOutput;
import org.simbotics.robot.io.SensorInput;
import org.simbotics.robot.util.SimBangBang;

public class ShooterSetSpeedUsingBangBang extends AutonCommand{

	private double minSpeed;
	private double maxSpeed;
	
	private RobotOutput robotOut;
	private SensorInput sensorIn;
	
	private SimBangBang speedControl;
	
	
	public ShooterSetSpeedUsingBangBang(double targetSpeed, double minSpeed, double maxSpeed) {
		super(RobotComponent.SHOOTER);
		this.robotOut = RobotOutput.getInstance();
		this.sensorIn = SensorInput.getInstance();
		
		this.minSpeed = minSpeed;
		this.maxSpeed = maxSpeed;
		
		this.speedControl = new SimBangBang(this.maxSpeed, this.minSpeed);
		this.speedControl.setDesiredValue(targetSpeed);
	}
	
	public ShooterSetSpeedUsingBangBang(double targetSpeed){
		this(targetSpeed,0.3,1.0);
	}
	
	
	
	@Override
	public boolean calculate() {
		double shooterSpeed = this.speedControl.calculate(this.sensorIn.getShooterRPM());
		
		this.robotOut.setShooter(shooterSpeed);
		
		return false;
	}

	@Override
	public void override() {
		this.robotOut.setShooter(0.0);
		// TODO Auto-generated method stub
		
	}
	
}
