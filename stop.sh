#!/bin/bash

PID_FILE="app.pid"

# Resolve PID: from file, or by finding the running JVM directly
if [ -f "$PID_FILE" ]; then
    PID=$(cat "$PID_FILE")
    if ! kill -0 "$PID" 2>/dev/null; then
        echo "Saved PID $PID is no longer running. Searching by process name..."
        PID=$(pgrep -f "VendingMachineApplication" 2>/dev/null | head -1)
    fi
else
    echo "PID file not found. Searching by process name..."
    PID=$(pgrep -f "VendingMachineApplication" 2>/dev/null | head -1)
fi

if [ -z "$PID" ]; then
    echo "Application is not running."
    rm -f "$PID_FILE"
    exit 0
fi

echo "Stopping Vending Machine Application (PID: $PID)..."
kill "$PID"

# Wait up to 10 seconds for graceful shutdown
for i in $(seq 1 10); do
    if ! kill -0 "$PID" 2>/dev/null; then
        echo "Application stopped."
        rm -f "$PID_FILE"
        exit 0
    fi
    sleep 1
done

echo "Forcing shutdown..."
kill -9 "$PID" 2>/dev/null
rm -f "$PID_FILE"
echo "Application forcefully stopped."
