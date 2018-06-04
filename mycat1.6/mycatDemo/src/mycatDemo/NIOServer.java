package mycatDemo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharsetEncoder;
import java.util.Iterator;

public class NIOServer {
	
	private ByteBuffer readBuff;
	private Selector selector;
	
	public static void main(String[] args) {
		
	}
	
	private void init() throws IOException {
		
		readBuff=ByteBuffer.allocate(1024);
		selector=Selector.open();
		
		ServerSocketChannel server=ServerSocketChannel.open();
		server.configureBlocking(false);

		server.socket().bind(new InetSocketAddress("127.0.0.1", 7777));
		
		server.register(selector, SelectionKey.OP_ACCEPT);
	}
	
	private void listen() throws IOException {
		
		while(true) {
			selector.select();
			Iterator ite=selector.selectedKeys().iterator();
			while(ite.hasNext()) {
				SelectionKey key=(SelectionKey) ite.next();
				ite.remove();
				handkey(key);
			}
			
			
		}
		
		
	}

	private void handkey(SelectionKey key) throws IOException {
		
		SocketChannel channel=null;
		
		if(key.isAcceptable()) {
			ServerSocketChannel server=(ServerSocketChannel) key.channel();
			channel=server.accept();
			channel.configureBlocking(false);
			channel.register(selector, SelectionKey.OP_READ);
		}
		if(key.isReadable()) {
			channel=(SocketChannel) key.channel();
			int count=channel.read(readBuff);
			if(count>0) {
				readBuff.flip();
				CharBuffer charBuff=CharsetHelper.decoder(readBuff);
				String question=charBuff.toString();
				String answer=getAnswer(question);
				channel.write(CharsetHelper.encoder(charBuff.wrap(answer)));
			}
			
			
		}
	}

	private String getAnswer(String question) {
		String answer=null;
		switch (question) {
		case "who":
			answer="我是who";
			break;

		default:
			answer="请输入";
			break;
		}
		
		
		return answer;
	}

}
