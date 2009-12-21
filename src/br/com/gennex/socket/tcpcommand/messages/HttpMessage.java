package br.com.gennex.socket.tcpcommand.messages;

import br.com.gennex.interfaces.TcpRequestCommand;

public class HttpMessage implements TcpRequestCommand {

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
		HttpMessage other = (HttpMessage) obj;
		if (tcpMessage == null) {
			if (other.tcpMessage != null)
				return false;
		} else if (!tcpMessage.equalsIgnoreCase(other.tcpMessage))
			return false;
		return true;
	}

	public static final String getCommand(String tcpMessage) {
		int indice = tcpMessage.indexOf(" ");
		if (indice > 0) {
			String s = tcpMessage.substring(0, indice);
			return s;
		} else {
			return tcpMessage;
		}
	}

	public static final String[] getParameters(String tcpMessage) {
		String[] s = tcpMessage.split(" ");
		String[] result = new String[s.length - 1];

		for (int i = 1; i < s.length; i++) {
			result[i - 1] = s[i];
		}
		return result;
	}

	private String tcpMessage;

	public HttpMessage(String tcpMessage) {
		setTcpMessage(tcpMessage.toUpperCase());
	}

	@Override
	public String getCommand() {
		return getCommand(getTcpMessage());
	}

	@Override
	public String[] getParameters() {
		return getParameters(getTcpMessage());
	}

	@Override
	public String getTcpMessage() {
		return tcpMessage;
	}

	private final void setTcpMessage(String tcpMessage) {
		this.tcpMessage = tcpMessage;
	}

}
