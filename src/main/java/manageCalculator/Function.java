package manageCalculator;

public class Function {
	
	private int id;
	private String name;
	private int count;
	
	public Function(int i, String n) {
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
	
}