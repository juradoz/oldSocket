package br.com.gennex.socket.tcpcommand.messages;

import br.com.gennex.interfaces.TcpRequestCommand;

public abstract class Message implements TcpRequestCommand {

	private String tcpMessage;

	public Message(String tcpMessage) {
		setTcpMessage(tcpMessage);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		if (tcpMessage == null) {
			if (other.getTcpMessage() != null)
				return false;
		} else if (!tcpMessage.equalsIgnoreCase(other.getTcpMessage()))
			return false;
		return true;
	}

	@Override
	public final String getCommand() {
		return getCommand(getTcpMessage());
	}

	protected abstract String getCommand(String tcpMessage);

	@Override
	public final String[] getParameters() {
		return getParameters(getTcpMessage());
	}

	protected abstract String[] getParameters(String tcpMessage);

	@Override
	public final String getTcpMessage() {
		return this.tcpMessage;
	}

	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((tcpMessage == null) ? 0 : tcpMessage.hashCode());
		return result;
	}

	private final void setTcpMessage(String tcpMessage) {
		this.tcpMessage = tcpMessage;
	}

	@Override
	public String toString() {
		return this.getTcpMessage();
	}
}
