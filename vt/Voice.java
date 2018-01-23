package vt;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;

/**
 * Test Voice activity
 */
public class Voice
{

	public static void main(String[] args)
	{
		String appMode = "car";
		if (args.length > 0) {
			appMode = args[0];
		}
		VoiceTest test = new VoiceTest();
		test.init(appMode);

		String testSource = "";
		if (args.length > 1) {
			testSource = args[1];
			System.out.println("Test source file: " + testSource);

			// Testing from file.
			File file = new File(testSource);
			BufferedReader reader = null;

			try {
				reader = new BufferedReader(new FileReader(file));
				String text = null;

				while ((text = reader.readLine()) != null) {
					test.test(text);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (reader != null) {
						reader.close();
					}
				} catch (IOException e) {
				}
			}
			
		} else {
			test.testNewRoute(1340.0, 420);
			test.testRecalc(19100.0, 12340);
		}
	}
}
