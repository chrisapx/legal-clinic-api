# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Legal Clinic Uganda - A mobile-first tool to ease the process of legal consultation. This is a Spring Boot application that serves as an adapter layer for knowledge engine integrations with AI capabilities (OpenAI).

## Build System

This project uses Gradle with the Gradle wrapper.

**Build the project:**
```bash
./gradlew build -x test  # Skip tests if database not configured
```

**Run the application:**
```bash
./gradlew bootRun
```

**Run tests:**
```bash
./gradlew test
```

**Clean build artifacts:**
```bash
./gradlew clean
```

**Note:** Tests require a configured database. Use `-x test` flag to skip tests during development.

## Technology Stack

- Java 17
- Spring Boot 3.5.9
- Spring Data JPA (Hibernate)
- Spring AI 1.1.2 with OpenAI integration
- MySQL/MariaDB database
- Jakarta Bean Validation
- Lombok for boilerplate reduction
- PDF document reading capabilities (Spring AI PDF Document Reader)

## Architecture

The application follows a layered architecture pattern with three main service domains:

### Common Infrastructure (`common` package)
Shared components used across all services:
- **Exception Handling**: Global exception handler with custom exceptions (ResourceNotFoundException, BadRequestException)
- **DTOs**: ApiResponse wrapper for consistent API responses, ErrorResponse for error handling
- **Base Entity**: Audited base entity with id, createdAt, updatedAt fields
- **JPA Config**: JPA auditing configuration

### 1. Identity Service (`identity_service`)
Manages user accounts, lawyers, and legal firms.

**Entities:**
- `User`: Base user with role (CLIENT, LAWYER, ADMIN)
- `Lawyer`: Professional profile linked to User with license info, specialization, verification status
- `LegalFirm`: Law firm entities with registration details and associated lawyers

**Key Relationships:**
- User ↔ Lawyer (One-to-One)
- LegalFirm ↔ Lawyer (One-to-Many)

**API Endpoints:**
- `/api/users` - User CRUD operations
- `/api/lawyers` - Lawyer management with verification
- `/api/legal-firms` - Legal firm management with verification

### 2. Document Adapter (`doc_adapter`)
Manages legal document templates and generates documents.

**Entities:**
- `DocumentTemplate`: Reusable templates with content, format (PDF/DOCX/HTML), version control
- `GeneratedDocument`: Generated documents with status tracking, metadata, file paths

**API Endpoints:**
- `/api/document-templates` - Template CRUD, category filtering, activation status
- `/api/documents/generate` - Document generation from templates
- `/api/documents` - Generated document management

**Note:** PDF/DOCX generation is stubbed for MVP - integrate Spring AI PDF capabilities for production.

### 3. Learning Service (`learn_service`)
Learning platform with blog, Q&A forum, news updates, and AI chat.

**Entities:**
- `BlogPost`: Articles with categories, tags, publish status
- `Question`: Q&A forum questions with vote counts, resolution status
- `Answer`: Answers to questions with vote counts, acceptance status
- `NewsUpdate`: Legal news with categories (LEGAL_UPDATES, REGULATORY_CHANGES, etc.)

**Key Relationships:**
- Question ↔ Answer (One-to-Many)

**API Endpoints:**
- `/api/blog-posts` - Blog management with publishing workflow
- `/api/questions` - Q&A forum questions
- `/api/answers` - Q&A answers with acceptance workflow
- `/api/news-updates` - News management with categories
- `/api/chat` - AI-powered legal chat using OpenAI

**AI Integration:**
- `LegalChatService`: Uses Spring AI ChatModel with OpenAI for legal assistance
- Configured with system context specific to Ugandan legal procedures

## Configuration

Database and AI configuration is managed in `src/main/resources/application.yaml`:
- MariaDB database connection on localhost:3306
- Database name: `lc-kw`
- OpenAI API integration configured for GPT model

**Security Note:** The application.yaml contains sensitive credentials that should be externalized to environment variables or a secure configuration management system in production.

## Package Structure

Base package: `org.lc.kwengineadapter`

Each service follows a consistent layered structure:
```
service_name/
  ├── controller/     # REST controllers (@RestController)
  ├── service/        # Business logic (@Service)
  ├── repository/     # Data access (@Repository, JpaRepository)
  ├── entity/         # JPA entities (@Entity)
  └── dto/           # Request/Response DTOs
```

**Common Package:**
```
common/
  ├── config/        # Configuration classes (JpaConfig)
  ├── dto/          # Shared DTOs (ApiResponse, ErrorResponse)
  ├── entity/       # Base entities (BaseEntity)
  └── exception/    # Custom exceptions and global exception handler
```

## Development Guidelines

### Adding New Endpoints
1. Create DTOs in the `dto` package with validation annotations
2. Add business logic in Service layer with @Transactional where needed
3. Create REST controller with appropriate HTTP methods and response wrappers
4. Use ApiResponse.success() or ApiResponse.error() for consistent responses

### Database Operations
- All entities extend `BaseEntity` for automatic audit fields
- Use JpaRepository for standard CRUD operations
- Add custom query methods in repository interfaces using Spring Data JPA naming conventions
- Mark write operations with `@Transactional` in service layer

### Exception Handling
- Use `ResourceNotFoundException` for missing entities
- Use `BadRequestException` for validation or business rule violations
- GlobalExceptionHandler automatically converts exceptions to proper HTTP responses
- Validation errors from Jakarta Bean Validation are automatically handled

### AI Integration
- ChatModel bean is auto-configured by Spring AI
- Use Prompt class to construct prompts for OpenAI
- Response text accessed via `.getResult().getOutput().getText()`

## Implementation Status

**Completed:**
- All three service domains fully implemented with CRUD operations
- Global exception handling and validation
- JPA entities with relationships and audit fields
- AI chat integration with OpenAI
- REST API with consistent response format

**TODO for Production:**
- Implement actual PDF/DOCX generation in DocumentGenerationService
- Add authentication and authorization (Spring Security)
- Implement password encryption for User entity
- Add pagination support for list endpoints
- Configure connection pooling for database
- Add API documentation (Swagger/OpenAPI)
- Implement file storage for generated documents
- Add comprehensive integration tests
- Externalize sensitive configuration to environment variables
