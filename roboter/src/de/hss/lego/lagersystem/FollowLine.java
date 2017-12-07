package de.hss.lego.lagersystem;

import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.utility.Delay;

public class FollowLine {
	
	public static final int speed = 50;
	public static final int fast = 100;
	public static final int slow = 40;
	
	EV3LargeRegulatedMotor ml = new EV3LargeRegulatedMotor(MotorPort.A);
	EV3LargeRegulatedMotor mr = new EV3LargeRegulatedMotor(MotorPort.C);
	
	//Touchsensor 
	EV3ColorSensor cs = new EV3ColorSensor(SensorPort.S3);
	EV3ColorSensor vs = new EV3ColorSensor(SensorPort.S2);
	EV3TouchSensor ts = new EV3TouchSensor(SensorPort.S1);
	EV3LargeRegulatedMotor mt = new EV3LargeRegulatedMotor(MotorPort.B);
	//Sample

	float [] colorsensorarray;
	float [] touchsensorarray;
	float [] vsarray;
	//Threads
	Thread runThread;
	Thread stopThread;


	public static void main(String[] args) {
		FollowLine follow = new FollowLine();
		follow.init();
		

	}
	
	public void init(){
		 
		ml.setSpeed(fast);
		mr.setSpeed(slow);
		ml.forward();
		mr.forward();
		cs.setCurrentMode("RGB");
		vs.setCurrentMode("Ambient");
		
		runThread = new Thread(new Runnable() {
			turn180left tl = new turn180left();
			turn180right tr = new turn180right();
			@Override
			public void run() {
				while(true){
					colorsensorarray = new float [cs.sampleSize()];
					cs.fetchSample(colorsensorarray, 0);
					vsarray = new float [vs.sampleSize()];
					vs.fetchSample(vsarray, 0);
				
					//black
					if(colorsensorarray[0] <= 0.1f && colorsensorarray[1] <= 0.1f && colorsensorarray[2] <= 0.1f){
						ml.setSpeed(fast);
						mr.setSpeed(slow);
						LCD.drawString("Black", 0, 0);
					}//white
					if(colorsensorarray[0] >= 0.15f && colorsensorarray[1] >= 0.15f && colorsensorarray[2] >= 0.15f){
						mr.setSpeed(fast);
						ml.setSpeed(slow);
						LCD.drawString("White", 0, 0);
					}

				}
			}
		});
		
		runThread.start();
		
		stopThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
					touchsensorarray = new float [ts.sampleSize()];
					ts.fetchSample(touchsensorarray, 0);
					if(touchsensorarray[0] != 0.0f){
						System.exit(1);
					}
				}
		
			}
		});
		stopThread.start();
	}

}