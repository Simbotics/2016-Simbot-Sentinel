package org.simbotics.robot.imaging;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;

public class ParticleReport implements Comparable<ParticleReport> {

    private static double areaTarget = 1200;
    private static double ratioTarget = 0.8;
    private static double widthTarget = 60;
    private static double heightTarget = 24;
    private static double centerTarget = 160;

    private static double areaWeight = 0.1;
    private static double ratioWeight = 0.2; 
    private static double widthWeight = 0.5;
    private static double heightWeight = 0.05;
    private static double centerWeight = 0.15;
    private double areaThreshold = 0.2;
    private double heightThreshold = 190; 
	
    public static void setScoreConstants(double newAreaTarget, double newAreaWeight, double newRatioTarget, double newRatioWeight, double newWidthTarget, 
    									double newWidthWeight, double newHeightTarget, double newHeightWidth){
        areaTarget = newAreaTarget;
        areaWeight = newAreaWeight;
        ratioTarget = newRatioTarget;
        ratioWeight = newRatioWeight;
        widthTarget = newWidthTarget;
        widthWeight = newWidthWeight;
        heightTarget = newHeightTarget;
        heightWeight = newHeightWidth;
    }

    
    private double score;
    private double percentAreaToImageArea;

    private double ratio;

    private double areaScaleFactor = 0.2;
    private double ratioScaleFactor = 24;
    private double widthScaleFactor = 5;
    private double heightScaleFactor = 3;
    private double centerScaleFactor = 1.25;
    
    private double particleX;
    private double particleY;
    
    private double area;
    private double height;
    private double width;
  

    Image img;
    int particleIndex;
    private String output;


    public ParticleReport(Image img, int particleIndex) {
         this.img = img;
      
        this.particleIndex = particleIndex;
        this.calcScore();
    }

    

    public int compareTo(ParticleReport report) {
        // positive means (this) is good
        return (int)(this.score - report.score);
    }

  

    private void calcScore() {
    	// scoring is based on how far away (on either side) actual value is from target value
    	 this.percentAreaToImageArea = NIVision.imaqMeasureParticle(img, particleIndex, 0, NIVision.MeasurementType.MT_AREA_BY_IMAGE_AREA);
         this.particleY = NIVision.imaqMeasureParticle(img, particleIndex, 0, NIVision.MeasurementType.MT_CENTER_OF_MASS_Y);
       
         if(this.particleY > this.heightThreshold || this.percentAreaToImageArea < this.areaThreshold){
        	 this.score =0;
        	 return;
         }
    	// differences from target values
         this.area = NIVision.imaqMeasureParticle(this.img, this.particleIndex, 0, NIVision.MeasurementType.MT_AREA);
     	 this.height = NIVision.imaqMeasureParticle(this.img, this.particleIndex, 0, NIVision.MeasurementType.MT_BOUNDING_RECT_HEIGHT);;
         this.width = NIVision.imaqMeasureParticle(this.img, this.particleIndex, 0, NIVision.MeasurementType.MT_BOUNDING_RECT_WIDTH);
         this.particleX = NIVision.imaqMeasureParticle(this.img, this.particleIndex, 0, NIVision.MeasurementType.MT_CENTER_OF_MASS_X);
         this.ratio = this.height / this.width;
         
        double areaDiffFromTarget = Math.abs(Math.sqrt(areaTarget) - Math.sqrt(this.percentAreaToImageArea));
        double ratioDiffFromTarget = Math.abs(ratioTarget - this.ratio);
		double widthDiffFromTarget = Math.abs(widthTarget - this.width);
		double heightDIffFromTarget = Math.abs(heightTarget - this.height);
		double centerXDIffFromTarget = Math.abs(centerTarget - this.particleX);
        // applying weightings to different attributes
        double areaScore = Math.max(100 - areaDiffFromTarget * areaScaleFactor, 0) * areaWeight;
        double ratioScore = Math.max(100 - ratioDiffFromTarget * ratioScaleFactor, 0) * ratioWeight;
        double widthScore = Math.max(100 - widthDiffFromTarget * widthScaleFactor, 0) * widthWeight;
        double heightScore = Math.max(100 - heightDIffFromTarget * heightScaleFactor, 0) * heightWeight;
        double centerScore = Math.max(100 - centerXDIffFromTarget * centerScaleFactor, 0) * centerWeight;
        
        // adding up the total score
        this.score = areaScore + ratioScore + widthScore + heightScore + centerScore;
       
    }
    
    public String toString() {
    	return ("areaPercent: " +this.percentAreaToImageArea + "ratio: "+this.ratio+" ParX: "+this.particleX +" ParY: "+this.particleY);
    }
   

    public double getScore() {
        return this.score;
    }
    
    public double getParticleX() {
    	return this.particleX;
    }
    
    public double getParticleY() {
    	return this.particleY;
    }
    
    public double getArea() {
    	return this.area;
    }
    
    public double getHeight() {
    	return this.height;
    }
    
    public double getRatio() {
    	return this.ratio;
    }
}
