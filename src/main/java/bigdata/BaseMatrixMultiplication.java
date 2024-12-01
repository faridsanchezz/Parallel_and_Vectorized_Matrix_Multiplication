package bigdata;

public class BaseMatrixMultiplication {

	public static int[][] execute(int[][] a, int[][] b) {
		assert a.length == b.length;
		int n = a.length;
		int[][] c = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				for (int k = 0; k < n; k++) {
					c[i][j] += a[i][k] * b[k][j];
				}
			}
		}
		return c;
	}
}
