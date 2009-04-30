package br.com.gennex.socket.tcpcommand.messages.requests;

import br.com.gennex.interfaces.TcpRequestCommand;

public class HttpRequestCommand extends HttpRequest implements
		TcpRequestCommand {

	public HttpRequestCommand(String tcpMessage) {
		super(tcpMessage);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HttpRequest)
			return false;

		return ((HttpRequest) obj).getCommand().equalsIgnoreCase(getCommand());
	}

	@Override
	public String toString() {
		return getCommand();
	}

}
