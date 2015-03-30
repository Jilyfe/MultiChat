package multichat;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

import java.io.IOException;
import java.net.InetAddress;

import blocking.Server;


public class Main
{
	public static void main(String[] args) throws IOException
	{
		String ip = "0.0.0.0";
		int port = 65535;
		boolean d = false, h = false, m = false, n = false, s = false, c = false;
		
		LongOpt[] longopts  = new LongOpt[8];
		StringBuffer sb = new StringBuffer();
		longopts[0] = new LongOpt("address", LongOpt.REQUIRED_ARGUMENT, sb, 'a');
		longopts[1] = new LongOpt("debug", LongOpt.NO_ARGUMENT, null, 'd');
		longopts[2] = new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h');
		longopts[3] = new LongOpt("multicast", LongOpt.NO_ARGUMENT, null, 'm');
		longopts[4] = new LongOpt("nio", LongOpt.NO_ARGUMENT, null, 'n');
		longopts[5] = new LongOpt("port", LongOpt.REQUIRED_ARGUMENT, sb, 'p');
		longopts[6] = new LongOpt("server", LongOpt.NO_ARGUMENT, null, 's');
		longopts[7] = new LongOpt("client", LongOpt.NO_ARGUMENT, null, 'c');
		Getopt g = new Getopt("ClientSelector", args, "a:dhmnp:sc", longopts);
		int choix;
		while((choix = g.getopt()) != -1)
		{
			switch(choix)
			{
			case 'a':
				ip = g.getOptarg();
				break;
			case 'd':
				d = true;
				break;
			case 'h':
				h = true;
				break;
			case 'm':
				m = true;
				break;
			case 'n':
				n = true;
				break;
			case 'p':
				port = Integer.parseInt(g.getOptarg());
				break;
			case 's':
				s = true;
				break;
			case 'c':
				c = true;
				break;
			default:
				System.out.println("Invalid option");
			}
		}
		
		if(h)
		{
			System.out.println("appuyez sur entrée pour continuer");
			while(System.in.read() != '\n') ;
		}
		
		if(n)
		{
			if(s)
			{
				
			}
			
			if(c)
			{
				
			}
		}
		else
		{
			if(s)
			{
				blocking.Server server = new Server(InetAddress.getLocalHost(), 80);
				server.start();
			}
			
			if(c)
			{
				// implémenter client
			}
		}
	}
}
