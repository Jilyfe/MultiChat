package blocking;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server
{
	private InetAddress address;
	private int port;
	
	private ServerSocket server;
	private Socket client;
	
	private ArrayList<OutputStream> connections;
	
	public Server(InetAddress address, int port)
	{
		this.address = address;
		this.port = port;
	}
	
	public void start() throws IOException
	{
		server = new ServerSocket();
		server.bind(server.getLocalSocketAddress(), port);
		
		connections = new ArrayList<OutputStream>();

		System.out.println("inet address: " + address);
		System.out.println("port: " + port);
		System.out.println("server address: " + server.getLocalSocketAddress());
		
		// boucle d'acceptation de client
		while(true)
		{
			System.out.println("attente de client");
			client = server.accept();
			System.out.println("client accepte");
			
			connections.add(client.getOutputStream());
			
			// création de thread géreant le client
			new Thread(new ClientServer(this, client)).start();
		}
		//server.close();
	}
	
	public synchronized void removeClient(OutputStream client)
	{
		connections.remove(client);
	}
	
	// envoie un message à un client
	public synchronized void write(OutputStream client, String clientMessage, String othersMessage) throws IOException
	{
		int i;
		for(OutputStream stream : connections)
		{
			if(stream == client)
			{
				if(clientMessage != null)
				{
					for(i=0; i<clientMessage.length(); i++)
					{
						stream.write(clientMessage.charAt(i));
					}
				}
			}
			else
			{
				if(othersMessage != null)
				{
					for(i=0; i<othersMessage.length(); i++)
					{
						stream.write(othersMessage.charAt(i));
					}
				}
			}
		}
	}
}