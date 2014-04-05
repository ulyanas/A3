package systemB;
import java.util.Timer;
import java.util.TimerTask;

import Configuration.Configuration;
import InstrumentationPackage.*;
import MessagePackage.Message;
import MessagePackage.MessageManagerInterface;
import MessagePackage.MessageQueue;


public class SprinklerController {
	
	private static Timer timer;
	private static MessageManagerInterface em = null;	// Interface object to the message manager
	private static int SprinklerState = 0;  //1-on, 2-ready, 0-off
	
	public static void main(String args[])
	{
		String MsgMgrIP;				// Message Manager IP address
		Message Msg = null;					// Message object
		MessageQueue eq = null;				// Message Queue
		
		
		int	Delay = 2500;				// The loop delay (2.5 seconds)
		
		
		boolean Done = false;			// Loop termination flag
		boolean isArmed = false;
		
		timer = new Timer();

		/////////////////////////////////////////////////////////////////////////////////
		// Get the IP address of the message manager
		/////////////////////////////////////////////////////////////////////////////////

 		if ( args.length == 0 )
 		{
			// message manager is on the local system

			System.out.println("\n\nAttempting to register on the local machine..." );

			try
			{
				// Here we create an message manager interface object. This assumes
				// that the message manager is on the local machine

				em = new MessageManagerInterface();
			}

			catch (Exception e)
			{
				System.out.println("Error instantiating message manager interface: " + e);

			} // catch

		} else {

			// message manager is not on the local system

			MsgMgrIP = args[0];

			System.out.println("\n\nAttempting to register on the machine:: " + MsgMgrIP );

			try
			{
				// Here we create an message manager interface object. This assumes
				// that the message manager is NOT on the local machine

				em = new MessageManagerInterface( MsgMgrIP );
			}

			catch (Exception e)
			{
				System.out.println("Error instantiating message manager interface: " + e);

			} // catch

		} // if

		// Here we check to see if registration worked. If em is null then the
		// message manager interface was not properly created.

		if (em != null)
		{
			System.out.println("Registered with the message manager." );

			/* Now we create the temperature control status and message panel
			** We put this panel about 1/3 the way down the terminal, aligned to the left
			** of the terminal. The status indicators are placed directly under this panel
			*/

			float WinPosX = 0.0f; 	//This is the X poSprinklerIndicatortion of the message window in terms 
								 	//of a percentage of the screen height
			float WinPosY = 0.3f; 	//This is the Y poSprinklerIndicatortion of the message window in terms 
								 	//of a percentage of the screen height 
			
			MessageWindow mw = new MessageWindow("Sprinkler Controller Status Console", WinPosX, WinPosY);
			
			// Put the status indicators under the panel...
			
			Indicator SprinklerIndicator = new Indicator ("Sprinkler OFF", mw.GetX()+mw.Width(), mw.GetY()+mw.Height()/2);

			mw.WriteMessage("Registered with the message manager." );

	    	try
	    	{
				mw.WriteMessage("   Participant id: " + em.GetMyId() );
				mw.WriteMessage("   Registration Time: " + em.GetRegistrationTime() );

			} // try

	    	catch (Exception e)
			{
				System.out.println("Error:: " + e);

			} // catch

			/********************************************************************
			** Here we start the main SprinklerIndicatormulation loop
			*********************************************************************/

			while ( !Done )
			{

				
				try
				{
					eq = em.GetMessageQueue();

				} // try

				catch( Exception e )
				{
					mw.WriteMessage("Error getting message queue::" + e );

				} // catch

				
				int qlen = eq.GetSize();

				for ( int i = 0; i < qlen; i++ )
				{
					Msg = eq.GetMessage();
					
					if (Msg.GetMessageId() == Configuration.SPRINKLER_CONTROLLER_ID)
					{
						if (Msg.GetMessage().equalsIgnoreCase("S1")) 
						{
							if (SprinklerState!=1) {
								SprinklerState = 1;
								mw.WriteMessage("Received turn sprinkler on message" );
							}
							


						} // if

						if (Msg.GetMessage().equalsIgnoreCase("S0")) 
						{
							
							//Thread.currentThread().interrupt();
							SprinklerState = 0;
							mw.WriteMessage("Received turn sprinkler off message" );

						} // if
						
						if (Msg.GetMessage().equalsIgnoreCase("S2")) 
						{
							if (SprinklerState==0) {
								SprinklerState = 2;
								mw.WriteMessage("Received  sprinker ready message" );
							}
							
						} // if

					} // if
					
					else if( Msg.GetMessageId() == Configuration.SMOKE_SENSOR_ID){
						
						mw.WriteMessage("Received "+Msg.GetMessage()+" Alarm");
						if(Msg.GetMessage().equals("Smoke")) {
							postMessage(em, "S2");	
							
							timer.schedule(new TimerTestTask(), 10*1000);
						}
						
					}
					
					
					else if(Msg.GetMessageId() == Configuration.SECURITY_MONITOR_ID){
						if (Msg.GetMessage().equalsIgnoreCase("ARM")){
							isArmed = true;
							mw.WriteMessage("Received security controller ON message" );
						}else if (Msg.GetMessage().equalsIgnoreCase("DISARM")){
							isArmed = false;
							mw.WriteMessage("Received security controller OFF message" );
						}						
					}
					
					// If the message ID == 99 then this is a SprinklerIndicatorgnal that the SprinklerIndicatormulation
					// is to end. At this point, the loop termination flag is set to
					// true and this process unregisters from the message manager.

					if ( Msg.GetMessageId() == 99 )
					{
						Done = true;

						try
						{
							em.UnRegister();

				    	} // try

				    	catch (Exception e)
				    	{
							mw.WriteMessage("Error unregistering: " + e);

				    	} // catch

				    	mw.WriteMessage( "\n\nSimulation Stopped. \n");

						// Get rid of the indicators. The message panel is left for the
						// user to exit so they can see the last message posted.

						SprinklerIndicator.dispose();

					} // if

				} // for

				// Update the lamp status
				
				

				if (SprinklerState==1)
				{
					// Set to green, heater is on
					if (isArmed)
					SprinklerIndicator.SetLampColorAndMessage("Sprinkler ON", 1);

				} else if (SprinklerState==0){
					if (isArmed)
					// Set to black, heater is off
					SprinklerIndicator.SetLampColorAndMessage("Sprinkler OFF", 0);
					

				} // if
				 else if (SprinklerState==2){
					 if (isArmed)
					// Set to black, heater is off
					 {
						 SprinklerIndicator.SetLampColorAndMessage("Sprinkler READY",2);
						
						
					 }
					
					
	
				} // if

				if (!isArmed){
					SprinklerIndicator.SetLampColorAndMessage("Sprinkler OFF", 0);
					SprinklerState=0;
				}
				
				try
				{
					Thread.sleep( Delay );

				} // try

				catch( Exception e )
				{
					System.out.println( "Sleep error:: " + e );

				} // catch

			} // while

		} else {

			System.out.println("Unable to register with the message manager.\n\n" );

		} // if

	} // main
	
	static private void postMessage(MessageManagerInterface mi, String m ){
		// Here we create the message.
		Message msg = new Message( Configuration.SPRINKLER_CONTROLLER_ID, m );
		// Here we send the message to the message manager.
		try{
			mi.SendMessage( msg );
		} // try
		catch (Exception e){
			System.out.println("Error posting Message:: " + e);
		} // catch
	} // PostMessage
	
	static class TimerTestTask extends TimerTask
	{
		public void run() {
			Message msg;
			if (SprinklerState==2) 
					postMessage(em, "S1");	
			//System.out.println("timer");
			
		}	
	}

} // SprinklerController

				