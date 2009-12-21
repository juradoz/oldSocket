package br.com.gennex.socket.tcpcommand.messages;

public class HttpMessage extends Message {

	public HttpMessage(String tcpMessage) {
		super(tcpMessage);
	}

	protected final String getCommand(String tcpMessage) {
		int indice = tcpMessage.indexOf(" ");
		if (indice > 0) {
			String s = tcpMessage.substring(0, indice);
			return s;
		} else {
			return tcpMessage;
		}
	}

	protected final String[] getParameters(String tcpMessage) {
		String[] s = tcpMessage.split(" ");
		String[] result = new String[s.length - 1];

		for (int i = 1; i < s.length; i++) {
			result[i - 1] = s[i];
		}
		return result;
	}
}
