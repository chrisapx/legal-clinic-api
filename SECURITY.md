# Security Configuration Guide

This document explains how to securely configure the Legal Clinic Knowledge Engine Adapter application.

## Environment Variables

The application requires several environment variables to be set. **NEVER commit sensitive credentials to version control.**

### Required Environment Variables

#### Database Configuration
```bash
LC_DB_URL=jdbc:mariadb://localhost:3306/lc-kw
LC_DB_USER=root
LC_DB_PASSWORD=your_database_password
```

#### Email Configuration
```bash
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_gmail_app_password
```

**Note:** For Gmail, you need to use an App Password, not your regular password. Create one at: https://myaccount.google.com/apppasswords

#### OpenAI API Configuration
```bash
OPENAI_API_KEY=sk-proj-your_openai_api_key
```

Get your API key from: https://platform.openai.com/api-keys

#### Tebi S3 Storage Configuration
```bash
TEBI_ACCESS_KEY=your_tebi_access_key
TEBI_SECRET_KEY=your_tebi_secret_key
TEBI_BUCKET=lc-bucket
```

#### Frontend URL (Optional)
```bash
FRONTEND_URL=http://localhost:3000
```

## Setup Methods

### Method 1: Using .env File (Local Development)

1. Copy the example file:
   ```bash
   cp .env.example .env
   ```

2. Edit `.env` and fill in your actual credentials:
   ```bash
   nano .env  # or use your preferred editor
   ```

3. The `.env` file is already in `.gitignore` and will NOT be committed

4. Load the environment variables before running:
   ```bash
   # Linux/Mac
   export $(cat .env | xargs)
   ./gradlew bootRun

   # Or use a tool like direnv
   direnv allow
   ./gradlew bootRun
   ```

### Method 2: Export Environment Variables

```bash
export LC_DB_PASSWORD="your_password"
export MAIL_PASSWORD="your_gmail_app_password"
export OPENAI_API_KEY="sk-proj-your_key"
export TEBI_ACCESS_KEY="your_access_key"
export TEBI_SECRET_KEY="your_secret_key"

./gradlew bootRun
```

### Method 3: IDE Configuration

**IntelliJ IDEA:**
1. Run → Edit Configurations
2. Select your Spring Boot run configuration
3. Environment Variables → Add each variable
4. Apply and run

**VS Code:**
1. Create `.vscode/launch.json`
2. Add env section:
   ```json
   {
     "configurations": [{
       "type": "java",
       "env": {
         "LC_DB_PASSWORD": "your_password",
         "OPENAI_API_KEY": "sk-proj-your_key"
       }
     }]
   }
   ```

### Method 4: Docker

```bash
docker run -p 8080:8080 \
  -e LC_DB_PASSWORD="your_password" \
  -e MAIL_PASSWORD="your_gmail_app_password" \
  -e OPENAI_API_KEY="sk-proj-your_key" \
  -e TEBI_ACCESS_KEY="your_access_key" \
  -e TEBI_SECRET_KEY="your_secret_key" \
  lc-kw-engine-adapter:latest
```

Or use a `.env` file with Docker:
```bash
docker run -p 8080:8080 --env-file .env lc-kw-engine-adapter:latest
```

### Method 5: Production Deployment

For production, use your platform's secrets management:

**AWS:**
- Use AWS Secrets Manager or Parameter Store
- Reference secrets in ECS task definitions or Elastic Beanstalk

**Kubernetes:**
- Create Kubernetes Secrets
- Mount as environment variables in pod specs

**Heroku:**
```bash
heroku config:set OPENAI_API_KEY=sk-proj-your_key
heroku config:set LC_DB_PASSWORD=your_password
```

**Azure:**
- Use Azure Key Vault
- Configure App Service application settings

## Security Best Practices

### 1. Never Commit Secrets
- The `.gitignore` already excludes `.env` files
- Use `.env.example` for documentation only (with placeholder values)
- Review code before committing to ensure no secrets are included

### 2. Rotate Credentials Regularly
- Change database passwords quarterly
- Rotate API keys every 90 days
- Update email app passwords if compromised

### 3. Limit Access
- Use principle of least privilege
- Create database users with minimal required permissions
- Use read-only API keys where possible

### 4. Validate in CI/CD
- GitHub's push protection will block commits with secrets
- Never bypass these warnings
- If secrets are exposed, immediately rotate them

### 5. Monitor for Leaks
- Enable GitHub secret scanning
- Use tools like git-secrets or truffleHog
- Regularly audit git history

## What to Do If Secrets Are Exposed

If you accidentally commit secrets:

1. **Immediately rotate the exposed credentials**
   - Change database passwords
   - Revoke and create new API keys
   - Update all deployment environments

2. **Remove from git history** (see instructions below)

3. **Notify your team**

4. **Monitor for unauthorized access**

## Removing Secrets from Git History

If secrets were already committed, you need to remove them from history:

```bash
# WARNING: This rewrites git history!
# Coordinate with your team before doing this

# Install git-filter-repo
brew install git-filter-repo  # Mac
# or
pip install git-filter-repo

# Remove sensitive files from all commits
git filter-repo --path src/main/resources/application.yaml --invert-paths --force
git filter-repo --path src/main/resources/application-test.yaml --invert-paths --force

# Or use BFG Repo-Cleaner for easier cleanup
brew install bfg  # Mac
bfg --replace-text passwords.txt  # file with secret patterns
git reflog expire --expire=now --all
git gc --prune=now --aggressive

# Force push (DANGER: coordinate with team!)
git push origin --force --all
git push origin --force --tags
```

**Important:** After rewriting history:
- All team members must re-clone the repository
- Any forks or copies will still contain the secrets
- Consider rotating the secrets anyway to be safe

## Testing Configuration

Verify your configuration is working:

```bash
# Run the application
./gradlew bootRun

# Check logs for successful connections
# You should see:
# - Database connection established
# - OpenAI client initialized
# - No authentication errors
```

## Troubleshooting

**Application fails to start:**
- Check all required environment variables are set
- Verify credentials are correct
- Check network connectivity to external services

**Database connection fails:**
- Verify `LC_DB_PASSWORD` is set correctly
- Check database is running: `mysql -u root -p`
- Verify database exists: `SHOW DATABASES;`

**OpenAI requests fail:**
- Verify `OPENAI_API_KEY` is valid
- Check API key hasn't expired or been revoked
- Ensure you have API credits available

## Additional Resources

- [Spring Boot Externalized Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config)
- [GitHub Secret Scanning](https://docs.github.com/en/code-security/secret-scanning)
- [OWASP Secrets Management Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Secrets_Management_Cheat_Sheet.html)
