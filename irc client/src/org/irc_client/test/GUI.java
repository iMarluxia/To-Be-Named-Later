package org.irc_client.test;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;

import javax.swing.*;

public class GUI extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public String command;
	
	public GUI() throws Exception{
		Container cp = this.getContentPane();
		cp.setLayout(new GridLayout(2,1));
		
		
		JTextField ircInput = new JTextField();
		JTextArea ircOutput = new JTextArea();
		JButton test = new JButton("test");
		JScrollPane scroll = new JScrollPane(ircOutput);
		
		Container buttonHolder = new Container();
		buttonHolder.setLayout(new FlowLayout());
		buttonHolder.add(test);
		
		Container inputHolder = new Container();
		inputHolder.setLayout(new GridLayout());
		inputHolder.add(ircInput);
		
		cp.add(scroll);
		cp.add(inputHolder);
		setSize(750, 500);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
        // The server to connect to and our details.
        String server = "tidium.ddns.net";
        String nick = "TuckerBot";
        String login = "TuckerBot";

        // The channel which the bot will join.
        String channel = "#lobby";
        
        // Connect directly to the IRC server.
        try{
        	Socket socket = new Socket(server, 6667);
        	BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream( )));
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream( )));
            System.out.print("Connected\n");
            // Log on to the server.
            writer.write("NICK " + nick + "\r\n");
            writer.write("USER " + login + " 8 * : Tucker's Bot\r\n");
            writer.flush( );
            
            // Read lines from the server until it tells us we have connected.
            String line = null;
          
            try {
                Thread.sleep(500);                 //1000 milliseconds is one second.
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            
            // Join the channel.
            writer.write("JOIN " + channel + "\r\n");
            writer.flush( );
            
            System.out.println("Joined " + channel);
            
            // Keep reading lines from the server.
            while ((line = reader.readLine( )) != null) {
            	
                if (line.toLowerCase( ).startsWith("ping ")) {
                    // We must respond to PINGs to avoid being disconnected.
                	System.out.print("Trying to PONG\n");
                    writer.write("PONG " + line.substring(5) + "\n");
                    
                    //Un comment to check for ping/pong connection
                    //writer.write("PRIVMSG " + channel + " :I got pinged!\r\n");
                    
                    writer.flush( );
                    System.out.print("Pong'd\n");
                }
                else {
                    // Print the raw line received by the bot.
                	String OldText, NewText = ircInput.getText();
                	ircInput.addActionListener(new ActionListener(){
            			public void actionPerformed(ActionEvent e){
            				OldText = NewText;
            				NewText = ircInput.getText();
            				try {
        						if(!NewText.equals(System.getProperty("line.separator")) || !NewText.equals("")){
        							writer.write(NewText + "\n");
            						writer.flush();
        							ircOutput.append(NewText.trim());
        							System.out.print(NewText.trim() + "      <WAS A TEST\n");
        						}else{
        							System.out.print("UHUH");
        						}
        					} catch (IOException e1) {
        						// TODO Auto-generated catch block
        						e1.printStackTrace();
        					}
            				ircInput.setText("");
            			}
            		});
                    ircOutput.append(line + "\n");
                }
            }
            socket.close();
        }catch (Exception e){
        	e.printStackTrace();
        	ircOutput.append("Not Connected\n"
        			+ "Quiting in 5 Seconds");
        	try {
                Thread.sleep(5000);                 //1000 milliseconds is one second.
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        	System.exit(1);
        }
	}

	public static void main(String[] args) throws Exception{
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} 
		catch (UnsupportedLookAndFeelException e) {
			// handle exception
		}
		catch (ClassNotFoundException e) {
			// handle exception
		}
		catch (InstantiationException e) {
			// handle exception
		}
		catch (IllegalAccessException e) {
			// handle exception
		}
		new GUI();
	}
}

