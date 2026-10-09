# 🛡️ AI-Powered Fraud Detection System

A web-based fraud detection prototype that analyzes transaction details, calculates a risk score, and classifies transactions based on their potential fraud risk.

## 🌐 Project Links

- **GitHub Repository:** [AI-Powered-Fraud-Detection](https://github.com/mukherjeeshreosi2005-beep/AI-Powered-Fraud-Detection)
- **Live Website:** Coming soon — public deployment required.

## 📌 Project Overview

The AI-Powered Fraud Detection System helps identify potentially suspicious financial transactions using rule-based risk scoring. It evaluates transaction details and generates a risk level with a recommended action.

## ✨ Features

- Transaction amount and activity analysis
- Automated fraud-risk score calculation
- LOW, MEDIUM, and HIGH risk classification
- Transaction actions: ALLOW, VERIFY, or BLOCK
- SQLite database integration using JDBC
- Transaction history with timestamps
- Web interface built with HTML and Java Servlets

## 🛠️ Technologies Used

- Java
- Jakarta Servlets
- Apache Tomcat 10.1
- SQLite
- JDBC
- HTML
- Git and GitHub

## ⚙️ How It Works

1. The user enters transaction details into the web form.
2. The Java Servlet processes the submitted information.
3. Rule-based logic calculates the risk score.
4. The system classifies the transaction as LOW, MEDIUM, or HIGH risk.
5. The transaction details and result are saved in the SQLite database.
6. The application displays the result and recent transaction history.

## 🚀 Run the Project Locally

### Requirements

- Java Development Kit (JDK)
- Apache Tomcat 10.1
- SQLite JDBC driver

### Setup

1. Clone the repository:

   ```bash
   git clone https://github.com/mukherjeeshreosi2005-beep/AI-Powered-Fraud-Detection.git
   ```

2. Open the project folder and configure the application for Apache Tomcat.

3. Ensure the SQLite JDBC driver is available to the application.

4. Compile the servlet and deploy the web application to Tomcat.

5. Start Tomcat and open:

   `http://localhost:8080/FraudApp/index.html`

## 🗄️ Database

The application uses SQLite to store transaction information, including:

- Transaction ID
- Transaction amount
- Risk score and risk level
- Recommended action
- Creation timestamp

The database is created locally when the application initializes its transaction table.

## 🧪 Example Test Results

| Transaction Amount | Risk Score | Risk Level |
|---:|---:|---|
| ₹1,000 | 0 | LOW |
| ₹75,000 | 90 | HIGH |

These are example results from local testing.

## ⚠️ Disclaimer

This is an educational prototype using rule-based scoring. It does not currently use a trained machine-learning model and should not be used to make real financial decisions.

## 🔮 Future Improvements

- Integrate a trained machine-learning model
- Add user authentication and an admin dashboard
- Improve fraud pattern detection
- Deploy the application to a public hosting service
- Add visual analytics and transaction reports

## 👩‍💻 Project Repository

Explore the source code:

https://github.com/mukherjeeshreosi2005-beep/AI-Powered-Fraud-Detection

---

**Developed as an educational project demonstrating Java web development, JDBC, database integration, and transaction risk analysis.**
