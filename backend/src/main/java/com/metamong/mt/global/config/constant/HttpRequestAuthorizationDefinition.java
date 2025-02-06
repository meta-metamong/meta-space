package com.metamong.mt.global.config.constant;

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
            "/api/members/reissue"
		);
	
    private static final Map<HttpMethod, String[]> WHITE_LIST = Map.of(
            HttpMethod.POST, new String[] {
                    "/api/members/consumer",
                    "/api/members/provider",
                    "/api/members/login",
                    "/api/members/find-member",
                    "/api/members/dup-email"
            },
            HttpMethod.GET, new String[] {
                    "/api/members/reissue",
                    "/api/members/*",
                    "/api/members/dup-id/*",
                    "/api/members/*/reservations",
                    "/api/reservations/*",
                    "/api/test"
            },
            HttpMethod.PUT, new String[] {
                    "/api/members",
                    "/api/members/password"
            }
    );
    
    private static final String[] WHITE_LIST_FOR_ALL_METHOD = {
            "/ws/**"
    };
    
    private static final Map<Role, Map<HttpMethod, String[]>> AUTHORIZATION_LIST = Map.of(
    );
    
    private static final Map<Role, String[]> AUTHORIZATION_LIST_FOR_ALL_METHOD = Map.of(
            Role.ROLE_ADMN, new String[] { "/file/**" } // TODO: what?
    );
    
    public static final void defineRequestMatcher(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        WHITE_LIST.forEach((method, paths) -> {
            registry.requestMatchers(method, paths).permitAll();
        });
        registry.requestMatchers(WHITE_LIST_FOR_ALL_METHOD).permitAll();
        
        // TODO: hasAnyRole을 사용할지 hasRole을 사용할지 케이스에 따라 다르게 선택하기
        AUTHORIZATION_LIST.forEach((role, pathsPerMethod) -> {
            pathsPerMethod.forEach((method, paths) -> {
                registry.requestMatchers(method, paths)
                        .hasAnyRole(role.role());
            });
        });
        
        AUTHORIZATION_LIST_FOR_ALL_METHOD.forEach((role, paths) -> {
            registry.requestMatchers(paths).hasAnyRole(role.role());
        });
        
        registry.anyRequest().authenticated();
    }
}
