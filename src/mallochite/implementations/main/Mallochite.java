package mallochite.implementations.main;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import mallochite.models.classes.nodes.SubNode;
import mallochite.models.nodes.classes.*;

public class Mallochite 
{
	public static void main ( String [] args ) throws IOException, NoSuchAlgorithmException
	{
		
		Scanner scanner = new Scanner( System.in );
		SubNode subNode1 = null;
		User thisUser = null;
		User remoteUser = new User();
		
//		System.out.println("Sign in");
//		System.out.println("Username: ");
//		String username = scanner.nextLine();
//		System.out.println("Password: ");
//		String password = scanner.nextLine();
		
		remoteUser.id = "0";
		remoteUser.ipAddress = "192.168.2.58";
		remoteUser.port = 42424;
		remoteUser.username = "bill";
		
		if ( true )
		{
			thisUser = new User();
			
			thisUser.id = "1234-12346743-3423567";
			thisUser.username = "user1";
			thisUser.ipAddress = "127.0.0.1";
			thisUser.port = 23895;
			thisUser.addUser ( remoteUser );
			
		}
//		else
//		{
//			System.out.println( " Authentication Failed " );
//			System.exit(0);
//		}

		

//		System.out.println( "enter your IP address" );
//		String localIpAddress = scanner.nextLine();

//		System.out.println( "enter port to listen on" );
//		int portToListen = scanner.nextInt();

		try
		{
			subNode1 = new SubNode( thisUser.ipAddress );
			subNode1.setThisUser ( thisUser );
			
			while ( subNode1.isListening() )
			{
				
				subNode1.openServerSocket( thisUser.port );
				subNode1.start();
				
	    		System.out.println( "Make Connection? [Y/n]" );
	    		String response = scanner.nextLine();
	    		
	    		if ( response.equals( "n" ) ) 
	    		{
	    			break;
	    		}
	    		else
	    		{
	    			System.out.println( "Who would you like to connect to" );
	    			String usernameToSearch = scanner.nextLine();
	    			User userToConnect = null;
	    			
	    			for	( int i = 0 ; i < thisUser.userCount ; i++ )
	    			{
	    				if ( thisUser.user[i].username.equals( usernameToSearch ) )
	    				{
	    					userToConnect = thisUser.user[i];
	    				}
	    				else
	    				{
	    	    			System.out.println( "user not found" );
	    	    			System.exit(0);
	    				}
	    			}
	    			
//	        		System.out.println( "enter IP address to connect to" );
//	        		String remoteIpAddress = scanner.nextLine();
//
//	        		System.out.println( "enter port to connect to" );
//	        		int portToListen = scanner.nextInt();
	        		
	    			if ( userToConnect != null ) 
	    			{
	    				System.out.println( "Connecting to " + usernameToSearch );
	    				subNode1.makeConnection( userToConnect );
	    			}
	    		}
			}   
      
		}
		finally
		{
			if ( subNode1.getServerSocket() != null )
			{
				subNode1.closeServerSocket();
			}
		}
        
	}
}
