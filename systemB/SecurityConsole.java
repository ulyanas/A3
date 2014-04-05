package systemB;

import MessagePackage.Message;
import TermioPackage.Termio;

public class SecurityConsole {
	public static void main(String args[])
	{
    	Termio UserInput = new Termio();	// Termio IO Object
		boolean Done = false;				// Main loop flag
		String Option = null;				// Menu choice from user
		Message Msg = null;					// Message object
		boolean Error = false;				// Error flag
		SecurityMonitor Monitor = null;			// The environmental control system monitor
		boolean isArmed = false;

		/////////////////////////////////////////////////////////////////////////////////
		// Get the IP address of the message manager
		/////////////////////////////////////////////////////////////////////////////////

 		if ( args.length != 0 )
 		{
			// message manager is not on the local system

			Monitor = new SecurityMonitor( args[0] );

		} else {

			Monitor = new SecurityMonitor();

		} // if


		// Here we check to see if registration worked. If ef is null then the
		// message manager interface was not properly created.

		if (Monitor.IsRegistered() )
		{
			Monitor.start(); // Here we start the monitoring and control thread

			while (!Done)
			{
				// Here, the main thread continues and provides the main menu

				System.out.println( "\n\n\n\n" );
				System.out.println( "Security Control System (SCS) Command Console: \n" );

				if (args.length != 0)
					System.out.println( "Using message manger at: " + args[0] + "\n" );
				else
					System.out.println( "Using local message manger \n" );

				System.out.println( "Select an Option: \n" );
				System.out.println( "1: Arm the security system" );
				System.out.println( "2: Disarm the security system" );
				System.out.println( "-------------------------------");
				System.out.println( "3: Disable Sprinkler" );
				System.out.println( "4: Turn on Sprinkler" );
				System.out.println( "-------------------------------");
				System.out.println( "X: Stop System\n" );
				System.out.print( "\n>>>> " );
				Option = UserInput.KeyboardReadString();


				if ( Option.equalsIgnoreCase( "1" ) ){
					System.out.println("Arm the security system");
					Monitor.setSecuritySystem(true);
					isArmed = true;
				} // if
				else if( Option.equalsIgnoreCase("2")){
					System.out.println("Disarm the security system");
					Monitor.setSecuritySystem(false);	
					isArmed = false;
				}
				else if( Option.equalsIgnoreCase("3")){
					if (isArmed) {
						Monitor.setSprinkler(0);
					}
					else {
						System.out.println("System is Disarmed");
					}		
				}
				else if( Option.equalsIgnoreCase("4")){
					if (isArmed) {
						Monitor.setSprinkler(1);
					}
					else {
						System.out.println("System is Disarmed");
					}		
				}
				else if ( Option.equalsIgnoreCase( "X" ) ){
					Monitor.Halt();
					Done = true;
					System.out.println( "\nConsole Stopped... Exit monitor mindow to return to command prompt." );
					Monitor.Halt();

				}
				else{
					System.out.println("Invalid input");
				}

			} // while

		} else {

			System.out.println("\n\nUnable start the monitor.\n\n" );

		} // if

  	} // main
}
