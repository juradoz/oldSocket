package br.com.gennex.socket;

import junit.framework.Test;
import junit.framework.TestSuite;
import br.com.gennex.socket.model.ServerNameTest;
import br.com.gennex.socket.model.ServerPortTest;
import br.com.gennex.socket.tcpcommand.FppsTcpCommandSocketTest;
import br.com.gennex.socket.tcpcommand.HttpTcpCommandSocketTest;
import br.com.gennex.socket.tcpcommand.messages.FppsMessageTest;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for br.com.gennex.socket");
		suite.addTestSuite(FppsMessageTest.class);
		suite.addTestSuite(FppsTcpCommandSocketTest.class);
		suite.addTestSuite(HttpTcpCommandSocketTest.class);
		suite.addTestSuite(ServerNameTest.class);
		suite.addTestSuite(ServerPortTest.class);
		return suite;
	}

}
