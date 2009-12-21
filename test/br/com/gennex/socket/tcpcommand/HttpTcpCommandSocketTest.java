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
import br.com.gennex.socket.tcpcommand.messages.requests.HttpRequestCommand;
import br.com.gennex.socket.tcpcommand.messages.responses.HttpResponse;
import br.com.gennex.socket.util.HttpUtil;

public class HttpTcpCommandSocketTest {

	private static final String ParamsVazio = "";
	private static final String Param1 = "param1";
	private static final String Param2 = "param1 param2";
	private static final String Param3 = "param1 param2 param3";
	private static final String Param3UltimoVazio = "param1 param2 param3;";
	private static final String Param5Vazio = "param1 param2 param3  param4";

	private static final String Command = "Command";

	private static final String ReqSemParams = Command + " " + ParamsVazio
			+ ")";
	private static final String ReqSem1Param = Command + " " + Param1;
	private static final String ReqSem2Param = Command + " " + Param2;
	private static final String ReqSem3Param = Command + " " + Param3;
	private static final String ReqSem3UltimoVazio = Command + " "
			+ Param3UltimoVazio;
	private static final String ReqSem5Vazio = Command + " " + Param5Vazio;

	private TcpCommandSocket tcpCommandSocket;

	@Before
	public void setUp() {
		tcpCommandSocket = new TcpCommandSocket(new java.net.Socket());
		assertNotNull(tcpCommandSocket);
		tcpCommandSocket.addHandler(new HttpRequestCommand(Command),
				new TcpRequestHandler() {

					@Override
					public TcpResponse process(Socket socket, TcpRequest request)
							throws Exception {
						for (int i = 0; i < ((HttpRequestCommand) request)
								.getParameters().length; i++)
							assertNotNull(((HttpRequestCommand) request)
									.getParameters()[i]);
						return new HttpResponse(HttpUtil.response);
					}
				});
	}

	@Test
	public void testAddHandler() {
		tcpCommandSocket.clearHandlers();
		TcpRequest request = new HttpRequestCommand(ReqSem5Vazio);
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
			assertEquals(new HttpResponse(HttpUtil.response), tcpCommandSocket
					.processRequest(new HttpRequestCommand(ReqSemParams)));
			assertEquals(new HttpResponse(HttpUtil.response), tcpCommandSocket
					.processRequest(new HttpRequestCommand(ReqSem1Param)));
			assertEquals(new HttpResponse(HttpUtil.response), tcpCommandSocket
					.processRequest(new HttpRequestCommand(ReqSem2Param)));
			assertEquals(new HttpResponse(HttpUtil.response), tcpCommandSocket
					.processRequest(new HttpRequestCommand(ReqSem3Param)));
			assertEquals(new HttpResponse(HttpUtil.response), tcpCommandSocket
					.processRequest(new HttpRequestCommand(ReqSem3UltimoVazio)));
			assertEquals(new HttpResponse(HttpUtil.response), tcpCommandSocket
					.processRequest(new HttpRequestCommand(ReqSem5Vazio)));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getClass().getSimpleName() + ":" + e.getMessage());
		}
	}
}
