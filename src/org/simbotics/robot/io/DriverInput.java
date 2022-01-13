package org.simbotics.robot.io;

import org.simbotics.robot.util.LogitechF310Gamepad;

public class DriverInput {

    private static DriverInput instance;
    //private LogitechDualAction driver;
    private LogitechF310Gamepad driver;
    private LogitechF310Gamepad operator;
    
    private boolean autonIncreaseStepWasPressed = false;
    private boolean autonDecreaseStepWasPressed = false;
    
    private boolean autonIncreaseModeWasPressed = false;
    private boolean autonDecreaseModeWasPressed = false;
    
    private DriverInput() {
        this.driver = new LogitechF310Gamepad(0); // new LogitechDualAction(0);
        this.operator = new LogitechF310Gamepad(1);
    }
    
    public static DriverInput getInstance() {
        if(instance == null) {
            instance = new DriverInput();
        }
        return instance;
    }
    
    // -------------------------------------
    // --- DRIVER --------------------------
    // -------------------------------------
    
    public double getDriverX() {
    	return this.driver.getLeftX();
    }
    
    public double getDriverY() {
        return this.driver.getLeftY();
    }
    
    public double getDriverManualIntakeStick(){
    	return this.driver.getRightY(); 
    }
    
    public boolean getJumpButton() {
    	return this.driver.getStartButton();
    }
    
    public boolean getAimButton(){
    	return false;//this.driver.getPOVDown();
    }
    
    public boolean getBaseLockButton(){
    	return false;//this.driver.getBlueButton();//this.driver.getPOVRight();
    }
    
    public boolean getHeadingLockButton(){
    	return false;//this.driver.getBlueButton();
    }
    
    public boolean getHighGearButton(){
    	return this.driver.getLeftBumper();
    }
    
    public boolean getLowGearButton(){
    	return (this.driver.getLeftTrigger() > 0.3);
    }
    
    public boolean getDriverIntakeButton(){
    	return this.driver.getRightBumper();
    }
    
    public boolean getDriverOuttakeButton(){
    	return (this.driver.getRightTrigger() > 0.3);
    }
    
    public boolean getDriverArmDown(){
    	return this.driver.getBlueButton();
    }
    
    public boolean getDriverRightStickClick(){
    	return this.driver.getRightStickClick();
    }
    
    public boolean getDriverArmPickupButton(){
    	return this.driver.getGreenButton();
    }
    
    public boolean getDriverArmMidButton(){
    	return this.driver.getRedButton();
    }
    
    public boolean getDriverArmHighButton(){
    	return this.driver.getYellowButton();
    }
    
    public boolean getDriverShiftToDriveButton(){
    	return this.driver.getLeftStickClick();
    }
    
    public boolean getDriveManualIntakeButton(){
    	return false;//this.driver.getPOVLeft();
    }
    
    public boolean getDriverIntakeAdjustmentUp(){
    	return this.driver.getPOVUp();
    }
    
    public boolean getDriverIntakeAdjustmentDown(){
    	return this.driver.getPOVDown();
    }
    
    
    // -------------------------------------
    // --- OPERATOR ------------------------
    // -------------------------------------
    
    public boolean getShooterOnButton(){
		return this.operator.getLeftBumper();
    	
    }
    
    public boolean getShooterOffButton(){
    	return  (this.operator.getLeftTrigger() > 0.3);
    }

    public boolean getShooterSlowButton(){
    	return this.operator.getGreenButton();
    }
    
    public boolean getShooterMediumButton(){
    	return this.operator.getBlueButton();
    }
    
    public boolean getShooterFastButton(){
    	return this.operator.getRedButton();
    }
    
    public boolean getShooterFastestButton(){
    	return this.operator.getYellowButton();
    }
    
    public double getShooterAdjustmentStick(){
    	return this.operator.getRightY();
    }
    
    public boolean getOperatorShootButton(){
    	return (this.operator.getLeftY() > 0.3);
    }
    
    public boolean getShooterManualOn(){
    	return this.operator.getStartButton();
    }
    
    public boolean getShooterManualOff(){
    	return this.operator.getBackButton();
    }
    
    public boolean getHangerReleaseButton(){
    	return this.operator.getRightBumper();
    }
    
    public boolean getHangerHangButton(){
    	return (this.operator.getRightTrigger() > 0.6);
    }
    
    public double getHangerHangTrigger(){
    	return this.operator.getRightTrigger();
    }
    
    public boolean getHangerArmExtendButton(){
    	return this.operator.getPOVUp();
    }
    
    public boolean getHangerArmRetractButton(){
    	return this.operator.getPOVDown();
    }
    

    
    // TODO: put me somewhere 
    public boolean getBackButton(){
    	return this.driver.getBackButton();
    }

    // -------------------------------------
    // --- AUTON SETUP ---------------------
    // -------------------------------------

   
    
    
    public boolean getAutonSetModeButton() {
        return this.driver.getGreenButton();
    }
    
    public boolean getAutonSetDelayButton() {
        return this.driver.getRedButton();
    }
    
    public double getAutonSelectStick() {
        return this.driver.getLeftY();
    }
    
    public double getAutonStepSelectStick() {
    	return this.driver.getRightY();
    }
    
    public boolean getAutonStepIncrease() {
    	// only returns true on rising edge
    	boolean result = this.driver.getRightBumper() && !this.autonIncreaseStepWasPressed;
    	this.autonIncreaseStepWasPressed = this.driver.getRightBumper();
    	return result;
    	
    }
    
    public boolean getAutonStepDecrease() {
    	// only returns true on rising edge
    	boolean result = this.driver.getLeftBumper() && !this.autonDecreaseStepWasPressed;
    	this.autonDecreaseStepWasPressed = this.driver.getLeftBumper();
    	return result;
    
    }
    
    public boolean getAutonModeIncrease() {
    	// only returns true on rising edge
    	boolean result = this.driver.getYellowButton() && !this.autonIncreaseModeWasPressed;
    	this.autonIncreaseModeWasPressed = this.driver.getYellowButton();
    	return result;
    	
    }
    
    public boolean getAutonModeDecrease() {
    	// only returns true on rising edge
    	boolean result = this.driver.getBlueButton() && !this.autonDecreaseModeWasPressed;
    	this.autonDecreaseModeWasPressed = this.driver.getBlueButton();
    	return result;
    
    }
     
    
}
