package systemC;

import InstrumentationPackage.*;
import MessagePackage.*;
import Configuration.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
class DoorSensor
{
	private MessageWindow mw = null;

	public DoorSensor(String title, float winPosX, float winPosY, final MessageManagerInterface em) {
		mw = new MessageWindow(title, winPosX, winPosY);
		JButton button = new JButton();
		button.setText("Door Sensor");
		button.setEnabled(true);
		button.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				Message msg = new Message( Configuration.DOOR_SENSOR_ID, "DB" );
				// Here we send the message to the message manager.
				try
				{
					em.SendMessage( msg );
					mw.WriteMessage("Detected Door Break!");
				} // try
				catch (Exception e)
				{
					System.out.println( "Door Sensor Error:: " + e );
				} // catch
			}
		});
		JFrame buttonFrame = new JFrame();
		buttonFrame.setBounds(mw.GetX(), mw.GetY(), 50, 80);
		JPanel buttonPanel = new JPanel();
		buttonFrame.add(buttonPanel);
		buttonPanel.add(button);
		buttonFrame.setVisible(true);
	}
	public MessageWindow getMessageWindow(){
		return this.mw;
	}
	public static void main(String args[])
	{
		MessageManagerInterface em = null;
		String MsgMgrIP;					// Message Manager IP address
		Message Msg = null;					// Message object
		MessageQueue eq = null;				// Message Queue
		int MsgId = 0;						// User specified message ID
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
			float WinPosX = 0.5f; 	//This is the X position of the message window in terms
			//of a percentage of the screen height
			float WinPosY = 0.60f;	//This is the Y position of the message window in terms
			//of a percentage of the screen height

			DoorSensor sensor = new DoorSensor("Door Sensor", WinPosX, WinPosY, em);
			sensor.getMessageWindow().WriteMessage("Registered with the message manager." );
			/********************************************************************
			** Here we start the main simulation loop
			*********************************************************************/

			sensor.getMessageWindow().WriteMessage("Beginning Simulation... ");


			while ( !Done )
			{
				
				// Detect the heartbeat for the door sensor 
                ServiceMonitor.detectHeartbeat(em, "DS01 - Door Sensor");
				
				// Get the message queue
				try
				{
					eq = em.GetMessageQueue();

				} // try

				catch( Exception e )
				{
					sensor.getMessageWindow().WriteMessage("Error getting message queue::" + e );

				} // catch

				int qlen = eq.GetSize();

				for ( int i = 0; i < qlen; i++ )
				{
					Msg = eq.GetMessage();

					if ( Msg.GetMessageId() == 99 )
					{
						Done = true;
						sensor.getMessageWindow().WriteMessage("Received End Message" );
						try
						{
							em.UnRegister();

				    	} // try

				    	catch (Exception e)
				    	{
				    		sensor.getMessageWindow().WriteMessage("Error unregistering: " + e);

				    	} // catch

						sensor.getMessageWindow().WriteMessage("\n\nSimulation Stopped. \n");

					} // if

				} // for


				// Here we wait for a 2.5 seconds before we start the next sample

				try
				{
					Thread.sleep( Delay );

				} // try

				catch( Exception e )
				{
					sensor.getMessageWindow().WriteMessage("Sleep error:: " + e );

				} // catch

			} // while
			
		} else {

			System.out.println("Unable to register with the message manager.\n\n" );

		} // if

	} // main

} // Door Sensor