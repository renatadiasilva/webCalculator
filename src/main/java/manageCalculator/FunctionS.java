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
	
	public int getCount() {
		return count;
	}
	
	@Override
	public int compareTo(FunctionS f) {
		if (f.count == count) return name.compareTo(f.name);
		else return f.count-count;
	}
	
}