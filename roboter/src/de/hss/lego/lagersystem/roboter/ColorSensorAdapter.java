package de.hss.lego.lagersystem.roboter;

import lejos.hardware.sensor.EV3ColorSensor;

public class ColorSensorAdapter {

	protected EV3ColorSensor sensor;
	
	private float[] sample;
	
	public ColorSensorAdapter(EV3ColorSensor sensor) {
		this.sensor = sensor;
		sensor.setCurrentMode("RGB");
		sample = new float[sensor.sampleSize()];
	}
	
	public boolean black() {
		return (sample[0] < 0.1 &&
				sample[1] < 0.1 &&
				sample[2] < 0.1);
	}
	
	public boolean red() {
		return (sample[0] >= 0.1 &&
				sample[1] < 0.1 &&
				sample[2] < 0.1);
	}
	
	public void updateSample() {
		sensor.fetchSample(sample, 0);
	}
}
