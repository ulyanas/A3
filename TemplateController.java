/******************************************************************************************************************
* File:TemplateController.java
* Course: 17655
* Project: Assignment A3
* Copyright: Copyright (c) 2014 Carnegie Mellon University
* Versions:
*	1.0 April 2014 - Initial version of template
*
* Description:
*
* This class is a template for controller of specific device. 
* Follow the TODO list to create your own device controller
*
* Parameters: IP address of the message manager (on command line). If blank, it is assumed that the message manager is
* on the local machine.
*
******************************************************************************************************************/
import InstrumentationPackage.*;
import MessagePackage.*;

class TemplateController
{
	public static void main(String args[])
	{
		String MsgMgrIP;					// Message Manager IP address
		Message Msg = null;					// Message object
		MessageQueue eq = null;				// Message Queue
		MessageManagerInterface em = null;	// Interface object to the message manager
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

		// Here we check to see if registration worked. If ef is null then the
		// message manager interface was not properly created.

		if (em != null)
		{
			System.out.println("Registered with the message manager." );

			/* Now we create the temperature control status and message panel
			** We put this panel about 1/3 the way down the terminal, aligned to the left
			** of the terminal. The status indicators are placed directly under this panel
			*/

			// TODO: Change the window position
			float WinPosX = 0.0f; 	//This is the X position of the message window in terms
								 	//of a percentage of the screen height
			float WinPosY = 0.0f; 	//This is the Y position of the message window in terms
								 	//of a percentage of the screen height

			// TODO: Add the controller name:
			String controllerName = "";
			MessageWindow mw = new MessageWindow(controllerName, WinPosX, WinPosY);

			// TODO: Add status indicators if needed
			// Put the status indicators
			// Indicator i = new Indicator ("Indicator", mw.GetX(), mw.GetY());

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

	    	// TODO: Modify the simulation loop 
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

				// Getting the queue of messages for the controller
				
				int qlen = eq.GetSize();

				for ( int i = 0; i < qlen; i++ )
				{
					Msg = eq.GetMessage();

					// TODO: Process the relevant messages
					/*
					 * if ( Msg.GetMessageId() == 1 ){
					 *  ...
					 *  mw.WriteMessage("...");
					 * }
					 */
						
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

				    	// TODO: Free the resources
				    	
					} // if

				} // for

				// TODO: Do some control actions
				
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

} // TemplateController