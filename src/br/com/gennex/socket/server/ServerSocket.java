package br.com.gennex.socket.server;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import org.apache.log4j.Logger;

import br.com.gennex.interfaces.SocketFactory;
import br.com.gennex.socket.Socket;

/**
 * Classe que implementa um servidor TCP que ouve as conexões e cria threads
 * específicas para cada comportamento.
 * 
 * @author Daniel Jurado
 * 
 */
public class ServerSocket implements Runnable, Observer {

	/**
	 * Lista em que são mantidos todos os sockets atualmente ativos.
	 */
	private LinkedList<Socket> sockets = new LinkedList<Socket>();

	private int port;

	private SocketFactory socketFactory;

	/**
	 * @param port
	 *            a porta que o servidor deverá aceitar novas conexões.
	 * @param socketFactory
	 *            a factory que deve criar os sockets de acordo com os
	 *            comportamentos esperados.
	 */
	public ServerSocket(int port, SocketFactory socketFactory) {
		super();
		if (port <= 0)
			throw new InvalidParameterException("invalid port");
		if (socketFactory == null)
			throw new InvalidParameterException("invalid socketFactory");
		this.port = port;
		this.socketFactory = socketFactory;
	}

	private void addSocket(Socket socket) {
		sockets.add(socket);
	}

	/**
	 * @return a porta onde o servidor atualmente escuta.
	 */
	public final int getPort() {
		return port;
	}

	private void removeSocket(Socket socket) {
		sockets.remove(socket);
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
				java.net.ServerSocket server = new java.net.ServerSocket(port);
				Logger.getLogger(getClass()).info(
						"Ready to accept connections...");
				do {
					java.net.Socket socket = server.accept();
					Socket threadSocket = socketFactory.createSocket(socket);
					threadSocket.addObserver(this);
					addSocket(threadSocket);
					new Thread(threadSocket, "Client "
							+ socket.getInetAddress().getHostName()).start();
				} while (Thread.currentThread().isAlive());
			} catch (IOException e) {
				Logger.getLogger(getClass()).fatal(e.getMessage(), e);
				try {
					Thread.sleep(10000);
				} catch (InterruptedException f) {
					Logger.getLogger(getClass()).error(f.getMessage(), f);
				}
			}
		} while (Thread.currentThread().isAlive());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public final void update(Observable o, Object arg) {
		if (arg instanceof Socket.EventDisconnected) {
			removeSocket((Socket) o);
		}
	}

}
