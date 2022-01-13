package org.simbotics.robot.auton.feeder;

import org.simbotics.robot.auton.AutonCommand;
import org.simbotics.robot.auton.RobotComponent;
import org.simbotics.robot.io.RobotOutput;

public class FeederSetSpeed extends AutonCommand{

    private RobotOutput robotOut;
    
    private double speed;
    
    public FeederSetSpeed(double speed) {
        super(RobotComponent.FEEDER);
        this.speed = speed;
        this.robotOut = RobotOutput.getInstance();
    }

    @Override
    public boolean calculate() {
        robotOut.setFeeder(this.speed);
        return true;
    }

    @Override
    public void override() {
        robotOut.setFeeder(0);
        
    }

}
