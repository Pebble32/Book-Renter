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

## Getting Started