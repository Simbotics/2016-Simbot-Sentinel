package org.simbotics.robot.auton.drive;

import org.simbotics.robot.auton.AutonCommand;
import org.simbotics.robot.auton.RobotComponent;
import org.simbotics.robot.io.RobotOutput;
import org.simbotics.robot.io.SensorInput;
import org.simbotics.robot.util.SimBangBang;
import org.simbotics.robot.util.SimLib;
import org.simbotics.robot.util.SimPID;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveToBackablePosition extends AutonCommand{

	private SensorInput sensorIn;
	private RobotOutput robotOut;

	private SimPID gyroPID;
	private double gyroP;
	private double gyroI;
	private double gyroD;
	
	private SimPID encPID;
	private double encP;
	private double encI;
	private double encD;

	private double horizontalDisplacement;
	private double verticalDisplacement;
	
	private int defensePosition;

	private boolean firstCycle = true;
	private boolean trueFirstCycle = true;
	private boolean hasTurned = false;
	
	//these are in inches
	private static final double ABSOLUTE_TOWER_X_POS = 170.7365;
	private static final double ABSOLUTE_TOWER_Y_POS = 170.71618;
	private static final double DEFENSE_WIDTH = 52.875;
	private static final double IDEAL_Y_POSITION_FOR_RETURN = 72.0; 

	private static final double DEFENSE_ONE_IDEAL_GOAL_X_TO_TOWER = DEFENSE_WIDTH / 2 - ABSOLUTE_TOWER_X_POS;
    private static final double DEFENSE_TWO_IDEAL_GOAL_X_TO_TOWER = DEFENSE_WIDTH * 3 / 2 - ABSOLUTE_TOWER_X_POS;
    private static final double DEFENSE_THREE_IDEAL_GOAL_X_TO_TOWER = DEFENSE_WIDTH * 5 / 2 - ABSOLUTE_TOWER_X_POS;
    private static final double DEFENSE_FOUR_IDEAL_GOAL_X_TO_TOWER = DEFENSE_WIDTH * 7 / 2 + ABSOLUTE_TOWER_X_POS;
    
	public DriveToBackablePosition( int defensePosition){
		super(RobotComponent.DRIVE);
		
		this.robotOut = RobotOutput.getInstance();
		this.sensorIn = SensorInput.getInstance();

		this.defensePosition = defensePosition;
		this.encP = SmartDashboard.getNumber("Drive Enc P: ");
		this.encI = SmartDashboard.getNumber("Drive Enc I: ");
		this.encD = SmartDashboard.getNumber("Drive Enc D: ");
		
		this.gyroP = SmartDashboard.getNumber("Drive Gyro P: ");
		this.gyroI = SmartDashboard.getNumber("Drive Gyro I: ");
		this.gyroD = SmartDashboard.getNumber("Drive Gyro D: ");
		
		//TODO: figure out correct PID valuesv
		this.encPID = new SimPID(this.encP, this.encI, this.encD, 0.5);
		this.encPID.setMaxOutput(1.0);
		this.encPID.setMinDoneCycles(5);
		
		this.gyroPID = new SimPID(this.gyroP, this.gyroI, this.gyroD, 0.5);
		this.gyroPID.setMinDoneCycles(5);
		
	}
	
	@Override
	public boolean calculate() {

		if(this.trueFirstCycle) {
			double xDistToTarget = 0.0;
			double yDistToTarget = 0.0;
			if (this.sensorIn.getParticleInfo() != null){
			    xDistToTarget =  -Math.sin(90 - this.sensorIn.getAbsoluteAngle()) * this.sensorIn.getParticleInfo().getDistanceToTarget();
			    yDistToTarget =  Math.cos(90 - this.sensorIn.getAbsoluteAngle()) * this.sensorIn.getParticleInfo().getDistanceToTarget();			
			}
		    	    

			switch (this.defensePosition) {
	            case 1:
	                this.horizontalDisplacement = DEFENSE_ONE_IDEAL_GOAL_X_TO_TOWER - xDistToTarget;
	                break;
	            case 2:
	                 this.horizontalDisplacement = DEFENSE_TWO_IDEAL_GOAL_X_TO_TOWER - xDistToTarget;
	                break;
	            case 3:
	                this.horizontalDisplacement = DEFENSE_THREE_IDEAL_GOAL_X_TO_TOWER - xDistToTarget;
	                break;
	            case 4:
	                this.horizontalDisplacement = DEFENSE_FOUR_IDEAL_GOAL_X_TO_TOWER - xDistToTarget;
	                break;
	            default:
	            	this.horizontalDisplacement = 0.0;
	                break;
	        }

	        this.verticalDisplacement = ABSOLUTE_TOWER_Y_POS - IDEAL_Y_POSITION_FOR_RETURN - yDistToTarget;

	        double desiredTurn = Math.atan(this.verticalDisplacement / this.horizontalDisplacement) + (90 - this.sensorIn.getAngle());
	        
	        SmartDashboard.putNumber("Desired Turn: ", desiredTurn);
	        
	        if(verticalDisplacement > 0) {
	        	if(horizontalDisplacement > 0) {
	        		desiredTurn = -Math.atan2(verticalDisplacement, horizontalDisplacement);
	        	} else {
	        		desiredTurn = 90 + Math.atan2(verticalDisplacement, horizontalDisplacement);        		
	        	}
	        } else {
	        	if(horizontalDisplacement > 0) {
	        		desiredTurn = Math.atan2(-verticalDisplacement, horizontalDisplacement);
	        	} else {
	        		desiredTurn = 90 + Math.atan2(-verticalDisplacement, -horizontalDisplacement);
	        	}
	        }

			this.gyroPID.setDesiredValue(desiredTurn);
			this.trueFirstCycle = false;
		}
		
		if (this.gyroPID.isDone()) {
			this.hasTurned = true;
		}

		double leftDrive;
		double rightDrive;

		if (!hasTurned) {
			double xVal = -this.gyroPID.calcPID(this.sensorIn.getAngle());
			leftDrive = SimLib.calcLeftTankDrive(xVal, 0.0);
			rightDrive = SimLib.calcRightTankDrive(xVal, 0.0);
			
		} else {
			if (this.firstCycle) {
				double targetDistance = Math.sqrt(this.horizontalDisplacement * this.horizontalDisplacement + this.verticalDisplacement * this.verticalDisplacement);
				
				this.encPID.setDesiredValue(this.sensorIn.getDriveInches() + targetDistance);
				this.gyroPID.setDesiredValue(this.sensorIn.getAngle());
				this.firstCycle = false;
			}

			if (this.encPID.isDone()) {
				this.robotOut.setDriveLeft(0);
				this.robotOut.setDriveRight(0);
				return true;
			} else {
				double yVal = this.encPID.calcPID(this.sensorIn.getDriveInches());
				double xVal = this.gyroPID.calcPID(this.sensorIn.getAngle());

				leftDrive = SimLib.calcLeftTankDrive(xVal, yVal);
				rightDrive = SimLib.calcRightTankDrive(xVal, yVal);
			}
		}
		this.robotOut.setDriveLeft(leftDrive);
		this.robotOut.setDriveRight(rightDrive);
		return false;
	}

	@Override
	public void override() {
		this.robotOut.setDriveLeft(0);
		this.robotOut.setDriveRight(0);
	}

}