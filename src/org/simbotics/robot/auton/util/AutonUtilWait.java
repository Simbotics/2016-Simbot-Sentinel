/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.simbotics.robot.auton.util;

import org.simbotics.robot.auton.AutonCommand;
import org.simbotics.robot.auton.RobotComponent;

/**
 *
 * @author Michael
 */
public class AutonUtilWait extends AutonCommand {

    public AutonUtilWait() {
        super(RobotComponent.UTIL);
    }

    public boolean calculate() {
        return true;
    }

	@Override
	public void override() {
		// nothing to do
		
	}
    
    
    
}
