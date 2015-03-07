package com.github.thbrown.actionrecorder;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class Command {

	private CommandType type;
	
	// Consider instantiating this only if there are arguments
	private List<Integer> arguments = new ArrayList<Integer>();
	
	StatusArea status;

	Command(StatusArea whereToSendStatusUpdates, CommandType type, Integer... arguments) {
		this.status = whereToSendStatusUpdates;
		this.type = type;
	    for(Integer argument : arguments){
	        this.arguments.add((int)argument);
	    }
	}

	public Command(CommandType type, Integer... arguments) {
		this.type = type;
	    for(Integer argument : arguments){
	        this.arguments.add((int)argument);
	    }
	}

	void execute(Robot r) {
		switch(this.type){
		case MOUSE_MOVE:
			r.mouseMove(arguments.get(0),this.arguments.get(1));
			break;
		case MOUSE_PRESS:
			switch(this.arguments.get(0)) {
			case 1: r.mousePress(InputEvent.BUTTON1_MASK); break;
			case 2: r.mousePress(InputEvent.BUTTON2_MASK); break;
			case 3: r.mousePress(InputEvent.BUTTON3_MASK); break;
			default: status.append("Mouse " + this.arguments.get(0) + " Button Not Recognized A");
			}
			break;
		case MOUSE_RELEASE:
			switch(this.arguments.get(0)) {
			case 1: r.mouseRelease(InputEvent.BUTTON1_MASK); break;
			case 2: r.mouseRelease(InputEvent.BUTTON2_MASK); break;
			case 3: r.mouseRelease(InputEvent.BUTTON3_MASK); break;
			default: status.append("Mouse " + this.arguments.get(0) + " Button Not Recognized B");
			}
			break;
		case KEY_PRESS:
			r.keyPress(arguments.get(0));
			break;
		case KEY_RELEASE:
			r.keyRelease(arguments.get(0));
			break;
		case MOUSE_WHEEL_MOVE:
			r.mouseWheel(arguments.get(0));
			break;
		case SLEEP:
			try {
				Thread.sleep(this.arguments.get(0));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		default:
			status.append("Error! Command Not Recognized!");
		}
	}
	
	private static String formatArguments(List<Integer> arguments) {
		StringBuilder result = new StringBuilder();
		result.append("(");
		String prefix = "";
		for(Integer argument : arguments) {
			result.append(prefix);
			result.append(argument.toString());
			prefix = ",";
		}
		result.append(");");
		return result.toString();
	}

	public String toString() {
		return type.toString() + formatArguments(arguments);
	}
}
