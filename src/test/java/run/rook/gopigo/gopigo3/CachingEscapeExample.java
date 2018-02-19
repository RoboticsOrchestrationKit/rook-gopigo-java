package run.rook.gopigo.gopigo3;

import java.io.IOException;

import run.rook.chi.client.Client;
import run.rook.chi.client.JsonWebsocketClient;
import run.rook.chi.client.ValueCache;

public class CachingEscapeExample {

	// number of back-to-back readings under threshold required to make it trip
	private static final int INITIAL_LATCH_COUNT = 3;

	public static void main(String... args) throws Exception {
		String url = args.length == 0 ? "ws://localhost:16182" : args[0];

		ValueCache cache = new ValueCache();
		Client client = new JsonWebsocketClient(url, cache);
		client.start();

		// stop the motors when the JVM exits
		createMotorStopHook(client);

		// subscribe to distance (alternatively, you may use null to subscribe to everything)
		client.subscribe("DISTANCE");

		// loop, checking the distance
		int latchCount = INITIAL_LATCH_COUNT;
		while (true) {
			Thread.sleep(100);
			long value = cache.getNumber("DISTANCE", 1);
			System.out.println("distance: " + value);
			if (value > 1 && value < 100) {
				latchCount--;
			} else {
				latchCount = INITIAL_LATCH_COUNT;
			}
			if (latchCount == 0) {
				System.out.println("ESCAPE INITIATED!");
				client.write("BOTH_MOTORS", -100);
				Thread.sleep(1000);
				client.write("LEFT_MOTOR", 100);
				Thread.sleep(1000);
				client.write("BOTH_MOTORS", 0);
				latchCount = INITIAL_LATCH_COUNT;
			}
		}
	}

	private static void createMotorStopHook(Client client) {
		// stop the motors when JVM exits
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				client.write("BOTH_MOTORS", 0);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}));
	}

}
