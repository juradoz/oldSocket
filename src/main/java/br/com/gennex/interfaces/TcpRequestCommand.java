package br.com.gennex.interfaces;

public interface TcpRequestCommand extends TcpRequest {

	public String getCommand();

	public String[] getParameters();
}
