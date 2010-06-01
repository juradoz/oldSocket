package br.com.gennex.socket.model;

import java.security.InvalidParameterException;

public class ServerName {
	private String serverName;

	public ServerName(String name) {
		if (name == null || name.length() <= 0)
			throw new InvalidParameterException();
		this.serverName = name;
	}

	public String getServerName() {
		return this.serverName;
	}

	@Override
	public String toString() {
		return getServerName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((serverName == null) ? 0 : serverName.hashCode());
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
		ServerName other = (ServerName) obj;
		if (serverName == null) {
			if (other.serverName != null)
				return false;
		} else if (!serverName.equals(other.serverName))
			return false;
		return true;
	}
}
