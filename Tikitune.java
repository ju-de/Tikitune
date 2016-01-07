/*  Judy Chen & Tammy Liu
	Mrs. Strelkovska
	ICS3U-03 & ICS3U-01
	Summative Project: Tikitune
	12-14-12
*/

import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import sun.audio.*;

class Tikitune extends JFrame implements ActionListener,KeyListener {

	JLabel player, comp, ask;
	JButton bPlay, bInstructions, bHighScores, bQuit, bEnter;
	JPanel button, game, in_score;
	JTextField name;
	int count = 0, pattern=3, streak=1, score = 0, in_move=0, com;
	boolean continue_game = true;	
	String line, writeScore;

	ImageIcon bg;
	Timer time;
	
	int[] ai=new int[9999];
	int list[] = new int[6];
	String names[] = new String[6];
	ImageIcon[][] moves=new ImageIcon[2][4];			
	
	public static void main(String[]args) {
		Tikitune frame=new Tikitune();
		frame.setSize(900,600);
		frame.setLocation(150,100);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

//Constructor
	public Tikitune(){
		
		moves[0][0]=new ImageIcon("Graphics/tktplayer_left.gif");
		moves[0][1]=new ImageIcon("Graphics/tktplayer_right.gif");
		moves[0][2]=new ImageIcon("Graphics/tktplayer_up.gif");
		moves[0][3]=new ImageIcon("Graphics/tktplayer_down.gif");

		moves[1][0]=new ImageIcon("Graphics/complayer_left.gif");
		moves[1][1]=new ImageIcon("Graphics/complayer_right.gif");
		moves[1][2]=new ImageIcon("Graphics/complayer_up.gif");
		moves[1][3]=new ImageIcon("Graphics/complayer_down.gif");
	
		setTitle("Tikitune");
		addKeyListener(this);		
		
	//Panels
	 //Button Panel
		button=new JPanel();
		
	 //Paint Panel
		game=new JPanel(){
			public void paintComponent(Graphics g){
				super.paintComponent(g);
				//Background
				g.drawImage(bg.getImage(),0,0,895,544,null);
				g.drawString("Score: " +score,420,450);
			}
        };	
		time=new Timer(600, this);
		
	 //High Scores Panel
		in_score = new JPanel();
		in_score.setVisible(false);
		ask = new JLabel("Game Over. Please enter your name:");
		name = new JTextField(10);
		bEnter = new JButton("Enter");
		bEnter.addActionListener(this);
		bEnter.addKeyListener(this);
		
		in_score.add(ask);
		in_score.add(name);
		in_score.add(bEnter);
		
	//Labels
		comp=new JLabel(new ImageIcon("Graphics/complayer.gif"));
		player=new JLabel(new ImageIcon("Graphics/tktplayer.gif"));
		bg=new ImageIcon("Graphics/tiki_background.gif");
		
	//Buttons
		bPlay = new JButton("Play");
		bPlay.addActionListener(this);
		bPlay.addKeyListener(this);

		bInstructions = new JButton("Instructions");
		bInstructions.addActionListener(this);
		bInstructions.addKeyListener(this);		
		
		bHighScores = new JButton("High Scores");
		bHighScores.addActionListener(this);
		bHighScores.addKeyListener(this);

		bQuit = new JButton("Quit");
		bQuit.addActionListener(this);
		bQuit.addKeyListener(this);
		
		button.add(bPlay);
		button.add(bInstructions);
		button.add(bHighScores);
		button.add(bQuit);
		
	//Layout
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		c.add(button,BorderLayout.SOUTH);
		c.add(in_score,BorderLayout.NORTH);
		button.setLayout(new GridLayout(1,4,10,30));
		
		game.setLayout(new FlowLayout(FlowLayout.CENTER, 140, 300));
		game.add(player);
		game.add(comp);
		game.addKeyListener(this);
		c.add(game,BorderLayout.CENTER);
	}
	
//Game
	public void gamePlay(int user) throws Exception{
	//User Press Key
		//Left
		if(user==37){
			user=0;
			player.setIcon(moves[0][0]);
			//reset gif
			moves[0][0].getImage().flush();
		}
		//Right
		else if(user==39){
			user=1;
			player.setIcon(moves[0][1]);
			moves[0][1].getImage().flush();
		}
		//Up
		else if(user==38){
			user=2;
			player.setIcon(moves[0][2]);
			moves[0][2].getImage().flush();
		}
		//Down
		else if(user==40){
			user=3;
			player.setIcon(moves[0][3]);
			moves[0][3].getImage().flush();
		}
		
		if(continue_game){
			//Checking user and ai input
			if(input(user)){
				//Increasing score
				score+=10*streak;
				
				//Check if the number of moves in pattern has been reached by user
				if(in_move==(pattern-1)){
					//Increase the number in the pattern, streak, and reset the user input move number
					streak++;
					pattern++;
					count = 0;
					in_move = 0;
					streak=1;
				}
				else{
					in_move++;
				}
			}
			else{
				//Inputting score
				in_score.setVisible(true);
				
				//end current game
				continue_game=false;
				player.setIcon(new ImageIcon("Graphics/tktplayer_deed.gif"));
				comp.setIcon(new ImageIcon("Graphics/complayer.gif"));
				System.out.println("Lose.");
			}
		}
	}

//Computer Move
	public void ai(){
		try{
			InputStream in = new FileInputStream("clap.wav");
			AudioStream aud = new AudioStream(in);
			AudioPlayer.player.start(aud);
		} catch (Exception ex){
			ex.printStackTrace();
		}
		com=(int)(Math.random()*4);
		ai[count]=com;
		
		if(com==0){
			System.out.println("Left");
		}
		else if(com==1){
			System.out.println("Right");
		}
		else if(com==2){
			System.out.println("Up");
		}
		else if(com==3){
			System.out.println("Down");
		}
		
		comp.setIcon(moves[1][com]);
		moves[1][com].getImage().flush();
	}

//Check Input
	public boolean input(int user){
		boolean match;

		//Input key matches
		if(user==ai[in_move]){
			match=true;
		}
		//Doesn't match
		else{
			match=false;
		}

		return match;
	}

//Actions
	public void actionPerformed(ActionEvent action){
	//Timer
		if(action.getSource()==time){	
			if(continue_game&&(count<pattern)){
				ai();
				count++;
				
			}
			game.repaint();			
		}
	//Button Pressed
		else{
			JButton b = (JButton)action.getSource();
	
			if (b.getText().equals("Play")){
				//reset sequence
				pattern=3;
				streak=1;
				count=0;
				score = 0;
				in_move=0;
				
				player.setIcon(new ImageIcon("Graphics/tktplayer.gif"));
				
				continue_game=true;
				time.start();
			}
			else if (b.getText().equals("Instructions")){
				JOptionPane.showMessageDialog(null, "Watch and memorize the silhouette's movement, \nthen repeat it using the arrows on your keyboard.\nWait until it's done or else you may be confused.","Instructions", JOptionPane.INFORMATION_MESSAGE);
			}
			else if (b.getText().equals("High Scores")){
				try{
					JOptionPane.showMessageDialog(null, read_high_score(), "High Scores", JOptionPane.INFORMATION_MESSAGE);
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
			else if (b.getText().equals("Quit")){
				System.exit(0);
			}
			else if (b.getText().equals("Enter")){
				writeScore = name.getText();
				try{
					write_high_score();
				} catch (Exception ex){
					ex.printStackTrace();
				}
				in_score.setVisible(false);
			}
		}
	}
	
//Key Listener
	public void keyPressed(KeyEvent e){
		System.out.println(e.getKeyText(e.getKeyCode()) + " " + e.getKeyCode());
		//if Ai is not playing, start checking for input
		if(continue_game){
			if(check(e.getKeyCode())){
				try{
					gamePlay(e.getKeyCode());
					InputStream in = new FileInputStream("clap.wav");
					AudioStream aud = new AudioStream(in);
					AudioPlayer.player.start(aud);
				}catch (Exception ex){
					ex.printStackTrace();
				}
			}
		}
	}
	public void keyTyped(KeyEvent e){}
	public void keyReleased(KeyEvent e){}
	
//Limit Input to Arrow Keys
	public boolean check(int user){
		if(!(user>=37 && user <=40)){
			return false;
		}
		return true;
	}
	
//High Score Input
	public void write_high_score() throws Exception{
		BufferedReader read = new BufferedReader(new FileReader("High_Scores.txt"));
		
		line = "";
		String l;
		for (int i = 0; i < 5; i++){
			line = read.readLine();
			l = line.substring(0,line.indexOf(" "));
			list[i] = Integer.parseInt(l);				
			names[i] = line.substring(line.indexOf(" ")+1);
		}
		list[5] = score;
		names[5] = writeScore;
		sort();
		
		line = "";
		for (int i = 0; i < 5; i++){
			line += list[i] + " " + names[i] + "\n";
		}
		
		PrintWriter write = new PrintWriter(new BufferedWriter(new FileWriter("High_Scores.txt")));

		write.println(line);
				
		read.close();
		write.close();
	}	
//High Scores Output
	public String read_high_score() throws IOException{
		BufferedReader read = new BufferedReader(new FileReader("High_Scores.txt"));
		
		line = "";
		for (int i = 0; i < 5; i++){
			line += read.readLine() + "\n";
		}
		
		read.close();
		return line;
	}
//High Scores Sort
	public void sort(){
		for (int top = 0; top < 6; top++){
			int s = list[top];
			String n = names[top];
			int index = top;
			
			while (index > 0 && s > list[index-1]){
				list[index] = list[index-1];
				names[index] = names[index-1];
				index--;
			}
			
		list[index] = s;
		names[index] = n;
		}
	}
}