/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simbotics.robot.util;

import edu.wpi.first.wpilibj.AnalogGyro;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Mike
 */
public class SimGyro extends AnalogGyro{
    private static final double FAILURE_CHECK = 80; // 50 was not big enough 
    private boolean hasFailed;
    private double resetAdjustment;
    private double initialAngle;
    private double prev;
    private double speed;
    
    public SimGyro(int channel, double initAngle) {
        super(channel);
        this.resetAdjustment = 0.0;
        this.initialAngle = initAngle;
        this.prev = initAngle;
        this.hasFailed = false;
    }
    
    public SimGyro(int channel) {
        this(channel, 0.0);
    }
    
    public double getAngle() {
        return this.initialAngle - (super.getAngle() - this.resetAdjustment);
    }
    
    public double getAbsoluteAngle() {
        double val = this.getAngle() % 360;
        
        if(val >= 0.0) {
            return val;
        } else {
            return 360 + val;
        }
        
    }
    
    public void reset(double initAngle) {
        this.initialAngle = initAngle;
        this.prev = initAngle; 
        this.resetAdjustment = super.getAngle();
    }
    
    public void reset() {
        this.reset(90);
    }
    
    
    public boolean hasFailed() { //flag for explosive gyro values
    	return this.hasFailed;
    }
    
    public void manualResetGyroFailed(){
    	this.hasFailed = false;
    }
    
    public void updateAngle() {
    	double curr = this.getAngle();
    	this.speed = curr - this.prev;
    	if (Math.abs(speed) > SimGyro.FAILURE_CHECK){
    		this.hasFailed = true;
    		SmartDashboard.putNumber("Gyro, Bad Tick: ", this.speed);
    	}
    	
    	this.prev = curr;
    }
    
    public double getSpeed()  {
    	return this.speed;
    }
}
