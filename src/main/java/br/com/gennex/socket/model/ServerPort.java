package br.com.gennex.socket.model;

import java.security.InvalidParameterException;

public class ServerPort {
	private int serverPort;

	public ServerPort(int port) {
		if (port <= 0)
			throw new InvalidParameterException();
		this.serverPort = port;
	}

	public int getServerPort() {
		return serverPort;
	}

	@Override
	public String toString() {
		return String.valueOf(getServerPort());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + serverPort;
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
		ServerPort other = (ServerPort) obj;
		if (serverPort != other.serverPort)
			return false;
		return true;
	}
}
