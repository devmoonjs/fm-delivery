<img src = "https://github.com/user-attachments/assets/9a43cb63-08cb-4751-8024-ba35b7df96fc">

# 배달 애플리케이션 API

## 1. 프로젝트 개요

이 프로젝트는 배달 애플리케이션의 API를 개발하는 것을 목표로 합니다. 주요 기능은 회원가입 및 로그인, 주문, 리뷰 작성 등 기본적인 배달 앱의 기능과 장바구니, 포인트 시스템과 같은 부가 기능을 포함합니다. 또한, AWS 인프라를 활용한 클라우드 기반의 서비스 제공을 목표로 하고 있으며, Docker 및 Github Actions를 통해 CI/CD 파이프라인을 구축하였습니다.

<br>

## 2. 주요 기술 스택
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white">
<img src="https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white"> <img src="https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white"> 
<img src="https://img.shields.io/badge/RDS-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white"> <br> <img src="https://img.shields.io/badge/elasticache-C925D1?style=for-the-badge&logo=amazonelasticache&logoColor=white">
<img src="https://img.shields.io/badge/githubactions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white"> <img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white"> <br>
<img src="https://img.shields.io/badge/amazons3-569A31?style=for-the-badge&logo=amazons3&logoColor=white"> <img src="https://img.shields.io/badge/amazonec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white"> <br>
<img src="https://img.shields.io/badge/slack-4A154B?style=for-the-badge&logo=slack&logoColor=white"> <img src="https://img.shields.io/badge/notion-000000?style=for-the-badge&logo=notion&logoColor=white">


- **Backend**: SpringBoot
- **Database**: MySQL, Redis (AWS Elastic Cache)
- **Cloud**: AWS S3, EC2
- **CI/CD**: Github Actions
- **Containerization**: Docker


<br>


## 3. 기능별 담당자

<table>
  <tbody>
    <tr>
      <td align="center"><a href="https://github.com/devmoonjs"><img src="https://avatars.githubusercontent.com/u/130039001?v=4"width="100px;" alt=""/><br /><sub><b>팀장 : 문정석 </b></sub></a><br /></td>
      <td align="center"><a href="https://github.com/rexRUBY"><img src="https://avatars.githubusercontent.com/u/87340601?v=4" width="100px;" alt=""/><br /><sub><b>팀원 : 박예서 </b></sub></a><br /></td>
      <td align="center"><a href="https://github.com/Sangmin1999"><img src="https://avatars.githubusercontent.com/u/175578622?v=4" width="100px;" alt=""/><br /><sub><b>팀원 : 이상민 </b></sub></a><br /></td>
      <td align="center"><a href="https://github.com/jay1864"><img src="https://avatars.githubusercontent.com/u/171054334?v=4" width="100px;" alt=""/><br /><sub><b>팀원 : 한지은 </b></sub></a><br /></td>
    </tr>
    
  </tbody>
</table>

| **세부 항목** | **담당자** |
| ------------ | ---------- |
| **테스크코드 30% 이상** | **전원** |
| **회원가입 및 로그인** | **이상민** |
| **가게** | **문정석** |
| **메뉴** | **박예서** |
| **주문** | **한지은** |
| **리뷰** | **박예서** |
| **장바구니** | **한지은** |
| **포인트** | **한지은** |
| **광고** | **이상민** |
| **이미지 저장** | **박예서** |
| **알림** | **문정석** |
| **(AWS) Elastic Cache Redis** | **한지은** |
| **(AWS) S3** | **박예서** |
| **(AWS) EC2** | **문정석** |
| **Docker** | **문정석** |
| **Github Actions** | **문정석** |
| **도메인 구매 및 연결** | **전원** |

<br>


## 4. ERD
<img src="https://teamsparta.notion.site/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F83c75a39-3aba-4ba4-a792-7aefe4b07895%2Fb21d2220-b15f-44b1-9b81-4d874f7faca3%2Fimage.png?table=block&id=fe8d625c-9ccb-4f4f-a36c-7a58d790f14a&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&width=2000&userId=&cache=v2" width = 80%>

<br>

## 5. 아키텍쳐
<img src="https://github.com/user-attachments/assets/5173bcb8-11c0-4f80-83c6-5b02f72f092d"  width = 80%>

<br>
<br>

## 6. API 명세

## 가게 API

- **POST /api/v1/shops/{id}**
  - **기능**: 가게 정보 수정
  - **request**: `ShopUpdateRequestDto`
  - **response**: `ShopResponseDto`
  - **request header**: `Authorization: jwt-token`
  - **response header**: -

- **POST /api/v1/shops**
  - **기능**: 가게 생성
  - **request**: `ShopCreateRequestDto`
  - **response**: `ShopResponseDto`
  - **request header**: `Authorization: jwt-token`
  - **response header**: -

- **GET /api/v1/shops/{id}**
  - **기능**: 가게 단건 조회
  - **request**: -
  - **response**: `ShopResponseDto`
  - **request header**: `Authorization: jwt-token`
  - **response header**: -

- **GET /api/v1/shops**
  - **기능**: 가게 리스트 조회
  - **request**: -
  - **response**: `ShopListResponseDto`
  - **request header**: `Authorization: jwt-token`
  - **response header**: -

- **PUT /api/v1/shops/{id}**
  - **기능**: 가게 삭제
  - **request**: -
  - **response**: -
  - **request header**: `Authorization: jwt-token`
  - **response header**: -

---

## 리뷰 API

- **POST /api/v1/reviews**
  - **기능**: 리뷰 생성
  - **request**: `ReviewRequestDto`
  - **response**: `ReviewResponseDto`
  - **request header**: `Authorization: jwt-token`
  - **response header**: -

- **GET /api/v1/reviews?shop_id={shop_id}**
  - **기능**: 리뷰 리스트 조회
  - **request**: `param: shopId`
  - **response**: -
  - **request header**: `Authorization: jwt-token`
  - **response header**: -

- **GET /api/v1/reviews/{id}**
  - **기능**: 리뷰 단건 조회
  - **request**: -
  - **response**: -
  - **request header**: `Authorization: jwt-token`
  - **response header**: -

- **DELETE /api/v1/review/{id}**
  - **기능**: 리뷰 삭제
  - **request**: -
  - **response**: -
  - **request header**: `Authorization: jwt-token`
  - **response header**: -

---

## 메뉴 API

- **POST /api/v1/menus**
  - **기능**: 메뉴 추가
  - **request**: `MenuRequestDto`
  - **response**: `MenuResponseDto`
  - **request header**: `Authorization: jwt-token`
  - **response header**: -

- **PATCH /api/v1/menus/{menu_id}**
  - **기능**: 메뉴 수정
  - **request**: -
  - **response**: -
  - **request header**: `Authorization: jwt-token`
  - **response header**: -

- **GET /api/v1/menus?shop_id={shop_id}**
  - **기능**: 메뉴 조회
  - **request**: `param: shopId`
  - **response**: -
  - **request header**: `Authorization: jwt-token`
  - **response header**: -

- **PUT /api/v1/menus/{menu_id}**
  - **기능**: 메뉴 삭제
  - **request**: -
  - **response**: -
  - **request header**: `Authorization: jwt-token`
  - **response header**: -

---

## 장바구니 API

- **POST /api/v1/carts**
  - **기능**: 장바구니에 메뉴 추가
  - **request**: `CartSaveRequestDto`
  - **response**: `CartResponseDto`
  - **request header**: `Authorization: jwt-token`
  - **response header**: -

- **GET /api/v1/carts**
  - **기능**: 장바구니 목록 조회
  - **request**: -
  - **response**: `CartResponseDto`
  - **request header**: `Authorization: jwt-token`
  - **response header**: -

- **DELETE /api/v1/carts**
  - **기능**: 장바구니 전체 삭제
  - **request**: -
  - **response**: -
  - **request header**: `Authorization: jwt-token`
  - **response header**: -

---

## 주문 API

- **POST /api/v1/orders**
  - **기능**: 주문 요청
  - **request**: `OrderRequestDto`
  - **response**: `OrderResponseDto`
  - **request header**: `Authorization: jwt-token`
  - **response header**: -

- **PATCH /api/v1/orders/{orderId}/status**
  - **기능**: 사장) 주문 상태 변경
  - **request**: `OrderStatusRequest`
  - **response**: `Long`
  - **request header**: `Authorization: jwt-token`

---


## 알림 API

- **POST /api/v1/admin/notices**
  - **기능**: 사용자 전체 알림 발송
  - **request**: `NoticeCreateRequestDto`
  - **response**: -
  - **request header**: `Authorization: jwt-token`
  - **response header**: 


