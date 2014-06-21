// Piece.java
package tetris;

import java.util.*;

/**
 An immutable representation of a tetris piece in a particular rotation.
 Each piece is defined by the blocks that make up its body.
 
 Typical client code looks like...
 <pre>
 Piece pyra = new Piece(PYRAMID_STR);		// Create piece from string
 int width = pyra.getWidth();			// 3
 Piece pyra2 = pyramid.computeNextRotation(); // get rotation, slow way
 
 Piece[] pieces = Piece.getPieces();	// the array of root pieces
 Piece stick = pieces[STICK];
 int width = stick.getWidth();		// get its width
 Piece stick2 = stick.fastRotation();	// get the next rotation, fast way
 </pre>
*/
public class Piece {
	// Starter code specs out a few basic things, leaving
	// the algorithms to be done.
	private TPoint[] body;
	private int[] skirt;		//the skirt is as many elements as the piece is wide
								//that is, the skirt stores the lowest y-value that 
								//appears in the body for each x value in the piece.
								//the x values are the index into the array.
	private int width;
	private int height;
	private Piece next; // "next" rotation

	static private Piece[] pieces;	// singleton static array of first rotations

	/**
	 Defines a new piece given a TPoint[] array of its body.
	 Makes its own copy of the array and the TPoints inside it.
	*/
	public Piece(TPoint[] points) {
		body = points;
		
		int heightFinder =0;
		int widthFinder = 0;
		next = null;
		
		
		//king of the hill algorithm to find height and width of this piece
		for(TPoint t: body){
			if(t.y>heightFinder) heightFinder = t.y;
			if(t.x>widthFinder)  widthFinder = t.x;
		}
		
		//add one because of zero-based arrays.
		this.width = widthFinder+1;
		this.height = heightFinder+1;
		
		
		//again, basic king-of-the-hill algorithm to find skirt.
		skirt = new int[width];
		for(int i=0;i<skirt.length;i++){
			skirt[i] = height+1;
		}

		for(TPoint t: body){
			if(t.y<skirt[t.x]) skirt[t.x] = t.y;
		}
		
 	}
	

	
	
	/**
	 * Alternate constructor, takes a String with the x,y body points
	 * all separated by spaces, such as "0 0  1 0  2 0	1 1".
	 * (provided)
	 */
	public Piece(String points) {
		this(parsePoints(points));
	}

	/**
	 Returns the width of the piece measured in blocks.
	*/
	public int getWidth() {
		return width;
	}

	/**
	 Returns the height of the piece measured in blocks.
	*/
	public int getHeight() {
		return height;
	}

	/**
	 Returns a pointer to the piece's body. The caller
	 should not modify this array.
	*/
	public TPoint[] getBody() {
		return body;
	}

	/**
	 Returns a pointer to the piece's skirt. For each x value
	 across the piece, the skirt gives the lowest y value in the body.
	 This is useful for computing where the piece will land.
	 The caller should not modify this array.
	*/
	public int[] getSkirt() {
		return skirt;
	}

	
	/**
	 Returns a *new* piece that is 90 degrees counter-clockwise
	 rotated from the receiver.
	 */
	public Piece computeNextRotation() {
		TPoint[] newPoint = new TPoint[body.length];
		
		//turn piece's coordinates into an nxn matrix (where n is the larger of width or height)
		int size;
		if(width>height) size = width;
		else size = height;
		
		boolean[][] pointMatrix = new boolean[size][size];
		
		for(TPoint t: body){
			pointMatrix[t.y][t.x] =true;
		}
		
		//rotate the matrix counterclockwise
		boolean[][] rotatedMatrix = rotateMatrixCounterClockwise(pointMatrix);
		
		//convert the matrix back into a TPoint array
		int counter = 0;
		for(int i =0; i<rotatedMatrix.length;i++){
			for(int j =0; j<rotatedMatrix[i].length;j++){
				if(rotatedMatrix[i][j]==true) {
					newPoint[counter] = new TPoint(j,i); 
					counter++;
				}
			}
		}
		
		//return the piece
		return new Piece(newPoint); 
	}
	
	/*
	 * Returns the integer multidimensional array rotated counterclockwise by 90 degree. 
	 * It only works for a square matrix.
	 */
	private static boolean[][] rotateMatrixCounterClockwise(boolean[][] oldArray){
		
		boolean[][] newArray = new boolean[oldArray.length][oldArray[0].length];
		
		//transpose the matrix
		for(int i = 0; i<oldArray.length;i++){
			for(int j =0; j<oldArray[i].length;j++){
				newArray[i][j] = oldArray[j][i];
			}
		}
		
		//reverse the rows of the matrix (change to columns?)
		for(int j = 0;j<oldArray[0].length;j++){
		for(int i =0 ; i<oldArray.length;i++){
			oldArray[i][oldArray.length-1-j] = newArray[i][j];
		}
		}
		
		//make sure the piece is in the bottom left hand corner of the array
		dropDown(oldArray);
		
		return oldArray;
	}
	
	private static void dropDown(boolean[][] array){
		for(int i = 0; i<array.length;i++){
			if(array[i][0]==true) return;
		}
		
		for(int i =0; i<array.length; i++){
			for(int j =0; j<array[0].length;j++){
				if(j<(array[0].length-1)){
					array[i][j] = array[i][j+1];
				}
				else{
					array[i][j] =false;
				}
			}
		}
		for(int j =0; j<array[0].length;j++){
				for(int i = 0; i<array.length;i++){
			}
		}
		dropDown(array);
		
	}
	
	/*
	 * Unit Testing
	 */
	
	
	public static void main(String[] args){
		
		boolean[][] array = new boolean[][]{
			{true, false, false},
			{true, true, false},
			{true,false,false}
		};
		
			for(int j =0; j<array[0].length;j++){
				for(int i = 0; i<array.length;i++){

				System.out.print(array[i][j] + " ");
				//System.out.print(new TPoint(j,i).toString());
			}
			System.out.println();
		}
		System.out.println();

		
		boolean[][] oldArray = rotateMatrixCounterClockwise(array);
			for(int j =0; j<oldArray[0].length;j++){
				for(int i = 0; i<oldArray.length;i++){
				System.out.print(oldArray[i][j] + " ");
			}
			System.out.println();
		}
			
			System.out.println();

			
			oldArray = rotateMatrixCounterClockwise(array);
				for(int j =0; j<oldArray[0].length;j++){
					for(int i = 0; i<oldArray.length;i++){
					System.out.print(oldArray[i][j] + " ");
				}
				System.out.println();
			}
				System.out.println();

				oldArray = rotateMatrixCounterClockwise(array);
				for(int j =0; j<oldArray[0].length;j++){
					for(int i = 0; i<oldArray.length;i++){
					System.out.print(oldArray[i][j] + " ");
				}
				System.out.println();
			}
		
		
	}


	/**
	 Returns a pre-computed piece that is 90 degrees counter-clockwise
	 rotated from the receiver.	 Fast because the piece is pre-computed.
	 This only works on pieces set up by makeFastRotations(), and otherwise
	 just returns null.
	*/	
	public Piece fastRotation() {
		return next;
	}
	


	/**
	 Returns true if two pieces are the same --
	 their bodies contain the same points.
	 Interestingly, this is not the same as having exactly the
	 same body arrays, since the points may not be
	 in the same order in the bodies. Used internally to detect
	 if two rotations are effectively the same.
	*/
	public boolean equals(Object obj) {
		// standard equals() technique 1
		if (obj == this) return true;
		
		// standard equals() technique 2
		// (null will be false)
		if (!(obj instanceof Piece)) return false;
		Piece other = (Piece)obj;
		
		TPoint[] otherArray = other.getBody();
		
		for(int i = 0; i<body.length;i++){
			if(contains(otherArray, body[i])==false) return false;
		}
		
		return true;
	}
	
	private boolean contains(TPoint[] array, TPoint point){
		for(int i = 0; i<array.length;i++){
			if(array[i].x==point.x&&array[i].y==point.y) return true;
		}
		return false;
	}


	// String constants for the standard 7 tetris pieces
	public static final String STICK_STR	= "0 0	0 1	 0 2  0 3";
	public static final String L1_STR		= "0 0	0 1	 0 2  1 0";
	public static final String L2_STR		= "0 0	1 0 1 1	 1 2";
	public static final String S1_STR		= "0 0	1 0	 1 1  2 1";
	public static final String S2_STR		= "0 1	1 1  1 0  2 0";
	public static final String SQUARE_STR	= "0 0  0 1  1 0  1 1";
	public static final String PYRAMID_STR	= "0 0  1 0  1 1  2 0";
	
	// Indexes for the standard 7 pieces in the pieces array
	public static final int STICK = 0;
	public static final int L1	  = 1;
	public static final int L2	  = 2;
	public static final int S1	  = 3;
	public static final int S2	  = 4;
	public static final int SQUARE	= 5;
	public static final int PYRAMID = 6;
	
	/**
	 Returns an array containing the first rotation of
	 each of the 7 standard tetris pieces in the order
	 STICK, L1, L2, S1, S2, SQUARE, PYRAMID.
	 The next (counterclockwise) rotation can be obtained
	 from each piece with the {@link #fastRotation()} message.
	 In this way, the client can iterate through all the rotations
	 until eventually getting back to the first rotation.
	 (provided code)
	*/
	public static Piece[] getPieces() {
		// lazy evaluation -- create static array if needed
		if (Piece.pieces==null) {
			// use makeFastRotations() to compute all the rotations for each piece
			Piece.pieces = new Piece[] {
				makeFastRotations(new Piece(STICK_STR)),
				makeFastRotations(new Piece(L1_STR)),
				makeFastRotations(new Piece(L2_STR)),
				makeFastRotations(new Piece(S1_STR)),
				makeFastRotations(new Piece(S2_STR)),
				makeFastRotations(new Piece(SQUARE_STR)),
				makeFastRotations(new Piece(PYRAMID_STR)),
			};
		}
		
		
		return Piece.pieces;
	}
	


	/**
	 Given the "first" root rotation of a piece, computes all
	 the other rotations and links them all together
	 in a circular list. The list loops back to the root as soon
	 as possible. Returns the root piece. fastRotation() relies on the
	 pointer structure setup here.
	*/
	/*
	 Implementation: uses computeNextRotation()
	 and Piece.equals() to detect when the rotations have gotten us back
	 to the first piece.
	*/
	private static Piece makeFastRotations(Piece root) {
		Piece rotation = root.computeNextRotation();
		root.next = rotation;
		while(true){
			if(rotation.computeNextRotation().equals(root)) {
				rotation.next = root; 
				break;
			}
			rotation.next = rotation.computeNextRotation();
			rotation = rotation.next;
		}
		
		return root;
		
	}
	
	

	/**
	 Given a string of x,y pairs ("0 0	0 1 0 2 1 0"), parses
	 the points into a TPoint[] array.
	 (Provided code)
	*/
	private static TPoint[] parsePoints(String string) {
		List<TPoint> points = new ArrayList<TPoint>();
		StringTokenizer tok = new StringTokenizer(string);
		try {
			while(tok.hasMoreTokens()) {
				int x = Integer.parseInt(tok.nextToken());
				int y = Integer.parseInt(tok.nextToken());
				
				points.add(new TPoint(x, y));
			}
		}
		catch (NumberFormatException e) {
			throw new RuntimeException("Could not parse x,y string:" + string);
		}
		
		// Make an array out of the collection
		TPoint[] array = points.toArray(new TPoint[0]);
		return array;
	}

	


}
