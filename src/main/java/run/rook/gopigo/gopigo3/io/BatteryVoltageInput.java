package run.rook.gopigo.gopigo3.io;

import java.io.IOException;

import run.rook.chi.data.DataType;
import run.rook.gopigo.gopigo3.GoPiGo3;

public class BatteryVoltageInput implements Input {

	private final GoPiGo3 goPiGo3;
	
	public BatteryVoltageInput(GoPiGo3 goPiGo3) throws IOException {
		this.goPiGo3 = goPiGo3;
	}

	@Override
	public DataType dataType() {
		return DataType.U32;
	}
	
	@Override
	public long read() throws IOException {
		return goPiGo3.getVoltageBatteryRaw();
	}
	
}
