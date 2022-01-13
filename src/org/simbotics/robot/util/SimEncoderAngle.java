package org.simbotics.robot.util;

import org.simbotics.robot.io.SensorInput;

public class SimEncoderAngle {

	private static final double WHEEL_BASE_WIDTH = 20; // INCHES 
	private SensorInput sensorIn;

    private double netAngle = 0.0;
	
	
	public SimEncoderAngle(SensorInput sensorIn){
		this.sensorIn = sensorIn;
	}

    public void update() {
        double arcLength =
            this.sensorIn.getRightDriveSpeedInches() - this.sensorIn.getLeftDriveSpeedInches();
        
        double angleChange = Math.toDegrees(arcLength / WHEEL_BASE_WIDTH);
        netAngle += angleChange;
    }

    public void reset(double angle){
        this.netAngle = angle;
    }
	public void reset(){
        reset(90);
	}
	
	public double getAngle(){
        return netAngle;
	}
}
