package org.simbotics.robot.util;

import org.simbotics.robot.imaging.ParticleReport;
import org.simbotics.robot.io.DriverInput;
import org.simbotics.robot.io.RobotOutput;
import org.simbotics.robot.io.SensorInput;


/** Be warned, long names. **/
public class SimParticleInfo {
	private RobotOutput robotOut;
    public static final double CAMERA_X_RESOLUTION = 320.0;
    public static final double CAMERA_Y_RESOLUTION = 240.0;
    public static final double CAMERA_X_OFFSET_FROM_ROTATION_AXIS = 0.0; // in inches
    public static final double CAMERA_Y_OFFSET_FROM_ROTATION_AXIS = 18.0; // in inches - MUST BE POSITIVE!!! (I think)
    public static final double CAMERA_DISTANCE_FROM_GROUND = 12.0; // in inches

    public static final double CAMERA_FOV = 68.5; // fov degrees
    public static final double CAMERA_DEGREES_PER_PIXEL = CAMERA_FOV / CAMERA_X_RESOLUTION;
    public static final double CAMERA_ANGLE_FROM_HORIZONTAL = 35.0; // in degrees
 
    public static final double OFFSET_DISTANCE = Math.sqrt(CAMERA_X_OFFSET_FROM_ROTATION_AXIS * CAMERA_X_OFFSET_FROM_ROTATION_AXIS +
                                                           CAMERA_Y_OFFSET_FROM_ROTATION_AXIS * CAMERA_Y_OFFSET_FROM_ROTATION_AXIS);
    public static final double ANGLE_FROM_CAMERA_CENTER_TO_ROTATION_AXIS =
        90.0 + ((CAMERA_X_OFFSET_FROM_ROTATION_AXIS == 0)
                ? 90.0
                : Math.toDegrees(Math.atan(CAMERA_Y_OFFSET_FROM_ROTATION_AXIS / CAMERA_X_OFFSET_FROM_ROTATION_AXIS)));

    public static final double ANGLE_TO_CAMERA_FROM_ROTATION_AXIS =  180 - ANGLE_FROM_CAMERA_CENTER_TO_ROTATION_AXIS;

    public static double TOWER_HEIGHT = (7.0 * 12.0) + 1.0; // in inches
    public static double TOWER_HEIGHT_FROM_CAMERA = TOWER_HEIGHT - CAMERA_DISTANCE_FROM_GROUND;

    private ParticleReport report;

    private double distance;
    private double angle;

    public SimParticleInfo(ParticleReport report) {
        this.report = report;
        this.robotOut = RobotOutput.getInstance();
        this.calculateInfo();
    }

    private void calculateInfo() {

        final double vertialAngleFromGroundToTarget = Math.toRadians(imageYToDegrees(report.getParticleY()) + CAMERA_ANGLE_FROM_HORIZONTAL);
        final double cameraDistance = TOWER_HEIGHT_FROM_CAMERA / Math.tan(vertialAngleFromGroundToTarget);
        final double horizontalAngleFromRotationAxisToTarget = ANGLE_FROM_CAMERA_CENTER_TO_ROTATION_AXIS + imageXToDegrees(report.getParticleX());
        this.distance = cosineLaw(cameraDistance, OFFSET_DISTANCE, horizontalAngleFromRotationAxisToTarget);

        this.angle = ANGLE_TO_CAMERA_FROM_ROTATION_AXIS - sineLaw(cameraDistance, distance, horizontalAngleFromRotationAxisToTarget);
    }

    public double getAngleToTarget() {
    	if(this.robotOut.getFarShot()){
    		return - 1.4*this.angle;
    	}else{
    		return - 2*this.angle;
    	}
    	
    }

    public double getDistanceToTarget() {
        return this.distance;
    }

    public double getXDistanceToTarget() {
        return Math.sin(90 - this.getAngleToTarget()) * this.getDistanceToTarget();
    }

    public double getYDistanceToTarget() {
    	
        return Math.cos(90 - this.getAngleToTarget()) * this.getDistanceToTarget();
    }

    private double imageYToDegrees(double imageY) {
        return (CAMERA_Y_RESOLUTION * 0.5 - imageY) * CAMERA_DEGREES_PER_PIXEL; // TODO: does this need to be flipped????
    }

    private double imageXToDegrees(double imageX) {
        return (imageX - CAMERA_X_RESOLUTION * 0.5) * CAMERA_DEGREES_PER_PIXEL;
    }

    private double cosineLaw(double b, double c, double angle) {
        final double aSquared = b * b + c * c - 2 * c * b * Math.cos(Math.toRadians(angle));
        return Math.sqrt(aSquared);
    }

    private double sineLaw(double a, double b, double angleB) {
        return Math.toDegrees(Math.asin(a/b*Math.sin(Math.toRadians(angleB))));
    }

    public String toString(){
        return ("Angle To Target: "+this.angle+" Distance to Target: "+this.distance);
    }
}
