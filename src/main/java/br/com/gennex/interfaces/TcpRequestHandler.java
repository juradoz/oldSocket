package br.com.gennex.interfaces;

import br.com.gennex.socket.Socket;

public interface TcpRequestHandler {
	public TcpResponse process(Socket socket, TcpRequest request)
			throws Exception;
}
