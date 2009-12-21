package br.com.gennex.socket.model;

import java.security.InvalidParameterException;

import junit.framework.TestCase;

import org.junit.Test;

import br.com.gennex.socket.model.ServerName;

public class ServerNameTest extends TestCase {

	private final String nullName = "";
	private final String name = "TESTE";
	private final String name2 = "TESTE2";

	private ServerName serverName;

	@Override
	protected void setUp() throws Exception {
		serverName = new ServerName(name);
	}

	@Test
	public void testToString() {
		assertEquals(name, serverName.toString());
	}

	@Test
	public void testGetServerName() {
		assertEquals(name, serverName.getServerName());
	}

	@Test
	public void testServerName() {
		assertNotNull(serverName);
		try {
			serverName = new ServerName(nullName);
		} catch (Exception e) {
			assertTrue(e instanceof InvalidParameterException);
		}
	}

	@Test
	public void testEqualsObject() {
		ServerName serverName = new ServerName(name);

		assertEquals(serverName, this.serverName);

		serverName = new ServerName(name2);

		assertFalse(serverName.equals(this.serverName));
	}

}
