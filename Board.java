// Board.java
package tetris;

/**
 CS108 Tetris Board.
 Represents a Tetris board -- essentially a 2-d grid
 of booleans. Supports tetris pieces and row clearing.
 Has an "undo" feature that allows clients to add and remove pieces efficiently.
 Does not do any drawing or have any idea of pixels. Instead,
 just represents the abstract 2-d board.
*/
public class Board	{
	
	//width and height of the board
	private int width;
	private int height;
	
	private boolean[][] grid;
	private boolean[][] backupGrid;
	
	private boolean DEBUG = true;
	boolean committed;
	
	private int[] columnHeights;	//the height of each column
	private int[] rowWidths;	//the width of each row
	
	//backups to allow board to undo illegal moves
	private int[] backupHeights;
	private int[] backupWidths;
	
	private int maxHeight;
	
	
	
	/**
	 Creates an empty board of the given width and height
	 measured in blocks.
	*/
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		grid = new boolean[width][height];
		backupGrid = new boolean[width][height];
		committed = true;
		
		//initialize arrays
		columnHeights = new int[width];
		rowWidths= new int[height];
		
		backupWidths = new int[height];
		backupHeights = new int[width];
		
		maxHeight = 0;
	}
	
	
	/**
	 Returns the width of the board in blocks.
	*/
	public int getWidth() {
		return width;
	}
	
	
	/**
	 Returns the height of the board in blocks.
	*/
	public int getHeight() {
		return height;
	}
	
	
	/**
	 Returns the max column height present in the board.
	 For an empty board this is 0.
	*/
	public int getMaxHeight() {	 
		return maxHeight; 
	}
	
	/*
	 * This method updates the maxHeight ivar
	 */
	private void recalculateMaxHeight(){
		maxHeight = 0;
		
		for(int i =0; i<columnHeights.length;i++){
			if(maxHeight<columnHeights[i]){
				maxHeight = columnHeights[i];
			}
		}
	}
	
	
	/**
	 Checks the board for internal consistency -- used
	 for debugging.
	*/
	public void sanityCheck() {
		if (DEBUG) {
			
			int tempMaxHeight = 0;
			
			//temp arrays to hold current heights and widths
			int[] tempColumns = new int[width];
			int[] tempRows = new int[height];
			
			//go through grid and calculate height and width of each row
			for(int i = 0; i<width;i++){
				for(int j = 0;j<height;j++){
					if(grid[i][j]==true){
						tempColumns[i] = j+1;
						tempRows[j]++;
					}
				}
				if(tempMaxHeight<tempColumns[i]) tempMaxHeight = tempColumns[i];
				
			}
			
			if(tempMaxHeight!=maxHeight){
				System.err.println("Max height should be " + tempMaxHeight + " but is " + maxHeight);
				throw new RuntimeException("Max height is wrong");

			}
			
			//go through height and width arrays and make sure they match
			
			for(int i = 0;i<width;i++){
				if(tempColumns[i]!=columnHeights[i]){
					System.err.println("Column " + i + " has height " + columnHeights[i] + " but should have width " + tempColumns[i]);
					throw new RuntimeException("Column " + i + " has the wrong height");
				}
			}
			
			for(int j = 0; j<height;j++){
				if(tempRows[j]!=rowWidths[j]){
					System.err.println("row " + j + " has width " + rowWidths[j] + " but should have width " + tempRows[j]);
					throw new RuntimeException("Row " + j + " has the wrong width");
				}
			}
			
		}
	}
	
	/**
	 Given a piece and an x, returns the y
	 value where the piece would come to rest
	 if it were dropped straight down at that x.
	 
	 <p>
	 Implementation: use the skirt and the col heights
	 to compute this fast -- O(skirt length).
	*/
	public int dropHeight(Piece piece, int x) {
		int[] skirt = piece.getSkirt();
		int king = 0;
		
		for(int i =0;i<skirt.length;i++){
			int temp =columnHeights[x+i] - skirt[i];
			if(temp>king) king = temp;
		}
		return king;
		
	}
	
	
	/**
	 Returns the height of the given column --
	 i.e. the y value of the highest block + 1.
	 The height is 0 if the column contains no blocks.
	*/
	public int getColumnHeight(int x) {
		return columnHeights[x];
	}
	
	
	/**
	 Returns the number of filled blocks in
	 the given row.
	*/
	public int getRowWidth(int y) {
		 return rowWidths[y];
	}
	

	
	
	/**
	 Returns true if the given block is filled in the board.
	 Blocks outside of the valid width/height area
	 always return true.
	*/
	public boolean getGrid(int x, int y) {
		if(x>=width||x<0||y>=height||y<0) return true;
		else return grid[x][y]; 
	}
	
	
	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;
	
	/**
	 Attempts to add the body of a piece to the board.
	 Copies the piece blocks into the board grid.
	 Returns PLACE_OK for a regular placement, or PLACE_ROW_FILLED
	 for a regular placement that causes at least one row to be filled.
	 
	 <p>Error cases:
	 A placement may fail in two ways. First, if part of the piece may falls out
	 of bounds of the board, PLACE_OUT_BOUNDS is returned.
	 Or the placement may collide with existing blocks in the grid
	 in which case PLACE_BAD is returned.
	 In both error cases, the board may be left in an invalid
	 state. The client can use undo(), to recover the valid, pre-place state.
	*/
	public int place(Piece piece, int x, int y) {
		
		// flag !committed problem
		if (!committed) throw new RuntimeException("place commit problem");
		committed = false;
		int result = PLACE_OK;
		
		//temp int arrays to hold the x and y values of the piece
		int xTemp[] = new int[piece.getBody().length];
		int yTemp[] = new int[piece.getBody().length];
		int counter = 0;
		backup();

		//for each point in the old body, fill the temp arrays with the new points and make sure
		//these new points aren't outside the grid nor do they overlap any pieces.
		for(TPoint p: piece.getBody()){
			
			//calculate new x and y values
			int newX = x+p.x;
			int newY = y+p.y;
			
			//if any of the values are outside the bounds of the grid return PLACE_OUT_BOUNDS
			if(newX<0||newX>=width||newY<0||newY>=height) {
				result = PLACE_OUT_BOUNDS;
				break;
			}
			
			//if the grid is already filled at that spot, return PLACE_BAD
			else if(grid[newX][newY]==true) {
				result = PLACE_BAD;
				break;
			}
			
			//if both these conditions are met then put the values in the array and move on
			xTemp[counter] = newX;
			yTemp[counter] = newY;
			counter++;
			
		}
				
		//if this point has been reached, you can put the squares of the piece in the grid. 
		//As this is done, the width and height of each column is adjusted.
		if(result==PLACE_OK){
			for(int i=0;i<xTemp.length;i++){
				grid[xTemp[i]][yTemp[i]] = true;
				if(columnHeights[xTemp[i]]<yTemp[i]+1){
					columnHeights[xTemp[i]] = yTemp[i] +1;
				}
				rowWidths[yTemp[i]]++;
				if(rowWidths[yTemp[i]]==width){
					result = PLACE_ROW_FILLED;
				}
			
			}
		}

		
		recalculateMaxHeight();
		sanityCheck();
		return result;
	}
	
	
	/*
	 * Copy current grid to backups
	 */
	private void backup(){
		for(int i = 0;i<grid.length;i++){
			System.arraycopy(grid[i], 0, backupGrid[i], 0, grid[i].length);
		}
		System.arraycopy(columnHeights, 0, backupHeights, 0, columnHeights.length);
		System.arraycopy(rowWidths, 0, backupWidths, 0, backupWidths.length);

	}
	
	
	/**
	 Deletes rows that are filled all the way across, moving
	 things above down. Returns the number of rows cleared.
	*/
	public int clearRows() {
		if(committed){
			committed = false;
			backup();
		}
		
		int rowsCleared = 0;
		for(int i =0; i<maxHeight;i++){
			rowsCleared+=clearSingleRow(i);
		}
		sanityCheck();
		return rowsCleared;
	}
	
	/*
	 * Checks whether each row is filled. If it is, then it deletes the entire row and increments the counter. Returns the number of times
	 * row x was cleared.
	 */
	private int clearSingleRow(int x){
		
		int rowsCleared =0;
		//first check if every space in row x is filled
		if(rowWidths[x]!=width) return rowsCleared;
		rowsCleared++;

		//shift all rows down one
		for(int i =0; i<width;i++){
			for(int j =x; j<=maxHeight;j++){
				if(j<(height-1)){		
					grid[i][j] = grid[i][j+1];
				}
				//this just sets the top row to false if need be
				else{
					grid[i][j] = false;
				}
				
			}
		}
		
		recalculateRowWidthsAndColumnHeights();
		recalculateMaxHeight();

		//check if a filled row dropped down into the current row and clear it if it did
		rowsCleared += clearSingleRow(x);
		
		return rowsCleared;
	}
	

	
	/*
	 * Basic brute force recalculation of heights and widths.
	 */
	private void recalculateRowWidthsAndColumnHeights(){
		int[] tempColumns = new int[width];
		int[] tempRows = new int[height];
		
		//go through grid and calculate height and width of each row
		for(int i = 0; i<width;i++){
			//int heightOfCurrentColumn = 0; //this helps calculate the max height

			for(int j = 0;j<height;j++){
				if(grid[i][j]==true){
					tempColumns[i] = j+1;
					tempRows[j]++;
				}
			}			
		}
		
		columnHeights = tempColumns;
		rowWidths = tempRows;
		
	}

	/**
	 Reverts the board to its state before up to one place
	 and one clearRows();
	 If the conditions for undo() are not met, such as
	 calling undo() twice in a row, then the second undo() does nothing.
	 See the overview docs.
	*/
	public void undo() {
		if(committed) return;

		int[] temp = backupWidths;
		backupWidths = rowWidths;
		rowWidths = temp;
		
		temp = backupHeights;
		backupHeights = columnHeights;
		columnHeights = temp;
		
		boolean[][] tempGrid = backupGrid;
		backupGrid = grid;
		grid = tempGrid;
		
		recalculateMaxHeight();
 	
		commit();
		sanityCheck();
}
	
	
	/**
	 Puts the board in the committed state.
	*/
	public void commit() {
		committed = true;
	}


	
	/*
	 Renders the board state as a big String, suitable for printing.
	 This is the sort of print-obj-state utility that can help see complex
	 state change over time.
	 (provided debugging utility) 
	 */
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = height-1; y>=0; y--) {
			buff.append('|');
			for (int x=0; x<width; x++) {
				if (getGrid(x,y)) buff.append('+');
				else buff.append(' ');
			}
			buff.append("|\n");
		}
		for (int x=0; x<width+2; x++) buff.append('-');
		return(buff.toString());
	}
	
}


