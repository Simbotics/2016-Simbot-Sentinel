package org.simbotics.robot.auton.mode.step1Modes;

import org.simbotics.robot.auton.AutonCommand;
import org.simbotics.robot.auton.drive.DriveAim;
import org.simbotics.robot.auton.drive.DriveWait;
import org.simbotics.robot.auton.mode.AutonBuilder;
import org.simbotics.robot.auton.mode.AutonMode;

public class AimTest implements AutonMode{

	@Override
	public void addToMode(AutonBuilder ab) {
		ab.addCommand(new DriveAim());
		ab.addCommand(new DriveWait());
	}

}
