package br.com.gennex.socket.tcpcommand.messages.responses;

import br.com.gennex.interfaces.TcpResponseCommand;
import br.com.gennex.socket.tcpcommand.messages.FppsMessage;

public class GennexResponse extends FppsMessage implements TcpResponseCommand {

	public GennexResponse(String tcpMessage) {
		super(tcpMessage);
	}

}
