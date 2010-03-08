package br.com.gennex.socket.server;

import java.io.IOException;
import java.net.SocketException;
import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import br.com.gennex.interfaces.SocketFactory;
import br.com.gennex.socket.Socket;
import br.com.gennex.socket.Socket.EventDisconnected;
import br.com.gennex.socket.model.ServerPort;

/**
 * Classe que implementa um servidor TCP que ouve as conexões e cria threads
 * específicas para cada comportamento.
 * 
 * @author Daniel Jurado
 * 
 */
public class ServerSocket implements Runnable, Observer {

	private class SocketAccepter implements Runnable {

		private final ServerSocket parent;
		private final SocketFactory socketFactory;

		private BlockingQueue<java.net.Socket> sockets = new LinkedBlockingQueue<java.net.Socket>();

		private SocketAccepter(ServerSocket parent, SocketFactory socketFactory) {
			this.parent = parent;
			this.socketFactory = socketFactory;
		}

		public void addSocket(java.net.Socket socket) {
			sockets.offer(socket);
		}

		@Override
		public void run() {
			do {
				java.net.Socket socket = null;
				try {
					socket = sockets.take();
				} catch (InterruptedException e) {
					Logger.getLogger(getClass()).error(e.getMessage(), e);
				}

				if (socket == null)
					continue;

				Socket threadSocket = socketFactory.createSocket(socket);
				synchronized (socketsAtivos) {
					socketsAtivos.add(threadSocket);
				}
				threadSocket.addObserver(parent);
				new Thread(threadSocket, "Client "
						+ socket.getInetAddress().getHostName()).start();

			} while (Thread.currentThread().isAlive());
		}
	}

	private ServerPort serverPort;

	private final SocketAccepter socketAccepter;

	private HashSet<Socket> socketsAtivos = new HashSet<Socket>();

	private java.net.ServerSocket server;

	private boolean ativo = true;

	/**
	 * @param port
	 *            a porta que o servidor deverá aceitar novas conexões.
	 * @param socketFactory
	 *            a factory que deve criar os sockets de acordo com os
	 *            comportamentos esperados.
	 */
	public ServerSocket(ServerPort serverPort, SocketFactory socketFactory) {
		this(serverPort, socketFactory, false);
	}

	/**
	 * @param port
	 *            a porta que o servidor deverá aceitar novas conexões.
	 * @param socketFactory
	 *            a factory que deve criar os sockets de acordo com os
	 *            comportamentos esperados.
	 * @param isDaemon
	 *            torna a thread do socket como daemon.
	 */
	public ServerSocket(ServerPort serverPort, SocketFactory socketFactory,
			boolean isDaemon) {
		super();
		if (serverPort == null)
			throw new InvalidParameterException("invalid port");
		if (socketFactory == null)
			throw new InvalidParameterException("invalid socketFactory");
		this.serverPort = serverPort;

		this.socketAccepter = new SocketAccepter(this, socketFactory);

		Thread t = new Thread(socketAccepter);
		t.setDaemon(true);
		t.start();

		t = new Thread(this);
		t.setDaemon(isDaemon);
		t.start();
	}

	public void close() throws IOException {
		synchronized (socketsAtivos) {
			Iterator<Socket> it = socketsAtivos.iterator();
			while (it.hasNext()) {
				Socket socket = it.next();
				try {
					socket.disconnect();
				} catch (IOException e) {
					Logger.getLogger(getClass()).error(e.getMessage(), e);
				}
				it.remove();
			}
		}
		this.ativo = false;
		this.server.close();
		Logger.getLogger(getClass()).info("Server socket closed");
	}

	/**
	 * @return a porta onde o servidor atualmente escuta.
	 */
	public final ServerPort getServerPort() {
		return serverPort;
	}

	public Collection<Socket> getSocketsAtivos() {
		Collection<Socket> result = new HashSet<Socket>();

		synchronized (socketsAtivos) {
			result.addAll(socketsAtivos);
		}

		return result;
	}

	public int getTotalConnections() {
		synchronized (socketsAtivos) {
			return socketsAtivos.size();
		}
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
				server = new java.net.ServerSocket(getServerPort()
						.getServerPort());
				Logger.getLogger(getClass()).info(
						"Ready to accept connections...");
				do {
					java.net.Socket socket = server.accept();
					this.socketAccepter.addSocket(socket);
				} while (Thread.currentThread().isAlive());
			} catch (SocketException e) {
				if (!ativo)
					return;
				Logger.getLogger(getClass()).fatal(e.getMessage(), e);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException f) {
					Logger.getLogger(getClass()).error(f.getMessage(), f);
				}
			} catch (Exception e) {
				Logger.getLogger(getClass()).fatal(e.getMessage(), e);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException f) {
					Logger.getLogger(getClass()).error(f.getMessage(), f);
				}
			}
		} while (Thread.currentThread().isAlive() && ativo);
	}

	@Override
	public void update(Observable socket, Object evento) {
		if (!(evento instanceof EventDisconnected))
			return;
		synchronized (socketsAtivos) {
			socketsAtivos.remove(socket);
		}
	}
}
