package com.metamong.mt.global.config.constant;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;

import com.metamong.mt.domain.member.model.constant.Role;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class HttpRequestAuthorizationDefinition {
	public static final List<String> NO_AUTH_REQUIRED_LIST = List.of(
			"/api/members/consumer",
            "/api/members/provider",
            "/api/members/login",
            "/api/members/reissue",
            "/api/members/send-validation-emails",
            "/api/members/check-email-validation",
            "/api/members/find-password"            
		);
	
    private static final Map<HttpMethod, String[]> WHITE_LIST = Map.of(
            HttpMethod.POST, new String[] {
                    "/api/members/consumer",
                    "/api/members/provider",
                    "/api/members/login",
                    "/api/members/find-member",
                    "/api/members/dup-email",
                    "/api/recommends/*",
                    "/api/members/send-validation-emails",
                    "/api/members/check-email-validation",
                    "/api/members/find-password",
                    "/api/search"
            },
            HttpMethod.GET, new String[] {
                    "/api/members/reissue",
                    "/api/members/*",
                    "/api/members/dup-id/*",
                    "/api/test",
                    "/api/facilities/*",
                    "/api/facilities",
                    "/api/banks",
                    "/api/search/popular"
            },
            HttpMethod.PUT, new String[] {
                    "/api/members",
                    "/api/members/password",
                    "/api/files/*"
            }
    );
    
    private static final String[] WHITE_LIST_FOR_ALL_METHOD = {
            "/ws/**"
    };
    
    // TODO: 역할 별 메소드와 endpoint 설정 중인데, endpoint 별로 역할과 메소드를 지정하게 바꿔야 함.
    // WHY: a 엔드포인트에 GET 방식, 시설 이용자로 설정하고, 밑에서 같은 a 엔드포인트에 GET 방식, 시설 제공자로 설정하면
    //      위에서 a 엔드포인트에 GET 방식, 시설 이용자로 설정한 내용이 사라짐.
    //      밑에서 해당 엔드포인트에 대한 설정을 GET 방식, 시설 제공자로 바꿨기 때문임.
    private static final Map<HttpMethod, Map<String, Role[]>> AUTHORIZATION_LIST = Map.of(
            HttpMethod.GET, Map.of("/api/members/*/reservations", new Role[] { Role.ROLE_CONS, Role.ROLE_PROV },
                                   "/api/members/reservations/*", new Role[] { Role.ROLE_CONS, Role.ROLE_PROV },
                                   "/api/reservations/facilities/*", new Role[] { Role.ROLE_PROV },
                                   "/api/payments", new Role[] { Role.ROLE_CONS, Role.ROLE_PROV },
                                   "/api/payments/*", new Role[] { Role.ROLE_CONS, Role.ROLE_PROV },
                                   "/api/reports", new Role[] { Role.ROLE_ADMN },
                                   "/api/reports/types", new Role[] { Role.ROLE_CONS, Role.ROLE_ADMN }),
            HttpMethod.POST, Map.of("/api/reservations", new Role[] { Role.ROLE_CONS, Role.ROLE_PROV },
                                    "/api/reports", new Role[] { Role.ROLE_CONS, Role.ROLE_PROV }),
            HttpMethod.PUT, Map.of("/api/reservations/*", new Role[] { Role.ROLE_CONS },
                                   "/api/*/refund", new Role[] { Role.ROLE_ADMN },
                                   "/api/reports", new Role[] { Role.ROLE_ADMN })
    );
    
    private static final Map<Role, String[]> AUTHORIZATION_LIST_FOR_ALL_METHOD = Map.of(
            Role.ROLE_ADMN, new String[] { "/file/**" }
    );
    
    public static final void defineRequestMatcher(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        WHITE_LIST.forEach((method, paths) -> {
            registry.requestMatchers(method, paths).permitAll();
        });
        registry.requestMatchers(WHITE_LIST_FOR_ALL_METHOD).permitAll();
        
        // TODO: hasAnyRole을 사용할지 hasRole을 사용할지 케이스에 따라 다르게 선택하기
        AUTHORIZATION_LIST.forEach((method, pathsPerRole) -> {
            pathsPerRole.forEach((path, roles) -> {
                registry.requestMatchers(method, path).hasAnyRole(Arrays.stream(roles).map(Role::role).toArray(String[]::new));
            });
        });
        
        AUTHORIZATION_LIST_FOR_ALL_METHOD.forEach((role, paths) -> {
            registry.requestMatchers(paths).hasAnyRole(role.role());
        });
        
        registry.anyRequest().authenticated();
    }
}
