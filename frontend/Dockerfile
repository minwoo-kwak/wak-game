# 단계 1: Node.js 환경에서 React 앱 빌드
FROM node:20.11.1 AS build-stage

WORKDIR /app

# 프로젝트 의존성 파일 복사
COPY package.json yarn.lock ./

# 프로젝트 의존성 설치
RUN yarn install --frozen-lockfile

# 애플리케이션 소스 코드 복사
COPY . .

# React 애플리케이션 빌드
RUN yarn build

# 문서화 목적 포트
EXPOSE 3000

# 단계 2: 프로덕션 환경을 위한 서브 이미지
FROM node:20.11.1 AS production-stage

WORKDIR /app

# 빌드 단계에서 생성된 빌드 파일 복사
COPY --from=build-stage /app/build /app/build

# 앱 구동
CMD ["npx", "serve", "-s", "build", "-l", "3000"]
