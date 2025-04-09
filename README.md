# RafBook-ZaMaru

A **JavaFX-based desktop application** for managing clients. This application allows administrators to perform various actions related to client management, including adding, editing, deleting, and viewing client information. It provides a user-friendly interface for interacting with client data stored on the backend (via API or database).

---

## Features
- **Client Management**: Add, edit, view, and delete client records.
- **Search Clients**: Search for clients by name, ID, or other attributes.
- **View Client Details**: View detailed information about individual clients.
- **Responsive UI**: A clean and intuitive user interface for easy navigation.
- **Database Integration**: Interface with the backend database to retrieve and update client information.
- **Secure Authentication**: Login functionality with secure authentication (JWT-based).

---

## Technologies
- **JavaFX** (for the desktop UI)
- **Spring Boot** (for backend API if applicable)
- **JDBC / Hibernate / JPA** (for database interaction)
- **JWT Authentication** (for secure login and access control)
- **Scene Builder** (optional for building JavaFX UI in a drag-and-drop manner)
- **Maven / Gradle** (for project dependency management)

## Requirements
- **Java 11+**
- **Maven / Gradle** (depending on your project build tool)
- **IDE**: IntelliJ IDEA, Eclipse, or any Java IDE
- **JDK**: JavaFX SDK

---

## Features and Usage

### 1. **Authentication**
   - **Login**: Admins can log in using their username and password. JWT token-based authentication is used for secure access.
   - **Logout**: Logs the user out, clearing the JWT token from the session.

### 2. **Client Management**
   - **Add Client**: Create new client records with essential details (e.g., name, contact, email).
   - **Edit Client**: Modify existing client information.
   - **Delete Client**: Remove clients from the system.
   - **View Client List**: Display all clients with basic information like name, status, and ID.
   - **View Client Details**: View detailed information about a selected client, including contact info, past interactions, etc.
   
### 3. **Search Clients**
   - **Search Functionality**: Quickly find clients based on name, ID, or other search criteria.
   - **Filters**: Filter clients by status (active, inactive, etc.) or other attributes.

### 4. **Security**
   - **JWT Authentication**: Ensures secure access to the application. Only authenticated users can access the admin features.
   - **Role-Based Access Control**: Only users with admin roles can manage client data. Other roles (if implemented) are restricted to view-only access.

### 5. **User Interface**
   - **Responsive Layout**: JavaFX UI components for a dynamic layout (tables, forms, etc.).
   - **Easy Navigation**: Simple navigation through the client list and client details.
   - **Alerts and Notifications**: Pop-up messages for actions such as adding, updating, or deleting clients.

### 6. **Database Integration**
   - **CRUD Operations**: Perform create, read, update, and delete operations on the client database via REST API or direct database connections (JDBC/Hibernate).
   - **Database Sync**: Synchronizes the client data with the backend database (API calls or JDBC queries).

---

## Security

- **JWT Authentication**: Secures the application by verifying the JWT token during login and every subsequent API call.
- **Role-Based Access**: Ensures only users with the admin role can access client management features.
- **Data Protection**: Ensure sensitive client information is encrypted during storage and transmission.

---

## Running the Application

### 1. **Install Dependencies**
   If you're using Maven, make sure the required dependencies are in your `pom.xml`. If you're using Gradle, they should be in your `build.gradle`.

### 2. **Build the Project**
   - Using **Maven**:
     ```bash
     mvn clean install
     ```
   - Using **Gradle**:
     ```bash
     gradle build
     ```

### 3. **Run the Application**
   After building the project, run the `Main` class that launches the JavaFX application.

   - If you’re using Maven, you can use:
     ```bash
     mvn javafx:run
     ```

   - If you’re using Gradle, you can use:
     ```bash
     gradle run
     ```

### 4. **Build for Distribution (Optional)**
   To package the app for distribution, you can use Maven or Gradle to create a JAR or a platform-specific installer (e.g., `.exe`, `.dmg`).

   - **Maven** (for packaging):
     ```bash
     mvn package
     ```
   - **Gradle**:
     ```bash
     gradle build
     ```

   This will create a JAR file or platform-specific executable that you can distribute.

---
