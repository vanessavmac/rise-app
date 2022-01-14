/*
package com.example.rise;

import java.util.Scanner;

public class Rise {

	public static void main(String[] args) {

		Scanner input = new Scanner(System.in);
		UserOptions userOptions = new UserOptions();
		UserInfo userInfo = new UserInfo();

		int choice;
		String stringA, stringB;
		String routineName, taskName, newRoutineName, newTaskName;
		int routineStartTime, taskDuration, newRoutineStartTime, newTaskDuration;

		do {
			do {
				System.out.println(
						"\n1. Edit Today's Motivation\n2. Add New Routine\n3. Delete Routine\n4. Edit Routine\n5. Home\nEnter -1 to Quit\n");
				choice = input.nextInt();
			} while (choice < 0 && choice > 5);

			switch (choice) {

			case -1:
				break;

			case 1:
				System.out.println("Today I want to");
				stringA = input.next();
				System.out.println("and finishing my morning routine will");
				stringB = input.next();
				userInfo.editMotivation(stringA, stringB);
				break;

			case 2:
				System.out.println("Enter routine name: ");
				routineName = input.next();
				System.out.println("Enter routine start time: ");
				routineStartTime = input.nextInt();
				userOptions.addRoutine(routineName, routineStartTime);
				break;

			case 3:
				System.out.println("Enter routine name to delete: ");
				routineName = input.next();
				userOptions.deleteRoutine(routineName);
				break;

			case 4:
				System.out.println("1. Add Task\n2. Delete Task\n3. Edit Task\n4. Edit Routine Info");
				choice = input.nextInt();

				// Add task
				switch (choice) {

				case 1:
					System.out.println("Enter routine name: ");
					routineName = input.next();
					System.out.println("Enter task name: ");
					taskName = input.next();
					System.out.println("Enter task duration: ");
					taskDuration = input.nextInt();
					userOptions.addTask(routineName, taskName, taskDuration);
					break;

				case 2:
					System.out.println("Enter routine name: ");
					routineName = input.next();
					System.out.println("Enter task name: ");
					taskName = input.next();
					userOptions.deleteTask(routineName, taskName);
					break;

				case 3:
					System.out.println("Enter routine name: ");
					routineName = input.next();
					System.out.println("Enter task name: ");
					taskName = input.next();
					System.out.println("Enter new task name: ");
					newTaskName = input.next();
					System.out.println("Enter new task duration: ");
					newTaskDuration = input.nextInt();
					userOptions.editTask(routineName, taskName, newTaskName, newTaskDuration);
					break;

				case 4:
					System.out.println("Enter routine name: ");
					routineName = input.next();
					System.out.println("Enter new routine name: ");
					newRoutineName = input.next();
					System.out.println("Enter new routine start time: ");
					newRoutineStartTime = input.nextInt();
					userOptions.editRoutine(routineName, newRoutineName, newRoutineStartTime);
					break;

				}
				break;

			case 5:
				userOptions.displayAll(userInfo);
				break;
			}

		} while (choice != -1);

		input.close();

	}

}
*/
