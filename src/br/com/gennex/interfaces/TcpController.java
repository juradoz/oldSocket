package br.com.gennex.interfaces;

public interface TcpController {
	void addHandler(TcpRequest request, TcpRequestHandler requestHandler);

	void clearHandlers();

	TcpRequestHandler removeHandler(TcpRequest request);

	TcpResponse processRequest(TcpRequestCommand request);
}
