package br.com.gennex.interfaces;

public interface TcpResponseCommand extends TcpResponse {
	public String getCommand();

	public String[] getParameters();
}
