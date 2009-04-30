package br.com.gennex.socket.tcpcommand.messages;

import br.com.gennex.interfaces.TcpRequestCommand;
import br.com.gennex.socket.tcpcommand.messages.responses.GennexResponse;

public abstract class FppsMessage implements TcpRequestCommand {

	public static final String getCommand(String tcpMessage) {
		if (tcpMessage.indexOf("(") > 0) {
			String s = tcpMessage.substring(0, tcpMessage.indexOf("("));
			return s;
		} else {
			return tcpMessage;
		}
	}

	public static final String[] getParameters(String tcpMessage) {
		String[] s = "".split(";");
		int i = tcpMessage.indexOf("(");
		int f = tcpMessage.indexOf(")");
		if (i > 0 && f > 0) {
			String sub = tcpMessage.substring(i + 1, f);
			s = sub.split(";");
		}
		return s;
	}

	private String tcpMessage;

	public FppsMessage(String tcpMessage) {
		setTcpMessage(tcpMessage);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof GennexResponse))
			return false;
		return ((GennexResponse) obj).getTcpMessage().equalsIgnoreCase(
				this.getTcpMessage());
	}

	@Override
	public final String getCommand() {
		return getCommand(getTcpMessage());
	}

	@Override
	public final String[] getParameters() {
		return getParameters(getTcpMessage());
	}

	@Override
	public final String getTcpMessage() {
		return this.tcpMessage;
	}

	private final void setTcpMessage(String tcpMessage) {
		this.tcpMessage = tcpMessage;
	}

	@Override
	public String toString() {
		return this.getTcpMessage();
	}
}
