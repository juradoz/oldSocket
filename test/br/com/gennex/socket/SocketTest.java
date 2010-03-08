package br.com.gennex.socket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import br.com.gennex.interfaces.SocketFactory;
import br.com.gennex.interfaces.TcpRequest;
import br.com.gennex.interfaces.TcpRequestHandler;
import br.com.gennex.interfaces.TcpResponse;
import br.com.gennex.socket.Socket.TcpMessageFilter;
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
import br.com.gennex.socket.util.Util;

public class SocketTest {

	private static class AcceptFilter implements TcpMessageFilter {
		@Override
		public boolean accept(String message) {
			return true;
		}
	}

	private class MockClientSocket extends TcpCommandSocket {

		private boolean recebido = false;

		public MockClientSocket(Socket socket) {
			super(socket);
			addHandler(new FppsRequestCommand(Util.response),
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
							return new FppsResponse(Util.response);
						}
					});
		}

		public boolean isRecebido() {
			return recebido;
		}

		public void setRecebido(boolean recebido) {
			this.recebido = recebido;
		}

	}

	private static class RejectFilter implements TcpMessageFilter {
		@Override
		public boolean accept(String message) {
			return false;
		}
	}

	private static final int port = 25000;

	@BeforeClass
	public static void classSetUp() {
		serverSocket = new ServerSocket(new ServerPort(port),
				new SocketFactory() {

					@Override
					public br.com.gennex.socket.Socket createSocket(
							Socket socket) {
						return new MockServerSocket(socket);
					}
				});

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

	private ClientSocket clientSocket;

	private static ServerSocket serverSocket;

	private void espera() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			fail(e1.getMessage());
		}
	}

	@Test
	public void logIfAccept() {
		Logger.getLogger(clientSocket.getSocket().getClass()).setLevel(
				Level.INFO);
		assertTrue(clientSocket.getSocket().canLog(new AcceptFilter(), "TESTE"));
	}

	@Test
	public void logIfNull() {
		Logger.getLogger(clientSocket.getSocket().getClass()).setLevel(
				Level.INFO);
		assertTrue(clientSocket.getSocket().canLog(null, "TESTE"));
	}

	@Test
	public void logIfReject() {
		Logger.getLogger(clientSocket.getSocket().getClass()).setLevel(
				Level.INFO);
		assertFalse(clientSocket.getSocket()
				.canLog(new RejectFilter(), "TESTE"));
	}

	@Test
	public void logIfRejectDebug() {
		Logger.getLogger(clientSocket.getSocket().getClass()).setLevel(
				Level.DEBUG);
		try {
			assertTrue(clientSocket.getSocket().canLog(new RejectFilter(),
					"TESTE"));
		} finally {
			Logger.getLogger(clientSocket.getSocket().getClass()).setLevel(
					Level.INFO);
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
		espera();
	}

	@After
	public void teardDown() {
		try {
			clientSocket.disconnect();
			assertFalse(clientSocket.isConnected());
			clientSocket.cancel();
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	@Test(timeout = 10000)
	public void testConnect() {
		if (clientSocket.isConnected()) {
			assertNotNull(clientSocket.getSocket());
			assertTrue(serverSocket.getTotalConnections() == 1);
			assertTrue(clientSocket.isConnected());
			assertTrue(clientSocket.getSocket().isConnected());
			try {
				assertNotNull(clientSocket.getSocket().getSocketOutputStream());
			} catch (IOException e) {
				fail(e.getMessage());
			}
		}
	}

	@Test(timeout = 10000)
	public void testDisconnect() {
		if (clientSocket.isConnected()) {
			assertTrue("Total: " + serverSocket.getTotalConnections(),
					serverSocket.getTotalConnections() == 1);
			try {
				clientSocket.disconnect();
				assertNull(clientSocket.getSocket());
				assertFalse(clientSocket.isConnected());
				espera();
				assertTrue("Total: " + serverSocket.getTotalConnections(),
						serverSocket.getTotalConnections() == 0);
				return;
			} catch (IOException e) {
				fail(e.getMessage());
			}
		}
	}

	@Test
	public void testInetAddress() {
		assertNotNull(clientSocket.getSocket().getInetAddress());
	}

	@Test
	public void testReconnect() {
		classTearDown();

		espera();

		assertFalse(clientSocket.isConnected());
		assertNull(clientSocket.getSocket());

		classSetUp();

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			fail(e.getMessage());
		}

		assertNotNull(clientSocket.getSocket());
		assertTrue(serverSocket.getTotalConnections() == 1);
		assertTrue(clientSocket.isConnected());
		assertTrue(clientSocket.getSocket().isConnected());
		try {
			assertNotNull(clientSocket.getSocket().getSocketOutputStream());
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	@Test(timeout = 10000)
	public void testRequest() {
		FppsRequest request = new FppsRequestCommand(FppsUtil.Command);
		clientSocket.getSocket().send(request);
		espera();
		assertTrue(((MockClientSocket) clientSocket.getSocket()).isRecebido());
		Iterator<br.com.gennex.socket.Socket> it = serverSocket
				.getSocketsAtivos().iterator();
		br.com.gennex.socket.Socket socket = it.next();
		assertNotNull(socket);
		assertTrue(((MockServerSocket) socket).isRecebido());
	}
}
