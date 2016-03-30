/**Reordered Multitap is the basic class that will interact with Keypad and cause Keypad to act like a cell phone text keypad with the characters based off of the actual English language letter frequency.
 * 
 * @author Kameren Lymon and Darin Lunde
 *
 */
public class ReorderedMultitap extends Multitap implements InterfaceName{

	/**
	 * Constructor for Reordered Multitap. Modifies the character array from Multitap to be relative to the English language letter Frequency.
	 */
	ReorderedMultitap(){
		int temp [] = {0,2,1,4,3,5,8,7,6,11,10,9,14,13,12,18,17,15,16,19,20,21,22,24,23,25};
		for(int i=0;i<2;i++){
			for(int j=0;j<26;j++){
				characters[26*i+j]=(char)(temp[j]+97-32*i);
			}
		}
		
	}
}
