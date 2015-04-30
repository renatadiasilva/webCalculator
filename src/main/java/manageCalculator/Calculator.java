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
	
	@Inject
	private Historic hist;
	
	@Inject
	private Statistics stat;
	
	public Calculator() {
		expression = "0";
		clean = true;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String ex) {
		expression = ex;
	}
	
	public Historic getHist() {
		return hist;
	}

	public void setHist(Historic hist) {
		this.hist = hist;
	}

	public Statistics getStat() {
		return stat;
	}

	public void setStat(Statistics stat) {
		this.stat = stat;
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
		
		String ex = expression, s = "";
//		Expression e = new ExpressionBuilder("0").build();
		
		//codigos das funções definidas
		
//		func="tan";
//		pi = 3.1415926535897932384626433832795
//		ex = func+"(3.1415926535897932384626433832795)";
//		ex = "10%4";
//		ex = "cbrt(-8)";  //erro n anotar pfff
		
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
		        double result = 1;
		        for (int i = 1; i <= arg; i++) {
		            result *= i;
		        }
		        return result;
		    }
		};

		Function mdc = new Function("mdc", 2) {
		    @Override
		    public double apply(double... args) {
		        return Math.log(args[0]) / Math.log(args[1]);
		    }
		};
		
		Function mmc = new Function("mmc", 2) {
		    @Override
		    public double apply(double... args) {
		        return Math.log(args[0]) / Math.log(args[1]);
		    }
		};
		
		Function logb = new Function("logb", 2) {
		    @Override
		    public double apply(double... args) {
		        return Math.log(args[0]) / Math.log(args[1]);
		    }
		};
		
		Function pi = new Function("pi",0) {
			@Override
		    public double apply(double... args) {
		        return Math.PI;
		    }
		};
		
		Function sind = new Function("sind", 1) {
		    @Override
		    public double apply(double... args) {
		    	return Math.sin(args[0]*Math.PI/180);
		    }
		};

		Function cosd = new Function("cosd", 1) {
		    @Override
		    public double apply(double... args) {
		    	return Math.cos(args[0]*Math.PI/180);
		    }
		};

		Function tand = new Function("tand", 1) {
		    @Override
		    public double apply(double... args) {
		    	return Math.tan(args[0]*Math.PI/180);
		    }
		};

		Function asind = new Function("asind", 1) {
		    @Override
		    public double apply(double... args) {
		    	return Math.asin(args[0])*180/Math.PI;
		    }
		};
		
		Function acosd = new Function("acosd", 1) {
		    @Override
		    public double apply(double... args) {
		    	return Math.acos(args[0])*180/Math.PI;
		    }
		};

		Function atand = new Function("atand", 1) {
		    @Override
		    public double apply(double... args) {
		    	return Math.atan(args[0])*180/Math.PI;
		    }
		};

//		e = new ExpressionBuilder("pi").function(pi).build();
		
//		e = new ExpressionBuilder("cosg(60)").function(cosg).build();

//		e = new ExpressionBuilder("2.5!").operator(factorial).build();

//		ex = "2cos(0)";
		
		try {
			Expression e = new ExpressionBuilder(ex).build();
//			e = new ExpressionBuilder("sqrt(2").build();
			//		e = new ExpressionBuilder("logb(8, 2)").function(logb).build();

			try{
				double r = e.evaluate();

				s = r+"";
				if (r % 1 == 0) s = s.substring(0, s.length()-2);

			} catch(Exception e1) {
				s = e1.getMessage();
			}		

		} catch (Exception e2) {
			s = e2.getMessage();
		}
		
		// tem de se carregar duas vezes no 1/x??
		
		return s;
				
	}
	
	public void key(ActionEvent event) {
		String add ="";

		// delete last digit/op
		boolean delLast = false;
		// use for statistics
		int codef = 0;
		boolean op = false;
		// basic operations
		boolean basicOp = false;
		// check if a computation was done
		boolean r = false;
		
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
		case "plus": add = "+"; 
			basicOp = true;
			break;
		case "minus": add = "-";
			basicOp = true;
			break;
		case "times": add = "*";
			basicOp = true;
			break;
		case "divide": add = "/"; 
			basicOp = true;
			break;
		// tirar
		case "inverse": add = "1/";
			op = true;
			break;
		case "square": add = "^2"; 
			basicOp = true;
			break;
		case "cubic": add = "^3"; 
			basicOp = true;
			break;
		case "power": add = "^"; 
			basicOp = true;
			break;
		case "sqrt": add = "sqrt("; // boolean para fechar
			op = true;
			break;
		case "cbrt": add = "cbrt("; // boolean para fechar 
			op = true;
			break;
		case "mod": add = "%";
			basicOp = true;
			break;
		case "sine": add = "sin("; // boolean para fechar
			op = true;
			break;
		case "cosine": add = "cos("; // boolean para fechar
			op = true;
			break;
		case "tangent": add = "tan("; // boolean para fechar
			op = true;
			break;
		case "sineD": add = "sind("; // boolean para fechar
			op = true;
			break;
		case "cosineD": add = "cosd("; // boolean para fechar
			op = true;
			break;
		case "tangentD": add = "tand("; // boolean para fechar
			op = true;
			break;
		case "arcsine": add = "asin("; // boolean para fechar
			op = true;
			break;
		case "arccosine": add = "acos("; // boolean para fechar
			op = true;
			break;
		case "arctangent": add = "atan("; // boolean para fechar
			op = true;
			break;
		case "arcsineD": add = "asind("; // boolean para fechar
			op = true;
			break;
		case "arccosineD": add = "acosd("; // boolean para fechar
			op = true;
			break;
		case "arctangentD": add = "atand("; // boolean para fechar
			op = true;
			break;
		case "ln": add = "log("; // boolean para fechar
			op = true;
			break;
		case "log": add = "log10("; // boolean para fechar
			op = true;
			break;
		case "logb": add = "logb("; // boolean para meio e fechar
			op = true;
			break;
		case "exp": add = "exp("; // boolean para fechar
			op = true;
			break;
		case "power10": add = "10^";
			op = true;
			break;
		case "pi": add = "pi"; //parentesis?
			op = true;
			break;
		case "factorial": add = "!";
			basicOp = true;
			break;
		case "mdc": add = "mdc("; // boolean para meio e fechar
			op = true;
			break;
		case "mmc": add = "mmc("; // boolean para meio e fechar
			op = true;
			break;

		// other
		case "lPar": add = "("; break;
		case "rPar": add = ")"; break;
		case "deleteL": add = "0"; delLast = true; break;  // não apagar se for o último, por zero
		case "reset": add = "0"; clean = true; break;
		case "result": add = result();
			r = true;
			clean = true;
			hist.addToList(expression);
			break;
		}

		if (op || basicOp) stat.updateElement(button); // verificar estatisticas
		
		if (expression.equals("0")) clean = !basicOp;

		addToExpression(delLast,add);
		
		clean = r;
					
	}

}
