package manageCalculator;

import java.util.ArrayList;
import java.util.Collections;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named
@ApplicationScoped
public class Statistics {
		
	private ArrayList<FunctionS> list;

	public Statistics() {
		list = new ArrayList<>();

		int cf = 0;
		list.add(new FunctionS(cf, "plus"));
		cf++;
		list.add(new FunctionS(cf, "minus"));
		cf++;
		list.add(new FunctionS(cf, "times"));
		cf++;
		list.add(new FunctionS(cf, "divides"));
		cf++;
		list.add(new FunctionS(cf, "inverse"));
		cf++;
		list.add(new FunctionS(cf, "square"));
		cf++;
		list.add(new FunctionS(cf, "cubic"));
		cf++;
		list.add(new FunctionS(cf, "power"));				
		cf++;
		list.add(new FunctionS(cf, "square root"));
		cf++;
		list.add(new FunctionS(cf, "cubic root"));
		cf++;
		list.add(new FunctionS(cf, "remainder"));
		cf++;
		list.add(new FunctionS(cf, "sine"));
		cf++;
		list.add(new FunctionS(cf, "cosine"));
		cf++;
		list.add(new FunctionS(cf, "tangent"));
		cf++;
		list.add(new FunctionS(cf, "sine (degrees)"));
		cf++;
		list.add(new FunctionS(cf, "cosine (degrees)"));
		cf++;
		list.add(new FunctionS(cf, "tangent (degrees)"));
		cf++;
		list.add(new FunctionS(cf, "arcsine"));
		cf++;
		list.add(new FunctionS(cf, "arccosine"));
		cf++;
		list.add(new FunctionS(cf, "arctangent"));
		cf++;
		list.add(new FunctionS(cf, "arcsine (degrees)"));
		cf++;
		list.add(new FunctionS(cf, "arccosine (degrees)"));
		cf++;
		list.add(new FunctionS(cf, "arctangent (degrees)"));
		cf++;
		list.add(new FunctionS(cf, "ln"));
		cf++;
		list.add(new FunctionS(cf, "log"));
		cf++;
		list.add(new FunctionS(cf, "log base x"));
		cf++;
		list.add(new FunctionS(cf, "exp"));
		cf++;
		list.add(new FunctionS(cf, "power of 10"));
		cf++;
		list.add(new FunctionS(cf, "pi")); //???
		cf++;
		list.add(new FunctionS(cf, "factorial"));
		cf++;
		list.add(new FunctionS(cf, "mdc"));
		cf++;
		list.add(new FunctionS(cf, "mmc"));		
	}	
	
	public ArrayList<FunctionS> getList() {
		Collections.sort(list);
		return list;
	}

	public void setList(ArrayList<FunctionS> l) {
		list = l;
	}
	
	public void updateElement(int id) {
		FunctionS f = list.get(id);
		int c = f.getCount();
		f.setCount(c+1); 
	}
		
}