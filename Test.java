public class RuleViolationsDemo {

    // =========================================================
    // 1️⃣ RULE VIOLATION: Hardcoded Credentials (CWE-798)
    // =========================================================
    public void connectToDb() {
        String username = "admin";
        String password = "SuperSecret123";  // ❌ Hardcoded password

        System.out.println("Connecting with " + username + ":" + password);
    }

    // =========================================================
    // 2️⃣ RULE VIOLATION: SQL Injection (CWE-89)
    // =========================================================
    public void fetchUser(String userId) {
        String query = "SELECT * FROM users WHERE id = " + userId;  // ❌ SQL Injection

        System.out.println("Query: " + query);
    }

    // =========================================================
    // 3️⃣ RULE VIOLATION: Directory Traversal (CWE-22)
    // =========================================================
    public void readFile(String filename) {
        String path = "C:/app/data/" + filename;  // ❌ User-controlled path

        System.out.println("Reading file: " + path);
    }

    // =========================================================
    // 4️⃣ RULE VIOLATION: Command Injection (CWE-78)
    // =========================================================
    public void pingHost(String host) throws Exception {
        String cmd = "ping " + host;  // ❌ Unsafe concatenation for system command

        Runtime.getRuntime().exec(cmd);  // ❌ Command Injection
    }

    // =========================================================
    // 5️⃣ RULE VIOLATION: Ignoring Exceptions
    // =========================================================
    public int divide(int a, int b) {
        try {
            return a / b;
        } catch (Exception e) {
            // ❌ Poor practice: hides real problem
            return 0;
        }
    }

    // =========================================================
    // 6️⃣ RULE VIOLATION: Single Responsibility Principle (SRP)
    // =========================================================
    public void registerUser(String username, String password) {

        // Responsibility 1 → Validate user
        if (username.isEmpty()) {
            System.out.println("Invalid username");
        }

        // Responsibility 2 → Logging
        System.out.println("Logging registration attempt…");

        // Responsibility 3 → Database save
        System.out.println("Saving user to database…");

        // Responsibility 4 → Security check
        if (password.length() < 3) {
            System.out.println("Weak password");
        }

        // Responsibility 5 → Notification
        System.out.println("Sending welcome email…");
    }

    // =========================================================
    // MAIN METHOD (For Demo)
    // =========================================================
    public static void main(String[] args) {
        try {
            RuleViolationsDemo demo = new RuleViolationsDemo();

            demo.connectToDb();
            demo.fetchUser("1 OR 1=1");                // SQL Injection example
            demo.readFile("../../secret.txt");         // Directory Traversal
            demo.pingHost("8.8.8.8 && rm -rf /");      // Command Injection
            demo.divide(10, 0);                        // Exception hidden
            demo.registerUser("", "12");               // SRP violation

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
