/******************************************************************************************************************
* File:TemplateSensor.java
* Course: 17655
* Project: Assignment A3
* Copyright: Copyright (c) 2014 Carnegie Mellon University
* Versions:
*	1.0 April 2014 - Initial version of template
*
* Description:
*
* This class is a template for creating the new sensors. Follow the TODO list to create your own sensors.
*
* Parameters: IP address of the message manager (on command line). If blank, it is assumed that the message manager is
* on the local machine.
*
******************************************************************************************************************/
import InstrumentationPackage.*;
import MessagePackage.*;

class TemplateSensor
{
	public static void main(String args[])
	{
		String MsgMgrIP;				// Message Manager IP address
		MessageManagerInterface em = null;// Interface object to the message manager
		int	Delay = 2500;				// The loop delay (2.5 seconds)
		boolean Done = false;			// Loop termination flag

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

			// We create a message window. 

			// TODO: Change position
			float WinPosX = 0.0f; 	//This is the X position of the message window in terms
								 	//of a percentage of the screen height
			// TODO: Change position
			float WinPosY = 0.0f; 	//This is the Y position of the message window in terms
								 	//of a percentage of the screen height

			// TODO: Define sensor name
			String sensorName = "";
			MessageWindow mw = new MessageWindow(sensorName, WinPosX, WinPosY );

			mw.WriteMessage("Registered with the message manager." );

	    	try
	    	{
				mw.WriteMessage("   Participant id: " + em.GetMyId() );
				mw.WriteMessage("   Registration Time: " + em.GetRegistrationTime() );

			} // try

	    	catch (Exception e)
			{
				mw.WriteMessage("Error:: " + e);

			} // catch

			// TODO: Init the values of the sensor
	    	
			/********************************************************************
			** Here we start the main simulation loop
			*********************************************************************/
	    	
			mw.WriteMessage("Beginning Simulation... ");

			// TODO: Modify the main simulation loop
			while ( !Done )
			{
				// TODO: Add sampling the environment
				
				// Here we wait for a 2.5 seconds before we start the next sample

				try
				{
					Thread.sleep( Delay );

				} // try

				catch( Exception e )
				{
					mw.WriteMessage("Sleep error:: " + e );

				} // catch

			} // while

		} else {

			System.out.println("Unable to register with the message manager.\n\n" );

		} // if

	} // main

} // TemplateSensor