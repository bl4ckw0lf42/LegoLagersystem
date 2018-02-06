package de.hss.lego.lagersystem;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;

public class turn180left {
	
	
	public final static double wheelradius = 0.028;
	public final static double wheelwidth = 0.028;
	public final static double wheeleCircum = 2.0 * Math.PI * wheelradius;
	public final static double wheeldistance = 0.116;
	public final static double pathCircum = 2.0 * Math.PI * wheeldistance;
	


	public static void main(String[] args) {
		turn180left tr = new turn180left();
		tr.turn(200);
	}
	
	public void turn(int angle){
		EV3LargeRegulatedMotor ml = new EV3LargeRegulatedMotor(MotorPort.A);
		EV3LargeRegulatedMotor mr = new EV3LargeRegulatedMotor(MotorPort.C);
		ml.resetTachoCount();
		while(ml.getTachoCount() <= angle){
			ml.setSpeed(100);
			mr.setSpeed(100);
			ml.forward();
			mr.backward();
		}
		ml.close();
		mr.close();		
	}

}
