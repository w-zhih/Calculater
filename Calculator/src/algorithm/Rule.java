package algorithm;

public class Rule {
	//保留小数位数
	private static int reservedDecimal=10;

	public static void setReservedDecimal(int reservedDecimal) {
		Rule.reservedDecimal = reservedDecimal;
	}

	public static int getReservedDecimal() {
		return reservedDecimal;
	}
	
	
}
