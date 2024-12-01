package com.alexovidiupopa.jmh;

import org.openjdk.jmh.annotations.*;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime) // Measure average time per operation
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class SortingBenchmark {

    @Param({"1000", "10000", "100000"}) // Array sizes to benchmark
    private int arraySize;

    private int[] data;

    @Setup(Level.Trial)
    public void setUp() {
        data = new int[arraySize];
        Random random = new Random();
        for (int i = 0; i < arraySize; i++) {
            data[i] = random.nextInt();
        }
    }

    @Benchmark
    @Fork(value = 2, warmups = 1) // Run 2 forks with 1 warmup iteration each
    @Warmup(iterations = 2, time = 500, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(iterations = 3, time = 500, timeUnit = TimeUnit.MILLISECONDS)
    public int[] benchmarkStandardSort() {
        int[] copy = Arrays.copyOf(data, data.length); // Prevent sorting original array
        Arrays.sort(copy);
        return copy;
    }

    @Benchmark
    @Fork(value = 2, warmups = 1)
    @Warmup(iterations = 2, time = 500, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(iterations = 3, time = 500, timeUnit = TimeUnit.MILLISECONDS)
    public int[] benchmarkBubbleSort() {
        int[] copy = Arrays.copyOf(data, data.length);
        bubbleSort(copy);
        return copy;
    }

    // Custom bubble sort algorithm for comparison
    private void bubbleSort(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = 0; j < array.length - 1 - i; j++) {
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
    }

    // Benchmarking concurrent sorting with multiple threads
    @Benchmark
    @Fork(value = 2, warmups = 1)
    @Warmup(iterations = 2, time = 500, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(iterations = 3, time = 500, timeUnit = TimeUnit.MILLISECONDS)
    @Threads(4) // Run with 4 threads to measure concurrent performance
    public int[] benchmarkConcurrentSort() {
        int[] copy = Arrays.copyOf(data, data.length);
        Arrays.parallelSort(copy); // Using Java’s parallelSort for concurrency
        return copy;
    }

    @TearDown(Level.Invocation)
    public void tearDown() {
        // Optional: Cleanup code after each invocation if needed
    }
}