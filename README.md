# README

## Overview

Ensure you have Java installed on your machine to execute the JAR file. The JAR file contains a compiled Java application that can be executed using the Java Runtime Environment (JRE).


## Prerequisites

1. **Java Installation**

   Ensure Java is installed on your machine. To check if Java is installed, run:

   java -version

*If java is not installed on your system, you can download and install it from https://jdk.java.net/22/

## Running the JAR File
1. Obtain the JAR file from the zip folder and save it to a directory on your computer.
2. Use the cd command (e.g cd path/to/your/directory) to change to the directory containing the JAR file.
3. Execute the JAR file using the 'java -jar jarfilename.jar'

*Note: Ensure you set the environment variables for email sender email credentials as EMAIL_USERNAME" and "EMAIL_PASSWORD " before executing the JAR.
On windows CMD it will be something close to:


1. Open the Start menu and search for "Environment Variables"
2. Click on "Edit the system environment variables"
3. In the System Properties window, click on "Environment Variables"
4. Under "User variables", click "New" to add a new variable
5. Set the variable name as EMAIL_USERNAME and the value as your email address
6. Repeat the process to add EMAIL_PASSWORD with your password as the value

To set variables temporarily for a single session, open Command Prompt and type:
1. set EMAIL_USERNAME=your_email@example.com
2. set EMAIL_PASSWORD=your_password


If you're using a Gmail account, you need to use an "App Password" instead of your regular account password since 2-Factor Authentication can cause "Bad Credentials" errors even with the correct password.


