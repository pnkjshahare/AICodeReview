// ================================================
//   SECURITY VIOLATIONS TEST FILE (15 rules)
//   All languages embedded inside one C# file
//   Semgrep pattern-regex can detect them.
// ================================================

using System;
using System.Security.Cryptography;
using System.Text;
using System.Diagnostics;

public class SecurityViolationsTest
{
    // 1. Hardcoded credentials (Java)
    string javaCreds = @"
        public class HardcodedCreds {
            private String password = ""Admin@123"";        // ❌ hardcoded
            private String apiKey   = ""sk_test_ABC"";      // ❌ hardcoded
        }
    ";

    // 2. Insecure Random (Java)
    string insecureRandom = @"
        import java.util.Random;
        class InsecureRandom {
            void run() {
                Random r = new Random();   // ❌ insecure
                System.out.println(r.nextInt());
            }
        }
    ";

    // 3. Weak Hash MD5 (C#)
    public string WeakHash(string input)
    {
        using var md5 = MD5.Create();     // ❌ weak hash
        var bytes = md5.ComputeHash(Encoding.UTF8.GetBytes(input));
        return Convert.ToHexString(bytes);
    }

    // 4. SQL Injection (Java)
    string sqlInjection = @"
        String sql = ""SELECT * FROM users WHERE name = '"" + username + ""'""; // ❌
    ";

    // 5. Disable SSL validation (C#)
    public void DisableSSL()
    {
        System.Net.ServicePointManager.ServerCertificateValidationCallback =
            (a, b, c, d) => true;   // ❌ insecure
    }

    // 6. HTTP instead of HTTPS (JavaScript)
    string httpCall = @"
        fetch('http://example.com/api/data'); // ❌ insecure HTTP
    ";

    // 7. Sensitive Logging (Python)
    string sensitiveLogging = @"
        password = 'Admin123'
        print(f'Password: {password}')   # ❌ sensitive log
    ";

    // 8. Command Injection (Java)
    string cmdInjection = @"
        Runtime.getRuntime().exec(""sh -c "" + userInput);  // ❌ vulnerable
    ";

    // 9. CSRF disabled (Java - Spring)
    string csrfOff = @"
        http.csrf().disable(); // ❌ CSRF disabled
    ";

    // 10. Weak JWT Secret (JS)
    string weakJwt = @"
        const SECRET = '12345'; // ❌ weak secret
    ";

    // 11. Missing PreparedStatement (Java)
    string noPreparedStatement = @"
        String q = ""UPDATE users SET email='"" + email + ""'""; // ❌ vulnerable
    ";

    // 12. eval() usage (JS)
    string jsEval = @"
        eval(userInput); // ❌ dangerous
    ";

    // 13. Insecure CORS (Node.js)
    string insecureCors = @"
        app.use(cors({ origin: '*' })); // ❌ insecure
    ";

    // 14. Flask debug mode (Python)
    string flaskDebug = @"
        app.run(debug=True)  # ❌ should be False
    ";

    // 15. No input validation (C#)
    public void NoValidation(string q)
    {
        Console.WriteLine("You searched for: " + q);  // ❌ unsanitized input
    }
}
