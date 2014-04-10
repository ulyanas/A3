package systemC;

import MessagePackage.Message;
import TermioPackage.Termio;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.awt.Color;
import java.util.Map.Entry;
import InstrumentationPackage.Indicator;


public class MaintenanceConsole {
	
	
	public static void main(String args[])
	{
    	Termio UserInput = new Termio();	// Termio IO Object
		boolean Done = false;				// Main loop flag
		String Option = null;				// Menu choice from user
		Message et = null;					// Message object
		boolean Error = false;				// Error flag
		ServiceMonitor Monitor = null;
		int delay = 2500;
		
		// Get the IP address of the message manager
		
		if ( args.length != 0 )
 		{
			// event manager is not on the local system
           Monitor = new ServiceMonitor( args[0] );

		} else {

			Monitor = new ServiceMonitor();

		} // if
  
        if (Monitor.IsRegistered() )
		{
			Monitor.start(); 

			while (!Done)
			{
				// Here, the main thread continues and provides the main menu

				System.out.println( "\n\n\n\n" );
				System.out.println( "Service Maintenance Console: \n" );

				if (args.length != 0)
					System.out.println( "Using message manger at: " + args[0] + "\n" );
				else
					System.out.println( "Using local message manager \n" );

				System.out.println( "Select an Option: \n" );
				// Listing the installed equipments
				System.out.println( "1: List equipment list" );
				// Stopping the system
				System.out.println( "X: Stop System\n" );
				System.out.print( "\n>>>> " );
				Option = UserInput.KeyboardReadString();
                
				//////////// option 1 ////////////

				if ( Option.equals( "1" ) )
				{
					for (Entry<String, Indicator> obj: Monitor.installedEquipment.entrySet()) {
						if (obj.getValue().getIluminationColor().equals(Color.green)) {
							System.out.println(obj.getKey() + " -> Connected");
						} else if (obj.getValue().getIluminationColor().equals(Color.red)) {
							System.out.println(obj.getKey() + " -> Not Connected");
						} else {
							System.out.println(obj.getKey() + " -> Cannot Recognize" );
						}
					}

				} // if
				
				//////////// option X ////////////

				if ( Option.equalsIgnoreCase( "X" ) )
				{
					// Here the user is done, so we set the Done flag and halt
					// the environmental control system. The monitor provides a method
					// to do this. Its important to have processes release their queues
					// with the event manager. If these queues are not released these
					// become dead queues and they collect events and will eventually
					// cause problems for the event manager.

					Monitor.Halt();
					Done = true;
					System.out.println( "\nConsole Stopped... Exit monitor window to return to command prompt." );
					Monitor.Halt();

				} // if
				
			} // while

		}//if
		
        else {

			System.out.println("\n\nUnable start the monitor.\n\n" );

		    }
       			 

  	} // main
}
