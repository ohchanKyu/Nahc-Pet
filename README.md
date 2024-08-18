# Nahc-Pet
## 2024년 문화 디지털혁신 및 문화데이터 활용 본선 진출 작품
### 반려동물과 함께하는 융합 플랫폼 서비스

## 개발 환경
Front : HTML / CSS / JavaScript
Back-end : Java / Maria DB / Redis 
Cloud : CloudType
Framework : Spring boot
Framework Main Dependency
* Jpa
* Redis
* Spring Security
* Thymeleaf

## 외부 데이터 및 API
* Kakao API
Kakao API를 통해 실시간 교통정보 및 관련 블로그 제공
* Google API
Google AI의 Gemini API를 통해 AI 채팅 모델 제공
* 문화 빅데이터 플랫폼
한국문화정보원 전국 반려동물 동반가능 문화시설 위치 데이터
활용 내용
현재 위치 또는 주소를 기반으로 근처에 존재하는 반려동물 동반 가능한 문화시설을 사용자에게 알려주고,
실시간 교통정보 또한 바로 제공한다. 해당 데이터의 위도, 경도 정보와 현재 위치 또는 주소의 좌표 통해 거리 계산 알고리즘을 적용하여
장소 데이터를 거리순으로 정렬하여 사용자에게 장소 정보들을 제공한다.

## 프로젝트 소개
해당 서비스는 반려동물 동반 가능한 장소를 현재 위치 기반으로 제공하며,
또한, 전국 동물 보호센터의 위치와 유기 동물 입양 절차 정보를 제공하고, 
AI 챗봇과 Pet Q&A 서비스를 통해 반려동물 건강 및 관리에 대한 실시간 상담과 커뮤니티 소통 기능을 지원한다.

## 서비스 기능
### 반려동물 동반 가능한 장소 정보 제공
#### 기능
  * 현재 위치 기반으로 근처 반려동물 동반 가능한 장소(카페, 공원, 식당 등) 제공
  * 주소 입력을 통해 해당 주소 근처의 장소 정보 제공
  * 장소별 카테고리 분류 및 상세 정보(주소, 연락처, 영업시간 등) 제공
#### 핵심 기술
  * 반려동물 동반 가능한 장소 데이터 (DBMS)
  * 지리정보 시스템 (GPS)
  * 실시간 교통 정보 API
#### 사용자 인터페이스
  * 현재 위치, 주소 기반, 검색 카테고리 선택 UI
![image](https://github.com/user-attachments/assets/cda206fe-294f-464f-81a6-f6854403c9fb)
  * 현재 위치로 검색한 지도 UI
![image](https://github.com/user-attachments/assets/d0c746b4-162c-4323-82e0-774428c4c358)
  * 현재 위치로 검색한 장소 리스트 UI
![image](https://github.com/user-attachments/assets/e9147555-0c67-42e6-8db7-e7f05466a653)
  * 장소 상세 정보 및 실시간 교통정보 UI
![image](https://github.com/user-attachments/assets/a65305b6-4a36-4d56-9668-55133734d740)
![image](https://github.com/user-attachments/assets/55a1a462-6a2d-4121-be19-0d85a7c985ff)

### 추가적인 장소 정보 및 유실 및 유기동물 입양 절차 안내
#### 기능
  * 현재 위치 기반으로 애견 카페 및 애견 훈련소의 장소 정보를 제공
  * 주소 입력을 통해 해당 애견 카페 및 애견 훈련소의 장소 정보를 제공
  * 애견 관련 블로그를 제공
  * 전국 동물 보호센터 정보 및 유실 및 유기동물 입양 절차 안내
#### 핵심 기술
  * 실시간 블로그 API
  * 지리정보 시스템 (GPS)
  * 실시간 교통 정보 API
  * 전국 동물 보호센터 장소 데이터 (DBMS)
#### 사용자 인터페이스
  * 현재 위치로 검색한 장소 리스트 UI
![image](https://github.com/user-attachments/assets/18b80c77-83eb-47a4-997a-038e64ce4e43)
  * 애견 훈련, 교육 블로그 UI
![image](https://github.com/user-attachments/assets/e276a46d-e9b5-4a4b-b423-c4156128bca5)
  * 전국 동물 보호센터 위치 및 지도 UI
![image](https://github.com/user-attachments/assets/1b5de92a-99f5-488d-9650-e6d18aedb89b)
  * 유실 및 유기동물 입양 절차 관련 카테고리 UI
![image](https://github.com/user-attachments/assets/977107b3-5eb7-44c6-9d20-39c5692ff01b)
  * 유실 및 유기동물 입양 절차 관련 상세 정보 UI
![image](https://github.com/user-attachments/assets/2e6da965-266c-4bcb-955a-ce4398b9f88d)
![image](https://github.com/user-attachments/assets/422f9852-ec0e-4b8e-9a32-e2537523fbaa)
![image](https://github.com/user-attachments/assets/cea3fbfb-74b7-4a80-b989-91fa6db965a4)

### AI 채팅 및 Pet Q&A 서비스
#### 기능
  * 반려동물 건강, 관리 방법 등에 대한 실시간으로 AI에게 질의 가능
  * 사용자 질문에 대한 즉각적인 AI 답변 제공
  * 반려동물 관련 질문과 답변을 주고받을 수 있는 커뮤니티 제공
  * 사용자 간 댓글을 통한 소통
#### 핵심 기술
  * 자연어 처리(NLP)
  * Google Gemini API
  * 커뮤니티 플랫폼 구축 및 댓글 시스템
#### 사용자 인터페이스
  * AI 채팅 서비스 UI
![image](https://github.com/user-attachments/assets/f9d2833a-b99d-4747-b3ea-d9c6ed128438)
![image](https://github.com/user-attachments/assets/3951ba0e-0370-4509-9512-26fa3d45b864)
  * Pet Q&A 메인화면
![image](https://github.com/user-attachments/assets/f527c484-76f8-40c5-9366-8439f20d9f81)
![image](https://github.com/user-attachments/assets/3cc7ba3e-9f62-4af6-88e2-68f9d6b7e9ef)




