package mycatDemo;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class NIOClient implements Runnable {
	
	private BlockingQueue<String> words;
	private Random random;
	
	public static void main(String[] args) {
		
	}

	@Override
	public void run() {
		
		Selector selector=null;
		SocketChannel channel=null;
		
		try {
			selector=Selector.open();
			channel=SocketChannel.open();
			channel.configureBlocking(false);
			channel.connect(new InetSocketAddress("127.0.0.1", 7777));
			channel.register(selector, SelectionKey.OP_CONNECT);
			while(true) {
				selector.select();
				Iterator ite=selector.selectedKeys().iterator();
				while(ite.hasNext()) {
					SelectionKey key=(SelectionKey) ite.next();
				     ite.remove();
					if(key.isConnectable()) {
					   if(channel.isConnectionPending()) {
						   if(channel.finishConnect()) {
							   key.interestOps(SelectionKey.OP_READ);
							   channel.write(CharsetHelper.encoder(CharBuffer.wrap(getWord())));
							   sleep();
						   }else {
							   key.cancel();
						   }
						   
					   }	
					}
					if(key.isReadable()) {
						ByteBuffer buff=ByteBuffer.allocate(1024);
						channel.read(buff);
						buff.flip();
						CharBuffer charBuff=CharsetHelper.decoder(buff);
						String answer = charBuff.toString();   
                        System.out.println(Thread.currentThread().getId() + "---" + answer);  
                           
                        char[] word = getWord();  
                        if(word != null){  
                            channel.write(CharsetHelper.encoder(CharBuffer.wrap(word)));  
                        }  
					}
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		
	}

	private void sleep() {
		// TODO Auto-generated method stub
		
	}

	private char[] getWord() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
