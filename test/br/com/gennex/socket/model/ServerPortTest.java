package br.com.gennex.socket.model;

import java.security.InvalidParameterException;

import junit.framework.TestCase;

import org.junit.Test;

import br.com.gennex.socket.model.ServerPort;

public class ServerPortTest extends TestCase {
	private static final int nullPort = 0;
	private static final int port = 22000;
	private static final int port2 = 22001;

	private ServerPort serverPort;

	@Test
	public void testServerPort() {
		assertNotNull(serverPort);
		try {
			serverPort = new ServerPort(nullPort);
		} catch (Exception e) {
			assertTrue(e instanceof InvalidParameterException);
		}
	}

	@Test
	public void testGetServerPort() {
		assertEquals(port, serverPort.getServerPort());
	}

	@Test
	public void testToString() {
		assertEquals(String.valueOf(port), serverPort.toString());
	}

	@Override
	protected void setUp() throws Exception {
		serverPort = new ServerPort(port);
	}

	@Test
	public void testEqualsObject() {
		ServerPort serverPort = new ServerPort(port);
		assertEquals(serverPort, this.serverPort);

		serverPort = new ServerPort(port2);
		assertFalse(serverPort.equals(this.serverPort));
	}

}
