package tetris;

import static org.junit.Assert.*;
import java.util.*;

import org.junit.*;

/*
  Unit test for Piece class -- starter shell.
 */
public class PieceTest {
	// You can create data to be used in the your
	// test cases like this. For each run of a test method,
	// a new PieceTest object is created and setUp() is called
	// automatically by JUnit.
	// For example, the code below sets up some
	// pyramid and s pieces in instance variables
	// that can be used in tests.
	private Piece pyr1, pyr2, pyr3, pyr4;
	private Piece sq1, sq2, sq3, sq4;
	private Piece s, sRotated, s3, s4;
	private Piece l1, l2, l3, l4;
	private Piece el1, el2, el3, el4;
	private Piece ell1, ell2, ell3, ell4;
	private Piece es1, es2, es3, es4;

	@Before
	public void setUp() throws Exception {
		
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();
		
		sq1 = new Piece(Piece.SQUARE_STR);
		sq2 = sq1.computeNextRotation();
		sq3 = sq2.computeNextRotation();
		sq4 = sq3.computeNextRotation();

		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();
		s3 = sRotated.computeNextRotation();
		s4 = s3.computeNextRotation();
		
		l1 = new Piece(Piece.STICK_STR);
		l2 = l1.computeNextRotation();
		l3 = l2.computeNextRotation();
		l4 = l3.computeNextRotation();
		
		el1 = new Piece(Piece.L1_STR);
		el2 = el1.computeNextRotation();
		el3 = el2.computeNextRotation();
		el4 = el3.computeNextRotation();
		
		ell1 = new Piece(Piece.L2_STR);
		ell2 = ell1.computeNextRotation();
		ell3 = ell2.computeNextRotation();
		ell4 = ell3.computeNextRotation();
		
		es1 = new Piece(Piece.S2_STR);
		es2 = es1.computeNextRotation();
		es3 = es2.computeNextRotation();
		es4 = es3.computeNextRotation();
	}
	
	// Here are some sample tests to get you started
	
	@Test
	public void testSampleSize() {
		// Check size of pyr piece
		assertEquals(3, pyr1.getWidth());
		assertEquals(2, pyr1.getHeight());
		
		// Now try after rotation
		// Effectively we're testing size and rotation code here
		assertEquals(2, pyr2.getWidth());
		assertEquals(3, pyr2.getHeight());
		
		assertEquals(3, pyr3.getWidth());
		assertEquals(2, pyr3.getHeight());
		
		
		assertEquals(2, pyr4.getWidth());
		assertEquals(3, pyr4.getHeight());
		
		//square
		
		assertEquals(2, sq1.getWidth());
		assertEquals(2, sq1.getHeight());
		
		assertEquals(2, sq2.getWidth());
		assertEquals(2, sq2.getHeight());
		
		assertEquals(2, sq3.getWidth());
		assertEquals(2, sq3.getHeight());
		
		assertEquals(2, sq4.getWidth());
		assertEquals(2, sq4.getHeight());
		
		//S
		assertEquals(3, s.getWidth());
		assertEquals(2, s.getHeight());
		
		assertEquals(2, sRotated.getWidth());
		assertEquals(3, sRotated.getHeight());
		
		assertEquals(3, s3.getWidth());
		assertEquals(2, s3.getHeight());
		
		assertEquals(2, s4.getWidth());
		assertEquals(3, s4.getHeight());
		
		// Now try with some other piece, made a different way
		assertEquals(1, l1.getWidth());
		assertEquals(4, l1.getHeight());
		
		assertEquals(4, l2.getWidth());
		assertEquals(1, l2.getHeight());
		
		assertEquals(1, l3.getWidth());
		assertEquals(4, l3.getHeight());
		
		assertEquals(4, l4.getWidth());
		assertEquals(1, l4.getHeight());
		
		//el
		assertEquals(2, el1.getWidth());
		assertEquals(3, el1.getHeight());
		
		assertEquals(3, el2.getWidth());
		assertEquals(2, el2.getHeight());
		
		assertEquals(2, el3.getWidth());
		assertEquals(3, el3.getHeight());
		
		assertEquals(3, el4.getWidth());
		assertEquals(2, el4.getHeight());
		
		//ell
		assertEquals(2, ell1.getWidth());
		assertEquals(3, ell1.getHeight());
		
		assertEquals(3, ell2.getWidth());
		assertEquals(2, ell2.getHeight());
		
		assertEquals(2, ell3.getWidth());
		assertEquals(3, ell3.getHeight());
		
		assertEquals(3, ell4.getWidth());
		assertEquals(2, ell4.getHeight());
		
		//es
		assertEquals(3, es1.getWidth());
		assertEquals(2, es1.getHeight());
		
		assertEquals(2, es2.getWidth());
		assertEquals(3, es2.getHeight());
		
		assertEquals(3, es3.getWidth());
		assertEquals(2, es3.getHeight());
		
		assertEquals(2, es4.getWidth());
		assertEquals(3, es4.getHeight());

	}
	
	
	// Test the skirt returned by a few pieces
	@Test
	public void testSampleSkirt() {
		// Note must use assertTrue(Arrays.equals(... as plain .equals does not work
		// right for arrays.
		assertTrue(Arrays.equals(new int[] {0, 0, 0}, pyr1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0}, pyr2.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0, 1}, pyr3.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0,1}, pyr4.getSkirt()));
		
		assertTrue(Arrays.equals(new int[] {0,0}, sq1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0,0}, sq2.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0,0}, sq3.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0,0}, sq4.getSkirt()));

		assertTrue(Arrays.equals(new int[] {0, 0, 1}, s.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0}, sRotated.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 0, 1}, s3.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0}, s4.getSkirt()));
		
		assertTrue(Arrays.equals(new int[] {0}, l1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0,0, 0,0}, l2.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0}, l3.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0,0,0,0}, l4.getSkirt()));

		assertTrue(Arrays.equals(new int[] {0,0}, el1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0,0, 0}, el2.getSkirt()));
		assertTrue(Arrays.equals(new int[] {2,0}, el3.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0,1,1}, el4.getSkirt()));
		
		assertTrue(Arrays.equals(new int[] {0,0}, ell1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1,1,0}, ell2.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0,2}, ell3.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0,0,0}, ell4.getSkirt()));

		assertTrue(Arrays.equals(new int[] {1,0,0}, es1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0,1}, es2.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1,0,0}, es3.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0,1}, es4.getSkirt()));



	}
	
	
}
