# Contributing to News App Backend

Thank you for your interest in contributing to News App Backend! This document provides guidelines for contributing to this project.

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Docker Desktop
- Maven 3.6+
- Git

### Setting up Development Environment

1. **Fork and clone the repository**
   ```bash
   git clone https://github.com/your-username/newsapp-backend.git
   cd newsapp-backend
   ```

2. **Set up the development environment**
   ```bash
   # Start database
   docker compose up db -d
   
   # Build and run the application
   cd newsapp
   mvn clean install
   mvn spring-boot:run
   ```

3. **Verify the setup**
   - API should be available at http://localhost:8080
   - Database should be running on localhost:5432

## 📝 How to Contribute

### Reporting Bugs
- Use the GitHub issue tracker
- Include detailed steps to reproduce the bug
- Provide system information (OS, Java version, etc.)

### Suggesting Enhancements
- Use the GitHub issue tracker with the "enhancement" label
- Describe the feature and its benefits
- Consider the impact on existing functionality

### Code Contributions

1. **Create a feature branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```

2. **Make your changes**
   - Follow the existing code style
   - Add tests for new functionality
   - Update documentation if needed

3. **Test your changes**
   ```bash
   mvn test
   docker compose up -d
   # Test manually
   ```

4. **Commit your changes**
   ```bash
   git add .
   git commit -m "Add: brief description of changes"
   ```

5. **Push and create a Pull Request**
   ```bash
   git push origin feature/your-feature-name
   ```

## 🎨 Code Style Guidelines

### Java
- Use meaningful variable and method names
- Add JavaDoc comments for public methods
- Follow Spring Boot conventions
- Use Lombok annotations where appropriate

### Database
- Use Flyway for database migrations
- Follow naming conventions (snake_case for tables/columns)
- Add proper indexes

### API Design
- Follow RESTful conventions
- Use appropriate HTTP status codes
- Include proper error handling
- Document API endpoints

## 🧪 Testing

### Running Tests
```bash
# Unit tests
mvn test

# Integration tests
mvn verify

# All tests with coverage
mvn clean test jacoco:report
```

### Test Guidelines
- Write unit tests for business logic
- Write integration tests for API endpoints
- Aim for at least 80% code coverage
- Use meaningful test names

## 📚 Documentation

### Code Documentation
- Add JavaDoc for public APIs
- Include inline comments for complex logic
- Update README.md for new features

### API Documentation
- Document all endpoints
- Include request/response examples
- Specify authentication requirements

## 🐛 Bug Reports

When reporting bugs, please include:

1. **Environment**
   - OS and version
   - Java version
   - Docker version
   - Maven version

2. **Steps to Reproduce**
   - Clear, numbered steps
   - Expected behavior
   - Actual behavior

3. **Additional Context**
   - Screenshots if applicable
   - Log files
   - Related issues

## 💡 Feature Requests

When suggesting features:

1. **Problem Description**
   - What problem does this solve?
   - Who would benefit from this feature?

2. **Proposed Solution**
   - How should this work?
   - Any alternatives considered?

3. **Additional Context**
   - Screenshots or mockups
   - Related issues or discussions

## 🔄 Pull Request Process

1. **Before Submitting**
   - Ensure all tests pass
   - Update documentation
   - Rebase on latest main branch

2. **Pull Request Template**
   - Description of changes
   - Related issues
   - Screenshots (if applicable)
   - Checklist of completed items

3. **Review Process**
   - Address review comments
   - Keep PRs focused and small
   - Respond to feedback promptly

## 📞 Getting Help

- Create an issue for questions
- Join our discussions
- Check existing documentation

## 📋 Checklist for Contributors

- [ ] Code follows project style guidelines
- [ ] Self-review completed
- [ ] Tests added/updated
- [ ] Documentation updated
- [ ] No breaking changes (or clearly documented)
- [ ] Commit messages are clear and descriptive

Thank you for contributing! 🎉
