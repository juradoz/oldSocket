package br.com.gennex.interfaces;

import br.com.gennex.socket.Socket;

public interface SocketFactory {
	Socket createSocket(java.net.Socket socket);
}
