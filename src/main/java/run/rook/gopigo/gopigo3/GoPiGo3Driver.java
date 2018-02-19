package run.rook.gopigo.gopigo3;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.agrona.DirectBuffer;

import run.rook.chi.throttled.OutputType;
import run.rook.chi.throttled.ThrottledDriver;
import run.rook.gopigo.gopigo3.io.Input;
import run.rook.gopigo.gopigo3.io.Output;

public class GoPiGo3Driver extends ThrottledDriver {

	private final Map<String, Input> inputs = new HashMap<>();
	private final Map<String, Output> outputs = new HashMap<>();
	
	public GoPiGo3Driver() {
		super(25);
	}
	
	public void addInput(String name, Input input) {
		addInput(name, input.dataType());
		inputs.put(name, input);
	}
	
	public void addOutput(String name, Output output) {
		addOutput(name, OutputType.LONG);
		outputs.put(name, output);
	}

	@Override
	protected long doReadLong(String name) throws IOException {
		Input input = inputs.get(name);
		if(input == null) {
			return 0;
		}
		return input.read();
	}

	@Override
	protected int doReadBuffer(String name, DirectBuffer buffer) throws IOException {
		return 0;
	}

	@Override
	protected void doWrite(String name, long value) throws IOException {
		Output output = outputs.get(name);
		if(output != null) {
			output.write(value);
		}
	}

	@Override
	protected void doWrite(String name, DirectBuffer value, int length) throws IOException {
		
	}

}
