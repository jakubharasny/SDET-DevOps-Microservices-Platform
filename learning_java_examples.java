// Java quick reference aligned with learning_ruby_examples.rb

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class learning_java_examples {
    // 01) VARIABLES + CONSTANTS
    static final int NOTES_LIMIT = 100;                  // constant

    public static void main(String[] args) throws IOException {
        String name = "Jakub";                           // mutable local variable
        int age = 29;                                    // integer
        double price = 19.99;                            // float/double
        boolean active = true;                           // boolean

        // 02) STRINGS
        String full = name + " is " + age;               // concatenation
        String caps = full.toUpperCase();                // string method
        String trimmed = "  hi  ".trim();                // trim spaces

        // 03) ARRAYS/LISTS
        List<Integer> nums = Arrays.asList(1, 2, 3, 4);  // list literal
        int first = nums.get(0);                         // first element
        List<Integer> evens = nums.stream().filter(n -> n % 2 == 0).collect(Collectors.toList()); // filter

        // 04) MAPS
        Map<String, Object> user = Map.of("id", 1, "role", "admin", "active", active); // map literal
        String role = (String) user.get("role");         // read value
        boolean userActive = (Boolean) user.get("active"); // typed cast

        // 05) CONDITIONALS
        String label = age >= 18 ? "adult" : "minor";    // ternary
        String status;
        if (active && age > 20) {                        // if with boolean logic
            status = "allowed";
        } else {
            status = "blocked";
        }

        // 06) SWITCH
        String access = switch (role) {                  // switch expression
            case "admin" -> "all";
            case "user" -> "limited";
            default -> "none";
        };

        // 07) LOOPS
        int sum = 0;
        for (int n : nums) {                             // enhanced for loop
            sum += n;
        }
        for (int n : nums) {                             // break in loop
            if (n > 3) {
                break;
            }
        }

        // 08) RANGES (SIMULATED)
        List<Integer> range = java.util.stream.IntStream.rangeClosed(1, 5).boxed().toList(); // inclusive range
        List<String> letters = List.of("a", "b", "c");   // range-like values

        // 09) METHODS
        String message = greet(name);                    // call method

        // 10) DEFAULT-LIKE + NAMED-STYLE VIA OVERLOAD
        String tag = buildTag("dev", "test");           // explicit params

        // 11) VARARGS ARGUMENTS
        int totalSum = total(1, 2, 3, 4);               // variable arg count

        // 12) LAMBDAS + FUNCTIONAL INTERFACES
        TimedResult<Integer> timed = withTiming(() -> doubleValue(10)); // execute lambda
        java.util.function.Function<Integer, Integer> triple = x -> x * 3; // function object

        // 13) CLASSES + OBJECTS
        Person person = new Person("Mia");

        // 14) INHERITANCE
        Admin admin = new Admin("Root");

        // 15) INTERFACES (MIXIN-LIKE DEFAULT METHODS)
        Worker worker = new Worker();
        String workerLog = worker.log("job started");

        // 16) VISIBILITY
        Vault vault = new Vault();
        String secret = vault.expose();

        // 17) EXCEPTIONS
        String handled;
        try {
            Integer.parseInt("oops");                    // throws NumberFormatException
            handled = "ok";
        } catch (NumberFormatException e) {
            handled = e.getMessage();
        } finally {
            boolean finalized = true;                    // always runs
        }

        // 18) FILE I/O
        Path path = Path.of("tmp_java_example.txt");
        Files.writeString(path, "hello\n");             // write text file
        String content = Files.readString(path);         // read text file
        Files.deleteIfExists(path);

        // 19) REGEX
        boolean hasDigits = "abc123".matches(".*\\d+.*"); // regex match
        String replaced = "2026-05-04".replaceFirst("\\d{4}", "YEAR");

        // 20) STREAM CHAINING
        List<Integer> squares = nums.stream().map(n -> n * n).collect(Collectors.toList()); // transform
        List<Integer> small = squares.stream().filter(n -> n <= 10).collect(Collectors.toList()); // reject inverse
        String joined = small.stream().map(String::valueOf).collect(Collectors.joining(","));

        // 21) NULL-SAFE HELPERS
        String nickname = null;
        Integer safeLength = Optional.ofNullable(nickname).map(String::length).orElse(null); // null-safe map
        String fallback = nickname != null ? nickname : "anonymous"; // default fallback

        // 22) TYPES + CONVERSIONS
        String symText = "status";                       // symbol-like identifier
        String numText = String.valueOf(42);             // number to string
        int toNum = Integer.parseInt("42");              // string to number
        LocalDateTime now = LocalDateTime.now();         // date-time type

        // 23) ENTRY OUTPUT
        System.out.println(message);
        System.out.println("tag=" + tag + " role=" + access + " count=" + Person.count());
        System.out.println("worker=" + workerLog + " secret=" + secret + " content=" + content.strip());
    }

    static String greet(String person) {                 // method definition
        return "Hello " + person;
    }

    static String buildTag(String prefix, String env) {  // simple helper
        return prefix + "-" + env;
    }

    static int total(int... values) {                    // varargs
        return Arrays.stream(values).sum();
    }

    static int doubleValue(int x) {                      // tiny helper
        return x * 2;
    }

    static <T> TimedResult<T> withTiming(ThrowingSupplier<T> work) {
        long start = System.nanoTime();
        try {
            T result = work.get();
            return new TimedResult<>(result, System.nanoTime() - start);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    interface ThrowingSupplier<T> {
        T get() throws Exception;
    }

    record TimedResult<T>(T value, long nanos) {}

    static class Person {
        private static int count = 0;                    // class variable
        private String name;                             // instance variable
        private final LocalDateTime createdAt;

        Person(String name) {
            this.name = name;
            this.createdAt = LocalDateTime.now();
            count++;
        }

        static int count() {                             // class method
            return count;
        }

        void rename(String newName) {
            this.name = newName;
        }
    }

    static class Admin extends Person {
        Admin(String name) {
            super(name);
        }

        String role() {
            return "admin";
        }
    }

    interface Loggable {
        default String log(String msg) {
            return "[LOG] " + msg;
        }
    }

    static class Worker implements Loggable {}

    static class Vault {
        String expose() {
            return secretCode();                         // private method call
        }

        private String secretCode() {
            return "42";
        }
    }
}
