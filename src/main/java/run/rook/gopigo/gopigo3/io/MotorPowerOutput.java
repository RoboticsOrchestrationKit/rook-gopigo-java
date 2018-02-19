package run.rook.gopigo.gopigo3.io;

import java.io.IOException;

import run.rook.gopigo.gopigo3.GoPiGo3;
import run.rook.gopigo.gopigo3.GoPiGo3.Motor;

public class MotorPowerOutput implements Output {

	private final GoPiGo3 goPiGo3;
	private final Motor motor;
	
	public MotorPowerOutput(GoPiGo3 goPiGo3, Motor motor) throws IOException {
		this.goPiGo3 = goPiGo3;
		this.motor = motor;
	}

	@Override
	public void write(long value) throws IOException {
		goPiGo3.setMotorPower(motor, (byte)value);
	}

}
