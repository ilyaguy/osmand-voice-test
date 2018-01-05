package vt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

	protected static final String C_ROUTE_NEW_CALC = "route_new_calc";  //$NON-NLS-1$

	protected Prolog prologSystem;

	protected static final String P_VERSION = "version";
	protected static final String P_RESOLVE = "resolve";

	private static int currentVersion;
	protected String language = "";


	public VoiceTest alt(Struct... s1) {
		if (s1.length == 1) {
			listStruct.add(s1[0]);
		} else {
			listStruct.add(new Struct(s1));
		}
		return this;
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

		System.out.println("Adding command : " + name + " " + Arrays.toString(args)); //$NON-NLS-1$ //$NON-NLS-2$
		
		return struct;
	}

	public void test()
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
			config = new FileInputStream(new File("/Users/illiahai/src/OsmAnd-test", "ttsconfig.p")); //$NON-NLS-1$
			
			prologSystem.getTheoryManager()
				.assertA(new Struct("appMode", new Struct("car")), true, "", true);
			
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

		prepareStruct(C_ROUTE_NEW_CALC, 1340.0, 123);
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

}
