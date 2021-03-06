package org.simbotics.robot.io;
 
import java.io.*;
import java.util.Date;

import org.simbotics.robot.io.RobotOutput;
import org.simbotics.robot.io.SensorInput;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
 
public class Logger {
   
    private BufferedWriter writer;
    private boolean logging =false; 
    private final String loggerBoolean = "Logging";
    private static Logger instance;
    private String fileName ="";
    private final String SDFileName = "File Name: ";
    SensorInput sensorIn;
    RobotOutput robotOut;
    DriverStation ds;
    
    private int max = 0;
    
    private String path;
    
    public static Logger getInstance() {
        if(instance == null) {
            instance = new Logger();
        }
        return instance;
    }
 
    private Logger() {
        this.sensorIn = SensorInput.getInstance();
        this.robotOut = RobotOutput.getInstance();
        this.ds = DriverStation.getInstance();
        SmartDashboard.putBoolean(this.loggerBoolean, this.logging);
        this.logging= SmartDashboard.getBoolean(this.loggerBoolean);
        SmartDashboard.putString(this.SDFileName, this.fileName);
        this.fileName = SmartDashboard.getString(SDFileName);
        File f = new File("/simlogs");
        if(!f.exists()) {
        	f.mkdir();
        }
        
    	File[] files = new File("/simlogs").listFiles();
    	if(files != null) {
	        for(File file : files) {
	            if(file.isFile()) {
	                System.out.println(file.getName());
	                try {
	                    int index = Integer.parseInt(file.getName().split("_")[0]);
	                    if(index > max) {
	                        max = index;
	                    }
	                } catch (Exception e){
	                    e.printStackTrace();
	                }
	            }
	        }
    	} else {
    		max = 0;
    	}
    }
	    
    public void openFile() {
    	if(this.wantToLog() || this.ds.isFMSAttached()){
	        try{
	            path = this.getPath();
	            this.writer = new BufferedWriter(new FileWriter(path));
	            this.writer.write("time,leftOut,rightOut,backOut,leftSpeed,rightSpeed,backSpeed,xPosition,yPosition,batteryVolt,leftCurrent1,leftCurrent2,rightCurrent1,rightCurrent2,backCurrent1,backCurrent2");
	            this.writer.newLine();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
    	}
    }
    
    private String getPath() {
    	this.fileName = SmartDashboard.getString(SDFileName);
        if(this.ds.isFMSAttached()) {
            return String.format("/simlogs/%d_%s_%d_log.txt", ++this.max, this.ds.getAlliance().name(), this.ds.getLocation());
        }else if(this.fileName != null){ 
        	return String.format("/simlogs/%d_%s.txt",++this.max,this.fileName);
        }else {
            return String.format("/simlogs/%d_log.txt", ++this.max);
        }
    }
   
    public void logAll() {
    	if(this.wantToLog()){
	        try {
	            this.writer.write(String.format("%d", new java.util.Date().getTime()));
	            
	            
	            this.writer.write(String.format(",%d", this.sensorIn.getEncoderLeftSpeed()));
	            this.writer.write(String.format(",%d", this.sensorIn.getEncoderRightSpeed()));
	            
	            this.writer.write(String.format(",%.3f", this.sensorIn.getVoltage()));
	            this.writer.write(String.format(",%.3f", this.sensorIn.getCurrent(0)));
	            this.writer.write(String.format(",%.3f", this.sensorIn.getCurrent(1)));
	            this.writer.write(String.format(",%.3f", this.sensorIn.getCurrent(2)));
	            this.writer.write(String.format(",%.3f", this.sensorIn.getCurrent(12)));
	            this.writer.write(String.format(",%.3f", this.sensorIn.getCurrent(13)));
	            this.writer.write(String.format(",%.3f", this.sensorIn.getCurrent(14)));
	            
	            
	            
	            
	           
	            
	            this.writer.newLine();
	        }
	        catch (IOException e) {
	            e.printStackTrace();
	        }
    	}
    }
    
    public boolean wantToLog(){
    	this.logging= SmartDashboard.getBoolean(this.loggerBoolean);
    	return this.logging;
    }
    
    
    
    public void close() {
    	if(this.wantToLog()){
	    	if(this.writer != null) {
	            try {
	                this.writer.close();
	            }
	            catch (IOException e) {
	                e.printStackTrace();
	            }
	    	}
    	}
    }
}
