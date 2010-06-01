package br.com.gennex.socket.tcpcommand.messages.responses;

import br.com.gennex.interfaces.TcpRequest;

public class FppsErrorResponse extends FppsResponse {

	public static final String errorCommand = "InvalidRequest";

	private TcpRequest originalRequest;

	private Exception originalException;

	public FppsErrorResponse() {
		super(errorCommand);
	}

	public FppsErrorResponse(TcpRequest originalRequest) {
		super(errorCommand);
		setOriginalRequest(originalRequest);
	}

	public FppsErrorResponse(TcpRequest originalRequest,
			Exception originalException) {
		super(errorCommand);
		setOriginalRequest(originalRequest);
		setOriginalException(originalException);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof FppsResponse))
			return false;
		return ((FppsResponse) obj).getCommand().equalsIgnoreCase(getCommand());
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
