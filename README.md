# URL Shortener

A simple URL shortening service built with **Java** + **Spring Boot** for the backend and **Angular + Bootstrap** for the frontend.

---

## Features

- Shorten long URLs with optional custom aliases and titles.
- Track access count and last access time for each link.
- Set expiration date/time for short URLs.
- Redirect users to the original URL with proper HTTP status codes:
  - `301` for valid links
  - `410` for expired links
  - `404` for missing links
- Copy short URLs to clipboard directly from the UI.
- Simple dashboard to view and delete links.

---

## Tech Stack

- **Backend**: Java, Spring Boot, Spring Data JPA, H2 Database (in-memory)
- **Frontend**: Angular, Bootstrap
- **Testing**: JUnit 5, TestRestTemplate
- **Build**: Maven

---

## Getting Started

### Backend

1. Clone the repository:
    ```bash
    git clone https://github.com/maximeacd/url-generator.git
    cd url-generator
    ```

2. Run the backend:
    ```bash
    mvn spring-boot:run
    ```

3. Default backend configuration:
    - Port: `8081`
    - Base URL: `http://localhost:8081`
    - H2 in-memory database for development/testing.

### Frontend

1. Navigate to the frontend folder (if separate):
    ```bash
    cd frontend
    ```

2. Install dependencies:
    ```bash
    npm install
    ```

3. Run the Angular app:
    ```bash
    ng serve
    ```

4. Open your browser:
    ```
    http://localhost:4200
    ```

---

## Usage

1. Fill in the form:
   - **Original URL** (required)
   - **Custom Alias** (required)
   - **Expires At** (optional)

2. Click **Shorten**.

3. Copy/click the generated short URL or navigate back to create another.

---

## API Endpoints

- `POST /api/shorten` → Shorten a URL
- `GET /api/redirect/{code}` → Redirect to original URL
- `PUT /api/links/{code}` → Update link info
- `DELETE /api/links/{code}` → Disable/delete a link

---

## Notes

- The backend uses an **H2 in-memory database**, so all data is lost on restart.
- Short codes are generated using a **Base62 encoding** of a sequence ID.
- Expired links return HTTP `410 Gone`.
