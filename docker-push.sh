#!/bin/bash

# Script to build and push Docker image to Docker Hub
# Usage: ./docker-push.sh <dockerhub-username> [version-tag]

set -e  # Exit on error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo -e "${RED}Error: Docker is not running${NC}"
    exit 1
fi

# Get Docker Hub username
DOCKERHUB_USERNAME="${1:-}"
if [ -z "$DOCKERHUB_USERNAME" ]; then
    echo -e "${RED}Error: Docker Hub username is required${NC}"
    echo "Usage: ./docker-push.sh <dockerhub-username> [version-tag]"
    echo "Example: ./docker-push.sh myusername 1.0.0"
    exit 1
fi

# Get version tag (default to latest)
VERSION_TAG="${2:-latest}"

# Image names (use docker.io registry explicitly)
IMAGE_NAME="lc"
FULL_IMAGE_NAME="docker.io/${DOCKERHUB_USERNAME}/${IMAGE_NAME}:${VERSION_TAG}"
LATEST_IMAGE_NAME="docker.io/${DOCKERHUB_USERNAME}/${IMAGE_NAME}:latest"

echo -e "${GREEN}Building application JAR locally...${NC}"
./gradlew clean bootJar -x test

# Check if JAR was built successfully
if [ ! -f build/libs/*.jar ]; then
    echo -e "${RED}Error: JAR file not found in build/libs/${NC}"
    exit 1
fi

echo -e "${GREEN}Setting up multi-platform builder...${NC}"
# Create buildx builder if it doesn't exist
if ! docker buildx inspect multiplatform-builder > /dev/null 2>&1; then
    docker buildx create --name multiplatform-builder --use
else
    docker buildx use multiplatform-builder
fi

# Ensure logged into Docker Hub
echo -e "${YELLOW}Logging into Docker Hub...${NC}"
if ! docker login docker.io; then
    echo -e "${RED}Failed to login to Docker Hub${NC}"
    exit 1
fi

echo -e "${GREEN}Building and pushing multi-platform Docker image...${NC}"
echo "Image: $FULL_IMAGE_NAME"
echo "Platforms: linux/amd64, linux/arm64"

# Build tags
BUILD_TAGS="-t $FULL_IMAGE_NAME"
if [ "$VERSION_TAG" != "latest" ]; then
    BUILD_TAGS="$BUILD_TAGS -t $LATEST_IMAGE_NAME"
fi

# Build and push multi-platform image using buildx
docker buildx build \
    --platform linux/amd64,linux/arm64 \
    -f Dockerfile.simple \
    $BUILD_TAGS \
    --push \
    .

echo -e "${GREEN}Success! Image pushed to Docker Hub${NC}"
echo "Image: $FULL_IMAGE_NAME"
if [ "$VERSION_TAG" != "latest" ]; then
    echo "Also available as: $LATEST_IMAGE_NAME"
fi
echo ""
echo -e "${GREEN}To pull this image:${NC}"
echo "  docker pull $FULL_IMAGE_NAME"
echo ""
echo -e "${GREEN}To run this image:${NC}"
echo "  docker run -p 8080:8080 $FULL_IMAGE_NAME"
