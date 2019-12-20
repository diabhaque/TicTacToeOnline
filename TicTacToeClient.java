import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
/**
 * This program implements a simple TicTacToe Client. It creates a GUI for the game. It connects with the TicTacToeServer and plays a TicTacToe game with another client.
 * 
 * @author Diabul Haque
 * @version 1.0
 */
public class TicTacToeClient {
	
	private static Box box1 = new Box(1);
	private static Box box2 = new Box(2);
	private static Box box3 = new Box(3);
	private static Box box4 = new Box(4);
	private static Box box5 = new Box(5);
	private static Box box6 = new Box(6);
	private static Box box7 = new Box(7);
	private static Box box8 = new Box(8);
	private static Box box9 = new Box(9);
	private static Box[] boxes= {box1, box2, box3, box4, box5, box6, box7, box8, box9};
	
	private static CrossIcon cross = new CrossIcon();
	private static CircleIcon circle= new CircleIcon();
	private static BlankIcon blank= new BlankIcon();
	
	private static Socket sock;
	private static BufferedReader reader;
	private static PrintWriter writer;
	
	private static JLabel label_interact;
	
	private static boolean submitEnabled=true;
	
	private static int[] board= new int[9];
	
	private static int player = 0;
	private static boolean playerTurn=false;
	private static boolean complete=false;
	private static boolean network= false;
	
	public class IncomingReader implements Runnable{
		public void run() {
			String message;
			try {
				while((message = reader.readLine())!=null) {
					System.out.println("read "+ message);
				}
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	private static void setUpBoard(int[] gotBoard) {
		for(int i=0; i<9; i++) {
			board[i]=gotBoard[i];
			if(gotBoard[i]==0) {
				boxes[i].setIcon(blank);
			}else if(board[i]==1) {
				boxes[i].setIcon(cross);
				boxes[i].gotClicked();
			}else if(board[i]==9) {
				boxes[i].setIcon(circle);
				boxes[i].gotClicked();
			}
		}
	}
	
	private static void setUpNetworking() {
		try {
			sock= new Socket("127.0.0.1", 5000);
			network=true;
			InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
			reader= new BufferedReader(streamReader);
			String num=reader.readLine();
			player=Integer.valueOf(num);
			if(player==1) {
				playerTurn=true;
			}
			writer= new PrintWriter(sock.getOutputStream());
			
			System.out.println("networking established");
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	private static int didWin(int[] gotBoard) {
//		123 456 789 147 258 369 159 357
		int sum123=gotBoard[0]+gotBoard[1]+gotBoard[2];
		int sum456=gotBoard[3]+gotBoard[4]+gotBoard[5];
		int sum789=gotBoard[6]+gotBoard[7]+gotBoard[8];
		int sum147=gotBoard[0]+gotBoard[3]+gotBoard[6];
		int sum258=gotBoard[1]+gotBoard[4]+gotBoard[7];
		int sum369=gotBoard[2]+gotBoard[5]+gotBoard[8];
		int sum159=gotBoard[0]+gotBoard[4]+gotBoard[8];
		int sum357=gotBoard[2]+gotBoard[4]+gotBoard[6];
		
		if (sum123==3 || sum456==3 || sum789==3 || sum147==3 || sum258==3 || sum369==3 || sum159==3 || sum357==3) {
			return 1;
		}else if(sum123==27 || sum456==27 || sum789==27 || sum147==27 || sum258==27 || sum369==27 || sum159==27 || sum357==27) {
			return 2;
		}else {
			return 0;
		}
	}
	
	private static boolean noZeros(int[] gotBoard) {
		for (int i=0; i<9; i++) {
			if(gotBoard[i]==0) {
				return false;
			}
		}
		return true;
	}
	
	private static MouseListener boxListener(Box box) {
		MouseListener Listener= new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				if (!box.isClicked() && !submitEnabled && playerTurn) {
					box.gotClicked();
					label_interact.setText("Valid move, wait for your opponent.");
					playerTurn=false;
					int index=box.getNumber()-1;
					if(player==1) {
						box.setIcon(cross);
						board[index]=1;
					}else if(player==2){
						box.setIcon(circle);
						board[index]=9;
					}
					String sendBoard="";
					for(int i=0; i<8; i++) {
						String toAdd=Integer.toString(board[i])+",";
						sendBoard+=toAdd;
					}
					if(player==1) {
						String toAdd=Integer.toString(board[8])+",2";
						sendBoard+=toAdd;
					}else if(player==2){
						String toAdd=Integer.toString(board[8])+",1";
						sendBoard+=toAdd;
					}
					writer.println(sendBoard);
					writer.flush();
				}
			}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			
		};
		return Listener;
	}
	/**
	 * This is the main function that creates the GUI and adds functionality through action listeners.
	 * @param args
	 */
	public static void main(String[] args) {
		
		//creating the menu bar
		JMenuBar menubar=new JMenuBar();
		
		JMenu control= new JMenu("Control");
		JMenuItem controlExit= new JMenuItem("Exit");
		control.add(controlExit);
		
		JMenu help= new JMenu("Help");
		JMenuItem rules= new JMenuItem("Instructions");
		help.add(rules);
		
		menubar.add(control);
		menubar.add(help);
		
		JPanel InteractionPanel = new JPanel();
		label_interact=new JLabel("Enter your player name...");
		InteractionPanel.add(label_interact);
		
		JPanel GamePanel = new JPanel();
		GamePanel.setLayout(new GridLayout(3,3));
		box1.setIcon(blank);
		box2.setIcon(blank);
		box3.setIcon(blank);
		box4.setIcon(blank);
		box5.setIcon(blank);
		box6.setIcon(blank);
		box7.setIcon(blank);
		box8.setIcon(blank);
		box9.setIcon(blank);
		GamePanel.add(box1);
		GamePanel.add(box2);
		GamePanel.add(box3);
		GamePanel.add(box4);
		GamePanel.add(box5);
		GamePanel.add(box6);
		GamePanel.add(box7);
		GamePanel.add(box8);
		GamePanel.add(box9);
		
		JPanel NamePanel = new JPanel();
		JTextField txt_inputname = new JTextField(15);
		JButton btn_submit= new JButton("Submit");
		NamePanel.add(txt_inputname);
		NamePanel.add(btn_submit);
		
		JFrame frame= new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(BorderLayout.NORTH, InteractionPanel);
		frame.getContentPane().add(BorderLayout.SOUTH, NamePanel);
		frame.getContentPane().add(BorderLayout.CENTER, GamePanel);
		frame.setTitle("Tic Tac Toe");
		frame.setJMenuBar(menubar);
		frame.setSize(295, 400);
		frame.setVisible(true);
		
		setUpNetworking();
		Thread readerThread= new Thread(new Runnable() {
			public void run() {
				String message;
				try {
					while((message = reader.readLine())!=null) {
						if(message.equals("0")) {
							
							String helpMessage="Game Ends. One of the players left.";
							JFrame DialogFrame= new JFrame("Message");
							JOptionPane.showMessageDialog(DialogFrame, helpMessage);
							playerTurn=false;
							break;
						}
						if(message.charAt(18)=='2') {
							if(player==2) {
								label_interact.setText("Your opponent has moved, now is your turn.");
								playerTurn=true;
							}
						}
						if(message.charAt(18)=='1') {
							if(player==1) {
								label_interact.setText("Your opponent has moved, now is your turn.");
								playerTurn=true;
							}
						}
						String[] stringBoard=message.split(",");
						int[] intBoard=new int[9];
						for (int i=0; i<9; i++) {
							intBoard[i]=Integer.valueOf(stringBoard[i]);
						}
						setUpBoard(intBoard);
						
						if(didWin(board)==1) {
							playerTurn=false;
							if (player==1) {
								String helpMessage="Congratulations. You Win.";
								JFrame DialogFrame= new JFrame("Message");
								JOptionPane.showMessageDialog(DialogFrame, helpMessage);
							}else if(player==2) {
								String helpMessage="You lose.";
								JFrame DialogFrame= new JFrame("Message");
								JOptionPane.showMessageDialog(DialogFrame, helpMessage);
							}
							complete=true;
						}else if(didWin(board)==2) {
							playerTurn=false;
							if (player==2) {
								String helpMessage="Congratulations. You Win.";
								JFrame DialogFrame= new JFrame("Message");
								JOptionPane.showMessageDialog(DialogFrame, helpMessage);
							}else if(player==1) {
								String helpMessage="You lose.";
								JFrame DialogFrame= new JFrame("Message");
								JOptionPane.showMessageDialog(DialogFrame, helpMessage);
							}
							complete=true;
						}else if(noZeros(board)) {
							String helpMessage="Draw";
							JFrame DialogFrame= new JFrame("Message");
							JOptionPane.showMessageDialog(DialogFrame, helpMessage);
							complete=true;
						}
					}
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			
		});
		
		readerThread.start();

		
		rules.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String helpMessage="Some information about the game:\nCriteria for a valid move:\n-The move is not occupied by any mark.\n-The move is made in the player's turn.\n-The move is made within the 3x3 board.\nThe game would continue and switch among the opposite player until it reaches either one of the following conditions:\n-Player 1 wins.\n-Player 2 wins.\n-Draw.";
				JFrame DialogFrame= new JFrame("Message");
				JOptionPane.showMessageDialog(DialogFrame, helpMessage);
			}
		});
		
		controlExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!complete && network) {
					writer.println("0");
					writer.flush();
				}
				System.exit(0);
			}
		});
		
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if(!complete && network) {
					writer.println("0");
					writer.flush();
				}
				System.exit(0);
			}
		});
		
		btn_submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(submitEnabled) {
					String name= txt_inputname.getText();
					if (name.length()>0) {
						label_interact.setText("WELCOME "+name);
						txt_inputname.setEditable(false);
						submitEnabled=false;
					}else {
						String helpMessage="WARNING: Name field can not be empty!";
						JFrame DialogFrame= new JFrame("Message");
						JOptionPane.showMessageDialog(DialogFrame, helpMessage);
					}
				}
			}
		});
		
		
		box1.addMouseListener(boxListener(box1));
		box2.addMouseListener(boxListener(box2));
		box3.addMouseListener(boxListener(box3));
		box4.addMouseListener(boxListener(box4));
		box5.addMouseListener(boxListener(box5));
		box6.addMouseListener(boxListener(box6));
		box7.addMouseListener(boxListener(box7));
		box8.addMouseListener(boxListener(box8));
		box9.addMouseListener(boxListener(box9));
		
	}

}
