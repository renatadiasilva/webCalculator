package manageCalculator;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.faces.event.ActionEvent;

import net.objecthunter.exp4j.*;

import java.io.Serializable;

@Named
@SessionScoped
public class Calculator implements Serializable {
	private static final long serialVersionUID = 1L;
	private String expression;
	private boolean clean;
	
	@Inject
	private Historic hist;
	
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
		Expression e = new ExpressionBuilder(ex).build();

		if (code != 0) ex = func+"("+expression+")";
			
		e = new ExpressionBuilder(ex).build();
		
		try{
			double r = e.evaluate();
		
			s = r+"";
			if (r % 1 == 0) s = s.substring(0, s.length()-2);

	    } catch(ArithmeticException ae){
	    	s = "Erro: Divisão por zero!";
	    }
		
		if (s.equals("NaN")) s = "Erro: Raiz de número negativo!";  //carregar a seguir ao erro (sqrt neg. e dp 1/x)
		
		// mais catchs
		
		// tem de se carregar duas vezes no 1/x??
		
		return s;
				
	}
	
	//	request (expressions) - aceder aos outros usar o inject
	//	application (statistics)
	//	session (historic)

	public void key(ActionEvent event) {
		String add ="";
		
		int code = 0;
		boolean r = false;
		
		switch (event.getComponent().getId()) {
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
		case "plus": add = "+"; break;
		case "minus": add = "-"; break;
		case "times": add = "*"; break;
		case "divide": add = "/"; break;
//		case "percent": add = "%"; break; x ao quadrado
		case "square": add = "^2"; break;
		case "sqrt": add = result(4,"sqrt"); r = true; code = 4; hist.addToList(expression); break;  //tirar result ou ver exp
		case "inverse": add = result(5,"1/"); r = true; code = 5; hist.addToList(expression); break;  //tirar result
		case "deleteL": code = 2; break;
		case "reset": add = "0"; code = 1; break;
		case "result": add = result(0,""); r = true; code = 1; hist.addToList(expression); break;
		}
		
		// meter expressão em cima? tirar +/-
		if (expression.equals("0") && code != 3) clean = true;

		addToExpression(code,add);
		
		if (r || expression.equals("0")) clean = true;
		else clean = false;
			
	}
	
	//limpar 0 e dp meter operação...

}
