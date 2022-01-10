package com.tgrear.simplebank;
import java.util.Random;

class BankSystem {
	
	private static void displayMainMenu() {
		System.out.println("1. Create an account");
		System.out.println("2. Log into account");
		System.out.println("0. Exit");
	}
	
	private static String generateAccountNumber() {
		Random random = new Random();
		String[] randomIntArr = new String[10];
		for (int i = 0; i < 10; i++) {
			randomIntArr[i] = String.valueOf(random.nextInt(10));			
		}
		String randomNumberAsString = "400000";
		for (int i = 0; i < 10; i++) {
			randomNumberAsString +=randomIntArr[i];
		}
		
		return randomNumberAsString;
	}
	
	public static void main(String[] args) {
//		displayMainMenu();
		System.out.println(generateAccountNumber());

	}

}
