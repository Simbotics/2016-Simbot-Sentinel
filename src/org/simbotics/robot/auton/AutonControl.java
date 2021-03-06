/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simbotics.robot.auton;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import org.simbotics.robot.auton.mode.AutonBuilder;
import org.simbotics.robot.auton.mode.AutonMode;
import org.simbotics.robot.auton.mode.DefaultMode;
import org.simbotics.robot.auton.mode.step1Modes.CDF;
import org.simbotics.robot.auton.mode.step1Modes.LowBar;
import org.simbotics.robot.auton.mode.step1Modes.Moat;
import org.simbotics.robot.auton.mode.step1Modes.Ramparts;
import org.simbotics.robot.auton.mode.step1Modes.RockWall;
import org.simbotics.robot.auton.mode.step1Modes.RoughTerrian;
import org.simbotics.robot.auton.mode.step2Modes.DriveUpToGoalFrom2ForHighGoal;
import org.simbotics.robot.auton.mode.step2Modes.TurnToGoalFrom1;
import org.simbotics.robot.auton.mode.step2Modes.TurnToGoalFrom1DriveCloser;
import org.simbotics.robot.auton.mode.step2Modes.TurnToGoalFrom2;
import org.simbotics.robot.auton.mode.step2Modes.TurnToGoalFrom3;
import org.simbotics.robot.auton.mode.step2Modes.TurnToGoalFrom3MoveToMiddle;
import org.simbotics.robot.auton.mode.step2Modes.TurnToGoalFrom4;
import org.simbotics.robot.auton.mode.step2Modes.TurnToGoalFrom5;
import org.simbotics.robot.auton.mode.step3Modes.FarHighGoalShot;
import org.simbotics.robot.auton.mode.step4Modes.DriveToMiddleIntakeBall;
import org.simbotics.robot.auton.mode.step4Modes.DriveToMiddleIntakeBallCDF;
import org.simbotics.robot.auton.mode.step4Modes.DriveToMiddleIntakeBallFrom2;
import org.simbotics.robot.auton.mode.step4Modes.DriveToMiddleIntakeBallFrom2CDF;
import org.simbotics.robot.auton.mode.step4Modes.DriveToMiddleIntakeBallFrom3;
import org.simbotics.robot.auton.mode.step4Modes.DriveToMiddleIntakeBallFrom3CDF;
import org.simbotics.robot.auton.mode.step4Modes.DriveToMiddleIntakeBallFrom5CDF;
import org.simbotics.robot.auton.mode.step4Modes.DriveToMiddleIntakeBallGoTo3;
import org.simbotics.robot.auton.mode.step4Modes.DriveToMiddleIntakeBallGoTo3RT;
import org.simbotics.robot.auton.mode.step4Modes.DriveToMiddleIntakeBallGoTo5;
import org.simbotics.robot.auton.mode.step4Modes.DriveToMiddleIntakeBallGoTo5RT;
import org.simbotics.robot.auton.mode.step4Modes.DriveToMiddleIntakeBallRoughTerrain;
import org.simbotics.robot.auton.mode.step4Modes.FaceOuterworks;
import org.simbotics.robot.auton.mode.step4Modes.PickUpFrom1;
import org.simbotics.robot.auton.mode.step4Modes.PickUpFrom1BackUp;
import org.simbotics.robot.io.DriverInput;
import org.simbotics.robot.io.RobotOutput;
import org.simbotics.robot.util.Debugger;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 
 * @author Programmers
 */
public class AutonControl {

    private static AutonControl instance;
    
    public static final int NUM_ARRAY_MODE_STEPS = 4;
    public static final int CLOSE_SHOT_RPM= 4700;
    public static final int FAR_SHOT_RPM= 5450;
    public static final int ARM_PICKUP = 790;
    public static final int ARM_LOWEST = 955;
    public static final int DRIVE_UNTIL_OUTERWORKS_TIME = 2000;
    
    private int autonDelay;
    private long autonStartTime;
    
    private boolean running;
    
    private int currentModeOffset=0;
    private int prevModeOffset =0;
    private boolean offsetWasChanged = false;
    
    
    private int curAutonStepToSet = 0;
    private int[] autonSubmodeSelections = new int[NUM_ARRAY_MODE_STEPS];
    private ArrayList<ArrayList<AutonMode>> autonSteps = new ArrayList<>();
    
    private int currIndex;
    private AutonCommand[] commands;
        
    private String autoSelectError = "NO ERROR";
    
    public static AutonControl getInstance() {
        if(instance == null) {
            instance = new AutonControl();
        }
        return instance;
    }

    private AutonControl() {
        this.autonDelay = 0;
        this.currIndex = 0;
        
        for(int i = 0; i < NUM_ARRAY_MODE_STEPS; i++) {
        	this.autonSteps.add(new ArrayList<AutonMode>());
        	this.autonSubmodeSelections[i] = 0; // default to default auto modes 
        }
        	
        // GOTCHA: remember to put all auton modes here
        
        // --- STEP 1 SUBMODES
        ArrayList<AutonMode> step1 = this.autonSteps.get(0);
        step1.add(new DefaultMode());      //0
        step1.add(new LowBar());
        step1.add(new CDF());
        step1.add(new Ramparts());
        step1.add(new Moat());
        step1.add(new RockWall());
        step1.add(new RoughTerrian());
       
        
        
        
        // --- STEP 2 SUBMODES
        ArrayList<AutonMode> step2 = this.autonSteps.get(1);
        step2.add(new DefaultMode()); //0
        step2.add(new TurnToGoalFrom1());
        step2.add(new TurnToGoalFrom1DriveCloser());
        step2.add(new TurnToGoalFrom2());
        step2.add(new DriveUpToGoalFrom2ForHighGoal());
        step2.add(new TurnToGoalFrom3());
        step2.add(new TurnToGoalFrom3MoveToMiddle());
        step2.add(new TurnToGoalFrom4());
        step2.add(new TurnToGoalFrom5());   
      
        
        // --- STEP 3 SUBMODES
        ArrayList<AutonMode> step3 = this.autonSteps.get(2);
        step3.add(new DefaultMode()); //0
        //step3.add(new LowGoal());  
        //step3.add(new CloseHighGoalShot());  
        step3.add(new FarHighGoalShot());  
        
        // --- STEP 4 SUBMODES
        ArrayList<AutonMode> step4 = this.autonSteps.get(3);
        step4.add(new DefaultMode());
        step4.add(new FaceOuterworks());
        step4.add(new PickUpFrom1());
        step4.add(new PickUpFrom1BackUp());
        step4.add(new DriveToMiddleIntakeBall());
        step4.add(new DriveToMiddleIntakeBallRoughTerrain());
        step4.add(new DriveToMiddleIntakeBallCDF());
        step4.add(new DriveToMiddleIntakeBallGoTo3());
        step4.add(new DriveToMiddleIntakeBallGoTo3RT());
        step4.add(new DriveToMiddleIntakeBallFrom2());
        step4.add(new DriveToMiddleIntakeBallFrom2CDF());
        step4.add(new DriveToMiddleIntakeBallFrom3());
        step4.add(new DriveToMiddleIntakeBallFrom3CDF());
        step4.add(new DriveToMiddleIntakeBallFrom5CDF());
        step4.add(new DriveToMiddleIntakeBallGoTo5());
        step4.add(new DriveToMiddleIntakeBallGoTo5RT());
        
        
        
        
       
    }

    public void initialize() {
        Debugger.println("START AUTO");
        
        this.currIndex = 0;
        this.running = true;

        // initialize auton in runCycle
        AutonBuilder ab = new AutonBuilder();

        // add auton commands from all the different steps
        for(int i = 0; i < this.autonSteps.size(); i++) {
        	this.autonSteps.get(i).get(this.autonSubmodeSelections[i]).addToMode(ab);
        }
        
        // get the full auton mode
        this.commands = ab.getAutonList();

        this.autonStartTime = System.currentTimeMillis();
        
        // clear out each components "run seat"
        AutonCommand.reset();
    }
    
    public void runCycle() {
        // haven't initialized list yet
        long timeElapsed = System.currentTimeMillis() - this.autonStartTime;
        if(timeElapsed > this.getAutonDelayLength() && this.running) {
            Debugger.println("Current index " + this.currIndex, "QTIP");
            
            
                // start waiting commands
                while(this.currIndex < this.commands.length &&
                        this.commands[this.currIndex].checkAndRun()) {
                    this.currIndex++;
               
            }
            // calculate call for all running commands
            AutonCommand.execute();
        } else {
            RobotOutput.getInstance().stopAll();
        }

    
    }
    
    public void stop() {
        this.running = false;
    }
    
    public long getAutonDelayLength() {
        return this.autonDelay * 500;
    }

    public void updateModes() {
        DriverInput driverIn = DriverInput.getInstance();
        
        if(driverIn.getAutonStepIncrease()) {
        	this.curAutonStepToSet++;
        	this.currentModeOffset = 0;
        	this.curAutonStepToSet = Math.min(this.curAutonStepToSet, this.autonSteps.size() - 1);
        }
        
        if(driverIn.getAutonStepDecrease()) {
        	this.curAutonStepToSet--;
        	this.currentModeOffset = 0;
        	this.curAutonStepToSet = Math.max(this.curAutonStepToSet, 0);
        }
        
        if(driverIn.getAutonModeIncrease()){
        	this.currentModeOffset++;
        
        }
        
        if(driverIn.getAutonModeDecrease()){
        	this.currentModeOffset--;
        }
        
        if(this.currentModeOffset != this.prevModeOffset){
        	this.offsetWasChanged = true;
        }else{
        	this.offsetWasChanged = false;
        }
        
        this.prevModeOffset = this.currentModeOffset;
        
        
        
        
       	boolean updatingAutoMode = false;

        try {
        
        	
        if(driverIn.getAutonSetModeButton() || this.offsetWasChanged) {
            updatingAutoMode = true;
            
            double val = driverIn.getAutonSelectStick();
            val = (val + 1) / 2.0;  // make it positive and between 0 - 1.0
            	
        	
            if(!driverIn.getAutonSetModeButton()){
            	val = 0.0;
            }
            	
            
            
            // figure out which auton mode is being selected
            int autonMode = ((int)(val *  this.autonSteps.get(this.curAutonStepToSet).size()))+this.currentModeOffset;
            
            
            
            // make sure we didn't go off the end of the list
            autonMode = Math.min(autonMode, this.autonSteps.get(this.curAutonStepToSet).size() - 1);          
            if(autonMode < 0 ){
            	autonMode = 0;
            }
            
            this.autonSubmodeSelections[this.curAutonStepToSet] = autonMode;
            

           
            
            /*
            if(val < 0) { this.autonMode = 0; }
            else { this.autonMode = 1; }
         */   
        } else if(driverIn.getAutonSetDelayButton()) {
            this.autonDelay = (int)((driverIn.getAutonSelectStick() + 1) * 5.0);
            if(this.autonDelay < 0 ) {
            	this.autonDelay =0;
            }
        }
        
        } catch(Exception e) {
        	//this.autonMode = 0;
        	// TODO: some kind of error catching
        	
        	
        	StringWriter sw = new StringWriter();
        	e.printStackTrace(new PrintWriter(sw));
        	
        	
        	this.autoSelectError = sw.toString();
        
        }
        
        // display steps of auto
        for(int i = 0; i < autonSteps.size(); i++) {
	        // name of the current auton mode
	        String name = this.autonSteps.get(i).get(this.autonSubmodeSelections[i]).getClass().getName();
	
	        // make sure there is a '.'
	        if(name.lastIndexOf('.') >= 0) {
	            // get just the last bit of the name
	            name = name.substring(name.lastIndexOf('.'));
	        }
	        
	        String outputString = "" + autonSubmodeSelections[i] + name + "";
	        
	        SmartDashboard.putString("Auton Step " + (i+1) + ": ", outputString);
	       
	        if(updatingAutoMode) {
            	//System.out.print(this.autonSubmodeSelections[i] + "-");
	        	System.out.println("Step " + (i + 1) + ": " + outputString);
	        }
	        	
	        	// System.out.println();
	        
	        //SmartDashboard.putString("Auton Error: ", this.autoSelectError);
        }
        
        if(updatingAutoMode) {
        	System.out.println("----------------------------------");
        }
        
        // step we are currently modifying
        SmartDashboard.putNumber("SETTING AUTON STEP: ", this.curAutonStepToSet+1);
        
        // delay 
        String delayAmt = "";
        if(this.autonDelay < 10) {
            // pad in a blank space for single digit delay
            delayAmt = " " + this.autonDelay;
        } else {
            delayAmt = "" + this.autonDelay;
        }
        SmartDashboard.putNumber("Auton Delay: ", this.autonDelay);


    }

}
