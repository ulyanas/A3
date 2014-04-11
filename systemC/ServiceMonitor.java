package systemC;

import MessagePackage.*;
import InstrumentationPackage.*;
import Configuration.*;
import java.util.*;
import java.util.Map.Entry;
import java.awt.Color;
// To distinguish between the message manager and the event manager , we call messages as Events 
 
public class ServiceMonitor extends Thread
{
	private MessageManagerInterface em = null;// Interface object to the event manager ( actually the message manager)
	private String EvtMgrIP = null;			// Event Manager IP address
	boolean Registered = true;				// Signifies that this class is registered with an event manager.
	MessageWindow mw = null;				// This is the message window
	
	Map<String,Indicator> installedEquipment;
	
	public ServiceMonitor()
	{
		// message manager is on the local system

		try
		{
			// Here we create an message manager interface object. This assumes
			// that the event manager is on the local machine

			em = new MessageManagerInterface();
		}

		catch (Exception e)
		{
			System.out.println("ServiceMonitor::Error instantiating event manager interface: " + e);
			Registered = false;

		} // catch

	} //Constructor Ends

	public ServiceMonitor( String EvtIpAddress )
	{
		// event manager is not on the local system

		EvtMgrIP = EvtIpAddress;

		try
		{
			// Here we create an message manager interface object. This assumes
			// that the event manager is NOT on the local machine

			em = new MessageManagerInterface( EvtMgrIP );
		}

		catch (Exception e)
		{
			System.out.println("ServiceMonitor::Error instantiating event manager interface: " + e);
			Registered = false;

		} // catch

	} // Constructor

	public void run()
	{
		Message Evt = null;				// Event object
		MessageQueue eq = null;			// Message Queue
		int EvtId = 0;					// User specified event ID
		int	Delay = 2000;				// The loop delay
		boolean Done = false;			// Loop termination flag

		if (em != null)
		{

			installedEquipment = new HashMap<String,Indicator >();
			mw = new MessageWindow("Service Maintenance Monitoring Console", 0, 0);

			mw.WriteMessage( "Registered with the event manager." );

	    	try
	    	{
				mw.WriteMessage("   Participant id: " + em.GetMyId() );
				mw.WriteMessage("   Registration Time: " + em.GetRegistrationTime() );

			} // try

	    	catch (Exception e)
			{
				System.out.println("Error:: " + e);

			} // catch


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

		
				int qlen = eq.GetSize();

				for ( int i = 0; i < qlen; i++ )
				{
					Evt = eq.GetMessage();
                     mw.WriteMessage( "Dummy test - " + Evt.GetMessageId());
					if ( Evt.GetMessageId() == 31 ) // Sensor Pulse
					{
						try
						{
							String equipDesc = Evt.GetMessage();
							
							//  create new indicator and add to the map in case of a new equipment
							
							if (!installedEquipment.containsKey(equipDesc)) {
								Indicator ind = new Indicator (equipDesc, mw.GetX()+ mw.Width(), 
										installedEquipment.size()*75, 1 );
								
								installedEquipment.put(equipDesc, ind);
								
								mw.WriteMessage( "Registered new sensor - " + equipDesc);
							} else { // if it is a existed equipment, update the lastUpdateDate property.
								installedEquipment.get(equipDesc).SetLampColor(1);
								installedEquipment.get(equipDesc).setLastUpdate(System.currentTimeMillis());
								mw.WriteMessage( "Received heartbeat from sensor - " + equipDesc);
							}
						} // try

						catch( Exception e )
						{
							mw.WriteMessage("Error reading sensor pulse: " + e);

						} // catch

					} // if

					// If the event ID == 99 then this is a signal that the simulation
					// is to end. At this point, the loop termination flag is set to
					// true and this process unregisters from the message manager.

					if ( Evt.GetMessageId() == 99 )
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

				    	for (Entry<String, Indicator> obj : installedEquipment.entrySet()) {
				    		obj.getValue().dispose();
						}
				    	installedEquipment.clear();
				    	
				    	break;
					} // if

				} // for
				
				// Check for the equipment which is up and running.
				// Equipment which does not send the heartbeat( pulse back ) is considered to be disconnected from the system
				long currentTime = System.currentTimeMillis();
				long timeInterval = 0;
				for (Entry<String, Indicator> obj : installedEquipment.entrySet()) {
				    timeInterval = currentTime - obj.getValue().getLastUpdate();
					if (timeInterval > 2 * Delay ) {
						if (!obj.getValue().getIluminationColor().equals(Color.red)) {
							obj.getValue().SetLampColorAndMessage(obj.getKey(), 3);
							mw.WriteMessage( "A sensor was disconnected - " + obj.getKey());					
						}
					}
				}

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

			System.out.println("Unable to register with the event manager.\n\n" );

		} // if

	} // main

	// Taken from the ECSMonitor.java
	public boolean IsRegistered()
	{
		return( Registered );

	} 

	//Taken from ECSMonitor.java
	public void Halt()
	{
		mw.WriteMessage( "***HALT MESSAGE RECEIVED - SHUTTING DOWN SYSTEM***" );

		// Here we create the stop event.

		Message evt;

		evt = new Message( (int) 99, "XXX" );

		// Here we send the event to the event manager.

		try
		{
			em.SendMessage( evt );

		} // try

		catch (Exception e)
		{
			System.out.println("Error sending halt message:: " + e);

		} // catch

	} // Halt
	
	
  public static void detectHeartbeat(MessageManagerInterface msg, String nameDesc)
	{
		// Here we create the message.

		Message evt = new Message( (int) 31, nameDesc );

		// Here we send the message to the message manager.

		try
		{
			msg.SendMessage( evt );		

		} // try

		catch (Exception e)
		{
			System.out.println( "Error Posting pulse:: " + e );

		} // catch

	} // detectHeartbeat	

} // ServiceMonitor