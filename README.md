# WAK Game

> 실시간 대규모 선착순 웹 게임 🔫

![main](https://github.com/annyoon/wak-game-frontend/assets/79207743/1b4c68a8-21f1-41fb-b7c8-d980ed1718e4)

<br/>

## 💻 프로젝트 개요

### 서비스 이름

**WAK Game** - Win Alive with Clicks

### 프로젝트 멤버

윤서안(FE), 곽민우(BE), 김라연(BE), 김이현(BE), 김한슬(BE), 조형준(BE)

### 프로젝트 기간

2024.04.09 - 2024.05.19 (6주)

> **기획 및 설계** 2024.04.09 - 2024.04.19 (2주)
>
> **기능 구현** 2024.04.22 - 2024.05.19 (4주)

### 배포 사이트

[wakgame.com](https://wakgame.com/)

<br/>

## ✨ 서비스 소개

WAK Game은 선착순 웹 게임 서비스로 **많은 인원**이 매우 **간단한 규칙**으로 참여할 수 있다는 것이 특징입니다!

### 기획 의도

인플루언서 또는 어떤 개인이 추첨 이벤트를 진행하려고 할 때 인터넷 방송 플랫폼이나 카카오톡에서 제공하는 기능을 주로 사용합니다. 하지만 이는 단순히 랜덤 또는 선착순 방식이기 때문에 재미 요소가 부족하다고 생각했습니다.

따라서 인플루언서의 시청자들, 또는 개개인이 **능동적으로 재미있게 참여할 수 있는 추첨 이벤트 서비스**를 기획하고자 했고, 여기서 새로운 게임을 통한 방법을 생각해냈습니다.

### 게임 방법

1. 게임이 시작되면 참가한 플레이어들의 이름과 도트가 화면에 랜덤으로 보이게 됩니다.
    - '닉네임 가리기' 모드를 선택할 경우 플레이어 이름이 표시되지 않으며 상대를 특정할 수 없습니다.

2. 마우스로 플레이어의 도트를 클릭하여 상대를 제거할 수 있습니다.

3. 플레이어 한 명이 남으면 라운드가 종료되며 게임은 총 3라운드로 진행됩니다.

4. 상대를 가장 많이 제거한 플레이어가 우승합니다.

### 기능

- 별도의 **회원가입 없이 닉네임을 입력**하여 로그인할 수 있습니다.

- 닉네임마다 랜덤으로 고유한 색깔을 부여받고 이는 화면에 계속 표시됩니다.

- 로비, 대기실, 게임 화면에서 **채팅**을 할 수 있습니다.

- 로비 화면에서 생성된 방들을 확인하고 입장할 수 있습니다.

- 호스트는 방 제목, 인원, 비밀 방 여부를 입력하고 방을 생성할 수 있습니다.

- 방에 입장하면 대기실 화면으로 이동하며 **참가한 플레이어 목록을 확인**할 수 있습니다.

- 호스트는 대기실에서 **닉네임 가리기 모드** 여부, 1라운드의 **도발 멘트**를 입력할 수 있습니다.

- 도발 멘트는 게임이 진행되는 동안 상단에 보여지며, 1라운드를 제외하고 각 라운드의 우승자가 입력할 수 있습니다.

- 대기실에서 게임 방법을 확인할 수 있고 호스트가 게임 시작 버튼을 누르면 5초의 카운트다운 후 게임이 시작됩니다.

- 게임이 진행되면 **킬로그**에서 누가 누구를 공격했는지 실시간으로 확인할 수 있습니다.

- **실시간 랭킹**을 제공하여 누가 순위권에 오르는지 바로 확인할 수 있습니다.

- 하나의 라운드가 종료되면 게임 시간, 생존 시간, 라운드 랭킹, 제거한 플레이어 수, 나를 제거한 플레이어의 닉네임 등의 **결과를 확인**할 수 있습니다.

- 결과 확인 화면에서 30초의 **카운트다운 후 다음 라운드가 자동으로 시작**됩니다.

- 게임이 종료되면 **최종 결과를 확인**할 수 있습니다.

<br/>

## 🌈 스크린샷

|![시작](https://github.com/user-attachments/assets/36a41c7e-9c5e-43d8-9012-e8a658de2392)|![전체채팅](https://github.com/user-attachments/assets/69e7e915-be06-44eb-b554-e1c8e0b0d995)|
|:---:|:---:|
| 시작 화면 및 닉네임 입력 | 게임 로비 및 전체 채팅 |

|![방만들기](https://github.com/user-attachments/assets/501d8ade-ae55-4df0-8a27-b498459a76b1)|![게임방법](https://github.com/user-attachments/assets/f13cc059-f943-4e74-9a83-80605f20c545)|
|:---:|:---:|
| 방 만들기 | 방(대기실)에서 게임 방법 확인 |

|![방입장](https://github.com/user-attachments/assets/1f89b42a-4140-4e92-853a-1ab7e9fdced5)|![방입장_참여자시점](https://github.com/user-attachments/assets/ba833492-71cc-440c-bb5c-57223eaf7790)|
|:---:|:---:|
| 방 입장(방장 시점) | 방 입장(참가자 시점) |

|![방채팅](https://github.com/user-attachments/assets/e8afcfce-b444-429b-9748-14a3d1cfb22f)|![게임시작](https://github.com/user-attachments/assets/780239c0-950f-4fab-a0a8-d1198f7fa44c)|
|:---:|:---:|
| 방 채팅 | 게임 시작 |

|![게임진행](https://github.com/user-attachments/assets/53c4b1f4-7d56-45d9-b183-20a9b9762bf8)|![게임진행_죽음](https://github.com/user-attachments/assets/e224c367-ad3f-4143-b27b-19ac307e39ba)|
|:---:|:---:|
| 게임 진행 화면(생존) | 게임 진행 화면(죽음) |

|![라운드결과](https://github.com/user-attachments/assets/350051d6-37bb-4907-89fc-287efa46f4ef)|![닉네임가리기모드](https://github.com/user-attachments/assets/cc30b4c0-3b5b-4a04-b86c-4ba127a830f0)|
|:---:|:---:|
| 라운드 결과 화면 | 닉네임 가리기 모드 |

<br/>

## 🌞 멤버 소개

### 프로필

| [![윤서안](https://github.com/annyoon.png)](https://github.com/annyoon) | [![조형준](https://github.com/ryuu9505.png)](https://github.com/ryuu9505) | [![곽민우](https://github.com/minwoo-kwak.png)](https://github.com/minwoo-kwak) | [![김라연](https://github.com/fkdusrh.png)](https://github.com/fkdusrh) | [![김이현](https://github.com/olnuyh.png)](https://github.com/olnuyh) | [![김한슬](https://github.com/slcloe.png)](https://github.com/slcloe) |
|:---:|:---:|:---:|:---:|:---:|:---:|
| 윤서안 <br/> Front-end | 조형준 <br/> 팀장 / Back-end | 곽민우 <br/> Back-end | 김라연 <br/> Back-end | 김이현 <br/> Back-end | 김한슬 <br/> Back-end |

### 역할

| 주요 기능 | 설명 | FE 담당 | BE 담당 | INFRA 담당 |
| :---- | :------------ | :-------- | :-------- | :-------- |
| 인증 | JWT 기반 인증 | 🐹윤서안 | 🦝김라연 <br/> 🐱김한슬 | 🐵곽민우 |
| 게임 | 웹소켓 기반 실시간 게임 | 🐹윤서안 | 🦝김라연 <br/> 🐱김한슬 | 🐵곽민우 |
| 채팅 | Redis Pub/Sub 기반 채팅 | 🐹윤서안 | 🐷조형준 <br/> 🐰김이현 | 🐵곽민우 |

<br/>

## 🛠 기술 스택

| 구분 | 기술 |
| :-- | :-------- |
| Frontend | ![TypeScript](https://img.shields.io/badge/typescript-%23007ACC.svg?style=for-the-badge&logo=typescript&logoColor=white) ![React](https://img.shields.io/badge/react-%2320232a.svg?style=for-the-badge&logo=react&logoColor=%2361DAFB) ![Styled Components](https://img.shields.io/badge/styled--components-DB7093?style=for-the-badge&logo=styled-components&logoColor=white)
| Backend | ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white) ![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white) ![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white) ![MariaDB](https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white) ![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white) ![MongoDB](https://img.shields.io/badge/MongoDB-%234ea94b.svg?style=for-the-badge&logo=mongodb&logoColor=white)
| Infra | ![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white) ![Nginx](https://img.shields.io/badge/nginx-%23009639.svg?style=for-the-badge&logo=nginx&logoColor=white) ![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white) ![Kubernetes](https://img.shields.io/badge/kubernetes-%23326ce5.svg?style=for-the-badge&logo=kubernetes&logoColor=white) ![Ubuntu](https://img.shields.io/badge/Ubuntu-E95420?style=for-the-badge&logo=ubuntu&logoColor=white) ![Jenkins](https://img.shields.io/badge/jenkins-%232C5263.svg?style=for-the-badge&logo=jenkins&logoColor=white)
| Mangement Tool | ![Jira](https://img.shields.io/badge/jira-%230A0FFF.svg?style=for-the-badge&logo=jira&logoColor=white) ![GitLab](https://img.shields.io/badge/gitlab-%23181717.svg?style=for-the-badge&logo=gitlab&logoColor=white) ![Notion](https://img.shields.io/badge/Notion-%23000000.svg?style=for-the-badge&logo=notion&logoColor=white) ![Figma](https://img.shields.io/badge/figma-%23F24E1E.svg?style=for-the-badge&logo=figma&logoColor=white)

<br/>

## 📃 산출물

### 시스템 아키텍쳐

![architecture](https://github.com/annyoon/wak-game-frontend/assets/79207743/de845a10-47ff-4755-85de-c73a9cbe10e9)

### 외부 문서

- [프로젝트 관리 페이지 | Notion](https://ritzy-doom-b84.notion.site/2024-04-09-ing-1666b82f3b1743dea5bdd5cadb70d23a?pvs=4)

- [화면 설계 | Figma](https://www.figma.com/design/n4P98ORpCAIpDXfny4mWdt/Untitled?node-id=0-1&t=EKuxFhTQti5T6gin-1)

<br/>
