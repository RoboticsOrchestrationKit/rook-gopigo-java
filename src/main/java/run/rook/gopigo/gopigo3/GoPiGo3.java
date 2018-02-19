package run.rook.gopigo.gopigo3;

import java.io.IOException;

import com.pi4j.io.spi.SpiDevice;

import run.rook.gopigo.gopigo3.io.MotorStatus;

public class GoPiGo3 {

	public static final byte DEFAULT_ADDRESS = 8;
	public static final byte MOTOR_FLOAT = -128;
	
	private static final String MANUFACTURER = "Dexter Industries";
	private static final String BOARD_NAME = "GoPiGo3";
	
	private static final byte GET_MANUFACTURER = 1;
	private static final byte GET_NAME = 2;
	private static final byte GET_HARDWARE_VERSION = 3;
	private static final byte GET_FIRMWARE_VERSION = 4;
	private static final byte GET_ID = 5;
	private static final byte SET_LED = 6;
	private static final byte GET_VOLTAGE_5V = 7;
	private static final byte GET_VOLTAGE_VCC = 8;
	private static final byte SET_SERVO = 9;
	private static final byte SET_MOTOR_PWM = 10;
	private static final byte SET_MOTOR_POSITION = 11;
	private static final byte SET_MOTOR_POSITION_KP = 12;
	private static final byte SET_MOTOR_POSITION_KD = 13;
	private static final byte SET_MOTOR_DPS = 14;
	private static final byte SET_MOTOR_LIMITS = 15;
	private static final byte OFFSET_MOTOR_ENCODER = 16;
	private static final byte GET_MOTOR_ENCODER_LEFT = 17;
	private static final byte GET_MOTOR_ENCODER_RIGHT = 18;
	private static final byte GET_MOTOR_STATUS_LEFT = 19;
	private static final byte GET_MOTOR_STATUS_RIGHT = 20;
	private static final byte SET_GROVE_TYPE = 21;
	private static final byte SET_GROVE_MODE = 22;
	private static final byte SET_GROVE_STATE = 23;
	private static final byte SET_GROVE_PWM_DUTY = 24;
	private static final byte SET_GROVE_PWM_FREQUENCY = 25;
	private static final byte GET_GROVE_VALUE_1 = 26;
	private static final byte GET_GROVE_VALUE_2 = 27;
	private static final byte GET_GROVE_STATE_1_1 = 28;
	private static final byte GET_GROVE_STATE_1_2 = 29;
	private static final byte GET_GROVE_STATE_2_1 = 30;
	private static final byte GET_GROVE_STATE_2_2 = 31;
	private static final byte GET_GROVE_VOLTAGE_1_1 = 32;
	private static final byte GET_GROVE_VOLTAGE_1_2 = 33;
	private static final byte GET_GROVE_VOLTAGE_2_1 = 34;
	private static final byte GET_GROVE_VOLTAGE_2_2 = 35;
	private static final byte GET_GROVE_ANALOG_1_1 = 36;
	private static final byte GET_GROVE_ANALOG_1_2 = 37;
	private static final byte GET_GROVE_ANALOG_2_1 = 38;
	private static final byte GET_GROVE_ANALOG_2_2 = 39;
	private static final byte START_GROVE_I2C_1 = 40;
	private static final byte START_GROVE_I2C_2 = 41;

	public enum Motor {
		LEFT(0x01), 
		RIGHT(0x02),
		BOTH(0x01|0x02);
		private final byte value;
		private Motor(int value) {
			this.value = (byte) value;
		}
		public byte getValue() {
			return value;
		}
	}
	
	public enum GrovePin {
		GROVE_1_1(0x01, GrovePort.GROVE_1), 
		GROVE_1_2(0x02, GrovePort.GROVE_1), 
		GROVE_2_1(0x04, GrovePort.GROVE_2), 
		GROVE_2_2(0x08, GrovePort.GROVE_2);
		private final byte value;
		private final GrovePort port;
		private GrovePin(int value, GrovePort port) {
			this.value = (byte) value;
			this.port = port;
		}
		public byte getValue() {
			return value;
		}
		public GrovePort getPort() {
			return port;
		}
	}
	
	public enum GrovePort {
		GROVE_1(0x01 | 0x02), 
		GROVE_2(0x04 | 0x08);
		private final byte value;
		private GrovePort(int value) {
			this.value = (byte) value;
		}
		public byte getValue() {
			return value;
		}
	}
	
	public enum GrovePinState {
		LOW(0), 
		HIGH(1);
		private final byte value;
		private GrovePinState(int value) {
			this.value = (byte) value;
		}
		public byte getValue() {
			return value;
		}
	}
	
	public enum GroveType {
		CUSTOM(1), 
		IR_DI_REMOTE(2), 
		IR_EV3_REMOTE(3), 
		ULTRA_SONIC(4), 
		I2C(5);
		private final byte value;
		private GroveType(int value) {
			this.value = (byte) value;
		}
		public byte getValue() {
			return value;
		}
	}
	
	public enum GroveMode {
		GROVE_INPUT_DIGITAL(0),
		GROVE_OUTPUT_DIGITAL(1),
		GROVE_INPUT_DIGITAL_PULLUP(2),
		GROVE_INPUT_DIGITAL_PULLDOWN(3),
		GROVE_INPUT_ANALOG(4),
		GROVE_OUTPUT_PWM(5),
		GROVE_INPUT_ANALOG_PULLUP(6),
		GROVE_INPUT_ANALOG_PULLDOWN(7);
	    private final byte value;
	    private GroveMode(int value) {
	    	this.value = (byte)value;
	    }
	    public byte getValue() {
			return value;
		}
	}

	public enum GroveState {
        VALID_DATA(0),
        NOT_CONFIGURED(1),
        CONFIGURING(2),
        NO_DATA(3),
        I2C_ERROR(4);
	    private final byte value;
	    private GroveState(int value) {
	    	this.value = (byte)value;
	    }
	    public byte getValue() {
			return value;
		}
	}
	
	public enum Led {
		RIGHT_EYE(0x01),
		LEFT_EYE(0x02),
		BLINKER_LEFT(0x04),
		BLINKER_RIGHT(0x08);
		private final byte value;
		private Led(int value) {
			this.value = (byte) value;
		}
		public byte getValue() {
			return value;
		}
	}
	
	public enum Servo {
		SERVO_1(0x01),
		SERVO_2(0x02);
		private final byte value;
		private Servo(int value) {
			this.value = (byte) value;
		}
		public byte getValue() {
			return value;
		}
	}

	private final SpiDevice spi;
	private final byte addr;
	
	public GoPiGo3(SpiDevice spi) throws IOException {
		this(spi, DEFAULT_ADDRESS);
	}
	
	public GoPiGo3(SpiDevice spi, byte addr) throws IOException {
		this.spi = spi;
		this.addr = addr;
		String manufacturer = getManufacturer();
		if(!MANUFACTURER.equals(manufacturer)) {
			throw new IOException("Device reported unexpected manufacturer: " + manufacturer);
		}
		String boardName = getBoardName();
		if(!BOARD_NAME.equals(boardName)) {
			throw new IOException("Device reported unexpected board name: " + boardName);
		}
	}
	
//	private int spiRead8(byte messageType) throws IOException {
//		byte command[] = new byte[] { addr, messageType, 0, 0, 0 };
//		byte[] reply = spi.write(command);
//        if((reply[3] & 0xFF) == 0xA5) {
//            return 0xFF & reply[4];
//        }
//        throw new IOException("No SPI response");
//	}
	
	private int spiRead16(byte messageType) throws IOException {
		byte command[] = new byte[] { addr, messageType, 0, 0, 0, 0 };
		byte[] reply = spi.write(command);
        if((reply[3] & 0xFF) == 0xA5) {
            return ((0xFF & reply[4]) << 8) | (0xFF & reply[5]);
        }
        throw new IOException("No SPI response");
	}
	
	private int spiRead32(byte messageType) throws IOException {
		byte command[] = new byte[] { addr, messageType, 0, 0, 0, 0, 0, 0 };
		byte[] reply = spi.write(command);
        if((reply[3] & 0xFF) == 0xA5) {
            return ((0xFF & reply[4]) << 24) | ((0xFF & reply[5]) << 16) | ((0xFF & reply[6]) << 8) | (0xFF & reply[7]);
        }
        throw new IOException("No SPI response");
	}
	
	public String getManufacturer() throws IOException {
		byte command[] = new byte[] { addr, GET_MANUFACTURER,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		byte[] reply = spi.write(command);
		StringBuilder name = new StringBuilder();
		if((reply[3] & 0xFF) == 0xA5) {
            for(int i = 4; i < 24; i++) {
                if(reply[i] != 0) {
                	name.append((char)reply[i]);
                }
            }
            return name.toString();
		}
		throw new IOException("No SPI response");
	}
	
	public String getBoardName() throws IOException {
		byte command[] = new byte[] { addr, GET_NAME,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		byte[] reply = spi.write(command);
		StringBuilder name = new StringBuilder();
		if((reply[3] & 0xFF) == 0xA5) {
            for(int i = 4; i < 24; i++) {
                if(reply[i] != 0) {
                	name.append((char)reply[i]);
                }
            }
            return name.toString();
		}
		throw new IOException("No SPI response");
	}
	
	public String getHardwareVersion() throws IOException {
		return Double.toString(spiRead32(GET_HARDWARE_VERSION) / 1000000.0);
	}
	
	public String getFirmwareVersion() throws IOException {
		int version = spiRead32(GET_FIRMWARE_VERSION);
		return (version / 1000000) + "." + ((version / 1000) % 1000) + "." + (version % 1000);
	}
	
	public void setLed(Led led, int red, int green, int blue) throws IOException {
		byte command[] = new byte[] { addr, SET_LED, led.getValue(), (byte)red, (byte)green, (byte)blue };
		spi.write(command);
	}
	
	public double getVoltage5V() throws IOException {
		return spiRead16(GET_VOLTAGE_5V) / 1000.0;
	}
	
	public double getVoltageBattery() throws IOException {
		return getVoltageBatteryRaw() / 1000.0;
	}
	
	public long getVoltageBatteryRaw() throws IOException {
		return spiRead16(GET_VOLTAGE_VCC);
	}
	
	public void setServo(Servo servo, int us) throws IOException {
		byte command[] = new byte[] { addr, SET_SERVO, servo.getValue(), (byte)((us >> 8) & 0xFF), (byte)(us & 0xFF) };
		spi.write(command);
	}
	
	public void setMotorPower(Motor motor, byte power) throws IOException {
		byte command[] = new byte[] { addr, SET_MOTOR_PWM, motor.getValue(), power };
		spi.write(command);
	}
	
	public int getMotorEncoderRaw(Motor motor) throws IOException {
		byte type;
		if(motor == Motor.LEFT) {
			type = GET_MOTOR_ENCODER_LEFT;
		} else if(motor == Motor.RIGHT) {
			type = GET_MOTOR_ENCODER_RIGHT;
		} else {
			throw new IllegalArgumentException("Invalid motor: " + motor);
		}
		return spiRead32(type);
	}
	
	public void setGrovePortType(GrovePort port, GroveType type) throws IOException {
		byte command[] = new byte[] { addr, SET_GROVE_TYPE, port.getValue(), type.getValue() };
		spi.write(command);
	}
	
	public void setGrovePortMode(GrovePort port, GroveMode type) throws IOException {
		byte command[] = new byte[] { addr, SET_GROVE_MODE, port.getValue(), type.getValue() };
		spi.write(command);
	}
	
	public void setGrovePinMode(GrovePin pin, GroveMode type) throws IOException {
		byte command[] = new byte[] { addr, SET_GROVE_MODE, pin.getValue(), type.getValue() };
		spi.write(command);
	}
	
	public void setGrovePinState(GrovePin pin, GrovePinState state) throws IOException {
		byte command[] = new byte[] { addr, SET_GROVE_STATE, pin.getValue(), state.getValue() };
		spi.write(command);
	}
	
	public void setGrovePinPwmDuty(GrovePin pin, byte duty) throws IOException {
		byte command[] = new byte[] { addr, SET_GROVE_PWM_DUTY, pin.getValue(), duty };
		spi.write(command);
	}
	
	public void setGrovePinPwmFrequency(GrovePin pin, int freq) throws IOException {
		if(freq < 3) {
            freq = 3;
		}
        if(freq > 48000) {
        	freq = 48000;
        }
		byte command[] = new byte[] { addr, SET_GROVE_PWM_FREQUENCY, pin.getValue(), (byte)((freq >> 8) & 0xFF), (byte)(freq & 0xFF) };
		spi.write(command);
	}
	
	public int getGrovePortValue(GrovePort port) throws IOException {
		byte type;
		if(port == GrovePort.GROVE_1) { 
            type = GET_GROVE_VALUE_1;
		} else if(port == GrovePort.GROVE_2) {
            type = GET_GROVE_VALUE_2;
		} else {
			throw new IllegalArgumentException("Invalid port: " + port);
		}
		byte command[] = new byte[] { addr, type, 0, 0, 0, 0, 0, 0 };
        byte[] reply = spi.write(command);
        if((0xFF & reply[3]) == 0xA5) {
            if(reply[5] == 0) {
                return (((reply[6] << 8) & 0xFF00) | (reply[7] & 0xFF));
            } else {
                throw new IOException("Invalid value received");
            }
        } else {
        	throw new IOException("No SPI response");
        }
	}
	
	public GrovePinState getGrovePinState(GrovePin pin) throws IOException {
		byte type;
		if(pin == GrovePin.GROVE_1_1) {
			type = GET_GROVE_STATE_1_1;
		} else if(pin == GrovePin.GROVE_1_2) {
        	type = GET_GROVE_STATE_1_2;
		} else if(pin == GrovePin.GROVE_2_1) {
        	type = GET_GROVE_STATE_2_1;
		} else if(pin == GrovePin.GROVE_2_2) {
        	type = GET_GROVE_STATE_2_2;
		} else {
        	throw new IllegalArgumentException("Invalid pin: " + pin);
        }

            
		byte command[] = new byte[] { addr, type, 0, 0, 0, 0 };
        byte[] reply = spi.write(command);
        if((0xFF & reply[3]) == 0xA5) {
            if(reply[4] == GroveState.VALID_DATA.getValue()) {
                int val = (0xFF & reply[5]);
                if(val == GrovePinState.LOW.getValue()) {
                	return GrovePinState.LOW;
                } else if(val == GrovePinState.HIGH.getValue()) {
                	return GrovePinState.HIGH;
                } else {
                	throw new IOException("Invalid value received");
                }
            } else {
                throw new IOException("Invalid value received");
            }
        } else {
        	throw new IOException("No SPI response");
        }
	}
	
	public double getGrovePinVoltage(GrovePin pin) throws IOException {
		return getGrovePinVoltageRaw(pin) / 1000.0;
	}
	
	public long getGrovePinVoltageRaw(GrovePin pin) throws IOException {
		byte type;
		if(pin == GrovePin.GROVE_1_1) {
			type = GET_GROVE_VOLTAGE_1_1;
		} else if(pin == GrovePin.GROVE_1_2) {
        	type = GET_GROVE_VOLTAGE_1_2;
		} else if(pin == GrovePin.GROVE_2_1) {
        	type = GET_GROVE_VOLTAGE_2_1;
		} else if(pin == GrovePin.GROVE_2_2) {
        	type = GET_GROVE_VOLTAGE_2_2;
		} else {
        	throw new IllegalArgumentException("Invalid pin: " + pin);
        }

		byte command[] = new byte[] { addr, type, 0, 0, 0, 0, 0};
        byte[] reply = spi.write(command);
        if((0xFF & reply[3]) == 0xA5) {
            if(reply[4] == GroveState.VALID_DATA.getValue()) {
                return (((reply[5] << 8) & 0xFF00) | (reply[6] & 0xFF));
            } else {
                throw new IOException("Invalid value received");
            }
        } else {
        	throw new IOException("No SPI response");
        }
	}
	
	public int getGrovePinAnalog(GrovePin pin) throws IOException {
		byte type;
		if(pin == GrovePin.GROVE_1_1) {
			type = GET_GROVE_ANALOG_1_1;
		} else if(pin == GrovePin.GROVE_1_2) {
        	type = GET_GROVE_ANALOG_1_2;
		} else if(pin == GrovePin.GROVE_2_1) {
        	type = GET_GROVE_ANALOG_2_1;
		} else if(pin == GrovePin.GROVE_2_2) {
        	type = GET_GROVE_ANALOG_2_2;
		} else {
        	throw new IllegalArgumentException("Invalid pin: " + pin);
        }

            
		byte command[] = new byte[] { addr, type, 0, 0, 0, 0, 0};
        byte[] reply = spi.write(command);
        if((0xFF & reply[3]) == 0xA5) {
            if(reply[4] == GroveState.VALID_DATA.getValue()) {
                return (((reply[5] << 8) & 0xFF00) | (reply[6] & 0xFF));
            } else {
                throw new IOException("Invalid value received");
            }
        } else {
        	throw new IOException("No SPI response");
        }
	}
	
	public MotorStatus getMotorStatus(Motor motor) throws IOException {
		byte type;
		if(motor == Motor.LEFT) {
            type = GET_MOTOR_STATUS_LEFT;
		} else if(motor == Motor.RIGHT) {
			type = GET_MOTOR_STATUS_RIGHT;
		} else {
            throw new IllegalArgumentException("Invalid Motor: " + motor.toString());
		}
		
		byte[] command = new byte[] {addr, type, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		byte[] reply = spi.write(command);
		MotorStatus status = new MotorStatus();
		if(reply[3] == (byte)0xA5) {
			status.setLowVoltage((reply[4] & 0b00000001) > 0);
			status.setOverloaded((reply[4] & 0b00000010) > 0);
			status.setPower(reply[5]);
	        status.setEncoder(((reply[6] & 0xFF) << 24) | ((reply[7] & 0xFF) << 16) | ((reply[8] & 0xFF) << 8) | (reply[9] & 0xFF));
			status.setDegreesPerSecond(((reply[10] & 0xFF) << 8) | (reply[11] & 0xFF));
			return status;
		} else {
			throw new IOException("No SPI response");
		}
	}
}
