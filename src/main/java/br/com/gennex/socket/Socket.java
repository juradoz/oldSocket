package br.com.gennex.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Calendar;
import java.util.Observable;

import org.apache.log4j.Logger;

import br.com.gennex.interfaces.TcpController;
import br.com.gennex.interfaces.TcpMessage;
import br.com.gennex.socket.tcpcommand.messages.responses.FppsResponse;

public abstract class Socket extends Observable implements Runnable,
		TcpController {

	public static final class EventConnected {

	}

	public static final class EventDisconnected {

	}

	public interface TcpMessageFilter {
		public boolean accept(String message);
	}

	protected java.net.Socket rawSocket = null;

	private BufferedReader bufferedreader;
	private PrintWriter printwriter;

	private TcpMessageFilter inputFilter = null;
	private TcpMessageFilter outputFilter = null;

	/**
	 * @param socket
	 *            o Socket da conexao que sera gerenciado.
	 */
	public Socket(java.net.Socket socket) {
		super();
		if (!socket.isConnected())
			return;
		setRawSocket(socket);
		if (socket.getInetAddress() != null)
			Logger.getLogger(getClass()).info(
					new StringBuffer(String.format("Connect from %s", socket
							.getInetAddress().getHostName())));
		try {
			bufferedreader = new BufferedReader(new InputStreamReader(
					getRawSocket().getInputStream()));
			printwriter = new PrintWriter(getRawSocket().getOutputStream(),
					true);
		} catch (IOException e) {
			Logger.getLogger(getClass()).error(e.getMessage(), e);
		}
	}

	boolean canLog(TcpMessageFilter filter, String message) {
		return filter == null || filter.accept(message);
	}

	/**
	 * Desconecta o socket.
	 * 
	 * @throws IOException
	 */
	public void disconnect() throws IOException {
		if (getRawSocket() == null)
			return;
		getRawSocket().close();
		setRawSocket(null);
	}

	public java.net.InetAddress getInetAddress() {
		if (getRawSocket() == null)
			return null;
		return getRawSocket().getInetAddress();
	}

	private TcpMessageFilter getInputFilter() {
		return inputFilter;
	}

	private TcpMessageFilter getOutputFilter() {
		return outputFilter;
	}

	protected java.net.Socket getRawSocket() {
		return this.rawSocket;
	}

	/**
	 * Devolve o OutputStream do socket conectado.
	 * 
	 * @return o OutputStream do socket conectado.
	 * @throws IOException
	 */
	public OutputStream getSocketOutputStream() throws IOException {
		if (getRawSocket() == null)
			return null;
		return getRawSocket().getOutputStream();
	}

	/**
	 * Devolve a situacao atual da conexao do socket.
	 * 
	 * @return true se conectado, false caso desconectado.
	 */
	public boolean isConnected() {
		return getRawSocket() != null && getRawSocket().isConnected();
	}

	/**
	 * Loga uma mensagem, respeitando um determinado filtro.
	 * 
	 * @param filter
	 *            the filter to be applied
	 * @param message
	 *            the message to log
	 */
	private void logMessage(TcpMessageFilter filter, String message) {
		if (!canLog(filter, message)) {
			if (Logger.getLogger(getClass()).isDebugEnabled())
				Logger.getLogger(getClass()).debug(message);
			return;
		}
		Logger.getLogger(getClass()).info(message);
	}

	/**
	 * Loga uma mensagem recebida.
	 * 
	 * @param message
	 *            the received message
	 */
	private void logReceived(String message) {
		logMessage(getInputFilter(),
				new StringBuffer("Received: ").append(message).toString());
	}

	/**
	 * Loga uma mensagem enviada.
	 * 
	 * @param message
	 *            the sent message
	 */
	private void logSent(String message) {
		logMessage(getOutputFilter(), new StringBuffer("Sent: ")
				.append(message).toString());
	}

	private void notifyConnection() {
		raiseEvent(new EventConnected());
	}

	private void notifyDisconnection() {
		raiseEvent(new EventDisconnected());
	}

	protected abstract void processStringRequest(String s) throws Exception;

	private void raiseEvent(Object event) {
		try {
			setChanged();
			notifyObservers(event);
		} catch (Exception e) {
			Logger.getLogger(getClass()).error(e.getMessage(), e);
		}

	}

	public void run() {
		socketLifeCycle();
	}

	private String delimitadorEnvio = "\r\n";

	private void send(String message) {

		logSent(message);

		try {
			printwriter.print(message + delimitadorEnvio);
			printwriter.flush();
		} catch (Exception e) {
			Logger.getLogger(getClass()).error(e.getMessage(), e);
		}
	}

	public void send(TcpMessage response) {
		send(response.toString());
	}

	private void sendHello() {
		send(new FppsResponse(getClass().getSimpleName().concat("()")));
	}

	public void setInputFilter(TcpMessageFilter inputFilter) {
		this.inputFilter = inputFilter;
	}

	public void setOutputFilter(TcpMessageFilter outputFilter) {
		this.outputFilter = outputFilter;
	}

	private void setRawSocket(java.net.Socket rawSocket) {
		this.rawSocket = rawSocket;
	}

	private boolean sendHello = true;

	private void socketLifeCycle() {
		notifyConnection();
		if (isSendHello())
			sendHello();
		try {
			waitLines();
			Logger.getLogger(getClass()).info("Disconnected!");
		} catch (SocketException e) {
			Logger.getLogger(getClass()).warn(e.getMessage());
		} catch (SocketTimeoutException e) {
			Logger.getLogger(getClass()).warn(e.getMessage());
		} catch (IOException e) {
			Logger.getLogger(getClass()).error(e.getMessage(), e);
		} finally {
			if (getRawSocket() != null)
				Logger.getLogger(getClass()).info(
						new StringBuffer("Disconnect from ")
								.append(getRawSocket().getInetAddress()
										.getHostName()));
			try {
				disconnect();
				notifyDisconnection();
			} catch (IOException e) {
				Logger.getLogger(getClass()).error(e.getMessage(), e);
			}
		}
	}

	private boolean isSendHello() {
		return sendHello;
	}

	private void waitLines() throws IOException {
		do {
			String s = bufferedreader.readLine();
			if (s == null)
				break;

			logReceived(s);

			try {
				Calendar start = Calendar.getInstance();
				processStringRequest(s);
				Calendar end = Calendar.getInstance();
				if (end.getTimeInMillis() - start.getTimeInMillis() > 1000)
					Logger.getLogger(getClass()).info(
							String.format("%s accept runned for %dms", s,
									Calendar.getInstance().getTimeInMillis()
											- start.getTimeInMillis()));
			} catch (Exception e) {
				Logger.getLogger(getClass()).error(e.getMessage(), e);
			}
		} while (Thread.currentThread().isAlive());
	}

	protected void setSendHello(boolean sendHello) {
		this.sendHello = sendHello;
	}

	protected void setDelimitadorEnvio(String delimitadorEnvio) {
		this.delimitadorEnvio = delimitadorEnvio;
	}
}
