# CaseKaro QA Automation Assessment

##  Project Overview
This project is a QA Automation framework built to validate search behavior, product filtering, and cart functionality on the CaseKaro website.

The automation covers:
- Brand search validation
- Negative validation to ensure other brands do not appear in results
- Adding multiple product material variants to the cart
- Verifying cart item count
- Generating a final cart audit report with product details

---

## 🛠 Tech Stack
- **Java (JDK 11+)**
- **Maven**
- **Playwright for Java**
- **Windows PowerShell / Terminal**

---

##  Project Structure

```
AutomationAssessment/
│
├── src/
│   └── main/
│       └── java/
│           └── com/Casekaro  
│           └── CaseKaroTest.java
│
├── Docs/
│   └── Test/
│       ├── Test_Plan_Casekaro.docx
│       ├── Test_Cases.xlsx
│       └── Test_Execution_Report.docx
│
├── pom.xml
└── README.md
```

---

## ⚙️ Setup Instructions

### 1️ Install Prerequisites
Make sure the following are installed:
- Java JDK 11 or higher
- Maven
- Git (optional)

Check versions:
```
java -version
mvn -version
```

---

### 2️ Install Playwright Browsers (First Time Only)

Run this from the project root:

```
mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"
```

---

### 3️ Run the Automation Test

```
mvn clean compile exec:java
```

---

##  Expected Execution Flow

1. Launch browser using Playwright
2. Search for **Apple** → validate that no other brand models appear
3. Search for **iPhone** → validate brand filtering again
4. Open a product page
5. Add **Soft**, **Glass**, and **Hard** material variants to cart
6. Open cart and verify **3 items are present**
7. Print **Final Cart Audit Report** including:
   - Material type
   - Price
   - Product link

---

## Sample Console Output

```
Negative Validation Passed for 'iPhone' search results.

================ FINAL CART AUDIT REPORT ================
ITEM 1:
 - Material: Soft
 - Price    : Rs. 149.00
 - Full Link: https://casekaro.com/...
---------------------------------------------------------
```

---

## Test Documentation Included

Inside the **Docs/Test** folder:
- Test Plan
- Test Cases
- Test Execution Report

---

## Author
**Jayesh Solanke**