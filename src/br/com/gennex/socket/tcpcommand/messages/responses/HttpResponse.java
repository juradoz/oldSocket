package br.com.gennex.socket.tcpcommand.messages.responses;

import br.com.gennex.socket.tcpcommand.messages.HttpMessage;

public class HttpResponse extends HttpMessage {

	public HttpResponse(String tcpMessage) {
		super(tcpMessage);
	}

}
