package manageCalculator;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.faces.event.ActionEvent;

import net.objecthunter.exp4j.*;
import net.objecthunter.exp4j.function.Function;
import net.objecthunter.exp4j.operator.Operator;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Named
@SessionScoped
public class Calculator implements Serializable {
	private static final long serialVersionUID = -501006408565378935L;
	private String expression;
	private boolean radians;
	private boolean clean;
	private boolean error;
	
	@Inject
	private Historic hist;
	
	@Inject
	private Statistics stat;
	
	public Calculator() {
		expression = "0";
		radians = true;
		clean = true;
		error = false;
	}

	public String getExpression() {
		return expression;
	}
	
	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	public boolean isRadians() {
		return radians;
	}

	public void setRadians(boolean radians) {
		this.radians = radians;
	}

	public Historic getHist() {
		return hist;
	}

	public Statistics getStat() {
		return stat;
	}

	public String result() {
		
		// ***** New defined Functions - net.objecthunter.exp4j *****
		
		// Number PI
		Function pi = new Function("pi",0) {
			@Override
		    public double apply(double... args) {
		        return Math.PI;
		    }
		};
		
		// Sine of an angle in degrees
		Function sind = new Function("sind", 1) {
		    @Override
		    public double apply(double... args) {
		    	return Math.sin(args[0]*Math.PI/180);
		    }
		};

		// Cosine of an angle in degrees
		Function cosd = new Function("cosd", 1) {
		    @Override
		    public double apply(double... args) {
		    	return Math.cos(args[0]*Math.PI/180);
		    }
		};

		// Tangent of an angle in degrees
		Function tand = new Function("tand", 1) {
		    @Override
		    public double apply(double... args) {
		    	final int arg = (int) Math.abs(args[0]);
		    	// Tangent of 90+k180 is infinity
		        if (arg % 180 == 90) {
		            throw new IllegalArgumentException("Infinity");
		        }		    	
		    	return Math.tan(args[0]*Math.PI/180);
		    }
		};

		// Arcsine of an angle in degrees
		Function asind = new Function("asind", 1) {
		    @Override
		    public double apply(double... args) {
		        if (Math.abs(args[0]) > 1) {
		            throw new IllegalArgumentException("The sine of an angle should be a value between -1 and 1!");
		        }		    	
		    	return Math.asin(args[0])*180/Math.PI;
		    }
		};
		
		// Arccosine of an angle in degrees
		Function acosd = new Function("acosd", 1) {
		    @Override
		    public double apply(double... args) {
		        if (Math.abs(args[0]) > 1) {
		            throw new IllegalArgumentException("The cosine of an angle should be a value between -1 and 1!");
		        }		    	
		    	return Math.acos(args[0])*180/Math.PI;
		    }
		};

		// Arctangent of an angle in degrees
		Function atand = new Function("atand", 1) {
		    @Override
		    public double apply(double... args) {
		    	return Math.atan(args[0])*180/Math.PI;
		    }
		};
		
		// Logarithm of base b
		Function logb = new Function("logb", 2) {
		    @Override
		    public double apply(double... args) {
		        if (args[0] <= 0) {
		            throw new IllegalArgumentException("The logarithm must be positive!");
		        }		    	
		        if (args[1] <= 0) {
		            throw new IllegalArgumentException("The logarithm base must be positive!");
		        }		    	
		        return Math.log(args[0])/Math.log(args[1]);
		    }
		};
		
		// Factorial
		Operator factorial = new Operator("!", 1, true, Operator.PRECEDENCE_POWER + 1) {

		    @Override
		    public double apply(double... args) {
		        final int arg = (int) args[0];
		        if ((double) arg != args[0]) {
		            throw new IllegalArgumentException("Operand for factorial has to be an integer!");
		        }
		        if (arg < 0) {
		            throw new IllegalArgumentException("The operand of the factorial can not be less than zero!");
		        }
		        double fat = 1;
		        for (int i = 1; i <= arg; i++) {
		            fat *= i;
		        }
		        return fat;
		    }
		};

		// Greatest common divisor (mudar nome) GCD
		Function gcd = new Function("gcd", 2) {
		    @Override
		    public double apply(double... args) {
		        int arg1 = (int) args[0];
		        int arg2 = (int) args[1]; 
		        if ((double) arg1 != args[0] || (double) arg2 != args[1]) {
		            throw new IllegalArgumentException("Arguments for gcd have to be integers!");
		        }
		    	
		        return myGCD(arg1, arg2);
		    }
		};
		
		// Least common multiple LCM
		Function lcm = new Function("lcm", 2) {
		    @Override
		    public double apply(double... args) {
		        int arg1 = (int) args[0];
		        int arg2 = (int) args[1]; 
		        if ((double) arg1 != args[0] || (double) arg2 != args[1]) {
		            throw new IllegalArgumentException("Arguments for lcm have to be integers!");
		        }
		        
		        return arg1*arg2/myGCD(arg1, arg2);
		    }
		};

		// Compute the result of the expression
		String s = "";
		error = false;
		
		try {
			Expression e = new ExpressionBuilder(expression)
					.function(pi)
					.function(sind)
					.function(cosd)
					.function(tand)
					.function(asind)
					.function(acosd)
					.function(atand)
					.function(logb)
					.operator(factorial)
					.function(gcd)
					.function(lcm)
					.build();

			try{
				double r = e.evaluate();
				
				s = r+"";

				double tol = Math.pow(10, -10);
				// if the result is very small, close to zero
				if (Math.abs(r) < tol) s = "0"; 
				else if (Math.abs(Math.round(r)-r) < tol) s = Math.round(r)+"";
				// if result is integer get ride of ".0"
				else if (r % 1 == 0) s = s.substring(0, s.length()-2);
				

			} catch(Exception e1) {
				s = e1.getMessage();
				error = true;
			}		

		} catch (Exception e2) {
			s = e2.getMessage();
			error = true;
		}
		
		// Error Not a Number
		if (!error) if (s.equals("NaN")) error = true;
		
		if (s == null) {
			s = "Mismatched parentheses detected. Please check the expression!";
			error = true;
		}		
				
		return s;
				
	}

	public void addToExpression(boolean delL, String add) {
		if (clean) expression = add;
		else if (delL) {
				if (expression.length() < 2) expression = "0";
				else expression = expression.substring(0, expression.length()-1);
			}
		else expression += add;
	}
	
	public void key(ActionEvent event) {
		String add ="";

		// delete last digit/op
		boolean delLast = false;
		// operations to add when zero is displayed
		boolean op = false;
		// check if a computation was done
		boolean r = false;
		// function with more than one argument

		String button = event.getComponent().getId();
		
		switch (button) {
		
		// numbers
		case "number0": add = "0"; break;
		case "number1": add = "1"; break;
		case "number2": add = "2"; break;
		case "number3": add = "3"; break;
		case "number4": add = "4"; break;
		case "number5": add = "5"; break;
		case "number6": add = "6"; break;
		case "number7": add = "7"; break;
		case "number8": add = "8"; break;
		case "number9": add = "9"; break;
		case "decPoint": add = "."; break;
		
		// operations
		case "plus": add = "+"; op = true; break;
		case "minus": add = "-"; op = true; break;
		case "times": add = "*"; op = true; break;
		case "divide": add = "/"; op = true; break;
		case "square": add = "^2"; op = true; break;
		case "cubic": add = "^3"; op = true; break;
		case "power": add = "^"; op = true; break;
		case "sqrt": add = "sqrt(";	break;
		case "cbrt": add = "cbrt("; break;
		case "mod": add = "%"; op = true; break;
		case "sine": if (radians) add = "sin("; else add = "sind("; break;
		case "cosine": if (radians) add = "cos("; else add = "cosd("; break;
		case "tangent": if (radians) add = "tan("; else add = "tand("; break;
		case "arcsine": if (radians) add = "asin("; else add = "asind("; break;
		case "arccosine": if (radians) add = "acos("; else add = "acosd("; break;
		case "arctangent": if (radians) add = "atan("; else add = "atand("; break;
		case "ln": add = "log("; break;
		case "log": add = "log10("; break;
		case "logb": add = "logb("; break;
		case "exp": add = "exp("; break;
		case "power10": add = "10^"; break;
		case "pi": add = "pi()"; break;
		case "factorial": add = "!"; op = true; break;
		case "gcd": add = "gcd("; break;
		case "lcm": add = "lcm(";  break;

		// other
		case "lPar": add = "("; break;
		case "rPar": add = ")"; break;
		case "virg": add = ","; break;
		case "deleteL": add = "0"; delLast = true; break; 
		case "reset": add = "0"; clean = true; break;
		case "result": add = result(); r = true; clean = true;
			if (!error) {hist.addToList(expression); updateStat();}
			break;
		}
		
		if (expression.equals("0")) clean = !op;

		addToExpression(delLast,add);
		
		clean = r;
					
	}
	
	// Counts the number of times that an operation appear in an expression
	public int countsOperation(String op) {
	    Pattern p = Pattern.compile(op);
	    Matcher m = p.matcher(expression);
	    
	    int count = 0;
	    while (m.find()) {
	    	count++;
	    }
	    return count;
	}
	
	public void updateStat() {		
		
		// no conflits
		stat.updateElement("plus", countsOperation("\\+"));
		stat.updateElement("minus", countsOperation("\\-"));
		stat.updateElement("times", countsOperation("\\*"));
		stat.updateElement("divide", countsOperation("/"));
		stat.updateElement("sqrt", countsOperation("sqrt"));
		stat.updateElement("cbrt", countsOperation("cbrt"));
		stat.updateElement("mod", countsOperation("%"));
		stat.updateElement("exp", countsOperation("exp"));
		stat.updateElement("factorial", countsOperation("\\!"));
		stat.updateElement("gcd", countsOperation("gcd"));
		stat.updateElement("lcm", countsOperation("lcm"));
		
		// power
		int c1 = countsOperation("\\^2");
		int c2 = countsOperation("\\^3");
		int c3 = countsOperation("10\\^");
		stat.updateElement("square", c1);
		stat.updateElement("cubic", c2);
		stat.updateElement("power10", c3);
		stat.updateElement("power", countsOperation("\\^")-c1-c2-c3);
		
		// sine
		c1 = countsOperation("asind");
		stat.updateElement("arcsineD", c1);
		c2 = countsOperation("asin")-c1;
		stat.updateElement("arcsine", c2);
		c3 = countsOperation("sind")-c1;
		stat.updateElement("sineD", c3);
		stat.updateElement("sine", countsOperation("sin")-c1-c2-c3);
		
		// cosine
		c1 = countsOperation("acosd");
		stat.updateElement("arccosineD", c1);
		c2 = countsOperation("acos")-c1;
		stat.updateElement("arccosine", c2);
		c3 = countsOperation("cosd")-c1;
		stat.updateElement("cosineD", c3);
		stat.updateElement("cosine", countsOperation("cos")-c1-c2-c3);

		// tangent
		c1 = countsOperation("atand");
		stat.updateElement("arctangentD", c1);
		c2 = countsOperation("atan")-c1;
		stat.updateElement("arctangent", c2);
		c3 = countsOperation("tand")-c1;
		stat.updateElement("tangentD", c3);
		stat.updateElement("tangent", countsOperation("tan")-c1-c2-c3);
		
		// logaritmic
		c1 = countsOperation("log10");
		c2 = countsOperation("logb");
		stat.updateElement("log", c1);
		stat.updateElement("logb", c2);
		stat.updateElement("ln", countsOperation("log")-c1-c2);
	}
	
	// Auxiliar method for gcd and lcm
	private int myGCD(int a, int b) {
		int r;
		while (b != 0) {
			r = a % b;
			a = b;
			b = r;
		}

		return a;
	}

}
