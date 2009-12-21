package br.com.gennex.socket.tcpcommand;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import br.com.gennex.interfaces.TcpRequest;
import br.com.gennex.interfaces.TcpRequestHandler;
import br.com.gennex.interfaces.TcpResponse;
import br.com.gennex.socket.Socket;
import br.com.gennex.socket.tcpcommand.messages.requests.FppsRequest;
import br.com.gennex.socket.tcpcommand.messages.responses.FppsResponse;

public class FppsTcpCommandSocketTest {
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

	private static final String response = "OK";
	private TcpCommandSocket tcpCommandSocket;

	@Before
	public void setUp() {
		tcpCommandSocket = new TcpCommandSocket(new java.net.Socket());
		assertNotNull(tcpCommandSocket);
		tcpCommandSocket.addHandler(new FppsRequest(Command),
				new TcpRequestHandler() {

					@Override
					public TcpResponse process(Socket socket, TcpRequest request)
							throws Exception {
						for (int i = 0; i < ((FppsRequest) request)
								.getParameters().length; i++)
							assertNotNull(((FppsRequest) request)
									.getParameters()[i]);
						return new FppsResponse(response);
					}
				});
	}

	@Test
	public void testAddHandler() {
		tcpCommandSocket.clearHandlers();
		TcpRequest request = new FppsRequest(ReqSem5Vazio);
		TcpRequestHandler handler = new TcpRequestHandler() {
			@Override
			public TcpResponse process(Socket socket, TcpRequest request)
					throws Exception {
				return null;
			}
		};
		tcpCommandSocket.addHandler(request, handler);

		assertSame(handler, tcpCommandSocket.removeHandler(request));

	}

	@Test
	public void testProcessRequest() {
		try {
			assertEquals(new FppsResponse(response), tcpCommandSocket
					.processRequest(new FppsRequest(ReqSemParams)));
			assertEquals(new FppsResponse(response), tcpCommandSocket
					.processRequest(new FppsRequest(ReqSem1Param)));
			assertEquals(new FppsResponse(response), tcpCommandSocket
					.processRequest(new FppsRequest(ReqSem2Param)));
			assertEquals(new FppsResponse(response), tcpCommandSocket
					.processRequest(new FppsRequest(ReqSem3Param)));
			assertEquals(new FppsResponse(response), tcpCommandSocket
					.processRequest(new FppsRequest(ReqSem3UltimoVazio)));
			assertEquals(new FppsResponse(response), tcpCommandSocket
					.processRequest(new FppsRequest(ReqSem5Vazio)));
		} catch (Exception e) {
			fail(e.getClass().getSimpleName() + ":" + e.getMessage());
		}
	}

}
