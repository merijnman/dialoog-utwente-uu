import pk.aamir.stompj.Connection;
import pk.aamir.stompj.ErrorHandler;
import pk.aamir.stompj.ErrorMessage;
import pk.aamir.stompj.Message;
import pk.aamir.stompj.MessageHandler;
import pk.aamir.stompj.StompJException;


public class testAvatarWrapper implements ErrorHandler, MessageHandler{
	
	private Connection con;
	private static String inTopic;
	private static String outTopic;
	private static String bmlRequestTopic;
	private static String bmlfeedbackTopic;
	static String apolloIP = "127.0.0.1";
	static int apolloPort = 61613;
	static testAvatarWrapper testavatar = new testAvatarWrapper(apolloIP, apolloPort);	
	
	public testAvatarWrapper (String apolloIP, int apolloPort){
		inTopic = "/topic/SayThisRequest";//hier ontvangen wij de zin die de agent moet uitspreken. we maken hier een bml bericht van.
		outTopic = "/topic/WrapperFeedback";//feedback geven aan dit topic over wrapper taken
		bmlRequestTopic = "/topic/bmlRequest";//bml hier heen sturen (asap moet hier op luisteren)
		bmlfeedbackTopic = "/topic/bmlFeedback"; //topiclisteners bmlfeedback: dit is de feedback van de asap realiser (ie. de aansturing vd embodiment).
		
        try {
            con = new Connection(apolloIP, apolloPort, "admin", "password");
            con.setErrorHandler(this);
			con.connect();
			System.out.println("Connection initialised.");
		} catch (StompJException e) {
			System.out.println("Error while initialising STOMP connection: "+e.getMessage());
			e.printStackTrace();
			return;
		}
        
        try {
			con.subscribe(inTopic, true);
			con.addMessageHandler(inTopic, this);
						
			con.subscribe(bmlfeedbackTopic, true);
			con.addMessageHandler(bmlfeedbackTopic, this);
			
			System.out.println("Subscriptions done.");			
		} catch (Exception e) {
			System.out.println("Error while subscribing: "+e.getMessage());
			e.printStackTrace();
			return;
		}
	}


	public static void main(String args[]){
		System.out.println("waiting for message");
		//testAvatar obj = new testAvatar ();
		//obj.run ();	   
		while (true){ 
			
		}
	}
	
	public void sendMessage(String message, String topic) {
		con.send(message, topic);
		System.out.println("Message '"+message+"' send to topc '"+topic+"' ");
	}
	
	@Override
	public void onMessage(Message msg) {
		System.out.println("Got a message:");
		System.out.println(msg.getContentAsString());   
		System.out.println("Testing by returning the message:");
		sendMessage(msg.getContentAsString(), outTopic);
	}

	@Override
	public void onError(ErrorMessage err) {
		System.out.println("[onError] "+err.getMessage());          
	}
	
}
