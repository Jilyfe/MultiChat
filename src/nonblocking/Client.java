package nonblocking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;


public class Client
{
	private String group;
	private int port;
	
	private SocketChannel socketChannel;
	private SocketAddress address;
	
	public Client(String group, int port) throws IOException, InterruptedException
	{
		this.group = group;
		this.port = port;
	}
	
	public void start() throws IOException
	{
		address = new InetSocketAddress(group, port);
		
		socketChannel = SocketChannel.open(address);
		socketChannel.configureBlocking(false);
		
		String msg;
		char c;
		
		do
		{
			msg = "";
			do
			{
				c = (char) System.in.read();
				msg += c;
			}
			while(c != '\n');
			
			send(msg);
		}
		while(!msg.equals("exit\n"));
	}
	
	public void send(String msg) throws IOException
	{
    	ByteBuffer buffer = null;
    	
		buffer = ByteBuffer.wrap(msg.getBytes());
		socketChannel.write(buffer);
		buffer.clear();
	}
}