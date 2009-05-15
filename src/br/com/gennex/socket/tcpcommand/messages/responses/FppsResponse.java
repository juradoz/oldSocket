package br.com.gennex.socket.tcpcommand.messages.responses;

import br.com.gennex.interfaces.TcpResponseCommand;
import br.com.gennex.socket.tcpcommand.messages.FppsMessage;

public class FppsResponse extends FppsMessage implements TcpResponseCommand {

	public FppsResponse(String tcpMessage) {
		super(tcpMessage);
	}

}
