package br.com.gennex.socket.tcpcommand.messages.requests;

import br.com.gennex.interfaces.TcpRequest;
import br.com.gennex.interfaces.TcpRequestHandler;
import br.com.gennex.interfaces.TcpResponse;
import br.com.gennex.socket.Socket;

public class InvalidTcpRequestHandler implements TcpRequestHandler {

	@Override
	public TcpResponse process(Socket socket, TcpRequest request)
			throws Exception {
		return null;
	}

}
