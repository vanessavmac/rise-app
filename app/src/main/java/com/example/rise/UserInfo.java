package com.example.rise;

public class UserInfo {

	private static String motivation;
	private String taskName;
	private String time;
	
	public UserInfo() {
		//empty constructor
	}
	
	public UserInfo(String inputName) {
		taskName = inputName;
	}
	
	public UserInfo(String inputName, String inputTime) {
		taskName = inputName;
		time = inputTime;
	}

	public String getTaskName() {
		return taskName;
	}

	public String getTime() {
		return time;
	}

	public void editMotivation(String stringA, String stringB) {
		motivation = "Today I want to " + stringA + "\nand finishing my morning routine will " + stringB;
	}

	public String getMotivation() {
		return motivation;
	}
	
	//checks if it's the correct routine name
	public boolean equals(Object task) {
		UserInfo testTask = (UserInfo) task;
		if (taskName.equals(testTask.taskName)) {
			return true;
		} else {
			return false;
		}
	}
	
	public String toString() {
		String event = taskName + " - " + time;
		return event;
	}

}
