package br.com.gennex.socket;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import br.com.gennex.socket.model.ServerNameTest;
import br.com.gennex.socket.model.ServerPortTest;
import br.com.gennex.socket.tcpcommand.FppsTcpCommandSocketTest;
import br.com.gennex.socket.tcpcommand.HttpTcpCommandSocketTest;
import br.com.gennex.socket.tcpcommand.messages.FppsMessageTest;
import br.com.gennex.socket.tcpcommand.messages.HttpMessageTest;

@RunWith(Suite.class)
@Suite.SuiteClasses( { ServerNameTest.class, ServerPortTest.class,
		FppsTcpCommandSocketTest.class, HttpTcpCommandSocketTest.class,
		FppsMessageTest.class, HttpMessageTest.class, SocketTest.class })
public class AllTests {

}
