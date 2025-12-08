// Hardcoded credentials
const USERNAME = "admin";
const PASSWORD = "P@ssw0rd123"; // ❌ Hardcoded password

// SQL Injection
function getUser(db, userId) {
  const query = "SELECT * FROM users WHERE id = " + userId; // ❌ SQL Injection
  return db.execute(query);
}

// Command Injection
const { exec } = require("child_process");
function runCommand(cmd) {
  exec("ping " + cmd); // ❌ Vulnerable: user-controlled command
}

// Path Traversal
const fs = require("fs");
function readLog(filename) {
  return fs.readFileSync("/var/logs/" + filename); // ❌ Path traversal
}

// Unsafe eval
function runCode(input) {
  eval(input); // ❌ Dangerous function
}

// Weak hashing (MD5)
const crypto = require("crypto");
function hashPassword(pwd) {
  return crypto.createHash("md5").update(pwd).digest("hex"); // ❌ Weak hash
}

// Insecure cryptography
function encrypt(text) {
  const cipher = crypto.createCipher("aes-128-ecb", "weakkey"); // ❌ ECB mode insecure
  return cipher.update(text, "utf8", "hex") + cipher.final("hex");
}

// Insecure random number
function generateOTP() {
  return Math.floor(Math.random() * 1000000); // ❌ Not cryptographically secure
}

// Hardcoded API key
const API_KEY = "sk_test_1234567890abcdef"; // ❌ Hardcoded secret

// Unsafe regex (ReDoS)
function validateEmail(email) {
  const regex = /(a+)+$/; // ❌ Vulnerable to ReDoS
  return regex.test(email);
}

module.exports = {
  getUser,
  runCommand,
  readLog,
  runCode,
  hashPassword,
  encrypt,
  generateOTP,
  validateEmail,
};
