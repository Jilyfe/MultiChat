package multicast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Server extends Thread
{
	private String address;
	private InetAddress group;
	private int port;
	
	private MulticastSocket socket;
	private byte[] input, output;
	private DatagramPacket msg;

	ByteArrayOutputStream sortie;
	
	view.ChatView view;
	
	public Server(String address, int port, view.ChatView view) throws IOException
	{
		this.address = address;
		this.port = port;
		this.view = view;
	}
	
	@Override
	public void run()
	{
		String bufferString = null;
		sortie = new ByteArrayOutputStream();
		try
		{
			group = InetAddress.getByName(address);
			socket = new MulticastSocket(port);
			socket.joinGroup(group);
			
			// attente d'un message
			do
			{
				input = new byte[1024];
				msg = new DatagramPacket(input, input.length);
				try
				{
					socket.receive(msg);
					bufferString = (new DataInputStream(new ByteArrayInputStream(input))).readUTF();
					view.addMessage(bufferString);
				}
				catch(Exception e)
				{
					System.out.println(e);
				}
			}
			while(!bufferString.equals("exit\n"));
			socket.close();
		}
		catch (IOException e)
		{
			System.out.println(e);
		}
	}
	
	// envoie d'un message
	public void sendMessage(String message) throws IOException
	{
		sortie = new ByteArrayOutputStream();
		new DataOutputStream(sortie).writeUTF(message);
		output = sortie.toByteArray();
		
		msg = new DatagramPacket(output, output.length, group, port);
		socket.send(msg);
	}
}