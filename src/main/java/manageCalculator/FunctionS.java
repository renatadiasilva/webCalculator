package manageCalculator;

public class FunctionS implements Comparable<FunctionS>{
	
	private String name;
	private int count;
	
	public FunctionS(String n, int c) {
		name = n;
		count = c;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getCount() {
		return count;
	}
	
	public void setCount(int c) {
		count = c;
	}

	@Override
	public int compareTo(FunctionS f) {
		if (f.count == count) return name.compareTo(f.name);
		else return f.count-count;
	}
	
}