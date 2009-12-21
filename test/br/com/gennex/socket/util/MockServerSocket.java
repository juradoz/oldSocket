package br.com.gennex.socket.util;

import static org.junit.Assert.assertNotNull;

import java.net.Socket;

import br.com.gennex.interfaces.TcpRequest;
import br.com.gennex.interfaces.TcpRequestHandler;
import br.com.gennex.interfaces.TcpResponse;
import br.com.gennex.socket.tcpcommand.TcpCommandSocket;
import br.com.gennex.socket.tcpcommand.messages.requests.FppsRequest;
import br.com.gennex.socket.tcpcommand.messages.requests.FppsRequestCommand;
import br.com.gennex.socket.tcpcommand.messages.responses.FppsResponse;

public class MockServerSocket extends TcpCommandSocket {

	private boolean recebido = false;

	public MockServerSocket(Socket socket) {
		super(socket);
		addHandler(new FppsRequestCommand(FppsUtil.Command),
				new TcpRequestHandler() {

					@Override
					public TcpResponse process(
							br.com.gennex.socket.Socket socket,
							TcpRequest request) throws Exception {
						for (int i = 0; i < ((FppsRequest) request)
								.getParameters().length; i++)
							assertNotNull(((FppsRequest) request)
									.getParameters()[i]);
						setRecebido(true);
						return new FppsResponse(FppsUtil.response);
					}
				});
	}

	private void setRecebido(boolean recebido) {
		this.recebido = recebido;
	}

	public boolean isRecebido() {
		return recebido;
	}
}