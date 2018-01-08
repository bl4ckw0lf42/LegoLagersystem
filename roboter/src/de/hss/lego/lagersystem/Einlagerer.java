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

	public static final int speed = 50;
	public static final int fast = 120;
	public static final int slow = 40;
	
	private LagerRoboter lagerRoboter;
	
	EV3LargeRegulatedMotor ml = new EV3LargeRegulatedMotor(MotorPort.A); //motor Links
	EV3LargeRegulatedMotor mr = new EV3LargeRegulatedMotor(MotorPort.C); // motor Recht
	EV3LargeRegulatedMotor mm = new EV3LargeRegulatedMotor(MotorPort.B); // Kran
	EV3ColorSensor cs = new EV3ColorSensor(SensorPort.S3);
	EV3ColorSensor vs = new EV3ColorSensor(SensorPort.S2);
	
	public Einlagerer() {
		this.lagerRoboter = new LagerRoboter(ml, mr, mm, cs, vs);
	}
	
	public void store(int slot) throws InterruptedException{
		
		lagerRoboter.followLine();
		lagerRoboter.stop();
		lagerRoboter.lowerCrane();
		lagerRoboter.driveDistance(-0.05f);
		lagerRoboter.turn(180);
		lagerRoboter.forward();
		
		int slotsToSkip = slot;
		
		while(slotsToSkip > 0) {
			lagerRoboter.followLine(); // line folgen bis rot
			lagerRoboter.driveDistance(0.02f); // über rote Linie fahren
			slotsToSkip--;
		}
		
		lagerRoboter.followLine();
		lagerRoboter.stop();
		lagerRoboter.raiseCrane();
		lagerRoboter.driveDistance(-0.05f);
		lagerRoboter.turn(180);
	}
	
	public void returnToStart() {
		lagerRoboter.followLine();
		lagerRoboter.turn(180);
		lagerRoboter.followLine();
	}
	
	public void turn (){
		double viertel = 0.45;
		int rotateangle =(int)(360*((viertel * pathCircum) / wheeleCircum));
		ml.setSpeed(100);
		mr.setSpeed(100);
		ml.rotate(-200, true);
		mr.rotate(-200, false);
		
		ml.rotate(rotateangle / 2, true);
		mr.rotate(-rotateangle / 2, false);

	}
}
		
		

		