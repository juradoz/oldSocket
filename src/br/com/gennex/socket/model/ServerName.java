package br.com.gennex.socket.model;

public class ServerName {
	private String name;

	public String getName() {
		return this.name;
	}

	public ServerName(String name) {
		if (name == null || name.length() <= 0)
			throw new IllegalArgumentException();
		this.name = name;
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
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
