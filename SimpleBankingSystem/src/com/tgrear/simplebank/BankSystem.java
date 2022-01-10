package com.tgrear.simplebank;
import java.util.Random;

class BankSystem {
	
	private static void displayMainMenu() {
		System.out.println("1. Create an account");
		System.out.println("2. Log into account");
		System.out.println("0. Exit");
	}
	
	private static String generateRandomNumberAsString() {
		Random random = new Random();
		String randomNumberAsString = String.valueOf(random.nextInt());
		return randomNumberAsString;
	}
	
	public static void main(String[] args) {
//		displayMainMenu();
		System.out.println(generateRandomNumberAsString());

	}

}
