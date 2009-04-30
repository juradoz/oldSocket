package br.com.gennex.socket.tcpcommand.messages.requests;

import br.com.gennex.socket.tcpcommand.messages.HttpMessage;

public class HttpRequest extends HttpMessage {

	public HttpRequest(String tcpMessage) {
		super(tcpMessage);
	}

}
