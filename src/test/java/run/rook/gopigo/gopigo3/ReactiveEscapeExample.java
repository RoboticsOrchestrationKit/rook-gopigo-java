package run.rook.gopigo.gopigo3;

import java.io.IOException;

import org.agrona.DirectBuffer;

import run.rook.chi.ValueListener;
import run.rook.chi.client.ThreadedValueListener;
import run.rook.chi.client.Client;
import run.rook.chi.client.JsonWebsocketClient;
import run.rook.chi.data.DataType;

public class ReactiveEscapeExample {

	// number of back-to-back readings under threshold required to make it trip
	private static final int INITIAL_LATCH_COUNT = 3;
	private static Client client;

	public static void main(String... args) throws Exception {
		// Note: callbacks and value handlers may be too advances for many
		// beginners. You may want to start with the CachingEscapeExample
		// instead.

		String url = args.length == 0 ? "ws://localhost:16182" : args[0];

		ValueListener myListener = new ValueListener() {
			int latchCount = INITIAL_LATCH_COUNT;

			@Override
			public void handle(String name, DataType dataType, DirectBuffer value, int length) {

			}

			@Override
			public void handle(String name, DataType dataType, long value) {
				if ("DISTANCE".equals(name)) {
					System.out.println("distance=" + value);
					// note "1" means "no value" for US sensor
					if (value > 1 && value < 100) {
						latchCount--;
					} else {
						latchCount = INITIAL_LATCH_COUNT;
					}
					if (latchCount == 0) {
						System.out.println("ESCAPE INITIATED!");
						escape();
						latchCount = INITIAL_LATCH_COUNT;
					}
				}
			}
		};

		// Note on new ThreadedValueListener(myListener):
		// An option utility you can use that wraps the user's ValueListener. It
		// consumes from the client as fast as possible, and will always
		// callback the latest value received for a given input. This
		// effectively allows the user to write "reactive" code, which can
		// execute control logic directly in the callback/listener, without
		// worrying about the effects of back-pressure or queuing.
		client = new JsonWebsocketClient(url, new ThreadedValueListener(myListener));
		client.start();

		// stop the motors when JVM exits
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				client.write("BOTH_MOTORS", 0);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}));

		// subscribe for all sensor callbacks
		client.subscribe(null);
	}

	private static void escape() {
		try {
			System.out.println("Back!");
			client.write("BOTH_MOTORS", -100);
			Thread.sleep(1000);
			System.out.println("Turn!");
			client.write("LEFT_MOTOR", 100);
			System.out.println("Stop!");
			Thread.sleep(1000);
			client.write("BOTH_MOTORS", 0);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}
