# 실습을 위한 개발 환경 세팅
* https://github.com/slipp/web-application-server 프로젝트를 자신의 계정으로 Fork한다. Github 우측 상단의 Fork 버튼을 클릭하면 자신의 계정으로 Fork된다.
* Fork한 프로젝트를 eclipse 또는 터미널에서 clone 한다.
* Fork한 프로젝트를 eclipse로 import한 후에 Maven 빌드 도구를 활용해 eclipse 프로젝트로 변환한다.(mvn eclipse:clean eclipse:eclipse)
* 빌드가 성공하면 반드시 refresh(fn + f5)를 실행해야 한다.

# 웹 서버 시작 및 테스트
* webserver.WebServer 는 사용자의 요청을 받아 RequestHandler에 작업을 위임하는 클래스이다.
* 사용자 요청에 대한 모든 처리는 RequestHandler 클래스의 run() 메서드가 담당한다.
* WebServer를 실행한 후 브라우저에서 http://localhost:8080으로 접속해 "Hello World" 메시지가 출력되는지 확인한다.

# 각 요구사항별 학습 내용 정리
* 구현 단계에서는 각 요구사항을 구현하는데 집중한다. 
* 구현을 완료한 후 구현 과정에서 새롭게 알게된 내용, 궁금한 내용을 기록한다.
* 각 요구사항을 구현하는 것이 중요한 것이 아니라 구현 과정을 통해 학습한 내용을 인식하는 것이 배움에 중요하다. 

### 요구사항 1 - http://localhost:8080/index.html로 접속시 응답
* Java I.O stream , streamReader, bufferReader 에 나만의 정확한 이해를 가지고 있어야 할 것 같다.
* http request가 어떤식으로 들어오는지 확인 할 수 있었다.
* Files.readAllBytes 를 처음 써 보았다.
* File 을 생성할 때 상대경로가 어떻게 쓰였는데 루트를 어디로 보는지 확인이 필요하다.
* java i.o 에 대한 충분한 이해가 부족했던 것 같다.

### 요구사항 2 - get 방식으로 회원가입
* response 를 어떻게 보내야햐는지 학습

### 요구사항 3 - post 방식으로 회원가입
* 본문의 내용이 헤더뒤에 공백을 두고 들어 온다는 것 확인
* 본문의 길이는 요청헤더의 contentLength로 확인가능하다는 것 학습.

### 요구사항 4 - redirect 방식으로 이동
* 리다이렉트의 response 응답코드는 302 Redirect

### 요구사항 5 - cookie
* header에 있는 cookie값 사용 하는 법 학습

### 요구사항 6 - stylesheet 적용
* html을 읽어 들여와 다시 서버로 request를 보낸 다는 것 학습.
* 응답의 reposnse의 content-type을 :text/css 로 해야 한다는 것 학습

### heroku 서버에 배포 후
* 