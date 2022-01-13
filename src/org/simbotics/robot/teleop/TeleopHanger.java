package org.simbotics.robot.teleop;

import org.simbotics.robot.io.DriverInput;
import org.simbotics.robot.io.RobotOutput;
import org.simbotics.robot.io.SensorInput;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TeleopHanger implements TeleopComponent {

	private static TeleopHanger instance;
	private RobotOutput robotOut;
	private DriverInput driverIn;
	private SensorInput sensorIn;
	
	private long hangerStartTime=-1; // starts at -1 to act as a boolean 
	private boolean firstCycle = true;
	private long timeSinceStartedHang=0;
	private int hangerWaitTime = 750;
	
	public static TeleopHanger getInstance(){
		if(instance == null){
			instance = new TeleopHanger();
		}
		return instance;
	}
	
	private TeleopHanger(){
		this.driverIn = DriverInput.getInstance();
		this.robotOut = RobotOutput.getInstance();
		this.sensorIn = SensorInput.getInstance();
	}
	
	@Override
	public void calculate() {
		this.hangerWaitTime = (int) SmartDashboard.getNumber("Hanger Wait Time: ");
		if(this.hangerStartTime > -1){ // only calculate the time since we have started hanging once we have actually started the hang
			this.timeSinceStartedHang = System.currentTimeMillis() - this.hangerStartTime; // the difference in time since we started to hang
		}
		if(this.driverIn.getHangerReleaseButton() && this.driverIn.getDriverArmPickupButton()){ // this should release the over centre and then the hooks after a delay
			this.robotOut.setHangerOverCentre(false);
		}
		
		if(this.driverIn.getHangerArmExtendButton()){ // we stil have manual hook control 
			this.robotOut.setHangerArmExtension(true);
			this.sensorIn.setHangStarted();
		}else if(this.driverIn.getHangerArmRetractButton()){
			this.robotOut.setHangerArmExtension(false);
		}
		
		if(this.driverIn.getDriverShiftToDriveButton()){ // this is for the driver to shift the PTO out of the hanger
			this.robotOut.setPTOInDrive(true);
		}
		
		if(this.driverIn.getHangerHangButton()){ // this button makes sure we are in low gear to keep things consistent 
			if(this.hangerStartTime == -1){
				this.hangerStartTime = System.currentTimeMillis();
			}
			this.robotOut.setPTOInDrive(false);
			this.robotOut.setShifter(false);
			this.robotOut.setHangerArmExtension(false);
			if(this.timeSinceStartedHang > 1000){
				this.robotOut.setHangerLeft(this.driverIn.getHangerHangTrigger()); // depending on how hard you press the trigger the faster you will go
				this.robotOut.setHangerRight(this.driverIn.getHangerHangTrigger());
			}else{
				this.robotOut.setHangerLeft(this.driverIn.getHangerHangTrigger()/2.5); // depending on how hard you press the trigger the faster you will go
				this.robotOut.setHangerRight(this.driverIn.getHangerHangTrigger()/2.5);
			}
		}else{
			this.robotOut.setHangerLeft(0); // we need to tell the hanger motors to stop if we release 
			this.robotOut.setHangerRight(0);
		}
		
	}

	@Override
	public void disable() {
		// TODO Auto-generated method stub
		
	}

}
