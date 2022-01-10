package com.tgrear.simplebank;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

class BankSystem {
	static Map<String, String> accountMap = new HashMap<String, String>();
	
	private static void displayMainMenu() {
		System.out.println("1. Create an account");
		System.out.println("2. Log into account");
		System.out.println("0. Exit");
	}
	
	private static String generateAccountNumber() {
		Random random = new Random();
		String[] randomIntArr = new String[10];
		String accountNumber = "400000";
		do {			
			for (int i = 0; i < 10; i++) {
				randomIntArr[i] = String.valueOf(random.nextInt(10));			
			}			
			for (int i = 0; i < 10; i++) {
				accountNumber +=randomIntArr[i];
			}			
		} while (accountMap.containsKey(accountNumber));
		accountMap.put(accountNumber, generatePinNumber());		
		return accountNumber;
	}
	
	private static String generatePinNumber() {
		Random random = new Random();
		String[] randomIntArr = new String[4];
		for (int i = 0; i < 4; i++) {
			randomIntArr[i] = String.valueOf(random.nextInt(10));			
		}
		String pinNumber = "";
		for (int i = 0; i < 4; i++) {
			pinNumber +=randomIntArr[i];
		}
		
		return pinNumber;
	}
	
	private static void displayAccountCreation() {
		String accountNumberOutput;
		System.out.println("Your card has been created");
		System.out.println("Your card number:");
		accountNumberOutput = generateAccountNumber();
		System.out.println(accountNumberOutput);
		System.out.println("Your card PIN:");
		System.out.println(accountMap.get(accountNumberOutput));
	}
	
	private static void successfulLoginMenu() {
		System.out.println("You have successfully logged in!\n");
		System.out.println("1. Balance");
		System.out.println("2. Log out");
		System.out.println("0. Exit");
	}
	
	public static void main(String[] args) {
//		displayMainMenu();
//		System.out.println(generateAccountNumber());
//		System.out.println(generatePinNumber());
		displayAccountCreation();
//		successfulLoginMenu();

	}

}
