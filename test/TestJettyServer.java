import net.jfly.server.ServerFactory;

public class TestJettyServer {
	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		try {
			ServerFactory serverFactory = new ServerFactory();
			serverFactory.getServer().start();

		} catch (Throwable e) {
		}

	}

}
