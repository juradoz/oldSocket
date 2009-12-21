package br.com.gennex.socket;

import br.com.gennex.socket.model.ServerNameTest;
import br.com.gennex.socket.model.ServerPortTest;
import br.com.gennex.socket.tcpcommand.FppsTcpCommandSocketTest;
import br.com.gennex.socket.tcpcommand.HttpTcpCommandSocketTest;
import br.com.gennex.socket.tcpcommand.messages.FppsMessageTest;
import junit.framework.Test;

public class TestAll {
	public static Test suite() {
		junit.framework.TestSuite suite = new junit.framework.TestSuite(
				"Framework Socket");
		suite.addTestSuite(FppsMessageTest.class);
		suite.addTestSuite(FppsTcpCommandSocketTest.class);
		suite.addTestSuite(HttpTcpCommandSocketTest.class);
		suite.addTestSuite(ServerNameTest.class);
		suite.addTestSuite(ServerPortTest.class);
		return suite;
	}
}
