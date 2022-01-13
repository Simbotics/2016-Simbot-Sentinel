/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simbotics.robot.auton.util;

import org.simbotics.robot.auton.AutonCommand;
import org.simbotics.robot.auton.RobotComponent;

/**
 *
 * @author Michael
 */
public class AutonWait extends AutonCommand {

    private long startTime;
    private long delayTime;
    private boolean firstCycle;
    
    public AutonWait(long howLong) {
        super(RobotComponent.UTIL);
        this.delayTime = howLong;
        this.firstCycle = true;
    }

    /*
     * need to override checkAndRun so that it
     * blocks even before going in to its "run seat"
     */
    public boolean checkAndRun() {
        if(this.firstCycle) {
            this.firstCycle = false;
            this.startTime = System.currentTimeMillis();
        }
        
        long timeElapsed = System.currentTimeMillis() - this.startTime;
        
        if(timeElapsed < this.delayTime) {
            // haven't reached time limit yet
            return false;
        } else {
            // if reached time, use the normal checkAndRun
            return super.checkAndRun();
        }
    }
    
    public boolean calculate() {
        return true;
    }

	@Override
	public void override() {
		// nothing to do
		
	}
    
    
    
}
