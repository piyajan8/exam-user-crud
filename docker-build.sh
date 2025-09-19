#!/bin/bash

echo "Building User API Docker image..."

docker build -t exam-user-crud:latest .

if [ $? -eq 0 ]; then
    echo "✅ Docker image built successfully!"
    echo ""
    echo "To run the container:"
    echo "  docker run -p 8080:8080 exam-user-crud:latest"
    echo ""
    echo "To run in detached mode:"
    echo "  docker run -d -p 8080:8080 --name exam-user-crud user--api:latest"
    echo ""
    echo "To view logs:"
    echo "  docker logs exam-user-crud"
    echo ""
    echo "To stop the container:"
    echo "  docker stop exam-user-crud"
else
    echo "❌ Docker build failed!"
    exit 1
fi
