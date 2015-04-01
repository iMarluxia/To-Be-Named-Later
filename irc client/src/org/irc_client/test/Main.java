package org.irc_client.test;

import java.io.*;
import java.net.*;

public class Main {
	public static void main(String[] args) throws Exception {

        // The server to connect to and our details.
        String server = "tidium.ddns.net";
        String nick = "TuckerBot";
        String login = "TuckerBot";

        // The channel which the bot will join.
        String channel = "#lobby";
        
        // Connect directly to the IRC server.
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
      
        /*try {
            Thread.sleep(2000);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        */
        
        // Join the channel.
        writer.write("JOIN " + channel + "\r\n");
        writer.flush( );
        
        // Keep reading lines from the server.
        while ((line = reader.readLine( )) != null) {
        	
            if (line.toLowerCase( ).startsWith("ping ")) {
                // We must respond to PINGs to avoid being disconnected.
            	System.out.print("Trying to PONG\n");
                writer.write("PONG " + line.substring(5) + "\r\n");
                
                //Un comment to check for ping/pong connection
                //writer.write("PRIVMSG " + channel + " :I got pinged!\r\n");
                
                writer.flush( );
                System.out.print("Pong'd\n");
            }
            else {
                // Print the raw line received by the bot.
                System.out.println(line);
            }
        }
        socket.close();
    }
}
