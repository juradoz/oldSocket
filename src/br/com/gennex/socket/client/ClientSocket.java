package br.com.gennex.socket.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.security.InvalidParameterException;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import br.com.gennex.interfaces.SocketFactory;
import br.com.gennex.socket.Socket;
import br.com.gennex.socket.model.ServerName;
import br.com.gennex.socket.model.ServerPort;

/**
 * 
 * Classe que implementa um client TCP que se mantem conectado.
 * 
 * @author Daniel Jurado
 * 
 */
public class ClientSocket extends TimerTask implements Observer {

	public class SocketNaoConectado extends Exception {
		private static final long serialVersionUID = 1L;

		public SocketNaoConectado(String string) {
			super(string);
		}

	}

	private Socket socket = null;
	private ServerName serverName;
	private ServerPort serverPort;
	private SocketFactory socketFactory;

	private static final int DEFAULT_RECONNECT_INTERVAL = (int) (1 + Math
			.random() * 10000);

	private int reconnectInterval = DEFAULT_RECONNECT_INTERVAL;

	public ClientSocket(ServerName host, ServerPort port,
			SocketFactory socketFactory) {
		this(host, port, socketFactory, DEFAULT_RECONNECT_INTERVAL, false);
	}

	public ClientSocket(ServerName host, ServerPort port,
			SocketFactory socketFactory, int reconnectInterval) {
		this(host, port, socketFactory, reconnectInterval, false);
	}

	public ClientSocket(ServerName host, ServerPort port,
			SocketFactory socketFactory, int reconnectInterval, boolean isDaemon) {
		super();
		this.serverName = host;
		this.serverPort = port;
		this.socketFactory = socketFactory;
		new Timer(getClass().getSimpleName(), isDaemon).schedule(this, 0,
				reconnectInterval);
	}

	private void checkConnection() {
		if (isConnected())
			return;

		if (getServerName() == null || getServerPort() == null) {
			return;
		}

		InetAddress addr = null;
		try {
			addr = InetAddress.getByName(getServerName().getServerName());
		} catch (UnknownHostException e) {
			Logger.getLogger(getClass()).error(
					"Host nao encontrado! " + getServerName());
			return;
		}

		SocketAddress sockaddr = new InetSocketAddress(addr, getServerPort()
				.getServerPort());

		java.net.Socket rawSocket = new java.net.Socket();

		try {
			Logger.getLogger(getClass()).info(
					"Conectando a " + getServerName() + ":" + getServerPort()
							+ "...");
			rawSocket.connect(sockaddr);
			Logger.getLogger(getClass()).info(
					"Conectado a " + getServerName() + ":" + getServerPort());
		} catch (IOException e) {
			Logger.getLogger(getClass()).error(
					"Erro na conexao a " + getServerName() + ":"
							+ getServerPort() + " " + e.getMessage(), e);
			return;
		}

		setSocket(socketFactory.createSocket(rawSocket));
		getSocket().addObserver(this);
		new Thread(getSocket(), "Server "
				+ rawSocket.getInetAddress().getHostName()).start();
	}

	public void disconnect() throws IOException {
		if (getSocket() == null)
			return;
		this.socket.disconnect();
		setSocket(null);
	}

	/**
	 * @return o intervalo de reconexao automatica.
	 */
	public final int getReconnectInterval() {
		return reconnectInterval;
	}

	/**
	 * @return o host atual onde o socket se conecta.
	 */
	public final ServerName getServerName() {
		return serverName;
	}

	/**
	 * @return a porta onde o socket atualmente se conecta.
	 */
	public final ServerPort getServerPort() {
		return serverPort;
	}

	public Socket getSocket() {
		return socket;
	}

	/**
	 * @return retorna a factory responsável pela geração dos sockets de conexão
	 *         que sao mantidos por este objeto.
	 */
	public final SocketFactory getSocketFactory() {
		return socketFactory;
	}

	public boolean isConnected() {
		return getSocket() != null && getSocket().isConnected();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public final void run() {
		checkConnection();
	}

	/**
	 * @param host
	 *            modifica o host onde o socket se conecta. Se o valor for
	 *            modificado enquanto uma conexão está ativa, ela é terminada e
	 *            uma nova se inicia.
	 */
	public final void setHost(ServerName host) {
		if (host == null)
			throw new InvalidParameterException("invalid host.");
		if (getServerName().equals(host))
			return;
		if (getSocket() != null && getSocket().isConnected())
			try {
				getSocket().disconnect();
			} catch (IOException e) {
				Logger.getLogger(getClass()).error(e.getMessage(), e);
			}
		this.serverName = host;
	}

	/**
	 * @param port
	 *            modifica a porta onde o socket se conecta. Se o valor for
	 *            modificado enquanto uma conexão está ativa, ela é terminada e
	 *            uma nova se inicia.
	 */
	public final void setPort(ServerPort port) {
		if (port == null)
			throw new InvalidParameterException("invalid port");
		if (getServerPort().equals(port))
			return;
		if (getSocket() != null && getSocket().isConnected())
			try {
				socket.disconnect();
			} catch (IOException e) {
				Logger.getLogger(getClass()).error(e.getMessage(), e);
			}
		this.serverPort = port;
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
