package br.com.gennex.interfaces;

public interface TcpController {
	void addHandler(TcpRequest request, TcpRequestHandler requestHandler);

	TcpResponse processRequest(TcpRequestCommand request);
}
