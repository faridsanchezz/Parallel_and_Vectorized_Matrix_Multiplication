package bigdata;

public class MatrixMultiplicationThreads {

	public static int[][] execute(int[][] matrixA, int[][] matrixB) {

		int rowsA = matrixA.length;
		int colsB = matrixB[0].length;
		int[][] result = new int[rowsA][colsB];

		Thread[] threads = new Thread[rowsA];
		for (int i = 0; i < rowsA; i++) {
			final int row = i;
			threads[i] = new Thread(() -> multiplyRow(row, matrixA, matrixB, result));
			threads[i].start();
		}

		for (int i = 0; i < rowsA; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	private static void multiplyRow(int row, int[][] matrixA, int[][] matrixB, int[][] result) {
		for (int j = 0; j < matrixB[0].length; j++) {
			for (int k = 0; k < matrixA[0].length; k++) {
				result[row][j] += matrixA[row][k] * matrixB[k][j];
			}
		}
	}
}
