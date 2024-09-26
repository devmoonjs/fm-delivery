<img src = "https://github.com/user-attachments/assets/9a43cb63-08cb-4751-8024-ba35b7df96fc">

# 배달 애플리케이션 API

## 1. 프로젝트 개요

이 프로젝트는 배달 애플리케이션의 API를 개발하는 것을 목표로 합니다. 주요 기능은 회원가입 및 로그인, 주문, 리뷰 작성 등 기본적인 배달 앱의 기능과 장바구니, 포인트 시스템과 같은 부가 기능을 포함합니다. 또한, AWS 인프라를 활용한 클라우드 기반의 서비스 제공을 목표로 하고 있으며, Docker 및 Github Actions를 통해 CI/CD 파이프라인을 구축하였습니다.

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


## 4. ERD
<img src="https://teamsparta.notion.site/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F83c75a39-3aba-4ba4-a792-7aefe4b07895%2Fb21d2220-b15f-44b1-9b81-4d874f7faca3%2Fimage.png?table=block&id=fe8d625c-9ccb-4f4f-a36c-7a58d790f14a&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&width=2000&userId=&cache=v2" width = 80%>

## 5. 아키텍쳐
<img src="https://github.com/user-attachments/assets/5173bcb8-11c0-4f80-83c6-5b02f72f092d"  width = 80%>

## 5. API 명세
## API 명세서

### 1. 가게 API
## API 명세서

### 1. 가게 API

| method | 기능             | URL                | request             | response             | request header            | response header |
|--------|------------------|--------------------|---------------------|----------------------|---------------------------|-----------------|
| POST   | 가게 정보 수정    | /api/v1/shops/{id} | ShopUpdateRequestDto | ShopResponseDto       | Authorization : jwt-token | -               |
| POST   | 가게 생성         | /api/v1/shops      | ShopCreateRequestDto | ShopResponseDto       | Authorization : jwt-token | -               |
| GET    | 가게 단건 조회    | /api/v1/shops/{id} | -                   | ShopResponseDto       | Authorization : jwt-token | -               |
| GET    | 가게 리스트 조회  | /api/v1/shops      | -                   | ShopListResponseDto   | Authorization : jwt-token | -               |
| PUT    | 가게 삭제         | /api/v1/shops/{id} | -                   | -                    | Authorization : jwt-token | -               |

### 2. 리뷰 API

| method | 기능             | URL                                  | request             | response            | request header            | response header |
|--------|------------------|--------------------------------------|---------------------|---------------------|---------------------------|-----------------|
| POST   | 리뷰 생성         | /api/v1/reviews                      | ReviewRequestDto     | ReviewResponseDto    | Authorization : jwt-token | -               |
| GET    | 리뷰 리스트 조회  | /api/v1/reviews?shop_id={shop_id}    | param : shopId       | -                   | Authorization : jwt-token | -               |
| GET    | 리뷰 단건 조회    | /api/v1/reviews/{id}                 | -                   | -                   | Authorization : jwt-token | -               |
| DELETE | 리뷰 삭제         | /api/v1/review/{id}                  | -                   | -                   | Authorization : jwt-token | -               |

### 3. 메뉴 API

| method | 기능             | URL                                  | request             | response            | request header            | response header |
|--------|------------------|--------------------------------------|---------------------|---------------------|---------------------------|-----------------|
| POST   | 메뉴 추가         | /api/v1/menus                        | MenuRequestDto       | MenuResponseDto      | Authorization : jwt-token | -               |
| PATCH  | 메뉴 수정         | /api/v1/menus/{menu_id}              | -                   | -                   | Authorization : jwt-token | -               |
| GET    | 메뉴 조회         | /api/v1/menus?shop_id={shop_id}      | param : shopId       | -                   | Authorization : jwt-token | -               |
| PUT    | 메뉴 삭제         | /api/v1/menus/{menu_id}              | -                   | -                   | Authorization : jwt-token | -               |

### 4. 장바구니 API

| method | 기능               | URL                 | request             | response            | request header            | response header |
|--------|--------------------|---------------------|---------------------|---------------------|---------------------------|-----------------|
| POST   | 장바구니에 메뉴 추가 | /api/v1/carts        | CartSaveRequestDto   | CartResponseDto      | Authorization : jwt-token | -               |
| GET    | 장바구니 목록 조회  | /api/v1/carts        | -                   | CartResponseDto      | Authorization : jwt-token | -               |
| DELETE | 장바구니 전체 삭제  | /api/v1/carts        | -                   | -                   | Authorization : jwt-token | -               |

### 5. 주문 API

| method | 기능               | URL                                   | request                | response               | request header            | response header |
|--------|--------------------|---------------------------------------|------------------------|------------------------|---------------------------|-----------------|
| POST   | 주문 요청           | /api/v1/orders                        | OrderRequestDto         | OrderResponseDto        | Authorization : jwt-token | -               |
| PATCH  | 사장) 주문 상태 변경 | /api/v1/orders/{orderId}/status       | OrderStatusRequest      | Long                   | Authorization : jwt-token | -               |
| PATCH  | 사장) 주문 수락     | /api/v1/orders/{orderId}              | -                      | Long                   | Authorization : jwt-token | -               |
| GET    | 주문 목록 조회      | /api/v1/orders                        | -                      | OrderListResponseDto    | Authorization : jwt-token | -               |
| GET    | 주문 상세 조회      | /api/v1/orders/{orderId}              | -                      | OrderDetailResponseDto  | Authorization : jwt-token | -               |

### 6. 회원가입/로그인 API

| method | 기능               | URL                  | request             | response         | request header            | response header |
|--------|--------------------|----------------------|---------------------|-----------------|---------------------------|-----------------|
| POST   | 로그인              | /api/v1/auth/login   | LoginRequestDto      | Bearer {Token}  | Authorization : jwt-token | -               |
| POST   | 회원가입            | /api/v1/auth/signup  | SigninRequestDto     | -               | Authorization : jwt-token | -               |
| PUT    | 회원 탈퇴           | /api/v1/signout      | SignOutRequestDto    | -               | Authorization : jwt-token | -               |

### 7. 포인트 API

| method | 기능               | URL                         | request                | response            | request header            | response header |
|--------|--------------------|----------------------------|------------------------|---------------------|---------------------------|-----------------|
| POST   | 포인트 사용         | 주문 시 사용            | -                   | -                   | Authorization : jwt-token | -               |
| POST   | 포인트 적립         | 주문 완료 시 자동 적립   | -                   | -                   | Authorization : jwt-token | -               |

### 8. 광고 API

| method | 기능               | URL                   | request             | response            | request header            | response header |
|--------|--------------------|-----------------------|---------------------|---------------------|---------------------------|-----------------|
| POST   | 광고 생성           | /api/v1/ads           | AdSaveRequestDto     | AdResponseDto        | Authorization : jwt-token | -               |
| PUT    | 광고 업데이트        | /api/v1/ads/{adId}    | AdChangeRequestDto   | AdResponseDto        | Authorization : jwt-token | -               |

### 9. 알림 API

| method | 기능                 | URL                          | request                   | response            | request header            | response header |
|--------|----------------------|------------------------------|---------------------------|---------------------|---------------------------|-----------------|
| POST   | 사용자 전체 알림 발송   | /api/v1/admin/notices         | NoticeCreateRequestDto     | -                   | Authorization : jwt-token | -               |


