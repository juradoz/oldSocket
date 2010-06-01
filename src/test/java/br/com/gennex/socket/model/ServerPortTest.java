package br.com.gennex.socket.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.security.InvalidParameterException;

import org.junit.Before;
import org.junit.Test;

public class ServerPortTest {
	private static final int nullPort = 0;
	private static final int port = 22000;
	private static final int port2 = 22001;

	private ServerPort serverPort;

	@Before
	public void setUp() throws Exception {
		serverPort = new ServerPort(port);
		assertNotNull(serverPort);
	}

	@Test
	public void testEqualsObject() {
		ServerPort serverPort = new ServerPort(port);
		assertEquals(serverPort, this.serverPort);

		serverPort = new ServerPort(port2);
		assertFalse(serverPort.equals(this.serverPort));
	}

	@Test
	public void testGetServerPort() {
		assertEquals(port, serverPort.getServerPort());
	}

	@Test(expected = InvalidParameterException.class)
	public void testServerPort() {
		serverPort = new ServerPort(nullPort);
		fail();
	}

	@Test
	public void testToString() {
		assertEquals(String.valueOf(port), serverPort.toString());
	}

}
