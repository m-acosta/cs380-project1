/**
 * @author Michael Acosta
 * CS 380
 * Project 1
 * A simple chat client
 */

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatClient {

	/**
	 * Contains the client in it's entirety
	 * All text is UTF-8 
	 */
	public static void main(String[] args) {
		try {
			Socket proj1Client = new Socket("codebank.xyz", 38001);
			System.out.println("Connected to server.");
			
			// Create an input, output, and keyboard objects
			DataInputStream inputStream = new DataInputStream(proj1Client.getInputStream());
			DataOutputStream outputStream = new DataOutputStream(proj1Client.getOutputStream());
			Scanner keyboard = new Scanner(System.in);
			
			// First line sent is stored as a user name
			System.out.println("Enter a user name: ");
			String s = keyboard.nextLine();
			outputStream.writeUTF(s);
			// If the name is already in use, the server will respond "Name in use."
			// and close the connection
			// System.out.println(inputStream.readUTF());
			
			// Any further lines sent will be broadcast to all connected clients
			// with a time stamp and the sender's user name via a separate thread
			Runnable readAndDisplay = () -> {
				try {
					System.out.println(inputStream.readUTF());
				} catch (Exception e) {
					System.out.println(e);
				}
			};
			// Creates and starts thread
			Thread readAndDisplayThread = new Thread(readAndDisplay);
			readAndDisplayThread.start();
			
			String temp;
			while (true) {
				temp = keyboard.nextLine();
				if (temp.isEmpty()){
					break;
				}
				outputStream.writeUTF(temp);
			}
			
			// Close all open objects
			keyboard.close();
			inputStream.close();
			outputStream.close();
			proj1Client.close();
			System.out.println("Disconnected from server.");
		}
		catch (Exception e){
			System.out.println(e);
		}
	}

}