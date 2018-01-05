package vt;

/**
 * Test Voice activity
 */
public class Voice
{

	public static void main(String[] args)
	{
		VoiceTest t = new VoiceTest();

		t.init();

		t.test(t.prepareStruct(t.C_ROUTE_NEW_CALC, 1340.0,  20)); // less 1 minute
		t.test(t.prepareStruct(t.C_ROUTE_NEW_CALC, 134.0,  70));  // 1 minute
		t.test(t.prepareStruct(t.C_ROUTE_NEW_CALC, 1340.0, 130)); // 2 minutes
		t.test(t.prepareStruct(t.C_ROUTE_NEW_CALC, 2340.0, 190));
		t.test(t.prepareStruct(t.C_ROUTE_NEW_CALC, 3340.0, 250));
		t.test(t.prepareStruct(t.C_ROUTE_NEW_CALC, 4340.0, 310));
		t.test(t.prepareStruct(t.C_ROUTE_NEW_CALC, 5340.0, 310));
		t.test(t.prepareStruct(t.C_ROUTE_NEW_CALC, 6340.0, 370));
		t.test(t.prepareStruct(t.C_ROUTE_NEW_CALC, 7340.0, 430));
		t.test(t.prepareStruct(t.C_ROUTE_NEW_CALC, 8340.0, 490));
		t.test(t.prepareStruct(t.C_ROUTE_NEW_CALC, 9340.0, 550));
		t.test(t.prepareStruct(t.C_ROUTE_NEW_CALC, 10340.0, 610));
		t.test(t.prepareStruct(t.C_ROUTE_NEW_CALC, 11340.0, 670));
		
	}
}
