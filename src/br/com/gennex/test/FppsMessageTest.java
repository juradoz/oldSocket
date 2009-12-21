package br.com.gennex.test;

import junit.framework.TestCase;

import org.junit.Test;

import br.com.gennex.socket.tcpcommand.messages.FppsMessage;
import br.com.gennex.socket.tcpcommand.messages.requests.FppsRequest;

public class FppsMessageTest extends TestCase {

	private static final String ParamsVazio = "";
	private static final String Param1 = "param1";
	private static final String Param2 = "param1;param2";
	private static final String Param3 = "param1;param2;param3";
	private static final String Param3UltimoVazio = "param1;param2;param3;";
	private static final String Param5Vazio = "param1;param2;param3;;param4";

	private static final String Command = "Command";

	private static final String ReqSemParams = Command + "(" + ParamsVazio
			+ ")";
	private static final String ReqSem1Param = Command + "(" + Param1 + ")";
	private static final String ReqSem2Param = Command + "(" + Param2 + ")";
	private static final String ReqSem3Param = Command + "(" + Param3 + ")";
	private static final String ReqSem3UltimoVazio = Command + "("
			+ Param3UltimoVazio + ")";
	private static final String ReqSem5Vazio = Command + "(" + Param5Vazio
			+ ")";

	private FppsMessage message;
	private FppsMessage message1;
	private FppsMessage message2;

	@Test
	public void testGetParametersString() {
		message = new FppsRequest(ReqSemParams);
		assertEquals(0, message.getParameters().length);
		message = new FppsRequest(ReqSem1Param);
		assertEquals(1, message.getParameters().length);
		message = new FppsRequest(ReqSem2Param);
		assertEquals(2, message.getParameters().length);
		message = new FppsRequest(ReqSem3Param);
		assertEquals(3, message.getParameters().length);
		message = new FppsRequest(ReqSem3UltimoVazio);
		assertEquals(3, message.getParameters().length);
		message = new FppsRequest(ReqSem5Vazio);
		assertEquals(5, message.getParameters().length);
	}

	@Test
	public void testEqualsObject() {
		message1 = new FppsRequest(ReqSemParams);
		message2 = new FppsRequest(ReqSemParams);
		assertEquals(message1, message2);
		message2 = new FppsRequest(ReqSem1Param);
		assertFalse(message1.equals(message2));

		message1 = new FppsRequest(ReqSem1Param);
		message2 = new FppsRequest(ReqSem1Param);
		assertEquals(message1, message2);
		message2 = new FppsRequest(ReqSem2Param);
		assertFalse(message1.equals(message2));

		message1 = new FppsRequest(ReqSem2Param);
		message2 = new FppsRequest(ReqSem2Param);
		assertEquals(message1, message2);
		message2 = new FppsRequest(ReqSem3Param);
		assertFalse(message1.equals(message2));

		message1 = new FppsRequest(ReqSem3Param);
		message2 = new FppsRequest(ReqSem3Param);
		assertEquals(message1, message2);
		message2 = new FppsRequest(ReqSem3UltimoVazio);
		assertFalse(message1.equals(message2));

		message1 = new FppsRequest(ReqSem3UltimoVazio);
		message2 = new FppsRequest(ReqSem3UltimoVazio);
		assertEquals(message1, message2);
		message2 = new FppsRequest(ReqSem5Vazio);
		assertFalse(message1.equals(message2));

		message1 = new FppsRequest(ReqSem5Vazio);
		message2 = new FppsRequest(ReqSem5Vazio);
		assertEquals(message1, message2);
		message2 = new FppsRequest(ReqSemParams);
		assertFalse(message1.equals(message2));
	}

	@Test
	public void testGetCommand() {
		message = new FppsRequest(ReqSemParams);
		assertEquals(Command, message.getCommand());

		message = new FppsRequest(ReqSem1Param);
		assertEquals(Command, message.getCommand());

		message = new FppsRequest(ReqSem2Param);
		assertEquals(Command, message.getCommand());

		message = new FppsRequest(ReqSem3Param);
		assertEquals(Command, message.getCommand());

		message = new FppsRequest(ReqSem3UltimoVazio);
		assertEquals(Command, message.getCommand());

		message = new FppsRequest(ReqSem5Vazio);
		assertEquals(Command, message.getCommand());
	}

	@Test
	public void testGetParameters() {
		message = new FppsRequest(ReqSemParams);
		assertEquals(0, message.getParameters().length);

		message = new FppsRequest(ReqSem1Param);
		for (int i = 0; i < Param1.split(";").length; i++)
			assertEquals(Param1.split(";")[i], message.getParameters()[i]);

		message = new FppsRequest(ReqSem2Param);
		for (int i = 0; i < Param2.split(";").length; i++)
			assertEquals(Param2.split(";")[i], message.getParameters()[i]);

		message = new FppsRequest(ReqSem3Param);
		for (int i = 0; i < Param3.split(";").length; i++)
			assertEquals(Param3.split(";")[i], message.getParameters()[i]);

		message = new FppsRequest(ReqSem3UltimoVazio);
		for (int i = 0; i < Param3UltimoVazio.split(";").length; i++)
			assertEquals(Param3UltimoVazio.split(";")[i], message
					.getParameters()[i]);

		message = new FppsRequest(ReqSem5Vazio);
		for (int i = 0; i < Param5Vazio.split(";").length; i++)
			assertEquals(Param5Vazio.split(";")[i], message.getParameters()[i]);
	}

	@Test
	public void testGetTcpMessage() {
		message = new FppsRequest(ReqSemParams);
		assertEquals(ReqSemParams, message.getTcpMessage());
		message = new FppsRequest(ReqSem1Param);
		assertEquals(ReqSem1Param, message.getTcpMessage());
		message = new FppsRequest(ReqSem2Param);
		assertEquals(ReqSem2Param, message.getTcpMessage());
		message = new FppsRequest(ReqSem3Param);
		assertEquals(ReqSem3Param, message.getTcpMessage());
		message = new FppsRequest(ReqSem3UltimoVazio);
		assertEquals(ReqSem3UltimoVazio, message.getTcpMessage());
		message = new FppsRequest(ReqSem5Vazio);
		assertEquals(ReqSem5Vazio, message.getTcpMessage());
	}

	@Test
	public void testToString() {
		message = new FppsRequest(ReqSemParams);
		assertEquals(ReqSemParams, message.toString());
		message = new FppsRequest(ReqSem1Param);
		assertEquals(ReqSem1Param, message.toString());
		message = new FppsRequest(ReqSem2Param);
		assertEquals(ReqSem2Param, message.toString());
		message = new FppsRequest(ReqSem3Param);
		assertEquals(ReqSem3Param, message.toString());
		message = new FppsRequest(ReqSem3UltimoVazio);
		assertEquals(ReqSem3UltimoVazio, message.toString());
		message = new FppsRequest(ReqSem5Vazio);
		assertEquals(ReqSem5Vazio, message.toString());
	}

}
