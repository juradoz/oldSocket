package br.com.gennex.socket.model;

public class ServerPort {
	private int port;

	public ServerPort(int port) {
		if (port <= 0)
			throw new IllegalArgumentException();
		this.port = port;
	}

	public int getPort() {
		return port;
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
		if (port != other.port)
			return false;
		return true;
	}
}
