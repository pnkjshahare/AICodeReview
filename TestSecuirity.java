import java.io.*;
import java.sql.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Base64;

public class SecurityTest {

    // 1️⃣ Hardcoded credentials (CWE-798)
    private static final String DB_USERNAME = "admin";
    private static final String DB_PASSWORD = "123456"; // insecure hardcoded password

    // 2️⃣ SQL Injection vulnerability (CWE-89)
    public void vulnerableLogin(String username) throws Exception {
        Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/test", DB_USERNAME, DB_PASSWORD);

        String query = "SELECT * FROM users WHERE username = '" + username + "'"; // SQL injection
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
    }

    // 3️⃣ Command Injection (CWE-78)
    public void runSystemCommand(String userInput) throws Exception {
        Runtime.getRuntime().exec("ping " + userInput); // command injection
    }

    // 4️⃣ Insecure File Handling (Path Traversal - CWE-22)
    public void readFile(String filename) throws Exception {
        File file = new File("/var/data/" + filename); // potential directory traversal
        BufferedReader br = new BufferedReader(new FileReader(file));
        br.lines().forEach(System.out::println);
    }

    // 5️⃣ Weak Hashing (MD5) - CWE-327
    public String insecureHash(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5"); // weak hash
        return Base64.getEncoder().encodeToString(md.digest(input.getBytes()));
    }

    // 6️⃣ Hardcoded encryption key (CWE-321)
    public String encrypt(String text) throws Exception {
        String key = "1234567812345678"; // hardcoded key
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        return Base64.getEncoder().encodeToString(cipher.doFinal(text.getBytes()));
    }

    // 7️⃣ Insecure Random (CWE-330)
    public int insecureRandom() {
        return (int)(Math.random() * 100000); // predictable random
    }

    // 8️⃣ Logging sensitive data (CWE-532)
    public void logPassword(String password) {
        System.out.println("User password is: " + password); // leak sensitive info
    }

    public static void main(String[] args) throws Exception {
        SecurityTest st = new SecurityTest();
        st.vulnerableLogin("admin' OR '1'='1");
        st.runSystemCommand("127.0.0.1 && rm -rf /");
        st.readFile("../../etc/passwd");
        System.out.println(st.insecureHash("password"));
        st.encrypt("secret");
        st.insecureRandom();
        st.logPassword("SuperSecretPass!");
    }
}
