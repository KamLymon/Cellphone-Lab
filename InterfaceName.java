/**
 * InterfaceName is the Interface used by all of our entry methods and Keypad. It's necessary so that you can pass any method into Keypad
 * @author Kameren Lymon and Darin Lunde
 *
 */
public interface InterfaceName {//We had originally named this "Interface" but we thought that that may cause some confusion amongst fellow programmers, since the only difference between our interface name and the interface operator would be the capital i.
	//Below are the two methods contained within the interface, that digits, Multitap, and ReorderedMultitap must have.
	public int getNumPresses();
	public String append(String txt, int key);
	
}

