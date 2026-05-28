#!/bin/bash

APP_NAME="AI-RECRUITER"

JAR_NAME="Vendor-0.0.1-SNAPSHOT.jar"

APP_PORT=8080

PROFILE="prod"

LOG_DIR="logs"

LOG_FILE="$LOG_DIR/application.log"

PID_FILE="application.pid"

JAVA_HOME="/usr/lib/jvm/java-21-openjdk-amd64"

# ==========================================
# ENVIRONMENT VARIABLES
# ==========================================

export MONGO_URI="mongodb://localhost:27017/jobs"

export MAIL_USERNAME="mnatikarsagar@gmail.com"
export MAIL_PASSWORD="gvjh ihtt xzxe oook"

export AWS_SECRET_KEY=" nQa43VaxSj6EKlrqhGh1cu8+P/0AtJlTcKkWVRDx"
export AWS_REGION="ap-south-1"
export AWS_BUCKET="offer-letter-bucket18"

export INTERVIEW_BASE_URL="http://localhost:4200/test/"


JAVA_OPTS="
-Xms512m
-Xmx1024m
-Dspring.profiles.active=$PROFILE
-Dserver.port=$APP_PORT
"

echo "========================================="
echo "Deploying $APP_NAME"
echo "========================================="

# ==========================================
# STEP 1 : GIT PULL
# ==========================================

echo "[1/6] Pulling latest code..."

git pull

if [ $? -ne 0 ]; then
    echo "Git pull failed!"
    exit 1
fi

# ==========================================
# STEP 2 : BUILD APPLICATION
# ==========================================

echo "[2/6] Building application..."

chmod +x gradlew

./gradlew clean build -x test

if [ $? -ne 0 ]; then
    echo "Build failed!"
    exit 1
fi

# ==========================================
# STEP 3 : CREATE LOG DIRECTORY
# ==========================================

echo "[3/6] Creating log directory..."

mkdir -p $LOG_DIR

# ==========================================
# STEP 4 : STOP OLD APPLICATION
# ==========================================

echo "[4/6] Stopping old application..."

PID=$(lsof -ti:$APP_PORT)

if [ ! -z "$PID" ]; then

    echo "Stopping process: $PID"

    kill -15 $PID

    sleep 10

    if ps -p $PID > /dev/null; then

        echo "Force killing process..."

        kill -9 $PID
    fi

else

    echo "No application running on port $APP_PORT"

fi

# ==========================================
# STEP 5 : START NEW APPLICATION
# ==========================================

echo "[5/6] Starting application..."

nohup $JAVA_HOME/bin/java $JAVA_OPTS \
-jar build/libs/$JAR_NAME \
> $LOG_FILE 2>&1 &

NEW_PID=$!

echo $NEW_PID > $PID_FILE

sleep 15

# ==========================================
# STEP 6 : VERIFY APPLICATION
# ==========================================

echo "[6/6] Verifying deployment..."

if ps -p $NEW_PID > /dev/null; then

    echo "Application started successfully"

    echo "PID: $NEW_PID"

    if lsof -i:$APP_PORT | grep LISTEN > /dev/null; then

        echo "Application running on port $APP_PORT"

    else

        echo "WARNING: Application not listening on port"

    fi

else

    echo "Application failed to start"

    echo "Checking logs..."

    tail -50 $LOG_FILE

    exit 1

fi

echo ""
echo "========================================="
echo "DEPLOYMENT SUCCESSFUL"
echo "========================================="

echo "Logs: tail -f $LOG_FILE"

echo "Stop App: kill -9 \$(cat $PID_FILE)"