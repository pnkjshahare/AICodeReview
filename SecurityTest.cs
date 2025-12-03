using System;
using System.IO;
using System.Security.Cryptography;
using System.Text;
using System.Net.Http;

namespace DemoSecurityIssues
{
    public class SecurityTest
    {
        // ❌ Hardcoded credentials (Semgrep: secrets, hardcoded-password, API keys)
        private string dbPassword = "password123";
        private string apiKey = "AKIA123456SECRETKEY";

        public void InsecureCrypto()
        {
            // ❌ Weak random (Semgrep: insecure-random)
            var random = new Random();
            int token = random.Next();

            Console.WriteLine("Generated token: " + token);

            // ❌ Weak hash (Semgrep: insecure-hash)
            using var md5 = MD5.Create();
            var hash = md5.ComputeHash(Encoding.UTF8.GetBytes("test"));
            Console.WriteLine(Convert.ToHexString(hash));
        }

        public void FileAccess(string userInput)
        {
            // ❌ Path traversal (Semgrep: filesystem.unsafe-path-combine)
            string filePath = Path.Combine("C:\\data\\files\\", userInput);
            string content = File.ReadAllText(filePath);
            Console.WriteLine(content);
        }

        public async void HttpTest()
        {
            var client = new HttpClient();

            // ❌ Disabling SSL validation (Semgrep: insecure-ssl)
            client.DefaultRequestHeaders.Add("Accept", "*/*");

            // ❌ Sending sensitive data over HTTP (Semgrep: http-with-sensitive-data)
            string url = "http://example.com/login?user=admin&password=admin123";
            var result = await client.GetStringAsync(url);

            Console.WriteLine(result);
        }

        public void SQLTest(string userName)
        {
            // ❌ SQL Injection (Semgrep: sql-injection)
            string query = "SELECT * FROM Users WHERE name = '" + userName + "'";
            Console.WriteLine(query);
        }
    }
}
