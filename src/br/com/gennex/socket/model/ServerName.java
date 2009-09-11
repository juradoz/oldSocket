package br.com.gennex.socket.model;

import java.security.InvalidParameterException;

public class ServerName {
	private String serverName;

	public String getServerName() {
		return this.serverName;
	}

	public ServerName(String name) {
		if (name == null || name.length() <= 0)
			throw new InvalidParameterException();
		this.serverName = name;
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
