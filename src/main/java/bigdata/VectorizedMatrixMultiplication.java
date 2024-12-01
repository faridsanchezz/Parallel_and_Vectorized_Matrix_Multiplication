package bigdata;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class VectorizedMatrixMultiplication {

	public static int[][] execute(int[][] matrixA, int[][] matrixB, int numThreads) {

		System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", String.valueOf(numThreads));

		int rowsA = matrixA.length;
		int colsB = matrixB[0].length;

		int[][] result = new int[rowsA][colsB];

		List<int[]> precomputedColumns = precomputeColumns(matrixB);

		IntStream.range(0, rowsA).parallel().forEach(i -> {
			for (int j = 0; j < colsB; j++) {
				result[i][j] = vectorizedDotProduct(matrixA[i], precomputedColumns.get(j));
			}
		});

		return result;
	}

	private static List<int[]> precomputeColumns(int[][] matrix) {

		List<int[]> columns = new ArrayList<>();
		for (int j = 0; j < matrix[0].length; j++) {
			columns.add(getColumn(matrix, j));
		}
		return columns;
	}

	private static int vectorizedDotProduct(int[] row, int[] column) {

		int sum = 0;
		for (int k = 0; k < row.length; k++) {
			sum += row[k] * column[k];
		}
		return sum;
	}

	private static int[] getColumn(int[][] matrix, int colIndex) {

		int[] column = new int[matrix.length];
		for (int i = 0; i < matrix.length; i++) {
			column[i] = matrix[i][colIndex];
		}
		return column;
	}
}
