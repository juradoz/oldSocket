package br.com.gennex.socket.tcpcommand.messages.responses;

import br.com.gennex.interfaces.TcpRequest;

public class GennexErrorResponse extends GennexResponse {

	public static final String errorCommand = "InvalidRequest";

	private TcpRequest originalRequest;

	private Exception originalException;

	public GennexErrorResponse() {
		super(errorCommand);
	}

	public GennexErrorResponse(TcpRequest originalRequest) {
		super(errorCommand);
		setOriginalRequest(originalRequest);
	}

	public GennexErrorResponse(TcpRequest originalRequest,
			Exception originalException) {
		super(errorCommand);
		setOriginalRequest(originalRequest);
		setOriginalException(originalException);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof GennexResponse))
			return false;
		return ((GennexResponse) obj).getCommand().equalsIgnoreCase(
				getCommand());
	}

	private void setOriginalException(Exception originalException) {
		this.originalException = originalException;
	}

	private void setOriginalRequest(TcpRequest originalRequest) {
		this.originalRequest = originalRequest;
	}

	@Override
	public String toString() {
		return String.format("%s(%s;%s)", getTcpMessage(), originalRequest
				.toString(), originalException.getClass().getSimpleName());
	}
}
