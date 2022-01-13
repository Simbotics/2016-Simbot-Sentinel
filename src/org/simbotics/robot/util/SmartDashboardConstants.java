package org.simbotics.robot.util;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SmartDashboardConstants {
	public static void pushValues(){
		
		SmartDashboard.putNumber("Arm P Times 100: ", 0.65); // 0.4
		SmartDashboard.putNumber("Arm I Times 100: ", 0.35); // 0.2
		SmartDashboard.putNumber("Arm D Times 100: ", 0.9); // 0.7
		SmartDashboard.putNumber("Arm Eps: ", 5);
		
		SmartDashboard.putNumber("Base Lock P: ", 0.0);
		SmartDashboard.putNumber("Base Lock I: ", 0.0);
		SmartDashboard.putNumber("Base Lock D: ", 0.0);
		
		SmartDashboard.putNumber("Drive Gyro P: ", 0.035);
		SmartDashboard.putNumber("Drive Gyro I: ", 0.012);
		SmartDashboard.putNumber("Drive Gyro D: ", 0.35);
		SmartDashboard.putNumber("Drive Gyro Eps: ", 0.5);
		
		SmartDashboard.putNumber("Drive Enc P: ", 0.060);
		SmartDashboard.putNumber("Drive Enc I: ", 0.01);
		SmartDashboard.putNumber("Drive Enc D: ", 0.010);
		SmartDashboard.putNumber("Drive Enc Eps: ", 0.5);
		
		SmartDashboard.putNumber("Drive P High: ", 0.035);
		SmartDashboard.putNumber("Drive I High: ", 0.015);
		SmartDashboard.putNumber("Drive D High: ", 0.012);
		SmartDashboard.putNumber("Drive Eps High: ", 0.5);
		
		SmartDashboard.putNumber("Baselock P: ", 0.15);
		SmartDashboard.putNumber("Baselock I: ", 0.01);
		SmartDashboard.putNumber("Baselock D: ", 0.005);
		SmartDashboard.putNumber("Baselock Eps: ", 0.5);
		
		SmartDashboard.putNumber("Drive Bang Min: ", 0.0);
		SmartDashboard.putNumber("Drive Bang Max: ", 1.0);
		
		SmartDashboard.putNumber("Hanger Wait Time: ",750);
		
		SmartDashboard.putBoolean("Image Use Binary", false);
		
		
	}
}
