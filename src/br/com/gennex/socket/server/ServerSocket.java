package br.com.gennex.socket.server;

import java.security.InvalidParameterException;

import org.apache.log4j.Logger;

import br.com.gennex.interfaces.SocketFactory;
import br.com.gennex.socket.Socket;
import br.com.gennex.socket.model.ServerPort;

/**
 * Classe que implementa um servidor TCP que ouve as conexões e cria threads
 * específicas para cada comportamento.
 * 
 * @author Daniel Jurado
 * 
 */
public class ServerSocket implements Runnable {

	private ServerPort serverPort;

	private SocketFactory socketFactory;

	/**
	 * @param port
	 *            a porta que o servidor deverá aceitar novas conexões.
	 * @param socketFactory
	 *            a factory que deve criar os sockets de acordo com os
	 *            comportamentos esperados.
	 */
	public ServerSocket(ServerPort serverPort, SocketFactory socketFactory) {
		super();
		if (serverPort == null)
			throw new InvalidParameterException("invalid port");
		if (socketFactory == null)
			throw new InvalidParameterException("invalid socketFactory");
		this.serverPort = serverPort;
		this.socketFactory = socketFactory;
	}

	/**
	 * @return a porta onde o servidor atualmente escuta.
	 */
	public final ServerPort getServerPort() {
		return serverPort;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public final void run() {
		do {
			try {
				java.net.ServerSocket server = new java.net.ServerSocket(
						getServerPort().getPort());
				Logger.getLogger(getClass()).info(
						"Ready to accept connections...");
				do {
					java.net.Socket socket = server.accept();
					Socket threadSocket = socketFactory.createSocket(socket);
					new Thread(threadSocket, "Client "
							+ socket.getInetAddress().getHostName()).start();
				} while (Thread.currentThread().isAlive());
			} catch (Exception e) {
				Logger.getLogger(getClass()).fatal(e.getMessage(), e);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException f) {
					Logger.getLogger(getClass()).error(f.getMessage(), f);
				}
			}
		} while (Thread.currentThread().isAlive());

	}

}
