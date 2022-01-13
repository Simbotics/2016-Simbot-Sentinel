package org.simbotics.robot.teleop;

import org.simbotics.robot.io.DriverInput;
import org.simbotics.robot.io.RobotOutput;
import org.simbotics.robot.io.SensorInput;
import org.simbotics.robot.util.SimBezierCurve;
import org.simbotics.robot.util.SimLib;
import org.simbotics.robot.util.SimPID;
import org.simbotics.robot.util.SimPoint;
import org.simbotics.robot.util.SimTargeting;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TeleopDrive implements TeleopComponent {

	private static TeleopDrive instance;

	private static final boolean TUNING = true;
 
	private RobotOutput robotOut;
	private DriverInput driverIn;
	private SensorInput sensorIn;
	private SimTargeting simTargeting;

	private SimBezierCurve SimCurveXH;
	private SimBezierCurve SimCurveYH;

	private SimBezierCurve SimCurveXL;
	private SimBezierCurve SimCurveYL;
	
	private double baseLockP = 0.0;
	private double baseLockI = 0.0;
	private double baseLockD = 0.0;
	
	
	private double baseLockEps = 0.5;
	private double encoderIRange = 0.0;
	private SimPID encoderPID;

	private double gyroP = 0.0;
	private double gyroI = 0.0;
	private double gyroD = 0.0;
	private double gyroEps = 0;
	private double gyroIRange = 0.0;
	private SimPID gyroPID;

	private boolean firstCycle = true;

	public static TeleopDrive getInstance() {
		if (instance == null) {
			instance = new TeleopDrive();
		}
		return instance;
	}

	private TeleopDrive() {

		this.driverIn = DriverInput.getInstance();
		this.robotOut = RobotOutput.getInstance();
		this.sensorIn = SensorInput.getInstance();

		this.simTargeting = new SimTargeting();

		SimPoint zero = new SimPoint(0, 0);
		//High gear
		SimPoint xP1H = new SimPoint(0.0, 0.30);
		SimPoint xP2H = new SimPoint(0.45, -0.1);

		SimPoint yP1H = new SimPoint(0.0, 0.54);
		SimPoint yP2H = new SimPoint(0.45, -0.07);
		//Low gear
		SimPoint xP1L = new SimPoint(0.49, 0.12);
		SimPoint xP2L = new SimPoint(0.89, 0.41);
		
		SimPoint yP1L = new SimPoint(0.0, 0.54);
		SimPoint yP2L = new SimPoint(0.45, -0.07);
		
		
		SimPoint one = new SimPoint(1, 1);
		
		this.encoderPID = new SimPID(this.baseLockP, this.baseLockI,
				this.baseLockD, this.baseLockEps, this.encoderIRange);
		this.gyroPID = new SimPID(this.gyroP, this.gyroI, this.gyroD,
				this.gyroEps, this.gyroIRange);

		this.SimCurveXH = new SimBezierCurve(zero, xP1H, xP2H, one);
		this.SimCurveYH = new SimBezierCurve(zero, yP1H, yP2H, one);
		this.SimCurveXL = new SimBezierCurve(zero, xP1L, xP2L, one);
		this.SimCurveYL = new SimBezierCurve(zero, yP1L, yP2L, one);

		if (TUNING) {

		}
	}

	@Override
	public void calculate() {

		// driver inputs for drive
		// these may be overridden by automatic sequences below (aim, baselock, etc)
		
		double x =0;
		double y= 0;
		if(Math.abs(this.driverIn.getDriverX()) < 0.05 && Math.abs(this.driverIn.getDriverY()) < 0.05){
			x = 0;
			y = 0;
		}else if(this.robotOut.getHighGear()){
			x = this.SimCurveXH.getPoint(this.driverIn.getDriverX()).getY();
			y = this.SimCurveYH.getPoint(this.driverIn.getDriverY()).getY();
		}else if(!this.robotOut.getHighGear()){
			x = this.SimCurveXL.getPoint(this.driverIn.getDriverX()).getY();
			y = this.SimCurveYL.getPoint(this.driverIn.getDriverY()).getY();
		}
		
		
		
		// shifter control
		if (this.driverIn.getHighGearButton()) {
			this.robotOut.setShifter(true);
			
		} else if (this.driverIn.getLowGearButton()) {
			this.robotOut.setShifter(false);
			
		}
		
		
		if (this.TUNING) {
			this.updateConstantsFromSmartDashboard();
		}

		// not aiming anymore
		if (!this.driverIn.getAimButton()) {
			this.simTargeting.reset(); // reset the targetting so it will start fresh next time
			this.sensorIn.stopImageProcessing();  // tell camera to stop wasting processing time
			
		}
		

		// target aiming
		if (this.driverIn.getAimButton()) {
			// init
			if (firstCycle) {				
				this.encoderPID.setDesiredValue(this.sensorIn.getDriveInches()); // want to hold current front/back location
				this.firstCycle = false;
				this.sensorIn.startImageProcessing();  // tell camera to start processing
			}
			this.robotOut.setShifter(false); // low gear to make turning easier
			
			// still not facing target
			if(this.simTargeting.getTargetingState() != SimTargeting.State.DONE) {
				x = this.simTargeting.calculate();	
			} else {
				x = 0; // facing target, stop turning
			}
			// keep holding y position until they release aim button
			y = this.encoderPID.calcPID(this.sensorIn.getDriveInches());

		} else if (this.driverIn.getBaseLockButton()) {
			// init of base lock
			if (this.firstCycle) {
				this.gyroPID.setDesiredValue(this.sensorIn.getAngle()); // hold current angle
				this.encoderPID.setDesiredValue(this.sensorIn.getDriveInches()); // hold current y-position

				this.firstCycle = false;
			}
			
			this.robotOut.setShifter(false); // low gear to hold position easier
			
			// calculate outputs to hold current position
			x = -this.gyroPID.calcPID(this.sensorIn.getAngle());
			y = this.encoderPID.calcPID(this.sensorIn.getDriveInches());
		} else if (this.driverIn.getHeadingLockButton()) {  // like baselock but only controls x
			if (this.firstCycle) {
				this.gyroPID.setDesiredValue(this.sensorIn.getAngle()); // keep on current heading
				this.firstCycle = false;
			}
			x = -this.gyroPID.calcPID(this.sensorIn.getAngle()); 
			// note: y is unchanged from driver input, they still have that control
		} else {
			firstCycle = true;  // no automatic control running
		}

		// arcade calculations
		double leftOut = SimLib.calcLeftTankDrive(x, y);
		double rightOut = SimLib.calcRightTankDrive(x, y);

		// outputs to motors
		this.robotOut.setDriveLeft(leftOut);
		this.robotOut.setDriveRight(rightOut);
		

		
	}

	@Override
	public void disable() {
		this.robotOut.setDriveLeft(0.0);
		this.robotOut.setDriveRight(0.0);
	}

	public void updateConstantsFromSmartDashboard() {
		// Encoder PID
		this.baseLockP = SmartDashboard.getNumber("Baselock P: ",
				this.baseLockP);
		this.baseLockI = SmartDashboard.getNumber("Baselock I: ",
				this.baseLockI);
		this.baseLockD = SmartDashboard.getNumber("Baselock D: ",
				this.baseLockD);
		this.baseLockEps = SmartDashboard.getNumber("Baselock Eps",
				this.baseLockEps);
		
		
		
		
		this.encoderPID.setConstants(this.baseLockP, this.baseLockI,
				this.baseLockD, this.baseLockEps, this.encoderIRange);
		// Gyro PID
		this.gyroP = SmartDashboard.getNumber("Drive Gyro P: ", this.gyroP);
		this.gyroI = SmartDashboard.getNumber("Drive Gyro I: ", this.gyroI);
		this.gyroD = SmartDashboard.getNumber("Drive Gyro D: ", this.gyroD);
		this.gyroEps = SmartDashboard.getNumber("Drive Gyro Eps: ",
				this.gyroEps);
		this.gyroPID.setConstants(this.gyroP, this.gyroI, this.gyroD,
				this.gyroEps, this.gyroIRange);
	}
}
