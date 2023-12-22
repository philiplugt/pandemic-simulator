
/**
 * Pandemulation - A simulator modeling virus spread
 * 
 * @author PvdL
 *
 */
public class Pandemulation {

	//Main method
	public static void main(String[] args) {
		
		//Try to launch the application
		try {
			Display.createDisplayFrame();
		} catch (ClassCastException err) {
			err.printStackTrace();
		}
	}
}
