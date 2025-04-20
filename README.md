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
- **Maven** (for project dependency management)

## Requirements
- **Java 11+**
- **Maven(depending on your project build tool)
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

This is a JavaFX desktop application that connects to a backend service, which you can find here:

https://github.com/RAFSoftLab/RafBook-Backend

To run the application locally, follow these steps:

# 1. Clone the repository
git clone https://github.com/RAFSoftLab/RafBook-ZaMaru.git

# 2. Navigate to the project directory
cd your-repo-name

# 3.Configure the API URL: In the application.properties file, set the api.url to the desired backend URL (default is http://localhost:8080/api), if you wish to connect to a different environment.

# 4. Build the project
mvn clean package

# 5. Run the application
mvn javafx:run


Make sure the backend is up and running before starting the desktop app.
