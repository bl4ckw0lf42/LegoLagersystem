package de.hss.lego.lagersystem.roboter;

import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class Roboter {
	

	public final static float WHEEL_RADIUS = 0.028f;
	public final static float WHEEL_WIDTH = 0.028f;
	public final static double WHEEL_CIRCUMFENCE = 2.0f * Math.PI * WHEEL_RADIUS;
	public final static float WHEEL_DISTANCE = 0.116f;
	public final static double PATH_CIRCUMFENCE = 2.0f * Math.PI * WHEEL_DISTANCE;

	protected EV3LargeRegulatedMotor motorRight;
	protected EV3LargeRegulatedMotor motorLeft;
	
	public Roboter(EV3LargeRegulatedMotor motorLeft, EV3LargeRegulatedMotor motorRight) {
		this.motorLeft = motorLeft;
		this.motorRight = motorRight;
	}
	
	public void turn (int degrees){
		double part = ((float)degrees)/360.0f;
		int rotateangle =(int)(360*((part * PATH_CIRCUMFENCE) / WHEEL_CIRCUMFENCE));
		
		motorLeft.rotate(rotateangle / 2, true);
		motorRight.rotate(-rotateangle / 2, false);

	}
	
	public void setSpeed(float speedBoth) {
		motorLeft.setSpeed(speedBoth);
		motorRight.setSpeed(speedBoth);
	}
	
	public void setSpeed(float speedLeft, float speedRight) {
		motorLeft.setSpeed(speedLeft);
		motorRight.setSpeed(speedRight);
	}
	
	public void setSpeedRight(float speed) {
		motorRight.setSpeed(speed);
	}
	
	public float getSpeedRight() {
		return motorRight.getSpeed();
	}
	
	public void setSpeedLeft(float speed) {
		motorLeft.setSpeed(speed);
	}
	
	public float getSpeedLeft() {
		return motorLeft.getSpeed();
	}
	
	public void forward() {
		motorLeft.forward();
		motorRight.forward();
	}
	
	public void backward() {
		motorLeft.backward();
		motorRight.backward();
	}
	
	public void driveDistance(float m) {
		int degrees = (int)((m/WHEEL_CIRCUMFENCE)*360.0f);
		rotateBoth(degrees);
	}
	
	public void stop() {
		motorLeft.stop();
		motorRight.stop();
	}
	
	protected void rotateBoth(int angle) {
		motorLeft.rotate(angle, true);
		motorRight.rotate(angle);
	}
	
}
