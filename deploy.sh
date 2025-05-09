#!/bin/bash
echo -e "배포 스크립트 시작\n\n"

# .env 파일 로드
echo "환경 변수 로드 중..."
if [ -f ".env" ]; then
    export $(grep -v '^#' .env | xargs)
    echo ".env 파일 로드 완료"
else
    echo ".env 파일이 존재하지 않습니다"
    exit 1
fi

# prometheus.yml 생성
echo "prometheus.yml 생성 중..."
if envsubst < ./config/prometheus.yml.tmpl > ./config/prometheus.yml; then
    echo "prometheus.yml 생성 완료"
else
    echo "prometheus.yml 생성 실패"
    exit 1
fi

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
