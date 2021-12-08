#!/bin/bash

REPOSITORY=/home/ubuntu/app

echo "> Build 파일 복사"

cp $REPOSITORY/zip/build/libs/*.jar $REPOSITORY/

echo "> 서비스 재시작"

systemctl restart delibuddy.service

