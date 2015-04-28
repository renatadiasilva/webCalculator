package manageCalculator;

import java.util.ArrayList;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named
@ApplicationScoped
public class Statistics {
		
	private ArrayList<Function> list;
//	private ArrayList<Function> list5;

	public Statistics() {
		list = new ArrayList<>();
		list.add(new Function(0, "plus"));
		list.add(new Function(1, "minus"));
		list.add(new Function(2, "times"));
		list.add(new Function(3, "divides"));
		list.add(new Function(4, "sqrt"));
		list.add(new Function(5, "square"));
		list.add(new Function(6, "inverse"));
//		list5 = new ArrayList<>();
	}

	public ArrayList<Function> getList() {
		return list;
	}

	public void setList(ArrayList<Function> l) {
		list = l;
	}
	
	public void updateElement(int id) {
		Function f = list.get(id);
		f.setCount(f.getCount()+1); 
	}
		
//	public ArrayList<String> getList5() {
//		list.sort(c);
//		return list5;
//	}
//
//	public void setList5(ArrayList<String> l5) {
//		list5 = l5;
//	}

}