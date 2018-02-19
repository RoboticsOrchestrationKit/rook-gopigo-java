package run.rook.gopigo.gopigo3;

import org.agrona.DirectBuffer;

import run.rook.chi.ValueListener;
import run.rook.chi.client.Client;
import run.rook.chi.client.JsonWebsocketClient;
import run.rook.chi.data.DataType;

public class ReadSensors {

	public static void main(String... args) throws Exception {
		String url = args[0];
		
		ValueListener listener = new ValueListener() {
			@Override
			public void handle(String name, DataType dataType, DirectBuffer value, int length) {
				
			}
			@Override
			public void handle(String name, DataType dataType, long value) {
				System.out.println(name + ": " + value);
			}
		};
		
		Client client = new JsonWebsocketClient(url, listener);
		client.start();
	
		// subscribe for all sensor callbacks
		client.subscribe(null);
	}
	
}
