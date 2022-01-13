package org.simbotics.robot.auton.drive;

import org.simbotics.robot.auton.AutonCommand;
import org.simbotics.robot.auton.RobotComponent;
import org.simbotics.robot.io.RobotOutput;


public class DriveShift extends AutonCommand {

    private boolean toHighGear;
    
    public DriveShift(boolean highGear) {
        super(RobotComponent.DRIVE);
        
        this.toHighGear = highGear;
    }
    
    public boolean calculate() {
        RobotOutput.getInstance().setShifter(this.toHighGear);
        return true;
    }

	@Override
	public void override() {
		// TODO Auto-generated method stub
		
	}
    
    
}
