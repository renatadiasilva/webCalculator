package manageCalculator;

public class FunctionS implements Comparable<FunctionS>{
	
	private int id;
	private String name;
	private int count;
	
	public FunctionS(int i, String n) {
		id = i;
		count = 0;
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
	
	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public int compareTo(FunctionS f) {
		return f.count-count;
	}
	
}