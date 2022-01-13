package org.simbotics.robot.teleop;

import org.simbotics.robot.io.DriverInput;
import org.simbotics.robot.io.RobotOutput;
import org.simbotics.robot.io.SensorInput;
import org.simbotics.robot.util.SimBezierCurve;
import org.simbotics.robot.util.SimPID;
import org.simbotics.robot.util.SimPoint;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TeleopIntake implements TeleopComponent {

	private static TeleopIntake instance;
	private RobotOutput robotOut;
	private DriverInput driverIn;
	private SensorInput sensorIn;
	
	//ARM PID
	private SimPID intakePID;
	private double P = 0.0065; // 0.0010
	private double I = 0.00350;
	private double D = 0.009; // 0.0065
	private double eps = 5;
	private double pMultiplier = 1;
	private double dMultiplier = 1;
	private double intakeOut;
	private double armPosition; 
	private int targetPosition;
	
	private int highHeight = 1;
	private int medHeight = 305;
	private int pickupHeight = 790; //805
	private int pickupAdjustment = 0;
	private boolean adjustUpWasPressed = false;
	private boolean adjustDownWasPressed = false;
	private int downHeight = 955;
	
	private long timeSinceSeenBall=0;
	private long lastTimeSeenBall =-1;
	private int intakeTime =1; // 1
	private int intakeLoopCounter=0;
	private int cyclesUntilLift = 1;
	private boolean intakeFlag = false;
	
	private SimBezierCurve intakeCurve;
	
	public static TeleopIntake getInstance(){
		if(instance == null){
			instance = new TeleopIntake();
		}
		return instance;
	}
	
	private TeleopIntake(){
		
		this.driverIn = DriverInput.getInstance();
		this.robotOut = RobotOutput.getInstance();
		this.sensorIn = SensorInput.getInstance();
		this.intakePID = new SimPID(this.P,this.I,this.D,this.eps);
		
		SimPoint zero = new SimPoint(0,0);
		
		SimPoint yP1 = new SimPoint(0.0,0.37);
		SimPoint yP2 = new SimPoint(0.87,0.41);
		
		SimPoint one = new SimPoint(1,1);
		this.intakeCurve = new SimBezierCurve(zero,yP1,yP2,one);
		SmartDashboard.putNumber("Intake Time: ", this.intakeTime);
		SmartDashboard.putNumber("Cycles Until Lift: ", this.cyclesUntilLift);
		SmartDashboard.putNumber("Intake Arm Position: ", this.pickupHeight);
	}

	@Override
	public void calculate() {
		this.armPosition = this.sensorIn.getIntakeArmEnc();
		// INTAKE ROLLER CONTROL
		this.P = SmartDashboard.getNumber("Arm P Times 100: ")/100;
		
		this.I = SmartDashboard.getNumber("Arm I Times 100: ")/100;
		this.D = SmartDashboard.getNumber("Arm D Times 100: ")/100;
		this.eps = SmartDashboard.getNumber("Arm Eps: ");
		this.intakeTime = (int) SmartDashboard.getNumber("Intake Time: ");
		this.cyclesUntilLift = (int) SmartDashboard.getNumber("Cycles Until Lift: ");
		SmartDashboard.putNumber("Current Intake Target: ", this.targetPosition);
		SmartDashboard.putNumber("Intake Arm Enc: ", this.sensorIn.getIntakeArmEnc());
		SmartDashboard.putNumber("Intake Output: ",this.intakeOut);
		SmartDashboard.putNumber("Intake Adjustment", -this.pickupAdjustment);
		SmartDashboard.putNumber("Time Since Seen Ball: ", this.timeSinceSeenBall);
		this.pickupHeight = (int)SmartDashboard.getNumber("Intake Arm Position: ");
		
			
		if(this.driverIn.getDriverIntakeAdjustmentUp() && !this.adjustUpWasPressed){
			this.pickupAdjustment += 10;
			this.adjustUpWasPressed = true;
		}else if(this.driverIn.getDriverIntakeAdjustmentDown() && !this.adjustDownWasPressed){
			this.pickupAdjustment -= 10;
			this.adjustDownWasPressed = true;
		}
		
		if(!this.driverIn.getDriverIntakeAdjustmentUp()){
			this.adjustUpWasPressed = false;
		}
		
		if(!this.driverIn.getDriverIntakeAdjustmentDown()){
			this.adjustDownWasPressed = false;
		}
	
		
		
		if(this.driverIn.getJumpButton()){
			this.sensorIn.resetIntakeArmEnc();
		}
		
		if(this.driverIn.getDriveManualIntakeButton()){ // manual override takes priority 
			this.robotOut.setIntake(1.0);
		}else if(this.sensorIn.getHangStarted() && this.sensorIn.getZAxisValue() <= -30){
			//this.robotOut.setFeeder(1.0);
			this.robotOut.setIntake(0.0);
		}else if(this.driverIn.getOperatorShootButton()){ // shooting takes priority next
			if(this.sensorIn.getShooterRPM() > 3000){ // make sure we dont feed a ball into a stopped wheel
				this.robotOut.setIntake(0.0);
				this.robotOut.setFeeder(1.0);
			}else{
				this.robotOut.setIntake(0.0);
				this.robotOut.setFeeder(0.0);
			}
			this.lastTimeSeenBall = -1;
		}else if(this.driverIn.getDriverIntakeButton()){ // intake control
			if(this.sensorIn.getIntakeLight() && this.lastTimeSeenBall == -1){ // if we see a ball but we havent before
				this.lastTimeSeenBall = System.currentTimeMillis(); // set the time to be the current time
			}else if(!this.sensorIn.getIntakeLight()){ // if we dont see a ball
				this.lastTimeSeenBall = -1; // reset the time
			}
			if(this.lastTimeSeenBall != -1){ // if the time isnt a bad value then calculate the difference
				this.timeSinceSeenBall = System.currentTimeMillis() - this.lastTimeSeenBall;
			}else{ // the time is bad therefore time since seen ball should be 0
				this.timeSinceSeenBall =0;
			}
			if(this.driverIn.getDriverArmPickupButton()){ // if the driver is intaking and in the pickup position
				if(this.sensorIn.getIntakeLight()){ // trying to pick up ball and sees one
					if(this.timeSinceSeenBall < this.intakeTime){ // keep intaking until we pass the timer
						this.robotOut.setIntake(1.0);
						this.intakeLoopCounter =0; // reset the intake lifting counter because we dont want to start lifing up until we have a ball alredy
						//this.intakeFlag = false;
					}else{ // time to lift the ball into the feeder
						
						this.intakeLoopCounter++; // keep increasing this every cycle
						if(this.intakeLoopCounter > this.cyclesUntilLift){ // once we have had the ball for a number of cycles then lift 
							this.intakeFlag = true; // start lifting
							this.robotOut.setIntake(0.0);
							if(this.armPosition < 50){ // run the intake to drop the ball into the feeder
								this.robotOut.setIntake(1.0);
								
								// if we wanted the intake to automatically go back down after feeding then use this code
								/*if(!this.sensorIn.getFeederLight()){
									this.robotOut.setFeeder(1.0);
								}else{
									this.robotOut.setFeeder(0.0);
									this.intakeFlag = false; // go back down after spitting it in
									this.intakeLoopCounter = 0;
								}*/
							}
						}else{ // we haven't waited enough cycles 
							this.intakeFlag = false; // dont lift the intake
							this.robotOut.setIntake(0.15);
						}
					}
					
				}else{ // we dont see a ball anymore 
					//this.intakeFlag = false;
					//this.intakeLoopCounter =0;
					this.robotOut.setIntake(1.0);
				}
				//always check if the feeder should be running 
				if(!this.sensorIn.getFeederLight()){
					this.robotOut.setFeeder(0.8);
				}else{
					this.robotOut.setFeeder(-0.05);
				}
			}else if(this.driverIn.getDriverArmMidButton()){ // if we are pressing the middle button
				this.intakeFlag = false; // reset these 
				this.intakeLoopCounter =0;
				if(this.armPosition > this.medHeight + 20){ // not inside the robot yet
					this.robotOut.setIntake(0);
				}else{ // drop into robot
					this.robotOut.setIntake(1.0);
					if(!this.sensorIn.getFeederLight()){
						this.robotOut.setFeeder(0.8);
					}else{
						this.robotOut.setFeeder(-0.05);
					}
					
				}
				
			}else if(this.driverIn.getDriverArmHighButton()){
				this.intakeFlag = false;
				this.intakeLoopCounter =0;
				if(this.armPosition > this.highHeight + 50){ // not inside the robot yet
					this.robotOut.setIntake(0);
				}else{ // drop into robot
					this.robotOut.setIntake(1.0);
					if(!this.sensorIn.getFeederLight()){
						this.robotOut.setFeeder(0.8);
					}else{
						this.robotOut.setFeeder(-0.05);
					}
				}
				
			}else{ // this code runs during 
				this.intakeFlag = false;
				this.intakeLoopCounter =0;
				//this.lastTimePressedButton = -1;
				if(this.timeSinceSeenBall > this.intakeTime){
					this.robotOut.setIntake(0.0);
				}else{
					this.robotOut.setIntake(1.0);
				}
				if(!this.sensorIn.getFeederLight()){
					this.robotOut.setFeeder(0.8);
				}else{
					this.robotOut.setFeeder(-0.05);
				}
				
			}
			
			//this.robotOut.setFeeder(1.0);
		}else if(this.driverIn.getDriverOuttakeButton()){
			//this.lastTimePressedButton = -1;
			//this.intakeFlag = false;
			if(this.driverIn.getDriverArmPickupButton()){
				this.robotOut.setIntake(-0.4);
				this.robotOut.setFeeder(-1.0);
			}else{
				this.robotOut.setIntake(-1.0);
				this.robotOut.setFeeder(-1.0);
			}
			this.lastTimeSeenBall = -1;
		}else{
			//this.lastTimePressedButton = -1;
			//this.intakeFlag = false;
			this.robotOut.setIntake(0.0);
			this.robotOut.setFeeder(0.0);
			this.lastTimeSeenBall = -1;
		}
		
		
		// INTAKE ARM CONTROL
		if(this.driverIn.getDriverArmPickupButton()){
			if(this.intakeFlag){ // we have the ball now go up && !this.sensorIn.getFeederLight()
				this.pMultiplier = 0.7;
				this.dMultiplier = 1;
				if(this.sensorIn.getIntakeArmEnc() > 150){
					this.targetPosition = 80;
				}else{
					this.targetPosition = this.highHeight;
				}
				this.intakePID.turnOnIReset();
			}else{ // dont have ball yet
				this.targetPosition = this.pickupHeight-this.pickupAdjustment;
				if(this.targetPosition-this.armPosition > 0){ // going down
					this.pMultiplier = 0.5;
					this.dMultiplier = 0.3;
				}else{ // going up
					this.pMultiplier = 3.5;
					this.dMultiplier = 0.8;
				}
				this.intakePID.turnOffIReset();
				
			}
			
		}else if(this.driverIn.getDriverArmDown()){
			this.intakeFlag = false;
			this.targetPosition = this.downHeight;
			if(this.targetPosition-this.armPosition > 0){ // going down
				this.pMultiplier = 1.0;
				this.dMultiplier = 1.0;
			}else{ // going up
				this.pMultiplier = 3.5;
				this.dMultiplier = 0.8;
			}
		}else if(this.driverIn.getDriverArmHighButton()){
			this.intakeFlag = false;
			this.pMultiplier = 1;
			this.dMultiplier = 1;
			if(this.sensorIn.getIntakeArmEnc() > 100){
				this.targetPosition = 50;
			}else{
				this.targetPosition = this.highHeight;
			}
			//}
			this.intakePID.turnOnIReset();
		}else if(this.driverIn.getDriverArmMidButton()){
			this.intakeFlag = false;
			this.pMultiplier = 1.0;
			this.targetPosition = this.medHeight;
			this.dMultiplier = 1;
			this.intakePID.turnOnIReset();
		}else{
			this.intakeFlag = false;
			this.pMultiplier = 1;
			this.dMultiplier = 1;
			this.targetPosition = -1;
			this.intakePID.turnOnIReset();
		}
		
		this.P = this.P * this.pMultiplier;
		this.D = this.D * this.dMultiplier;
		this.intakePID.setConstants(this.P, this.I, this.D,this.eps);
		
		if(this.targetPosition == -1){
			double y = this.intakeCurve.getPoint(this.driverIn.getDriverManualIntakeStick()).getY();
			this.intakeOut = y*0.75;
		}else{
			this.intakePID.setDesiredValue(this.targetPosition);
			this.intakeOut = -this.intakePID.calcPID(this.sensorIn.getIntakeArmEnc());
		}
		
		if(this.sensorIn.getIntakeArmEnc() < 190){
			if(this.intakeOut > 0.15 && this.driverIn.getDriverArmHighButton()){
				this.intakeOut = 0.15;
			}
		}
		
		/*if(this.driverIn.getDriverArmMidButton() && this.intakeOut > 0.5){
			this.intakeOut=0.5;
		}*/
		this.robotOut.setIntakeArm(this.intakeOut);
		
		
	}

	@Override
	public void disable() {
		// TODO Auto-generated method stub
		this.robotOut.setIntake(0.0);
		
	}
}
		