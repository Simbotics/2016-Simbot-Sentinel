/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simbotics.robot.util;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Mike
 */
public class SimPID {
    
    private double pConst;
    private double iConst;
    private double dConst;
    private double desiredVal;
    private double previousError;
    private double errorSum;
    private double errorIncrement;
    private double iRange;
    private double doneRange;
    private boolean firstCycle;
    private double maxOutput;
    private int minCycleCount;
    private int cycleCount;
    private boolean vomit;
    private boolean iReset = true;
    
    public SimPID() {
        this(0.0, 0.0, 0.0, 0.0,0.0);
    }
    
    public SimPID(double p, double i, double d,double eps,double iRange) {
        this.pConst = p;
        this.iConst = i;
        this.dConst = d;
        this.iRange = iRange;
        this.doneRange = eps;
        
        this.desiredVal = 0.0;
        this.firstCycle = true;
        this.maxOutput = 1.0;
        this.errorIncrement = 1.0;
        
        this.cycleCount = 0;
        this.minCycleCount = 5;
        this.vomit = false;
    }
    
    public SimPID(double p, double i, double d, double eps){
    	this(p,i,d,eps,eps*0.8);
    }
    
    public SimPID(double p, double i, double d) {
        this(p, i, d, 1.0);
    }
    
    public void setConstants(double p, double i, double d) {
        this.pConst = p;
        this.iConst = i;
        this.dConst = d;
    }
    
    public void setConstants(double p, double i, double d, double eps, double iRange){
    	 this.pConst = p;
         this.iConst = i;
         this.dConst = d;
         this.doneRange = eps;
         this.iRange = iRange;
    }
    
    public void setConstants(double p, double i, double d, double eps){
    	setConstants(p,i,d,eps,eps*0.8);
   }
    
    public void setVomitTrue() {
    	this.vomit = true;
    }
    
    public void turnOffIReset(){
    	this.iReset = false;
    }
    
    public void turnOnIReset(){
    	this.iReset = true;
    }
    
    public void setDoneRange(double range) {
        this.doneRange = range;
    }
    
    public void setIRange(double eps) {
        this.iRange = eps;
    }
    
    public void setDesiredValue(double val) {
        this.desiredVal = val;
    }
    
    public void setMaxOutput(double max) {
        if(max < 0.0) {
            this.maxOutput = 0.0;
        } else if(max > 1.0) {
            this.maxOutput = 1.0;
        } else {
            this.maxOutput = max;
        }
    }
    
    public void setMinDoneCycles(int num) {
        this.minCycleCount = num;
    }
    
    public void resetErrorSum() {
        this.errorSum = 0.0;
    }
    
    public double getDesiredVal() {
        return this.desiredVal;
    }
    
        
    public double calcPID(double current) {
    	return calcPIDError(this.desiredVal - current);
    }
    
    
    public double calcPIDError (double error){
    	double pVal = 0.0;
        double iVal = 0.0;
        double dVal = 0.0;
        
        if(this.firstCycle) {
            this.previousError = error;
            this.firstCycle = false;
        }
        
        ///////P Calc///////
        pVal = this.pConst * error;
        
        ///////I Calc///////
        
        //+ error outside of acceptable range
        if(error > this.iRange) {
            //check if error sum was in the wrong direction
            if(this.errorSum < 0.0) {
                this.errorSum = 0.0;
            }
            //only allow up to the max contribution per cycle
            this.errorSum += Math.min(error, this.errorIncrement);
        } //- error outside of acceptable range
        else if(error < -1.0 * this.iRange) {
            //error sum was in the wrong direction
            if(this.errorSum > 0.0) {
                this.errorSum = 0.0;
            }
            //add either the full error or the max allowable amount to sum
            this.errorSum += Math.max(error, -1.0 * this.errorIncrement);
        }
        //within the allowable epsilon
        else {
            //reset the error sum
        	if(this.iReset){
        		this.errorSum = 0.0;
        	}
        }
        //i contribution (final) calculation
        iVal = this.iConst * this.errorSum;
        
        ///////D Calc///////
        double deriv = error - this.previousError;
        dVal = this.dConst * deriv;
        
        //overal PID calc
        double output = pVal + iVal + dVal;
        
        //limit the output
        output = SimLib.limitValue(output, this.maxOutput);
        
        //store current value as previous for next cycle
        this.previousError = error;
        
        if(this.vomit) {
        	SmartDashboard.putNumber("P out", pVal);
        	SmartDashboard.putNumber("I out", iVal);
        	SmartDashboard.putNumber("D out", dVal);
        }
        
        return output;
    }
    
    public boolean isDone() {
        double currError = Math.abs(this.previousError);
        
        //close enough to target
        if(currError <= this.doneRange) {
            this.cycleCount++;
        }
        //not close enough to target
        else {
            this.cycleCount = 0;
        }
        
        return this.cycleCount > this.minCycleCount;
    }
    
    public boolean getFirstCycle(){
    	return this.firstCycle;
    }
    
    public void resetPreviousVal() {
        this.firstCycle = true;
    } 
}
