#!/bin/bash

REPOSITORY=/home/ubuntu/app

echo "> Build 파일 복사"

cp $REPOSITORY/zip/build/libs/*.jar $REPOSITORY/

echo "> 심볼릭 링크 연결(덮어쓰기)"

ln -sf $(ls -tr $REPOSITORY/*.jar | tail -n 1) delibuddy.jar

echo "> 서비스 재시작"

systemctl restart delibuddy.service