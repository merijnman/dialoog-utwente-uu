
import pk.aamir.stompj.Connection;
import pk.aamir.stompj.ErrorHandler;
import pk.aamir.stompj.ErrorMessage;
import pk.aamir.stompj.Message;
import pk.aamir.stompj.MessageHandler;
import pk.aamir.stompj.StompJException;


public class testAvatar implements ErrorHandler, MessageHandler{
	
	private static Connection con;
	private static String inTopic;
	private static String outTopic;
	private static String bmlfeedbackTopic;
	static String apolloIP = "127.0.0.1";
	static int apolloPort = 61613;
	static testAvatar testavatar = new testAvatar(apolloIP, apolloPort);
	
	
	
	public testAvatar (String apolloIP, int apolloPort){
		outTopic = "/topic/SayThisRequest";//topic waar je op schrijft. schrijf de zin die je agent moet uitspreken. een wrapper (to be made) luistert en maakt hier een bml bericht van.
		inTopic = "/topic/WrapperFeedback";//topiclisteners eventueel feedback ding van de asap wrapper (ie. het stukje wat van de zin die je wilt, een bml bericht maakt)
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
		System.out.println("loading");
		//testAvatar obj = new testAvatar ();
		//obj.run ();	   
		for(int i=1; i<11; i++){
			System.out.println("Sending test message");
			String message = "Test"+i;	
			sendMessage(message);
		}
	}
	
	public static void sendMessage(String message) {
		con.send(message, outTopic);
		System.out.println("Message '"+message+"' send!");
	}
	
	@Override
	public void onMessage(Message msg) {
		System.out.println(msg.getContentAsString());       
	}

	@Override
	public void onError(ErrorMessage err) {
		System.out.println("[onError] "+err.getMessage());          
	}
	
	
	/*public void run(){
		String apolloIP = "";
		int apolloPort = 61613;
		String Topic = "/topic/bmlRequests";
		Connection con;
		
		try{
			System.out.println("connectie");
			con = new Connection(apolloIP, apolloPort, "user", "pass");
			con.setErrorHandler(this);
			System.out.println("voor connect");
			con.connect();
			System.out.println("Connection initialised.");

			String prefix = "{ \"bml\": { \"content\": \"";
			String suffix = "\" } }";
			String bmlprefix = "<bml xmlns=\"http://www.bml-initiative.org/bml/bml-1.0\" id=\"bml1\"><speech id=\"speech1\" start=\"2\"><text>";
			String bmlsuffix = "</text></speech></bml>";
			String bmltext = "Hello! This is a basic BML test for the realizer bridge!";
			String msg = prefix+bmlprefix+bmltext+bmlsuffix+suffix;
			con.send(msg, Topic);
			System.out.println("na send");
		}catch (StompJException e) {
			System.out.println("Error while initialising STOMP connection: "+e.getMessage());
			e.printStackTrace();
			return;
		}
	}
	*/
}
