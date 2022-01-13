package org.simbotics.robot.io;



import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class RobotOutput {

	private static RobotOutput instance;
	
	//Drive 
	private VictorSP leftDriveFront;
	private VictorSP leftDriveBack;
	
	private VictorSP rightDriveFront;
	private VictorSP rightDriveBack;
	
	
	private VictorSP shooter;
	
	private VictorSP intakeArmLeft;
	private VictorSP intakeArmRight; 
	
	
	private VictorSP intakeFront;
	private VictorSP feeder; 
	
    private SensorInput sensorIn;
    
    private Solenoid shifterHigh;
    private Solenoid shifterLow;
   // private Solenoid shifterPTO;
    private Solenoid hood;
    private Solenoid hangerDeployOut;
    private Solenoid hangerDeployIn; 
    private Solenoid hangerArmExtend;
    private Solenoid shooterMid;
    private Solenoid ptoEngaged;
    
    private Relay flashLight;
    
    
    private boolean highGear = true; 
    private boolean ptoInDrive = true;
    
	private RobotOutput() {
		
		this.leftDriveFront = new VictorSP(0);
		this.leftDriveBack = new VictorSP(1);
		this.rightDriveFront = new VictorSP(2);
		this.rightDriveBack = new VictorSP(3);
		
		this.intakeFront = new VictorSP(4);
		this.intakeArmLeft = new VictorSP(5);
		this.intakeArmRight = new VictorSP(6);
		
		this.shooter = new VictorSP(7);//new VictorSP(6);
		this.feeder = new VictorSP(8);
		
		//Solenoids
		
		this.shifterLow = new Solenoid(1);
		this.shifterHigh = new Solenoid(0); 
		//this.shifterPTO = new Solenoid(2); // will be needed for 3 position cylinder  
		this.shooterMid = new Solenoid(2); // new hood pancake for close shot 
		this.hood = new Solenoid(3);
		this.ptoEngaged = new Solenoid(4); // dog gear to engage the pto
		
		
		this.hangerDeployOut = new Solenoid(7); // over centre out
		this.hangerArmExtend = new Solenoid(6); // hooks 
		this.hangerDeployIn = new Solenoid(5); // over centre in 
		
		this.flashLight = new Relay(0);
	}
	
	public static RobotOutput getInstance() {
		if(RobotOutput.instance == null) {
			RobotOutput.instance = new RobotOutput();
		}
		return RobotOutput.instance;
	}
	
	
	// -----------------------------------------
    // --------------- DRIVE -------------------
    // -----------------------------------------

	public void setDriveLeft(double val){
		if(this.ptoInDrive){
			this.leftDriveFront.set(val);
			this.leftDriveBack.set(val);
		}
	}
	
	public void setDriveRight(double val){
		if(this.ptoInDrive){
			this.rightDriveFront.set(-val);
			this.rightDriveBack.set(-val);
		}
	}
	
	public void setShifter(boolean highGear){
		this.shifterHigh.set(!highGear);
		this.shifterLow.set(highGear);
		this.highGear = highGear;
	}
	
	public void setPTOInDrive(boolean inDrive){
		this.ptoEngaged.set(!inDrive);
		this.ptoInDrive = inDrive;
	}
	
	public boolean getHighGear(){
		return this.highGear;
	}
	
	// -------------------------------------------
    // --------------- SHOOTER -------------------
    // -------------------------------------------
	
	public void setShooter(double val){
		this.shooter.set(val);
	}

	public double getShooter(){
		return this.shooter.get();
		
	}
	
	public void setHood(boolean out){
		this.hood.set(out);
	}
	
	public void setMidPiston(boolean out){
		this.shooterMid.set(out);
	}
	
	public void setFlashLight(boolean on) {
		if (on) {
			this.flashLight.set(Relay.Value.kForward);
		} else {
			this.flashLight.set(Relay.Value.kOff);
		}
		
	}
	
	// -------------------------------------------
    // --------------- Intake -------------------
    // -------------------------------------------
	
	
	
	public void setIntake(double val){
		this.intakeFront.set(-val);
	}
	
	public void setFeeder(double val){
		this.feeder.set(-val);
	}
	
	public void setIntakeArm(double val){
		this.intakeArmLeft.set(val);
		this.intakeArmRight.set(-val);
	}
	
	
	
	// -------------------------------------------
    // --------------- Hanger --------------------
    // -------------------------------------------
	public void setHangerOverCentre(boolean out){
		this.hangerDeployOut.set(out);
		this.hangerDeployIn.set(!out);
	}
	
	public void setHangerArmExtension(boolean isExtended){
		this.hangerArmExtend.set(isExtended);
	}
	
	public void setHangerLeft(double val){
		if(!this.ptoInDrive){
			this.leftDriveFront.set(val);
			this.leftDriveBack.set(val);
		}
	}
	
	public void setHangerRight(double val){
		if(!this.ptoInDrive){
			this.rightDriveFront.set(-val);
			this.rightDriveBack.set(-val);
		}
	}
	
	public boolean getFarShot(){
		return !this.shooterMid.get();
	}
	
	//////////////////////////////////////////
	//////////////////////////////////////////
    // GOTCHA: remember to turn everything off
    public void stopAll() {
    	setDriveLeft(0);
    	setDriveRight(0);
    	setIntake(0);
    	setIntakeArm(0);
    	setShooter(0);
    	// shut off things here
    }
    
    
    
    
	
}
