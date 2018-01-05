package MainApp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import alice.tuprolog.Struct;
import alice.tuprolog.Term;


public class VoiceTest
{
	private List<Struct> listStruct = new ArrayList<Struct>();
	
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

}
