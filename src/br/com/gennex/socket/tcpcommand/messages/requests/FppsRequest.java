package br.com.gennex.socket.tcpcommand.messages.requests;

import br.com.gennex.interfaces.TcpRequestCommand;
import br.com.gennex.socket.tcpcommand.messages.FppsMessage;

public class FppsRequest extends FppsMessage implements TcpRequestCommand {

	public FppsRequest(String tcpMessage) {
		super(tcpMessage);
	}

}
