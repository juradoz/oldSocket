package br.com.gennex.socket.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.security.InvalidParameterException;

import org.junit.Before;
import org.junit.Test;

public class ServerNameTest {

	private static final String nullName = "";
	private static final String name = "TESTE";
	private static final String name2 = "TESTE2";

	private ServerName serverName;

	@Before
	public void setUp() throws Exception {
		serverName = new ServerName(name);
		assertNotNull(serverName);
	}

	@Test
	public void testEqualsObject() {
		ServerName serverName = new ServerName(name);

		assertEquals(serverName, this.serverName);

		serverName = new ServerName(name2);

		assertFalse(serverName.equals(this.serverName));
	}

	@Test
	public void testGetServerName() {
		assertEquals(name, serverName.getServerName());
	}

	@Test(expected = InvalidParameterException.class)
	public void testServerName() {
		serverName = new ServerName(nullName);
		fail();
	}

	@Test
	public void testToString() {
		assertEquals(name, serverName.toString());
	}

}
