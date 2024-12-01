package bigdata;

import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.AverageTime})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.MILLISECONDS)
@Fork(1)

public class benchmarking {

	@State(Scope.Benchmark)
	public static class BenchmarkState {

		private ArrayList<Double> memoryUsageList;
		private int[][] matrixA;
		private int[][] matrixB;
		private long initialMemory;
		@Param({"100", "500", "700", "1000"})
		private int size;

		@Param({"2", "3", "4", "6", "8"})
		private int numThread;

		public static int[][] generateMatrix(int size) {
			int[][] matrix = new int[size][size];
			Random random = new Random();

			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					matrix[i][j] = random.nextInt(1000);
				}
			}

			return matrix;
		}

		@Setup(Level.Trial)
		public void setupTrial() {
			matrixA = generateMatrix(size);
			matrixB = generateMatrix(size);
			memoryUsageList = new ArrayList<>();
		}

		@Setup(Level.Iteration)
		public void setupIteration() {
			Runtime runtime = Runtime.getRuntime();
			runtime.gc();
			initialMemory = runtime.totalMemory() - runtime.freeMemory();
		}

		@TearDown(Level.Iteration)
		public void tearDownIteration() {
			Runtime runtime = Runtime.getRuntime();
			long finalMemory = runtime.totalMemory() - runtime.freeMemory();
			long memoryUsed = (finalMemory - initialMemory);
			memoryUsageList.add(memoryUsed / 1024.0);
			//System.out.println("\nCurrent memory usage: " + (memoryUsed / 1024.0) + " KB");
		}

		@TearDown(Level.Trial)
		public void tearDownTrial() {
			ArrayList<Double> subList = new ArrayList<>(memoryUsageList.subList(3, memoryUsageList.size()));
			double averageMemoryUsage = subList.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
			System.out.println("Average memory usage of the last 10 iterations: " + averageMemoryUsage + " KB");
		}
	}

	@Benchmark
	public int[][] FixedThreadsMultiplication(BenchmarkState state) {
		return MatrixMultiplicationFixedThreads.execute(state.matrixA, state.matrixB, state.numThread);
	}


	@Benchmark
	public int[][] ParallelStreamsMultiplication(BenchmarkState state) {
		return MatrixMultiplicationParallelStreams.execute(state.matrixA, state.matrixB, state.numThread);
	}


	@Benchmark
	public int[][] BaseMatrixMultiplication(BenchmarkState state) {
		return BaseMatrixMultiplication.execute(state.matrixA, state.matrixB);
	}


	@Benchmark
	public int[][] VectorizedMatrixMultiplication(BenchmarkState state) {
		return VectorizedMatrixMultiplication.execute(state.matrixA, state.matrixB, state.numThread);
	}
}
