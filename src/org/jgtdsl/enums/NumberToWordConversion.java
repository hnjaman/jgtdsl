package org.jgtdsl.enums;

import java.text.DecimalFormat;
public class NumberToWordConversion {

	private static final String[] tensNames={
		"",
		" Ten",
		" Twenty",
		" Thirty",
		" Forty",
		" Fifty",
		" Sixty",
		" Seventy",
		" Eighty",
		" Ninety"
	};
	public static final String[] numNames={
		"",
		" One",
		" Two",
		" Three",
		" Four",
		" Five",
		" Six",
		" Seven",
		" Eight",
		" Ten",
		" Eleven",
		" Twelve",
		" Thirteen",
		" Fourteen",
		" Fifteen",
		" Sixteen",
		" Seventeen",
		" Eighteen",
		" Nineteen"
	};
	
	private NumberToWordConversion(){}
	
	private static String convertLessthenOneThousand(int number){
		String soFar;
		
		if (number % 100 < 20){
		      soFar = numNames[number % 100];
		      number /= 100;
		}else {
		      soFar = numNames[number % 10];
		      number /= 10;

		      soFar = tensNames[number % 10] + soFar;
		      number /= 10;
		    }if (number == 0) return soFar;
		return numNames[number] + " hundred" + soFar;
	}
	public static String convert(long number) {
	    // 0 to 999 999 999 999
	    if (number == 0) { return "zero"; }

	    String snumber = Long.toString(number);

	    // pad with "0"
	    String mask = "000000000000";
	    DecimalFormat df = new DecimalFormat(mask);
	    snumber = df.format(number);

	    // XXXnnnnnnnnn
	    int core = Integer.parseInt(snumber.substring(0,3));
	    // nnnXXXnnnnnn
	    int lac  = Integer.parseInt(snumber.substring(3,6));
	    // nnnnnnXXXnnn
	    int hundredThousands = Integer.parseInt(snumber.substring(6,9));
	    // nnnnnnnnnXXX
	    int thousands = Integer.parseInt(snumber.substring(9,12));

	    String tradBillions;
	    switch (core) {
	    case 0:
	      tradBillions = "";
	      break;
	    case 1 :
	      tradBillions = convertLessthenOneThousand(core)
	      + " core ";
	      break;
	    default :
	      tradBillions = convertLessthenOneThousand(core)
	      + " core ";
	    }
	    String result =  tradBillions;

	    String tradMillions;
	    switch (lac) {
	    case 0:
	      tradMillions = "";
	      break;
	    case 1 :
	      tradMillions = convertLessthenOneThousand(lac)
	         + " lac ";
	      break;
	    default :
	      tradMillions = convertLessthenOneThousand(lac)
	         + " lac ";
	    }
	    result =  result + tradMillions;

	    String tradHundredThousands;
	    switch (hundredThousands) {
	    case 0:
	      tradHundredThousands = "";
	      break;
	    case 1 :
	      tradHundredThousands = "one thousand ";
	      break;
	    default :
	      tradHundredThousands = convertLessthenOneThousand(hundredThousands)
	         + " thousand ";
	    }
	    result =  result + tradHundredThousands;

	    String tradThousand;
	    tradThousand = convertLessthenOneThousand(thousands);
	    result =  result + tradThousand;

	    // remove extra spaces!
	    return result.replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ");
	  }
}
