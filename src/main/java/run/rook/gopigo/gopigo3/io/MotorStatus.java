package run.rook.gopigo.gopigo3.io;

public class MotorStatus {
	private boolean lowVoltage;
	private boolean overloaded;
	private byte power;
	private int encoder;
	private int degreesPerSecond;

	public boolean isLowVoltage() {
		return lowVoltage;
	}

	public void setLowVoltage(boolean lowVoltage) {
		this.lowVoltage = lowVoltage;
	}

	public boolean isOverloaded() {
		return overloaded;
	}

	public void setOverloaded(boolean overloaded) {
		this.overloaded = overloaded;
	}

	public byte getPower() {
		return power;
	}

	public void setPower(byte power) {
		this.power = power;
	}

	public int getEncoder() {
		return encoder;
	}

	public void setEncoder(int encoder) {
		this.encoder = encoder;
	}

	public int getDegreesPerSecond() {
		return degreesPerSecond;
	}

	public void setDegreesPerSecond(int degreesPerSecond) {
		this.degreesPerSecond = degreesPerSecond;
	}

	@Override
	public String toString() {
		return "MotorStatus [lowVoltage=" + lowVoltage + ", overloaded=" + overloaded + ", power=" + power
				+ ", encoder=" + encoder + ", degreesPerSecond=" + degreesPerSecond + "]";
	}
	
}
