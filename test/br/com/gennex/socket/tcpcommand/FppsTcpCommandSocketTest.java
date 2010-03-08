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
import br.com.gennex.socket.tcpcommand.messages.requests.FppsRequestCommand;
import br.com.gennex.socket.tcpcommand.messages.responses.FppsResponse;
import br.com.gennex.socket.util.FppsUtil;
import br.com.gennex.socket.util.Util;

public class FppsTcpCommandSocketTest {

	private TcpCommandSocket tcpCommandSocket;

	@Before
	public void setUp() {
		tcpCommandSocket = new TcpCommandSocket(new java.net.Socket());
		assertNotNull(tcpCommandSocket);
		tcpCommandSocket.addHandler(new FppsRequestCommand(FppsUtil.Command),
				new TcpRequestHandler() {

					@Override
					public TcpResponse process(Socket socket, TcpRequest request)
							throws Exception {
						for (int i = 0; i < ((FppsRequest) request)
								.getParameters().length; i++)
							assertNotNull(((FppsRequest) request)
									.getParameters()[i]);
						return new FppsResponse(Util.response);
					}
				});
	}

	@Test
	public void testAddHandler() {
		tcpCommandSocket.clearHandlers();
		TcpRequest request = new FppsRequestCommand(FppsUtil.ReqSem5Vazio);
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
			assertEquals(new FppsResponse(Util.response), tcpCommandSocket
					.processRequest(new FppsRequestCommand(
							FppsUtil.ReqSemParams)));
			assertEquals(new FppsResponse(Util.response), tcpCommandSocket
					.processRequest(new FppsRequestCommand(
							FppsUtil.ReqSem1Param)));
			assertEquals(new FppsResponse(Util.response), tcpCommandSocket
					.processRequest(new FppsRequestCommand(
							FppsUtil.ReqSem2Param)));
			assertEquals(new FppsResponse(Util.response), tcpCommandSocket
					.processRequest(new FppsRequestCommand(
							FppsUtil.ReqSem3Param)));
			assertEquals(new FppsResponse(Util.response), tcpCommandSocket
					.processRequest(new FppsRequestCommand(
							FppsUtil.ReqSem3UltimoVazio)));
			assertEquals(new FppsResponse(Util.response), tcpCommandSocket
					.processRequest(new FppsRequestCommand(
							FppsUtil.ReqSem5Vazio)));
		} catch (Exception e) {
			fail(e.getClass().getSimpleName() + ":" + e.getMessage());
		}
	}

}
