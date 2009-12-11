package br.com.gennex.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Observable;

import org.apache.log4j.Logger;

import br.com.gennex.interfaces.TcpController;
import br.com.gennex.interfaces.TcpMessage;
import br.com.gennex.socket.tcpcommand.messages.responses.FppsResponse;

public abstract class Socket extends Observable implements Runnable,
		TcpController {

	public class EventConnected {

	}

	public class EventDisconnected {

	}

	public interface TcpMessageFilter {
		public boolean accept(String message);
	}

	protected java.net.Socket rawSocket;

	private boolean connected = true;

	private BufferedReader bufferedreader;
	private PrintWriter printwriter;

	private TcpMessageFilter inputFilter = null;
	private TcpMessageFilter outputFilter = null;

	/**
	 * @param socket
	 *            o Socket da conexão que será gerenciado.
	 */
	public Socket(java.net.Socket socket) {
		super();
		this.rawSocket = socket;
		Logger.getLogger(getClass()).info(
				new StringBuffer("Connect from ").append(socket
						.getInetAddress().getHostName()));
		try {
			bufferedreader = new BufferedReader(new InputStreamReader(
					this.rawSocket.getInputStream()));
			printwriter = new PrintWriter(this.rawSocket.getOutputStream(),
					true);
		} catch (IOException e) {
			Logger.getLogger(getClass()).error(e.getMessage(), e);
		}
	}

	/**
	 * Desconecta o socket.
	 * 
	 * @throws IOException
	 */
	public void disconnect() throws IOException {
		this.rawSocket.close();
	}

	private synchronized TcpMessageFilter getInputFilter() {
		return inputFilter;
	}

	private synchronized TcpMessageFilter getOutputFilter() {
		return outputFilter;
	}

	/**
	 * Devolve o OutputStream do socket conectado.
	 * 
	 * @return o OutputStream do socket conectado.
	 * @throws IOException
	 */
	public OutputStream getSocketOutputStream() throws IOException {
		return rawSocket.getOutputStream();
	}

	/**
	 * Devolve a situação atual da conexão do socket.
	 * 
	 * @return true se conectado, false caso desconectado.
	 */
	public boolean isConnected() {
		return connected;
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
		if (filter != null && filter.accept(message))
			Logger.getLogger(getClass()).info(message);
		else if (Logger.getLogger(getClass()).isDebugEnabled())
			Logger.getLogger(getClass()).debug(message);
	}

	/**
	 * Loga uma mensagem recebida.
	 * 
	 * @param message
	 *            the received message
	 */
	private void logReceived(String message) {
		logMessage(getInputFilter(), new StringBuffer("Received: ").append(
				message).toString());
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

	private void raiseEvent(Object event) {
		try {
			setChanged();
			notifyObservers(event);
		} catch (Exception e) {
			Logger.getLogger(getClass()).error(e.getMessage(), e);
		}

	}

	private void notifyConnection() {
		raiseEvent(new EventConnected());
	}

	private void notifyDisconnection() {
		raiseEvent(new EventDisconnected());
	}

	protected abstract void processStringRequest(String s) throws Exception;

	public void run() {
		socketLifeCycle();
	}

	private void send(String message) {

		logSent(message);

		try {
			printwriter.print(message + "\r\n");
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

	public synchronized void setInputFilter(TcpMessageFilter inputFilter) {
		this.inputFilter = inputFilter;
	}

	public synchronized void setOutputFilter(TcpMessageFilter outputFilter) {
		this.outputFilter = outputFilter;
	}

	private void socketLifeCycle() {
		notifyConnection();
		sendHello();
		try {
			waitLines();
		} catch (SocketException e) {
			if (Logger.getLogger(getClass()).isDebugEnabled())
				Logger.getLogger(getClass()).debug(e.getMessage());
		} catch (SocketTimeoutException e) {
			Logger.getLogger(getClass()).warn(e.getMessage());
		} catch (IOException e) {
			Logger.getLogger(getClass()).error(e.getMessage(), e);
		} finally {
			Logger.getLogger(getClass()).info(
					new StringBuffer("Disconnect from ").append(rawSocket
							.getInetAddress().getHostName()));
			connected = false;
			notifyDisconnection();
			try {
				disconnect();
			} catch (IOException e) {
				Logger.getLogger(getClass()).error(e.getMessage(), e);
			}
		}
	}

	private void waitLines() throws IOException {
		do {
			String s = bufferedreader.readLine();
			if (s == null)
				break;

			logReceived(s);

			try {
				processStringRequest(s);
			} catch (Exception e) {
				Logger.getLogger(getClass()).error(e.getMessage(), e);
			}
		} while (Thread.currentThread().isAlive());
	}

	protected java.net.Socket getRawSocket() {
		return this.rawSocket;
	}

	public java.net.InetAddress getInetAddress() {
		return getRawSocket().getInetAddress();
	}
}
