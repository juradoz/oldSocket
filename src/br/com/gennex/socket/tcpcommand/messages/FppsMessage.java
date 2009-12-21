package br.com.gennex.socket.tcpcommand.messages;

import br.com.gennex.interfaces.TcpRequestCommand;

public abstract class FppsMessage implements TcpRequestCommand {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((tcpMessage == null) ? 0 : tcpMessage.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FppsMessage other = (FppsMessage) obj;
		if (tcpMessage == null) {
			if (other.tcpMessage != null)
				return false;
		} else if (!tcpMessage.equalsIgnoreCase(other.tcpMessage))
			return false;
		return true;
	}

	public static final String getCommand(String tcpMessage) {
		if (tcpMessage.indexOf("(") > 0) {
			String s = tcpMessage.substring(0, tcpMessage.indexOf("("));
			return s;
		} else {
			return tcpMessage;
		}
	}

	public static final String[] getParameters(String tcpMessage) {
		String[] s = new String[0];
		int i = tcpMessage.indexOf("(");
		int f = tcpMessage.indexOf(")");
		if (i > 0 && f > 0 && f - i > 1) {
			String sub = tcpMessage.substring(i + 1, f);
			s = sub.split(";");
		}
		return s;
	}

	private String tcpMessage;

	public FppsMessage(String tcpMessage) {
		setTcpMessage(tcpMessage.toUpperCase());
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
