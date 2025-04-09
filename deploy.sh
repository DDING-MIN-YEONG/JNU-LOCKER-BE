#!/bin/bash
echo -e "배포 스크립트 시작\n\n"

# WAS 이미지 pull
echo "WAS 이미지 pull"
docker compose pull was

# 도커 컴포즈 실행
echo "Docker Compose 실행 중..."
if docker compose up -d; then
    echo -e "Docker Compose 실행 완료\n\n"
else
    echo -e "Docker Compose 실행 실패\n\n"
    exit 1
fi

echo "배포 스크립트 종료"
