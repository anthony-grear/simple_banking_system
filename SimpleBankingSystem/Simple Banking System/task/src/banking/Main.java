package banking;

import java.util.Random;
import java.util.Scanner;
import java.sql.*;
import org.sqlite.*;

public class Main {

	static Scanner scanner = new Scanner(System.in);
	static boolean isLoggedIn = false;
	private static boolean exit = false;
	private static String dbName=null;
	private static String flag=null;
	private static String loggedInAccountNumber="0";
    
    public enum BankSystemState {
		START_SYSTEM {
			@Override
			public BankSystemState nextState() {

				if ((dbName != null && !dbName.isEmpty())) {
					createTable(dbName);
					return MAIN_MENU;
				} else {
					return EXIT_SYSTEM;
				}
			}
		},
		MAIN_MENU {
			@Override
			public BankSystemState nextState() throws SQLException {
				displayMainMenu();
				String input = scanner.next();
				switch (input) {
					case "1":
						String[] acctNumAndPin = registerAccountNumberAndPin();
						displayAccountCreation(acctNumAndPin[0], acctNumAndPin[1]);
						return MAIN_MENU;
					case "2":
						System.out.println("\nEnter your card number:");
						String cardNumber = scanner.next();		
						System.out.println("Enter your PIN:");
						String pinNumber = scanner.next();
						if (findCard(cardNumber, pinNumber)) {
								isLoggedIn = true;
								System.out.println("\nYou have successfully logged in!");
								loggedInAccountNumber = cardNumber;
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
			public BankSystemState nextState() throws SQLException {
				successfulLoginMenu();
				String input = scanner.next();
				switch (input) {
					case "1":
						if (isLoggedIn) {
							System.out.println("\nBalance: " + getAccountBalance() + "\n");
							return ACCOUNT_HOME;
						} else {
							return MAIN_MENU;
						}
					case "2":
						updateAccountBalance();
						return ACCOUNT_HOME;
					case "4":
						closeAccount();
						return MAIN_MENU;
					case "5":
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
		
		public abstract BankSystemState nextState() throws SQLException;
	}
    
    private static String generateCheckDigit(String accountNumber) {
		String checkDigitString;
		int checkDigitValue=0;
		int tempNum;
		int total = 0;
		for (int i = 0; i < 15; i++) {
			tempNum = Integer.valueOf(Character.toString(accountNumber.charAt(i)));
			if (i % 2 == 0) {
				tempNum = tempNum * 2;
				if (tempNum > 9) {
					tempNum = tempNum - 9;
				}
				total += tempNum;
			} else {
				total += tempNum;
			}
		}
		while ((total + checkDigitValue) % 10 != 0) {
			checkDigitValue++;
		}
		checkDigitString = String.valueOf(checkDigitValue);
		return checkDigitString;
	}

    private static String generateAccountNumber() {
		Random random = new Random();
		String[] randomIntArr = new String[9];
		String accountNumber = "400000";
		for (int i = 0; i < 9; i++) {
			randomIntArr[i] = String.valueOf(random.nextInt(9));
		}
		accountNumber = "400000";
		for (int i = 0; i < 9; i++) {
			accountNumber +=randomIntArr[i];
		}
		accountNumber += generateCheckDigit(accountNumber);
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
	
	private static String[] registerAccountNumberAndPin() throws SQLException {
		String acctNum = generateAccountNumber();
		String pinNum = generatePinNumber();
		while (findCard(acctNum)) {
			acctNum = generateAccountNumber();
		}
		insertCard(acctNum, pinNum);
		String[] acctNumAndPin = new String[2];
		acctNumAndPin[0] = acctNum;
		acctNumAndPin[1] = pinNum;
		return acctNumAndPin;
	}

	private static int getAccountBalance() throws SQLException {
		String url = "jdbc:sqlite:" + dbName;
		SQLiteDataSource dataSource = new SQLiteDataSource();
		dataSource.setUrl(url);
		String selectBalance = "SELECT balance FROM card WHERE number = ?";
		ResultSet rs;
		int currentBalance=0;
		try (Connection con = dataSource.getConnection()) {
			try (PreparedStatement statement = con.prepareStatement(selectBalance)) {
				statement.setString(1, loggedInAccountNumber);
				rs = statement.executeQuery();
				currentBalance = rs.getInt(1);

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return currentBalance;
	}

	private static void updateAccountBalance() throws SQLException {
		int deposit = 0;
		String url = "jdbc:sqlite:" + dbName;
		SQLiteDataSource dataSource = new SQLiteDataSource();
		dataSource.setUrl(url);
		String selectBalance = "UPDATE card SET balance = ? WHERE number = ?";
		System.out.println("\nEnter income:");
		deposit = scanner.nextInt();
		int currentBalance=0;
		try (Connection con = dataSource.getConnection()) {
			try (PreparedStatement statement = con.prepareStatement(selectBalance)) {
				statement.setInt(1, deposit);
				statement.setString(2, loggedInAccountNumber);
				statement.executeUpdate();
				System.out.println("Income was added!");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private static void closeAccount() throws SQLException {
		String url = "jdbc:sqlite:" + dbName;
		SQLiteDataSource dataSource = new SQLiteDataSource();
		dataSource.setUrl(url);
		String selectAccount = "DELETE FROM card WHERE number = ?";
		try (Connection con = dataSource.getConnection()) {
			try (PreparedStatement statement = con.prepareStatement(selectAccount)) {
				statement.setString(1, loggedInAccountNumber);
				statement.executeUpdate();
				System.out.println("\nThe account has been closed!\n");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
    
    private static void displayAccountCreation(String accountNumber, String pin) {
		System.out.println("\nYour card has been created");
		System.out.println("Your card number:");		
		System.out.println(accountNumber);
		System.out.println("Your card PIN:");
		System.out.println(pin + "\n");

	}

	private static void displayMainMenu() {
		System.out.println("1. Create an account");
		System.out.println("2. Log into account");
		System.out.println("0. Exit");
	}

    private static void successfulLoginMenu() {
		
		System.out.println("\n1. Balance");
		System.out.println("2. Add income");
		System.out.println("3. Do transfer");
		System.out.println("4. Close account");
		System.out.println("5. Log out");
		System.out.println("0. Exit");
	}
    
    private static void getConnection() {
		String url = "jdbc:sqlite:" + dbName;
		SQLiteDataSource dataSource = new SQLiteDataSource();
		dataSource.setUrl(url);
		try (Connection con = dataSource.getConnection()) {
			if (con.isValid(5)) {
				System.out.println("Connection is valid.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void createTable(String dbName) {
		String url = "jdbc:sqlite:" + dbName;
		SQLiteDataSource dataSource = new SQLiteDataSource();
		dataSource.setUrl(url);
		try (Connection con = dataSource.getConnection()) {
			try (Statement statement = con.createStatement()) {
				statement.executeUpdate("CREATE TABLE IF NOT EXISTS card(" +
						"id INTEGER PRIMARY KEY AUTOINCREMENT," +
						"number TEXT NOT NULL," +
						"pin TEXT NOT NULL," +
						"balance INT DEFAULT 0)");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void insertCard(String cardNumber, String pinNumber) {
		String url = "jdbc:sqlite:" + dbName;
		SQLiteDataSource dataSource = new SQLiteDataSource();
		dataSource.setUrl(url);
		try (Connection con = dataSource.getConnection()) {
			try (PreparedStatement statement = con.prepareStatement("INSERT INTO card (number, pin, balance) " +
					"VALUES (?,?,0)")) {
				statement.setString(1, cardNumber);
				statement.setString(2, pinNumber);
				statement.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static boolean findCard(String cardNumber) throws SQLException {
		String url = "jdbc:sqlite:" + dbName;
		SQLiteDataSource dataSource = new SQLiteDataSource();
		dataSource.setUrl(url);
		ResultSet rs = null;
		boolean cardFound = true;
		try (Connection con = dataSource.getConnection()) {
			try (PreparedStatement statement = con.prepareStatement("SELECT number, balance FROM card WHERE number = ?" );) {
				statement.setString(1, cardNumber);
				rs = statement.executeQuery();
				if (rs.next()) {
					cardFound = true;

				} else {

					cardFound = false;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cardFound;
	}

	private static boolean findCard(String cardNumber, String pinNumber) throws SQLException {
		String url = "jdbc:sqlite:" + dbName;
		SQLiteDataSource dataSource = new SQLiteDataSource();
		dataSource.setUrl(url);
		ResultSet rs = null;
		boolean cardAndPinFound = true;
		try (Connection con = dataSource.getConnection()) {
			try (PreparedStatement statement = con.prepareStatement("SELECT number, balance FROM card WHERE number = ? AND pin = ?" );) {
				statement.setString(1, cardNumber);
				statement.setString(2, pinNumber);
				rs = statement.executeQuery();
				if (!rs.next()) {
					cardAndPinFound = false;

				} else {

					cardAndPinFound = true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cardAndPinFound;
	}


    
    public static void main(String[] args) throws SQLException {
        if (args.length == 2) {
			flag = args[0];
			dbName = args[1];

		}
		BankSystemState state = BankSystemState.START_SYSTEM;
		while (!exit) {
			state = state.nextState();
		}

    }
}

