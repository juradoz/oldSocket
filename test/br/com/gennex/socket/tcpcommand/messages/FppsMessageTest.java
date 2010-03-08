package br.com.gennex.socket.tcpcommand.messages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import br.com.gennex.socket.tcpcommand.messages.requests.FppsRequestCommand;
import br.com.gennex.socket.util.FppsUtil;

public class FppsMessageTest {
	private FppsMessage message;
	private FppsMessage message1;
	private FppsMessage message2;

	@Test
	public void testEqualsObject() {
		message1 = new FppsRequestCommand(FppsUtil.ReqSemParams);
		message2 = new FppsRequestCommand(FppsUtil.ReqSemParams);
		assertEquals(message1, message2);
		message2 = new FppsRequestCommand(FppsUtil.Command2);
		assertFalse(message1.equals(message2));

		message1 = new FppsRequestCommand(FppsUtil.ReqSem1Param);
		message2 = new FppsRequestCommand(FppsUtil.ReqSem1Param);
		assertEquals(message1, message2);
		message2 = new FppsRequestCommand(FppsUtil.Command2);
		assertFalse(message1.equals(message2));

		message1 = new FppsRequestCommand(FppsUtil.ReqSem2Param);
		message2 = new FppsRequestCommand(FppsUtil.ReqSem2Param);
		assertEquals(message1, message2);
		message2 = new FppsRequestCommand(FppsUtil.Command2);
		assertFalse(message1.equals(message2));

		message1 = new FppsRequestCommand(FppsUtil.ReqSem3Param);
		message2 = new FppsRequestCommand(FppsUtil.ReqSem3Param);
		assertEquals(message1, message2);
		message2 = new FppsRequestCommand(FppsUtil.Command2);
		assertFalse(message1.equals(message2));

		message1 = new FppsRequestCommand(FppsUtil.ReqSem3UltimoVazio);
		message2 = new FppsRequestCommand(FppsUtil.ReqSem3UltimoVazio);
		assertEquals(message1, message2);
		message2 = new FppsRequestCommand(FppsUtil.Command2);
		assertFalse(message1.equals(message2));

		message1 = new FppsRequestCommand(FppsUtil.ReqSem5Vazio);
		message2 = new FppsRequestCommand(FppsUtil.ReqSem5Vazio);
		assertEquals(message1, message2);
		message2 = new FppsRequestCommand(FppsUtil.Command2);
		assertFalse(message1.equals(message2));
	}

	@Test
	public void testGetCommand() {
		message = new FppsRequestCommand(FppsUtil.ReqSemParams);
		assertEquals(FppsUtil.Command, message.getCommand());

		message = new FppsRequestCommand(FppsUtil.ReqSem1Param);
		assertEquals(FppsUtil.Command, message.getCommand());

		message = new FppsRequestCommand(FppsUtil.ReqSem2Param);
		assertEquals(FppsUtil.Command, message.getCommand());

		message = new FppsRequestCommand(FppsUtil.ReqSem3Param);
		assertEquals(FppsUtil.Command, message.getCommand());

		message = new FppsRequestCommand(FppsUtil.ReqSem3UltimoVazio);
		assertEquals(FppsUtil.Command, message.getCommand());

		message = new FppsRequestCommand(FppsUtil.ReqSem5Vazio);
		assertEquals(FppsUtil.Command, message.getCommand());
	}

	@Test
	public void testGetParameters() {
		message = new FppsRequestCommand(FppsUtil.ReqSemParams);
		assertEquals(0, message.getParameters().length);

		message = new FppsRequestCommand(FppsUtil.ReqSem1Param);
		for (int i = 0; i < FppsUtil.Param1
				.split(FppsUtil.DelimitadorParametros).length; i++)
			assertEquals(
					FppsUtil.Param1.split(FppsUtil.DelimitadorParametros)[i],
					message.getParameters()[i]);

		message = new FppsRequestCommand(FppsUtil.ReqSem2Param);
		for (int i = 0; i < FppsUtil.Param2
				.split(FppsUtil.DelimitadorParametros).length; i++)
			assertEquals(
					FppsUtil.Param2.split(FppsUtil.DelimitadorParametros)[i],
					message.getParameters()[i]);

		message = new FppsRequestCommand(FppsUtil.ReqSem3Param);
		for (int i = 0; i < FppsUtil.Param3
				.split(FppsUtil.DelimitadorParametros).length; i++)
			assertEquals(
					FppsUtil.Param3.split(FppsUtil.DelimitadorParametros)[i],
					message.getParameters()[i]);

		message = new FppsRequestCommand(FppsUtil.ReqSem3UltimoVazio);
		for (int i = 0; i < FppsUtil.Param3UltimoVazio
				.split(FppsUtil.DelimitadorParametros).length; i++)
			assertEquals(FppsUtil.Param3UltimoVazio
					.split(FppsUtil.DelimitadorParametros)[i], message
					.getParameters()[i]);

		message = new FppsRequestCommand(FppsUtil.ReqSem5Vazio);
		for (int i = 0; i < FppsUtil.Param5Vazio
				.split(FppsUtil.DelimitadorParametros).length; i++)
			assertEquals(FppsUtil.Param5Vazio
					.split(FppsUtil.DelimitadorParametros)[i], message
					.getParameters()[i]);
	}

	@Test
	public void testGetParametersString() {
		message = new FppsRequestCommand(FppsUtil.ReqSemParams);
		assertEquals(0, message.getParameters().length);
		message = new FppsRequestCommand(FppsUtil.ReqSem1Param);
		assertEquals(1, message.getParameters().length);
		message = new FppsRequestCommand(FppsUtil.ReqSem2Param);
		assertEquals(2, message.getParameters().length);
		message = new FppsRequestCommand(FppsUtil.ReqSem3Param);
		assertEquals(3, message.getParameters().length);
		message = new FppsRequestCommand(FppsUtil.ReqSem3UltimoVazio);
		assertEquals(3, message.getParameters().length);
		message = new FppsRequestCommand(FppsUtil.ReqSem5Vazio);
		assertEquals(5, message.getParameters().length);
	}

	@Test
	public void testGetTcpMessage() {
		message = new FppsRequestCommand(FppsUtil.ReqSemParams);
		assertEquals(FppsUtil.ReqSemParams, message.getTcpMessage());
		message = new FppsRequestCommand(FppsUtil.ReqSem1Param);
		assertEquals(FppsUtil.ReqSem1Param, message.getTcpMessage());
		message = new FppsRequestCommand(FppsUtil.ReqSem2Param);
		assertEquals(FppsUtil.ReqSem2Param, message.getTcpMessage());
		message = new FppsRequestCommand(FppsUtil.ReqSem3Param);
		assertEquals(FppsUtil.ReqSem3Param, message.getTcpMessage());
		message = new FppsRequestCommand(FppsUtil.ReqSem3UltimoVazio);
		assertEquals(FppsUtil.ReqSem3UltimoVazio, message.getTcpMessage());
		message = new FppsRequestCommand(FppsUtil.ReqSem5Vazio);
		assertEquals(FppsUtil.ReqSem5Vazio, message.getTcpMessage());
	}

	@Test
	public void testToString() {
		// message = new FppsRequestCommand(ReqSemParams);
		// assertEquals(ReqSemParams, message.toString());
		message = new FppsRequestCommand(FppsUtil.ReqSem1Param);
		assertEquals(FppsUtil.ReqSem1Param, message.toString());
		message = new FppsRequestCommand(FppsUtil.ReqSem2Param);
		assertEquals(FppsUtil.ReqSem2Param, message.toString());
		message = new FppsRequestCommand(FppsUtil.ReqSem3Param);
		assertEquals(FppsUtil.ReqSem3Param, message.toString());
		message = new FppsRequestCommand(FppsUtil.ReqSem3UltimoVazio);
		assertEquals(FppsUtil.ReqSem3UltimoVazio, message.toString());
		message = new FppsRequestCommand(FppsUtil.ReqSem5Vazio);
		assertEquals(FppsUtil.ReqSem5Vazio, message.toString());
	}

}
