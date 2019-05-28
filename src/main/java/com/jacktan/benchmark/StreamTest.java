package com.jacktan.benchmark;

import com.jacktan.benchmark.dto.Result;
import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@State(Scope.Thread)
public class StreamTest {
    private static final int MAX_SIZE = 10_000_000;

    private List<Result> results;

    @Setup(Level.Invocation)
    public void init() {
        StringBuilder bs = new StringBuilder();
        bs.setLength(0);
        results = new ArrayList<>();
        for (int i = 0; i < MAX_SIZE; i++) {
            results.add(new Result("Name_" + i, i));
        }
    }

   @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public Map<String, Result> add_trandtionalWay() {
        Map<String, Result> resultSimpleOldForLoop = new HashMap<>();
        for (int i = 0; i < results.size(); i++) {
            Result result = results.get(i);
            String name = result.getName();
            if (name != null && name.contains("Name_99")) {
                resultSimpleOldForLoop.put(name, result);
            }
        }
        return resultSimpleOldForLoop;

    }

   @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public Map<String, Result> add_withFilter() {
        return results.stream()
                .filter(result -> (result.getName() != null && result.getName().contains("Name_99")))
                .collect(Collectors.toMap(Result::getName, result -> result));
    }

    // @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(value = 20)
    public Map<String, Result> add_withFilter_parallel() {
        return results.parallelStream()
                .filter(result -> (result.getName() != null && result.getName().contains("Name_99")))
                .collect(Collectors.toMap(Result::getName, result -> result));
    }

    //@Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(value = 20)
    public Map<String, Result> add_withMergeFilter() {
        return results.stream()
                .filter(result -> result.getName() != null)
                .filter(result -> result.getName().contains("Name_99"))
                .collect(Collectors.toMap(Result::getName, result -> result));
    }

    @TearDown
    public void tearDown(){
        results = null;
    }

}
