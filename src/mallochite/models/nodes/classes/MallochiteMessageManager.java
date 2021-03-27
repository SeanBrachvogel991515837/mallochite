package mallochite.models.nodes.classes;

import java.io.IOException;
import java.util.HashMap;

public class MallochiteMessageManager
{
	private String deliminators = ":"; // change to new line
	private final int GREET_PARSE_COUNT = 2;
	private final int CONVERSE_PARSE_COUNT = 4;
	private final int AFFIRM_PARSE_COUNT = 4;
	private final int DEPART_PARSE_COUNT = 4;
	
	
    public MallochiteMessageManager () {};
    
    
    /* 
     * After decrypting the metadata of the message is parsed, stored in a hashmap and returned to be
     * used by the connection Negotiator 
     */
    public HashMap<String , String> parseHeader( String message )
    {
    	
    	HashMap messageSegment;
    
    	if ( message != null && message.contains( "GREET" ) ) // TODO must make regex to ensure start of line
    	{
    		messageSegment = this.parseDataFromHeader( message );
    	}
    	else
    	{
    		// TODO decrypt message as GREETINGS is the only one allowed not to be encrypted
    		messageSegment = this.parseDataFromHeader( message );
    	}
    	
    	return messageSegment;
    }
    
    public HashMap<String , String> parseDataFromHeader( String message )
    {
    	String[] parsedMessage = message.split( this.deliminators );
    	HashMap<String , String > parsedMessageHashMap = new HashMap<String , String>();
    	
    	switch ( parsedMessage[0] )
    	{
	    	case "GREET":
	    		parsedMessageHashMap = parseGreet( parsedMessage );
	    		break;
	    	case "CONVERSE":
	    		parsedMessageHashMap = parseConverse( parsedMessage );
	    		break;
	    	case "AFFIRM":
	    		parsedMessageHashMap = parseAffirm( parsedMessage );
	    		break;
	    	case "DEPART":
	    		parsedMessageHashMap = parseDepart( parsedMessage );
	    		break;
	    	default:
	    		parsedMessageHashMap.put( "method", "INVALID" );
    	}
    	
    	return parsedMessageHashMap;
    }
    
    /*
     * Takes method from message sent and returns the appropriate response method 
     */
	public String generateResponseSocket( HashMap<String , String> clientMetaDataHashMap , HashMap<String , String> localMetaDataHashMap )
	{
		String method = clientMetaDataHashMap.get( "method" ); 
		String port = clientMetaDataHashMap.get( "port" );
		
		String publicKey = localMetaDataHashMap.get( "publicKey" );
		String UUID = localMetaDataHashMap.get( "UUID" );
		String ipv4 = localMetaDataHashMap.get( "ipv4" );
		String response;	
		
		switch ( method )
		{
		case "GREET":
			response = "AFFIRM:" + UUID + ":" + ipv4 + ":" + port;
			break;
		case "CONVERSE":
			response = "OPEN";
			break;
		case "DEPART":
			response = "DEPART:" + UUID + ":" + ipv4 + ":" + port;
			break;
		default:
			response = "INVALID";
			break;
		}
		return response;
	}
	
	public String generateResponseServer( HashMap<String , String> clientMetaDataHashMap , HashMap<String , String> localMetaDataHashMap )
	{
		String method = clientMetaDataHashMap.get( "method" ); 
		String port = clientMetaDataHashMap.get( "port" );
		
		String publicKey = localMetaDataHashMap.get( "publicKey" );
		String UUID = localMetaDataHashMap.get( "UUID" );
		String ipv4 = localMetaDataHashMap.get( "ipv4" );
		String response;	
		
		switch ( method )
		{
		case "GREET":
			response = "GREET:" + publicKey;
			break;
		case "AFFIRM":
			response = "CONVERSE:" + UUID + ":" + ipv4 + ":" + port;
			break;
		case "DEPART":
			response = "DEPART:" + UUID + ":" + ipv4 + ":" + port;
			break;
    	case "OPEN":
    		response = "OPEN";
    		break;
		default:
			response = "INVALID";
			break;
		}
		return response;
	}
    
    /*
     * The following private methods are helper methods. 
     * They take the array of strings from it's caller and ensures it has the correct amount of parameters
     * for their respective header. It then places the associative values in a hashmap to be returned
     */
    private HashMap<String , String> parseGreet( String[] parsedMessage )
    {
    	HashMap<String , String> parsedMessageHashMap = new HashMap<String , String>();
    	
    	if ( parsedMessage.length >= GREET_PARSE_COUNT )
    	{
    		parsedMessageHashMap.put( "method", parsedMessage[0] );
    		parsedMessageHashMap.put( "pubkey", parsedMessage[1] );
    	}
    	else
    	{
    		parsedMessageHashMap.put( "method", "INVALID");
    	}
    	
    	return parsedMessageHashMap;
    }
    
    private HashMap<String , String> parseConverse( String[] parsedMessage )
    {
    	HashMap<String , String> parsedMessageHashMap = new HashMap<String , String>();
    	
    	if ( parsedMessage.length >= CONVERSE_PARSE_COUNT )
    	{
    		parsedMessageHashMap.put( "method", parsedMessage[0] );
    		parsedMessageHashMap.put( "UUID", parsedMessage[1] );
    		parsedMessageHashMap.put( "ipv4", parsedMessage[2] );
    		parsedMessageHashMap.put( "port", parsedMessage[3] );
    	}
    	else
    	{
    		parsedMessageHashMap.put( "method", "INVALID");
    	}
    	
    	return parsedMessageHashMap;
    }
    
    private HashMap<String , String> parseAffirm( String[] parsedMessage )
    {
    	HashMap<String , String> parsedMessageHashMap = new HashMap<String , String>();
    	
    	if ( parsedMessage.length >= AFFIRM_PARSE_COUNT )
    	{
    		parsedMessageHashMap.put( "method", parsedMessage[0] );
    		parsedMessageHashMap.put( "UUID", parsedMessage[1] );
    		parsedMessageHashMap.put( "ipv4", parsedMessage[2] );
    		parsedMessageHashMap.put( "port", parsedMessage[3] );
    	}
    	else
    	{
    		parsedMessageHashMap.put( "method", "INVALID");
    	}
    	
    	return parsedMessageHashMap;
    }
    
    private HashMap<String , String> parseDepart( String[] parsedMessage )
    {
    	HashMap<String , String> parsedMessageHashMap = new HashMap<String , String>();
    	
    	if ( parsedMessage.length >= DEPART_PARSE_COUNT )
    	{
    		parsedMessageHashMap.put( "method", parsedMessage[0] );
    		parsedMessageHashMap.put( "UUID", parsedMessage[1] );
    		parsedMessageHashMap.put( "ipv4", parsedMessage[2] );
    		parsedMessageHashMap.put( "port", parsedMessage[3] );
    	}
    	else
    	{
    		parsedMessageHashMap.put( "method", "INVALID");
    	}
    	
    	return parsedMessageHashMap;
    }
    
}
