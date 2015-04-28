package manageCalculator;

import java.util.ArrayList;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class Historic implements Serializable {
	
	private static final long serialVersionUID = -282702558525389183L;
	private ArrayList<String> list;

	public Historic() {
		list = new ArrayList<>();
	}

	public ArrayList<String> getList() {
		return list;
	}

	public void setList(ArrayList<String> l) {
		list = l;
	}
	
	public void addToList(String s) {
		list.add(s);
	}

}
