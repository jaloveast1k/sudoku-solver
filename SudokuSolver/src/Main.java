import java.util.ArrayList;

public class Main {
	private static final String SPACE = " ";
	private static final String STR_NULL = "null";
	private static final int MATRIX_SIZE = 9;
	private static final int SQUARE_SIZE = 3;
	
	private static byte[][] matrix = new byte[][]{
		{8, 0, 0, 0, 0, 0, 0, 0, 0}, 
		{0, 0, 3, 6, 0, 0, 0, 0, 0},
		{0, 7, 0, 0, 9, 0, 2, 0, 0},
		{0, 5, 0, 0, 0, 7, 0, 0, 0},
		{0, 0, 0, 0, 4, 5, 7, 0, 0},
		{0, 0, 0, 1, 0, 0, 0, 3, 0},
		{0, 0, 1, 0, 0, 0, 0, 6, 8},
		{0, 0, 8, 5, 0, 0, 0, 1, 0},
		{0, 9, 0, 0, 0, 0, 4, 0, 0}};
	private static byte[][][] validDigits = new byte[MATRIX_SIZE][][];
	private static byte[][] currMatrix = new byte[MATRIX_SIZE][];
	
	public static void main(String[] args) {
		System.out.println("start at: " + System.currentTimeMillis());
		long time = System.currentTimeMillis();
		
		init();
		boolean solved = solve(0, 0);
		if (solved) {
			printM(currMatrix);
		}
		
		System.out.println("stop at: " + System.currentTimeMillis());
		System.out.println("total time spend: " + (System.currentTimeMillis() - time) / 1000 + "sec");
	}
	
	private static boolean checkRow(int row) {
		if (row < 0) {
			return true;
		}
		
		for (int i = 0; i < currMatrix[row].length; i++) {
			for (int j = i + 1; j < currMatrix[row].length; j++) {
				if (currMatrix[row][i] == currMatrix[row][j]) {
					return false;
				}
			}
		}
		return true;
	}
	
	private static boolean checkColumns() {
		for (int col = 0; col < MATRIX_SIZE; col++) {
			for (int i = 0; i < MATRIX_SIZE; i++) {
				for (int j = i + 1; j < MATRIX_SIZE; j++) {
					if (currMatrix[i][col] == currMatrix[j][col]) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	private static boolean checkSquare(int row, int col) {
		row = (row - 1) / 3 * 3;
		col = col - 3;
		if (col < 0) {
			col = 6;
		}
		
		for (int i = 0; i < MATRIX_SIZE; i++) {
			for (int j = i + 1; j < MATRIX_SIZE; j++) {
				if (currMatrix[row + i / 3][col + i % 3] == currMatrix[row + j / 3][col + j % 3]) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	public static boolean solve(int row, int col) {
		// check row
		if (col == 0) {
			if (!checkRow(row - 1)) {
				return false;
			}
		}
		
		// check square
		if (col % 3 == 0 && row % 3 == 0 && row > 0) { // col == 0, 3, 6; row == 3, 6, 9.
			if (!checkSquare(row, col)) {
				return false;
			}
		}
		
		if (row == MATRIX_SIZE) {
			boolean valid = true;
			valid = valid && checkSquare(9, 0);
			valid = valid && checkSquare(9, 3);
			valid = valid && checkSquare(9, 6);
			valid = valid && checkColumns();
			
			return valid;
		}
		
		int nextCol = col + 1;
		int nextRow = row;
		if (nextCol == MATRIX_SIZE) {
			nextCol = 0;
			nextRow++;
		}
		
		if (validDigits[row][col] == null) {
			if (solve(nextRow, nextCol)) {
				return true;
			}
		} else {
			for (int i = 0; i < validDigits[row][col].length; i++) {
				byte currValue = validDigits[row][col][i];
				currMatrix[row][col] = currValue;
				
				if (solve(nextRow, nextCol)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static void init() {
		ArrayList<Byte> digits = new ArrayList<Byte>();
		for (byte i = 1; i < MATRIX_SIZE + 1; i++) {
			digits.add(i);
		}
		
		for(int i = 0; i < validDigits.length; i++) {
			validDigits[i] = new byte[MATRIX_SIZE][];
			currMatrix[i] = new byte[MATRIX_SIZE];
			
			for(int j = 0; j < validDigits[i].length; j++) {
				if (matrix[i][j] != 0) {
					currMatrix[i][j] = matrix[i][j];
					continue;
				}
					
				ArrayList<Byte> currDigits = new ArrayList<Byte>(digits);
				
				// delete by row
				for(int k = 0; k < validDigits[i].length; k++) {
					if (matrix[i][k] != 0) {
						int id = currDigits.indexOf(matrix[i][k]);
						if (id != -1) {
							currDigits.remove(id);
						}
					}
				}
				
				// delete by column
				for(int k = 0; k < validDigits.length; k++) {
					if (matrix[k][j] != 0) {
						int id = currDigits.indexOf(matrix[k][j]);
						if (id != -1) {
							currDigits.remove(id);
						}
					}
				}
				
				// delete by square
				int startI = i / SQUARE_SIZE * SQUARE_SIZE;
				int startJ = j / SQUARE_SIZE * SQUARE_SIZE;
				
				for(int k1 = startI; k1 < startI + SQUARE_SIZE; k1++) {
					for(int k2 = startJ; k2 < startJ + SQUARE_SIZE; k2++) {
						if (matrix[k1][k2] != 0) {
							int id = currDigits.indexOf(matrix[k1][k2]);
							if (id != -1) {
								currDigits.remove(id);
							}
						}
					}
				}
				
				validDigits[i][j] = new byte[currDigits.size()];
				for(int k = 0; k < validDigits[i][j].length; k++) {
					validDigits[i][j][k] = currDigits.get(k);
				}
			}
		}
	}
	
	public static void printM(byte[][] m) {
		for(int i = 0; i < m.length; i++) {
			if (m[i] != null) {
				for(int j = 0; j < m[i].length; j++) {
					System.out.print(m[i][j] + SPACE);
				}
			} else {
				System.out.print(STR_NULL);
			}
			System.out.println();
		}
	}
}
