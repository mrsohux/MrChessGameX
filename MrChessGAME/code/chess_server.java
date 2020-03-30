import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.Thread;


public class chess_server 
{
		
	public static void main(String args[])throws IOException
	{
		PrintWriter out ,out1;
		BufferedReader in,in1;
		ServerSocket server;
		Socket s,s1;
		String data="";
/*System.out.println("Day" +tdate.get(java.util.Calendar.DATE)+"<BR>");*/
			
			server =new ServerSocket(4000,2);
		
			System.out.println("Waiting.....");
			s=server.accept();
			System.out.println("Connection from"+s.getInetAddress().getHostName());
			s1=server.accept();
			System.out.println("\nAnother Connection from"+s1.getInetAddress().getHostName());		

			conductGame game1=new conductGame(s,s1);		
			conductGame game2=new conductGame(s1,s);
		
		
			out=new PrintWriter(s.getOutputStream(),true);
			in=new BufferedReader(new InputStreamReader(s.getInputStream()));
			out1=new PrintWriter(s1.getOutputStream(),true);
			in1=new BufferedReader(new InputStreamReader(s1.getInputStream()));
	
			out1.println(in.readLine());
			out.println(in1.readLine());
			out1.println("1");
			out.println("0");
		
			game1.start();	
			game2.start();
	
	}
}
			
	
class conductGame extends Thread //implements Runnable
{
		private PrintWriter out;
		private BufferedReader in;
		private Socket soc1,soc2;
		private String data="";
		
	public conductGame(Socket soc1,Socket soc2)
	{
		this.soc1=soc1;
		this.soc2=soc2;	
	}
	
	public void run()
	{
		String msg="";
		try
		{
			out=new PrintWriter(soc2.getOutputStream(),true);
			in=new BufferedReader(new InputStreamReader(soc1.getInputStream()));
		
			while (true)
			{
				try
				{
					if (in.ready())
					{
							msg=in.readLine();
							out.println(msg);
							System.out.println("\n"+msg);
							msg="";
					}
				}
					
				catch(IOException e)
				{
					System.out.println("Unknown type");
					break;
				}
			}
			System.out.println("Connection terminated");
			out.close();
			in.close();
			soc1.close();
			soc2.close();
		}
		catch(IOException ioe)
		{
		
		}
		
	}
	
}
