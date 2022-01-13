/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simbotics.robot.auton.util;

import org.simbotics.robot.auton.AutonCommand;
import org.simbotics.robot.auton.AutonControl;
import org.simbotics.robot.auton.RobotComponent;

/**
 *
 * @author Michael
 */
public class AutonStop extends AutonCommand {

    public AutonStop() {
        super(RobotComponent.UTIL);
    }
    
    public boolean calculate() {
        AutonControl.getInstance().stop();
        return true;
    }

	@Override
	public void override() {
		// nothing to do
		
	}
    
}
