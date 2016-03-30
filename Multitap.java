/**Multitap is the basic class that will interact with Keypad and cause Keypad to act like a cell phone text keypad
 * 
 * @author Kameren Lymon and Darin Lunde
 *
 */
public class Multitap implements InterfaceName {

	protected char [] characters = new char[84];//Our character array. Holds all of the characters that our keypad is capable of displaying.
	private int oldkey=-1;//Keeps track of the last key pressed. Set to -1 (as a sort of null value) for the first time used. 
	private int timespressed=0;//Keeps track of how many times the last key was pressed.
	private boolean shiftflag = false;//Keeps track of whether or not the user has indicated an upper case character.
	private int n=0;//Keeps track of the number of button presses. We kept the name of this variable identical to the name you gave it in Digits.
	private String oldinput="";//Keeps track of the actual saved input so that we can differentiate between letters definitively added and letters potentially going to be added. Note that it defaults to an empty field upon startup.
	/**
	 * Constructor for Multitap. Fills our character array.
	 */
	public Multitap(){
		for(int i=0;i<26;i++){//For loop filling the first 26 spots in our array with lower case Latin letters.
			characters[i] = (char)(i+97);//97 is the ASCII value for the Latin letter "a"  The count is maintained because of the fact that the first spot in an array is spot zero.
		}
		for (int i=26;i<52;i++){//For loop filling the next 26 spots in our array with upper case Latin letters.
			characters[i] = (char)(i-26+65);//65 is the ASCII value for the Latin letter "A"  The -26 is to adjust for the first 26 spots in the array.
		}
		for (int i=52;i<84;i++){//For loop filling all remaining places in our array with special characters.
			characters[i] = (char)(i-52+33);//Characters with ASCII values between 33 and 64 inclusive are included. The -52 is to adjust for the first 52 spots in the array.
		}
	}
	/**
	 * Returns the number of button presses
	 */
	public int getNumPresses(){return n;}
	/**
	 * The main append function called by Keypad. The main bread and butter of our class. The append method does not actually append as it's name implies, but instead acts as a gigantic switch board for different situations, thereby controlling how the incoming string will be appended.
	 */
	public String append(String input, int key){
		n++;//Increments the number of button presses
		//Everything below is the main bread and butter of our class. The append method does not actually append as it's name implies, but instead acts as a gigantic switch board for different situations, thereby controlling how the incoming string will be appended.
		if(key<0 || key>11){//This If statement checks that the incoming key is valid, and executes if it isn't.
			throw new IllegalArgumentException("Key not Supported");//This throws the exception in response to an invalid key.
		}
		else if (oldkey != key && oldkey != -1){//This If statement executes only if we've detected a change in the button pressed. Note that the oldkey != -1 works to eliminate this acting right away on startup due to our aforementioned null value.
			if(oldkey > 0 && oldkey < 10){//This If statement (embedded) executes only if the previous button pressed was a character key.
				input = addchar(chooseletter(oldkey))+chooseletter(key);//When executed, this sets the input to add the appropriate old key and then display (but not add) the new button that was pressed. If a non letter button was pressed, it will display a null or blank space, which will not be visible by the user.
			}
			else if(oldkey == 0 || oldkey == 10){//This If statement (embedded) executes only if the previous button pressed was the "next" key or the "shift" key.
				input = cyclechar(chooseletter(key));//When executed, this simply sets the input to display it's saved text in addition to the new button that was pressed. If a non letter button was pressed, it will display a null or blank space, which will not be visible by the user.
			}
			else if (oldkey == 11){//This If statement (embedded) executes only if the previous button pressed was the "space" key.
				oldinput = oldinput + " ";//This saves the space key as an addition to our overall display.
				input = cyclechar(chooseletter(key));//After the above addition, this simply sets the input to display it's saved text in addition to the new button that was pressed. If a non letter button was pressed, it will display a null or blank space, which will not be visible by the user.
			}
			if(key == 10){//This If statement (embedded) executes if the new button pressed was the shift key, and will execute even if one of the above if statements also executed.
				shift();//This executes the shift method, recording that the shift button has been pressed.
			}
			//Notice that both of the next two statements occur AFTER everything else in the If statement has had a chance to occur. This is crucial to the program's function, as doing otherwise can cause erroneous results.
			timespressed = 1;//Since the If statement will only execute if the previous key differed from the new key, this statement resets the timespressed variable to 1, so that we can keep an accurate count of our button presses for letter rotation purposes.
			oldkey = key;//This records the key that was pressed
		}
		else if (key == 10){//This If statement executes only if the shift key has been pressed more than once consecutively (because if it had only been pressed once, it would be called in the above if statement) OR if the shift key was the very first button pressed after startup.
			shift();//Records the current state of shiftflag, which is used to differentiate between upper and lower case outputs.
			timespressed = 1;//Since the shift statement is being called, this resets our timespressed variable to 1, since shift does not need to utilize our character rotation tracking.
			oldkey = key;//Records that the shift key was pressed last.
		}
		else if (key == 11){//This If statement executes only if the space key has been pressed more than once consecutively (because if it had only been pressed once, it would be called in the above if statement) OR if the space key was the very first button pressed after startup.
			timespressed = 1;//Since the space is being added, this resets our timespressed variable to 1, since shift does not need to utilize our character rotation tracking.
			oldinput = oldinput + " ";//Adds the space to our input and saves it.
			input = oldinput;//Sets the input to our saved input
			oldkey = key;//Records that the space key was pressed last.
		}
		else if (key == 0){//This if statement executes only if the next key has been pressed more than once consecutively (because if it had only been pressed once, it would be called in the above if statement) OR if the next key was the very first button pressed after startup.
			//Note that nothing needs to happen when the next key is pressed, other than us recognizing that the key has been pressed. As such, no changes to the input are made within this If statement.
			timespressed = 1;//Since the space is being added, this resets our timespressed variable to 1, since shift does not need to utilize our character rotation tracking.
			oldkey = key;//Records that the next key was pressed last.
		}
		else{//This if statement executes if none of the above statements executes, namely: if the last button pressed is identical to the current button being received AND if both those buttons are character buttons.
			timespressed++;//Records that the button has been pressed an additional time, incrementing our timespressed. Note that if this is the first button being pressed after startup, this will increment timespressed from it's startup value of zero so that our counts will remain correct.
			input = cyclechar(chooseletter(key));//This simply sets the input to display it's saved text in addition to the new button that was pressed.
			oldkey = key;//Records that this key was pressed last. It's probably not actually necessary here, but it doesn't hurt to have it.
		}
		return input;//Returns our modified input
	}
	/**
	 * addchar is the method used to save an additional character to our display. Note that it does not choose what character it's adding, as it is instead given that character as an argument.
	 * @param letter
	 * 
	 */
	private String addchar(char letter){
		shiftflag = false;//Resets the shiftflag to false, since this method will only be called when an additional character is being saved in. As such, the next character should not default to uppercase.
		timespressed = 1;//Resets timespressed to 1 to keep our counts accurate. Not including this statement will cause erroneous results.
		oldinput = oldinput + letter;//This is the method that actually adds the character to our saved input.
		return oldinput;//Returns the saved input
	}
	/**
	 * cyclechar is the method used to cycle through the character to be eventually added to our display. Note that it does not choose what character it's displaying, as it is instead given that character as an argument.
	 * @param letter
	 * 
	 */
	private String cyclechar(char letter){
		return oldinput + letter;//Returns the saved input plus the appropriate character
	}
	/**
	 * chooseletter is our switchboard that selects which character is the appropriate one to be dealing with given the circumstance dictated by append. No other method directly accesses our character string. Note that since all of our variables are global class variables, it only needs to be passed the appropriate key, again determined by our append method.
	 * @param key
	 * 
	 */
	private char chooseletter(int key){ 
		char temp = 0;//Sets a temporary (and named as such) character variable to a null space. This is important not only to ensure we return said variable, but also because then if a non-character key has been pressed, the null space will be returned, and the user will not notice any difference.
		//PLEASE NOTE: The following if statements are all almost entirely identical. They only differ slightly with their numeric values, and this is only so that the correct spot in the character array (and therefore the correct character) is returned.
		if(key>1 && key<7){//Reads in which key is being passed. Again, this is essential to choosing the correct charater and interacting with our character array. All of the if statements contained within this method that use key have this purpose.
			if(shiftflag == false){//Reads shiftflag to determine whether or not a lowercase or uppercase character should be returned
				temp = characters[3*(key-2)+(timespressed-1)%3];//returns the proper character. Note that on most keys, there are 3 characters, which is why we multiply by three. We then decrement key by 2 to get back to a correlation with our array. After this, we then add the times pressed minus 1 remainer of 3. This allows our user to endlessly press any key s/he desires without throwing off our counts and therefore our button rotation.
			}
			else{//Executes when the character should be uppercase.
				temp = characters[3*(key-2)+(timespressed-1)%3+26];//Adds 26 so that we're back in line with our array for uppercase characters.
			}
		}//Since the If statements are inherently similar, I will now only comment on the differences between them.
		else if(key==7){
			if(shiftflag == false){
				temp = characters[3*(key-2)+(timespressed-1)%4];//Has remainder 4 to keep our counts in order since that key has 4 characters rather than 3.
			}
			else{
				temp = characters[3*(key-2)+(timespressed-1)%4+26];
			}
		}
		else if(key==8){
			if(shiftflag == false){
				temp = characters[3*(key-2)+(timespressed-1)%3+1];//+1 compensates for the previous 4 character key
			}
			else{
				temp = characters[3*(key-2)+(timespressed-1)%3+27];//+1 (+26+1) compensates for the previous 4 character key
			}
		}
		else if(key==9){
			if(shiftflag == false){
				temp = characters[3*(key-2)+(timespressed-1)%4+1];//Has remainder 4 to keep our counts in order since that key has 4 characters rather than 3.
			}
			else{
				temp = characters[3*(key-2)+(timespressed-1)%4+27];
			}
		}
		else if (key==1){//This If statement will execute if the 1 key is pressed.
			temp = characters[(timespressed-1)%32+52];//Note that this statement does not make another reference to key. This is because this block will only execute if the one key is being pressed. The remainder (like the above if statements) will allow us to cycle through all of our available special characters.
		}
		return temp;//Returns the character that temp has been set to. Note that this will sometimes return a null character, but that's okay because our user will not notice any difference in functionality.
	}
	/**
	 * This method controls the shiftflag (almost exclusively). The addchar method also will directly change the shiftflag after every added character.
	 */
	private void shift(){
		if (shiftflag == false){//If shift flag is false
			shiftflag = true;//Set shift flag to true.
			return;//Return so that the next statement does not need to be contained within an else block
		}//Because of the above return, the below will only execute if shiftflag is true
		shiftflag = false;//Set shift flag to false.
		return;//Return
	}
	
}
