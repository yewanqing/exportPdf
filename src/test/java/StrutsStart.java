import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.webapp.WebAppContext;

public class StrutsStart {

	public static void main(String[] args) throws Exception {
		Server jettyServer = new Server();
		SocketConnector conn = new SocketConnector();
		conn.setPort(9080);
		jettyServer.setConnectors(new Connector[] { conn });
		WebAppContext wah = new WebAppContext();
		wah.setContextPath("/export");
		wah.setWar("src/main/webapp");
		jettyServer.setHandler(wah);
		jettyServer.setGracefulShutdown(5000);
		jettyServer.start();
	}

}
