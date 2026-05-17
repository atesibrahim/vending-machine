#!/bin/bash

JAVA_HOME_PATH="/Users/tciates/Library/Java/JavaVirtualMachines/corretto-17.0.18/Contents/Home"
PID_FILE="app.pid"

if [ -f "$PID_FILE" ]; then
    PID=$(cat "$PID_FILE")
    if kill -0 "$PID" 2>/dev/null; then
        echo "Application is already running with PID $PID"
        exit 1
    fi
    rm -f "$PID_FILE"
fi

echo "Starting Vending Machine Application..."
JAVA_HOME="$JAVA_HOME_PATH" ./mvnw spring-boot:run &
MAVEN_PID=$!

# Wait for the Spring Boot JVM child process to appear
echo "Waiting for application to start..."
for i in $(seq 1 30); do
    JVM_PID=$(pgrep -f "VendingMachineApplication" 2>/dev/null | head -1)
    if [ -n "$JVM_PID" ]; then
        echo "$JVM_PID" > "$PID_FILE"
        echo "Application started. PID: $JVM_PID"
        echo "Swagger UI: http://localhost:8090/swagger-ui/index.html"
        echo "H2 Console: http://localhost:8090/h2-console"
        exit 0
    fi
    sleep 1
done

echo "Application did not start in time. Check logs."
kill "$MAVEN_PID" 2>/dev/null
exit 1
