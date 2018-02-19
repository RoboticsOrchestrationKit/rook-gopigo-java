package run.rook.gopigo.gopigo3.io;

import java.io.IOException;

import run.rook.gopigo.gopigo3.GoPiGo3;
import run.rook.gopigo.gopigo3.GoPiGo3.Servo;

public class ServoOutput implements Output {

	private final GoPiGo3 goPiGo3;
	private final Servo servo;
	
	public ServoOutput(GoPiGo3 goPiGo3, Servo servo) {
		this.goPiGo3 = goPiGo3;
		this.servo = servo;
	}

	@Override
	public void write(long value) throws IOException {
		goPiGo3.setServo(servo, (int)value);
	}

}
