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
		String accountNumber = "400000";
		for (int i = 0; i < 10; i++) {
			accountNumber +=randomIntArr[i];
		}
		
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
		System.out.println("Your card has been created");
		System.out.println("Your card number:");
		System.out.println(generateAccountNumber());
		System.out.println("Your card PIN:");
		System.out.println(generatePinNumber());
	}
	
	public static void main(String[] args) {
//		displayMainMenu();
//		System.out.println(generateAccountNumber());
//		System.out.println(generatePinNumber());
		displayAccountCreation();

	}

}
