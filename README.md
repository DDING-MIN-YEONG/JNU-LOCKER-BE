## JNU-LOCKER, 전남대학교 사물함 신청 서비스

![](https://velog.velcdn.com/images/dubu1001/post/417a1d62-273a-4297-970e-61510f382bec/image.png)


🌐 이제는 온라인으로 신청하고 하나의 플랫폼에서 통합 관리하는 사물함 신청 및 관리 서비스 

> **전남대학교 사물함 신청 서비스**는 학생들이 줄 서지 않고도 온라인으로 간편하게 신청할 수 있도록 돕고, 학생회는 효율적으로 사물함을 통합 관리할 수 있게 해주는 플랫폼입니다.

✨ 서비스 바로 가기

[[학생용] 전남대학교 사물함 신청 서비스](https://www.jnu-locker.site/)

테스트 계정: test@jnu.ac.kr(이메일), testpassword123!(비밀번호)

[[위원회용] 전남대학교 사물함 신청 서비스](https://www.jnu-locker.site/committee)

테스트 계정: test@example.com(이메일), abcde12345!(비밀번호)

**목차**

- [서비스 개요](#서비스-개요)
- [서비스 소개](#서비스-소개)
- [주요 특징](#주요-특징)
- [시스템 아키텍처](#시스템-아키텍쳐)
- [문서](#문서)
- [BE 기술 스택](#be-기술-스택)
- [실행 방법](#실행-방법)
- [팀원](#팀원)

## 서비스 개요

기존 전남대학교에서 각 학과와 위원회의 사물함 배정 방식은 사물함 배정일을 공지하여 줄을 세우는 방식으로 진행하였습니다. 하지만 이 과정에서 경로통행방해 및 갈등이 발생하고 현장 대기로 인한 불편함과 비효율성이 있었습니다. 

본 서비스는 해당 점을 개선하기 위해 학생들이 언제 어디서나 간편하게 신청하고 위원회에서 보다 효율적으로 사물함을 관리할 수 있도록 만든 서비스입니다.

## 서비스 소개

전남대학교 사물함 신청 서비스는 학생들이 보다 편리하게 사물함을 신청하고 위원회에서 하나의 플랫폼에서 효율적으로 사물함을 통합 관리할 수 있는 서비스입니다. 


## 주요 특징

### 사물함 신청하기 만들기

이벤트 기본 정보를 설정하고 사물함 신청 참여 학과를 설정함으로써 사물함 신청하기를 만들 수 있습니다.

<p align="center">
  <img src="https://velog.velcdn.com/images/dubu1001/post/3dc39bbe-7bac-4c6a-81ce-c4c873cb73fc/image.png" width="400px">
</p>

### 사물함 신청하기

학생들이 언제 어디서나 편리하게 이용할 수 있도록 접근성을 높이기 위해 반응형 웹으로 구현하였습니다. 

인증 이후 본인의 소속학과가 참여하는 공지사항과 사물함 신청을 확인하고 신청할 수 있습니다.

<table>
  <tr>
    <td style="vertical-align: top;">
      <img src="https://velog.velcdn.com/images/dubu1001/post/dde17006-f3b0-4c33-af92-a1dc3e077832/image.png" alt="Image 1" width="200">
    </td>
    <td style="vertical-align: top;">
      <img src="https://velog.velcdn.com/images/dubu1001/post/30c519e0-2a55-4ad7-be28-3c4e9472109b/image.png" alt="Image 2" width="200">
    </td>
  </tr>
</table>


### 그 외 기능

- 공지사항 작성
- 실시간 사물함 현황 조회
- 사물함 신청 현황 관리
    ![](https://velog.velcdn.com/images/dubu1001/post/ef88b2d4-155d-4bad-a415-a93b7ca91fbf/image.png)
- 위원회 관리자 목록 관리
![](https://velog.velcdn.com/images/dubu1001/post/0c3e9971-3dfd-45e4-9e1e-e63fb2b26d7e/image.png) 


## 인프라 아키텍처

![](https://velog.velcdn.com/images/dubu1001/post/f69cbcfc-8396-4eb7-a966-bfb432fd5f84/image.png)

## 시스템 아키텍쳐

![](https://velog.velcdn.com/images/dubu1001/post/5c93fa82-d80b-489b-9d0a-e957991afa8a/image.png)

## 문서

[API 명세서](https://api.dev.jnu-locker.site/swagger-ui/index.html)

[ERD](https://westzeroright.notion.site/ERD-1a60a6bd00e6801688a8f36ee617e300?source=copy_link)

[학생회 메뉴얼](https://westzeroright.notion.site/1f90a6bd00e6801a900cddea5115f1d1?source=copy_link)

## BE 기술 스택

| Category     | Technology          | 선택 이유                                                                 |
|--------------|---------------------|--------------------------------------------------------------------------|
| Language     | Java 21             | 21 버전부터 경량 스레드인 Virtual Thread를 지원 → 이를 활용하는 방안 모색 |
| Framework    | Spring Boot 3.3.3   | 익숙한 Java 언어로 서버 애플리케이션 개발                               |
|              | Spring Data JPA     | ORM 기술로 DB를 객체지향적으로 다룸                                     |
|              | Spring Security     | 인증/인가에 대한 간편한 설정                                            |
| Database     | MySQL 8.0           | RDB 중 가장 많이 사용되는 MySQL 사용                                    |
|              | Redis               | RefreshToken 및 캐시 저장소로 인메모리 기반의 DB 사용         |
| Infra        | Amazon Web Service  | AWS – 클라우드 환경에서 가용성 높은 서비스 가능                         |
|              | Nginx | Nginx – 리버스 프록시를 통한 SSL/TLS                                 |
| Mail System  | SMTP                | 이메일 인증을 위한 프로토콜                                             |



## 실행 방법

```
git clone https://github.com/DDING-MIN-YEONG/JNU-LOCKER-BE.git

# 환경변수 설정 .env
# docker-compose.yml 파일이 있는 경로에서 실행

docker compose up -d

```

## 팀원

| [강명덕(BE)](https://github.com/Profile-exe) | [심민보(FE)](https://github.com/smb0123) | [서영우(BE)](https://github.com/westzeroright) |
|------------------------------------------|---------------------------------------|---------------------------------------------|
| <img src="https://github.com/user-attachments/assets/871df70d-b8d1-47cb-8ce6-ed29e2d3e9d5" width="100"> | <img src="https://github.com/user-attachments/assets/e73c9af5-3d47-4a66-a2b6-cc7c94af1fc9" width="100"> | <img src="https://github.com/user-attachments/assets/1eb63d83-6bef-493f-9ad0-6bd3eabfcdda" width="100"> |



