package dev.jiwonlee;
import java.util.Arrays;

public class Test {
    public static void main(String[] args) throws Exception {
        ClassLoader classLoader = new CustomClassLoader(Thread.currentThread().getContextClassLoader(),
                Arrays.asList("/Users/jiwonlee/dev/java-custom-class-loader"));
        Class cls = Class.forName("dev.jiwonlee.TestClass", true, classLoader);
        cls.getMethod("test").invoke(cls.newInstance());
    }
}
