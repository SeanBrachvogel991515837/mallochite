package mallochite.models.nodes.abstractClasses;

/*
 * Andrew Hollands
 * March 20th 2020
 * holandre@sheridancollege.ca
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

import mallochite.models.exceptions.UninitializedSocket;
import mallochite.models.nodes.classes.*;

public abstract class Node extends Thread
{
	private String hostIpAddress;
    private Socket socket;
    private ServerSocket serverSocket;
    private BufferedReader in;
    private PrintStream out;
    private String messageBuffer;
    
    /* Must have hostIp Address
     */
    public Node ( String hostIpAddress )
    {
    	this.hostIpAddress = hostIpAddress;
    }
    
    /*
     * optionally set port right away instead of getter and setter
     */
    public Node ( String hostIpAddress , int portToUse )
    {
    	this.hostIpAddress = hostIpAddress;
    }
	
	
    /* in: 		Takes String of IP address to send message to and port as an int
     * process: Tries to open Socket that connects to a remote client. Creates a buffer reader and printstream
     * 			to listen to the socket's traffic 
     * out: 	Return void or throws SocketException or IOException on failure
     */
	public void openSocket ( String remoteIpAddress , int portNumberToUse ) throws IOException , SocketException
	{
		Scanner scanner = new Scanner (System.in);
		
        try
        {
            this.socket = new Socket ( remoteIpAddress , portNumberToUse );
            this.in = new BufferedReader ( new InputStreamReader( socket.getInputStream() ) );
            this.out = new PrintStream ( socket.getOutputStream() );
            this.messageBuffer = " "; // made empty so can be compared in if statement
            
            while ( !this.messageBuffer.equals( "" ) )
            {
            	this.messageBuffer = scanner.nextLine();
            	this.sendMessage( this.messageBuffer );
            }
        }
        catch ( SocketException socketException )
        {
            throw socketException;
        }
	}
	
	
    public void startListeningOnPort ( int portNumberToUse )
    {
        try
        {
            this.serverSocket = new ServerSocket( portNumberToUse );
            this.start();
        }
        catch ( IOException ex )
        {
            ex.printStackTrace();
        }
    }
    
    // must add a stopListenOnPort method
	
	
	/* In: 			take no arguments but works with this.in , this.out and this.socket
	 * Process: 	closes all attributes listed above
	 * out:			Returns void on success, throws an IOException or SocketException on fail
	 */
	public void closeSocket () throws SocketException , IOException
	{
        try
        {
            this.in.close();
            this.out.close();
            this.socket.close();
        }
        catch ( SocketException ex )
        {
            throw ex;
        }
	}
	
	
	public void closeServerSocket () throws IOException
	{
        try
        {
            this.serverSocket.close();
            this.interrupt();
        }
        catch ( SocketException ex )
        {
            throw ex;
        }
	}
	
	
	
	/* in: 		takes a String to send to client
	 * process:	prints message to this.out (which is the output for the socket)
	 * out:		returns void on success, throws SocketException on fail
	 */
	public void sendMessage ( String message ) throws SocketException
	{
		this.out.println( message );
		this.out.flush();
	}
	
	
    /* in: 		Takes no arguments but works with this.in
     * process: Reads from this.in and stores the value in messageBuffer
     * out: 	Return void or throws IOException on failure
     */
	public void updateMessageBuffer () throws IOException , UninitializedSocket
	{
		if ( this.socket != null )
		{
	        try
	        {
	            this.messageBuffer = this.in.readLine();
	            
	            while( this.messageBuffer != null )
	            {
	                this.messageBuffer += in.readLine();
	            }
	        }
	        catch ( IOException iOException )
	        {
	            throw iOException;
	        }
		}
		else
		{
			UninitializedSocket usEx = new UninitializedSocket( "this method requires openSocket to be run first" );
			throw usEx;
		}
	}
	
	
	/* runs methods on a separate thread 
	 * 
	 * Since this method loops, it checks the last message that was sent against the message 
	 * it's going to send. If it is the same message, it will not send again. It's the same concept with
	 * read. If it already read the message it means it already knows about it. This method will run
	 * until terminated
	 */
	@Override
	public void run ()
	{
        try
        {
        	Socket socket = this.serverSocket.accept();
        	
            System.out.println( "Listening for a connection" );

            // Pass the socket to the RequestHandler thread for processing
            RequestHandler requestHandler = new RequestHandler( socket );
            ResponseHandler responseHandler = new ResponseHandler( socket );
            
            requestHandler.start();
            responseHandler.start();
        }
        catch ( IOException ex )
        {
            ex.printStackTrace();
        }
	}

	
	public BufferedReader getIn()
	{
		return in;
	}

	public void setIn(BufferedReader in)
	{
		this.in = in;
	}

	public PrintStream getOut()
	{
		return out;
	}

	public void setOut(PrintStream out)
	{
		this.out = out;
	}

	public String getHostIpAddress()
	{
		return hostIpAddress;
	}
	
	public void setHostIpAddress(String hostIpAddress)
	{
		this.hostIpAddress = hostIpAddress;
	}


	public String getMessageBuffer() throws UninitializedSocket , IOException
	{
		try
		{
			this.updateMessageBuffer ();
			return messageBuffer;
		}
		catch ( UninitializedSocket ex )
		{
			throw ex = new UninitializedSocket ("this method requires openSocket to be run first");
		}
		
	}


	public void setMessageBuffer(String messageBuffer)
	{
		this.messageBuffer = messageBuffer;
	}
	
	
}
