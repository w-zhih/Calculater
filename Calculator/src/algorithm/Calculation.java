package algorithm;

import java.util.EmptyStackException;
import java.util.Stack;

import tool.CalculateException;

/**
 * @author Wang ZH
 *
 */
public class Calculation {
	
	public static double calculate(String s,boolean calHistory, double answer) throws CalculateException{
		
		Stack<Double> numStack=new Stack<Double>();
		Stack<String> operStack=new Stack<String>();
		int index=0;//扫描到的字符位数
		int nums=1;//式子中应该出现的数的个数
		double num1=0;
		double num2=0;
		String oper=null;
		double result=0;
		String ch="";

		s=preprocess(s);
//		System.out.println(s);
		while(index<s.length()) {
			
			ch=getOperOrNumber(s, index);
//			System.out.println(ch);
			index+=ch.length();
			nums=setNums(ch,nums);
			if(isOper(ch)) {//当前为运算符
				//判断当前操作栈是否为空
				if(operStack.empty()) {
					operStack.push(ch);
				}else {
					if(getPriority(ch)==4) {
						operStack.push(ch);
					}else if(ch.equals("(")) {
						operStack.push(ch);
					}else if(ch.equals(")")) {
						while(!(operStack.peek().equals("(")) && !operStack.empty()) {
							oper=operStack.pop();
							try {
								if(getPriority(oper)==5 || oper.equals("!")) {
									num1=numStack.pop();
									numStack.push(cal1(oper,num1));
								}else {
									num2=numStack.pop();
									num1=numStack.pop();
									numStack.push(cal2(oper,num1,num2));
								}
							}catch(EmptyStackException e) {
								throw new CalculateException("numStack中操作数个数缺少");
							}
						}
						if(operStack.empty()) throw new CalculateException("输入式子缺少(");
						operStack.pop();
					}else {
						while(!operStack.empty()&&getPriority(ch)<=getPriority(operStack.peek())){
	                        //如果新读取的操作符的优先级小于等于当前栈顶操作符的优先级那么我们需要从操作数
	                        //栈中弹出操作数进行运算
							oper=operStack.pop();
							try {
								if(getPriority(oper)==5 || oper.equals("!")) {
									num1=numStack.pop();
									numStack.push(cal1(oper,num1));
								}else {
									num2=numStack.pop();
									num1=numStack.pop();
									numStack.push(cal2(oper,num1,num2));
								}
							}catch(EmptyStackException e) {
								throw new CalculateException("numStack中操作数缺少");
							}
	                    }
						
	                        operStack.push(ch);
	                    
					}
				}
			}else {//ch为数
				if(ch.equals("Ans")) {
					if(!calHistory) {
						throw new CalculateException("No Answer");
					}else {
						numStack.push(answer);
					}
				}else if(ch.equals("e")) {
					numStack.push(Math.E);
				}else if(ch.equals("Π")) {
					numStack.push(Math.PI);
				}else {
					numStack.push(Double.parseDouble(ch));
				}
			}
//			System.out.println("numStack:"+numStack.toString());
//			System.out.println("operStack:"+operStack.toString());
		}
		
		if(nums!=0) throw new CalculateException("number数与操作符数不匹配");
		
		while(!operStack.empty()) {
			oper=operStack.pop();
			if(getPriority(oper)==5 || oper.equals("!")) {
				num1=numStack.pop();
				numStack.push(cal1(oper,num1));
			}else {
				num2=numStack.pop();
				num1=numStack.pop();
				numStack.push(cal2(oper,num1,num2));
			}
//			System.out.println("numStack:"+numStack.toString());
//			System.out.println("operStack:"+operStack.toString());
		}
		result=numStack.pop();
		return result;
	}

	/**
	 * @Description 对负数进行补0处理以及补*
	 * @author Wang ZH
	 */
	private static String preprocess(String s) throws CalculateException{
		StringBuilder str=new StringBuilder(s);
		int i;
		for(i=0;i<str.length();i++) {
			if(i>0 &&(str.charAt(i)=='Π'||str.charAt(i)=='e'||str.indexOf("Ans", i)==i||str.indexOf("log", i)==i||str.indexOf("ln", i)==i||str.indexOf("sin", i)==i||str.indexOf("cos", i)==i||str.indexOf("tan", i)==i||str.indexOf("arcsin", i)==i||str.indexOf("arccos", i)==i||str.indexOf("arctan", i)==i)&&((str.charAt(i-1)>='0'&&str.charAt(i-1)<='9')||str.charAt(i-1)=='.'||str.charAt(i-1)==')')) {
					str.insert(i, '*');
				}
			
			if(str.charAt(i)=='+' || str.charAt(i)=='-') {
				if(i==0) {
					str.insert(i, 0);
				}else if(!((str.charAt(i-1)>='0'&&str.charAt(i-1)<='9')||str.charAt(i-1)=='.'||str.charAt(i-1)=='e'||str.charAt(i-1)=='Π'||(i>2&&str.lastIndexOf("Ans", i)==i-3))&&str.charAt(i-1)!=')') {
					if(str.charAt(i)=='+'&&str.charAt(i-1)=='-') {
						str.deleteCharAt(i--);
						continue;
					}else if(str.charAt(i)=='-'&&str.charAt(i-1)=='-') {
						str.deleteCharAt(i--);
						str.deleteCharAt(i);
						str.insert(i--, '+');
						continue;
					}
					str.insert(i, 0);
				}
				
			}
			
		}
		
		return str.toString();
	}

	private static int setNums(String ch, int nums) {
		if(ch.equals("+")||ch.equals("-")||ch.equals("*")||ch.equals("/")||ch.equals("^")) {
			nums++;
		}else if(isNum(ch)||ch.equals("e")||ch.equals("Π")||ch.equals("Ans")) {
			nums--;
		}
		return nums;
		
	}

	/**
	 * @Description 获取index处的操作符或数
	 * @author Wang ZH
	 */
	private static String getOperOrNumber(String s,int index) throws CalculateException{
		
		String ch="";
		ch=ch+s.charAt(index++);
		try {
			if(isNum(ch)) {
				while(index<s.length() && ((s.charAt(index)>='0' && s.charAt(index)<='9')|| s.charAt(index)=='.')) {
					ch=ch+s.charAt(index++);
				}
				if(isNum(ch))
					return ch;
				else
					throw new CalculateException("number不合法");
			}else if(ch.equals("Π") || ch.equals("e") || ch.equals("+") || ch.equals("-") || ch.equals("*") || ch.equals("/") || ch.equals("^")|| ch.equals("!")|| ch.equals("(") || ch.equals(")")){
				return ch;
			}else if(ch.equals("l")) {
					ch=ch+s.charAt(index++);
				if(ch.equals("ln")) {
					return ch;
				}else if(ch.equals("lo")) {
					ch=ch+s.charAt(index++);
					if(ch.equals("log")) return ch;
					else throw new CalculateException("无对应操作符");
				}else {
					throw new CalculateException("无对应操作符");
				}
			}else if(ch.equals("s")) {
				ch=ch+s.substring(index, index+2);
				if(ch.equals("sin")) return ch;
				else throw new CalculateException("无对应操作符");
			}else if(ch.equals("c")) {
				ch=ch+s.substring(index, index+2);
				if(ch.equals("cos")) return ch;
				else throw new CalculateException("无对应操作符");
			}else if(ch.equals("t")) {
				ch=ch+s.substring(index, index+2);
				if(ch.equals("tan")) return ch;
				else throw new CalculateException("无对应操作符");
			}else if(ch.equals("a")) {
				ch=ch+s.substring(index, index+5);
				if(ch.equals("arcsin") || ch.equals("arccos") || ch.equals("arctan")) 
					return ch;
				else throw new CalculateException("无对应操作符");
			}else if(ch.equals("A")) {
				ch=ch+s.substring(index, index+2);
				if(ch.equals("Ans")) return ch;
				else throw new CalculateException("无对应操作符");
			}else {
				throw new CalculateException("无对应操作符");
			}
		}catch(StringIndexOutOfBoundsException e) {
			throw new CalculateException("数组越界");
		}
	}

	private static boolean isNum(String str) throws CalculateException{
		char[] chars=str.toCharArray();
		for(char c:chars) {
			if(!((c>='0'&&c<='9')||c=='.')) {
				return false;
			}
		}
		if(countPointNumber(str)>1) throw new CalculateException("小数点个数大于1");
		return true;
	}
	
	private static boolean isOper(String str) throws CalculateException{
		
		return str.equals("+")||str.equals("-")||str.equals("*")||str.equals("/")||str.equals("log")||str.equals("ln")||str.equals("sin")||str.equals("cos")||str.equals("tan")||str.equals("!")||str.equals("^")||str.equals("arcsin")||str.equals("arccos")||str.equals("arctan")||str.equals("(")||str.equals(")");
		
	}
	
	
	/**
	 * @Description 输出字符串中的小数点个数
	 * @author Wang ZH
	 */
	private static int countPointNumber(String str) {
		int n=0;
		char[] chars=str.toCharArray();
		for(char c:chars) {
			if(c=='.') {
				n++;
			}
		}
		return n;
	}

	private static int getPriority(String str) throws CalculateException{
		if(!isOper(str)) {//判断str是否为操作符
			throw new CalculateException("非操作符无法获取优先级");
		}else {
			if(str.equals("log")||str.equals("ln")||str.equals("sin")||str.equals("cos")||str.equals("tan")||str.equals("arcsin")||str.equals("arccos")||str.equals("arctan")) {
				return 5;
			}else if(str.equals("!")){
				return 4;
			}else if(str.equals("^")){
				return 3;
			}else if(str.equals("*")||str.equals("/")) {
				return 2;
			}else if(str.equals("+")||str.equals("-")) {
				return 1;
			}else if(str.equals("(")||str.equals(")")) {
				return 0;
			}
			throw new CalculateException("该操作符不具有优先级");
			
		}
	}
	
	private static double cal1(String oper,double num) throws CalculateException{
		double result=11111111;
		
		if(oper.equals("log")) {
			result=Math.log10(num);
		}else if(oper.equals("ln")) {
			result=Math.log(num);
		}else if(oper.equals("sin")) {
			result=Math.sin(num);
		}else if(oper.equals("cos")) {
			result=Math.cos(num);
		}else if(oper.equals("tan")) {
			result=Math.tan(num);
		}else if(oper.equals("arcsin")) {
			result=Math.asin(num);
		}else if(oper.equals("arccos")) {
			result=Math.acos(num);
		}else if(oper.equals("arctan")) {
			result=Math.atan(num);
		}else if(oper.equals("!")) {
			if(num<0 || Math.round(num)-num!=0) {
				throw new CalculateException("阶乘数不合法");
			}else {
				result=getFactorial(num);
			}
		}
		
		return result;
	}
	
	/**
	 * @Description 计算阶乘
	 * @author Wang ZH
	 */
	private static double getFactorial(double num) {
		double result=1;
		for(double i=1;i<=num;i++) {
			result*=i;
		}
		return result;
	}

	private static double cal2(String oper,double num1,double num2) throws CalculateException{
		double result=22222222;
		
		if(oper.equals("+")) {
			result=num1+num2;
		}else if(oper.equals("-")) {
			result=num1-num2;
		}else if(oper.equals("*")) {
			result=num1*num2;
		}else if(oper.equals("/")) {
			if(num2==0) {
				throw new CalculateException("除数不能为0");
			}else {
				result=num1/num2;
			}
		}else if(oper.equals("^")) {
			result=Math.pow(num1, num2);
		}
		
		return result;
	}
}

