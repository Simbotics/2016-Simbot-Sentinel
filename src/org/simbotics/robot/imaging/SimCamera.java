package org.simbotics.robot.imaging;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.Range;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.USBCamera;

import java.util.ArrayList;
import java.util.Collections;

import org.simbotics.robot.util.SimParticleInfo;


public class SimCamera implements Runnable {

	
    private USBCamera camera;
    private CameraServer camServer;

    // images to load in to/modify
    private Image image;
    private Image binaryImage;

   
    private boolean isProcessingImage = false;
    private ParticleReport bestReport; 
    
    
    // HSL Ranges for targetting
    private Range H_RANGE = new NIVision.Range(95, 135);	
    private Range S_RANGE = new NIVision.Range(60, 255);	
    private Range L_RANGE = new NIVision.Range(95, 255);	

    private boolean usingSmartdashboard = true;


    public SimCamera() {
    	 if(this.usingSmartdashboard) {
             initSmartdashboardStream();
         }
    	
    	 try {
    		 this.camera = new USBCamera("cam0");
    	 } catch(Exception e) {
    		 System.out.println(">>>>>> ERROR MAKING CAMERA");
    		 e.printStackTrace();
    	 }
        // check if the camera was created before initializing
        if(this.camera != null) {
        	this.initCamera();
        }  
        
        // allocate space for images
        this.image = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
        this.binaryImage = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_U8, 0);
    }
    
    private void initCamera() {
    	this.camera.setFPS(30);
        this.camera.setBrightness(0);
        this.camera.setSize((int)SimParticleInfo.CAMERA_X_RESOLUTION, (int)SimParticleInfo.CAMERA_Y_RESOLUTION);
        this.camera.updateSettings(); //When calling camera configuration methods, call this last
        
        this.camera.openCamera();
        this.camera.startCapture();
    }

    private void initSmartdashboardStream() {
        this.camServer = CameraServer.getInstance();      
    }

    public void run() {
    	
        while(true) {
    
    		// not initialized yet
    		if(this.camera == null) {
    			try {
    				this.camera = new USBCamera("cam0");
    			} catch(Exception e) {
    				System.out.println(">>>>> ERROR MAKING CAMERA");
    				e.printStackTrace();
    				
    			}
    				
    			if(this.camera != null) {
    				this.initCamera();
    			}
    			
    		} else { // camera already initialized
    			
    			try {
					this.camera.getImage(this.image);
					if(this.isProcessingImage) { // are we currently targeting
						this.processImage();
					}  
					if(this.usingSmartdashboard) {
						this.sendToDashboard();
					}
    			} catch(Exception e) {
    				System.out.println("CAMERA ERROR >>>>>>>>>>>>>>>>");
    				e.printStackTrace();
    			
    				this.bestReport = null;  // error reading images, get rid of stale data
    			}
    			
	
	          
        	}
    		
    		Thread.yield();
	   }	    
    }

    
    // starting image processing (we want to aim)
    public void startProcessing() {
    	this.isProcessingImage = true;
    }
    
    // stopping image processing (done aiming)
    public void stopProcessing() {
    	this.isProcessingImage = false;
    }
    
    
    private void sendToDashboard() {
        if(this.camServer != null) {
        	// see if we are showing the binary or color image
	    	boolean useBinary = SmartDashboard.getBoolean("Image Use Binary");
	    	// send the appropriate image to smart dashboard
	    	if(useBinary) {
	    		this.camServer.setImage(this.binaryImage);
	    	} else {
	    		this.camServer.setImage(this.image);
	    	}
        }
    }

    public void processImage() {
        try {
        	// HSL thresholding to binary image
            NIVision.imaqColorThreshold(this.binaryImage, this.image, 255,
                                        NIVision.ColorMode.HSL,
                                        this.H_RANGE, this.S_RANGE, this.L_RANGE);

            // see how many particles we have
            int numParticles = NIVision.imaqCountParticles(binaryImage, 1);
            
            // get reports for each particle
            ArrayList<ParticleReport> reports = new ArrayList<ParticleReport>(numParticles);
            
            // initialize the particleReports
            for (int i = 0 ; i < numParticles ; i++) {
                reports.add(new ParticleReport(this.binaryImage,i));
            }

            // remove all pointless reports
            reports.removeIf((report)-> report.getScore() < 10);
            
           /* for (ParticleReport report : reports) {
            	report.calcProperties();
            }*/
            
            // find the report with the highest score (Comparable ordering)
            if(reports.size() == 0) {  // nothing bigger than the area threshold
            	this.bestReport = null;
            } else {
            	this.bestReport = Collections.max(reports);
            }
        
        } catch (Exception e) {
            // error if there is no bestReport, or no camera
        	System.out.println("IMAGE PROCESSING ERROR >>>>>>>");
        	e.printStackTrace();
        	this.bestReport = null;  // had an error - get rid of stale data
        }
    }
    
    public synchronized ParticleReport getBestParticle() {
    	return this.bestReport;    	
    }
    
   
    
    
    
    
}
