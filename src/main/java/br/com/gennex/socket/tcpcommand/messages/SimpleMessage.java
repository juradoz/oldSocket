package br.com.gennex.socket.tcpcommand.messages;

import java.util.Arrays;

public class SimpleMessage extends Message {

	public SimpleMessage(String tcpMessage) {
		super(tcpMessage);
	}

	@Override
	protected String getCommand(String tcpMessage) {
		if(tcpMessage==null)
			throw new IllegalArgumentException();
		if(tcpMessage.isEmpty())
			return tcpMessage;
		return tcpMessage.split(" ")[0];
	}

	@Override
	protected String[] getParameters(String tcpMessage) {
		if(tcpMessage==null)
			throw new IllegalArgumentException();
		if(tcpMessage.isEmpty())
			return new String[0];
		String[] result = tcpMessage.split(" ");
		return Arrays.copyOfRange(result, 1, result.length);
	}

}
