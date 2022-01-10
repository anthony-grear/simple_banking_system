package com.tgrear.simplebank;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

class BankSystem {
	static Map<String, String> accountMap = new HashMap<String, String>();
	static Scanner scanner = new Scanner(System.in);
	static boolean isLoggedIn = false;
	private static boolean exit = false;
	
	public enum BankSystemState {
		START_SYSTEM {
			@Override
			public BankSystemState nextState() {
				return MAIN_MENU;
			}
		},
		MAIN_MENU {
			@Override
			public BankSystemState nextState() {
				displayMainMenu();
				String input = scanner.next();
				switch (input) {
					case "1":
						String accountNumber = generateAccountNumber();
						displayAccountCreation(accountNumber);
						return MAIN_MENU;
					case "2":
						System.out.println("\nEnter your card number:");
						String cardNumber = scanner.next();		
						System.out.println("Enter your PIN:");
						String pinNumber = scanner.next();
						if (accountMap.containsKey(cardNumber) && 
							accountMap.get(cardNumber).equals(pinNumber)) {
								isLoggedIn = true;
								System.out.println("\nYou have successfully logged in!");
								return ACCOUNT_HOME;
						} else {
							System.out.println("\nWrong card number or PIN!\n");
							return MAIN_MENU;
						}						
					case "0":
						return EXIT_SYSTEM;
				}
				return START_SYSTEM;
			}
		},		
		ACCOUNT_HOME {
			@Override
			public BankSystemState nextState() {
				successfulLoginMenu();
				String input = scanner.next();
				switch (input) {
					case "1":
						if (isLoggedIn == true) {
							System.out.println("\nBalance: 0\n");
							return ACCOUNT_HOME;
						} else {
							return MAIN_MENU;
						}						
					case "2":
						isLoggedIn = false;
						System.out.println("\nYou have successfully logged out!");
						return MAIN_MENU;
					case "0":
						return EXIT_SYSTEM;
				}
				return START_SYSTEM;
			}
		},
		EXIT_SYSTEM {
			@Override
			public BankSystemState nextState() {
				System.out.println("\nBye!");
				exit = true;
				return this;
			}
		};
		
		public abstract BankSystemState nextState();
	}
	
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
			accountNumber = "400000";
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
	
	private static void displayAccountCreation(String accountNumber) {		
		System.out.println("\nYour card has been created");
		System.out.println("Your card number:");		
		System.out.println(accountNumber);
		System.out.println("Your card PIN:");
		System.out.println(accountMap.get(accountNumber)+"\n");
	}
	
	private static void successfulLoginMenu() {
		
		System.out.println("\n1. Balance");
		System.out.println("2. Log out");
		System.out.println("0. Exit");
	}
	
	private static void printAllAccounts() {
		System.out.println(accountMap.toString());
	}
	
	public static void main(String[] args) {
		BankSystemState state = BankSystemState.START_SYSTEM;
		while (!exit) {
			state = state.nextState();
		}
		

	}

}
