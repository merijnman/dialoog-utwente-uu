import pk.aamir.stompj.Connection;
import pk.aamir.stompj.ErrorHandler;
import pk.aamir.stompj.ErrorMessage;
import pk.aamir.stompj.Message;
import pk.aamir.stompj.MessageHandler;
import pk.aamir.stompj.StompJException;

public class testAvatar implements ErrorHandler, MessageHandler{

	public static void main(String args[]){
		System.out.println("loading");
		testAvatar obj = new testAvatar ();
		obj.run ();	   
	}
	
	@Override
	public void onMessage(Message msg) {
		System.out.println(msg.getContentAsString());       
	}

	@Override
	public void onError(ErrorMessage err) {
	System.out.println("[onError] "+err.getMessage());          
	}

	public void run(){
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
}
