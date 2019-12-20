import javax.swing.JLabel;
/**
 * This class generates a Box to be clicked on the TicTacToe game.
 * @author Diabul Haque
 *
 */

public class Box extends JLabel{
	private int number=0;
	private boolean clicked=false;
	
	/**
	 * This constructor assigns a number to the box
	 * @param i
	 */
	public Box(int i) {
		number=i;
	}
	
	/**
	 * This function returns whether the box has been clicked previously
	 * @return boolean
	 */
	public boolean isClicked() {
		return clicked;
	}
	
	/**
	 * This function sets the 'clicked' variable to true
	 * @return boolean
	 */
	public void gotClicked() {
		clicked=true;
	}
	
	/**
	 * This getter function returns the number of the box 
	 * @return boolean
	 */
	public int getNumber() {
		return number;
	}

}
