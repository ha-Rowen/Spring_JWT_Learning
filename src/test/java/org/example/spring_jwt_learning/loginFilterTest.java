package org.example.spring_jwt_learning;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.example.spring_jwt_learning.loginFilter.JWT;
import org.example.spring_jwt_learning.loginFilter.JWTutil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.util.Collections;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
public class loginFilterTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private FilterChain filterChain;

    JWT jwtfilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        // JWT 필터에 Mock AuthenticationManager를 주입하여 객체를 생성합니다.
        jwtfilter = new JWT(authenticationManager,new JWTutil("vmfhaltmskdlstkfkdgodyroqkfwkdbalroqkfwkdbalaaaaaaaaaaaaaaaabbbbb"));

        // 서블릿 Mock 객체를 초기화합니다.
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }
    @Test
    @DisplayName("인증 시도: 요청에서 사용자 이름과 비밀번호를 추출하고 UPAT를 생성하는지 확인")
    void attemptAuthentication_CreatesCorrectTokenAndCallsManager() {
        // Given
        final String TEST_USERNAME = "TestUserName";
        final String TEST_PASSWORD = "TestPassword";

        // 요청에 인증 정보를 설정합니다.
        request.setMethod("POST");
        request.setParameter("username", TEST_USERNAME); // 기본 필터가 사용하는 파라미터 이름
        request.setParameter("password", TEST_PASSWORD); // 기본 필터가 사용하는 파라미터 이름

        // Mock AuthenticationManager가 호출될 때 가짜 성공 인증 객체를 반환하도록 설정합니다.
        // (attemptAuthentication은 인증을 시도하는 역할만 합니다.)
        UserDetails userDetails = new User(TEST_USERNAME, TEST_PASSWORD, Collections.emptyList()); // 권한은 지정하지 않았다.
        Authentication successAuth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        // 인증이 완료되면 pssword 값은 null로 저장되는것이 일반적이다.
        // null은 자격증명, 인증된 사용자가 접근할 수 있는 권하 목록
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(successAuth);
        // authenticationManager.authenticate 메서드는 userdeile 정보를 받아서 처리 해야하는 부분이다. 인증을 해주는 부분이니까
        // 하지만 여기서 즉각적으로 userdeile를 바로 넣어주는건 별로 좋은 선택이 아니다.
        // 때문에 인증된 사용 및 비인증된 사용자를 모두 가지고 있는 Authentication 타입인 successAuth를 넣어주는 것이 올바른 방법이다.
        // 기타 다른 부분은 mock의 문법 적인 부분이 있기 때문에 감안하고 보면 된다.


        // ArgumentCaptor를 사용하여 authenticationManager로 전달되는 토큰을 캡처합니다.
        ArgumentCaptor<Authentication> tokenCaptor = ArgumentCaptor.forClass(Authentication.class);

        // When
        Authentication result = jwtfilter.attemptAuthentication(request, response);

        // Then
        // 1. AuthenticationManager가 정확히 한 번 호출되었는지 검증합니다.
        verify(authenticationManager, times(1)).authenticate(tokenCaptor.capture());

        // 2. 전달된 토큰이 UsernamePasswordAuthenticationToken인지 확인합니다.
        Authentication capturedToken = tokenCaptor.getValue();
        assertThat(capturedToken).isInstanceOf(UsernamePasswordAuthenticationToken.class);

        // 3. 토큰에 예상했던 사용자 이름과 비밀번호가 들어있는지 확인합니다.
        assertThat(capturedToken.getPrincipal()).isEqualTo(TEST_USERNAME);
        assertThat(capturedToken.getCredentials()).isEqualTo(TEST_PASSWORD);

        // 4. 반환된 결과가 Mock 객체가 반환하도록 설정했던 성공 객체인지 확인합니다.
        assertThat(result).isEqualTo(successAuth);
    }

    @Test
    @DisplayName("인증 성공: successfulAuthentication가 호출되었을 때 후속 로직을 처리하는지 확인 (현재는 super 호출)")
    void successfulAuthentication_CallsSuperMethod() throws IOException, ServletException {
        // Given
        Authentication authResult = mock(Authentication.class); // Mock 인증 결과

        // When
        jwtfilter.successfulAuthentication(request, response, filterChain, authResult);

        // Then
        // 현재 코드는 super.successfulAuthentication()만 호출하므로,
        // JWT 발급 등의 커스텀 로직이 들어갈 경우 해당 로직을 검증해야 합니다.
        // 현재는 super 메서드 외에 다른 커스텀 로직은 없으므로 추가 검증은 생략합니다.
        // 만약 여기에 JWT를 생성하고 응답 헤더에 담는 로직이 추가된다면,
        // response.getHeader()를 검증해야 합니다.
    }


    @Test
    @DisplayName("인증 실패: unsuccessfulAuthentication가 호출되었을 때 후속 로직을 처리하는지 확인 (현재는 super 호출)")
    void unsuccessfulAuthentication_CallsSuperMethod() throws IOException, ServletException {
        // Given
        AuthenticationException failed = mock(AuthenticationException.class); // Mock 실패 예외

        // When
        jwtfilter.unsuccessfulAuthentication(request, response, failed);

        // Then
        // 현재 코드는 super.unsuccessfulAuthentication()만 호출하므로,
        // 응답 코드 설정 등의 커스텀 로직이 들어갈 경우 해당 로직을 검증해야 합니다.
        // 예를 들어, 응답 코드를 401로 설정하는 커스텀 로직이 있다면:
        // verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // 현재는 추가 커스텀 로직이 없으므로 검증을 생략합니다.
    }
}



