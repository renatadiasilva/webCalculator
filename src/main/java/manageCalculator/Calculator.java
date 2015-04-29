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
	
	public void addToExpression(int code, String add) {
		if (clean && code != 4) expression=add;
		else {
			switch (code) {
			case 0: expression += add; break;
			case 1: expression = add; break;
			case 2: if (expression.length() < 2) expression ="0";
					else if (0 <= expression.length()-1) 
						expression = expression.substring(0, expression.length()-1); break;
			case 3: if (1 <= expression.length()) {
						char first = expression.charAt(0);
						if (first == '0') expression="0"; 
						else if (first == '-') expression = expression.substring(1, expression.length());
						else expression = "-"+expression; 
					}
					break;
			case 4: expression = add; break;
			}
		}
	}
	
	public String result(int code, String func) {
		
		String ex = expression, s = "";
		Expression e = new ExpressionBuilder("0").build();

		if (code != 0) {
			if (!error) ex = func+"("+expression+")";
			else return "Não pode calcular "+func+" de um erro!";
		}
			
//		func="tan";
//		pi = 3.1415926535897932384626433832795
//		ex = func+"(3.1415926535897932384626433832795)";
//		ex = "10%4";
//		ex = "cbrt(-8)";  //erro n anotar pfff cos, sin, tan, acos, asin, atan, exp, log, log10, log2
		
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
		
		Function cosg = new Function("cosg", 1) {
		    @Override
		    public double apply(double... args) {
		    	return Math.cos(args[0]*Math.PI/180);
		    }
		};

//		e = new ExpressionBuilder("pi").function(pi).build();
		
//		e = new ExpressionBuilder("cosg(60)").function(cosg).build();

//		e = new ExpressionBuilder("2.5!").operator(factorial).build();

//		ex = "2cos(0)";
		e = new ExpressionBuilder(ex).build();
		
//		e = new ExpressionBuilder("logb(8, 2)").function(logb).build();
		
		error = false;
		try{
			double r = e.evaluate();
		
			s = r+"";
			if (r % 1 == 0) s = s.substring(0, s.length()-2);

	    } catch(Exception ae) {
	    	s = ae.getMessage();
	    	error = true;
	    }
//	    } catch(IllegalArgumentException ae) {
//	    	s = ae.getMessage();;
//	    	error = true;
//	    }
//		IllegalArgumentException (expressão vazia)
		// parêntesis
		
		
		
		
		if (s.equals("NaN")) {
			error = true;
			if (func == "log") s = "Erro: Logarítmo de um número não positivo!";
			else if (func == "sqrt") s = "Erro: Raiz de número negativo!";
			else s = "Erro: NaN!";
		}
		
		if (s.equals("Infinity")) {
			error = true;
			s = "Infinito";
		}
		
		// mais catchs
		
		// tem de se carregar duas vezes no 1/x??
		
		return s;
				
	}
	
	//	request (expressions) - aceder aos outros usar o inject
	//	application (statistics)
	//	session (historic)

	public void key(ActionEvent event) {
		String add ="";
		
		int code = 0, codef = 0;
		boolean r = false, op = false;
		
		switch (event.getComponent().getId()) {
		
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
			codef = 0;
			op = true;
			break;
		case "minus": add = "-";
			codef = 1;
			op = true;
			break;
		case "times": add = "*";
			codef = 2;
			op = true;
			break;
		case "divide": add = "/"; 
			codef = 3;
			op = true;
			break;
		case "inverse": add = result(5,"1/");
			r = true; 
			code = 5; 
			hist.addToList(expression); 
			codef = 4;
			op = true;
			break;  //tirar result
		case "square": add = "^2"; 
			codef = 5;
			op = true;
			break;
		case "cubic": add = "^3"; 
			codef = 6;
			op = true;
			break;
		case "power": add = "^"; 
			codef = 7;
			op = true;
			break;
		case "sqrt": add = result(4,"sqrt"); 
			r = true; 
			code = 4; 
			hist.addToList(expression);
			codef = 8;
			op = true;
			break;  //tirar result ou ver exp	
		case "cbrt": add = result(4,"cbrt"); 
			r = true; 
			code = 4; 
			hist.addToList(expression);
			codef = 9;
			op = true;
			break;  //tirar result ou ver exp
		case "dMod": add = "%";
			codef = 10;
			op = true;
			break;
		case "sine": add = "sin";
			codef = 11;
			op = true;
			break;
		case "cosine": add = "cos";
			codef = 12;
			op = true;
			break;
		case "tangent": add = "tan";
			codef = 13;
			op = true;
			break;
		case "sineD": add = "sin"; //mudar para degree
			codef = 14;
			op = true;
			break;
		case "cosineD": add = "cos";
			codef = 15;
			op = true;
			break;
		case "tangentD": add = "tan";
			codef = 16;
			op = true;
			break;
		case "arcSine": add = "asin";
			codef = 17;
			op = true;
			break;
		case "arcCosine": add = "acos";
			codef = 18;
			op = true;
			break;
		case "arcTangent": add = "atan";
			codef = 19;
			op = true;
			break;
		case "arcsineD": add = "asin"; //mudar para degree
			codef = 20;
			op = true;
			break;
		case "arcCosineD": add = "acos";
			codef = 21;
			op = true;
			break;
		case "arcTangentD": add = "atan";
			codef = 22;
			op = true;
			break;
		case "natLog": add = "log";
			codef = 23;
			op = true;
			break;
		case "Log10": add = "log10";
			codef = 24;
			op = true;
			break;
		case "LogX": add = "log"; //mudar para função definida
			codef = 25;
			op = true;
			break;
		case "exp": add = "exp";
			codef = 26;
			op = true;
			break;
		case "power10": add = "10^";
			codef = 27;
			op = true;
			break;
		case "pi": add = "pi"; //função definida
			codef = 28;
			op = true;
			break;
		case "factorial": add = "!"; //função definida
			codef = 29;
			op = true;
			break;
		case "mdc": add = "mdc"; //função definida
			codef = 30;
			op = true;
			break;
		case "mmc": add = "mmc"; //função definida
			codef = 31;
			op = true;
			break;

		// other
		case "lPar": add = "("; break;
		case "rPar": add = "("; break;
		case "deleteL": code = 2; break;
		case "reset": add = "0"; code = 1; break;
		case "result": add = result(0,"");
			r = true;
			code = 1;
			hist.addToList(expression);
			break;
		}

		if (op && !error) stat.updateElement(codef);

		// meter expressão em cima? tirar +/-
		if (expression.equals("0") && code != 3) clean = true;

		addToExpression(code,add);
		
		if (r || expression.equals("0")) clean = true;
		else clean = false;
			
	}

	//limpar 0 e dp meter operação...

}
