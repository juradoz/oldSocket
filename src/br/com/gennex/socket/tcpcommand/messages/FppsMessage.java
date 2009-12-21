package br.com.gennex.socket.tcpcommand.messages;

public abstract class FppsMessage extends Message {

	public FppsMessage(String tcpMessage) {
		super(tcpMessage);
	}

	protected final String getCommand(String tcpMessage) {
		if (tcpMessage.indexOf("(") > 0) {
			String s = tcpMessage.substring(0, tcpMessage.indexOf("("));
			return s;
		} else {
			return tcpMessage;
		}
	}

	protected final String[] getParameters(String tcpMessage) {
		String[] s = new String[0];
		int i = tcpMessage.indexOf("(");
		int f = tcpMessage.indexOf(")");
		if (i > 0 && f > 0 && f - i > 1) {
			String sub = tcpMessage.substring(i + 1, f);
			s = sub.split(";");
		}
		return s;
	}
}
