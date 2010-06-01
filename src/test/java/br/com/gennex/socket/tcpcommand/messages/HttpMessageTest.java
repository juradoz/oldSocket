package br.com.gennex.socket.tcpcommand.messages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import br.com.gennex.socket.tcpcommand.messages.requests.HttpRequestCommand;
import br.com.gennex.socket.util.HttpUtil;

public class HttpMessageTest {
	private HttpMessage message;
	private HttpMessage message1;
	private HttpMessage message2;

	@Test
	public void testEqualsObject() {
		message1 = new HttpRequestCommand(HttpUtil.ReqSemParams);
		message2 = new HttpRequestCommand(HttpUtil.ReqSemParams);
		assertEquals(message1, message2);
		message2 = new HttpRequestCommand(HttpUtil.Command2);
		assertFalse(message1.equals(message2));

		message1 = new HttpRequestCommand(HttpUtil.ReqSem1Param);
		message2 = new HttpRequestCommand(HttpUtil.ReqSem1Param);
		assertEquals(message1, message2);
		message2 = new HttpRequestCommand(HttpUtil.Command2);
		assertFalse(message1.equals(message2));

		message1 = new HttpRequestCommand(HttpUtil.ReqSem2Param);
		message2 = new HttpRequestCommand(HttpUtil.ReqSem2Param);
		assertEquals(message1, message2);
		message2 = new HttpRequestCommand(HttpUtil.Command2);
		assertFalse(message1.equals(message2));

		message1 = new HttpRequestCommand(HttpUtil.ReqSem3Param);
		message2 = new HttpRequestCommand(HttpUtil.ReqSem3Param);
		assertEquals(message1, message2);
		message2 = new HttpRequestCommand(HttpUtil.Command2);
		assertFalse(message1.equals(message2));

		message1 = new HttpRequestCommand(HttpUtil.ReqSem3UltimoVazio);
		message2 = new HttpRequestCommand(HttpUtil.ReqSem3UltimoVazio);
		assertEquals(message1, message2);
		message2 = new HttpRequestCommand(HttpUtil.Command2);
		assertFalse(message1.equals(message2));

		message1 = new HttpRequestCommand(HttpUtil.ReqSem5Vazio);
		message2 = new HttpRequestCommand(HttpUtil.ReqSem5Vazio);
		assertEquals(message1, message2);
		message2 = new HttpRequestCommand(HttpUtil.Command2);
		assertFalse(message1.equals(message2));
	}

	@Test
	public void testGetCommand() {
		message = new HttpRequestCommand(HttpUtil.ReqSemParams);
		assertEquals(HttpUtil.Command, message.getCommand());

		message = new HttpRequestCommand(HttpUtil.ReqSem1Param);
		assertEquals(HttpUtil.Command, message.getCommand());

		message = new HttpRequestCommand(HttpUtil.ReqSem2Param);
		assertEquals(HttpUtil.Command, message.getCommand());

		message = new HttpRequestCommand(HttpUtil.ReqSem3Param);
		assertEquals(HttpUtil.Command, message.getCommand());

		message = new HttpRequestCommand(HttpUtil.ReqSem3UltimoVazio);
		assertEquals(HttpUtil.Command, message.getCommand());

		message = new HttpRequestCommand(HttpUtil.ReqSem5Vazio);
		assertEquals(HttpUtil.Command, message.getCommand());
	}

	@Test
	public void testGetParameters() {
		message = new HttpRequestCommand(HttpUtil.ReqSemParams);
		assertEquals(0, message.getParameters().length);

		message = new HttpRequestCommand(HttpUtil.ReqSem1Param);
		for (int i = 0; i < HttpUtil.Param1
				.split(HttpUtil.DelimitadorParametros).length; i++)
			assertEquals(
					HttpUtil.Param1.split(HttpUtil.DelimitadorParametros)[i],
					message.getParameters()[i]);

		message = new HttpRequestCommand(HttpUtil.ReqSem2Param);
		for (int i = 0; i < HttpUtil.Param2
				.split(HttpUtil.DelimitadorParametros).length; i++)
			assertEquals(
					HttpUtil.Param2.split(HttpUtil.DelimitadorParametros)[i],
					message.getParameters()[i]);

		message = new HttpRequestCommand(HttpUtil.ReqSem3Param);
		for (int i = 0; i < HttpUtil.Param3
				.split(HttpUtil.DelimitadorParametros).length; i++)
			assertEquals(
					HttpUtil.Param3.split(HttpUtil.DelimitadorParametros)[i],
					message.getParameters()[i]);

		message = new HttpRequestCommand(HttpUtil.ReqSem3UltimoVazio);
		for (int i = 0; i < HttpUtil.Param3UltimoVazio
				.split(HttpUtil.DelimitadorParametros).length; i++)
			assertEquals(HttpUtil.Param3UltimoVazio
					.split(HttpUtil.DelimitadorParametros)[i], message
					.getParameters()[i]);

		message = new HttpRequestCommand(HttpUtil.ReqSem5Vazio);
		for (int i = 0; i < HttpUtil.Param5Vazio
				.split(HttpUtil.DelimitadorParametros).length; i++)
			assertEquals(HttpUtil.Param5Vazio
					.split(HttpUtil.DelimitadorParametros)[i], message
					.getParameters()[i]);
	}

	@Test
	public void testGetParametersString() {
		message = new HttpRequestCommand(HttpUtil.ReqSemParams);
		assertEquals(0, message.getParameters().length);
		message = new HttpRequestCommand(HttpUtil.ReqSem1Param);
		assertEquals(1, message.getParameters().length);
		message = new HttpRequestCommand(HttpUtil.ReqSem2Param);
		assertEquals(2, message.getParameters().length);
		message = new HttpRequestCommand(HttpUtil.ReqSem3Param);
		assertEquals(3, message.getParameters().length);
		message = new HttpRequestCommand(HttpUtil.ReqSem3UltimoVazio);
		assertEquals(3, message.getParameters().length);
		message = new HttpRequestCommand(HttpUtil.ReqSem5Vazio);
		assertEquals(5, message.getParameters().length);
	}

	@Test
	public void testGetTcpMessage() {
		message = new HttpRequestCommand(HttpUtil.ReqSemParams);
		assertEquals(HttpUtil.ReqSemParams, message.getTcpMessage());
		message = new HttpRequestCommand(HttpUtil.ReqSem1Param);
		assertEquals(HttpUtil.ReqSem1Param, message.getTcpMessage());
		message = new HttpRequestCommand(HttpUtil.ReqSem2Param);
		assertEquals(HttpUtil.ReqSem2Param, message.getTcpMessage());
		message = new HttpRequestCommand(HttpUtil.ReqSem3Param);
		assertEquals(HttpUtil.ReqSem3Param, message.getTcpMessage());
		message = new HttpRequestCommand(HttpUtil.ReqSem3UltimoVazio);
		assertEquals(HttpUtil.ReqSem3UltimoVazio, message.getTcpMessage());
		message = new HttpRequestCommand(HttpUtil.ReqSem5Vazio);
		assertEquals(HttpUtil.ReqSem5Vazio, message.getTcpMessage());
	}

	@Test
	public void testToString() {
		message = new HttpRequestCommand(HttpUtil.ReqSemParams);
		assertEquals(HttpUtil.ReqSemParams, message.toString());
		message = new HttpRequestCommand(HttpUtil.ReqSem1Param);
		assertEquals(HttpUtil.ReqSem1Param, message.toString());
		message = new HttpRequestCommand(HttpUtil.ReqSem2Param);
		assertEquals(HttpUtil.ReqSem2Param, message.toString());
		message = new HttpRequestCommand(HttpUtil.ReqSem3Param);
		assertEquals(HttpUtil.ReqSem3Param, message.toString());
		message = new HttpRequestCommand(HttpUtil.ReqSem3UltimoVazio);
		assertEquals(HttpUtil.ReqSem3UltimoVazio, message.toString());
		message = new HttpRequestCommand(HttpUtil.ReqSem5Vazio);
		assertEquals(HttpUtil.ReqSem5Vazio, message.toString());
	}
}
