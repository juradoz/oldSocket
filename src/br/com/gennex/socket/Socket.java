package br.com.gennex.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.SocketException;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import br.com.gennex.interfaces.TcpController;
import br.com.gennex.interfaces.TcpResponse;

public abstract class Socket implements Runnable, TcpController {

	public interface TcpMessageFilter {
		public boolean accept(String message);
	}

	private static LinkedList<Socket> sockets = new LinkedList<Socket>();

	protected java.net.Socket socket;

	private boolean connected = true;
	private BufferedReader bufferedreader;
	private PrintWriter printwriter;

	private TcpMessageFilter inputFilter = null;

	private TcpMessageFilter outputFilter = null;

	public Socket(java.net.Socket conexao) {
		super();
		this.socket = conexao;
		Logger.getLogger(getClass()).info(
				new StringBuffer("Connect from ").append(conexao
						.getInetAddress().getHostName()));
		try {
			bufferedreader = new BufferedReader(new InputStreamReader(
					this.socket.getInputStream()));
			printwriter = new PrintWriter(this.socket.getOutputStream(), true);
		} catch (IOException e) {
			Logger.getLogger(getClass()).error(e.getMessage(), e);
		}
	}

	private void addSocket(Socket e) {
		sockets.add(e);
	}

	public void disconnect() throws IOException {
		socket.close();
	}

	private TcpMessageFilter getInputFilter() {
		return inputFilter;
	}

	private TcpMessageFilter getOutputFilter() {
		return outputFilter;
	}

	public OutputStream getSocketOutputStream() throws IOException {
		return socket.getOutputStream();
	}

	public boolean isConnected() {
		return connected;
	}

	protected abstract void processStringRequest(String s) throws Exception;

	private void removeSocket(Socket e) {
		sockets.remove(e);
	}

	public void run() {
		addSocket(this);
		try {
			do {
				String s = bufferedreader.readLine();
				if (s == null)
					break;
				if (getInputFilter() == null || getInputFilter().accept(s)) {
					Logger.getLogger(getClass()).info("Received: " + s);
				}
				try {
					processStringRequest(s);
				} catch (Exception e) {
					Logger.getLogger(getClass()).error(e.getMessage(), e);
				}
			} while (Thread.currentThread().isAlive());
		} catch (SocketException e) {
			// ignore
		} catch (IOException e) {
			Logger.getLogger(getClass()).error(e.getMessage(), e);
		} finally {
			Logger.getLogger(getClass()).info(
					new StringBuffer("Disconnect from ").append(socket
							.getInetAddress().getHostName()));
			connected = false;
			removeSocket(this);
		}

	}

	public void send(Object obj) {
		send(obj.toString());
	}

	public void send(String message) {
		if (getOutputFilter() == null || getOutputFilter().accept(message)) {
			Logger.getLogger(getClass()).info("Send: " + message);
		}
		try {
			printwriter.print(message + "\r\n");
			printwriter.flush();
		} catch (Exception e) {
			Logger.getLogger(getClass()).error(e.getMessage(), e);
		}
	}

	public void send(TcpResponse response) {
		send(response.toString());
	}

	public void setInputFilter(TcpMessageFilter inputFilter) {
		this.inputFilter = inputFilter;
	}

	public void setOutputFilter(TcpMessageFilter outputFilter) {
		this.outputFilter = outputFilter;
	}
}
