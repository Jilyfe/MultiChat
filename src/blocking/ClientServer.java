package blocking;

import java.io.IOException;
import java.net.Socket;


public class ClientServer implements Runnable
{
	private Server server;
	private Socket client;
	
	private String bufferString;
	private char bufferChar;
	
	private String name;
	
	public ClientServer(Server server, Socket client)
	{
		this.server = server;
		this.client = client;
		name = "";
	}
	
	public void run()
	{
		int i;
		try
		{
			server.write(client.getOutputStream(), "bienvenue\n", "un client s'est connecte au chat\n");
		
			do
			{
				bufferString = "";
				do
				{
					bufferChar = (char) client.getInputStream().read();
					bufferString += bufferChar;
				}
				while(bufferChar != '\n');
				
				if(bufferString.startsWith("/nick "))
				{
					System.out.println(bufferString);
					name = "";
					for(i=6; i<bufferString.length() - 1; i++)
					{
						name += bufferString.charAt(i);
					}
				}
				else
				{
					if(bufferString.equals("exit\n"))
					{
						if(name.equals(""))
						{
							server.write(client.getOutputStream(), "au revoir " + name + "\n", "un client s'est deconnecte\n");
						}
						else
						{
							server.write(client.getOutputStream(), "au revoir " + name + "\n", name + " s'est deconnecte\n");
						}
						bufferString = "";
					}
					else
					{
						if(!name.equals(""))
							bufferString = name + " : " + bufferString;
						
						server.write(client.getOutputStream(), null, bufferString);
						System.out.print(bufferString);
					}
				}
			}
			while(!bufferString.equals(""));
			server.removeClient(client.getOutputStream());
			client.close();
			System.out.println("client ferme");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
