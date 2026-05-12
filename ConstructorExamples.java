/*
 * ConstructorExamples.java
 *
 * This is a local learning file.
 * It is intentionally verbose and heavily commented.
 *
 * HOW TO RUN (from project root):
 *   javac ConstructorExamples.java && java ConstructorExamples
 */
public class ConstructorExamples {

	/*
	 * =========================
	 * 1) NO-ARGS CONSTRUCTOR
	 * =========================
	 *
	 * If you create NO constructor yourself, Java auto-generates a default no-args
	 * constructor.
	 *
	 * But if you create ANY constructor manually, Java will NOT auto-generate it.
	 * Then, if you still want no-args behavior, you must write it explicitly.
	 */
	static class User {
		String id;
		String name;

		// Explicit no-args constructor.
		// Good when you want your own defaults.
		User() {
			this.id = "unknown";
			this.name = "anonymous";
		}
	}

	/*
	 * ======================================
	 * 2) OVERLOADED CONSTRUCTORS (MULTIPLE)
	 * ======================================
	 *
	 * Same class, different constructor signatures.
	 * This is overload, not override.
	 */
	static class Job {
		String title;
		int priority;

		Job() {
			this.title = "default-job";
			this.priority = 1;
		}

		Job(String title) {
			this.title = title;
			this.priority = 1;
		}

		Job(String title, int priority) {
			this.title = title;
			this.priority = priority;
		}
	}

	/*
	 * ====================================
	 * 3) CONSTRUCTOR CHAINING WITH this()
	 * ====================================
	 *
	 * Instead of duplicating logic, call one constructor from another.
	 * Rule: this(...) must be the FIRST line in constructor body.
	 */
	static class DatabaseConfig {
		String host;
		int port;
		String schema;

		DatabaseConfig() {
			// Delegates to 3-args constructor.
			this("localhost", 3306, "sdet_dev");
		}

		DatabaseConfig(String host) {
			this(host, 3306, "sdet_dev");
		}

		DatabaseConfig(String host, int port, String schema) {
			this.host = host;
			this.port = port;
			this.schema = schema;
		}
	}

	/*
	 * ===================
	 * 4) COPY CONSTRUCTOR
	 * ===================
	 *
	 * Java has no built-in copy constructor keyword.
	 * You define one manually by taking same class as input.
	 */
	static class QueryPayload {
		String message;
		String source;

		QueryPayload(String message, String source) {
			this.message = message;
			this.source = source;
		}

		// Copy constructor.
		QueryPayload(QueryPayload other) {
			this.message = other.message;
			this.source = other.source;
		}
	}

	/*
	 * ============================================
	 * 5) PRIVATE CONSTRUCTOR + STATIC FACTORY API
	 * ============================================
	 *
	 * Use private constructor when you want controlled creation.
	 * Common for value objects and validation rules.
	 */
	static class TopicName {
		String value;

		private TopicName(String value) {
			this.value = value;
		}

		static TopicName of(String raw) {
			if (raw == null || raw.isBlank()) {
				throw new IllegalArgumentException("Topic cannot be blank");
			}
			return new TopicName(raw.trim());
		}
	}

	/*
	 * ==========================================
	 * 6) INHERITANCE: CONSTRUCTOR + super(...)
	 * ==========================================
	 *
	 * Parent constructor runs before child constructor.
	 * Child can call parent constructor with super(...).
	 * Rule: super(...) must be first line in child constructor.
	 */
	static class Worker {
		String workerId;

		Worker(String workerId) {
			this.workerId = workerId;
		}
	}

	static class KafkaWorker extends Worker {
		String groupId;

		KafkaWorker(String workerId, String groupId) {
			super(workerId); // initialize parent state first
			this.groupId = groupId;
		}
	}

	/*
	 * ===================================================
	 * 7) COMMON CONFUSION: "CONSTRUCTOR VS NORMAL METHOD"
	 * ===================================================
	 *
	 * Constructor:
	 * - same name as class
	 * - no return type (not even void)
	 * - runs when "new ClassName(...)" happens
	 *
	 * Normal method:
	 * - has return type (or void)
	 * - called after object exists
	 */
	static class Counter {
		int value;

		Counter(int startValue) { // constructor
			this.value = startValue;
		}

		void increment() { // normal method
			this.value++;
		}
	}

	public static void main(String[] args) {
		System.out.println("=== Constructor examples ===");

		User user = new User();
		System.out.println("User defaults -> id=" + user.id + ", name=" + user.name);

		Job jobA = new Job();
		Job jobB = new Job("nightly-sync");
		Job jobC = new Job("critical-repair", 5);
		System.out.println("Job A -> " + jobA.title + ", p=" + jobA.priority);
		System.out.println("Job B -> " + jobB.title + ", p=" + jobB.priority);
		System.out.println("Job C -> " + jobC.title + ", p=" + jobC.priority);

		DatabaseConfig cfgA = new DatabaseConfig();
		DatabaseConfig cfgB = new DatabaseConfig("mysql");
		DatabaseConfig cfgC = new DatabaseConfig("kafka-db", 5432, "platform");
		System.out.println("Cfg A -> " + cfgA.host + ":" + cfgA.port + "/" + cfgA.schema);
		System.out.println("Cfg B -> " + cfgB.host + ":" + cfgB.port + "/" + cfgB.schema);
		System.out.println("Cfg C -> " + cfgC.host + ":" + cfgC.port + "/" + cfgC.schema);

		QueryPayload original = new QueryPayload("hello kafka", "frontend");
		QueryPayload copy = new QueryPayload(original);
		System.out.println("Copy -> message=" + copy.message + ", source=" + copy.source);

		TopicName topic = TopicName.of(" query.created.v1 ");
		System.out.println("Topic factory -> " + topic.value);

		KafkaWorker worker = new KafkaWorker("worker-1", "query-worker-v1");
		System.out.println("KafkaWorker -> id=" + worker.workerId + ", group=" + worker.groupId);

		Counter counter = new Counter(10);
		counter.increment();
		System.out.println("Counter -> " + counter.value);

		System.out.println("=== End ===");
	}
}
