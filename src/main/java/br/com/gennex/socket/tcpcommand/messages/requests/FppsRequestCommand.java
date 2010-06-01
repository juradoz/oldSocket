package br.com.gennex.socket.tcpcommand.messages.requests;

import br.com.gennex.interfaces.TcpRequestCommand;

public class FppsRequestCommand extends FppsRequest implements
		TcpRequestCommand {

	public FppsRequestCommand(String tcpMessage) {
		super(tcpMessage);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof FppsRequestCommand))
			return false;

		return ((FppsRequest) obj).getCommand().equalsIgnoreCase(getCommand());
	}

}
