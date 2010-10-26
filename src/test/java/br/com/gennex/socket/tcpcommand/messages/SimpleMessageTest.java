package br.com.gennex.socket.tcpcommand.messages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class SimpleMessageTest {
	private static final String STRING_SIMPLES = "COMANDO";
	private static final String STRING_COMPOSTA = "COMANDO PARAMETRO";
	private static final String STRING_COMPOSTA_GRANDE = "COMANDO PARAMETRO1 PARAMETRO2 PARAMETRO3 PARAMETRO4 PARAMETRO5";
	private static final String COMANDO_STRING_COMPOSTA = "COMANDO";
	private SimpleMessage simpleMessage;

	@Before
	public void setUp() {
		simpleMessage = new SimpleMessage(STRING_SIMPLES);
	}

	@Test
	public void getCommandDeveriaRetornarStringSimples() {
		assertEquals(STRING_SIMPLES, simpleMessage.getCommand());
	}

	@Test(expected = IllegalArgumentException.class)
	public void getCommandDeveriaLancarExceptionSeMsgNull() {
		simpleMessage.getCommand(null);
	}

	@Test
	public void getComandoDeveriaRetornarComandoStringComposta() {
		simpleMessage = new SimpleMessage(STRING_COMPOSTA);
		assertEquals(COMANDO_STRING_COMPOSTA, simpleMessage.getCommand());
	}

	@Test
	public void getComandoDeveriaRetornarComandoStringCompostaGrande() {
		simpleMessage = new SimpleMessage(STRING_COMPOSTA_GRANDE);
		assertEquals(COMANDO_STRING_COMPOSTA, simpleMessage.getCommand());
	}

	@Test
	public void getParametrosDeveriaRetornarParametroStringComposta() {
		simpleMessage = new SimpleMessage(STRING_COMPOSTA);
		assertEquals(STRING_COMPOSTA.split(" ")[1],
				simpleMessage.getParameters()[0]);
		assertEquals(1, simpleMessage.getParameters().length);
	}

	@Test
	public void getParametrosDeveriaRetornarParametrosStringCompostaGrande() {
		simpleMessage = new SimpleMessage(STRING_COMPOSTA_GRANDE);
		String[] split = STRING_COMPOSTA_GRANDE.split(" ");
		assertEquals(split.length - 1, simpleMessage.getParameters().length);
		assertTrue(Arrays.asList(Arrays.copyOfRange(split, 1, split.length))
				.containsAll(Arrays.asList(simpleMessage.getParameters())));
		assertTrue(Arrays.asList(simpleMessage.getParameters()).containsAll(
				Arrays.asList(Arrays.copyOfRange(split, 1, split.length))));
	}
}
