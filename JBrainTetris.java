package tetris;

import java.awt.Dimension;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.UIManager;


@SuppressWarnings("serial")
public class JBrainTetris extends JTetris{
	
	private Brain brain;	//created per the specs
	private JCheckBox brainMode;
	private JSlider adversary;
	private JPanel little;
	//private JLabel status;
	private JCheckBox animateFalling;
	
	private Brain.Move move;
	private int currentCount;
	
	/*
	 * Constructor just calls parent class.
	 */
	public JBrainTetris(int pixels){
		super(pixels);
		brain = new DefaultBrain();
		currentCount = 0;
		move = new Brain.Move();
	}
	
	/**
	 Creates the panel of UI controls. Adds 
	*/
	@Override
	public JComponent createControlPanel(){
		JPanel panel = (JPanel) super.createControlPanel();
		
		//add Brain checkbox
		panel.add(new JLabel("Brain: "));
		brainMode = new JCheckBox("Brain active");
		
		//add checkbox that allows user to determine if AI can drop blocks or not
		animateFalling = new JCheckBox("Animate fall");
		animateFalling.setSelected(true);
		panel.add(brainMode);
		panel.add(animateFalling);
		
		
		//add panel for difficulty
		little = new JPanel();
		little.add(new JLabel("Difficulty: "));
		adversary = new JSlider(0,100,0);
		adversary.setPreferredSize(new Dimension(100,15));
		little.add(adversary);
		
		panel.add(little);
		
		//status = new JLabel("Ok");
		//panel.add(status);
		
		return panel;
	}
	
	@Override
	protected void enableButtons(){
		startButton.setEnabled(!gameOn);
		stopButton.setEnabled(gameOn);
	}
	
	@Override
	public Piece pickNextPiece(){
		int sliderValue = adversary.getValue();
		int randomNumber = random.nextInt(100);
		
		//if the random number is greater than the slider value, just return a random 
		//piece. Otherwise, return the worst piece possible given the current board state
		if(randomNumber>=sliderValue){
		//	status.setText("Ok");
			return super.pickNextPiece();
			
		}
		else{
			
			//status.setText("*Ok*");
			Piece worstPiece = null;
			int worstScore = 0;
			
			for(int i = 0; i<pieces.length;i++){
				move = brain.bestMove(board, pieces[i], board.getHeight(), move);
				
				if(move==null) continue;
				else if(worstPiece==null){
					worstPiece = move.piece;
					worstScore = (int)move.score;
				}
				else if(move.score>worstScore){
					worstPiece = move.piece;
					worstScore = (int) move.score;
				}
			}
			
			if(worstPiece==null) 	return super.pickNextPiece();
			else			return worstPiece;
		}
		
		
	}
	
	
	/*
	 * "Everytime the system calls tick(DOWN) to move the piece down one, 
	 * JBrainTetris takes the opportunity to move the piece a bit first. 
	 * The Brain may do up to one rotation and one left/right move each 
	 * time tick(DOWN) is called."
	 */
	
	@Override
	public void tick(int verb){
		
		if(brainMode.isSelected() && verb == DOWN){
			
			if(currentCount != count){
				currentCount = count;
				board.undo();
				move = brain.bestMove(board, currentPiece, board.getHeight(), move);
			}
			
			if(move!=null){
				if(!currentPiece.equals(move.piece))	tick(ROTATE);
				else if(move.x>currentX)		tick(RIGHT);
				else if(move.x<currentX) 		tick(LEFT);
				else if(!animateFalling.isSelected()&&move.y<currentY){
					verb = DROP;
				}
			}
		}
		
		super.tick(verb);
	}
	
	/*
	 * This is basically a copy of the super classes main method 
	 * except it creates a JBrainTetris instead of a JTetris.
	 */
	public static void main(String[] args){
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception ignored){
			//specs says its okay to ignore this exception
		}
		
		JBrainTetris tetris = new JBrainTetris(16);
		JFrame frame = JTetris.createFrame(tetris);
		frame.setVisible(true);
	}

}
