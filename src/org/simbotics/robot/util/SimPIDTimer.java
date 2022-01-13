package org.simbotics.robot.util;

public class SimPIDTimer extends SimPID {
	private long startTime =-1;
	private long timeout;
	
	public SimPIDTimer(double p,double i,double d,double eps,double iRange, long timeout) {
		super(p, i, d, eps, iRange);
		this.timeout = timeout;
	}
	
	public SimPIDTimer(double p,double i,double d, double eps, long timeout){
		super(p,i,d,eps);
		this.timeout = timeout;
	}
	
	@Override
	public double calcPID(double current){
		if(this.startTime == -1 || this.getFirstCycle()){
			this.startTime = System.currentTimeMillis();
		}
		return calcPIDError(this.getDesiredVal() - current);
		
	}
	public boolean isDone(long currentTime) {
		if((currentTime - this.startTime) > this.timeout){
			System.out.println("SimPIDTimer TIMEOUT");
		}
		return super.isDone() || ((currentTime - this.startTime) > this.timeout);
	}
		
	public void setTimeOut(long timeout){
		this.timeout = timeout;
	}
}
