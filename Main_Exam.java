package Main_Exam;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 */
public class Main_Exam {
public class Main_Exam_fix {

	static final Scanner scanner = new Scanner(System.in);
	private static final int START_NUMBER = 1000;
	private static final int ROW_LENGTH = 6;

	private static final int USER_ID = 0;
	private static final int EDUCATION_CREDITS = 1;
	private static final int USER_NAME = 2;
	private static final int START_TIME = 3;
	private static final int END_TIME = 4;
	private static final int START_TIME_LOG = 2;
	private static final int END_TIME_LOG = 3;
	private static final int SALARY_LOG = 4;
	private static final int DATE = 5;

	/**
	 * The method that handles all program-logic
	 */
	public static void main(String[] args) {
		System.out.println("This is the main exam for D0017D HT23 held on 2023-12-21");
		System.out.println("----------------------------------\n" +
				"# LTU Lab Assistant Manager\n" +
				"----------------------------------\n");
		String[][] users = new String[10][ROW_LENGTH];
		//Have npo time to implement this for 4 and 5.
		String[][] logUsersSalary = new String[10][ROW_LENGTH];

		while (true) {
			menu();
			int option = input();
			int userId;
			System.out.println("option " + option);
			switch (option) {
				case 1:
					users = addLabAssistant(users);
					break;
				case 2:
					do {
						System.out.println("Enter assistant's ID number:");
						String idNumber = scanner.next();
						userId = checkUserId(idNumber);
					} while (userId <= 0);
					removeUser(users, userId);
					break;
				case 3:
					do {
						System.out.println("Enter lab assistant's ID number:");
						String idNumber = scanner.next();
						userId = checkUserId(idNumber);
					} while (userId <= 0);
					String startTime = setTime("Enter start time of the session:");
					String endTime = setTime("Enter end time of the session:");
					String date = setDate("Enter date formatted dd-mm-yyyy");
					boolean needSpace = checkIfNeedMoreSpace(logUsersSalary);
					if (needSpace) {
						logUsersSalary = extendArray(logUsersSalary, 1);
					}
					setTimeDataToUser(users, logUsersSalary, userId, startTime, endTime, date);
					break;
				case 4:
					do {
						System.out.println("Enter lab assistant's ID number:");
						String idNumber = scanner.next();
						userId = checkUserId(idNumber);
					} while (userId <= 0);
					System.out.printf("%-13s %-8s %-6s %-10s%n", "Date", "Start", "End", "Salary");
					int sessions = 0;
					int salary = 0;
					for (final String[] strings : logUsersSalary) {
						if (getInteger(strings[USER_ID]) == userId) {
							System.out.printf("%-13s %-8s %-6s %-10s%n", strings[DATE], strings[START_TIME_LOG], strings[END_TIME_LOG], strings[SALARY_LOG]);
							sessions++;
							salary += getInteger(strings[SALARY_LOG]);
						}
					}
					System.out.println("Total number of sessions: " + sessions);
					System.out.println("Total salary: " + salary);
					break;
				case 5:
				default:
					if (option == -1)
						return;
					if (option == -2)
						System.out.println("Type in valid option. [1,2,3,4,5,q]");
			}
		}
	}

	/**
	 * Check if it is a valid input and set the date for the salary.
	 */
	private static String setDate(String message) {
		do {
			System.out.println(message);
			String date = scanner.next();
			String[] dateSpited = message.split("-");
			if (dateSpited.length >= 3) {
				return date;
			} else {
				System.out.println("this input " + date + " is not valid. Should be formatted 00:00");
			}
		} while (true);
	}

	/**
	 * Check if it is a valid input and set the time for the salary.
	 */
	private static String setTime(String message) {
		do {
			System.out.println(message);
			String time = scanner.next();
			Pattern timePattern = Pattern.compile("^([01]?[0-9]|2[0-3]):[0-5][0-9]$");
			Matcher matcher = timePattern.matcher(time);
			if (matcher.matches()) {
				return time;
			} else {
				System.out.println("this input " + time + " is not valid. Should be formatted 00:00");
			}
		} while (true);
	}

	/**
	 * This method checks what number or letter you type in.
	 * Will only accept numbers between 1 and 5 or q rest will
	 * be ignored.
	 *
	 * @return
	 */
	public static int input() {

		if (scanner.hasNextInt()) {
			return scanner.nextInt();
		}
		if (scanner.hasNext()) {
			String quit = scanner.next();
			if (quit.equalsIgnoreCase("q"))
				return -1;
		}
		return -2;
	}

	/**
	 * Show the menu with all options.
	 */
	public static void menu() {
		System.out.println(
				"1. Add lab assistant\n" +
						"2. Remove lab assistant\n" +
						"3. Register working hours\n" +
						"4. Print pay slip\n" +
						"5. Print assistant summary\n" +
						"q. End program\n" +
						"> Enter your option:"
		);
	}

	/**
	 * Add the lab assistant to the register and add the education credit.
	 */
	private static String[][] addLabAssistant(final String[][] users) {
		System.out.println("Enter lab assistant's name.");
		String labAssistant = "";
		for (int i = 0; i < 2; i++) {
			String name = scanner.next();
			labAssistant += i > 0 ? " " + name : "" + name;
		}
		int credit = getCredit();

		return addUser(users, 1000, labAssistant, credit);
	}

	/**
	 * Sets the time, salary and date for the lab assistant. This will also log the data.
	 */
	public static void setTimeDataToUser(String[][] users, String[][] logUsersSalary, int userId, String startTime, String endTime, String date) {
		for (int i = 0; i < users.length; i++) {
			if (users[i] == null || users[i].length <= 0) {
				users[i] = new String[ROW_LENGTH];
			}
			int userID = getInteger(users[i][USER_ID]);
			if (userID == userId) {
				users[i][START_TIME] = startTime;
				users[i][END_TIME] = endTime;
				users[i][DATE] = date;
				System.out.println("Lab assistant " + users[i][USER_NAME]);
				String[] start = startTime.split(":");
				String[] end = endTime.split(":");
				int totalHoers = getTotalTime(start, end, 0);
				int totalMinute = getTotalTime(start, end, 1);

				System.out.println("Session time: " + totalHoers + " hours " + totalMinute + " minutes");
				int educationCredits = getInteger(users[i][EDUCATION_CREDITS]);
				int salary = getSalary(totalHoers, educationCredits);

				logSalary(logUsersSalary, userId, startTime, endTime, date, salary);

				System.out.println("Salary " + salary);
				return;
			}
		}
		System.out.println("could not find this ID " + userId);
	}

	/**
	 * Add the log entity to the logger to be printed later.
	 */
	private static void logSalary(final String[][] logUsersSalary, final int userId, final String startTime, final String endTime, final String date, final int salary) {
		for (int j = 0; j < logUsersSalary.length; j++) {
			if (logUsersSalary[j][USER_ID] == null || logUsersSalary[j][USER_ID].isEmpty()) {
				logUsersSalary[j][USER_ID] = String.valueOf(userId);
				logUsersSalary[j][START_TIME_LOG] = startTime;
				logUsersSalary[j][END_TIME_LOG] = endTime;
				logUsersSalary[j][DATE] = date;
				logUsersSalary[j][SALARY_LOG] = String.valueOf(salary);
				break;
			}
		}
	}

	/**
	 * Get the salary from amount of hours you have been working.
	 */
	private static int getSalary(final int totalHours, final int educationCredits) {
		int salary = 0;
		if (educationCredits < 100)
			salary = 120 * totalHours;
		if (educationCredits > 99 && educationCredits < 250)
			salary = 140 * totalHours;
		if (educationCredits > 249 && educationCredits < 400)
			salary = 160 * totalHours;
		return salary;
	}

	/**
	 * Get the time between the start and end time of your working hours.
	 */
	private static int getTotalTime(final String[] start, String[] end, int timeUnit) {
		int startTime = getInteger(start[timeUnit]);
		int endTime = getInteger(end[timeUnit]);

		return Math.max(startTime, endTime) - Math.min(startTime, endTime);
	}

	/**
	 * Retrieve the education credits and will check you put in valid number.
	 */
	private static int getCredit() {
		int credit;
		do {
			System.out.println("Enter number of education credits:");
			String educationCredits = scanner.next();
			credit = getCredit(educationCredits);
		} while (credit <= 0);
		return credit;
	}

	/**
	 * Retrieve the education credits and will check you put in valid number.
	 */
	private static int getCredit(final String educationCredits) {
		int credit = -1;
		try {
			credit = getInteger(educationCredits);
		} catch (NumberFormatException exception) {
			System.out.println("This input " + educationCredits + " is not valid number.");
			System.out.println("");
		}
		return credit;
	}

	/**
	 * Will get the user Id and make sure you don't put in invalid numbers.
	 */
	private static int checkUserId(final String userIdNumber) {
		int userId = -1;
		try {
			userId = getInteger(userIdNumber);
		} catch (NumberFormatException exception) {
			System.out.println("This input " + userIdNumber + " is not valid number.");
			System.out.println("");
		}
		return userId;
	}

	/**
	 * Add the lab assistant to the array and checks so the array need to be expanded.
	 */
	public static String[][] addUser(String[][] users, int startNumber, String userName, int educationCredits) {
		boolean needSpace = checkIfNeedMoreSpace(users);
		int amountToAdd = needSpace ? 1 : 0;

		users = extendArray(users, amountToAdd);
		String[] nextAvailableID = getNextAvailableID(users, Math.max(START_NUMBER, startNumber), 1);
		int availableNumbers = 0;
		for (int i = 0; i < users.length; i++) {
			if (users[i] == null || users[i].length <= 0) {
				users[i] = new String[ROW_LENGTH];
			}
			if (users[i][USER_ID] != null) continue;
			if (availableNumbers >= nextAvailableID.length) break;
			String userID = nextAvailableID[availableNumbers++];
			users[i][USER_ID] = userID;
			users[i][EDUCATION_CREDITS] = String.valueOf(educationCredits);
			users[i][USER_NAME] = userName;
			System.out.println("Lab assistant " + userName + " was assigned ID " + userID + " and added to the system.");
		}
		return users;
	}

	/**
	 * Remove the lab assistant to the array and checks so the array need to be expanded.
	 */
	public static void removeUser(String[][] users, int userID) {
		int indexToRemove = -1;
		String userName = "";
		for (int i = 0; i < users.length; i++) {
			if (users[i].length > 0 && users[i][USER_ID] != null && getInteger(users[i][USER_ID]) == userID) {
				userName = users[i][USER_NAME];
				indexToRemove = i;
				break;
			}
		}
		if (indexToRemove != -1) {
			if (users.length - 1 - indexToRemove >= 0) {
				System.arraycopy(users, indexToRemove + 1, users, indexToRemove, users.length - 1 - indexToRemove);
			}
			users[users.length - 1] = null;
			System.out.println("Lab assistant " + userName + " was removed from the system. ");
		} else {
			System.out.println("There is no lab assistant registered with ID " + userID);
		}
	}

	/**
	 * Retrieve the number from a string.
	 */
	private static int getInteger(final String userId) {
		if (userId == null) return -1;

		return Integer.parseInt(userId);
	}

	/**
	 * Get the next available ID, so it is not several copes of same ID.
	 */
	public static String[] getNextAvailableID(String[][] array, int startNumber, int amountOfNumbersNeeded) {
		String[] numbersToUse = new String[amountOfNumbersNeeded];
		int index = startNumber;
		int articlesFound = 0;

		while (articlesFound < amountOfNumbersNeeded) {
			boolean exists = false;
			for (String[] row : array) {
				if (row.length > 0 && row[USER_ID] != null && getInteger(row[USER_ID]) == index) {
					exists = true;
					break;
				}
			}
			if (!exists) {
				numbersToUse[articlesFound] = String.valueOf(index);
				articlesFound++;
			}
			index++;
		}
		return numbersToUse;
	}

	/**
	 * Check if the array need to be expanded.
	 */
	public static boolean checkIfNeedMoreSpace(String[][] array) {
		int needMoreSpace = 0;
		for (String[] row : array) {
			if (row.length > 0 && row[USER_ID] != null) {
				needMoreSpace++;
			}
		}
		return needMoreSpace == array.length;
	}

	/**
	 * Expand the array after amount of users or lab assistants added.
	 */
	public static String[][] extendArray(String[][] users, int numberOfUsers) {
		if ((users.length - numberOfUsers) != 0) {
			String[][] newArticles = new String[users.length + numberOfUsers][3];
			System.arraycopy(users, 0, newArticles, 0, users.length);
			return newArticles;
		}
		return users;
	}
}




