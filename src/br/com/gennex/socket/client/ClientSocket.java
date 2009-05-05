package br.com.gennex.socket.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.security.InvalidParameterException;
import java.util.Observable;
import java.util.Observer;

import org.apache.log4j.Logger;

import br.com.gennex.interfaces.SocketFactory;
import br.com.gennex.socket.Socket;

/**
 * 
 * Classe que implementa um client TCP que se mantem conectado.
 * 
 * @author Daniel Jurado
 * 
 */
public class ClientSocket implements Runnable, Observer {

	private Socket socket = null;
	private String host;
	private int port;
	private SocketFactory socketFactory;
	private int reconnectInterval = 10000;

	public ClientSocket(String host, int port, SocketFactory socketFactory) {
		super();
		this.host = host;
		this.port = port;
		this.socketFactory = socketFactory;
	}

	private void checkConnection() {
		if (getSocket() != null)
			return;

		InetAddress addr = null;
		try {
			addr = InetAddress.getByName(getHost());
		} catch (UnknownHostException e) {
			Logger.getLogger(getClass()).error(e.getMessage(), e);
			return;
		}

		SocketAddress sockaddr = new InetSocketAddress(addr, getPort());

		java.net.Socket socket = new java.net.Socket();

		try {
			socket.connect(sockaddr);
		} catch (IOException e) {
			Logger.getLogger(getClass()).error(e.getMessage(), e);
			return;
		}

		setSocket(socketFactory.createSocket(socket));
		getSocket().addObserver(this);
		new Thread(getSocket(), "Server "
				+ socket.getInetAddress().getHostName()).start();
	}

	/**
	 * @return o host atual onde o socket se conecta.
	 */
	public final String getHost() {
		return host;
	}

	/**
	 * @return a porta onde o socket atualmente se conecta.
	 */
	public final int getPort() {
		return port;
	}

	/**
	 * @return o intervalo de reconexao automatica.
	 */
	public final int getReconnectInterval() {
		return reconnectInterval;
	}

	private Socket getSocket() {
		return socket;
	}

	/**
	 * @return retorna a factory responsável pela geração dos sockets de conexão
	 *         que sao mantidos por este objeto.
	 */
	public final SocketFactory getSocketFactory() {
		return socketFactory;
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
				checkConnection();
			} finally {
				try {
					Thread.sleep(getReconnectInterval());
				} catch (InterruptedException e) {
					Logger.getLogger(getClass()).error(e.getMessage(), e);
				}
			}
		} while (Thread.currentThread().isAlive());
	}

	/**
	 * @param host
	 *            modifica o host onde o socket se conecta. Se o valor for
	 *            modificado enquanto uma conexão está ativa, ela é terminada e
	 *            uma nova se inicia.
	 */
	public final void setHost(String host) {
		if (getHost().equalsIgnoreCase(host))
			return;
		if (getSocket() != null && getSocket().isConnected())
			try {
				getSocket().disconnect();
			} catch (IOException e) {
				Logger.getLogger(getClass()).error(e.getMessage(), e);
			}
		this.host = host;
	}

	/**
	 * @param port
	 *            modifica a porta onde o socket se conecta. Se o valor for
	 *            modificado enquanto uma conexão está ativa, ela é terminada e
	 *            uma nova se inicia.
	 */
	public final void setPort(int port) {
		if (getPort() == port)
			return;
		if (getSocket() != null && getSocket().isConnected())
			try {
				getSocket().disconnect();
			} catch (IOException e) {
				Logger.getLogger(getClass()).error(e.getMessage(), e);
			}
		this.port = port;
	}

	/**
	 * @param reconnectInterval
	 * 
	 *            Altera o intervalo em milisegindos entre as tentativas de
	 *            conexao. O valor minimo eh de 1000 ms. Caso seja informado um
	 *            valor maior, lanca uma
	 *            {@link java.security.InvalidParameterException}.
	 */
	public final void setReconnectInterval(int reconnectInterval) {
		if (reconnectInterval < 1000)
			throw new InvalidParameterException(
					"interval must be at least 1000 ms");
		this.reconnectInterval = reconnectInterval;
	}

	private void setSocket(Socket socket) {
		this.socket = socket;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public final void update(Observable o, Object arg) {
		if (!(arg instanceof Socket.EventDisconnected))
			return;
		setSocket(null);
	}

}
