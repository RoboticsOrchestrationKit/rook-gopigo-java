package run.rook.gopigo.gopigo3;

import java.io.IOException;

import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;

import run.rook.chi.CommonHardwareInterface;
import run.rook.gopigo.gopigo3.GoPiGo3.GroveMode;
import run.rook.gopigo.gopigo3.GoPiGo3.GrovePin;
import run.rook.gopigo.gopigo3.GoPiGo3.GrovePort;
import run.rook.gopigo.gopigo3.GoPiGo3.GroveType;
import run.rook.gopigo.gopigo3.GoPiGo3.Led;
import run.rook.gopigo.gopigo3.GoPiGo3.Motor;
import run.rook.gopigo.gopigo3.GoPiGo3.Servo;
import run.rook.gopigo.gopigo3.io.BatteryVoltageInput;
import run.rook.gopigo.gopigo3.io.GrovePinAnalogInput;
import run.rook.gopigo.gopigo3.io.GrovePinDigitalInput;
import run.rook.gopigo.gopigo3.io.GrovePortInput;
import run.rook.gopigo.gopigo3.io.Input;
import run.rook.gopigo.gopigo3.io.LedBrightnessOutput;
import run.rook.gopigo.gopigo3.io.LedPowerOutput;
import run.rook.gopigo.gopigo3.io.MotorPowerOutput;
import run.rook.gopigo.gopigo3.io.Output;
import run.rook.gopigo.gopigo3.io.ServoOutput;

public class GPG3CHI {

	public static void main(String... args) throws Exception {
		GoPiGo3 goPiGo3 = new GoPiGo3(SpiFactory.getInstance(
                SpiChannel.CS1,
                500000,
                SpiDevice.DEFAULT_SPI_MODE));
		GoPiGo3Driver driver = new GoPiGo3Driver();
		for(String arg : args) {
			int splitIdx = arg.indexOf('=');
			String name = arg.substring(0, splitIdx);
			String cfg = arg.substring(splitIdx+1);
			Object o = create(goPiGo3, cfg);
			if(o instanceof Input) {
				driver.addInput(name, (Input)o);
			} else if(o instanceof Output) {
				driver.addOutput(name, (Output)o);
			}
		}
		
		Thread.sleep(1000);
		driver.init();
		
		CommonHardwareInterface chi = new CommonHardwareInterface(driver);
		chi.start();
	}
	
	private static Object create(GoPiGo3 goPiGo3, String cfg) throws IOException {
		int splitIdx = cfg.indexOf(':');
		String type = splitIdx == -1 ? cfg : cfg.substring(0, splitIdx);
		String params = splitIdx == -1 ? null : cfg.substring(splitIdx+1);
		switch(type) {
		case "GrovePinAnalogInput":
			return createGrovePinAnalogInput(goPiGo3, params);
		case "GrovePinDigitalInput":
			return createGrovePinDigitalInput(goPiGo3, params);
		case "GrovePortInput":
			return createGrovePortInput(goPiGo3, params);
		case "LedBrightnessOutput":
			return createLedBrightnessOutput(goPiGo3, params);
		case "LedPowerOutput":
			return createLedPowerOutput(goPiGo3, params);
		case "MotorPowerOutput":
			return new MotorPowerOutput(goPiGo3, Motor.valueOf(params));
		case "ServoOutput":
			return new ServoOutput(goPiGo3, Servo.valueOf(params));
		case "BatteryVoltageInput":
			return new BatteryVoltageInput(goPiGo3);
		default:
			throw new IllegalArgumentException("Unrecognized type: " + type);
		}
	}

	private static GrovePinAnalogInput createGrovePinAnalogInput(GoPiGo3 goPiGo3, String params) throws IOException {
		String[] arr = params.split(",");
		return new GrovePinAnalogInput(goPiGo3, GrovePin.valueOf(arr[0]), GroveMode.valueOf(arr[1]));
	}

	private static GrovePinDigitalInput createGrovePinDigitalInput(GoPiGo3 goPiGo3, String params) throws IOException {
		String[] arr = params.split(",");
		return new GrovePinDigitalInput(goPiGo3, GrovePin.valueOf(arr[0]), GroveMode.valueOf(arr[1]));
	}

	private static GrovePortInput createGrovePortInput(GoPiGo3 goPiGo3, String params) throws IOException {
		String[] arr = params.split(",");
		return new GrovePortInput(goPiGo3, GrovePort.valueOf(arr[0]), GroveType.valueOf(arr[1]));
	}

	private static LedBrightnessOutput createLedBrightnessOutput(GoPiGo3 goPiGo3, String params) {
		String[] arr = params.split(",");
		return new LedBrightnessOutput(goPiGo3, Led.valueOf(arr[0]), Integer.parseInt(arr[1]), Integer.parseInt(arr[2]), Integer.parseInt(arr[3]));
	}

	private static LedPowerOutput createLedPowerOutput(GoPiGo3 goPiGo3, String params) {
		String[] arr = params.split(",");
		return new LedPowerOutput(goPiGo3, Led.valueOf(arr[0]), Integer.parseInt(arr[1]), Integer.parseInt(arr[2]), Integer.parseInt(arr[3]));
	}

}
