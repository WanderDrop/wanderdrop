# Wanderdrop

**Wanderdrop** is a Google Maps-based application developed by two students as a final project for the SDA Java course.
The app allows users to search and add attractions, comments, and reports everywhere in the World. It also features a search 
bar that assists users in navigating to any location they wish to check or where they want to add a new attraction. Users can see their profile, modify their name and
password. Users can also see their created attractions and comments. Administrators can manage all the created reports. There are three user profiles: administrators,
authenticated users, and unauthorized users, each with different permissions and functionalities.

## Table of Contents

- [Project Description](#project-description)
- [Technologies Used](#technologies-used)
- [Highlights](#highlights)
- [Installation and Setup](#installation-and-setup)
- [Usage](#usage)
- [License](#license)
- [Contact](#contact)

## Project Description

Wanderdrop is a web application that integrates Google Maps Maps JavaScript API and Places API to enable users to explore and interact with
various attractions. Users can add new attractions, leave comments, and make reports about the attraction. The app provides different levels
of access and functionalities based on user roles.

- **Administrators:** Full access to manage attractions, comments, reports, profile and adding new users with different roles.
- **Authenticated Users:** Can view and add new attractions and comments. Able to create reports and manage the profile page.
- **Unauthorized Users:** Can view attractions and comments but cannot add or modify them.

GitHub repository: [Wanderdrop](https://github.com/WanderDrop/wanderdrop)

## Technologies Used

- **Backend:** Spring Boot, Spring Security, JPA/Hibernate, JWT
- **Frontend:** Angular 17, TypeScript, HTML, CSS, Bootstrap
- **Database:** MySQL
- **Tools & Practices:** Git, RESTful APIs, Agile methodologies, Trello, Postman
- **APIs:** Google Maps- Maps JavaScript API, Places API

## Highlights

- **JWT Authentication System:** JWT-based authentication system for secure user authentication.
- **Data Loader:** Startup component to preload data into the database.
- **CORS Configuration:** CORS policies for cross-origin requests.
- **Google Maps Integration:** Maps JavaScript API and Places API for location-based functionalities.

## Installation and Setup

1. **Clone the repository:**
   ```sh
   git clone https://github.com/WanderDrop/wanderdrop.git
   ```
   or

    ```sh
    git clone git@github.com:WanderDrop/wanderdrop.git
   ```

### Backend Setup:

1. Navigate to the backend directory:
   ```sh
   cd wanderdrop/wserver
    ```
2. Configure the application properties with your database credentials inside application.properties
3. Build and run the Spring Boot application

### Frontend Setup:
1. Navigate to the frontend directory:
   ```sh
   cd wanderdrop/wclient
    ```
2. Install dependencies
    ```sh
   npm install
    ```
3. Add environments: 
   ```sh
   ng generate environments
   ```
   or
   ```sh
   ng g environments
   ```
   
Inside environment.development.ts and environment.ts add two variables and assign your values to:
- API_KEY
- MAP_ID

4. Start the Angular development server:
     ```sh
   ng serve
    ```
## Usage

- **Administrators**: Create, Modify and Soft Delete attractions, comments, and reports. Change name, password. View list of created attractions, comments and your-profile page.
- **Authenticated Users**: Add new attractions, comments, reports. Change name, password. View list of created attractions, comments and your-profile page.
- **Unauthorized Users**: View attractions and comments.

## License

This project is licensed under the MIT License.

## Contact

#### GitHub:
- [Ellucy](https://github.com/Ellucy)
- [AngieSepulveda](https://github.com/AngieSepulveda)

### Project Repository:
- [Wanderdrop](https://github.com/WanderDrop/wanderdrop)