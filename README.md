# android_study_compose

## 개발 환경
- Android Studio Ladybug Feature Drop | 2024.2.2 Patch 1
- Kotlin 2.0.0
- Gradle 8.10.2

## 요구사항
- Kakao API를 통해 데이터 가져오기
  - 카카오 API 문서 찾아보기
- 2개의 화면이 존재 어떤식으로든 전환되면됨
  - bottomNavigation 사용
  - 서로의 화면으로 탭전환할때 마지막으로 사용하던 데이터 유지하도록 구현
  - 서로의 화면에서 즐겨찾기를 설정하면 다른화면에서도 즐겨찾기가 설정되어있는것을 보여주기
  - 즐겨찾기는 로컬에 저장하기 -> Room 사용
  - 저장할때 createAt는 필수로 넣어주기
  - 즐겨찾기 해제하면 삭제하기
- 1번째 화면(검색화면)
  - 검색할때 이미지검색, 동영상검색 2개를 검색하여 보여주기, 날짜기준으로 정렬
    - api 모델은 따로 만들고 entity는 상속을 통해 공통으로 사용(같은 list에 담자)
    - 네트워크 통신시 combine을 사용하여 동시에 요청하고 결과를 받아오기
    - 검색결과를 보여줄때는 paging을 사용하여 무한스크롤 구현
    - 20개씩 가져오자(페이지네이션)
  - 한번 조회한검색어는 5분간 캐싱하기
    - retrofit에서 @Header 어노테이션으로 캐싱처리
- 2번째화면(즐겨찾기 화면)
  - 즐겨찾기를 설정한 순서대로 보여주기
    - 정렬은 createAt으로 정렬해서 보여주기
    - Room과 연동해서 flow로 화면에 그려주기!
    - 보관한 이미지 리스트는 앱 재시작시에도 보여야함

### 추가사항
- 검색시 필터링 옵션있으면 확인해보기

## 앱 구조
### App 의존성
- 모듈 의존성
  - Core
  - EntityModel
  - UI 

## 모듈 의존성

### Core
- NONE
---
### Data
- DataModel
- Domain
---
### DataModel
- NONE
---
### Domain
- EntityModel
### EntityModel
- NONE
---
### DI
- Data
- DataModel
- Domain
- EntityModel
---
