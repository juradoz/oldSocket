package br.com.gennex.socket.tcpcommand;

import java.util.HashMap;

import org.apache.log4j.Logger;

import br.com.gennex.interfaces.TcpRequest;
import br.com.gennex.interfaces.TcpRequestCommand;
import br.com.gennex.interfaces.TcpRequestHandler;
import br.com.gennex.interfaces.TcpResponse;
import br.com.gennex.socket.tcpcommand.messages.requests.FppsRequestCommand;
import br.com.gennex.socket.tcpcommand.messages.requests.InvalidTcpRequest;
import br.com.gennex.socket.tcpcommand.messages.requests.InvalidTcpRequestHandler;
import br.com.gennex.socket.tcpcommand.messages.responses.FppsErrorResponse;

public class TcpCommandSocket extends br.com.gennex.socket.Socket {

	private HashMap<String, TcpRequestHandler> handlerList = new HashMap<String, TcpRequestHandler>();

	public TcpCommandSocket(java.net.Socket socket) {
		super(socket);
		addHandler(new InvalidTcpRequest(), new InvalidTcpRequestHandler());
	}

	public void addHandler(TcpRequest request, TcpRequestHandler requestHandler) {
		handlerList.put(request.toString(), requestHandler);
	}

	public void clearHandlers() {
		handlerList.clear();
	}

	private TcpResponse handleInvalidRequest(TcpRequest request,
			UnsupportedOperationException e) {
		TcpResponse response;
		if (Logger.getLogger(getClass()).isDebugEnabled())
			Logger.getLogger(getClass()).debug(
					new StringBuffer("Invalid request: ").append(request
							.getTcpMessage()));
		response = new FppsErrorResponse(request, e);
		return response;
	}

	private TcpResponse handleRequestException(TcpRequest request, Exception e) {
		TcpResponse response;
		Logger.getLogger(getClass()).error(e.getMessage(), e);
		response = new FppsErrorResponse(request, e);
		return response;
	}

	public TcpResponse processRequest(TcpRequestCommand request) {
		String strRequest = request.getCommand().toUpperCase();
		if (!handlerList.containsKey(strRequest))
			throw new UnsupportedOperationException();

		TcpRequestHandler handler = handlerList.get(strRequest);

		TcpResponse response = null;

		try {
			response = handler.process(this, request);
		} catch (UnsupportedOperationException e) {
			response = handleInvalidRequest(request, e);
		} catch (Exception e) {
			response = handleRequestException(request, e);
		}

		return response;
	}

	@Override
	protected void processStringRequest(String s) throws Exception {
		TcpResponse response = null;
		TcpRequestCommand request = new FppsRequestCommand(s);
		try {
			response = processRequest(request);
		} catch (UnsupportedOperationException e) {
			response = handleInvalidRequest(request, e);
		} catch (Exception e) {
			response = handleRequestException(request, e);
			Logger.getLogger(getClass()).error(e.getMessage(), e);
		} finally {
			if (response == null)
				return;
			send(response);
		}
	}

	public TcpRequestHandler removeHandler(TcpRequest request) {
		return handlerList.remove(request.toString());
	}

}
