package br.com.gennex.socket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.Socket;

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
import br.com.gennex.socket.util.MockServerSocket;

public class SocketTest {

	private static final int port = 25000;

	private ClientSocket clientSocket;

	private static ServerSocket serverSocket;

	@BeforeClass
	public static void classSetUp() throws Exception {
		serverSocket = new ServerSocket(new ServerPort(port),
				new SocketFactory() {

					@Override
					public br.com.gennex.socket.Socket createSocket(
							Socket socket) {
						return new MockServerSocket(socket);
					}
				});

	}

	private class MockClientSocket extends TcpCommandSocket {

		public MockClientSocket(Socket socket) {
			super(socket);
			addHandler(new FppsRequestCommand(FppsUtil.response),
					new TcpRequestHandler() {

						@Override
						public TcpResponse process(
								br.com.gennex.socket.Socket socket,
								TcpRequest request) throws Exception {
							for (int i = 0; i < ((FppsRequest) request)
									.getParameters().length; i++)
								assertNotNull(((FppsRequest) request)
										.getParameters()[i]);
							setRecebido(true);
							return new FppsResponse(FppsUtil.response);
						}
					});
		}

		private boolean recebido = false;

		public boolean isRecebido() {
			return recebido;
		}

		public void setRecebido(boolean recebido) {
			this.recebido = recebido;
		}

	}

	@Before
	public void setUp() {
		clientSocket = new ClientSocket(new ServerName("localhost"),
				new ServerPort(port), new SocketFactory() {

					@Override
					public br.com.gennex.socket.Socket createSocket(
							Socket socket) {
						return new MockClientSocket(socket);
					}
				});

	}

	@Test(timeout = 10000)
	public void testConnect() {
		while (true) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				fail(e1.getMessage());
			}
			if (clientSocket.isConnected()) {
				assertTrue("Total: " + serverSocket.getTotalConnections(),
						serverSocket.getTotalConnections() == 1);
				assertTrue(true);
				return;
			}
		}
	}

	@Test(timeout = 10000)
	public void testDisconnect() {
		while (true) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				fail(e1.getMessage());
			}
			if (clientSocket.isConnected()) {
				assertTrue("Total: " + serverSocket.getTotalConnections(),
						serverSocket.getTotalConnections() == 1);
				try {
					clientSocket.disconnect();
					try {
						Thread.sleep(500);
					} catch (InterruptedException e1) {
						fail(e1.getMessage());
					}
					assertTrue("Total: " + serverSocket.getTotalConnections(),
							serverSocket.getTotalConnections() == 0);
					return;
				} catch (IOException e) {
					fail(e.getMessage());
				}
			}
		}
	}

	@After
	public void teardDown() {
		try {
			clientSocket.disconnect();
			assertFalse(clientSocket.isConnected());
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	@AfterClass
	public static void classTearDown() {
		try {
			serverSocket.close();
			assertEquals(0, serverSocket.getTotalConnections());
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	@Test(timeout = 10000)
	public void testRequest() {
		FppsRequest request = new FppsRequestCommand(FppsUtil.Command);
		while (!clientSocket.isConnected())
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				fail(e.getMessage());
			}
		clientSocket.getSocket().send(request);
		while (!((MockClientSocket) clientSocket.getSocket()).isRecebido()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				fail(e.getMessage());
			}
		}
		assertTrue(true);
	}
}
