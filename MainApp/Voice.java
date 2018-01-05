package MainApp;

import MainApp.VoiceTest;

/**
 * Test Voice activity
 */
public class Voice
{

	protected static final String C_ROUTE_NEW_CALC = "route_new_calc";  //$NON-NLS-1$

	public static void main(String[] args)
	{
		System.out.println("Hello world!");

		VoiceTest vt = new VoiceTest();

		vt.prepareStruct(C_ROUTE_NEW_CALC, 1340.0, 123);
		
	}
}
