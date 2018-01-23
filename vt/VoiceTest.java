package vt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Iterator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

// import org.apache.commons.logging.Log;
// import org.apache.commons.logging.LogFactory;

import alice.tuprolog.InvalidLibraryException;
import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.NoSolutionException;
import alice.tuprolog.Number;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import alice.tuprolog.Theory;
import alice.tuprolog.Var;



public class VoiceTest
{
	private List<Struct> listStruct = new ArrayList<Struct>();

	protected static final String C_PREPARE_TURN = "prepare_turn";  //$NON-NLS-1$
	protected static final String C_PREPARE_ROUNDABOUT = "prepare_roundabout";  //$NON-NLS-1$
	protected static final String C_PREPARE_MAKE_UT = "prepare_make_ut";  //$NON-NLS-1$
	protected static final String C_ROUNDABOUT = "roundabout";  //$NON-NLS-1$
	protected static final String C_GO_AHEAD = "go_ahead";  //$NON-NLS-1$
	protected static final String C_TURN = "turn";  //$NON-NLS-1$
	protected static final String C_MAKE_UT = "make_ut";  //$NON-NLS-1$
	protected static final String C_MAKE_UTWP = "make_ut_wp";  //$NON-NLS-1$
	protected static final String C_AND_ARRIVE_DESTINATION = "and_arrive_destination";  //$NON-NLS-1$
	protected static final String C_REACHED_DESTINATION = "reached_destination";  //$NON-NLS-1$
	protected static final String C_AND_ARRIVE_INTERMEDIATE = "and_arrive_intermediate";  //$NON-NLS-1$
	protected static final String C_REACHED_INTERMEDIATE = "reached_intermediate";  //$NON-NLS-1$
	protected static final String C_AND_ARRIVE_WAYPOINT = "and_arrive_waypoint";  //$NON-NLS-1$
	protected static final String C_AND_ARRIVE_FAVORITE = "and_arrive_favorite";  //$NON-NLS-1$
	protected static final String C_AND_ARRIVE_POI_WAYPOINT = "and_arrive_poi";  //$NON-NLS-1$
	protected static final String C_REACHED_WAYPOINT = "reached_waypoint";  //$NON-NLS-1$
	protected static final String C_REACHED_FAVORITE = "reached_favorite";  //$NON-NLS-1$
	protected static final String C_REACHED_POI = "reached_poi";  //$NON-NLS-1$
	protected static final String C_THEN = "then";  //$NON-NLS-1$
	protected static final String C_SPEAD_ALARM = "speed_alarm";  //$NON-NLS-1$
	protected static final String C_ATTENTION = "attention";  //$NON-NLS-1$
	protected static final String C_OFF_ROUTE = "off_route";  //$NON-NLS-1$
	protected static final String C_BACK_ON_ROUTE ="back_on_route"; //$NON-NLS-1$
	
	
	protected static final String C_BEAR_LEFT = "bear_left";  //$NON-NLS-1$
	protected static final String C_BEAR_RIGHT = "bear_right";  //$NON-NLS-1$
	protected static final String C_ROUTE_RECALC = "route_recalc";  //$NON-NLS-1$
	protected static final String C_ROUTE_NEW_CALC = "route_new_calc";  //$NON-NLS-1$
	protected static final String C_LOCATION_LOST = "location_lost";  //$NON-NLS-1$
	protected static final String C_LOCATION_RECOVERED = "location_recovered";  //$NON-NLS-1$

	protected Prolog prologSystem;

	protected static final String P_VERSION = "version";
	protected static final String P_RESOLVE = "resolve";

	private static int currentVersion;
	protected String language = "";

	public List<Struct> alt(Struct... s1) {
		if (s1.length == 1) {
			listStruct.add(s1[0]);
		} else {
			listStruct.add(new Struct(s1));
		}
		return listStruct;
	}

	public Struct prepareStruct(String name, Object... args) {
		Term[] list = new Term[args.length];
		for (int i = 0; i < args.length; i++) {
			Object o = args[i];
			if(o instanceof Term){
				list[i] = (Term) o;
			} else if(o instanceof java.lang.Number){
				if(o instanceof java.lang.Double){
					list[i] = new alice.tuprolog.Double((Double) o);
				} else if(o instanceof java.lang.Float){
					list[i] = new alice.tuprolog.Float((Float) o);
				} else if(o instanceof java.lang.Long){
					list[i] = new alice.tuprolog.Long((Long) o);
				} else {
					list[i] = new alice.tuprolog.Int(((java.lang.Number)o).intValue());
				}
			} else if(o instanceof String){
				list[i] = new Struct((String) o);
			}
			if(o == null){
				list[i] = new Struct("");
			}
		}
		Struct struct = new Struct(name, list);

		return struct;
	}

	public void init(String appMode)
	{
		try {
			prologSystem = new Prolog(getLibraries());
		} catch (InvalidLibraryException e) {
			throw new RuntimeException(e);
		}

		prologSystem.clearTheory();

		boolean wrong = false;

		try {
			InputStream config;
			config = new FileInputStream(new File(".", "ttsconfig.p")); //$NON-NLS-1$
			
			prologSystem.getTheoryManager()
				.assertA(new Struct("appMode", new Struct(appMode)), true, "", true);
			
			prologSystem.addTheory(new Theory("measure('km-m')."));
			prologSystem.addTheory(new Theory(config));
			
			config.close();
		} catch (InvalidTheoryException e) {
			System.out.println("1. Loading voice config exception: " + e.toString() + e.getStackTrace().toString()); //$NON-NLS-1$
			wrong = true;
		} catch (IOException e) {
			System.out.println("2. Loading voice config exception: " + e.toString()); //$NON-NLS-1$
			wrong = true;
		}

		if (wrong) {
			System.out.println("Unable to work");
			return;
		}

		Term val = solveSimplePredicate(P_VERSION);
		currentVersion = ((Number)val).intValue();
		System.out.println("File version: " + currentVersion);

		Term langVal = solveSimplePredicate("language");
		if (langVal instanceof Struct) {
			language = ((Struct) langVal).getName();
		}
		System.out.println("Language: " + language);

		System.out.println("");
	}

	public String[] getLibraries(){
		return new String[] { "alice.tuprolog.lib.BasicLibrary",
					"alice.tuprolog.lib.ISOLibrary"/*, "alice.tuprolog.lib.IOLibrary"*/};
	}

	protected Term solveSimplePredicate(String predicate) {
		Term val = null;
		Var v = new Var("MyVariable"); //$NON-NLS-1$
		SolveInfo s = prologSystem.solve(new Struct(predicate, v));
		if (s.isSuccess()) {
			prologSystem.solveEnd();
			try {
				val = s.getVarValue(v.getName());
			} catch (NoSolutionException e) {
			}
		}
		return val;
	}

	public List<String> execute(List<Struct> listCmd)
	{
		Struct list = new Struct(listCmd.toArray(new Term[listCmd.size()]));
		Var result = new Var("RESULT"); //$NON-NLS-1$
		List<String> files = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		if(prologSystem == null) {
			return files;
		}

		System.out.println("Query speak files " + listCmd);

		SolveInfo res = prologSystem.solve(new Struct(P_RESOLVE, list, result));

		if (res.isSuccess()) {
			try {
				prologSystem.solveEnd();
				Term solution = res.getVarValue(result.getName());

				Iterator<?> listIterator = ((Struct) solution).listIterator();
				while(listIterator.hasNext()){
					Object term = listIterator.next();
					if(term instanceof Struct){
						files.add(((Struct) term).getName());
						sb.append(((Struct) term).getName());
					}
				}

			} catch (NoSolutionException e) {
			}
		}
		System.out.println("Speak phrase: \"" + sb.toString() + "\"");

		System.out.println("");
		return files;
	}	

	public void test(Struct testVoiceParam)
	{
		execute(alt(testVoiceParam));
		listStruct.clear();
	}

	public void test(String command)
	{
		String[] tokens = command.split("[,]");
		switch (tokens[0]) {
		case C_ROUTE_NEW_CALC:
		case C_ROUTE_RECALC:
			// 1 - dist, 2 -- time
			test(prepareStruct(tokens[0], new Double(tokens[1]), new Integer(tokens[2])));
		break;
			
		default:
			System.out.println("Unknown command: " + command);
			break;
		}
		listStruct.clear();
	}

	public void testNewRoute(double dist, int time)
	{
		test(prepareStruct(C_ROUTE_NEW_CALC, dist, time));
	}

	public void testRecalc(double dist, int time)
	{
		test(prepareStruct(C_ROUTE_RECALC, dist, time));
	}

}
