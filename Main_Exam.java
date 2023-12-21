package Main_Exam;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 */
public class Main_Exam {

	static final Scanner scanner = new Scanner(System.in);
	private static final int START_NUMBER = 1000;
	private static final int ROW_LENGTH = 6;

	private static final int USER_ID = 0;
	private static final int EDUCATION_CREDITS = 1;
	private static final int USER_NAME = 2;
	private static final int START_TIME = 3;
	private static final int END_TIME = 4;
	private static final int DATE = 5;


	public static void main(String[] args) {
		System.out.println("This is the main exam for D0017D HT23 held on 2023-12-21");
		System.out.println("----------------------------------\n" +
				"# LTU Lab Assistant Manager\n" +
				"----------------------------------\n");
		String[][] users = new String[10][ROW_LENGTH];
		//Have npo time to implement this for 4 and 5.
		int[][][] logUsersSalary = new int[10][5][ROW_LENGTH];

		while (true) {
			menu();
			int option = input();

			switch (option) {
				case 1:
					users = addLabAssistant(users);
					break;
				case 2:
					int userId;
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
					setTimeDataToUser(users, userId, startTime, endTime, date);
				case 4:

				case 5:
				default:
					if (option == -1)
						return;
					if (option == -2)
						System.out.println("Type in valid option. [1,2,3,4,5,q]");
			}
		}
	}

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

	public static void setTimeDataToUser(String[][] users, int userId, String startTime, String endTime, String date) {
		for (int i = 0; i < users.length; i++) {
			if (users[i] == null || users[i].length <= 0) {
				users[i] = new String[ROW_LENGTH];
			}
			if (getInteger(users[i][USER_ID]) == userId) {
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

				System.out.println("Salary " + salary);
				return;
			}
		}
		System.out.println("could not find this ID " + userId);
	}

	private static int getSalary(final int totalHoers, final int educationCredits) {
		int credit = 0;
		if (educationCredits < 100)
			credit = 120 * totalHoers;
		if (educationCredits > 99 && educationCredits < 250)
			credit = 140 * totalHoers;
		if (educationCredits > 249 && educationCredits < 400)
			credit = 160 * totalHoers;
		return credit;
	}

	private static int getTotalTime(final String[] start, String[] end, int timeUnit) {
		int startTime = getInteger(start[timeUnit]);
		int endTime = getInteger(end[timeUnit]);

		return Math.max(startTime, endTime) - Math.min(startTime, endTime);
	}

	private static int getCredit() {
		int credit;
		do {
			System.out.println("Enter number of education credits:");
			String educationCredits = scanner.next();
			credit = getCredit(educationCredits);
		} while (credit <= 0);
		return credit;
	}

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

	private static int getInteger(final String userId) {
		if (userId == null) return -1;

		return Integer.parseInt(userId);
	}

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


	public static boolean checkIfNeedMoreSpace(String[][] array) {
		int needMoreSpace = 0;
		for (String[] row : array) {
			if (row.length > 0 && row[USER_ID] != null) {
				needMoreSpace++;
			}
		}
		return needMoreSpace == array.length;
	}

	public static String[][] extendArray(String[][] users, int numberOfUsers) {
		if ((users.length - numberOfUsers) != 0) {
			String[][] newArticles = new String[users.length + numberOfUsers][3];
			System.arraycopy(users, 0, newArticles, 0, users.length);
			return newArticles;
		}
		return users;
	}
}



