# **CaseKaro QA Automation Assessment**

## **Overview**

This project is an automated test suite for the [CaseKaro](https://casekaro.com/) e-commerce platform. It validates the "Mobile Covers" user journey, specifically focusing on:

1. **Search Logic:** Verifying brand isolation (e.g., searching for "Apple"/"iPhone" excludes "Samsung").  
2. **Cart Operations:** Adding multiple material variants (Hard, Soft, Glass) of the same product.  
3. **Data Integrity:** Auditing the final cart prices and links.

**Tech Stack:** Java, Playwright, Maven.

##  **Project Structure**

* src/java/CaseKaroTest.java \- The main automation script.  
* docs/Test\_Test\_Plan\_Casekaro.md \- Strategy and scope definition.  
* docs/Test\_Case.csv \- Detailed test steps and expected results.  
* docs/Test\_Execution\_Report.md \- Summary of the latest test run.

##  **Key Highlights of the Code**

* **Negative Validation Logic:** A custom method validateNoOtherBrands() parses search results to strictly ensure no competitor brands appear in specific searches.  
* **Resilient Locators:** Uses locator().filter() and scrollIntoViewIfNeeded() to handle dynamic content and overlays without hard-coding fragile XPaths.  
* **Console Reporting:** Generates a structured "Audit Report" at the end of execution for quick debugging.

**Author:** Jayesh Solanke