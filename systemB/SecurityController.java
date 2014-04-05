package systemB;
import InstrumentationPackage.*;
import MessagePackage.*;
import Configuration.*;
class SecurityController
{
	public static void main(String args[])
	{
		String MsgMgrIP;					// Message Manager IP address
		Message Msg = null;					// Message object
		MessageQueue eq = null;				// Message Queue
		MessageManagerInterface em = null;	// Interface object to the message manager
		boolean isArmed = false;	
		int	Delay = 2500;					// The loop delay (2.5 seconds)
		boolean Done = false;				// Loop termination flag

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

			/* Now we create the humidity control status and message panel
			 ** We put this panel about 2/3s the way down the terminal, aligned to the left
			 ** of the terminal. The status indicators are placed directly under this panel
			 */

			float WinPosX = 0.0f; 	//This is the X position of the message window in terms
			//of a percentage of the screen height
			float WinPosY = 0.5f;	//This is the Y position of the message window in terms
			//of a percentage of the screen height

			MessageWindow mw = new MessageWindow("Security Controller Status Console", WinPosX, WinPosY);


			Indicator armedIndicator= new Indicator ("Disarmed", mw.GetX()+mw.Width(), mw.GetY());

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
			 ** Here we start the main simulation loop
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
					if(Msg.GetMessageId() == Configuration.SECURITY_MONITOR_ID){
						if (Msg.GetMessage().equalsIgnoreCase("ARM")){
							isArmed = true;
							mw.WriteMessage("Received security controller ON message" );
						}else if (Msg.GetMessage().equalsIgnoreCase("DISARM")){
							isArmed = false;
							mw.WriteMessage("Received security controller OFF message" );
						}						
					}
					else if(Msg.GetMessageId() == Configuration.DOOR_SENSOR_ID){
						if(isArmed) postMessage(em, "Door");
						mw.WriteMessage("Received Door Alarm" );
					}
					else if(Msg.GetMessageId() == Configuration.WINDOW_SENSOR_ID){
						if(isArmed) postMessage(em, "Window");
						mw.WriteMessage("Received Window Alarm" );
					}
					else if(Msg.GetMessageId() == Configuration.MOTION_SENSOR_ID){
						if(isArmed) postMessage(em, "Motion");
						mw.WriteMessage("Received Motion Alarm" );
					}
					else if(Msg.GetMessageId() == Configuration.SMOKE_SENSOR_ID){
						if(isArmed) postMessage(em, "Smoke");
						mw.WriteMessage("Received Smoke Alarm" );
					}
					else if(Msg.GetMessageId() == 99){
						Done = true;
						mw.WriteMessage("Received End Message" );
						try{
							em.UnRegister();
						}catch (Exception e){
							mw.WriteMessage("Error unregistering: " + e);
						} // catch
						mw.WriteMessage( "\n\nSimulation Stopped. \n");
						armedIndicator.dispose();
					}
				} // for

				// Update the lamp status

				if (isArmed){
					armedIndicator.SetLampColorAndMessage("SECURITY ON", 1);
				} else {
					armedIndicator.SetLampColorAndMessage("SECURITY OFF", 0);

				} // if
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


	static private void postMessage(MessageManagerInterface ei, String m ){
		// Here we create the message.
		Message msg = new Message( Configuration.SECURITY_CONTROLLER_ID, m );
		// Here we send the message to the message manager.
		try{
			ei.SendMessage( msg );
		} // try
		catch (Exception e){
			System.out.println("Error posting Message:: " + e);
		} // catch
	} // PostMessage

}