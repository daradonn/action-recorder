package com.github.thbrown.actionrecorder;

import java.awt.AWTException;
import java.awt.Robot;

/**
 * This class contains the variables and methods necessary to execute the mouse movements and button presses
 * saved during a recording session.
 * 
 * @author thbrown
 */
public class Playback extends Thread {
	
	// Flag variable so the Thread can be halted from the main thread
	private volatile boolean run;
	
	// The text area on the UI, used to show status updates
	private StatusArea statusConsole;
	private Storage newRecord;
	
	Playback(Storage dataHolder, StatusArea statusConsole) {
		run = true;
		this.statusConsole = statusConsole;
		this.newRecord = dataHolder;
	}
	
	public void requestThreadStop() {
		run = false;
	}
	
	public void run() {
		// Launch the robot in a new thread so the buttons on the UI are still pressable
		Robot executingRobot;
		
		// Execute each command using a java robot
		try {
			executingRobot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
			statusConsole.append("Error: Unable to start java robot\n");
			return;
		}
		
		// Call execute on each command in the list
		for(Command c : newRecord.getCommandList()) {
			if(!run) {
				break;
			}
			c.execute(executingRobot);
			statusConsole.append(c.toString() + "\n");
		}
	}

}
