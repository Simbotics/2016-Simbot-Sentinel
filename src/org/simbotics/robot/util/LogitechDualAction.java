/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simbotics.robot.util;


import edu.wpi.first.wpilibj.Joystick;

/**
 *
 * @author Programmers
 */
public class LogitechDualAction {
    
    private Joystick joystick;
    
    public LogitechDualAction(int portNum) {
        this.joystick = new Joystick(portNum);
    }
    
    public double getLeftX() {
        return this.joystick.getRawAxis(0);
    }
    
    public double getLeftY() {
        return -this.joystick.getRawAxis(1);
    }
    
    public double getRightX() {
        return this.joystick.getRawAxis(2);
    }
    
    public double getRightY() {
        return -this.joystick.getRawAxis(3);
    }

    public boolean getGreenButton() {
        return this.joystick.getRawButton(2);
    }
    
    public boolean getBlueButton() {
        return this.joystick.getRawButton(1);
    }
    
    public boolean getRedButton() {
        return this.joystick.getRawButton(3);
    }
    
    public boolean getYellowButton() {
        return this.joystick.getRawButton(4);
    }
    
    public boolean getBackButton() {
        return this.joystick.getRawButton(9);
    }
    
    public boolean getStartButton() {
        return this.joystick.getRawButton(10);
    }
    
    public boolean getLeftBumper() {
        return this.joystick.getRawButton(5);
    }
    
    public boolean getRightBumper() {
        return this.joystick.getRawButton(6);
    }
    
    public boolean getLeftStickClick() {
        return this.joystick.getRawButton(11);
    }
    
    public boolean getRightStickClick() {
        return this.joystick.getRawButton(12);
    }
    
    public boolean getRightTrigger() {
        return this.joystick.getRawButton(8);
    }
    
    public boolean getLeftTrigger() {
        return this.joystick.getRawButton(7);
    }
    
}
