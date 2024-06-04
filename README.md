# 🌟 루틴 에이드

<div align="center">
 
 ### _**🍹 개인 및 그룹 루틴 관리 서비스, 루틴에이드 🍹**_
 </div>
<br>

>루틴 에이드는 사용자들이 루틴을 등록하고 일상적인 활동을 
체계적으로 관리하며, 서로 동기부여를 주고 받으며 더 나은 삶을 살도록 돕는 앱 서비스입니다.

<br>

**루틴에이드는 다음과 같은 기능을 제공합니다.** <br>
- 개인 및 그룹 루틴을 등록하고 관리할 수 있으며, 루틴을 수행함으로써 자신의 일상 속 여러 활동과 목표 달성에 집중할 수 있도록 돕습니다.

- 다른 사용자와 함께 루틴을 수행하고 공유함으로써 동기부여를 할 수 있고, 감정 기록과 통계를 통해 사용자는 자신의 루틴 수행을 더욱 효과적으로 관리할 수 있습니다.

<br>

## 🧑‍💻 Developer


|<img width="250px" alt="채은" src="https://avatars.githubusercontent.com/u/109871579?v=4">|
|:----:|
|[이채은](https://github.com/ChaeAg)|

<br>

## ⚖️ Technology Stack
#### Development Stack

<img src="https://img.shields.io/badge/java 17-007396?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"> <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> <img src="https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white"> <img src="https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white">


<br>


## ⛓️ Architectural

![시스템 구성도](https://github.com/RoutineAde/RoutineAde-Backend/assets/109871579/5bb7de58-96f6-4cf5-a8dc-1b6bf7be7a06)


<br>


## 🔧 ERD
![데이터베이스 설계](https://github.com/RoutineAde/RoutineAde-Backend/assets/109871579/c22ae91d-a09e-400a-9ee1-e9ae97898367)


<br>


## 🗂 Folder Structure

```java
RoutineAde
├── java
│   └── org
│       └── routineade
│           └── RoutineAdeServer
│               ├── RoutineAdeServerApplication.java
│               ├── config
│               │   ├── ....
│               │   └── jwt
│               │       ├── ....
│               ├── controller
│               │   ├── ....
│               ├── domain
│               │   └── ....
│               ├── dto
│               │   ├── routine
│               │   │   ├── ....
│               │   └── user
│               │       └── ....
│               ├── repository
│               │   ├── ....
│               └── service
│                   ├── ....
└── resources
    └── application.yml
 ....
```
<br>

## 📝 Git Convention

### Commit Convention

```text
0. 제목과 본문으로만 구성
1. 제목과 본문은 빈 행으로 구분
2. 제목 끝에는 마침표 X
3. 제목은 명령문 사용
4. 제목은 과거형 X
5. 본문은 '무엇과 왜'를 설명
```
**Commit 예시 :**
`[FEAT] 로그인 기능 구현`

|    태그    | 설명                            |
|:--------:|:------------------------------|
|   DOCS   | 문서 작성 및 수정 작업 (README, 템플릿 등) |
|   FEAT   | 새로운 기능 추가 작업                  |
|   FIX    | 에러 및 버그 수정, 기능 수정 작업          |
|  HOTFIX  | 긴급 수정                         |
| REFACTOR | 코드 리팩토링 작업 (버그 수정이나 기능 추가 X)  |
|  RENAME  | 네이밍 변경 (파일명, 변수명 등)           |
|  REMOVE  | 파일 및 코드 삭제                    |
| COMMENT  | 주석 추가                         |
|  CHORE   | 빌드 업무 및 패키지 매니저 수정 작업 등의 작업   |
|  MERGE   | 다른 브랜치 머지 작업                  |
|   TEST   | 테스트 관련 작업                     |
|  STYLE   | 코드 포맷팅                        |

<br>

### Branch Convention

**브랜치명 형식 :** `branchType/#issue`

- **예시 :** `feat/#1`

|   브랜치    | 설명           |
|:--------:|:-------------|
|   main   | 실제 프로덕트 브랜치  |
|   dev    | 신규 버전 개발 브랜치 |
|   feat   | 기능 구현 브랜치    |
|   fix    | 기능 수정 브랜치    |
| refactor | 리팩토링 브랜치     |
|  bugfix  | 버그 수정 브랜치    |

<!-- ## 패키지 구조 -->

</div>
</details>
