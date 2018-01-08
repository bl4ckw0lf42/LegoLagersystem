package de.hss.lego.lagersystem.roboter;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.sensor.EV3ColorSensor;

public class LagerRoboter extends Roboter {
	
	public static final float BASE_SPEED = 50;
	public static final float FAST_SPEED = BASE_SPEED * 1.25f;
	public static final float SLOW_SPEED = BASE_SPEED * 0.75f;

	protected EV3LargeRegulatedMotor motorMiddle;
	protected ColorSensorAdapter colorFront;
	protected ColorSensorAdapter colorFloor;
	
	public LagerRoboter(EV3LargeRegulatedMotor motorLeft, 
			EV3LargeRegulatedMotor motorRight,
			EV3LargeRegulatedMotor motorMiddle,
			EV3ColorSensor colorFront,
			EV3ColorSensor colorFloor) {
		super(motorLeft, motorRight);
		
		this.motorMiddle = motorMiddle;
		this.colorFloor = new ColorSensorAdapter(colorFloor);
		this.colorFront = new ColorSensorAdapter(colorFront);
	}
	
	public void followLine() {
		do {
			colorFloor.updateSample();
			if (colorFloor.black()){
				setSpeedLeft(FAST_SPEED);
				setSpeedRight(SLOW_SPEED);
			} else {
				setSpeedLeft(SLOW_SPEED);
				setSpeedRight(FAST_SPEED);
			}
		} while (colorFloor.red());
	}
	
	public void lowerCrane() {
		motorMiddle.rotate(150);
	}
	
	public void raiseCrane() {
		motorMiddle.rotate(-150);
	}
	
}
