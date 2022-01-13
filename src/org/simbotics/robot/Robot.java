
package org.simbotics.robot;

import org.simbotics.robot.Robot;
import org.simbotics.robot.auton.AutonControl;
import org.simbotics.robot.io.DriverInput;
import org.simbotics.robot.io.Logger;
import org.simbotics.robot.io.RobotOutput;
import org.simbotics.robot.io.SensorInput;
import org.simbotics.robot.teleop.TeleopControl;
import org.simbotics.robot.util.SmartDashboardConstants;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Robot extends IterativeRobot {
  

	private RobotOutput robotOut;
	private DriverInput driverInput;
	private SensorInput sensorInput;
	private TeleopControl teleopControl;
    private Logger logger;
    

    public void robotInit() {
    	
    	SmartDashboardConstants.pushValues();
    	this.robotOut = RobotOutput.getInstance();
    	this.driverInput = DriverInput.getInstance();
    	this.sensorInput = SensorInput.getInstance();
    	this.teleopControl = TeleopControl.getInstance();
    	this.logger = Logger.getInstance();
    }

    
    public void disabledInit() {
    	this.robotOut.stopAll();
    	this.logger.close();
    	this.teleopControl.disable();
    }
  
    
    @Override
	public void disabledPeriodic() {
    	this.sensorInput.update();
    	AutonControl.getInstance().updateModes();	
	}
    

    public void autonomousInit() {
    	AutonControl.getInstance().initialize();
    	SensorInput.getInstance().reset();
    }
    
    
    public void autonomousPeriodic() {
    	this.sensorInput.update();
    	AutonControl.getInstance().runCycle();
    }

    
    public void teleopInit(){
    	this.logger.openFile();
    	//SensorInput.getInstance().reset();
    }
    
    
    public void teleopPeriodic() {
    	
    	if(this.driverInput.getJumpButton()) {
    		this.sensorInput.reset();
    	}
    	
    	this.sensorInput.update();
    	this.teleopControl.runCycle();
    	
        this.logger.logAll(); // write the log data
    }
}
