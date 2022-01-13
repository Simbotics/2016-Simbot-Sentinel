package org.simbotics.robot.io;


import org.simbotics.robot.imaging.ParticleReport;
import org.simbotics.robot.imaging.SimCamera;
import org.simbotics.robot.util.SimEncoder;
import org.simbotics.robot.util.SimEncoderAngle;
import org.simbotics.robot.util.SimNavx;
import org.simbotics.robot.util.SimParticleInfo;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class SensorInput {
    
	public static final boolean USING_CAMERA = true;
	
    private static SensorInput instance;
    private RobotOutput robotOut; 
    private SimEncoder encDriveLeft;
    private SimEncoder encDriveRight;
    private SimEncoder encShooter;
    private SimEncoder encArm;
    
    private DigitalInput intakeLight; 
    private DigitalInput feederLight; 
   
    private SimNavx navx;
    
    private SimCamera camera;

    private SimEncoderAngle encoderAngle;
    
    //private Compressor compressor; 
    private PowerDistributionPanel pdp;
    
	private static double TICKSPERINCH = (128*3) * (3) / (Math.PI*7.874); // drive
	private static double TICKSPERREV = 1024; // shooter
	private static double SHOOTERRATIO = 1;
	
	private double lastTime = 0.0;
	private double deltaTime = 20.0;
	
	private double leftDriveSpeedFPS;
	private double rightDriveSpeedFPS;
	private double shooterSpeedRPM;
	
	private boolean firstCycle =true;
	private boolean hangStarted = false;
	private Thread cameraThread;
	
	private double zAxisAutoInit=0.0;
	
	private SensorInput() {
		this.robotOut = RobotOutput.getInstance();
		this.encDriveLeft = new SimEncoder(0, 1); // was 0,1 MUST FLIP FOR COMP BOT
    	this.encDriveRight = new SimEncoder(2, 3); // was 2,3MUST FLIP FOR COMP BOT
    	this.encShooter = new SimEncoder(4, 5);
    	this.encArm = new SimEncoder(6,7);
    	this.intakeLight = new DigitalInput(8);
    	this.feederLight = new DigitalInput(9);
    	
    	if(USING_CAMERA) {
    		this.camera = new SimCamera();
	    	this.cameraThread = new Thread(this.camera);
	    	this.cameraThread.setPriority(Thread.NORM_PRIORITY-1);
	    	this.cameraThread.start();
    	}
    	// this.compressor = new Compressor();
    	
        this.encoderAngle = new SimEncoderAngle(this);
    	
    	this.navx = new SimNavx();
    	
    	this.pdp = new PowerDistributionPanel();
    	this.reset();
    }
    	
    public static SensorInput getInstance() {
        if(instance == null) {
            instance = new SensorInput();
        }
        
        return instance;
    }
    
    public void update() {
    	
    	
    	
    	if (this.lastTime == 0.0) {
    		this.deltaTime = 20;
    		this.lastTime = System.currentTimeMillis();
    	} else {
    		this.deltaTime = System.currentTimeMillis() - this.lastTime;
    		this.lastTime = System.currentTimeMillis();
    	}

    	if(this.firstCycle){
    		
    		this.firstCycle = false;
    	}
    	//this.camera.update();
    	
    	//System.out.println("Gyro Speed" + this.gyro.getSpeed());
    	
    	this.encDriveLeft.updateSpeed();
    	this.encDriveRight.updateSpeed();
    	this.encShooter.updateSpeed();
    	this.encArm.updateSpeed();
    	
    	this.encoderAngle.update();
    	this.navx.update();
    	
       // ParticleReport.setScoreConstants(SmartDashboard.getNumber("areaTarget: "), SmartDashboard.getNumber("areaWeight: ")
                                         // , SmartDashboard.getNumber("ratioTarget: "), SmartDashboard.getNumber("ratioWeight: "), SmartDashboard.getNumber("widthWeight: "),
                                         // SmartDashboard.getNumber("widthTarget: "), SmartDashboard.getNumber("heightWeight: "), SmartDashboard.getNumber("heightTarget: "));
    	SmartDashboard.putNumber("Absolute Angle", this.getAbsoluteAngle());
    	//SmartDashboard.putNumber("Continuous Angle", this.getAngle());
        //SmartDashboard.putNumber("Encoder Angle", this.getEncoderAngle());
        //SmartDashboard.putNumber("Angle Difference", this.getAngle() - this.getEncoderAngle());
    	
    	SmartDashboard.putNumber("GYRO: ", this.getAngle());
    	
    	double left = this.getEncoderLeftSpeed() / TICKSPERINCH;
    	double right = this.getEncoderRightSpeed() / TICKSPERINCH;
    	
    	this.leftDriveSpeedFPS = (left/12)/(this.deltaTime/1000);
		this.rightDriveSpeedFPS =  (right/12)/(this.deltaTime/1000);
        this.shooterSpeedRPM = (this.getEncoderShooterSpeed() / TICKSPERREV)*SHOOTERRATIO /(this.deltaTime/60000.0); // I tried just this.getEncoderShooterSpeed but it didnt work either

		double score;
		String output;
		
		/*if(this.camera.getBestParticle() != null){
            score = this.camera.getBestParticle().getScore();
		}else{*/
			score = 0;
			output = "";
		//}
	
	
		SmartDashboard.putBoolean("Is air low?", this.getIsAirLow());
		//SmartDashboard.putNumber("LEFT ENC Inches: ", left);
    	//SmartDashboard.putNumber("RIGHT ENC Inches: ", right);
    	SmartDashboard.putBoolean("Intake Light: ", this.getIntakeLight());
    	SmartDashboard.putBoolean("Feeder Light: ", this.getFeederLight());
    	SmartDashboard.putNumber("Intake Arm Enc: ", this.getIntakeArmEnc());
    	
    	//SmartDashboard.putNumber("Left Speed FPS: ", this.leftDriveSpeedFPS);
    	//SmartDashboard.putNumber("Right Speed FPS: ", this.rightDriveSpeedFPS);
    	SmartDashboard.putNumber("Drive Speed FPS: ", this.getDriveSpeedFPS());
    	SmartDashboard.putNumber("Drive Position Inches: ",this.getDriveInches());
    	//SmartDashboard.putNumber("Navx Altitude: ", this.navx.getAltitude());
    	//SmartDashboard.putNumber("Navx Pitch: ", this.navx.getPitch());
    	SmartDashboard.putNumber("Navx Roll: ", this.navx.getRoll());
    	SmartDashboard.putNumber("Navx Gyro X: ", this.navx.getRawGyroX());
    	SmartDashboard.putNumber("Navx Gyro Z: ", this.navx.getRawGyroZ());
    	SmartDashboard.putNumber("Navx Gyro Y: ", this.navx.getRawGyroY());
    	//SmartDashboard.putNumber("Navx Mag Z: ", this.navx.getRawMagZ());
    	//SmartDashboard.putNumber("Navx Mag X: ", this.navx.getRawMagX());
    	//SmartDashboard.putNumber("Navx Mag Y: ", this.navx.getRawMagY());
    	SmartDashboard.putNumber("Navx Zaxis Auto Init: ",this.zAxisAutoInit);

    	
		SmartDashboard.putNumber("Shooter RPM: ", this.shooterSpeedRPM);
		//SmartDashboard.putNumber("left speed Raw: ", this.encDriveLeft.rawSpeed());
		//SmartDashboard.putNumber("right speed Raw: ", this.encDriveRight.rawSpeed());
		SmartDashboard.putNumber("Shooter Enc: ",this.encShooter.get());
		
		SmartDashboard.putNumber("Left Enc",this.getEncoderLeft());
    	SmartDashboard.putNumber("Right Enc",this.getEncoderRight());
    	
    	//SmartDashboard.putNumber("Encoder Angle: ", this.getEncoderAngle());
    	
    	
    	
    	//NAVX CRAP///////////////////////////////////////////////////
    	//SmartDashboard.putNumber("NavX Angle: ",this.navx.getAngle());
    	//SmartDashboard.putNumber("NavX Compass Heading: ",this.navx.getCompassHeading());
    	//SmartDashboard.putNumber("NavX Fused: ",this.navx.getFusedHeading());
    	//SmartDashboard.putNumber("NavX Pitch: ",this.navx.getPitch());
    	//SmartDashboard.putNumber("NavX Yaw: ",this.navx.getYaw());
    	//SmartDashboard.putNumber("NavX Roll: ",this.navx.getRoll());
    	
    	
    	
    	
    	ParticleReport particle = this.camera.getBestParticle();
    	
    	if(USING_CAMERA && particle != null){
//        	SmartDashboard.putNumber("Highest Particle Ratio:", particle.getRatio());
//        	SmartDashboard.putNumber("Highest Particle Area:", particle.getArea());
//        	SmartDashboard.putNumber("Highest Particle Height:", particle.getHeight());
//        	SmartDashboard.putNumber("Highest Particle C_M_X:", particle.getParticleX());
//        	SmartDashboard.putNumber("Highest Particle C_M_Y:", particle.getParticleY());  
//        	SmartDashboard.putNumber("Highest Particle Score:", particle.getScore());			
//   		
        	
    	}
    	if(USING_CAMERA && this.getParticleInfo() != null) {
    	 // SmartDashboard.putNumber("Dist To Target: ",this.getParticleInfo().getDistanceToTarget());
      	 // SmartDashboard.putNumber("X Dist To Target",-Math.sin(90 - getAbsoluteAngle()) * getParticleInfo().getDistanceToTarget());
      	 // SmartDashboard.putNumber("Y Dist To Target",Math.cos(90 - getAbsoluteAngle()) * getParticleInfo().getDistanceToTarget());
     	 // SmartDashboard.putNumber("X Dist To Target 2: ",this.getParticleInfo().getXDistanceToTarget());
    	 // SmartDashboard.putNumber("Y Dist To Target 2: ",this.getParticleInfo().getYDistanceToTarget());

    	}
    	
    	
    	
    }
    
    public void reset() {
        //TODO: add sensors that need to be reset before auto here
		this.navx.reset();
		this.encoderAngle.reset();
        this.encDriveLeft.reset();
		this.encDriveRight.reset();
		this.encArm.reset();
		this.zAxisAutoInit = this.getZAxisValue();
		this.firstCycle = true;
		//this.camera.stopCamera();
		//this.robotOut.resetIntakeArmEnc();
		//this.encIndexer.reset();
		
		
	} 
    
    public double getLastTickLength() {
    	return this.deltaTime;
    }
    
    // -----------------------------------------------------
    // ---- Component Specific Methods ---------------------
    // -----------------------------------------------------
    
   
    
    // ----------------- DRIVE ------------------------------
    public int getEncoderLeft() {
    	return this.encDriveLeft.get();
    }
    
    public int getEncoderLeftSpeed() {
    	return this.encDriveLeft.speed();
    }
    
    public double getEncoderLeftRawSpeed() {
    	return this.encDriveLeft.rawSpeed();
    }

    public double getLeftDriveInches(){
    	return this.getEncoderLeft() / TICKSPERINCH;
    }
    
    public double getLeftDriveSpeedInches(){
    	return this.getEncoderLeftSpeed() / TICKSPERINCH;
    }
       
    public int getEncoderRight() {
    	return this.encDriveRight.get();
    }
    
    public int getEncoderRightSpeed() {
    	return this.encDriveRight.speed();
    }

    public double getRightDriveInches(){
    	return this.getEncoderRight() / TICKSPERINCH;
    }

    public double getRightDriveSpeedInches(){
    	return this.getEncoderRightSpeed() / TICKSPERINCH;
    }
    
    public double getEncoderRightRawSpeed() {
    	return this.encDriveRight.rawSpeed();
    }
    
    public double getDriveEncoderAverage() {
    	return (this.getEncoderRight() + this.getEncoderLeft())/2;
    }
    
    public double getDriveInches(){
    	return this.getDriveEncoderAverage() / TICKSPERINCH;
    }
    
    public SimEncoder getEncoderLeftObj() {
    	return this.encDriveLeft;
    }
   
    public SimEncoder getEncoderRightObj() {
    	return this.encDriveRight;
    }
    
    public double getDriveEncoderSpeedAverage(){
    	return (this.getEncoderLeftSpeed() + this.getEncoderRightSpeed()) / 2.0;
    }
    
    public double getDriveSpeedFPS(){
    	return (this.leftDriveSpeedFPS+this.rightDriveSpeedFPS)/2;
    }
    
 // ----------------------- Shooter -------------------------------    
    public int getEncoderShooter(){
    	return this.encShooter.get();
    }
   
    public double getEncoderShooterSpeed(){
    	return this.encShooter.speed(); // .getRate();
    }
    
    public double getShooterRPM(){
    	return this.shooterSpeedRPM;
    }
    
    public boolean getHangStarted(){
    	return this.hangStarted;
    }
    
    public void setHangStarted(){
    	this.hangStarted = true;
    }
    
    
 // ----------------------- Intake  -------------------------------    
    public void resetIntakeArmEnc(){
		this.encArm.reset();
	}
    
	public int getIntakeArmEnc(){
		return this.encArm.get();
	}
	
	public double getIntakeArmEncSpeed(){
		return this.encArm.speed();
	}
    
 // ----------------------- Light Sensors ----------------------------------    
    public boolean getIntakeLight(){
    	return !this.intakeLight.get();
    }
    
    public boolean getFeederLight(){
    	return !this.feederLight.get();
    }
    
    
    
    
 // ----------------------- GYRO ----------------------------------
    

    
    public double getAngle(){
    	return this.navx.getAngle();
    }
    
    public double getAbsoluteAngle() {
    	return 360 - this.navx.getFusedHeading();
    }
    
    public double getZAxisValue() {
    	return this.navx.getRoll();
    }
    
    public double getZAxisAutoInit(){
    	return this.zAxisAutoInit;
    }
    
    // ----------------------- POSITIONAL SYSTEM ----------------------------------
    
	public double getEncoderAngle() {
		return this.encoderAngle.getAngle();
	}
	
	// ----------------------- PDP  ----------------------------------
    public double getVoltage() {
    	return this.pdp.getVoltage();
    }
    
    public double getCurrent(int channel) {
    	return this.pdp.getCurrent(channel);
    }
    
    public double getTotalCurrent() {
    	return this.pdp.getTotalCurrent();
    }
    
 // ----------------------- Compressor  ----------------------------------
    
    public boolean getIsAirLow(){
    	return false;//this.compressor.getPressureSwitchValue();
    }

//  ----------------------- Camera ----------------------------------

    public void startImageProcessing(){
    	if(USING_CAMERA) {
    		this.camera.startProcessing();
    	}
    }
    
    public void stopImageProcessing(){
    	if(USING_CAMERA) {
    		this.camera.stopProcessing();
    	}
    }
    

    public boolean cameraHasTarget() {
    	if(USING_CAMERA) {
    		return camera.getBestParticle() != null;
    	} else {
    		return false;
    	}
    }
    
    public SimParticleInfo getParticleInfo() {
    	if(USING_CAMERA) {
	    	ParticleReport report = camera.getBestParticle();
	        if (report != null){
	            return new SimParticleInfo(report);
	        } else {
	            return null;
	        }
    	} else {
    		return null;
    	}
    	
    }
   
}
