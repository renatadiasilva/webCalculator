package manageCalculator;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.faces.event.ActionEvent;

import net.objecthunter.exp4j.*;
import net.objecthunter.exp4j.function.Function;
import net.objecthunter.exp4j.operator.Operator;

import java.io.Serializable;

@Named
@SessionScoped
public class Calculator implements Serializable {
	private static final long serialVersionUID = -501006408565378935L;
	private String expression;
	private boolean clean;
	private boolean error;
	
	@Inject
	private Historic hist;
	
	@Inject
	private Statistics stat;
	
	public Calculator() {
		expression = "0";
		clean = true;
		error = false;
	}

	public String getExpression() {
		return expression;
	}
	
	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	public Historic getHist() {
		return hist;
	}

	public Statistics getStat() {
		return stat;
	}

	public void addToExpression(boolean delL, String add) {
		if (clean) expression = add;
		else if (delL) {
				if (expression.length() < 2) expression = "0";
				else expression = expression.substring(0, expression.length()-1);
			}
		else expression += add;
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
		            throw new IllegalArgumentException("The sine of an angle should be a value between -1 and 1");
		        }		    	
		    	return Math.asin(args[0])*180/Math.PI;
		    }
		};
		
		// Arccosine of an angle in degrees
		Function acosd = new Function("acosd", 1) {
		    @Override
		    public double apply(double... args) {
		        if (Math.abs(args[0]) > 1) {
		            throw new IllegalArgumentException("The cosine of an angle should be a value between -1 and 1");
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
		            throw new IllegalArgumentException("The logarithm must be positive");
		        }		    	
		        if (args[1] <= 0) {
		            throw new IllegalArgumentException("The logarithm base must be positive");
		        }		    	
		        return Math.log(args[0]) / Math.log(args[1]);
		    }
		};
		
		// Factorial
		Operator factorial = new Operator("!", 1, true, Operator.PRECEDENCE_POWER + 1) {

		    @Override
		    public double apply(double... args) {
		        final int arg = (int) args[0];
		        if ((double) arg != args[0]) {
		            throw new IllegalArgumentException("Operand for factorial has to be an integer");
		        }
		        if (arg < 0) {
		            throw new IllegalArgumentException("The operand of the factorial can not be less than zero");
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
		        return Math.log(args[0]) / Math.log(args[1]);
		    }
		};
		
		// Least common multiple LCM
		Function lcm = new Function("lcm", 2) {
		    @Override
		    public double apply(double... args) {
		        return Math.log(args[0]) / Math.log(args[1]);
		    }
		};

		// não contar na estatística
		
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

				// if the result is very small, close to zero
				if (Math.abs(r) < Math.pow(10, -10)) s = "0"; 
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
		if (!error && s.equals("NaN")) error = true;
		
		if (s == null) {
			s = "Mismatched parentheses detected. Please check the expression";
			error = true;
		}		
				
		return s;
				
	}
	
	public void key(ActionEvent event) {
		String add ="";

		// delete last digit/op
		boolean delLast = false;
		// use for statistics
		boolean op = false;
		// basic operations
		boolean basicOp = false;
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
		case "plus": add = "+"; basicOp = true; break;
		case "minus": add = "-"; basicOp = true; break;
		case "times": add = "*"; basicOp = true; break;
		case "divide": add = "/"; basicOp = true; break;
		case "square": add = "^2"; basicOp = true; break;
		case "cubic": add = "^3"; basicOp = true; break;
		case "power": add = "^"; basicOp = true; break;
		case "sqrt": add = "sqrt(";	op = true; break;
		case "cbrt": add = "cbrt("; op = true; break;
		case "mod": add = "%"; basicOp = true; break;
		case "sine": add = "sin("; op = true; break;
		case "cosine": add = "cos("; op = true; break;
		case "tangent": add = "tan("; op = true; break;
		case "sineD": add = "sind("; op = true;	break;
		case "cosineD": add = "cosd("; op = true; break;
		case "tangentD": add = "tand("; op = true; break;
		case "arcsine": add = "asin("; op = true; break;
		case "arccosine": add = "acos("; op = true; break;
		case "arctangent": add = "atan("; op = true; break;
		case "arcsineD": add = "asind("; op = true; break;
		case "arccosineD": add = "acosd("; op = true; break;
		case "arctangentD": add = "atand("; op = true; break;
		case "ln": add = "log("; op = true; break;
		case "log": add = "log10("; op = true; break;
		case "logb": add = "logb("; op = true; break;
		case "exp": add = "exp("; op = true; break;
		case "power10": add = "10^"; op = true; break;
		case "pi": add = "pi()"; break;
		case "factorial": add = "!"; basicOp = true; break;
		case "mdc": add = "mdc("; op = true; break;
		case "mmc": add = "mmc(";  op = true; break;

		// other
		case "lPar": add = "("; break;
		case "rPar": add = ")"; break;
		case "virg": add = ","; break;
		case "deleteL": add = "0"; delLast = true; break; 
		case "reset": add = "0"; clean = true; break;
		case "result": add = result(); r = true; clean = true;
			if (!error) hist.addToList(expression); break;
		}
		
		if (op || basicOp) stat.updateElement(button);
		
		if (expression.equals("0")) clean = !basicOp;

		addToExpression(delLast,add);
		
		clean = r;
					
	}

}
