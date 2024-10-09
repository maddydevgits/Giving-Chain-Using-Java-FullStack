
# GivingChain Charity Funding Platform

## Project Overview

**GivingChain** is a charity donation and request platform that connects donors with charitable organizations and individuals in need. The platform allows donors to offer donations, and charitable organizations (NGOs) to make requests for specific items or funds. An admin user can then map donations to requests, ensuring that the right resources reach the people who need them most.

## Features

### 1. User Registration and Login
- Donors and NGOs can register on the platform.
- Admin has a special login for managing mappings between donations and requests.
  
### 2. Donations
- Donors can submit various types of donations including food, clothing, funds, gadgets, and more.
- Each donation includes details such as the donor's name, contact information, and a description of the donation.
  
### 3. Requests
- NGOs and charitable organizations can make requests for specific items or funds.
- Each request includes details such as the requester's name, contact information, and a description of the request.
  
### 4. Admin Dashboard
- Admin users can map donations to requests via the Admin Dashboard.
- Donations marked as "distributed" once successfully mapped.
- Requests are marked as "accepted" once successfully mapped.

### 5. Email Notifications
- After a successful donation-to-request mapping, both the donor and requester receive email notifications.
- Emails include full details of the donation and request, such as names, contact information, and descriptions.

### 6. History Log
- Donors and requesters can view their donation and request histories.
- A mapping log shows the history of all donations and requests that have been mapped.

---

## Setup and Installation

### Prerequisites

Ensure you have the following installed on your system:
- **Java** (JDK 11 or later)
- **Maven** (for managing dependencies)
- **Spring Boot** (Spring Boot framework for backend development)
- **MySQL** (Database for storing donations, requests, and user data)
- **Thymeleaf** (For the front-end templating engine)

### Installation Steps

1. **Clone the repository**:
   ```bash
   git clone https://github.com/yourusername/givingchain.git
   cd givingchain
   ```

2. **Set up the database**:
   - Create a MySQL database for the project.
   - Modify the `application.properties` file to include your database configurations:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/givingchain_db
     spring.datasource.username=your_db_username
     spring.datasource.password=your_db_password
     ```

3. **Build and Run the Project**:
   - Use Maven to build and run the project:
     ```bash
     mvn clean install
     mvn spring-boot:run
     ```

4. **Access the Application**:
   - Once the project is running, you can access the web application on `http://localhost:8080`.

---

## Email Service

- The project uses **ZeptoMail** to send email notifications to donors and requesters.
- Ensure you configure your ZeptoMail API key in the `application.properties` file:
  ```properties
  zeptomail.api.url=https://api.zeptomail.in/v1.1/email
  zeptomail.api.key=your_api_key_here
  ```

---

## Project Structure

```bash
src/
 └── main/
     ├── java/
     │   └── com/makeskilled/GivingChain/
     │       ├── Controllers/       # Controllers for handling requests
     │       ├── Models/            # Models for entities (Donations, Requests, Users, Mappings)
     │       ├── Repositories/      # JPA repositories for database operations
     │       ├── Services/          # Services for business logic, including EmailService
     ├── resources/
     │   ├── templates/             # Thymeleaf templates for the UI
     │   └── application.properties # Configurations for database and other services
 └── test/
     └── java/com/makeskilled/GivingChain/ # Unit tests for the application
```

---

## API Endpoints

### Public Endpoints

- **User Registration**: `/register`
- **User Login**: `/login`
- **List All Donations**: `/donations/list`
- **List All Requests**: `/requests/list`

### Admin Endpoints

- **Admin Dashboard**: `/admin/dashboard`
- **Map Donations to Requests**: `/admin/map`
- **View All Mappings**: `/admin/mappings`

---

## Technologies Used

- **Java (Spring Boot)**: Backend framework.
- **Thymeleaf**: Frontend templating engine.
- **MySQL**: Database to store user, donation, and request information.
- **ZeptoMail**: Email API for sending notifications.
- **HTML/CSS/Bootstrap**: Frontend styling and layout.
  
---

## Future Enhancements

- **Search Functionality**: Allow users to search for specific donations or requests.
- **Notifications System**: Implement in-app notifications for real-time updates.
- **Detailed Admin Reports**: Generate detailed reports for donations, requests, and mappings.

---

## Contributors

- **Madhu Parvathaneni** - Project Lead & Backend Developer
- **Giridhar Jonalagadda** - Frontend Developer

---

## License

This project is licensed under the MIT License. See the `LICENSE` file for more details.
