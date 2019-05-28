package com.jacktan.benchmark;


import com.jacktan.benchmark.dto.Result;
import org.openjdk.jmh.annotations.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class StringBuilderTest {

    private static final int MAX = 5_000;
    private static final int MIN = 1_000;

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public String stringBuilder_resue() {
        String result = "";
        StringBuilder abc = new StringBuilder();
        for (int i = 0; i < MIN; i++) {
            abc.setLength(0);
            for (int j = 0; j < MAX; j++) {
                abc.append(j);
            }
            result += abc.toString();
        }

        return result;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public String stringBuilder_notresue() {
        String result = "";
        for (int i = 0; i < MIN; i++) {
            StringBuilder abc = new StringBuilder();
            for (int j = 0; j < MAX; j++) {
                abc.append(j);
            }
            result += abc.toString();
        }
        return result;
    }
}
