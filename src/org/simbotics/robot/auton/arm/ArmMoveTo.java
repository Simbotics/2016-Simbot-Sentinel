package org.simbotics.robot.auton.arm;

import org.simbotics.robot.auton.AutonCommand;
import org.simbotics.robot.auton.RobotComponent;
import org.simbotics.robot.io.RobotOutput;
import org.simbotics.robot.io.SensorInput;
import org.simbotics.robot.util.SimPID;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ArmMoveTo extends AutonCommand{

	private SensorInput sensorIn;
	private RobotOutput robotOut;
	
	private SimPID armPID;
	
	private double P = 0.0065;
	private double I = 0.0035;
	private double D = 0.009;
	private double eps = 5;
	
	public ArmMoveTo(int goal) {
		super(RobotComponent.ARM);
		
		this.sensorIn = SensorInput.getInstance();
		this.robotOut = RobotOutput.getInstance();
		
		this.P = SmartDashboard.getNumber("Arm P Times 100: ")/100;
		this.I = SmartDashboard.getNumber("Arm I Times 100: ")/100;
		this.D = SmartDashboard.getNumber("Arm D Times 100: ")/100;
		this.eps = SmartDashboard.getNumber("Arm Eps: ");
		this.armPID = new SimPID(this.P, this.I, this.D, this.eps);
		this.armPID.setDesiredValue(goal);
	}

	@Override
	public boolean calculate() {
		this.robotOut.setIntakeArm(-this.armPID.calcPID(this.sensorIn.getIntakeArmEnc()));
		
		return false;
	}

	@Override
	public void override() {
		// TODO Auto-generated method stub
		this.robotOut.setIntakeArm(0);
	}
	
}
