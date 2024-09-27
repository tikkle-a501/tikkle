package com.taesan.tikkle.global.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Spring Security 이용 시 인증된 이용자의 UUID 식별자(username)를 파라미터로 받아옴.
 * 어노테이션 이용 시 UUID로 파라미터 타입을 설정해야 합니다.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthedUsername {
}
