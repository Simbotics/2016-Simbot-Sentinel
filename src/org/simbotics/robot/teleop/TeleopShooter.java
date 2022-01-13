package org.simbotics.robot.teleop;

import org.simbotics.robot.io.DriverInput;
import org.simbotics.robot.io.RobotOutput;
import org.simbotics.robot.io.SensorInput;
import org.simbotics.robot.util.SimBangBang;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TeleopShooter implements TeleopComponent {

	private static TeleopShooter instance;
	private RobotOutput robotOut;
	private DriverInput driverIn;
	private SensorInput sensorIn;
	
	//PID
	//private SimPID shooterPID;
	private double eps = 250;
	
	private boolean shooterOn = false;
	private int targetRPM =0;
	private boolean manualMode = false; // change to false
	
	
	private int preset = 0;
	private int hoodCycles =0;
	private int hoodCyclesToWait = 50;
	//Bang Bang
	private SimBangBang shooterBangBang;
	private double adjustableLow = 0.00;
	private double adjustableHigh = 1.0;
	
	private boolean hadFeederLight=false;
	
	//Speeds
	private int slowSpeed = 4850; // 10000
	private int mediumSpeed = 4950; // 11000
	private int fastSpeed = 5950;
	private int fastestSpeed = 5550;
	private int adjustment;
	
	public static TeleopShooter getInstance(){
		if(instance == null){
			instance = new TeleopShooter();
		}
		return instance;
		
	}
	
	private TeleopShooter(){
		this.driverIn = DriverInput.getInstance();
		this.robotOut = RobotOutput.getInstance();
		this.sensorIn = SensorInput.getInstance();
		this.shooterBangBang = new SimBangBang(0.0, 0.0);
		
		SmartDashboard.putNumber("Shooter Eps: ", this.eps);
		
		SmartDashboard.putNumber("Slow Speed: ", this.slowSpeed);
		SmartDashboard.putNumber("Medium Speed: ", this.mediumSpeed);
		SmartDashboard.putNumber("Adjustable Speed: ", this.fastSpeed);
		SmartDashboard.putNumber("Fastest Speed: ", this.fastestSpeed);
		
		SmartDashboard.putNumber("Bang Low: ", this.adjustableLow);
		SmartDashboard.putNumber("Bang High: ", this.adjustableHigh);
	}
	
	@Override
	public void calculate() {
		
		this.eps = SmartDashboard.getNumber("Shooter Eps: ");
		
		
		
		SmartDashboard.putNumber("Shooter Output: ", this.robotOut.getShooter());
		//Update Presets from SmartDashboard
		this.slowSpeed = (int)SmartDashboard.getNumber("Slow Speed: ");
		this.mediumSpeed = (int)SmartDashboard.getNumber("Medium Speed: ");
		this.fastSpeed = (int)SmartDashboard.getNumber("Adjustable Speed: ");
		this.fastestSpeed = (int)SmartDashboard.getNumber("Fastest Speed: ");
		
		SmartDashboard.putBoolean("Manual Mode: ", this.manualMode);
		SmartDashboard.putNumber("Current Target: ", this.targetRPM);
		//Bang bang
		this.adjustableLow = SmartDashboard.getNumber("Bang Low: ");
		this.adjustableHigh = SmartDashboard.getNumber("Bang High: ");
		
		SmartDashboard.putBoolean("At Speed BangBang: ", this.shooterBangBang.isAtSpeed(this.sensorIn.getShooterRPM(), this.eps));
		
	
		
		//Check if we should be in manual mode
		if(this.driverIn.getShooterManualOn()){
			this.manualMode = true;
		}else if(this.driverIn.getShooterManualOff()){
			this.manualMode = false;
		}
		
		
		
		
		if(this.driverIn.getShooterSlowButton()){
			this.preset = 1;
			this.robotOut.setMidPiston(true);
			this.hoodCycles++;
			if(this.hoodCycles > this.hoodCyclesToWait){
				this.robotOut.setHood(true);
			}
			this.shooterOn = true;
			this.robotOut.setFlashLight(true);
			
		}else if(this.driverIn.getShooterMediumButton()){
			this.preset = 2;
			this.robotOut.setMidPiston(true);
			this.hoodCycles++;
			if(this.hoodCycles > this.hoodCyclesToWait){
				this.robotOut.setHood(true);
			}
			this.shooterOn = true;
			this.robotOut.setFlashLight(true);
			
		}else if(this.driverIn.getShooterFastButton()){
			this.hoodCycles = 0;
			this.preset = 3;
			this.robotOut.setHood(true); // make this true again
			this.robotOut.setMidPiston(false);
			this.shooterOn = true;
			this.robotOut.setFlashLight(true);
			
		}else if(this.driverIn.getShooterFastestButton()){
			this.hoodCycles =0;
			this.preset = 4;
			this.robotOut.setHood(true);
			this.robotOut.setMidPiston(false);
			this.shooterOn = true;
			this.robotOut.setFlashLight(true);
			
		}else if(this.sensorIn.getHangStarted() && (this.sensorIn.getFeederLight() || this.hadFeederLight)){
			this.hoodCycles = 0;
			//this.preset =5;
			//this.robotOut.setHood(true); // make this true again
			//this.robotOut.setMidPiston(false);
			//this.shooterOn = true;
			//this.hadFeederLight = true;
		}else{
			this.hoodCycles=0;
			this.robotOut.setHood(false);
			this.robotOut.setMidPiston(false);
			this.shooterOn = false;
			this.robotOut.setFlashLight(false);
		}
		
		this.adjustment = 0;///(int)(500 * this.driverIn.getShooterAdjustmentStick()); // +- speed value
		
		if(this.preset == 1){
			this.targetRPM = this.slowSpeed; // preset + stick value
			this.shooterBangBang.setMinMax(0.0, 1.0);
			//this.robotOut.setFlashLight(true);
		}else if(this.preset == 2){
			this.targetRPM = this.mediumSpeed;
			this.shooterBangBang.setMinMax(0.0, 1.0);
			//this.robotOut.setFlashLight(true);
		}else if(this.preset == 3){
			this.targetRPM = this.fastSpeed;
			this.shooterBangBang.setMinMax(this.adjustableLow, this.adjustableHigh);
			//this.robotOut.setFlashLight(true);
		}else if(this.preset == 4){
			this.targetRPM = this.fastestSpeed;
			this.shooterBangBang.setMinMax(0.0, 1.0);
			//this.robotOut.setFlashLight(true);
		}else if(this.preset == 5){
			this.targetRPM = 4000;
			this.shooterBangBang.setMinMax(0.0, 0.75);
			//this.robotOut.setFlashLight(true);
		}else{
			this.targetRPM = 0;
			this.shooterBangBang.setMinMax(0.0, 0.0);
			this.adjustment = 0;
			//this.robotOut.setFlashLight(false);
		}
		
		
		this.targetRPM += this.adjustment;
		this.shooterBangBang.setDesiredValue(this.targetRPM);
		
		
		if(this.shooterOn && this.targetRPM != 0 && !this.manualMode ){ // bang bang control
			if(this.sensorIn.getShooterRPM() < 3000){
				this.robotOut.setShooter(1.0);
			}else{
				double bangBangOut = this.shooterBangBang.calculate(this.sensorIn.getShooterRPM());
				this.robotOut.setShooter(bangBangOut);
			}
		}else if(this.manualMode && this.shooterOn){
			this.robotOut.setShooter((this.driverIn.getShooterAdjustmentStick()+1)/2); // middle of the stick is 0.5
		}else{
			
			this.robotOut.setShooter(0.0);
		}
	}

	@Override
	public void disable() {
		// TODO Auto-generated method stub
		this.shooterOn = false;
		this.robotOut.setShooter(0.0);
		
	}

}
