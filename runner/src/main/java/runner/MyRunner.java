package runner;

import interpreter.IterableInterpreter;
import output.OutputResult;

import java.io.InputStream;
import java.util.List;

public class MyRunner {
    public static void execute(InputStream code, String version, OutputResult printLog, OutputResult errorLog) {
        IterableInterpreter iterableInterpreter = new IterableInterpreter("1.0", code);

        try {
            List<String> nextPrints;
            while(iterableInterpreter.hasNext()) {
                nextPrints = iterableInterpreter.next();
                for (String print : nextPrints) {
                    printLog.saveResult(print);
                }
            }
        } catch (Throwable e) {
            iterableInterpreter = null;
            System.gc();
            System.out.println(e.getMessage());
            errorLog.saveResult(e.getMessage());
        }
    }
}
