package demo;

import java.util.Random;

public class IDCheckDigit {

	public static void main(String[] args) {
		//Run test 100 times
		for(int i = 0; i < 100; i++){
			String ucn = generateID();
			int chkDigit = getCheckDigit(ucn);
			String ucnWithCheckDigit = ucn + chkDigit;
			System.out.println(ucnWithCheckDigit + ", verify: " + isValid(ucnWithCheckDigit));
		}
	}
	
	//Generate a random 9 alphanumberics ID value
	private static String generateID(){
		String charPool = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		int charPoolSize = charPool.length();
		Random r = new Random();
		StringBuilder sb = new StringBuilder();
		for(int i = 1 ; i < 9; i++){
			sb.append(charPool.charAt(r.nextInt(charPoolSize)));
		}
		return sb.toString();
	}
	
	//Converting a Base36 value to Base10
	private static long convertToBaseTen(String ucn) {
		return Long.parseLong(ucn, 36);
	}
	
	//Calculate the check digit using ucn to base10 then with LUHN algorithm to find
	//check digit value that add up to mod 10.
	private static int getCheckDigit(String ucn){
		long ucnBaseTen = convertToBaseTen(ucn);
		int sum = luhnSum(ucnBaseTen);
		int chkDigit = ((sum / 10 + 1) * 10 - sum) % 10;
		return chkDigit;
	}
	
	//Validate a given ID with check digit is indeed valid.
	private static boolean isValid(String ucnWithCheckDigit){
		int size = ucnWithCheckDigit.length();
		String ucn = ucnWithCheckDigit.substring(0, size - 1);
		int chkDigit = Integer.parseInt(ucnWithCheckDigit.substring(size - 1));
		
		long baseTen = convertToBaseTen(ucn);
		return (luhnSum(baseTen) + chkDigit) % 10 == 0;
	}
	
	//Typical algorithm of LUHN sum.
	private static int luhnSum(long num) {
		String number = "" + num;
		int sum = 0;

		boolean alternate = false;
		for (int i = number.length() - 1; i >= 0; i--) {
			int n = Integer.parseInt(number.substring(i, i + 1));
			if (alternate) {
				n *= 2;
				if (n > 9) {
					n = (n % 10) + 1;
				}
			}
			sum += n;
			alternate = !alternate;
		}

		return sum;
	}
}
