package bigdata;

import java.util.stream.IntStream;

public class MatrixMultiplicationParallelStreams {

	public static int[][] execute(int[][] matrixA, int[][] matrixB, int numThreads) {

		System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", String.valueOf(numThreads));

		int size = matrixA.length;
		int[][] resultMatrix = new int[size][matrixB[0].length];

		IntStream.range(0, size).parallel().forEach(i -> {
			for (int j = 0; j < matrixB[0].length; j++) {
				for (int k = 0; k < matrixA[0].length; k++) {
					resultMatrix[i][j] += matrixA[i][k] * matrixB[k][j];
				}
			}
		});

		return resultMatrix;
	}
}
