package systemC;

import Configuration.Configuration;
import InstrumentationPackage.*;
import MessagePackage.*;

import java.util.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

class SecurityMonitor extends Thread
{
	private MessageManagerInterface em = null;	// Interface object to the message manager
	private String MsgMgrIP = null;				// Message Manager IP address
	private boolean Registered = true;					// Signifies that this class is registered with an message manager.
	private MessageWindow mw = null;					// This is the message window
	private Indicator montionIndicator;					// Motion indicator
	private Indicator windowIndicator;					// Window indicator
	private Indicator doorIndicator;					// Door indicator


	public SecurityMonitor()
	{
		// message manager is on the local system
		try
		{
			// Here we create an message manager interface object. This assumes
			// that the message manager is on the local machine

			em = new MessageManagerInterface();
			Registered = true;
		}

		catch (Exception e)
		{
			System.out.println("ECSMonitor::Error instantiating message manager interface: " + e);
			Registered = false;

		} // catch
	} //Constructor

	public SecurityMonitor( String MsgIpAddress )
	{
		// message manager is not on the local system

		MsgMgrIP = MsgIpAddress;

		try
		{
			// Here we create an message manager interface object. This assumes
			// that the message manager is NOT on the local machine

			em = new MessageManagerInterface( MsgMgrIP );
		}

		catch (Exception e)
		{
			System.out.println("ECSMonitor::Error instantiating message manager interface: " + e);
			Registered = false;

		} // catch

	} // Constructor

	public void run()
	{
		Message Msg = null;				// Message object
		MessageQueue eq = null;			// Message Queue
		int MsgId = 0;					// User specified message ID
		int	Delay = 1000;				// The loop delay (1 second)
		boolean Done = false;			// Loop termination flag
		boolean ON = true;				// Used to arm the security controller
		boolean OFF = false;			// Used to disarm the security controller

		
		if (em != null)
		{
			// Now we create the ECS status and message panel
			// Note that we set up two indicators that are initially yellow. This is
			// because we do not know if the temperature/humidity is high/low.
			// This panel is placed in the upper left hand corner and the status
			// indicators are placed directly to the right, one on top of the other

			mw = new MessageWindow("Security Monitoring Console", 0, 0);
			montionIndicator = new Indicator ("Motion Indicator", mw.GetX()+ mw.Width(), 0);
			windowIndicator = new Indicator ("Window Indicator", mw.GetX()+ mw.Width(), (int)(mw.Height()/3), 0);
			doorIndicator = new Indicator ("Door Indicator", mw.GetX()+ mw.Width(), (int)(mw.Height()/3*2), 0);

			mw.WriteMessage( "Registered with the message manager." );

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
				// Here we get our message queue from the message manager

				try
				{
					eq = em.GetMessageQueue();

				} // try

				catch( Exception e )
				{
					mw.WriteMessage("Error getting message queue::" + e );

				} // catch

				// If there are messages in the queue, we read through them.
				// We are looking for MessageIDs = 1 or 2. Message IDs of 1 are temperature
				// readings from the temperature sensor; message IDs of 2 are humidity sensor
				// readings. Note that we get all the messages at once... there is a 1
				// second delay between samples,.. so the assumption is that there should
				// only be a message at most. If there are more, it is the last message
				// that will effect the status of the temperature and humidity controllers
				// as it would in reality.

				int qlen = eq.GetSize();

				for ( int i = 0; i < qlen; i++ )
				{
					Msg = eq.GetMessage();
					if( Msg.GetMessageId() == Configuration.SECURITY_CONTROLLER_ID){
						mw.WriteMessage("Detected "+Msg.GetMessage()+" Alarm");
						if(Msg.GetMessage().equals("Door")) doorIndicator.SetLampColorAndMessage("Door Alarm", 3);
						else if(Msg.GetMessage().equals("Window")) windowIndicator.SetLampColorAndMessage("Window Alarm", 3);
						else if(Msg.GetMessage().equals("Motion")) montionIndicator.SetLampColorAndMessage("Motion Alarm", 3);
					}
					// If the message ID == 99 then this is a signal that the simulation
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

						windowIndicator.dispose();
						montionIndicator.dispose();
						doorIndicator.dispose();

					} // if

				} // for
				// This delay slows down the sample rate to Delay milliseconds

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

	public boolean IsRegistered()
	{
		return( Registered );

	} // SetTemperatureRange

	
	public void Halt()
	{
		mw.WriteMessage( "***HALT MESSAGE RECEIVED - SHUTTING DOWN SYSTEM***" );

		// Here we create the stop message.

		Message msg;

		msg = new Message( (int) 99, "XXX" );

		// Here we send the message to the message manager.

		try
		{
			em.SendMessage( msg );

		} // try

		catch (Exception e)
		{
			System.out.println("Error sending halt message:: " + e);

		} // catch

	} // Halt
	

	public void setSecuritySystem( boolean ON )
	{
		// Here we create the message.

		Message msg;

		if ( ON )
		{
			msg = new Message( Configuration.SECURITY_MONITOR_ID, "ARM" );
			doorIndicator.SetLampColorAndMessage("Door Safe", 1);
			windowIndicator.SetLampColorAndMessage("Window Safe", 1);
			montionIndicator.SetLampColorAndMessage("Motion Safe", 1);
		} else {

			msg = new Message( Configuration.SECURITY_MONITOR_ID, "DISARM" );
			doorIndicator.SetLampColorAndMessage("Door Off", 0);
			windowIndicator.SetLampColorAndMessage("Window Off", 0);
			montionIndicator.SetLampColorAndMessage("Motion Off", 0);
		} // if

		try
		{
			em.SendMessage( msg );

		} // try

		catch (Exception e)
		{
			System.out.println("Error sending security control message:: " + e);

		} // catch

	}
}