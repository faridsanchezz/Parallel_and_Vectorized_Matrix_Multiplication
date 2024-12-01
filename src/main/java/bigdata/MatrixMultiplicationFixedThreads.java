package bigdata;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MatrixMultiplicationFixedThreads {

	public static int[][] execute(int[][] matrixA, int[][] matrixB, int numThreads) {

		int[][] resultMatrix = new int[matrixA.length][matrixB[0].length];
		ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

		for (int i = 0; i < matrixA.length; i++) {
			final int row = i;
			executorService.submit(() -> multiplyRow(row, matrixA, matrixB, resultMatrix));
		}

		executorService.shutdown();

		try {
			executorService.awaitTermination(1, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return resultMatrix;
	}


	private static void multiplyRow(int row, int[][] matrixA, int[][] matrixB, int[][] resultMatrix) {
		for (int j = 0; j < matrixB[0].length; j++) {
			for (int k = 0; k < matrixA[0].length; k++) {
				resultMatrix[row][j] += matrixA[row][k] * matrixB[k][j];
			}
		}
	}
}
