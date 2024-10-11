# Tikkle

## production

### 사전 설정

배포 과정에서 letsencrypt와 docker(docker compose), nginx를 이용하므로 ec2 인스턴스에 먼저 설치해주세요.

### 설정 파일

볼륨 폴더와 이미지 저장 폴더는 미리 생성되어 있어야합니다.

```yaml
services:
  mariadb:
    image: mariadb:10.5
    container_name: mariadb
    restart: always
    environment:
      MARIADB_ROOT_PASSWORD: ${your-password}
      MARIADB_DATABASE: tikkle
      MARIADB_USER: myuser
      MARIADB_PASSWORD: ${your-password}
    ports:
      - "3306:3306"
    volumes:
      - mariadb_data:/var/lib/mysql
    networks:
      - tikkle-backend

  mm-db:
    image: mariadb:10.5
    container_name: mm-db
    restart: always
    environment:
      MARIADB_ROOT_PASSWORD: ${your-password}
      MARIADB_DATABASE: mattermost
      MARIADB_USER: mmuser
      MARIADB_PASSWORD: ${your-password}
    ports:
      - "3307:3306"
    volumes:
      - mm_db_data:/var/lib/mysql
    networks:
      - tikkle-backend

  mongodb:
    image: mongo:4.4
    container_name: mongodb
    restart: always
    environment:
      MONGO_INITDB_ROOT_USERNAME: mongoadmin
      MONGO_INITDB_ROOT_PASSWORD: ${your-password}
      MONGO_INITDB_DATABASE: mymongodb
    ports:
      - "27017:27017"
    volumes:
      - /home/ubuntu/mongodb_data:/data/db
      - ./mongod.conf:/etc/mongod.conf.orig
    networks:
      - tikkle-backend

  redis:
    image: redis:6.0
    container_name: redis
    restart: always
    ports:
      - "6379:6379"
    networks:
      - tikkle-backend

  zookeeper:
    image: confluentinc/cp-zookeeper:7.2.1
    container_name: zookeeper
    environment:
      ZOOKEEPER_CLIENT_PORT: 42181
      ZOOKEEPER_TICK_TIME: 500
    ports:
      - "42181:42181"
    networks:
      - tikkle-backend

  kafka:
    image: confluentinc/cp-kafka:7.2.1
    container_name: kafka
    depends_on:
      - zookeeper
    ports:
      - "8092:8092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:42181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://127.0.0.1:8092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
      KAFKA_LOG4J_LOGGERS: "kafka.controller=DEBUG,kafka.producer.async.DefaultEventHandler=DEBUG,state.change.logger=TRACE"
    networks:
      - tikkle-backend

  mattermost:
    image: mattermost/mattermost-team-edition:latest
    restart: unless-stopped
    container_name: mattermost
    ports:
      - "8065:8065"
    environment:
      - MM_SQLSETTINGS_DRIVERNAME=mysql
      - MM_SQLSETTINGS_DATASOURCE=mmuser:7e1O0zQD11FFJ4n9dCli7GAc@tcp(mm-db:3306)/mattermost?charset=utf8mb4&readTimeout=30s&writeTimeout=30s
      - MM_SERVICESETTINGS_SITEURL=${site-url}
      - MM_SERVICESETTINGS_ENABLEOUTGOINGWEBHOOKS=true
      - MM_SERVICESETTINGS_ENABLEINCOMINGWEBHOOKS=true
      - MM_SERVICESETTINGS_ENABLESIGNUPWITHEMAIL=true
      - MM_SERVICESETTINGS_ENABLEAPIv4=true
      - MM_SERVICESETTINGS_ENABLECUSTOMEMOJI=true
      - MM_SERVICESETTINGS_ENABLEPOSTUSERNAMEOVERRIDE=true
      - MM_SERVICESETTINGS_ENABLEPOSTICONOVERRIDE=true
      - MM_PLUGINSETTINGS_ENABLE=true
      - MM_PLUGINSETTINGS_ENABLEUPLOADS=true
    volumes:
      - mattermost-volume:/mattermost/data
    depends_on:
      - mm-db
    networks:
      - tikkle-backend

networks:
  tikkle-backend:
    name: tikkle-backend
    driver: bridge

volumes:
  mariadb_data:
  mm_db_data:
  mongodb_data:
  mattermost-volume:
```

- application-prod.yaml

```yaml
server:
  port: 8080

file:
  upload:
    image-dir: /home/ubuntu/tikkle-resources/images/profiles

spring:
  http:
    encoding:
      charset: UTF-8
      enabled: true
      force: true
  datasource:
    url: jdbc:mariadb://127.0.0.1:3306/tikkle
    username: myuser
    password: ${mariadb-password}
    driver-class-name: org.mariadb.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    database-platform: org.hibernate.dialect.MariaDBDialect

  security:
    oauth2:
      client:
        registration:
          mattermost:
            client-id: ${client-id}
            client-secret: ${client-secret}
            scope: profile,email
            authorization-grant-type: authorization_code
            redirect-uri: "https://j11a501.p.ssafy.io/login/oauth2/code/mattermost"
            # client-name: Mattermost
            client-authentication-method: client_secret_post
        provider:
          mattermost:
            authorization-uri: https://j11a501.p.ssafy.io/oauth/authorize
            token-uri: https://j11a501.p.ssafy.io/mattermost/oauth/access_token
            user-info-uri: https://j11a501.p.ssafy.io/mattermost/api/v4/users/me
            user-name-attribute: id


  data:
    mongodb:
      uri: mongodb://mongoadmin:${mongodb-password}@127.0.0.1:27017/mymongodb?authSource=admin
    redis:
      host: localhost
      port: 6379
      password: '' # 비밀번호 설정 필요 시 추가
  kafka:
    bootstrap-servers: 127.0.0.1:8092
    consumer:
      group-id: my-group
      properties:
        spring.json.trusted.packages: "*"
      auto-offset-reset: earliest
      enable-auto-commit: true
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
      properties:
        linger.ms: 1 # 네트워크 과부하 시 해당 값을 올려 배치 처리량을 늘림
logging:
  level:
    root: INFO
    com:
      taesan:
        tikkle: DEBUG
      fasterxml.jackson.databind: DEBUG
      # org.apache.kafka: DEBUG
      # kafka: DEBUG
      # org.springframework.kafka: DEBUG
      # kafka.server.KafkaConfig: DEBUG
    org.springframework.security: DEBUG
    org.springframework.data.mongodb.core.MongoTemplate: DEBUG
    org.springframework.data.mongodb.core.MongoOperations: DEBUG


```

- mongod.conf
```
# Where and how to store data.
storage:
  dbPath: /var/lib/mongodb
  journal:
    enabled: true

# where to write logging data.
systemLog:
  destination: file
  logAppend: true
  path: /var/log/mongodb/mongod.log

# network interfaces
net:
  port: 27017  # 포트를 8017로 변경
  bindIp: 0.0.0.0  # 외부에서 접근 가능하도록 설정
  ipv6: true

# how the process runs
processManagement:
  timeZoneInfo: /usr/share/zoneinfo
```

### NginX

- j11.a501.p.ssafy.io

해당 파일을 nginx.conf에 임포트해주세요.

```
server {
    listen 80;
    server_name j11a501.p.ssafy.io;

    # HTTP를 HTTPS로 리다이렉트
    return 301 https://$host$request_uri;
}

server {
    listen 443 ssl;
    server_name j11a501.p.ssafy.io;
            
    # SSL 인증서 경로 설정 (Let's Encrypt 예시)
    ssl_certificate ${your-cert-path}
    ssl_certificate_key ${your-cert-key}
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_prefer_server_ciphers on;
    ssl_ciphers ${your-ssl-cipher}

    # /api/v1 경로는 백엔드 애플리케이션 서버로 전달
    location /api/v1/ {
        proxy_pass http://127.0.0.1:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    
	proxy_pass_request_headers on;
        proxy_pass_request_body on;
        proxy_set_header Cookie $http_cookie;  # 클라이언트의 쿠키를 전달
    }

    location /login/oauth2/code/mattermost {
	# 캐시를 비활성화하여 항상 새 요청을 처리하도록 설정
    	add_header Cache-Control "no-cache, no-store, must-revalidate";
    	add_header Pragma "no-cache";
    	add_header Expires 0;

    	proxy_pass http://127.0.0.1:8080;  # Mattermost 서버로 프록시
    	proxy_set_header Host $host;
    	proxy_set_header X-Real-IP $remote_addr;
    	proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    	proxy_set_header X-Forwarded-Proto $scheme;
    
	proxy_pass_request_headers on;
        proxy_pass_request_body on;
        proxy_set_header Cookie $http_cookie;  # 클라이언트의 쿠키를 전달
    }

    location /oauth2/ {
            proxy_pass http://127.0.0.1:8080;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
    }

    location /oauth/authorize {
            proxy_pass http://127.0.0.1:8065;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
    }

    # 웹소켓 관련 경로
    location /ws {
        proxy_pass http://127.0.0.1:8080;
        proxy_set_header Host $host;
	proxy_set_header Content-Type "application/json; charset=UTF-8";
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;

        # WebSocket 설정 (Mattermost 실시간 통신용)
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
    }

    location /app/ {
        proxy_pass http://127.0.0.1:8080;
        proxy_set_header Host $host;
        proxy_set_header Content-Type "application/json; charset=UTF-8";
	proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;

        # WebSocket 설정 (Mattermost 실시간 통신용)
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
    }

    location /topic/ {
        proxy_pass http://127.0.0.1:8080;
        proxy_set_header Host $host;
        proxy_set_header Content-Type "application/json; charset=UTF-8";
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;

        # WebSocket 설정 (Mattermost 실시간 통신용)
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
    }

    location = /mattermost/error {
        if ($arg_type = "team_not_found") {
            return 302 /api/v1/login;
        }

        # 다른 에러 타입에 대한 처리 또는 프록시 설정
        proxy_pass http://127.0.0.1:8065/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    location /mattermost/api/v4/teams/name/mattermost {
	# 요청을 /api/v1/login으로 프록시 패스
        # rewrite ^/mattermost/(.*)$ /$1 break;
	# proxy_pass http://127.0.0.1:8080/api/v1/login;
        
        # 클라이언트의 쿠키와 헤더를 전달
        # proxy_set_header Host $host;
        # proxy_set_header X-Real-IP $remote_addr;
        # proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        # proxy_set_header X-Forwarded-Proto $scheme;

        # 쿠키 전달을 위해 설정
        # proxy_pass_request_headers on;
        # proxy_pass_request_body on;
        # proxy_set_header Cookie $http_cookie;  # 클라이언트의 쿠키를 전달    
    
	error_page 404 = @handle_error;
    }

    location @handle_error {
        return 302 /api/v1/login;  # /api/v1/login으로 리디렉션
    }

    location /mattermost/mattermost/oauth/authorize {
        # 쿼리 스트링과 함께 경로 재작성
        rewrite ^/mattermost/mattermost/oauth/authorize(.*)$ /oauth/authorize$1 break;
        
        # 프록시 패스를 통해 POST 요청 본문(body)와 쿼리 스트링 전달
        proxy_pass http://127.0.0.1:8065;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_set_header Content-Length $content_length;
        proxy_set_header Content-Type $content_type;
    
	proxy_pass_request_headers on;
        proxy_pass_request_body on;
        proxy_set_header Cookie $http_cookie;  # 클라이언트의 쿠키를 전달
   }

     location /mattermost/api/v4 {
        proxy_pass http://127.0.0.1:8065;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;

        # WebSocket 설정 (Mattermost 실시간 통신용)
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
    }


    location /mattermost/ {
        proxy_pass http://127.0.0.1:8065;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;

        # WebSocket 설정 (Mattermost 실시간 통신용)
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";

	proxy_pass_request_headers on;
        proxy_pass_request_body on;
        proxy_set_header Cookie $http_cookie;  # 클라이언트의 쿠키를 전달
    }

    location /jenkins/ {
        proxy_pass http://127.0.0.1:8088/jenkins/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
	proxy_redirect http://localhost:8088/jenkins/ /jenkins/;
    }

    # 그 외 모든 경로는 프론트엔드 서버로 전달
    location / {
        proxy_pass http://127.0.0.1:3000;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    
	proxy_pass_request_headers on;
        proxy_pass_request_body on;
        proxy_set_header Cookie $http_cookie;  # 클라이언트의 쿠키를 전달
    }
}


```

### Mattermost 설정

mattermost siteSetting URL에 접속해 세팅을 진행해주세요.

1. 회원 가입 및 조직 생성
2. System Console 입장
3. Mattermost enterprise로 업그레이드
4. CORS 설정, Open Server (자유로운 가입을 허용할 시에만), OAuth2 로그인 이용 설정
5. Integeration에서 OAuth 2.0 Application 추가
6. `application-prod.yaml`에서 해당 client-id와 client-secret 추가 