<img src = "https://github.com/user-attachments/assets/9a43cb63-08cb-4751-8024-ba35b7df96fc">

# 배달 애플리케이션 API

## 1. 프로젝트 개요

이 프로젝트는 배달 애플리케이션의 API를 개발하는 것을 목표로 합니다. 주요 기능은 회원가입 및 로그인, 주문, 리뷰 작성 등 기본적인 배달 앱의 기능과 장바구니, 포인트 시스템과 같은 부가 기능을 포함합니다. 또한, AWS 인프라를 활용한 클라우드 기반의 서비스 제공을 목표로 하고 있으며, Docker 및 Github Actions를 통해 CI/CD 파이프라인을 구축하였습니다.

## 2. 주요 기술 스택
- **Backend**: SpringBoot
- **Database**: MySQL, Redis (AWS Elastic Cache)
- **Cloud**: AWS S3, EC2
- **CI/CD**: Github Actions
- **Containerization**: Docker

## 3. 필수 기능 및 담당자
| 기능 | 세부 항목 | 담당자 |
| --- | --- | --- |
| **필수기능** | 0. 테스트코드 30% 이상 | 전원 |
| | 1. 회원가입 및 로그인 | 이상민 |
| | 2. 가게 | 문정석 |
| | 3. 메뉴 | 박예서 |
| | 4. 주문 | 한지은 |
| | 5. 리뷰 | 박예서 |
| **도전기능** | 1. 장바구니 | 한지은 |
| | 2. 포인트 | 한지은 |
| | 3. 광고 | 이상민 |
| | 4. 이미지 저장 | 박예서 |
| | 5. 알림 | 문정석 |
| **차별화** | (AWS) Elastic Cache Redis | 한지은 |
| | (AWS) S3 | 박예서 |
| | (AWS) EC2 | 문정석 |
| | Docker | 문정석 |
| | Github Actions | 문정석 |
| | 도메인 구매 및 연결 | 문정석 |

## 4. ERD
<img src="https://teamsparta.notion.site/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F83c75a39-3aba-4ba4-a792-7aefe4b07895%2Fb21d2220-b15f-44b1-9b81-4d874f7faca3%2Fimage.png?table=block&id=fe8d625c-9ccb-4f4f-a36c-7a58d790f14a&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&width=2000&userId=&cache=v2">

## 5. API 명세
[API 명세 바로가기](https://teamsparta.notion.site/1dc232604f6a4238850efab3272da8ca?v=b67e9b089f934b6fb0a8701b95d7ce54&pvs=4)
