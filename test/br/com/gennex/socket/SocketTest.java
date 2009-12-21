package br.com.gennex.socket;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import br.com.gennex.interfaces.SocketFactory;
import br.com.gennex.interfaces.TcpRequest;
import br.com.gennex.interfaces.TcpRequestHandler;
import br.com.gennex.interfaces.TcpResponse;
import br.com.gennex.socket.client.ClientSocket;
import br.com.gennex.socket.model.ServerName;
import br.com.gennex.socket.model.ServerPort;
import br.com.gennex.socket.server.ServerSocket;
import br.com.gennex.socket.tcpcommand.TcpCommandSocket;
import br.com.gennex.socket.tcpcommand.messages.requests.FppsRequest;
import br.com.gennex.socket.tcpcommand.messages.requests.FppsRequestCommand;
import br.com.gennex.socket.tcpcommand.messages.responses.FppsResponse;
import br.com.gennex.socket.util.FppsUtil;

public class SocketTest {

	private static final int port = 25000;

	private ClientSocket clientSocket;

	private static ServerSocket serverSocket;

	@BeforeClass
	public static void classSetUp() throws Exception {
		serverSocket = new ServerSocket(new ServerPort(port),
				new SocketFactory() {

					@Override
					public Socket createSocket(java.net.Socket socket) {
						TcpCommandSocket tcpCommandSocket = new TcpCommandSocket(
								socket) {

						};
						tcpCommandSocket.addHandler(new FppsRequestCommand(
								FppsUtil.Command), new TcpRequestHandler() {

							@Override
							public TcpResponse process(Socket socket,
									TcpRequest request) throws Exception {
								for (int i = 0; i < ((FppsRequest) request)
										.getParameters().length; i++)
									assertNotNull(((FppsRequest) request)
											.getParameters()[i]);
								return new FppsResponse(FppsUtil.response);
							}
						});
						return tcpCommandSocket;
					}
				});
	}

	@Before
	public void setUp() {
		clientSocket = new ClientSocket(new ServerName("localhost"),
				new ServerPort(port), new SocketFactory() {

					@Override
					public Socket createSocket(java.net.Socket socket) {
						TcpCommandSocket tcpCommandSocket = new TcpCommandSocket(
								socket) {

						};
						tcpCommandSocket.addHandler(new FppsRequestCommand(
								FppsUtil.response), new TcpRequestHandler() {

							@Override
							public TcpResponse process(Socket socket,
									TcpRequest request) throws Exception {
								for (int i = 0; i < ((FppsRequest) request)
										.getParameters().length; i++)
									assertNotNull(((FppsRequest) request)
											.getParameters()[i]);
								return new FppsResponse(FppsUtil.response);
							}
						});
						return tcpCommandSocket;
					}
				});

	}

	@Test(timeout = 10000)
	public void testConnect() {
		while (true)
			if (clientSocket.isConnected()) {
				assertTrue(true);
				return;
			}
	}

	@Test(timeout = 10000)
	public void testDisconnect() {
		while (true)
			if (clientSocket.isConnected()) {
				try {
					clientSocket.disconnect();
					assertTrue(true);
					return;
				} catch (IOException e) {
					fail(e.getMessage());
				}
			}
	}

	@After
	public void teadDown() {
		try {
			clientSocket.disconnect();
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	@AfterClass
	public static void classTearDown() {
		try {
			serverSocket.close();
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}
}
