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
    
    // TODO: 역할 별 메소드와 endpoint 설정 중인데, endpoint 별로 역할과 메소드를 지정하게 바꿔야 함.
    // WHY: a 엔드포인트에 GET 방식, 시설 이용자로 설정하고, 밑에서 같은 a 엔드포인트에 GET 방식, 시설 제공자로 설정하면
    //      위에서 a 엔드포인트에 GET 방식, 시설 이용자로 설정한 내용이 사라짐.
    //      밑에서 해당 엔드포인트에 대한 설정을 GET 방식, 시설 제공자로 바꿨기 때문임.
    private static final Map<HttpMethod, String[]> WHITE_LIST = Map.of(
            HttpMethod.POST, new String[] {
                    "/api/members/password",
                    "/api/members/send-validation-emails",
                    "/api/members/provider",
                    "/api/members/logout",
                    "/api/members/login",
                    "/api/members/find-password",
                    "/api/members/consumer",
                    "/api/members/check-email-validation",
                    "/api/recommends/*",
                    "/api/search"
            },
            HttpMethod.GET, new String[] {
                    "/api/categories",
                    "/api/facilities/**",
                    "/api/members/*/facilities",
                    "/api/members/reissue",
                    "/api/banks",
                    "/api/top-facilities",
                    "/api/search/popular"
            },
            HttpMethod.PUT, new String[] {
                    "/api/files/**",
                    "/api/members/password"
            }
    );
    
    private static final Map<HttpMethod, Map<String, Role[]>> AUTHORIZATION_LIST = Map.of(
            HttpMethod.GET, Map.of(
                    "/api/payments/**", new Role[] { Role.ROLE_CONS, Role.ROLE_ADMN },
                    "/api/reports", new Role[] { Role.ROLE_ADMN },
                    "/api/reservations/*", new Role[] { Role.ROLE_CONS, Role.ROLE_PROV },
                    "/api/reservations/facilities/*", new Role[] { Role.ROLE_PROV },
                    "/api/members/*/reservations", new Role[] { Role.ROLE_CONS },
                    "/api/admin/**", new Role[] { Role.ROLE_ADMN }
            ),
            HttpMethod.POST, Map.of(
                    "/api/facilities", new Role[] { Role.ROLE_PROV },
                    "/api/reports", new Role[] { Role.ROLE_CONS },
                    "/api/reservations", new Role[] { Role.ROLE_CONS },
                    "/api/reservations/reportable", new Role[] { Role.ROLE_CONS },
                    "/api/reservations/remain", new Role[] { Role.ROLE_CONS },
                    "/api/admin/**", new Role[] { Role.ROLE_ADMN }
            ),
            HttpMethod.PUT, Map.of(
                    "/api/facilities/*", new Role[] { Role.ROLE_PROV },
                    "/api/reports", new Role[] { Role.ROLE_ADMN },
                    "/api/reservations/*", new Role[] { Role.ROLE_CONS },
                    "/api/admin/**", new Role[] { Role.ROLE_ADMN }
            ),
            HttpMethod.DELETE, Map.of(
                    "/api/facilities/*", new Role[] { Role.ROLE_PROV, Role.ROLE_ADMN },
                    "/api/admin/**", new Role[] { Role.ROLE_ADMN }
            )
    );
    
    private static final Map<HttpMethod, String[]> AUTHENTICATED_LIST = Map.of(
            HttpMethod.GET, new String[] {
                    "/api/members/*",
                    "/api/members/*/unread-notification-count",
                    "/api/members/*/notifications",
                    "/api/reports/types"
            },
            HttpMethod.DELETE, new String[] {
                    "/api/members",
                    "/api/notifications/*"
            },
            HttpMethod.PUT, new String[] {
                    "/api/members"
            },
            HttpMethod.PATCH, new String[] {
                    "/api/notifications/*/read"
            }
    );
    
    public static final void defineRequestMatcher(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        AUTHORIZATION_LIST.forEach((HttpMethod method, Map<String, Role[]> roleByUri) -> {
            roleByUri.forEach((String uri, Role[] roles) -> {
                registry.requestMatchers(method, uri).hasAnyRole(Arrays.stream(roles).map(Role::role).toArray(String[]::new));
            });
        });
        
        WHITE_LIST.forEach((method, uris) -> {
            registry.requestMatchers(method, uris).permitAll();
        });
        
        AUTHENTICATED_LIST.forEach((method, uris) -> {
            registry.requestMatchers(method, uris).authenticated();
        });
        
        registry.anyRequest().authenticated();
    }
}
