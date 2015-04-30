package manageCalculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named
@ApplicationScoped
public class Statistics {
		
	HashMap<String, Integer> list =  new HashMap<String, Integer>();

	public Statistics() {
		list.put("plus",0);
		list.put("minus",0);
		list.put("times",0);
		list.put("divide",0);
		list.put("square",0);
		list.put("cubic",0);
		list.put("power",0);				
		list.put("sqrt",0);
		list.put("cbrt",0);
		list.put("mod",0);
		list.put("sine",0);
		list.put("cosine",0);
		list.put("tangent",0);
		list.put("sineD",0);
		list.put("cosineD",0);
		list.put("tangentD",0);
		list.put("arcsine",0);
		list.put("arccosine",0);
		list.put("arctangent",0);
		list.put("arcsineD",0);
		list.put("arccosineD",0);
		list.put("arctangentD",0);
		list.put("ln",0);
		list.put("log",0);
		list.put("logb",0);
		list.put("exp",0);
		list.put("power10",0);
		list.put("factorial",0);
		list.put("gcd",0);
		list.put("lcm",0);		
	}	
	
	public ArrayList<FunctionS> getList() {
		ArrayList<FunctionS> af = new ArrayList<FunctionS>();
		
		for (Map.Entry<String, Integer> e: list.entrySet()) {
			af.add(new FunctionS(e.getKey(),e.getValue()));
		}
		
		Collections.sort(af);
		return af;
	}

	public void updateElement(String name, int c) {
		list.put(name, list.get(name)+c);
	}
	
}