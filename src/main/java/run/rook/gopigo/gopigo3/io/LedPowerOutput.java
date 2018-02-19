package run.rook.gopigo.gopigo3.io;

import java.io.IOException;

import run.rook.gopigo.gopigo3.GoPiGo3;
import run.rook.gopigo.gopigo3.GoPiGo3.Led;

public class LedPowerOutput implements Output {

	private final GoPiGo3 goPiGo3;
	private final Led led;
	private final int red;
	private final int green;
	private final int blue;
	
	public LedPowerOutput(GoPiGo3 goPiGo3, Led led, int red, int green, int blue) {
		this.goPiGo3 = goPiGo3;
		this.led = led;
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	@Override
	public void write(long value) throws IOException {
		if(value == 0) {
			goPiGo3.setLed(led, 0, 0, 0);
		} else {
			goPiGo3.setLed(led, red, green, blue);
		}
	}

}
