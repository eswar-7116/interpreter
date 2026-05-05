package benchmark;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class BenchmarkGenerator {
    static void main() {
        try (PrintWriter out = new PrintWriter(new FileWriter("src/main/resources/benchmark/benchmark.flux"))) {
            out.println("let total = 0");
            for (int i = 0; i < 9998; i++) {
                if (i % 10 == 0) {
                    // Static math for optimizer
                    out.println("let x" + i + " = 5 + 5 * 2 + 10 / 2 - 3");
                    out.println("total = total + x" + i);
                } else if (i % 3 == 0) {
                    // Constant propagation opportunity
                    out.println("let y" + i + " = 10");
                    out.println("total = total + y" + i + " * 0 + " + i);
                } else {
                    // Complex arithmetic
                    out.println("total = total + (" + i + " + 20) * 5 / 2");
                }
            }
            out.println("print total");
        } catch (IOException e) {
            IO.println(e);
        }
    }
}
