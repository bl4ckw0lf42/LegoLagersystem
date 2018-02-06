package de.hss.lego.lagersystem;

import de.hss.lego.lagersystem.roboter.ColorSensorAdapter;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;

public class Calibration {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
			EV3ColorSensor cs = new EV3ColorSensor(SensorPort.S3);
			
			ColorSensorAdapter ad = new ColorSensorAdapter(cs);
			
			while(true){
				ad.updateSample();
				float[] s = ad.getSample();
				LCD.drawString("r: " + s[0] ,0, 0);
				LCD.drawString("g: " + s[1] ,0, 1);
				LCD.drawString("b: " + s[2] ,0, 2);
			}
	}

}
