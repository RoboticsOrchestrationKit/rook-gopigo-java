package run.rook.gopigo.gopigo3.io;

import java.io.IOException;

import run.rook.chi.data.DataType;
import run.rook.gopigo.gopigo3.GoPiGo3;
import run.rook.gopigo.gopigo3.GoPiGo3.GroveMode;
import run.rook.gopigo.gopigo3.GoPiGo3.GrovePin;
import run.rook.gopigo.gopigo3.GoPiGo3.GrovePinState;
import run.rook.gopigo.gopigo3.GoPiGo3.GroveType;

public class GrovePinDigitalInput implements Input {

	private final GoPiGo3 goPiGo3;
	private final GrovePin grovePin;
	
	public GrovePinDigitalInput(GoPiGo3 goPiGo3, GrovePin grovePin, GroveMode mode) throws IOException {
		this.goPiGo3 = goPiGo3;
		this.grovePin = grovePin;
		goPiGo3.setGrovePortType(grovePin.getPort(), GroveType.CUSTOM);
		goPiGo3.setGrovePinMode(grovePin, mode);
	}
	
	@Override
	public DataType dataType() {
		return DataType.U8;
	}

	@Override
	public long read() throws IOException {
		return goPiGo3.getGrovePinState(grovePin) == GrovePinState.HIGH ? 1 : 0;
	}
	
}
