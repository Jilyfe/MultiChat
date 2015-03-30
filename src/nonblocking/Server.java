package nonblocking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;


public class Server implements Runnable
{
	private String address;
	private int port;
	
	private ServerSocketChannel channel;
	
	private Selector selector;
	
	public Server(String address, int port) throws IOException
	{
		this.address = address;
		this.port = port;
	}

	public void run()
	{
		ByteBuffer bbuf = ByteBuffer.allocate(8192);
		
		try
		{
			channel = ServerSocketChannel.open();
			channel.bind(new InetSocketAddress(address, port));
			
		    selector = Selector.open();
		    
		    channel.configureBlocking(false);
			
		    channel.register(selector, SelectionKey.OP_ACCEPT);
			
			while(true)
			{
				selector.select();
				Set<SelectionKey> keys = selector.selectedKeys();
				Iterator<SelectionKey> keyIterator = keys.iterator();
				// on parcourt les clients
				while(keyIterator.hasNext())
				{
					SelectionKey mykey = keyIterator.next();
					if(mykey.isAcceptable())
					{
						System.out.println(mykey + " accept");
						
						SocketChannel client = channel.accept();
						client.configureBlocking(false);
						client.register(selector, SelectionKey.OP_READ);
					}
					if(mykey.isReadable())
					{
						System.out.println(mykey + " read");
						
						SocketChannel client = ((SocketChannel) mykey.channel());
						client.read(bbuf);
						bbuf.flip();
					    
					    bbuf.compact();
					    
				    	client.register(selector, SelectionKey.OP_READ);
						//write(cbuf);
						//client.write(buf);
					}
					if(mykey.isWritable())
					{
						System.out.println(mykey + " write");
						
						SocketChannel client = ((SocketChannel) mykey.channel());
						client.read(bbuf);
						Charset charset = Charset.defaultCharset();
						bbuf.flip();
						CharBuffer cbuf = charset.decode(bbuf);

					    bbuf.compact();
					    
						System.out.println(cbuf.toString());
					    
						client.register(selector, SelectionKey.OP_READ);
					}
					keyIterator.remove();
				}
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}