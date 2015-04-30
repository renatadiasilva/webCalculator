package manageCalculator;

public class FunctionS implements Comparable<FunctionS>{
	
	private int id;
	private String name;
	private int count = 0;
	
	public FunctionS(int i, String n) {
		id = i;
		name = n;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
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
		return f.count-count;
	}
	
}