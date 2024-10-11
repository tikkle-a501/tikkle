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
    ssl_certificate /etc/letsencrypt/live/j11a501.p.ssafy.io/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/j11a501.p.ssafy.io/privkey.pem;
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_prefer_server_ciphers on;
    ssl_ciphers ECDHE-ECDSA-AES256-GCM-SHA384:ECDHE-RSA-AES256-GCM-SHA384;

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

