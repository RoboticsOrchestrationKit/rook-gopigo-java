package run.rook.gopigo.gopigo3.io;

import java.io.IOException;

import run.rook.chi.data.DataType;

public interface Input {
	DataType dataType();
	long read() throws IOException;
}
