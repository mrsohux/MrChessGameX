 
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.InetAddress;


import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;


import java.awt.Graphics;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;



import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.Box;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.RepaintManager;
import javax.swing.SizeRequirements;





//********************   Class  MyNetChess5  ***********************


public class MyNetChess5 extends JFrame //implements ActionListener
{


/*	
 				Decleration done here for 
 		
 		Box,Panel,Button,RadioButton,ButtonGroup,Textfield 
 		Label,Menu,MenuItem,ButtonGroup,
*/


private Box box[],box1,box2;
private JPanel panel1,panel2,panel3,panel4;
private JButton  connect,play,send,Reset;
private JRadioButton local,remote;
private ButtonGroup radio;
private JTextField pl1,pl2,prompt,sendtext;
private JLabel label1,label2,label3,label4,label5,chatl,msgl,sendl; 
private JMenu filemenu,customize,castling,promotion,help;
private JMenuItem rscastle,lscastle, queen,rook,knight,bishop;
private ButtonGroup piece_group;


/*
				Decleation done here for
				private JManueBar

 	TextArea,ScrollPane,Flowlayout,GridBagLayout,GridBagConstraints
*/


private JTextArea msgg,chat;
private JScrollPane scroller,scroller1;
private FlowLayout layout ;
private GridBagLayout gbl;
private GridBagConstraints gblconst;


/*
			DECLERATION DONE FOR VARIOUS KINDS IF CLASSES AND 
			DECLERATION FOR THEIR OBJECTS DONE HERE ..
*/


private chesspiece piece[][];
private game chess;
private rook wr,br;
private pawn wp,bp;
private king wk,bk;
private queen wq,bq;
private knight wkn,bkn;
private bishop wb,bb;
private ImageIcon bbishop,wbishop,bking,wking,brook,wrook,bpawn,wpawn,bknight,wknight,bqueen,wqueen;

private int curplayer=0,bpromx=-1,bpromy=-1,wpromx=-1,wpromy=-1;
private int bkx,bky,wkx,wky;
private boolean castle_w=true,castle_b=true;
private int posx,posy,movx,movy,netmove,flag=0,savemove;




/*
		//...........NETWORKING............ 

		Network relaited Decleration Done Here
*/


private BufferedReader in;
private String msg="",msg1,ipaddress="";
private Socket s;
private PrintWriter out;
private int move=-1;
private boolean Connected=false,isRemote=false,isConnected=false;
private String namel,namer;



//********************  Constructor MyNetChess5  ***********************


public MyNetChess5()
{

	super("INTRANET  CHESS  GAME");
	Container con=getContentPane();
	
	
//SETTING UP MENUBAR.................................

	
	JMenuBar bar=new JMenuBar();
	setJMenuBar(bar);
	
	filemenu=new JMenu("File");
	bar.add(filemenu); 
	

	
	castling=new JMenu("Castling");
	bar.add(castling);
	
	promotion=new JMenu("Promotion");
	bar.add(promotion);
	
	help=new JMenu("Help");
	bar.add(help);
	
	JMenuItem help1=new JMenuItem("Chess Help");
	help.add(help1);
	
	JMenuItem exit=new JMenuItem("Exit  X");
	exit.setMnemonic('X');
	JMenuItem about=new JMenuItem("About   A");
	
	filemenu.add(exit);
	exit.addActionListener(
	new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			if (JOptionPane.showConfirmDialog(null,"Are You Sure You Want To Exit ? ","Confirmation",JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE)==0)
			{
				System.exit(0);
				try
				{
					if (s!=null)
					s.close();
				}
				catch(IOException ooo)
				{ }
			}
		}
	}
	);
	about.addActionListener(
	new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
					JOptionPane.showMessageDialog(null,"  This Is A Chess Game \n           Developed By \n         Aniket R Koshe.\n          Vitthal S Kudal.","Credits",JOptionPane.INFORMATION_MESSAGE);			
		}
	}
	);
	

	queen=new JMenuItem("Queen");
	rook=new JMenuItem("Rook");
	knight=new JMenuItem("Knight");
	bishop=new JMenuItem("Bishop");
	
	
	promotion.add(queen);
	promotion.add(rook);
	promotion.add(knight);
	promotion.add(bishop);


	rscastle=new JMenuItem("Right side castling");
	lscastle=new JMenuItem("Left side castling");
	castling.add(rscastle);
	castling.add(lscastle);

	
//SETTING UP OTHER GUI ELEMENTS................................	
	
	
	box=new Box[2];
	box[0]=Box.createVerticalBox();
	box[1]=Box.createVerticalBox();
	
	box1=Box.createHorizontalBox();
	box2=Box.createVerticalBox();
	
	panel1=new JPanel();
	layout=new FlowLayout();
	panel1.setLayout(layout);
	layout.setAlignment(FlowLayout.LEFT);
	
	panel2=new JPanel();
	panel2.setLayout(layout);
	layout.setAlignment(FlowLayout.LEFT);

	panel3=new JPanel();
	panel3.setLayout(layout);
	layout.setAlignment(FlowLayout.LEFT);


	local=new JRadioButton("Local",true);
	local.setMnemonic('l');
	remote =new JRadioButton("Remote",false);
	remote.setMnemonic('r');
	radio=new ButtonGroup();
	radio.add(local);
	radio.add(remote);
		
	
	
	
	label1=new JLabel("      IP address/URL/Machine name");
	
	Icon icon1=new ImageIcon("loc2.jpg");
	Icon icon2=new ImageIcon("loc1.jpg");
	label2=new JLabel("       Player one       ",icon1,SwingConstants.LEFT);
	
	label3=new JLabel("       Player two       ",icon2,SwingConstants.LEFT);
	label4=new JLabel("This is a label");
	chatl=new JLabel("Chat box");
	msgl=new JLabel("Message box");
	sendl=new JLabel("Chat");
	
		
	prompt=new JTextField("localhost",20);
	prompt.setEnabled(false);
	pl1=new JTextField("Local player",12);
	pl2=new JTextField("Remote player",12);
	sendtext=new JTextField("",35);
	sendtext.setEnabled(false);
	
	
	msgg=new JTextArea("",4,12);
	msgg.setEditable(false);
	scroller=new JScrollPane(msgg,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	chat=new JTextArea("",8,12);
	chat.setEditable(false);
	scroller1=new JScrollPane(chat,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);	
	connect=new JButton("Connect");
	connect.setToolTipText("Type IP Address and press Connect \n To initialize connection");
	Reset=new JButton("Reset");
	play=new JButton("Play"); 
	play.setToolTipText("Press to start game");
	send=new JButton("   Send   ");		
	send.setToolTipText("Type your message first ,then press send");
		
		
		
	//box[0].add(connect);
	panel1.add(local);
	panel1.add(label1);
	panel2.add(remote);
	panel2.add(prompt);
	panel2.add(connect);
	panel2.add(play);
	panel2.add(Reset);
	
	panel3.add(sendl);
	panel3.add(sendtext);
	panel3.add(send);

	box[0].add(Box.createVerticalStrut(5));
	box[0].add(panel1);
	box[0].add(Box.createVerticalStrut(5));
	box[0].add(panel2);

	scroller.setBorder(BorderFactory.createLoweredBevelBorder());
	scroller1.setBorder(BorderFactory.createLoweredBevelBorder());
	
	
	gbl=new GridBagLayout();
	gblconst=new GridBagConstraints();
	
	
	panel4=new JPanel();
	panel4.setLayout(gbl);
	
	
	addCom(label2,0,0,1,2);
	addCom(pl1,0,2,1,1);
	addCom(label3,0,3,1,2);
	addCom(pl2,0,5,1,1);
	addCom(msgl,0,6,1,1);
	addCom(scroller,0,7,1,2);
	addCom(chatl,0,11,1,1);
	addCom(scroller1,0,12,1,6);
	box[1].add(panel4);

	bbishop=new ImageIcon("BBishop.gif");
	wbishop=new ImageIcon("WBishop.gif");
	bking=new ImageIcon("BKing.gif");
	wking=new ImageIcon("WKing.gif");
	bqueen=new ImageIcon("BQueen.gif");
	wqueen =new ImageIcon("WQueen.gif");
	bpawn=new ImageIcon("BPawn.gif");	
	wpawn=new ImageIcon("WPawn.gif");
	brook=new ImageIcon("BRook.gif");
	wrook=new ImageIcon("WRook.gif");
	bknight=new ImageIcon("BKnight.gif");
	wknight=new ImageIcon("WKnight.gif");						 


	chess=new game();
	chess.init_Game();

	
	con.add(box[0],BorderLayout.NORTH);
	//con.add(box1,BorderLayout.CENTER);
	con.add(box[1],BorderLayout.EAST);	
	con.add(panel3,BorderLayout.SOUTH);

	
	//nwtworking ends..........
	
	setSize(540,550);
	setVisible(true);
	namel=JOptionPane.showInputDialog("Player name :");
	setResizable(true);
}

public void init_Chess()
{

	radioHandler h=new radioHandler();
	remote.addItemListener(h);
	local.addItemListener(h);
	
	Reset.addActionListener(
	new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			chess.reset();
			repaint();
			if(isRemote)
			out.println("5555");	
		}
	}
	);
	
	play.addActionListener(
	new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			if (!isRemote)
			{
				playLocal();
			}
			else
			{
			
				if (isConnected)
				{	
					Connected=true;
					sendtext.setEnabled(true);
				}
				else
				{
					JOptionPane.showMessageDialog(null,"Type IP ADDRESS "+"\nThen press connect","Not connected",JOptionPane.INFORMATION_MESSAGE);
				}
			 
			}
		}
	}
	);
	connect.addActionListener(
	
		new ActionListener()
		{
			public void actionPerformed(ActionEvent con)
			{
				isConnected=true;
				ipaddress=prompt.getText();
			}
		}
		);
		
//////////////////////////////////////menuitem//////////////////////		
		
		rscastle.addActionListener(
		new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (isRemote)			
				{			
					if (move==1)
					{
						chess.do_Castling(curplayer,1);
						repaint();
					}
				}
				else
				{
					chess.do_Castling(curplayer,1);
					repaint();
				}
			}
		}
		);
		
		lscastle.addActionListener(
		new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if(isRemote)
				
				{
					if(move==1)
					{
	
						chess.do_Castling(curplayer,0);
						repaint();
					}
				}
				else
				{
					chess.do_Castling(curplayer,0);
					repaint();
				}
			}
		}
		);

		queen.addActionListener(
		new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (chess.isPromoted(curplayer))
				{
					if (isRemote)
					{
						if (move==1)
						{
							if (curplayer==1)
							{
								out.println("promwq");
								chess.do_Promotion(wq);
							}
							else
							{
								out.println("prombq");
								chess.do_Promotion(bq);
							}
							move=move^1;							
						}
					}
					else
					{
						if (curplayer==1)
						{
							chess.do_Promotion(wq);
						}
						else
						{
							chess.do_Promotion(bq);
						}
					
					}
					repaint();					
				}
				else
				{
					JOptionPane.showMessageDialog(null,"No promoted pawn  ","Sorry",JOptionPane.WARNING_MESSAGE);				
							
				}

			}
		}
		);

		rook.addActionListener(
		new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (chess.isPromoted(curplayer))
				{
					if (isRemote)
					{
						if (move==1)
						{
							if (curplayer==1)
							{
								out.println("promwr");
								chess.do_Promotion(wr);
							}
							else
							{
								out.println("prombr");
								chess.do_Promotion(br);
							}
							move=move^1;							
						}
					}
					else
					{
						if (curplayer==1)
						{
							chess.do_Promotion(wr);
						}
						else
						{
							chess.do_Promotion(br);
						}
					}
					repaint();					
				}
				else
				{
					JOptionPane.showMessageDialog(null,"No promoted pawn  ","Sorry",JOptionPane.WARNING_MESSAGE);				
							
				}
			}
		}
		);

		knight.addActionListener(
		new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{

				if (chess.isPromoted(curplayer))
				{
					if (isRemote)
					{								
						if (move==1)
						{
							if (curplayer==1)
							{
								out.println("promwkn");
								chess.do_Promotion(wkn);
							}
							else
							{
								out.println("prombkn");
								chess.do_Promotion(bkn);
							}
							move=move^1;
						}
					}
					else
					{
						if (curplayer==1)
						{
							chess.do_Promotion(wkn);
						}
						else
						{
							chess.do_Promotion(bkn);
						}
					}
					repaint();					
				}
				else
				{
					JOptionPane.showMessageDialog(null,"No promoted pawn  ","Sorry",JOptionPane.WARNING_MESSAGE);				
							
				}
			
			}
		}
		);

		bishop.addActionListener(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
				if (chess.isPromoted(curplayer))
				{
					if (isRemote)
					{
						if (move==1)
						{
							if (curplayer==1)
							{
								out.println("promwb");
								chess.do_Promotion(wb);
							}
							else
							{
								out.println("prombb");
								chess.do_Promotion(bb);
							}
							move=move^1;
						}
					}
					else
					{
						if (curplayer==1)
						{
							chess.do_Promotion(wb);
						}
						else
						{
							chess.do_Promotion(bb);
						}
					}
					repaint();					
				}
				else
				{
					JOptionPane.showMessageDialog(null,"No promoted pawn  ","Sorry",JOptionPane.WARNING_MESSAGE);				
							
				}
			}
		}
		);
		
		
		
		
////////////////////////ends menu item*****************************/

		
	while (true)
	{
		if (Connected)
		{
			if (connects())
			{
				JOptionPane.showMessageDialog(null,"Connection established\nRemote player connected","Connection",JOptionPane.WARNING_MESSAGE);				
				playRemote();
			}
			else
			{
				msgg.setText("Coultd'nt Connect.\nTry again");
			}
		}
	}

}

private void addCom(Component c,int row,int col,int w,int h)
{
	gblconst.gridx=row;
	gblconst.gridy=col;
	gblconst.gridwidth=w;
	gblconst.gridheight=h;
	gbl.setConstraints(c,gblconst);
	panel4.add(c);
}


public void paint(Graphics g)
{
	int f=0;
	super.paint(g);
	g.setColor(new Color(222,55,77));
	g.drawRect(40,140,320,352);
	for(int k=0;k<8;k++)
	{
		for(int l=0;l<8;l++)
		{
			
			if(f==0)g.setColor(Color.black);
			g.fillRect(40+k*40,140+l*44,40,44);
			g.setColor(Color.white);
			f=f^1;		
		}
		f=f^1;
	}

	
	for(int l=0;l<8;l++)
	{
		for(int k=0;k<8;k++)
		{
			if (piece[k][l]!=null)
			{
				if (piece[k][l].get_id()==1&&piece[k][l].get_code()==6)
				{

					wpawn.paintIcon(this,g,40+40*k,140+44*l);

				}
				else if (piece[k][l].get_id()==1&&piece[k][l].get_code()==5)
				{

					wbishop.paintIcon(this,g,40+40*k,140+44*l);

				}
				else if (piece[k][l].get_id()==1&&piece[k][l].get_code()==4)
				{

					wknight.paintIcon(this,g,40+40*k,140+44*l);

				}
				else if (piece[k][l].get_id()==1&&piece[k][l].get_code()==3)
				{

					wrook.paintIcon(this,g,40+40*k,140+44*l);

				}
				else if (piece[k][l].get_id()==1&&piece[k][l].get_code()==2)
				{

					wqueen.paintIcon(this,g,40+40*k,140+44*l);

				}
				else if (piece[k][l].get_id()==1&&piece[k][l].get_code()==1)
				{

					wking.paintIcon(this,g,40+40*k,140+44*l);

				}
				else if (piece[k][l].get_id()==0&&piece[k][l].get_code()==6)
				{

					bpawn.paintIcon(this,g,40+40*k,140+44*l);

				}
				else if (piece[k][l].get_id()==0&&piece[k][l].get_code()==5)
				{

					bbishop.paintIcon(this,g,40+40*k,140+44*l);

				}
				else if (piece[k][l].get_id()==0&&piece[k][l].get_code()==4)
				{

					bknight.paintIcon(this,g,40+40*k,140+44*l);

				}
				else if (piece[k][l].get_id()==0&&piece[k][l].get_code()==3)
				{

					brook.paintIcon(this,g,40+40*k,140+44*l);

				}
				else if (piece[k][l].get_id()==0&&piece[k][l].get_code()==2)
				{

					bqueen.paintIcon(this,g,40+40*k,140+44*l);

				}
				else if (piece[k][l].get_id()==0&&piece[k][l].get_code()==1)
				{

					bking.paintIcon(this,g,40+40*k,140+44*l);

				}
			
			}
		}
	}
}





//-------------    FUNCTION PlayLocal   --------------------





private  void playLocal()
{

	msgg.setText(curplayer==1?"Move For White":"Move For Black");
	sendtext.setEnabled(false);
	addMouseListener(
	
	new MouseAdapter()
	{
		public void mousePressed(MouseEvent e)
		{
			
				posx=e.getX();posx=(posx-40)/40;
				posy=e.getY();posy=(posy-140)/44;
				msgg.setText(curplayer==1?"Move For White":"Move For Black");
				
		}		
			
	
	 	public void mouseReleased(MouseEvent e)
	 	{
			movx=e.getX();movx=(movx-40)/40;
			movy=e.getY();movy=(movy-140)/44;
				try
				{
					if (piece[posx][posy]!=null)
					{
						if(piece[posx][posy].get_id()==curplayer)
						{				
							chess.movepiece(posx,posy,movx,movy);
							repaint();
							msgg.setText(curplayer==1?"Move For White":"Move For Black");
						}
						else
						{
								JOptionPane.showMessageDialog(null,"Invalid move !!!","Information",JOptionPane.WARNING_MESSAGE);	
						}		
					}
				}
				catch(ArrayIndexOutOfBoundsException arr)
				{
				
				
				}
			
	 	}
	}
);	


}
	




//--------------------   FUNCTION PlayRemote   ---------------  



private void playRemote()
{
	try
	{	
		msg=in.readLine();
	}
	catch(IOException err)
	{

	}
	
	move=Integer.parseInt(msg);
	savemove=move;
	msgg.setText("\nYou are player "+(move==1?"one" : "two"));
	if (move==1)
	{
		pl1.setText(namel);
		pl2.setText(namer);
	}
	else
	{
		pl2.setText(namel);
		pl1.setText(namer);
	}
	
	addMouseListener(
	
	new MouseAdapter()
	{
		public void mousePressed(MouseEvent e)
		{
			
				posx=e.getX();posx=(posx-40)/40;
				posy=e.getY();posy=(posy-140)/44;
				
		}		
	 
	 	public void mouseReleased(MouseEvent e)
	 	{
			movx=e.getX();movx=(movx-40)/40;
			movy=e.getY();movy=(movy-140)/44;
			if (move==1)
			{
				try
				{
					if (piece[posx][posy]!=null)
					{
						if(piece[posx][posy].get_id()==curplayer)
						{				
							chess.movepiece(posx,posy,movx,movy);
							repaint();
						}
						else
						{
								JOptionPane.showMessageDialog(null,"Invalid move !!!","Information",JOptionPane.WARNING_MESSAGE);	
						}		
					}
				}
				catch(ArrayIndexOutOfBoundsException arr)
				{
				
				
				}
			}
			if(move==1)
				msgg.setText("\nYOUR TURN");
			else
				msgg.setText("\nOPPONENT'S TURN");	
	 	}
	}
);	



	send.addActionListener(
	new ActionListener()
	{
		public void actionPerformed (ActionEvent eee)
		{
		
			chat.append("\n"+pl1.getText()+" says:\n"+sendtext.getText());
			out.println(pl1.getText()+" says:\n"+sendtext.getText());
		}
	}
	);

		
	sendtext.addActionListener(
	new ActionListener()
	{
		public void actionPerformed(ActionEvent eee)
		{
			
			chat.append("\n"+pl2.getText()+" says:\n"+sendtext.getText());
			out.println(pl2.getText()+" says:\n"+sendtext.getText());
		}
	}
	);

	

	while (true)
	{
		
		if(move==1)
			msgg.setText("\nYOUR TURN");
		else
			msgg.setText("\nOPPONENT'S TURN");	

		try
		{
			msg=in.readLine();
		}
		catch(IOException ioo)
		{
		}
		if (msg.charAt(0)=='0')
		{
			if (move==0)
			{
	
				try
				{
					netmove=Integer.parseInt(msg);
				}
				catch(Exception e)
				{
			
				}
			
				posx=netmove/1000;
				posy=(netmove%1000)/100;
				movx=(netmove%100)/10;
				movy=netmove%10;
				chess.movepiece(posx,posy,movx,movy);
				repaint();
	//			chess.check_mate(curplayer);				
				if(!isRemote)break;
				if(move==1)
				msgg.setText("\nYOUR TURN");
				else
				msgg.setText("\nOPPONENT'S TURN");
				
			}
		}
		else if(msg.equals("1111"))
		{
	
			piece[4][7]=piece[7][7];
			piece[5][7]=piece[3][7];
			piece[7][7]=null;
			piece[3][7]=null;

			curplayer=curplayer^1;
			wkx=5;wky=7;	
			move=move^1;
			repaint();
		}
		else if (msg.equals("2222"))
		{
			piece[1][7]=piece[3][7];
			piece[2][7]=piece[0][7];
			piece[0][7]=null;
			piece[3][7]=null;

			curplayer=curplayer^1;				
			castle_w=false;
			wkx=1;wky=7;
			move=move^1;
			repaint();			
		}
		else if (msg.equals("3333"))
		{

			piece[5][0]=piece[3][0];
			piece[4][0]=piece[7][0];
			piece[7][0]=null;
			piece[3][0]=null;
		
			curplayer=curplayer^1;				
			castle_b=false;						
			bkx=5;bky=0;
			move=move^1;
			repaint();			
		}
		else if (msg.equals("4444"))
		{
	
			piece[1][0]=piece[3][0];
			piece[2][0]=piece[0][0];
			piece[0][0]=null;
			piece[3][0]=null;

			curplayer=curplayer^1;				
			castle_b=false;						
			bkx=0;bky=0;
			move=move^1;
			repaint();			
		}
		else if (msg.equals("promwq"))
		{
			chess.isPromoted(curplayer);		
			chess.do_Promotion(wq);
			move=move^1;
			repaint();
		}
		else if (msg.equals("prombq"))
		{
			chess.isPromoted(curplayer);		
			chess.do_Promotion(bq);
			move=move^1;
			repaint();
		}
		else if (msg.equals("promwr"))
		{
			chess.isPromoted(curplayer);		
			chess.do_Promotion(wr);
			move=move^1;
			repaint();
		}
		else if (msg.equals("prombr"))
		{
			chess.isPromoted(curplayer);		
			chess.do_Promotion(br);
			move=move^1;
			repaint();
		}
		else if (msg.equals("promwkn"))
		{
			chess.isPromoted(curplayer);		
			chess.do_Promotion(wkn);
			move=move^1;
			repaint();			
		}
		else if (msg.equals("prombkn"))
		{
			chess.isPromoted(curplayer);		
			chess.do_Promotion(bkn);
			move=move^1;
			repaint();			
		}
		else if (msg.equals("promwb"))
		{
			chess.isPromoted(curplayer);		
			chess.do_Promotion(wb);
			move=move^1;
			repaint();			
		}
		else if (msg.equals("prombb"))
		{
			chess.isPromoted(curplayer);
			chess.do_Promotion(bb);
			move=move^1;
			repaint();			
		}
		else if (msg.equals("5555"))
		{
			if (JOptionPane.showConfirmDialog(null,"Remote Player wants to reset.\nDo you agree ? ","Information",JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE)==0)				
			{
				chess.reset();
				move=savemove;
				repaint();
			}
			else
			{
				out.println("6666");
			}								
				
		}
		else if (msg.equals("6666"))
		{
			JOptionPane.showMessageDialog(null,"Remote player refuses","Reset failed",JOptionPane.INFORMATION_MESSAGE);			
		}
	
		else if (msg.equals("terminate"))
		{
			if (JOptionPane.showConfirmDialog(null,"Remote Player dismissed.\nDo you want to exit ? ","Information",JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE)==0)	
			{
				System.exit(0);
			}
		}
		else 
		{
			chat.append("\n"+msg);
		
		}
	}

}


private boolean connects()
{

	//.........NETWORKING..............
	
	try
	{
		msgg.setText("ATTEMPTING......");
		s=new Socket(InetAddress.getByName(ipaddress),4000);
		msgg.setText("\nConnection from:\n"+s.getInetAddress().getHostName());
		msgg.append("\nWaiting.... ");
		out=new PrintWriter(s.getOutputStream(),true);
		in=new BufferedReader(new InputStreamReader(s.getInputStream()));
		out.println(namel);
		namer=in.readLine();
		return true;
	}
	catch(UnknownHostException host)
	{
		
		JOptionPane.showMessageDialog(null,"Wrong IP Address\n Try again","Could'nt Connect",JOptionPane.INFORMATION_MESSAGE);
		return false;
		
	}
	catch(IOException eee)
	{
		return false;
	}
}



//**********************************************************************




/*
 	DECLERATION FOR THE CLASS " GAME " & DEFINITIONS FOR IT'S FUNCTIONS
	
 */


class game
{
	

	public game()
	{
	
		piece=new chesspiece[8][];
		for(int m=0;m<8;m++)
		{
			piece[m]=new chesspiece[8];
		}
		
		for(int i=0;i<8;i++)
			for(int j=0;j<8;j++)
			{
				piece[i][j]=null;
			}
		bkx=3;bky=0;wkx=3;wky=7;				
	}
	
	
	public void checkmate(int x,int y)
	{
		
	
	}



	public void placepiece(chesspiece p,int x,int y)
	{
		if(piece[x][y]==null)
			piece[x][y]=p;
			
	}
	
	
	
	public void reset()
	{
		for(int i=0;i<8;i++)
			for(int j=0;j<8;j++)
			{
				piece[i][j]=null;
			}
		curplayer=0;			
		init_Game();	
		bkx=3;bky=0;wkx=3;wky=7;
		castle_w=true;
		castle_b=true;		
	}
	
	
	
	
////////////////////////do promotion ////////////////////

public void do_Promotion(chesspiece p)
{
	if (curplayer==1)
	{
		piece[wpromx][wpromy]=p;
	}
	else
	{
		piece[bpromx][bpromy]=p;
	}	
	curplayer=curplayer^1;
	check_mate(curplayer);

}
	
	
	
	
///////////////////////do castling ////////////////////////////////	
	
	public void do_Castling(int current,int side)
	{
		chesspiece temp;
		if (current==1)
		{
			if (castle_w)
			{
				if ( side==1)
				{
					if ((piece[4][7]!=null)||(piece[5][7]!=null)||(piece[6][7]!=null))
					{
						JOptionPane.showMessageDialog(null,"Castling not allowed","Castling",JOptionPane.WARNING_MESSAGE);
					}
					else
					{
						piece[4][7]=piece[7][7];
						piece[5][7]=piece[3][7];
						piece[7][7]=null;
						piece[3][7]=null;
						curplayer=curplayer^1;
						wkx=5;wky=7;	
						castle_w=false;			
						if (isRemote&&move==1)
						{
							move=move^1;
							out.println("1111");
						}
							
					}
				}
				else
				{
					if ((piece[2][7]!=null)||(piece[1][7]!=null))
					{
						JOptionPane.showMessageDialog(null,"Castling not allowed","Castling",JOptionPane.WARNING_MESSAGE);
					}
					else
					{
						piece[1][7]=piece[3][7];
						piece[2][7]=piece[0][7];
						piece[0][7]=null;
						piece[3][7]=null;
						curplayer=curplayer^1;				
						castle_w=false;
						wkx=1;wky=7;
						if (isRemote&&move==1)
						{
							move=move^1;
							out.println("2222");
						}
					
					}
					
				}
			}
			else
			{
					JOptionPane.showMessageDialog(null,"Castling not allowed"+"\nKing or Rook was previously moved","Castling",JOptionPane.WARNING_MESSAGE);			
			}
		}
		else
		{
			if (castle_b)
			{	
				if ( side==1)
				{
					if ((piece[4][0]!=null)||(piece[5][0]!=null)||(piece[6][0]!=null))
					{
						JOptionPane.showMessageDialog(null,"Castling not allowed","Castling",JOptionPane.WARNING_MESSAGE);
					}
					else
					{
						piece[5][0]=piece[3][0];
						piece[4][0]=piece[7][0];
						piece[7][0]=null;
						piece[3][0]=null;
						curplayer=curplayer^1;				
						castle_b=false;						
						bkx=5;bky=0;
						if (isRemote&&move==1)
						{
							move=move^1;
							out.println("3333");
						}
						
					}
				}
				else
				{
					if ((piece[3][0]!=null)||(piece[2][0]!=null)||(piece[1][0]!=null))
					{
						JOptionPane.showMessageDialog(null,"Castling not allowed","Castling",JOptionPane.WARNING_MESSAGE);
					}
					else
					{
						piece[1][0]=piece[3][0];
						piece[2][0]=piece[0][0];
						piece[0][0]=null;
						piece[3][0]=null;
						curplayer=curplayer^1;				
						castle_b=false;						
						bkx=1;bky=0;
						if (isRemote&&move==1)
						{
							move=move^1;
							out.println("4444");
						}
						
					}
					
				}
			}
			else
			{
					JOptionPane.showMessageDialog(null,"Castling not allowed"+"\nKing or Rook was previously moved","Castling",JOptionPane.WARNING_MESSAGE);			
			}
		}
	}

/////////////////////castling ends ////////////////////////
	
	
	
	private boolean isCheck(int king)
	{
		int x,y,opp_id,savex,savey;
		boolean temp;
		
		if (king==0)
		{
			x=bkx;y=bky;
		}
		else
		{
			x=wkx;y=wky;
		}
		opp_id=king^1;


		for(int i=0;i<8;i++)
			for(int j=0;j<8;j++)
			{
				if(piece[i][j]!=null)
				{
					if (piece[i][j].get_id()==opp_id)
					{
						temp=piece[i][j].validity(i,j,x,y,piece);
						if(temp)return true;
					}
				}
			}	
		return false;
	}

///////////////////overloaded isCheck()///////////////////////


	
	private boolean isCheck(int king,int rx,int ry)
	{
		int x,y,opp_id,savex,savey;
		boolean temp;
		
		x=rx;y=ry;
		opp_id=king^1;


		for(int i=0;i<8;i++)
			for(int j=0;j<8;j++)
			{
				if(piece[i][j]!=null)
				{
					if (piece[i][j].get_id()==opp_id)
					{
						temp=piece[i][j].validity(i,j,x,y,piece);
						if(temp)return true;
					}
				}
			}	
		return false;
	}


//////////////////ends overloaded is check()//////////////////

///////////////////////////is game over ?//////////////////////////

public  boolean isOver(int cp)
{

final chesspiece save[][];
save=new chesspiece[8][];
for(int c=0;c<8;c++)
{
	save[c]=new chesspiece[8];
}
for(int a=0;a<8;a++)
	for(int b=0;b<8;b++)
	{
		save[a][b]=piece[a][b];
	
	}

//temp=new chesspiece[8][8];
boolean r=true;
for(int i=0;i<8;i++)
	for(int j=0;j<8;j++)
	{	
		if (piece[i][j]!=null )
		{
			if(piece[i][j].get_id()==cp)
			{
				for(int l=0;l<8;l++)
					for(int m=0;m<8;m++)
					{
						if (piece[i][j].validity(i,j,l,m,piece))
						{
							
							if ((piece[i][j].get_code())==1)
							{
									piece[l][m]=piece[i][j];
									piece[i][j]=null;
								
									if(!isCheck(cp,l,m))
										r=false;
							}
							else
							{
								piece[l][m]=piece[i][j];
								piece[i][j]=null;
								
								if(!isCheck(cp))		
									r=false;
							}
						}
						
//   reset piece

						for(int a=0;a<8;a++)
							for(int b=0;b<8;b++)
							{
								piece[a][b]=save[a][b];
	
							}						
							
					}
				}
			}
		}
//reset piece		
					for(int a=0;a<8;a++)
							for(int b=0;b<8;b++)
							{
								piece[a][b]=save[a][b];
	
							}
	return r;
}






/////////////////////////end of is game over ////////////////////////


/////////////check is there any promoted item ////////////
public boolean isPromoted(int cp)
{
	if (cp==0)
	{
	for(int i=0;i<8;i++)
		if (piece[i][7]!=null)
		{
			if (piece[i][7].get_code()==6&&piece[i][7].get_id()==0)
			{
				bpromx=i;bpromy=7;
				return true;
			}
			
		}
	}
	else
	{
		for(int i=0;i<8;i++)
			if (piece[i][0]!=null)
			{
				if (piece[i][0].get_code()==6&&piece[i][0].get_id()==1)
				{
					wpromx=i;wpromy=0;
					return true;
				}
			
			}
	}
	return false;
}

///////////////////////end of check promotion..................

	
	public void check_mate(int p)
	{
		if (isCheck(p))
		{
			JOptionPane.showMessageDialog(null,(p==1?"White":"Black")+"  King Checked","Check Warning",JOptionPane.WARNING_MESSAGE);
			if (isOver(p))
			{
				JOptionPane.showMessageDialog(null,(p==1?"White":"Black")+" Player lost","Game Over",JOptionPane.PLAIN_MESSAGE);		
				if (move!=curplayer)
				{
					msgg.append(p==0?"\nYou Lost":"\nYou win") ;
				}
				else
				{
					msgg.append(p==0?"\nYou Win":"\nYou Lost") ;				
				}
			}
		}
	}	
	
	

	public void movepiece(int x1,int y1,int x2,int y2)
	{
		chesspiece temp1,temp2;
		int tempx,tempy;
		boolean t;
		
		if ((piece[x1][y1]!=null)&&piece[x1][y1].validity(x1,y1,x2,y2,piece))
		{

/////////////save pieces ////////////////
			
			temp1=piece[x1][y1];	
			temp2=piece[x2][y2];
				
			piece[x2][y2]=piece[x1][y1];
			piece[x1][y1]=null;
			if (temp1.get_code()==1)
			{
				t=isCheck(curplayer,x2,y2);				
			}
			else
				t=isCheck(curplayer);
			
			if (t)
			{
				JOptionPane.showMessageDialog(null,"This move leaves\nYou checked","Information",JOptionPane.INFORMATION_MESSAGE);
				piece[x1][y1]=temp1;
				piece[x2][y2]=temp2;
			}
			else
			{
				
//save king pos and castling status..................
				if (temp1.get_code()==1)
				{
					if (temp1.get_id()==1)
					{
						castle_w=false;
						wkx=x2;wky=y2;
					}
					else
					{
						castle_b=false;
						bkx=x2;bky=y2;
					}
				}
			
//save castling status...............................

				if (temp1.get_code()==3)
				{
					if (temp1.get_id()==1)
					{
						castle_w=false;
					}
					else
					{
						castle_b=false;
					}
				}
//move and change current player and send move
				
				curplayer=curplayer^1;
				if((move==1)&&isRemote)
				{
					msg1="0"+posx+posy+movx+movy;
					out.println(msg1);
				}
				move=move^1;
				check_mate(curplayer);				
			}

		}		
	}

	
	public void init_Game()
	{
		wp=new pawn(1);
		bp=new pawn(0);
		wk=new king(1);placepiece(wk,3,7);
		bk=new king(0);placepiece(bk,3,0);
		wr=new rook(1);placepiece(wr,0,7);placepiece(wr,7,7);
		br=new rook(0);placepiece(br,0,0);placepiece(br,7,0);
		wb=new bishop(1);placepiece(wb,2,7);placepiece(wb,5,7);
		bb=new bishop(0);placepiece(bb,2,0);placepiece(bb,5,0);
		wkn=new knight(1);placepiece(wkn,1,7);placepiece(wkn,6,7);
		bkn=new knight(0);placepiece(bkn,1,0);placepiece(bkn,6,0);
		wq=new queen(1);placepiece(wq,4,7);
		bq=new queen(0);placepiece(bq,4,0);
		
		
		for(int i=0;i<8;i++)
		{
			placepiece(bp,i,1);
		}
		for(int i=0;i<8;i++)
		{
			placepiece(wp,i,6);
		}
	}

}


	public static void main(String args[])
	{
		MyNetChess5 c=new MyNetChess5();

		c.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		c.init_Chess();
	}
	
	
	
	private class radioHandler implements ItemListener
	{

	
		public void itemStateChanged(ItemEvent event)
		{
			if (event.getSource()==local)
			{
				prompt.setEnabled(false);
				sendtext.setEnabled(false);
				isRemote=false;
				isConnected=false;
				Connected=false;
			//	prompt.setText("Local");
				chess.reset();
				repaint();
			}
			else if(event.getSource()==remote)
			{
				try
				{
					if(s!=null)s.close();
				}
				catch(IOException iooe)
				{
				}
					in=null;
					out=null;
					prompt.setEnabled(true);
			
					isRemote=true;
					chess.reset();
					repaint();
			}
		}
	}	
	
	
}	

	
	
/*********************************chess pieces**********************/	
	


abstract class chesspiece
{
	protected int player_id;
	protected int piece_code;
	abstract public int get_id();//{return player_id;}
	abstract public int get_code();//{return piece_code;}
	abstract public boolean validity(int x1,int y1,int x2,int y2,chesspiece p[][]);
//	{return;};
}



					
class rook extends chesspiece
{
	public rook(int c)
	{
		player_id=c;
		piece_code=3;
	}
	public int get_id()
	{
		return  player_id;
	}
	public int get_code()
	{
		return piece_code;
	}
	
	
	public boolean validity(int x1,int y1,int x2,int y2,chesspiece p[][])
	{
	
		if((p[x2][y2]==null)||(p[x2][y2]!=null &&p[x1][y1].get_id()!=p[x2][y2].get_id()))
		{
			if(x1==x2)
			{
				if(y1>y2)
				{
					for(int i=y2+1;i<y1;i++)
				  		if(p[x1][i]!=null)return false;
					return true;
				}
				else
				{
					for(int i=y1+1;i<y2;i++)
				  	if(p[x1][i]!=null)return false;
					return true;
				}
			}
			else if(y1==y2)
			{
				if(x1>x2)
				{
					for(int i=x2+1;i<x1;i++)
				  		if(p[i][y1]!=null)return false;
					return true;
				}
				else
				{
					for(int i=x1+1;i<x2;i++)
				  		if(p[i][y1]!=null)return false;
					return true;
				}
			}
	    	else
			   return false;
		}
		else
			return false;
	
	}
	
}

	

class pawn extends chesspiece
{
	public pawn(int c)
	{
		player_id=c;
		piece_code=6;
	}
	public int get_id()
	{
		return  player_id;
	}
	public int get_code()
	{
		return piece_code;
	}
	
	
	public boolean validity(int x1,int y1,int x2,int y2,chesspiece p[][])
	{
	
		if(player_id==0)
		{
			if(p[x2][y2]==null)
			{
				if(x1==x2&&y2-y1==1)
				return true;

			}
			else if(p[x2][y2]!=null)
			{
				if((Math.abs(x2-x1)==1&&y2-y1==1)&&(p[x2][y2].get_id()!=p[x1][y1].get_id()))
				return true;
			}
			else
				return false;
		}
		else
		{
			if(p[x2][y2]==null)
			{
				if(x1==x2&&y1-y2==1)
				return true;
			}
			else if(p[x2][y2]!=null)
			{
				if(Math.abs(x2-x1)==1&&y1-y2==1&&p[x1][y1].get_id()!=p[x2][y2].get_id())
				return true;
			}
			else
				return false ;
		}
		return false;
	}

}	


class bishop extends chesspiece
{
	public bishop(int c)
	{
		player_id=c;
		piece_code=5;
	}
	public int get_id()
	{
		return  player_id;
	}
	public int get_code()
	{
		return piece_code;
	}

	public boolean validity(int x1,int y1,int x2,int y2,chesspiece p[][])
	{
		int dirx,diry;
		if((Math.abs(x2-x1)==Math.abs(y2-y1))&&((p[x2][y2]==null)||((p[x2][y2]!=null)&&(p[x1][y1].get_id()!=p[x2][y2].get_id()))))
		{

			if (x1<x2)
			{
				for(int i=x1+1;i<x2;i++)
				{
					if(((y1<y2)&&(p[i][y1+i-x1]!=null))||((y1>y2)&&(p[i][y1-(i-x1)]!=null)))return false;
					
				}
				return true;
			}
			else if(x1>x2)
			{
				for(int i=x2+1;i<x1;i++)
				{
					if(((y2<y1)&&(p[i][y2+i-x2]!=null))||((y2>y1)&&(p[i][y2-(i-x2)]!=null)))return false;
				}
				return true;
			}
			else 
			return false;
		}
		else
			return false;
	}

}	

class knight extends chesspiece
{
	public knight(int c)
	{
		player_id=c;
		piece_code=4;
	}
	public int get_id()
	{
		return  player_id;
	}
	public int get_code()
	{
		return piece_code;
	}
	
	
	public boolean validity(int x1,int y1,int x2,int y2,chesspiece p[][])
	{
		if(((Math.abs(x2-x1)==1&&Math.abs(y2-y1)==2)||(Math.abs(x2-x1)==2&&Math.abs(y2-y1)==1))&&((p[x2][y2]==null)||((p[x2][y2]!=null)&&(p[x1][y1].get_id()!=p[x2][y2].get_id()))))
			return true;
		else
			return false;
	
	}
	
}	


class king extends chesspiece
{
	public king(int c)
	{
		player_id=c;
		piece_code=1;
	}
	public int get_id()
	{
		return  player_id;
	}
	public int get_code()
	{
		return piece_code;
	}

	public boolean validity(int x1,int y1,int x2,int y2,chesspiece p[][])
	{
		if(p[x2][y2]==null)
		{
			if(((Math.abs(x2-x1))==1&&(y2==y1))||((Math.abs(x2-x1))==1&&((Math.abs(y2-y1))==1))||((x2==x1)&&(Math.abs(y2-y1))==1))
			return true;
		}
		else if(p[x2][y2]!=null)
		{
						if((((Math.abs(x2-x1))==1&&(y2==y1))||((Math.abs(x2-x1))==1&&((Math.abs(y2-y1))==1))||((x2==x1)&&(Math.abs(y2-y1))==1))&&(p[x1][y1].get_id()!=p[x2][y2].get_id()))
			return true;
		}
		return false;	
	}

}	


class queen extends chesspiece
{
	public queen(int c)
	{
		player_id=c;
		piece_code=2;
	}
	public int get_id()
	{
		return  player_id;
	}
	public int get_code()
	{
		return piece_code;
	}

	public boolean validity(int x1,int y1,int x2,int y2,chesspiece p[][])
	{
		int dirx,diry;

		if(((x2==x1||y2==y1)||(Math.abs(x2-x1)==Math.abs(y2-y1)))&&((p[x2][y2]==null)||(p[x2][y2]!=null &&(p[x1][y1].get_id()!=p[x2][y2].get_id()))))
		{
			if(x1==x2)
			{
				if(y1>y2)
				{
					for(int i=y2+1;i<y1;i++)
					  if(p[x1][i]!=null)return false;
					return true;  
				}
				else
				{
					for(int i=y1+1;i<y2;i++)
					  	if(p[x1][i]!=null)return false;
					return true;
				}
			}
			else if(y1==y2)
			{
				if(x1>x2)
				{
					for(int i=x2+1;i<x1;i++)
					  if(p[i][y1]!=null)return false;
					return true;
				}
				else
				{
					for(int i=x1+1;i<x2;i++)
					  if(p[i][y1]!=null)return false;
					return true;
				}
			}
			else
			{	
				dirx=x1>x2?-1:1;
				diry=y1>y2?-1:1;
				if (x1<x2)
				{
					for(int i=x1+1;i<x2;i++)
					{
						if(((y1<y2)&&(p[i][y1+i-x1]!=null))||((y1>y2)&&(p[i][y1-(i-x1)]!=null)))return false;
						
					}
					return true;
				}
				else if(x1>x2)
				{
					for(int i=x2+1;i<x1;i++)
					{
						if(((y2<y1)&&(p[i][y2+i-x2]!=null))||((y2>y1)&&(p[i][y2-(i-x2)]!=null)))return false;
					}
					return true;
				}
				else 
					return false;
			}
		}
		return false;
	}
	
}	


