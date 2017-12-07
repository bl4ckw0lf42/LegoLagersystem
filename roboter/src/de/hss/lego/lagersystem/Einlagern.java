package de.hss.lego.lagersystem;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.utility.Delay;

public class Einlagern {

	public static final int speed = 50;
	public static final int fast = 120;
	public static final int slow = 40;
	public String color = "";
	public int driven = 0;
	public boolean beladen = false;
	public int slot;
	public int counter = 0;
	public final static double wheelradius = 0.028;
	public final static double wheelwidth = 0.028;
	public final static double wheeleCircum = 2.0 * Math.PI * wheelradius;
	public final static double wheeldistance = 0.116;
	public final static double pathCircum = 2.0 * Math.PI * wheeldistance;
	
	
	EV3LargeRegulatedMotor ml = new EV3LargeRegulatedMotor(MotorPort.A); //motor Links
	EV3LargeRegulatedMotor mr = new EV3LargeRegulatedMotor(MotorPort.C); // motor Recht
	EV3LargeRegulatedMotor mm = new EV3LargeRegulatedMotor(MotorPort.B); // Kran
	EV3TouchSensor ts = new EV3TouchSensor(SensorPort.S1);
	EV3ColorSensor cs = new EV3ColorSensor(SensorPort.S3);
	EV3ColorSensor vs = new EV3ColorSensor(SensorPort.S2);
	turn180right tr = new turn180right();
	turn180left tl = new turn180left();
	//Sample
	float [] colorsensorarray;
	float [] touchsensorarray;
	float [] vsarray;
	//Threads
	static Thread runThread;
	Thread stopThread;
	public static void main(String[] args) {
		final Einlagern el = new Einlagern();
		
		runThread = new Thread(new Runnable() {
			@Override
			public void run(){
				try {
					el.init(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		runThread.start();

	}
	
	@SuppressWarnings("deprecation")
	public  void init(int slot) throws InterruptedException{
		
		cs.setCurrentMode("RGB");
		vs.setCurrentMode("Ambient");
		ml.setSpeed(fast);
		mr.setSpeed(slow);
		ml.forward();
		mr.forward();
		mm.rotate(1);
		while(true){
			colorsensorarray = new float [cs.sampleSize()];
			cs.fetchSample(colorsensorarray, 0);
			vsarray = new float [vs.sampleSize()];
			vs.fetchSample(vsarray, 0);
		
			/*if(counter == slot){
				ml.forward();
				mr.forward();
				ml.setSpeed(fast);
				mr.setSpeed(slow);
			}*/
			//black
			if(colorsensorarray[0] <= 0.1f && colorsensorarray[1] <= 0.1f && colorsensorarray[2] <= 0.1f){
				ml.setSpeed(fast);
				mr.setSpeed(slow);
			
			}else 
			//white
			if(colorsensorarray[0] >= 0.15f && colorsensorarray[1] >= 0.15f && colorsensorarray[2] >= 0.15f){
				mr.setSpeed(fast);
				ml.setSpeed(slow);
				
			}
			//red // turn 90 Grad um in Lager 
			if(colorsensorarray[0] >= 0.1f && colorsensorarray[1] <= 0.1f && colorsensorarray[2] <= 0.1f){
				counter++;
				
				if(beladen){
					ml.setSpeed(0);
					mr.setSpeed(0);
					mm.rotate(150);
					Remote.sendCommand(WebTest.SA, "/fetched", "0");
					driven = 0;
					mm.resetTachoCount();
					turn();
					mr.setSpeed(fast);
					ml.setSpeed(slow);
					ml.forward();
					mr.forward();
				}
				if(!beladen){
					beladen = true;
					mm.rotate(-180);
					mr.stop();
					ml.stop();
					turn();
					mr.setSpeed(fast);
					ml.setSpeed(slow);
					ml.forward();
					mr.forward();
					}
				}
				
			// Object in Front
			/*if(vsarray[0] <= 0.05f){
				vs.setCurrentMode("RGB");
				//read color of object
				vsarray = new float [vs.sampleSize()];
				vs.fetchSample(vsarray, 0);
				color = vsarray.toString();	
				
				vs.setCurrentMode("Ambient");
				vsarray = new float [vs.sampleSize()];
				vs.fetchSample(vsarray, 0);
				LCD.drawString(color, 0, 0);
				if(!beladen){
					beladen = true;
					mm.rotate(-180);
					
					mr.resetTachoCount();
					if(mm.getTachoCount() < 100){
						while(driven <= 150){
							LCD.drawInt(driven, 0, 0);
							ml.rotate(driven);
							mr.rotate(-driven);
							driven=driven +40;
						};
						mr.setSpeed(fast);
						ml.setSpeed(slow);
						ml.forward();
						mr.forward();	
					}
				}else{
					driven = 0;

				}

			}else{
				mr.setSpeed(fast);
				ml.setSpeed(slow);
				ml.forward();
				mr.forward();
				
			}*/

		}
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
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
		
		

		