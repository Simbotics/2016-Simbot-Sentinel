/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simbotics.robot.util;

import edu.wpi.first.wpilibj.Counter;

/**
 *
 * @author Michael
 */
public class SimCounter extends Counter {
    
    public SimCounter(int pinNum) {
        super(pinNum);
        //this.start();
    }
    
}
