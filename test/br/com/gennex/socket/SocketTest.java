package br.com.gennex.socket;

import org.junit.BeforeClass;

import br.com.gennex.interfaces.SocketFactory;
import br.com.gennex.socket.model.ServerPort;
import br.com.gennex.socket.server.ServerSocket;
import br.com.gennex.socket.tcpcommand.TcpCommandSocket;

public class SocketTest {

	private static final int port = 25000;

	@BeforeClass
	public void setUp() throws Exception {
		ServerSocket serverSocket = new ServerSocket(new ServerPort(port),
				new SocketFactory() {

					@Override
					public Socket createSocket(java.net.Socket socket) {
						TcpCommandSocket tcpCommandSocket = new TcpCommandSocket(
								socket) {

						};
						tcpCommandSocket.addHandler(request, requestHandler);

						return tcpCommandSocket;
					}

				});
	}
}
