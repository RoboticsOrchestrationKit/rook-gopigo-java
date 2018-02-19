package run.rook.gopigo.gopigo3.io;

import java.io.IOException;

import run.rook.chi.data.DataType;
import run.rook.gopigo.gopigo3.GoPiGo3;
import run.rook.gopigo.gopigo3.GoPiGo3.GrovePort;
import run.rook.gopigo.gopigo3.GoPiGo3.GroveType;

public class GrovePortInput implements Input {

	private final GoPiGo3 goPiGo3;
	private final GrovePort grovePort;
	
	public GrovePortInput(GoPiGo3 goPiGo3, GrovePort grovePort, GroveType type) throws IOException {
		this.goPiGo3 = goPiGo3;
		this.grovePort = grovePort;
		goPiGo3.setGrovePortType(grovePort, type);
	}

	@Override
	public DataType dataType() {
		return DataType.U32;
	}
	
	@Override
	public long read() throws IOException {
		return goPiGo3.getGrovePortValue(grovePort);
	}
	
}
