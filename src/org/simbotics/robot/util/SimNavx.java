package org.simbotics.robot.util;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SPI.Port;

public class SimNavx extends AHRS{

	
	
	private double prevAngle;
	
	private double angle;
	
	public SimNavx(){
		super(SPI.Port.kMXP);
		this.reset();
	}
	
	public double getAngle(){
		return 180 - (angle + this.getFusedHeading());
	}
	
	public void update(){	
		double diff = this.getFusedHeading() - this.prevAngle;
		
		if(diff > 180){
			this.angle -= 360;
		}else if(diff < -180){
			this.angle += 360;
		}
		this.prevAngle += diff;
	}
	
	public void reset(){
		this.angle = 90 - this.getFusedHeading();
		this.prevAngle = this.getFusedHeading();
	}
	

}
