# Docker and CI/CD Documentation

This document describes the Docker containerization and CI/CD pipeline setup for the Legal Clinic Knowledge Engine Adapter.

## Docker Setup

### Dockerfile

The application uses a multi-stage Dockerfile for optimal image size and build efficiency:

**Stage 1: Build**
- Uses `gradle:8.11.1-jdk17` as the build image
- Copies source code and dependencies
- Builds the application using `./gradlew bootJar -x test`

**Stage 2: Runtime**
- Uses `eclipse-temurin:17-jre-alpine` for a lightweight runtime image
- Runs as non-root user for security
- Includes health check endpoint
- Exposes port 8080

### Building Docker Image Locally

```bash
# Build the Docker image
docker build -t lc-kw-engine-adapter:latest .

# Build with custom tag
docker build -t lc-kw-engine-adapter:v1.0.0 .
```

### Running the Container

**Basic run:**
```bash
docker run -p 8080:8080 lc-kw-engine-adapter:latest
```

**With environment variables:**
```bash
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mariadb://host.docker.internal:3306/lc-kw \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=your_password \
  -e SPRING_AI_OPENAI_API_KEY=your_openai_key \
  lc-kw-engine-adapter:latest
```

**With external configuration:**
```bash
docker run -p 8080:8080 \
  -v $(pwd)/application.yaml:/app/config/application.yaml \
  -e SPRING_CONFIG_LOCATION=/app/config/application.yaml \
  lc-kw-engine-adapter:latest
```

**With JVM options:**
```bash
docker run -p 8080:8080 \
  -e JAVA_OPTS="-Xmx512m -Xms256m" \
  lc-kw-engine-adapter:latest
```

### Docker Compose (Optional)

Create a `docker-compose.yml` file for local development:

```yaml
version: '3.8'

services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:mariadb://db:3306/lc-kw
      - SPRING_DATASOURCE_USERNAME=root
      - SPRING_DATASOURCE_PASSWORD=rootpassword
      - SPRING_AI_OPENAI_API_KEY=${OPENAI_API_KEY}
    depends_on:
      - db

  db:
    image: mariadb:10.11
    environment:
      - MYSQL_ROOT_PASSWORD=rootpassword
      - MYSQL_DATABASE=lc-kw
    ports:
      - "3306:3306"
    volumes:
      - mariadb_data:/var/lib/mysql

volumes:
  mariadb_data:
```

Run with: `docker-compose up`

## CI/CD Pipeline

### GitHub Actions Workflow

The CI/CD pipeline is configured in `.github/workflows/docker-build-push.yml` and automatically:

1. **Builds** the Docker image on every push to `main` or `develop` branches
2. **Tests** the Docker image to ensure it starts correctly
3. **Pushes** to Docker Hub (only for pushes to main/develop, not PRs)
4. **Tags** images based on:
   - Branch name (e.g., `main`, `develop`)
   - Git tags (e.g., `v1.0.0`, `1.0`, `1`)
   - Commit SHA (e.g., `main-abc1234`)
   - `latest` tag for main branch

### Setting Up Docker Hub Credentials

Before the pipeline can push images, you need to configure Docker Hub credentials as GitHub Secrets:

#### Step 1: Create Docker Hub Access Token

1. Log in to [Docker Hub](https://hub.docker.com/)
2. Click on your username → **Account Settings**
3. Go to **Security** → **New Access Token**
4. Create a token with **Read, Write, Delete** permissions
5. Copy the token (you won't be able to see it again)

#### Step 2: Add GitHub Secrets

1. Go to your GitHub repository
2. Navigate to **Settings** → **Secrets and variables** → **Actions**
3. Click **New repository secret**
4. Add the following secrets:

   **DOCKERHUB_USERNAME**
   - Value: Your Docker Hub username

   **DOCKERHUB_TOKEN**
   - Value: The access token you created in Step 1

### Triggering the Pipeline

The pipeline automatically triggers on:

**Push to main or develop:**
```bash
git add .
git commit -m "feat: add new feature"
git push origin main
```

**Creating a release tag:**
```bash
git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin v1.0.0
```

**Pull requests** (build and test only, no push):
```bash
# The pipeline runs automatically when you create or update a PR
```

### Pipeline Workflow

1. **Checkout**: Clones the repository
2. **Setup Buildx**: Configures Docker BuildKit for advanced features
3. **Login**: Authenticates with Docker Hub (skipped for PRs)
4. **Extract Metadata**: Generates tags and labels
5. **Build**: Builds the Docker image with layer caching
6. **Test**: Runs the container to verify it starts correctly
7. **Push**: Pushes image to Docker Hub (skipped for PRs)
8. **Summary**: Creates a deployment summary with pull commands

### Viewing Pipeline Results

After the pipeline runs:

1. Go to **Actions** tab in your GitHub repository
2. Click on the workflow run
3. View the deployment summary with:
   - Published image tags
   - Docker pull commands
   - Image digest

### Pulling Images from Docker Hub

Once pushed, anyone can pull the image:

```bash
# Pull latest
docker pull <your-dockerhub-username>/lc-kw-engine-adapter:latest

# Pull specific version
docker pull <your-dockerhub-username>/lc-kw-engine-adapter:v1.0.0

# Pull by branch
docker pull <your-dockerhub-username>/lc-kw-engine-adapter:main
```

## Image Tags Strategy

The pipeline creates multiple tags for flexibility:

| Trigger | Tags Created | Example |
|---------|--------------|---------|
| Push to main | `latest`, `main`, `main-<sha>` | `latest`, `main`, `main-abc1234` |
| Push to develop | `develop`, `develop-<sha>` | `develop`, `develop-xyz5678` |
| Git tag v1.2.3 | `v1.2.3`, `1.2.3`, `1.2`, `1` | All version variations |
| Pull request | `pr-<number>` (not pushed) | `pr-42` |

## Security Best Practices

1. **Never commit secrets** to the repository
2. **Use GitHub Secrets** for sensitive data (Docker Hub credentials, API keys)
3. **Rotate access tokens** periodically
4. **Use non-root user** in Docker container (already configured)
5. **Scan images** for vulnerabilities (consider adding to pipeline)
6. **Keep base images updated** (update Dockerfile FROM statements regularly)

## Troubleshooting

### Build Fails

**Issue**: Gradle build fails in Docker
```bash
# Check build locally first
./gradlew clean build -x test

# Build Docker image with verbose output
docker build --progress=plain -t lc-kw-engine-adapter:debug .
```

### Container Won't Start

**Issue**: Container exits immediately
```bash
# Check container logs
docker logs <container-id>

# Run interactively to debug
docker run -it --entrypoint /bin/sh lc-kw-engine-adapter:latest

# Check if JAR was created
docker run --rm lc-kw-engine-adapter:latest ls -la /app/
```

### Pipeline Authentication Fails

**Issue**: Docker Hub login fails in GitHub Actions
- Verify `DOCKERHUB_USERNAME` and `DOCKERHUB_TOKEN` are set correctly
- Ensure the access token has **Read, Write, Delete** permissions
- Check if the token has expired and create a new one

### Image Too Large

**Issue**: Docker image is larger than expected
```bash
# Analyze image layers
docker history lc-kw-engine-adapter:latest

# Use dive to explore image
docker run --rm -it -v /var/run/docker.sock:/var/run/docker.sock \
  wagoodman/dive:latest lc-kw-engine-adapter:latest
```

## Next Steps

Consider enhancing the pipeline with:

- **Image scanning**: Add Trivy or Snyk for vulnerability scanning
- **Multi-architecture builds**: Support ARM64 for Apple Silicon
- **Deployment automation**: Auto-deploy to staging/production
- **Slack notifications**: Notify team on successful deployments
- **Image signing**: Use Docker Content Trust for image verification
