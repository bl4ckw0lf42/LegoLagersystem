package de.hss.lego.lagersystem;
import de.hss.lego.lagersystem.roboter.LagerRoboter;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.utility.Delay;

public class Einlagerer {

	
	private LagerRoboter lagerRoboter;
	
	EV3LargeRegulatedMotor ml = new EV3LargeRegulatedMotor(MotorPort.A); //motor Links
	EV3LargeRegulatedMotor mr = new EV3LargeRegulatedMotor(MotorPort.C); // motor Recht
	EV3LargeRegulatedMotor mm = new EV3LargeRegulatedMotor(MotorPort.B); // Kran
	EV3ColorSensor cs = new EV3ColorSensor(SensorPort.S2);
	EV3ColorSensor vs = new EV3ColorSensor(SensorPort.S3);
	
	public Einlagerer() {
		this.lagerRoboter = new LagerRoboter(ml, mr, mm, cs, vs);
	}
	
	public void store(int slot) throws InterruptedException{
		lagerRoboter.followLineTilRed();
		
		int slotsToSkip = slot;
		LCD.drawString("Skip: " + slotsToSkip, 0, 0);
		while(slotsToSkip > 0) {
			lagerRoboter.stop();
			lagerRoboter.setSpeed(LagerRoboter.BASE_SPEED);
			lagerRoboter.driveDistance(0.05f);// über rote Linie fahren
			lagerRoboter.followLineTilRed(); // line folgen bis rot
			slotsToSkip--;
			LCD.drawString("Skip: " + slotsToSkip, 0, 5);
		}
		lagerRoboter.stop();
		lagerRoboter.setSpeed(LagerRoboter.BASE_SPEED);
		lagerRoboter.turn(-90);
		lagerRoboter.followLineTilBlue();
		lagerRoboter.stop();
		lagerRoboter.raiseCrane(); // Kran heben 
		lagerRoboter.driveDistance(-0.05f);
		lagerRoboter.turn(180);
	}
	
	public void returnToStart() {
		lagerRoboter.followLineTilRed();
		lagerRoboter.stop();
		lagerRoboter.setSpeed(LagerRoboter.BASE_SPEED);
		lagerRoboter.turn(180);
		lagerRoboter.followLineTilRed();
		lagerRoboter.stop();
		lagerRoboter.setSpeed(LagerRoboter.BASE_SPEED);
		lagerRoboter.driveDistance(0.1f);
		lagerRoboter.turn(90);
		lagerRoboter.driveDistance(0.1f);
		lagerRoboter.stop();
	}
	
	public float[] fetch(){
		
		lagerRoboter.setSpeed(LagerRoboter.BASE_SPEED);
		lagerRoboter.forward();
		lagerRoboter.followLineTilRed();
		lagerRoboter.stop();
		float[] color = lagerRoboter.getFrontColor();
		lagerRoboter.lowerCrane();
		lagerRoboter.setSpeed(LagerRoboter.BASE_SPEED);
		lagerRoboter.driveDistance(-0.05f);
		lagerRoboter.turn(180);
		return color;
	}
	
}
		
		

		