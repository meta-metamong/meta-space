package com.metamong.mt.testutil;

import static org.mockito.ArgumentMatchers.matches;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Simple {

    @Test
    void test() {
        String sql = """
                INSERT INTO "HR"."MEMBER" 
                  (MEM_ID, BIRTH_DATE, GENDER, IS_DEL, CREATED_AT, MEM_BANNED_UNTIL, UPDATED_AT, EMAIL, MEM_ADDRESS, MEM_DETAIL_ADDRESS, MEM_NAME, MEM_PHONE, MEM_POSTAL_CODE, PASSWORD, REFRESH_TOKEN, ROLE)
                VALUES 
                  (1, TO_DATE('1985-06-15', 'YYYY-MM-DD'), 'M', 'N', SYSDATE, NULL, NULL, 'admin1@example.com', '서울특별시 강남구', '테헤란로 10', '김철수', '010-1111-2222', '06001', 'password1', NULL, 'ROLE_ADMN');
                
                INSERT INTO "HR"."MEMBER" 
                  (MEM_ID, BIRTH_DATE, GENDER, IS_DEL, CREATED_AT, MEM_BANNED_UNTIL, UPDATED_AT, EMAIL, MEM_ADDRESS, MEM_DETAIL_ADDRESS, MEM_NAME, MEM_PHONE, MEM_POSTAL_CODE, PASSWORD, REFRESH_TOKEN, ROLE)
                VALUES 
                  (2, TO_DATE('1992-03-22', 'YYYY-MM-DD'), 'W', 'N', SYSDATE, NULL, NULL, 'user2@example.com', '부산광역시 해운대구', '센텀중앙로 20', '이영희', '010-2222-3333', '10120', 'password2', NULL, 'ROLE_CONS');
                
                INSERT INTO "HR"."MEMBER" 
                  (MEM_ID, BIRTH_DATE, GENDER, IS_DEL, CREATED_AT, MEM_BANNED_UNTIL, UPDATED_AT, EMAIL, MEM_ADDRESS, MEM_DETAIL_ADDRESS, MEM_NAME, MEM_PHONE, MEM_POSTAL_CODE, PASSWORD, REFRESH_TOKEN, ROLE)
                VALUES 
                  (3, TO_DATE('1990-12-05', 'YYYY-MM-DD'), 'M', 'N', SYSDATE, NULL, NULL, 'user3@example.com', '대구광역시 수성구', '범어동 35', '박민수', '010-3333-4444', '20230', 'password3', NULL, 'ROLE_PROV');
                
                INSERT INTO "HR"."MEMBER" 
                  (MEM_ID, BIRTH_DATE, GENDER, IS_DEL, CREATED_AT, MEM_BANNED_UNTIL, UPDATED_AT, EMAIL, MEM_ADDRESS, MEM_DETAIL_ADDRESS, MEM_NAME, MEM_PHONE, MEM_POSTAL_CODE, PASSWORD, REFRESH_TOKEN, ROLE)
                VALUES 
                  (4, TO_DATE('1988-07-11', 'YYYY-MM-DD'), 'W', 'N', SYSDATE, NULL, NULL, 'user4@example.com', '인천광역시 연수구', '송도동 50', '최은정', '010-4444-5555', '03001', 'password4', NULL, 'ROLE_CONS');
                
                INSERT INTO "HR"."MEMBER" 
                  (MEM_ID, BIRTH_DATE, GENDER, IS_DEL, CREATED_AT, MEM_BANNED_UNTIL, UPDATED_AT, EMAIL, MEM_ADDRESS, MEM_DETAIL_ADDRESS, MEM_NAME, MEM_PHONE, MEM_POSTAL_CODE, PASSWORD, REFRESH_TOKEN, ROLE)
                VALUES 
                  (5, TO_DATE('1995-09-30', 'YYYY-MM-DD'), 'M', 'N', SYSDATE, NULL, NULL, 'user5@example.com', '광주광역시 북구', '운암동 15', '한도현', '010-5555-6666', '00001', 'password5', NULL, 'ROLE_PROV');
                
                INSERT INTO "HR"."MEMBER" 
                  (MEM_ID, BIRTH_DATE, GENDER, IS_DEL, CREATED_AT, MEM_BANNED_UNTIL, UPDATED_AT, EMAIL, MEM_ADDRESS, MEM_DETAIL_ADDRESS, MEM_NAME, MEM_PHONE, MEM_POSTAL_CODE, PASSWORD, REFRESH_TOKEN, ROLE)
                VALUES 
                  (6, TO_DATE('1983-02-17', 'YYYY-MM-DD'), 'W', 'N', SYSDATE, NULL, NULL, 'user6@example.com', '대전광역시 유성구', '장동 22', '오민지', '010-6666-7777', '20230', 'password6', NULL, 'ROLE_CONS');
                
                INSERT INTO "HR"."MEMBER" 
                  (MEM_ID, BIRTH_DATE, GENDER, IS_DEL, CREATED_AT, MEM_BANNED_UNTIL, UPDATED_AT, EMAIL, MEM_ADDRESS, MEM_DETAIL_ADDRESS, MEM_NAME, MEM_PHONE, MEM_POSTAL_CODE, PASSWORD, REFRESH_TOKEN, ROLE)
                VALUES 
                  (7, TO_DATE('1998-11-23', 'YYYY-MM-DD'), 'M', 'N', SYSDATE, NULL, NULL, 'user7@example.com', '울산광역시 남구', '삼산동 8', '서동현', '010-7777-8888', '06001', 'password7', NULL, 'ROLE_PROV');
                
                INSERT INTO "HR"."MEMBER" 
                  (MEM_ID, BIRTH_DATE, GENDER, IS_DEL, CREATED_AT, MEM_BANNED_UNTIL, UPDATED_AT, EMAIL, MEM_ADDRESS, MEM_DETAIL_ADDRESS, MEM_NAME, MEM_PHONE, MEM_POSTAL_CODE, PASSWORD, REFRESH_TOKEN, ROLE)
                VALUES 
                  (8, TO_DATE('1979-05-04', 'YYYY-MM-DD'), 'W', 'N', SYSDATE, NULL, NULL, 'user8@example.com', '세종특별자치시', '고운동 99', '김지영', '010-8888-9999', '10120', 'password8', NULL, 'ROLE_CONS');
                
                INSERT INTO "HR"."MEMBER" 
                  (MEM_ID, BIRTH_DATE, GENDER, IS_DEL, CREATED_AT, MEM_BANNED_UNTIL, UPDATED_AT, EMAIL, MEM_ADDRESS, MEM_DETAIL_ADDRESS, MEM_NAME, MEM_PHONE, MEM_POSTAL_CODE, PASSWORD, REFRESH_TOKEN, ROLE)
                VALUES 
                  (9, TO_DATE('1993-04-16', 'YYYY-MM-DD'), 'M', 'N', SYSDATE, NULL, NULL, 'user9@example.com', '경기도 성남시', '분당구 정자동 12', '이준호', '010-9999-0000', '20230', 'password9', NULL, 'ROLE_PROV');
                
                INSERT INTO "HR"."MEMBER" 
                  (MEM_ID, BIRTH_DATE, GENDER, IS_DEL, CREATED_AT, MEM_BANNED_UNTIL, UPDATED_AT, EMAIL, MEM_ADDRESS, MEM_DETAIL_ADDRESS, MEM_NAME, MEM_PHONE, MEM_POSTAL_CODE, PASSWORD, REFRESH_TOKEN, ROLE)
                VALUES 
                  (10, TO_DATE('1987-08-08', 'YYYY-MM-DD'), 'W', 'N', SYSDATE, NULL, NULL, 'user10@example.com', '경기도 수원시', '권선구 영통동 5', '박수진', '010-1010-2020', '00001', 'password10', NULL, 'ROLE_CONS');
                
                INSERT INTO "HR"."MEMBER" 
                  (MEM_ID, BIRTH_DATE, GENDER, IS_DEL, CREATED_AT, MEM_BANNED_UNTIL, UPDATED_AT, EMAIL, MEM_ADDRESS, MEM_DETAIL_ADDRESS, MEM_NAME, MEM_PHONE, MEM_POSTAL_CODE, PASSWORD, REFRESH_TOKEN, ROLE)
                VALUES 
                  (11, TO_DATE('1991-01-29', 'YYYY-MM-DD'), 'M', 'N', SYSDATE, NULL, NULL, 'user11@example.com', '충청북도 청주시', '흥덕구 오송동 14', '정민호', '010-2020-3030', '03001', 'password11', NULL, 'ROLE_PROV');
                
                INSERT INTO "HR"."MEMBER" 
                  (MEM_ID, BIRTH_DATE, GENDER, IS_DEL, CREATED_AT, MEM_BANNED_UNTIL, UPDATED_AT, EMAIL, MEM_ADDRESS, MEM_DETAIL_ADDRESS, MEM_NAME, MEM_PHONE, MEM_POSTAL_CODE, PASSWORD, REFRESH_TOKEN, ROLE)
                VALUES 
                  (12, TO_DATE('1980-12-12', 'YYYY-MM-DD'), 'W', 'N', SYSDATE, NULL, NULL, 'user12@example.com', '강원도 춘천시', '후평동 7', '한예린', '010-3030-4040', '06001', 'password12', NULL, 'ROLE_CONS');
                
                INSERT INTO "HR"."MEMBER" 
                  (MEM_ID, BIRTH_DATE, GENDER, IS_DEL, CREATED_AT, MEM_BANNED_UNTIL, UPDATED_AT, EMAIL, MEM_ADDRESS, MEM_DETAIL_ADDRESS, MEM_NAME, MEM_PHONE, MEM_POSTAL_CODE, PASSWORD, REFRESH_TOKEN, ROLE)
                VALUES 
                  (13, TO_DATE('1996-10-10', 'YYYY-MM-DD'), 'M', 'N', SYSDATE, NULL, NULL, 'user13@example.com', '경상북도 포항시', '남구 대송동 30', '최승현', '010-4040-5050', '10120', 'password13', NULL, 'ROLE_PROV');
                
                INSERT INTO "HR"."MEMBER" 
                  (MEM_ID, BIRTH_DATE, GENDER, IS_DEL, CREATED_AT, MEM_BANNED_UNTIL, UPDATED_AT, EMAIL, MEM_ADDRESS, MEM_DETAIL_ADDRESS, MEM_NAME, MEM_PHONE, MEM_POSTAL_CODE, PASSWORD, REFRESH_TOKEN, ROLE)
                VALUES 
                  (14, TO_DATE('1989-03-03', 'YYYY-MM-DD'), 'W', 'N', SYSDATE, NULL, NULL, 'user14@example.com', '전라북도 전주시', '완산구 서신동 40', '이수진', '010-5050-6060', '20230', 'password14', NULL, 'ROLE_CONS');
                
                INSERT INTO "HR"."MEMBER" 
                  (MEM_ID, BIRTH_DATE, GENDER, IS_DEL, CREATED_AT, MEM_BANNED_UNTIL, UPDATED_AT, EMAIL, MEM_ADDRESS, MEM_DETAIL_ADDRESS, MEM_NAME, MEM_PHONE, MEM_POSTAL_CODE, PASSWORD, REFRESH_TOKEN, ROLE)
                VALUES 
                  (15, TO_DATE('1975-07-07', 'YYYY-MM-DD'), 'M', 'N', SYSDATE, NULL, NULL, 'user15@example.com', '충청남도 천안시', '동남구 성정동 2', '김영수', '010-6060-7070', '03001', 'password15', NULL, 'ROLE_PROV');
                
                INSERT INTO "HR"."MEMBER" 
                  (MEM_ID, BIRTH_DATE, GENDER, IS_DEL, CREATED_AT, MEM_BANNED_UNTIL, UPDATED_AT, EMAIL, MEM_ADDRESS, MEM_DETAIL_ADDRESS, MEM_NAME, MEM_PHONE, MEM_POSTAL_CODE, PASSWORD, REFRESH_TOKEN, ROLE)
                VALUES 
                  (16, TO_DATE('1994-02-14', 'YYYY-MM-DD'), 'W', 'N', SYSDATE, NULL, NULL, 'user16@example.com', '제주특별자치도 제주시', '한림읍 18', '박지은', '010-7070-8080', '06001', 'password16', NULL, 'ROLE_CONS');
                
                INSERT INTO "HR"."MEMBER" 
                  (MEM_ID, BIRTH_DATE, GENDER, IS_DEL, CREATED_AT, MEM_BANNED_UNTIL, UPDATED_AT, EMAIL, MEM_ADDRESS, MEM_DETAIL_ADDRESS, MEM_NAME, MEM_PHONE, MEM_POSTAL_CODE, PASSWORD, REFRESH_TOKEN, ROLE)
                VALUES 
                  (17, TO_DATE('1982-11-30', 'YYYY-MM-DD'), 'M', 'N', SYSDATE, NULL, NULL, 'user17@example.com', '서울특별시 종로구', '세종대로 100', '송재훈', '010-8080-9090', '10120', 'password17', NULL, 'ROLE_PROV');
                
                INSERT INTO "HR"."MEMBER" 
                  (MEM_ID, BIRTH_DATE, GENDER, IS_DEL, CREATED_AT, MEM_BANNED_UNTIL, UPDATED_AT, EMAIL, MEM_ADDRESS, MEM_DETAIL_ADDRESS, MEM_NAME, MEM_PHONE, MEM_POSTAL_CODE, PASSWORD, REFRESH_TOKEN, ROLE)
                VALUES 
                  (18, TO_DATE('1990-05-20', 'YYYY-MM-DD'), 'W', 'N', SYSDATE, NULL, NULL, 'user18@example.com', '부산광역시 동래구', '온천장 25', '홍길동', '010-9090-1010', '20230', 'password18', NULL, 'ROLE_CONS');
                
                INSERT INTO "HR"."MEMBER" 
                  (MEM_ID, BIRTH_DATE, GENDER, IS_DEL, CREATED_AT, MEM_BANNED_UNTIL, UPDATED_AT, EMAIL, MEM_ADDRESS, MEM_DETAIL_ADDRESS, MEM_NAME, MEM_PHONE, MEM_POSTAL_CODE, PASSWORD, REFRESH_TOKEN, ROLE)
                VALUES 
                  (19, TO_DATE('1986-06-06', 'YYYY-MM-DD'), 'M', 'N', SYSDATE, NULL, NULL, 'user19@example.com', '대구광역시 동구', '신암동 40', '오세훈', '010-1010-1111', '00001', 'password19', NULL, 'ROLE_PROV');
                
                INSERT INTO "HR"."MEMBER" 
                  (MEM_ID, BIRTH_DATE, GENDER, IS_DEL, CREATED_AT, MEM_BANNED_UNTIL, UPDATED_AT, EMAIL, MEM_ADDRESS, MEM_DETAIL_ADDRESS, MEM_NAME, MEM_PHONE, MEM_POSTAL_CODE, PASSWORD, REFRESH_TOKEN, ROLE)
                VALUES 
                  (20, TO_DATE('1993-09-09', 'YYYY-MM-DD'), 'W', 'N', SYSDATE, NULL, NULL, 'user20@example.com', '인천광역시 부평구', '부평동 55', '이민정', '010-1111-2223', '03001', 'password20', NULL, 'ROLE_CONS');
                
                INSERT INTO "HR"."MEMBER" 
                  (MEM_ID, BIRTH_DATE, GENDER, IS_DEL, CREATED_AT, MEM_BANNED_UNTIL, UPDATED_AT, EMAIL, MEM_ADDRESS, MEM_DETAIL_ADDRESS, MEM_NAME, MEM_PHONE, MEM_POSTAL_CODE, PASSWORD, REFRESH_TOKEN, ROLE)
                VALUES 
                  (21, TO_DATE('1987-04-04', 'YYYY-MM-DD'), 'M', 'N', SYSDATE, NULL, NULL, 'user21@example.com', '경기도 고양시', '일산동구 장항동 70', '강동원', '010-2222-3334', '06001', 'password21', NULL, 'ROLE_PROV');
                
                INSERT INTO "HR"."MEMBER" 
                  (MEM_ID, BIRTH_DATE, GENDER, IS_DEL, CREATED_AT, MEM_BANNED_UNTIL, UPDATED_AT, EMAIL, MEM_ADDRESS, MEM_DETAIL_ADDRESS, MEM_NAME, MEM_PHONE, MEM_POSTAL_CODE, PASSWORD, REFRESH_TOKEN, ROLE)
                VALUES 
                  (22, TO_DATE('1998-08-08', 'YYYY-MM-DD'), 'W', 'N', SYSDATE, NULL, NULL, 'user22@example.com', '충청북도 충주시', '내수읍 12', '윤아름', '010-3333-4445', '10120', 'password22', NULL, 'ROLE_CONS');
                
                INSERT INTO "HR"."MEMBER" 
                  (MEM_ID, BIRTH_DATE, GENDER, IS_DEL, CREATED_AT, MEM_BANNED_UNTIL, UPDATED_AT, EMAIL, MEM_ADDRESS, MEM_DETAIL_ADDRESS, MEM_NAME, MEM_PHONE, MEM_POSTAL_CODE, PASSWORD, REFRESH_TOKEN, ROLE)
                VALUES 
                  (23, TO_DATE('1984-12-24', 'YYYY-MM-DD'), 'M', 'N', SYSDATE, NULL, NULL, 'user23@example.com', '강원도 원주시', '소초면 3', '백승민', '010-4444-5556', '20230', 'password23', NULL, 'ROLE_PROV');
                
                INSERT INTO "HR"."MEMBER" 
                  (MEM_ID, BIRTH_DATE, GENDER, IS_DEL, CREATED_AT, MEM_BANNED_UNTIL, UPDATED_AT, EMAIL, MEM_ADDRESS, MEM_DETAIL_ADDRESS, MEM_NAME, MEM_PHONE, MEM_POSTAL_CODE, PASSWORD, REFRESH_TOKEN, ROLE)
                VALUES 
                  (24, TO_DATE('1995-11-11', 'YYYY-MM-DD'), 'W', 'N', SYSDATE, NULL, NULL, 'user24@example.com', '경상남도 창원시', '의창구 중앙동 85', '정다은', '010-5555-6667', '00001', 'password24', NULL, 'ROLE_CONS');
                
                INSERT INTO "HR"."MEMBER" 
                  (MEM_ID, BIRTH_DATE, GENDER, IS_DEL, CREATED_AT, MEM_BANNED_UNTIL, UPDATED_AT, EMAIL, MEM_ADDRESS, MEM_DETAIL_ADDRESS, MEM_NAME, MEM_PHONE, MEM_POSTAL_CODE, PASSWORD, REFRESH_TOKEN, ROLE)
                VALUES 
                  (25, TO_DATE('1989-07-07', 'YYYY-MM-DD'), 'M', 'N', SYSDATE, NULL, NULL, 'user25@example.com', '전라남도 여수시', '돌산읍 9', '최민수', '010-6666-7778', '03001', 'password25', NULL, 'ROLE_PROV');
                
                INSERT INTO "HR"."MEMBER" 
                  (MEM_ID, BIRTH_DATE, GENDER, IS_DEL, CREATED_AT, MEM_BANNED_UNTIL, UPDATED_AT, EMAIL, MEM_ADDRESS, MEM_DETAIL_ADDRESS, MEM_NAME, MEM_PHONE, MEM_POSTAL_CODE, PASSWORD, REFRESH_TOKEN, ROLE)
                VALUES 
                  (26, TO_DATE('1992-10-10', 'YYYY-MM-DD'), 'W', 'N', SYSDATE, NULL, NULL, 'user26@example.com', '서울특별시 마포구', '상수동 27', '한지민', '010-7777-8889', '06001', 'password26', NULL, 'ROLE_CONS');
                
                INSERT INTO "HR"."MEMBER" 
                  (MEM_ID, BIRTH_DATE, GENDER, IS_DEL, CREATED_AT, MEM_BANNED_UNTIL, UPDATED_AT, EMAIL, MEM_ADDRESS, MEM_DETAIL_ADDRESS, MEM_NAME, MEM_PHONE, MEM_POSTAL_CODE, PASSWORD, REFRESH_TOKEN, ROLE)
                VALUES 
                  (27, TO_DATE('1981-03-15', 'YYYY-MM-DD'), 'M', 'N', SYSDATE, NULL, NULL, 'user27@example.com', '부산광역시 금정구', '부곡동 44', '이재훈', '010-8888-9990', '10120', 'password27', NULL, 'ROLE_PROV');
                
                INSERT INTO "HR"."MEMBER" 
                  (MEM_ID, BIRTH_DATE, GENDER, IS_DEL, CREATED_AT, MEM_BANNED_UNTIL, UPDATED_AT, EMAIL, MEM_ADDRESS, MEM_DETAIL_ADDRESS, MEM_NAME, MEM_PHONE, MEM_POSTAL_CODE, PASSWORD, REFRESH_TOKEN, ROLE)
                VALUES 
                  (28, TO_DATE('1996-12-31', 'YYYY-MM-DD'), 'W', 'N', SYSDATE, NULL, NULL, 'user28@example.com', '대구광역시 서구', '상인동 13', '김수진', '010-9999-0001', '20230', 'password28', NULL, 'ROLE_CONS');
                
                INSERT INTO "HR"."MEMBER" 
                  (MEM_ID, BIRTH_DATE, GENDER, IS_DEL, CREATED_AT, MEM_BANNED_UNTIL, UPDATED_AT, EMAIL, MEM_ADDRESS, MEM_DETAIL_ADDRESS, MEM_NAME, MEM_PHONE, MEM_POSTAL_CODE, PASSWORD, REFRESH_TOKEN, ROLE)
                VALUES 
                  (29, TO_DATE('1983-08-08', 'YYYY-MM-DD'), 'M', 'N', SYSDATE, NULL, NULL, 'user29@example.com', '인천광역시 계양구', '계산동 60', '박재현', '010-0000-1112', '00001', 'password29', NULL, 'ROLE_PROV');
                
                INSERT INTO "HR"."MEMBER" 
                  (MEM_ID, BIRTH_DATE, GENDER, IS_DEL, CREATED_AT, MEM_BANNED_UNTIL, UPDATED_AT, EMAIL, MEM_ADDRESS, MEM_DETAIL_ADDRESS, MEM_NAME, MEM_PHONE, MEM_POSTAL_CODE, PASSWORD, REFRESH_TOKEN, ROLE)
                VALUES 
                  (30, TO_DATE('1990-01-01', 'YYYY-MM-DD'), 'W', 'N', SYSDATE, NULL, NULL, 'user30@example.com', '경기도 용인시', '기흥구 구성동 100', '오하영', '010-1111-2224', '03001', 'password30', NULL, 'ROLE_CONS')
                """;
        Pattern pattern = Pattern.compile("password\\d{1,2}");
        Matcher matcher = pattern.matcher(sql);
        
        
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        List<String> raw = new ArrayList<>();
        List<String> encoded = new ArrayList<>();
        String result = matcher.replaceAll((from) -> {
            String g = from.group();
            String str = encoder.encode(g);
            str = str.replaceAll("\\$", "\\\\$");
            raw.add(g);
            encoded.add(str);
            return str;
        });
        System.out.println(result);
    }
}
