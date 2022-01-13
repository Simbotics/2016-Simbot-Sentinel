package org.simbotics.robot.util;

import org.simbotics.robot.io.RobotOutput;
import org.simbotics.robot.io.SensorInput;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SimTargeting {
    public static final boolean TUNING = true;
    
    public static final double INITIAL_DONE_RANGE = 3.5;
    public static final double PRECISE_DONE_RANGE = 0.1;
    public static final long START_TIMEOUT = 1000;
    public static final long PRECISE_TIMEOUT = 1500;
    
    private RobotOutput robotOut;
    private SensorInput sensorIn;

    private SimPIDTimer gyroPID;
    private boolean isTargeting;

    private double gyroP = 0.07;
    private double gyroI = 0.015;
    private double gyroD = 0.055;
    private double gyroEps = 0.1;
	
    private int count = 0;
    private double isDoneEps = 3;

    private State targetingState = State.NO_TARGET;

    public SimTargeting() {
        this.robotOut = RobotOutput.getInstance();
        this.sensorIn = SensorInput.getInstance();
        this.gyroPID = new SimPIDTimer(gyroP, gyroI, gyroD, gyroEps,START_TIMEOUT);
        this.gyroPID.setDoneRange(INITIAL_DONE_RANGE);
        this.isTargeting = false;
        this.targetingState = State.NO_TARGET;
    }

    public double calculate() {
    	 SmartDashboard.putString("Targeting State:", this.targetingState.toString());
        // SmartDashboard.putNumber("Targeting targetDiff:",this.sensorIn.getParticleInfo().getAngleToTarget());
         SmartDashboard.putNumber("Targeting Count:", this.count);
         SmartDashboard.putNumber("desired Angle: ", this.gyroPID.getDesiredVal());  
    	if (this.targetingState == State.DONE) {
    		return 0.0;
    	}
        double gyroAngle = this.sensorIn.getAngle();

        if (TUNING) {
            updateConstantsFromSmartDashboard();
            outputData(gyroAngle);
        }

        if (!isTargeting && gyroPIDTargetSet(gyroAngle)) {
            isTargeting = true;
        }

       
        
        if (isTargeting) {

            // calcPID needs to be before isDone, as isDone, uses values
            // calcuated from calcPID - anything else and we'll be done, without
            // actually being done
            double gyroOutput = this.gyroPID.calcPID(gyroAngle);

            if (this.gyroPID.isDone(System.currentTimeMillis())) {
                this.isTargeting = this.gyroPIDTargetSet(gyroAngle);

                SmartDashboard.putNumber("Targeting Output:", gyroOutput);
                SmartDashboard.putString("simtargeting done", "new image " + count);
                // calculate again in order to do the double check
                this.gyroPID.calcPID(gyroAngle);
                
                if (this.gyroPID.isDone(System.currentTimeMillis())) {
                	SmartDashboard.putString("simtargeting done", "actually done");
                	SmartDashboard.putBoolean("Aimed At Goal: ",true);
                //    if(SimLib.isWithinRange(0, this.sensorIn.getParticleInfo().getAngleToTarget(), isDoneEps)){
                	if(this.count >=5){
                		this.targetingState = State.DONE;
                	}
                 //   }
                    return 0.0;
                }
            }

            this.targetingState = State.PROCESSING;
            return -gyroOutput;

        } else {
            // if we aren't targeting after gyroPIDTargetSet, then there is no
            // target in view
            this.targetingState = State.NO_TARGET;
            SmartDashboard.putBoolean("Aimed At Goal: ",false);
            return 0.0;
        }
    }

    private boolean gyroPIDTargetSet(double gyroAngle) {
        this.count++;
        
       SimParticleInfo target = sensorIn.getParticleInfo();
              
        if (target != null) {
           //This.gyroPID.resetPreviousVal();
            //this.gyroPID.resetErrorSum();
            this.gyroPID.setDesiredValue(gyroAngle + target.getAngleToTarget());
            if (this.count == 1 ){
            	this.gyroPID.setDoneRange(1.5);
            	this.gyroPID.setTimeOut(START_TIMEOUT);
            }else if(this.count == 2){
            	this.gyroPID.setDoneRange(0.5);
            	this.gyroPID.setTimeOut(START_TIMEOUT);
            }else if(this.count > 2){
            	this.gyroPID.setDoneRange(PRECISE_DONE_RANGE);
            	this.gyroPID.setTimeOut(PRECISE_TIMEOUT);
            }else{
            	this.gyroPID.setDoneRange(INITIAL_DONE_RANGE);
            	this.gyroPID.setTimeOut(START_TIMEOUT);
            }
            
            return true;
        } else {
        	this.reset();
            // no target
            return false;
        }
        
       
    }

    public State getTargetingState() {
        return this.targetingState;
    }

    public void reset() {
        isTargeting = false;
        this.targetingState = State.NO_TARGET;
        this.gyroPID.resetErrorSum();
        this.gyroPID.resetPreviousVal();
        this.gyroPID.setDoneRange(INITIAL_DONE_RANGE);
        this.gyroPID.setTimeOut(START_TIMEOUT);
        this.count = 0;
    }

    public void updateConstantsFromSmartDashboard() {
        this.gyroP   = SmartDashboard.getNumber("Drive Gyro P: ", this.gyroP);
        this.gyroI   = SmartDashboard.getNumber("Drive Gyro I: ", this.gyroI);
        this.gyroD   = SmartDashboard.getNumber("Drive Gyro D: ", this.gyroD);
        //this.gyroEps = SmartDashboard.getNumber("Drive Gyro Eps: ", this.gyroEps);
        this.gyroPID.setConstants(this.gyroP, this.gyroI, this.gyroD, this.gyroEps, this.gyroEps * 0.8);
    }

    public void outputData(double gyroAngle) {
        SmartDashboard.putNumber("Targeting Angle Offset: ",
                                 this.gyroPID.getDesiredVal() - gyroAngle);
        SmartDashboard.putNumber("Targeting Image Count: ", this.count);
        
    }

    public enum State {
        NO_TARGET,
        PROCESSING,
        DONE
    }
}
