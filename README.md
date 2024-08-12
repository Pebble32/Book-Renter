# Book Renter - Peer-to-Peer Book Renting Platform

## Introduction
**Book Renter** is a peer-to-peer book renting platform that allows users to rent books from each other. The application is built using **Spring Boot** for the backend and **Angular** for the frontend. It includes features like book management, sharing, and borrowing. The project leverages several technologies, including Docker for containerization, PostgreSQL for database management, and MailDev for email testing.

## Features
- **User Registration and Authentication**: Secure user sign-up, login, and authentication using JWT tokens (with planned migration to Keycloak).
- **Book Management**: Users can add, update, delete, and archive their books.
- Book Sharing: Users can mark their books as available for rent and share them with other users.
- **Book Borrowing**: Users can browse available books and borrow them from other users.
- ***[TODO]* Notifications**: Implement email notifications for book borrowing requests, confirmations, and returns.
- ***[TODO]* Admin Panel**: Develop an admin panel for administrators to manage users and monitor platform activities.
- ***[TODO]* CI/CD Pipeline**: Implement a CI/CD pipeline using Docker.

## Technologies Used
- **Backend**: [Spring Boot](https://spring.io/projects/spring-boot)
- **Frontend**: [Angular](https://angular.dev/)
- **Database**: [PostgreSQL](https://www.postgresql.org/)
- **Containerization**: [Docker](https://www.docker.com/)
- **Email Testing**: [MailDev](https://github.com/maildev/maildev)
- **JWT Authentication**: Secure token-based authentication for API endpoints *(planned migration to [Keycloak](https://www.keycloak.org/))*.

## Project Structure
The current setup includes a Dockerfile for the backend, allowing it to be containerized and managed easily. A Dockerfile for the frontend is planned but not yet implemented. Once the frontend Dockerfile is ready, both the backend and frontend can be added to the Docker Compose file, enabling the entire application to be run within containers.
```
BookRenter/
├── book-renter/               # Spring Boot backend
│   ├── src/main/java/         # Java source files
│   ├── src/main/resources/    # Configuration files
│   ├── Dockerfile             # Dockerfile for backend
│   └── ...
├── br-ur/                     # Angular frontend
│   ├── src/                   # Angular source files
│   ├── Dockerfile             # Dockerfile for frontend (TODO)
│   └── ...
├── docker-compose.yml         # Docker Compose file for DB and MailDev 
└── README.md                  # This README file
```
## Getting Started
### Prerequisits
- **Docker** installed on your machine
- **Java 11+** for running Spring Boot backend locally
 - **Node.js** and **npm** for running Angular front end locally

### Installation and Setup
1. **Clone the repository**:
```bash
git clone https://github.com/Pebble32/Book-Renter.git
cd book-renter
```
2. **Set up the Database nad MailDev**:
    * Use Docker Compose to set up the PostgreSQL database and MailDev services: 
    ```bash
    docker-compose up -d
    ``` 
    * This will start the PostgreSQL container (postgres-sql-book) and MailDev container (mail-dev-book), connecting them via the book-renter-net network.
    <br>

3. **Backend Setup**:
    * Naviagte to the backend directory:
    ```bash
    cd book-renter
    ```
    * Build and run the Spring Boot application:
    ```bash
    ./mvn clean install
    java -jar target/book-renter.jar
    ```
    * Alternatively running as Docker Container:
    ```bash
    docker build -t book-renter-backend .
    docker run --network=book-renter-net -p 8088:8080 book-renter-backend
    ```

4. **Frontend Setup**:
    * Naviagte to the backend directory:
    ```bash
    cd br-ur
    ```
    * Install Angular dependencies:
    ```bash
    npm install
    ```
    * Serve the Angular application:
    ```
    ng serve
    ```
    * Alternatively, once the frontend Dockerfile is implemented, you will be able to build and run the frontend using Docker, similar to the backend setup.

### Accessing the application
- **Frontend**: The Angular frontend is served on http://localhost:4200.
- **Backend**: The Spring Boot backend is served on http://localhost:8088.
- **MailDev**: Access the MailDev web interface on http://localhost:1080 to see test emails.

### Envioroment Variables
The application uses environment variables for configuration. These are set within the Docker Compose file for PostgreSQL:
 * **Backend**:
    * `DB_HOST`: postgres-sql-book (Docker service name in the book-renter-net network).
    * `DB_PORT`: 5432 (mapped to 5434 on your local machine).
    * `DB_NAME`: book_db.
    * `DB_USERNAME`: username.
    * `DB_PASSWORD`: mysecretpassword.
    * `JWT_SECRET`: Set as an environment variable for JWT token generation (planned migration to **Keycloak**).

### MailDev
MailDev is used for capturing and testing emails sent by the application. It runs on http://localhost:1080 as configured in the Docker Compose file. The SMTP service is available on port 1025. [GitHub Repo](https://github.com/maildev/maildev)

## Deployment on AWS uscing ECS [TODO]
Steps will be added later on.

## Demo Screenshots [TODO]
* Home Page
* Book Management
* Borrowing Process
* Notifications (when implemented)
* Admin Panel (when implemented)

## TODO
* **Implement Notifications**: Add email notifications for key user actions.
* **Develop Admin Panel**: Build an admin interface for managing users and system monitoring.
* **Migrate Authentication to Keycloak**: Replace JWT with Keycloak for more robust authentication and user management.
* **CI/CD Pipeline**: Set up continuous integration and deployment using Docker.
* **Deploy on AWS ECS**: Complete deployment on AWS using ECS and add steps to the README file
* **Implement Frontend Dockerfile**: Create a Dockerfile for the Angular frontend and integrate it into Docker Compose.
* **Add Screenshots**: Add Screenshots of main page components


## License
This project is licensed under the MIT License. See the `LICENSE` file for details.
