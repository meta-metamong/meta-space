-- DROP TABLE account CASCADE CONSTRAINT;
-- DROP TABLE additional_info CASCADE CONSTRAINT;
-- DROP TABLE bank CASCADE CONSTRAINT;
-- DROP TABLE category CASCADE CONSTRAINT;
-- DROP TABLE facility CASCADE CONSTRAINT;
-- DROP TABLE fct_provider CASCADE CONSTRAINT;
-- DROP TABLE image CASCADE CONSTRAINT;
-- DROP TABLE member CASCADE CONSTRAINT;
-- DROP TABLE notification CASCADE CONSTRAINT;
-- DROP TABLE payment CASCADE CONSTRAINT;
-- DROP TABLE report CASCADE CONSTRAINT;
-- DROP TABLE reservation CASCADE CONSTRAINT;
-- DROP TABLE zone CASCADE CONSTRAINT;

-- DROP SEQUENCE mem_pk_seq;
-- DROP SEQUENCE noti_pk_seq;
-- DROP SEQUENCE fct_prov_pk_seq;
-- DROP SEQUENCE fct_pk_seq;
-- DROP SEQUENCE cat_pk_seq;
-- DROP SEQUENCE addinfo_pk_seq;
-- DROP SEQUENCE report_pk_seq;
-- DROP SEQUENCE rvt_pk_seq;
-- DROP SEQUENCE pay_pk_seq;
-- DROP SEQUENCE zone_pk_seq;
-- DROP SEQUENCE img_pk_seq;

CREATE SEQUENCE mem_pk_seq        START WITH 31 INCREMENT BY 1;
CREATE SEQUENCE noti_pk_seq       START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE fct_prov_pk_seq   START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE fct_pk_seq        START WITH 31 INCREMENT BY 1;
CREATE SEQUENCE cat_pk_seq        START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE addinfo_pk_seq    START WITH 16 INCREMENT BY 1;
CREATE SEQUENCE report_pk_seq     START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE rvt_pk_seq        START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE pay_pk_seq        START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE zone_pk_seq       START WITH 61 INCREMENT BY 1;
CREATE SEQUENCE img_pk_seq        START WITH 121 INCREMENT BY 1;

CREATE TABLE member (
    mem_id                       NUMBER(19, 0)   NOT NULL,
    email                        VARCHAR2(50)   NOT NULL UNIQUE,
    mem_name                     VARCHAR2(30)   NOT NULL,
    password                     VARCHAR2(80)   NOT NULL,
    mem_phone                    VARCHAR2(15)   NOT NULL,
    birth_date                   DATE           NOT NULL,
    gender                       CHAR(1)        NOT NULL,
    mem_postal_code              CHAR(5)        NOT NULL,
    mem_address                  VARCHAR2(80)   NOT NULL,
    mem_detail_address           VARCHAR2(60)   NOT NULL,
    role                         CHAR(9)        NOT NULL,
    refresh_token                VARCHAR2(1000),
    created_at                   DATE           DEFAULT SYSDATE,
    updated_at                   DATE           DEFAULT SYSDATE,
    mem_banned_until             DATE,
    is_del                       CHAR(1)        DEFAULT 'N',
    
    CONSTRAINT pk_member PRIMARY KEY (mem_id),
    CONSTRAINT member_gender_domain CHECK (gender IN ('M', 'W')),
    CONSTRAINT member_role_domain CHECK (role IN ('ROLE_PROV', 'ROLE_CONS', 'ROLE_ADMN')),
    CONSTRAINT member_is_del_domain CHECK (is_del IN ('Y', 'N'))
);

CREATE TABLE notification (
    noti_id       NUMBER(19, 0),
    receiver_id   NUMBER(19, 0)   NOT NULL,
    noti_msg      VARCHAR2(150)  NOT NULL,
    created_at    DATE           DEFAULT SYSDATE,
    is_read       CHAR(1)        DEFAULT 'N',

    CONSTRAINT pk_notification PRIMARY KEY (noti_id),
    CONSTRAINT fk_noti_receiver_id FOREIGN KEY (receiver_id)
        REFERENCES member (mem_id),
    CONSTRAINT noti_is_read_domain CHECK (is_read IN ('Y', 'N'))
);

CREATE TABLE bank (
    bank_code CHAR(3),
    bank_name VARCHAR2(30) NOT NULL,
    
    CONSTRAINT pk_bank PRIMARY KEY (bank_code)
);

CREATE TABLE fct_provider (
    prov_id            NUMBER(19, 0),
    biz_name           VARCHAR2(30) NOT NULL,
    biz_reg_num        VARCHAR2(30) NOT NULL,

    CONSTRAINT pk_fct_provider PRIMARY KEY (prov_id),
    CONSTRAINT fk_fct_provider_prov_id FOREIGN KEY (prov_id)
        REFERENCES member (mem_id)
);

CREATE TABLE account (
    prov_id            NUMBER(19, 0),
    bank_code          CHAR(3)       NOT NULL,
    account_number     VARCHAR2(20)  NOT NULL,
    balance            NUMBER(10, 0) DEFAULT 0,
    is_agreed_info     CHAR(1)       DEFAULT 'N',

    CONSTRAINT pk_account PRIMARY KEY (prov_id),
    CONSTRAINT fk_account_prov_id FOREIGN KEY (prov_id)
        REFERENCES fct_provider (prov_id),
    CONSTRAINT fk_account_bank_code FOREIGN KEY (bank_code)
        REFERENCES bank (bank_code),
    CONSTRAINT account_is_agreed_info_domain CHECK (is_agreed_info IN ('Y', 'N'))
);

CREATE TABLE report (
    report_id      NUMBER(19, 0),
    reporter_id    NUMBER(19, 0)     NOT NULL,
    reported_id    NUMBER(19, 0)     NOT NULL,
    report_msg     VARCHAR2(255)    NOT NULL,
    report_date    DATE             DEFAULT SYSDATE,
    report_type    VARCHAR2(50)     NOT NULL,
    is_processed   CHAR(1)          DEFAULT 'N',
    
    CONSTRAINT pk_report PRIMARY KEY (report_id),
    CONSTRAINT fk_report_reporter_id FOREIGN KEY (reporter_id)
        REFERENCES member (mem_id),
    CONSTRAINT fk_report_reported_id FOREIGN KEY (reported_id)
        REFERENCES member (mem_id),
    CONSTRAINT report_is_processed_domain CHECK(is_processed IN ('Y', 'N'))
);

CREATE TABLE category (
    cat_id         CHAR(3),
    parent_cat_id  CHAR(3),
    cat_name       VARCHAR2(30)   NOT NULL,

    CONSTRAINT pk_category PRIMARY KEY (cat_id),
    CONSTRAINT fk_category_parent_cat_id FOREIGN KEY (parent_cat_id)
        REFERENCES category (cat_id)
);

CREATE TABLE facility (
    fct_id                        NUMBER(19, 0),
    cat_id                        CHAR(3)        NOT NULL,
    prov_id                       NUMBER(19, 0)   NOT NULL,
    fct_name                      VARCHAR2(50)   NOT NULL,
    fct_postal_code               CHAR(5)        NOT NULL,
    fct_address                   VARCHAR2(80)   NOT NULL,
    fct_detail_address            VARCHAR2(60)   NOT NULL,
    fct_tel                       VARCHAR2(15)   NOT NULL,
    fct_guide                     VARCHAR2(1000) NOT NULL,
    is_open_on_holidays           CHAR(1)        NOT NULL,
    fct_open_time                 DATE           NOT NULL,
    fct_close_time                DATE           NOT NULL,
    unit_usage_time               NUMBER(19, 0)   NOT NULL,
    created_at                    DATE           DEFAULT SYSDATE,
    updated_at                    DATE           DEFAULT SYSDATE,
    fct_state                     VARCHAR2(15)   DEFAULT 'REG_REQUESTED', -- 'REG_REQUESTED', 'REGISTERED', 'DEL_REQUESTED', 'DEL_APPROVED', 'DEL_REJECTED'
    fct_latitude                  NUMBER(9, 6)   NOT NULL,
    fct_longitude                 NUMBER(9, 6)   NOT NULL,

    CONSTRAINT pk_facility PRIMARY KEY (fct_id),
    CONSTRAINT fk_facility_cat_id FOREIGN KEY (cat_id)
        REFERENCES category (cat_id),
    CONSTRAINT fk_facility_prov_id FOREIGN KEY (prov_id)
        REFERENCES fct_provider (prov_id),
    CONSTRAINT chk_fct_state CHECK (fct_state IN ('REG_REQUESTED', 'REG_APPROVED', 'REG_REJECTED', 'DEL_REQUESTED', 'DEL_APPROVED', 'DEL_REJECTED')),
    CONSTRAINT fct_is_open_on_holidays_domain CHECK (is_open_on_holidays IN ('Y', 'N'))
);

CREATE TABLE additional_info (
    addinfo_id      NUMBER(19, 0),
    fct_id          NUMBER(19, 0) NOT NULL,
    addinfo_desc    VARCHAR2(500) NOT NULL,
 
    CONSTRAINT pk_addinfo PRIMARY KEY (addinfo_id),
    CONSTRAINT fk_addinfo_fct_id FOREIGN KEY (fct_id)
        REFERENCES facility (fct_id)
);

CREATE TABLE zone (
    zone_id            NUMBER(19, 0),
    fct_id             NUMBER(19, 0)  NOT NULL,
    zone_name          VARCHAR2(30)  NOT NULL,
    max_user_count     NUMBER(8, 0)  NOT NULL,
    is_shared_zone     CHAR(1)       NOT NULL,
    hourly_rate        NUMBER(5, 0)  NOT NULL,
    created_at         DATE          DEFAULT SYSDATE,
    updated_at         DATE          DEFAULT SYSDATE,
    is_del             CHAR(1)       DEFAULT 'N',

    CONSTRAINT pk_zone PRIMARY KEY (zone_id),
    CONSTRAINT fk_zone_fct_id FOREIGN KEY (fct_id)
        REFERENCES facility (fct_id),
    CONSTRAINT zone_is_shared_zone_domain CHECK (is_shared_zone IN ('Y', 'N')),
    CONSTRAINT zone_is_del_domain CHECK (is_del IN ('Y', 'N'))
);

CREATE TABLE image (
    img_id              NUMBER(19, 0),
    fct_id              NUMBER(19, 0),
    zone_id             NUMBER(19, 0),
    mem_id              NUMBER(19, 0),
    img_path            VARCHAR2(255)  NOT NULL,
    img_attached_to     CHAR(1)        NOT NULL,
    img_display_order   NUMBER(4, 0)   NOT NULL,
    created_at          DATE           DEFAULT SYSDATE,
    updated_at          DATE           DEFAULT SYSDATE,

    CONSTRAINT pk_image PRIMARY KEY (img_id),
    CONSTRAINT fk_image_fct_id FOREIGN KEY (fct_id)
        REFERENCES facility (fct_id),
    CONSTRAINT fk_image_zone_id FOREIGN KEY (zone_id)
        REFERENCES zone (zone_id),
    CONSTRAINT fk_image_mem_id FOREIGN KEY (mem_id)
        REFERENCES member (mem_id)
);

CREATE TABLE reservation (
    rvt_id                 NUMBER(19, 0),
    cons_id                NUMBER(19, 0)  NOT NULL,
    zone_id                NUMBER(19, 0)  NOT NULL,
    rvt_date               DATE          NOT NULL,
    usage_start_time       DATE          NOT NULL,
    usage_end_time         DATE          NOT NULL,
    usage_count            NUMBER(4, 0)  NOT NULL,
    created_at             DATE          DEFAULT SYSDATE,
    rvt_cancelation_reason VARCHAR2(8),

    CONSTRAINT pk_reservation PRIMARY KEY (rvt_id),
    CONSTRAINT fk_rvt_cons_id FOREIGN KEY (cons_id)
        REFERENCES member (mem_id),
    CONSTRAINT fk_rvt_zone_id FOREIGN KEY (zone_id)
        REFERENCES zone (zone_id)
);

CREATE TABLE payment (
    rvt_id                NUMBER(19, 0),
    pay_price             NUMBER(19, 0)  NOT NULL,
    pay_state             CHAR(1)       DEFAULT 'P',
    pay_method            VARCHAR2(100)  NOT NULL,
    pay_date              DATE          DEFAULT SYSDATE,
    cancel_date           DATE,
    refund_account        VARCHAR2(50),
    refund_bank_code      CHAR(3),
    refund_account_owner  VARCHAR2(50),

    CONSTRAINT pk_payment PRIMARY KEY (rvt_id),
    CONSTRAINT fk_payment_rvt_id FOREIGN KEY (rvt_id)
        REFERENCES reservation (rvt_id),
    CONSTRAINT payment_pay_state_domain CHECK (pay_state IN ('P', 'Q', 'R', 'N')),
    CONSTRAINT fk_payment_refund_bank_code FOREIGN KEY (refund_bank_code)
        REFERENCES bank (bank_code)
);

INSERT INTO bank (bank_code, bank_name) VALUES ('001', 'KB국민은행');
INSERT INTO bank (bank_code, bank_name) VALUES ('002', 'SC제일은행');
INSERT INTO bank (bank_code, bank_name) VALUES ('003', '경남은행');
INSERT INTO bank (bank_code, bank_name) VALUES ('004', '광주은행');
INSERT INTO bank (bank_code, bank_name) VALUES ('005', '기업은행');
INSERT INTO bank (bank_code, bank_name) VALUES ('006', '농협');
INSERT INTO bank (bank_code, bank_name) VALUES ('007', '대구은행');
INSERT INTO bank (bank_code, bank_name) VALUES ('008', '부산은행');
INSERT INTO bank (bank_code, bank_name) VALUES ('009', '산업은행');
INSERT INTO bank (bank_code, bank_name) VALUES ('010', '수협');
INSERT INTO bank (bank_code, bank_name) VALUES ('011', '신한은행');
INSERT INTO bank (bank_code, bank_name) VALUES ('012', '신협');
INSERT INTO bank (bank_code, bank_name) VALUES ('013', '외환은행');
INSERT INTO bank (bank_code, bank_name) VALUES ('014', '우리은행');
INSERT INTO bank (bank_code, bank_name) VALUES ('015', '우체국');
INSERT INTO bank (bank_code, bank_name) VALUES ('016', '전북은행');
INSERT INTO bank (bank_code, bank_name) VALUES ('017', '제주은행');
INSERT INTO bank (bank_code, bank_name) VALUES ('018', '축협');
INSERT INTO bank (bank_code, bank_name) VALUES ('019', '하나은행');
INSERT INTO bank (bank_code, bank_name) VALUES ('020', '한국씨티은행');
INSERT INTO bank (bank_code, bank_name) VALUES ('021', 'K뱅크');
INSERT INTO bank (bank_code, bank_name) VALUES ('022', '카카오뱅크');
INSERT INTO bank (bank_code, bank_name) VALUES ('023', '유안타증권');
INSERT INTO bank (bank_code, bank_name) VALUES ('024', '현대증권');
INSERT INTO bank (bank_code, bank_name) VALUES ('025', '미래에셋증권');
INSERT INTO bank (bank_code, bank_name) VALUES ('026', '대우증권');
INSERT INTO bank (bank_code, bank_name) VALUES ('027', '삼성증권');
INSERT INTO bank (bank_code, bank_name) VALUES ('028', '한국투자증권');
INSERT INTO bank (bank_code, bank_name) VALUES ('029', '우리투자증권');
INSERT INTO bank (bank_code, bank_name) VALUES ('030', '교보증권');
INSERT INTO bank (bank_code, bank_name) VALUES ('031', '하이투자증권');
INSERT INTO bank (bank_code, bank_name) VALUES ('032', '에이치엠씨투자증권');
INSERT INTO bank (bank_code, bank_name) VALUES ('033', '키움증권');
INSERT INTO bank (bank_code, bank_name) VALUES ('034', '이트레이드증권');
INSERT INTO bank (bank_code, bank_name) VALUES ('035', '에스케이증권');
INSERT INTO bank (bank_code, bank_name) VALUES ('036', '대신증권');
INSERT INTO bank (bank_code, bank_name) VALUES ('037', '솔로몬투자증권');
INSERT INTO bank (bank_code, bank_name) VALUES ('038', '한화증권');
INSERT INTO bank (bank_code, bank_name) VALUES ('039', '하나대투증권');
INSERT INTO bank (bank_code, bank_name) VALUES ('040', '굿모닝신한증권');
INSERT INTO bank (bank_code, bank_name) VALUES ('041', '동부증권');
INSERT INTO bank (bank_code, bank_name) VALUES ('042', '유진투자증권');
INSERT INTO bank (bank_code, bank_name) VALUES ('043', '메리츠증권');
INSERT INTO bank (bank_code, bank_name) VALUES ('044', '엔에이치투자증권');
INSERT INTO bank (bank_code, bank_name) VALUES ('045', '부국증권');

INSERT INTO category (cat_id, parent_cat_id, cat_name) VALUES ('100', NULL, '스포츠시설');
INSERT INTO category (cat_id, parent_cat_id, cat_name) VALUES ('101', '100', '배드민턴');
INSERT INTO category (cat_id, parent_cat_id, cat_name) VALUES ('102', '100', '풋살');
INSERT INTO category (cat_id, parent_cat_id, cat_name) VALUES ('103', '100', '클라이밍');
INSERT INTO category (cat_id, parent_cat_id, cat_name) VALUES ('104', '100', '탁구장');
INSERT INTO category (cat_id, parent_cat_id, cat_name) VALUES ('105', '100', '당구장');
INSERT INTO category (cat_id, parent_cat_id, cat_name) VALUES ('106', '100', '농구장');
INSERT INTO category (cat_id, parent_cat_id, cat_name) VALUES ('200', NULL, '레크리에이션시설');
INSERT INTO category (cat_id, parent_cat_id, cat_name) VALUES ('201', '200', '강당');
INSERT INTO category (cat_id, parent_cat_id, cat_name) VALUES ('202', '200', '운동장');

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
  (30, TO_DATE('1990-01-01', 'YYYY-MM-DD'), 'W', 'N', SYSDATE, NULL, NULL, 'user30@example.com', '경기도 용인시', '기흥구 구성동 100', '오하영', '010-1111-2224', '03001', 'password30', NULL, 'ROLE_CONS');

INSERT INTO fct_provider (prov_id, biz_name, biz_reg_num)
VALUES (3, '푸른산 주식회사', '123-45-67890');

INSERT INTO fct_provider (prov_id, biz_name, biz_reg_num)
VALUES (5, '미래상사', '234-56-78901');

INSERT INTO fct_provider (prov_id, biz_name, biz_reg_num)
VALUES (7, '동방기업', '345-67-89012');

INSERT INTO fct_provider (prov_id, biz_name, biz_reg_num)
VALUES (9, '한빛산업', '456-78-90123');

INSERT INTO fct_provider (prov_id, biz_name, biz_reg_num)
VALUES (11, '비전코퍼레이션', '567-89-01234');

INSERT INTO fct_provider (prov_id, biz_name, biz_reg_num)
VALUES (13, '새로운도약', '678-90-12345');

INSERT INTO fct_provider (prov_id, biz_name, biz_reg_num)
VALUES (15, '꿈나무 상사', '789-01-23456');

INSERT INTO fct_provider (prov_id, biz_name, biz_reg_num)
VALUES (17, '도약기업', '890-12-34567');

INSERT INTO fct_provider (prov_id, biz_name, biz_reg_num)
VALUES (19, '미소기업', '901-23-45678');

INSERT INTO fct_provider (prov_id, biz_name, biz_reg_num)
VALUES (21, '행복한상사', '012-34-56789');

INSERT INTO fct_provider (prov_id, biz_name, biz_reg_num)
VALUES (23, '열정코퍼레이션', '123-45-67891');

INSERT INTO fct_provider (prov_id, biz_name, biz_reg_num)
VALUES (25, '도약상사', '234-56-78902');

INSERT INTO fct_provider (prov_id, biz_name, biz_reg_num)
VALUES (27, '새로운미래', '345-67-89013');

INSERT INTO fct_provider (prov_id, biz_name, biz_reg_num)
VALUES (29, '한울기업', '456-78-90124');

INSERT INTO "ACCOUNT" (prov_id, balance, account_number, bank_code, IS_AGREED_INFO)
VALUES (3, 0, '003-1234-5678', '007', 'Y');

INSERT INTO "ACCOUNT" (prov_id, balance, account_number, bank_code, IS_AGREED_INFO)
VALUES (5, 0, '005-2345-6789', '013', 'Y');

INSERT INTO "ACCOUNT" (prov_id, balance, account_number, bank_code, IS_AGREED_INFO)
VALUES (7, 0, '007-3456-7890', '029', 'Y');

INSERT INTO "ACCOUNT" (prov_id, balance, account_number, bank_code, IS_AGREED_INFO)
VALUES (9, 0, '009-4567-8901', '022', 'Y');

INSERT INTO "ACCOUNT" (prov_id, balance, account_number, bank_code, IS_AGREED_INFO)
VALUES (11, 0, '011-5678-9012', '035', 'Y');

INSERT INTO "ACCOUNT" (prov_id, balance, account_number, bank_code, IS_AGREED_INFO)
VALUES (13, 0, '013-6789-0123', '041', 'Y');

INSERT INTO "ACCOUNT" (prov_id, balance, account_number, bank_code, IS_AGREED_INFO)
VALUES (15, 0, '015-7890-1234', '003', 'Y');

INSERT INTO "ACCOUNT" (prov_id, balance, account_number, bank_code, IS_AGREED_INFO)
VALUES (17, 0, '017-8901-2345', '019', 'Y');

INSERT INTO "ACCOUNT" (prov_id, balance, account_number, bank_code, IS_AGREED_INFO)
VALUES (19, 0, '019-9012-3456', '027', 'Y');

INSERT INTO "ACCOUNT" (prov_id, balance, account_number, bank_code, IS_AGREED_INFO)
VALUES (21, 0, '021-0123-4567', '011', 'Y');

INSERT INTO "ACCOUNT" (prov_id, balance, account_number, bank_code, IS_AGREED_INFO)
VALUES (23, 0, '023-1234-5678', '045', 'Y');

INSERT INTO "ACCOUNT" (prov_id, balance, account_number, bank_code, IS_AGREED_INFO)
VALUES (25, 0, '025-2345-6789', '032', 'Y');

INSERT INTO "ACCOUNT" (prov_id, balance, account_number, bank_code, IS_AGREED_INFO)
VALUES (27, 0, '027-3456-7890', '018', 'Y');

INSERT INTO "ACCOUNT" (prov_id, balance, account_number, bank_code, IS_AGREED_INFO)
VALUES (29, 0, '029-4567-8901', '024', 'Y');

INSERT INTO facility
  (fct_id, prov_id, fct_name, fct_latitude, fct_longitude, fct_postal_code, fct_address, fct_detail_address, fct_open_time, fct_close_time, unit_usage_time, created_at, updated_at, fct_guide, cat_id, fct_state, fct_tel, is_open_on_holidays)
VALUES 
  (1, 3, '서울 푸른수련장', 37.5665, 126.9780, '06001', '서울특별시 강남구 테헤란로 100', '역삼동 1-1', TO_TIMESTAMP('2000-01-01 08:00:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), TO_TIMESTAMP('2000-01-01 16:00:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 30, SYSDATE, NULL, '시설 이용 안내: 서울 푸른수련장 이용 시 안전수칙을 준수해 주세요.', '101', 'REG_REQUESTED', '010-0001-0001', 'Y');

INSERT INTO facility 
  (fct_id, prov_id, fct_name, fct_latitude, fct_longitude, fct_postal_code, fct_address, fct_detail_address, fct_open_time, fct_close_time, unit_usage_time, created_at, updated_at, fct_guide, cat_id, fct_state, fct_tel, is_open_on_holidays)
VALUES 
  (2, 5, '부산 해운 스포츠 센터', 35.1796, 129.0756, '10120', '부산광역시 해운대구 우동로 200', '중동 2-2', TO_TIMESTAMP('2000-01-01 09:30:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), TO_TIMESTAMP('2000-01-01 18:30:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 60, SYSDATE, NULL, '시설 이용 안내: 부산 해운 스포츠 센터 이용 시 안전수칙을 준수해 주세요.', '102', 'REG_REQUESTED', '010-0002-0002', 'N');

INSERT INTO facility 
  (fct_id, prov_id, fct_name, fct_latitude, fct_longitude, fct_postal_code, fct_address, fct_detail_address, fct_open_time, fct_close_time, unit_usage_time, created_at, updated_at, fct_guide, cat_id, fct_state, fct_tel, is_open_on_holidays)
VALUES 
  (3, 7, '대구 동성 레크 클럽', 35.8714, 128.6014, '20230', '대구광역시 수성구 동대구로 300', '범어동 3-3', TO_TIMESTAMP('2000-01-01 07:30:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), TO_TIMESTAMP('2000-01-01 16:00:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 30, SYSDATE, NULL, '시설 이용 안내: 대구 동성 레크 클럽 이용 시 안전수칙을 준수해 주세요.', '103', 'REG_REQUESTED', '010-0003-0003', 'Y');

INSERT INTO facility 
  (fct_id, prov_id, fct_name, fct_latitude, fct_longitude, fct_postal_code, fct_address, fct_detail_address, fct_open_time, fct_close_time, unit_usage_time, created_at, updated_at, fct_guide, cat_id, fct_state, fct_tel, is_open_on_holidays)
VALUES 
  (4, 9, '인천 바다 레저파크', 37.4563, 126.7052, '03001', '인천광역시 남동구 만수로 400', '구월동 4-4', TO_TIMESTAMP('2000-01-01 08:00:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), TO_TIMESTAMP('2000-01-01 16:30:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 60, SYSDATE, NULL, '시설 이용 안내: 인천 바다 레저파크 이용 시 안전수칙을 준수해 주세요.', '104', 'REG_REQUESTED', '010-0004-0004', 'N');

INSERT INTO facility 
  (fct_id, prov_id, fct_name, fct_latitude, fct_longitude, fct_postal_code, fct_address, fct_detail_address, fct_open_time, fct_close_time, unit_usage_time, created_at, updated_at, fct_guide, cat_id, fct_state, fct_tel, is_open_on_holidays)
VALUES 
  (5, 11, '대전 미래 스포츠 아카데미', 36.3504, 127.3845, '00001', '대전광역시 서구 둔산로 500', '탄방동 5-5', TO_TIMESTAMP('2000-01-01 10:00:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), TO_TIMESTAMP('2000-01-01 18:30:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 30, SYSDATE, NULL, '시설 이용 안내: 대전 미래 스포츠 아카데미 이용 시 안전수칙을 준수해 주세요.', '105', 'REG_REQUESTED', '010-0005-0005', 'Y');

INSERT INTO facility 
  (fct_id, prov_id, fct_name, fct_latitude, fct_longitude, fct_postal_code, fct_address, fct_detail_address, fct_open_time, fct_close_time, unit_usage_time, created_at, updated_at, fct_guide, cat_id, fct_state, fct_tel, is_open_on_holidays)
VALUES 
  (6, 13, '광주 꿈 레크 센터', 35.1595, 126.8526, '06001', '광주광역시 동구 충장로 600', '동명동 6-6', TO_TIMESTAMP('2000-01-01 06:30:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), TO_TIMESTAMP('2000-01-01 15:00:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 60, SYSDATE, NULL, '시설 이용 안내: 광주 꿈 레크 센터 이용 시 안전수칙을 준수해 주세요.', '106', 'REG_REQUESTED', '010-0006-0006', 'N');

INSERT INTO facility 
  (fct_id, prov_id, fct_name, fct_latitude, fct_longitude, fct_postal_code, fct_address, fct_detail_address, fct_open_time, fct_close_time, unit_usage_time, created_at, updated_at, fct_guide, cat_id, fct_state, fct_tel, is_open_on_holidays)
VALUES 
  (7, 15, '제주 해변 레저 센터', 33.4996, 126.5312, '10120', '제주특별자치도 제주시 용담로 700', '연동 7-7', TO_TIMESTAMP('2000-01-01 09:00:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), TO_TIMESTAMP('2000-01-01 17:00:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 30, SYSDATE, NULL, '시설 이용 안내: 제주 해변 레저 센터 이용 시 안전수칙을 준수해 주세요.', '201', 'REG_REQUESTED', '010-0007-0007', 'Y');

INSERT INTO facility 
  (fct_id, prov_id, fct_name, fct_latitude, fct_longitude, fct_postal_code, fct_address, fct_detail_address, fct_open_time, fct_close_time, unit_usage_time, created_at, updated_at, fct_guide, cat_id, fct_state, fct_tel, is_open_on_holidays)
VALUES 
  (8, 17, '서울 도심 스포츠 클럽', 37.5666, 127.0011, '20230', '서울특별시 종로구 세종대로 800', '청운동 8-8', TO_TIMESTAMP('2000-01-01 08:30:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), TO_TIMESTAMP('2000-01-01 17:00:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 60, SYSDATE, NULL, '시설 이용 안내: 서울 도심 스포츠 클럽 이용 시 안전수칙을 준수해 주세요.', '202', 'REG_REQUESTED', '010-0008-0008', 'N');

INSERT INTO facility 
  (fct_id, prov_id, fct_name, fct_latitude, fct_longitude, fct_postal_code, fct_address, fct_detail_address, fct_open_time, fct_close_time, unit_usage_time, created_at, updated_at, fct_guide, cat_id, fct_state, fct_tel, is_open_on_holidays)
VALUES 
  (9, 19, '부산 해양 레크 센터', 35.2059, 129.0852, '03001', '부산광역시 동래구 온천장로 900', '수민동 9-9', TO_TIMESTAMP('2000-01-01 07:30:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), TO_TIMESTAMP('2000-01-01 16:30:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 30, SYSDATE, NULL, '시설 이용 안내: 부산 해양 레크 센터 이용 시 안전수칙을 준수해 주세요.', '101', 'REG_REQUESTED', '010-0009-0009', 'Y');

INSERT INTO facility 
  (fct_id, prov_id, fct_name, fct_latitude, fct_longitude, fct_postal_code, fct_address, fct_detail_address, fct_open_time, fct_close_time, unit_usage_time, created_at, updated_at, fct_guide, cat_id, fct_state, fct_tel, is_open_on_holidays)
VALUES 
  (10, 21, '대구 명문 스포츠 센터', 37.5512, 126.9910, '00001', '대구광역시 중구 동성로 101', '신당동 10-10', TO_TIMESTAMP('2000-01-01 09:00:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), TO_TIMESTAMP('2000-01-01 17:30:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 60, SYSDATE, NULL, '시설 이용 안내: 대구 명문 스포츠 센터 이용 시 안전수칙을 준수해 주세요.', '102', 'REG_REQUESTED', '010-0010-0010', 'N');

INSERT INTO facility 
  (fct_id, prov_id, fct_name, fct_latitude, fct_longitude, fct_postal_code, fct_address, fct_detail_address, fct_open_time, fct_close_time, unit_usage_time, created_at, updated_at, fct_guide, cat_id, fct_state, fct_tel, is_open_on_holidays)
VALUES 
  (11, 23, '인천 미래 레저 센터', 36.6261, 127.4890, '06001', '인천광역시 연수구 송도대로 102', '송도동 11-11', TO_TIMESTAMP('2000-01-01 08:00:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), TO_TIMESTAMP('2000-01-01 16:30:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 30, SYSDATE, NULL, '시설 이용 안내: 인천 미래 레저 센터 이용 시 안전수칙을 준수해 주세요.', '103', 'REG_REQUESTED', '010-0011-0011', 'Y');

INSERT INTO facility 
  (fct_id, prov_id, fct_name, fct_latitude, fct_longitude, fct_postal_code, fct_address, fct_detail_address, fct_open_time, fct_close_time, unit_usage_time, created_at, updated_at, fct_guide, cat_id, fct_state, fct_tel, is_open_on_holidays)
VALUES 
  (12, 25, '대전 행복 스포츠 아카데미', 35.9468, 129.0362, '10120', '대전광역시 유성구 대학로 103', '봉명동 12-12', TO_TIMESTAMP('2000-01-01 07:30:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), TO_TIMESTAMP('2000-01-01 16:30:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 60, SYSDATE, NULL, '시설 이용 안내: 대전 행복 스포츠 아카데미 이용 시 안전수칙을 준수해 주세요.', '104', 'REG_REQUESTED', '010-0012-0012', 'N');

INSERT INTO facility 
  (fct_id, prov_id, fct_name, fct_latitude, fct_longitude, fct_postal_code, fct_address, fct_detail_address, fct_open_time, fct_close_time, unit_usage_time, created_at, updated_at, fct_guide, cat_id, fct_state, fct_tel, is_open_on_holidays)
VALUES 
  (13, 27, '광주 으뜸 레크 클럽', 37.4783, 126.9050, '20230', '광주광역시 북구 첨단로 104', '운암동 13-13', TO_TIMESTAMP('2000-01-01 08:30:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), TO_TIMESTAMP('2000-01-01 17:30:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 30, SYSDATE, NULL, '시설 이용 안내: 광주 으뜸 레크 클럽 이용 시 안전수칙을 준수해 주세요.', '105', 'REG_REQUESTED', '010-0013-0013', 'Y');

INSERT INTO facility 
  (fct_id, prov_id, fct_name, fct_latitude, fct_longitude, fct_postal_code, fct_address, fct_detail_address, fct_open_time, fct_close_time, unit_usage_time, created_at, updated_at, fct_guide, cat_id, fct_state, fct_tel, is_open_on_holidays)
VALUES 
  (14, 29, '제주 태양 스포츠 파크', 36.3260, 127.4414, '03001', '제주특별자치도 서귀포시 중앙로 105', '중앙동 14-14', TO_TIMESTAMP('2000-01-01 09:00:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), TO_TIMESTAMP('2000-01-01 18:00:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 60, SYSDATE, NULL, '시설 이용 안내: 제주 태양 스포츠 파크 이용 시 안전수칙을 준수해 주세요.', '106', 'REG_REQUESTED', '010-0014-0014', 'N');

INSERT INTO facility 
  (fct_id, prov_id, fct_name, fct_latitude, fct_longitude, fct_postal_code, fct_address, fct_detail_address, fct_open_time, fct_close_time, unit_usage_time, created_at, updated_at, fct_guide, cat_id, fct_state, fct_tel, is_open_on_holidays)
VALUES 
  (15, 3, '서울 한강 레저 센터', 35.1600, 126.8500, '00001', '서울특별시 강동구 올림픽로 106', '명일동 15-15', TO_TIMESTAMP('2000-01-01 07:30:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), TO_TIMESTAMP('2000-01-01 16:00:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 30, SYSDATE, NULL, '시설 이용 안내: 서울 한강 레저 센터 이용 시 안전수칙을 준수해 주세요.', '201', 'REG_REQUESTED', '010-0015-0015', 'Y');

INSERT INTO facility 
  (fct_id, prov_id, fct_name, fct_latitude, fct_longitude, fct_postal_code, fct_address, fct_detail_address, fct_open_time, fct_close_time, unit_usage_time, created_at, updated_at, fct_guide, cat_id, fct_state, fct_tel, is_open_on_holidays)
VALUES 
  (16, 5, '부산 서부 스포츠 클럽', 37.5700, 126.9800, '06001', '부산광역시 부산진구 전포대로 107', '부전동 16-16', TO_TIMESTAMP('2000-01-01 08:00:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), TO_TIMESTAMP('2000-01-01 16:30:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 60, SYSDATE, NULL, '시설 이용 안내: 부산 서부 스포츠 클럽 이용 시 안전수칙을 준수해 주세요.', '202', 'REG_REQUESTED', '010-0016-0016', 'N');

INSERT INTO facility 
  (fct_id, prov_id, fct_name, fct_latitude, fct_longitude, fct_postal_code, fct_address, fct_detail_address, fct_open_time, fct_close_time, unit_usage_time, created_at, updated_at, fct_guide, cat_id, fct_state, fct_tel, is_open_on_holidays)
VALUES 
  (17, 7, '대구 상록 레크 센터', 35.2000, 129.0700, '10120', '대구광역시 달서구 월배로 108', '상인동 17-17', TO_TIMESTAMP('2000-01-01 09:30:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), TO_TIMESTAMP('2000-01-01 18:30:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 30, SYSDATE, NULL, '시설 이용 안내: 대구 상록 레크 센터 이용 시 안전수칙을 준수해 주세요.', '101', 'REG_REQUESTED', '010-0017-0017', 'Y');

INSERT INTO facility 
  (fct_id, prov_id, fct_name, fct_latitude, fct_longitude, fct_postal_code, fct_address, fct_detail_address, fct_open_time, fct_close_time, unit_usage_time, created_at, updated_at, fct_guide, cat_id, fct_state, fct_tel, is_open_on_holidays)
VALUES 
  (18, 9, '인천 글로벌 스포츠 아카데미', 36.3500, 127.3800, '20230', '인천광역시 미추홀구 주안로 109', '도화동 18-18', TO_TIMESTAMP('2000-01-01 07:30:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), TO_TIMESTAMP('2000-01-01 16:30:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 60, SYSDATE, NULL, '시설 이용 안내: 인천 글로벌 스포츠 아카데미 이용 시 안전수칙을 준수해 주세요.', '102', 'REG_REQUESTED', '010-0018-0018', 'N');

INSERT INTO facility 
  (fct_id, prov_id, fct_name, fct_latitude, fct_longitude, fct_postal_code, fct_address, fct_detail_address, fct_open_time, fct_close_time, unit_usage_time, created_at, updated_at, fct_guide, cat_id, fct_state, fct_tel, is_open_on_holidays)
VALUES 
  (19, 11, '대전 올림픽 레저 파크', 37.4500, 126.7000, '03001', '대전광역시 동구 대종로 110', '중구 19-19', TO_TIMESTAMP('2000-01-01 08:00:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), TO_TIMESTAMP('2000-01-01 16:30:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 30, SYSDATE, NULL, '시설 이용 안내: 대전 올림픽 레저 파크 이용 시 안전수칙을 준수해 주세요.', '103', 'REG_REQUESTED', '010-0019-0019', 'Y');

INSERT INTO facility 
  (fct_id, prov_id, fct_name, fct_latitude, fct_longitude, fct_postal_code, fct_address, fct_detail_address, fct_open_time, fct_close_time, unit_usage_time, created_at, updated_at, fct_guide, cat_id, fct_state, fct_tel, is_open_on_holidays)
VALUES 
  (20, 13, '광주 활력 스포츠 센터', 35.8700, 128.6000, '00001', '광주광역시 서구 치평로 111', '치평동 20-20', TO_TIMESTAMP('2000-01-01 09:00:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), TO_TIMESTAMP('2000-01-01 17:30:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 60, SYSDATE, NULL, '시설 이용 안내: 광주 활력 스포츠 센터 이용 시 안전수칙을 준수해 주세요.', '104', 'REG_REQUESTED', '010-0020-0020', 'N');

INSERT INTO facility 
  (fct_id, prov_id, fct_name, fct_latitude, fct_longitude, fct_postal_code, fct_address, fct_detail_address, fct_open_time, fct_close_time, unit_usage_time, created_at, updated_at, fct_guide, cat_id, fct_state, fct_tel, is_open_on_holidays)
VALUES 
  (21, 15, '제주 자연 레크 클럽', 37.5600, 126.9900, '06001', '제주특별자치도 제주시 탑동로 112', '탑동 21-21', TO_TIMESTAMP('2000-01-01 08:30:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), TO_TIMESTAMP('2000-01-01 17:30:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 30, SYSDATE, NULL, '시설 이용 안내: 제주 자연 레크 클럽 이용 시 안전수칙을 준수해 주세요.', '105', 'REG_REQUESTED', '010-0021-0021', 'Y');

INSERT INTO facility 
  (fct_id, prov_id, fct_name, fct_latitude, fct_longitude, fct_postal_code, fct_address, fct_detail_address, fct_open_time, fct_close_time, unit_usage_time, created_at, updated_at, fct_guide, cat_id, fct_state, fct_tel, is_open_on_holidays)
VALUES 
  (22, 17, '서울 비전 스포츠 파크', 36.3400, 127.4000, '10120', '서울특별시 마포구 월드컵로 113', '상암동 22-22', TO_TIMESTAMP('2000-01-01 07:30:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), TO_TIMESTAMP('2000-01-01 16:30:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 60, SYSDATE, NULL, '시설 이용 안내: 서울 비전 스포츠 파크 이용 시 안전수칙을 준수해 주세요.', '106', 'REG_REQUESTED', '010-0022-0022', 'N');

INSERT INTO facility 
  (fct_id, prov_id, fct_name, fct_latitude, fct_longitude, fct_postal_code, fct_address, fct_detail_address, fct_open_time, fct_close_time, unit_usage_time, created_at, updated_at, fct_guide, cat_id, fct_state, fct_tel, is_open_on_holidays)
VALUES 
  (23, 19, '부산 청춘 레저 센터', 35.1500, 126.8400, '20230', '부산광역시 영도구 청학동로 114', '동삼동 23-23', TO_TIMESTAMP('2000-01-01 08:00:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), TO_TIMESTAMP('2000-01-01 16:30:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 30, SYSDATE, NULL, '시설 이용 안내: 부산 청춘 레저 센터 이용 시 안전수칙을 준수해 주세요.', '201', 'REG_REQUESTED', '010-0023-0023', 'Y');

INSERT INTO facility 
  (fct_id, prov_id, fct_name, fct_latitude, fct_longitude, fct_postal_code, fct_address, fct_detail_address, fct_open_time, fct_close_time, unit_usage_time, created_at, updated_at, fct_guide, cat_id, fct_state, fct_tel, is_open_on_holidays)
VALUES 
  (24, 21, '대구 다이나믹 스포츠 센터', 37.5800, 126.9700, '03001', '대구광역시 북구 칠곡로 115', '칠곡동 24-24', TO_TIMESTAMP('2000-01-01 09:30:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), TO_TIMESTAMP('2000-01-01 18:30:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 60, SYSDATE, NULL, '시설 이용 안내: 대구 다이나믹 스포츠 센터 이용 시 안전수칙을 준수해 주세요.', '202', 'REG_REQUESTED', '010-0024-0024', 'N');

INSERT INTO facility 
  (fct_id, prov_id, fct_name, fct_latitude, fct_longitude, fct_postal_code, fct_address, fct_detail_address, fct_open_time, fct_close_time, unit_usage_time, created_at, updated_at, fct_guide, cat_id, fct_state, fct_tel, is_open_on_holidays)
VALUES 
  (25, 23, '인천 미래 레저 파크', 35.2100, 129.0800, '00001', '인천광역시 강화군 강화대로 116', '불은동 25-25', TO_TIMESTAMP('2000-01-01 08:30:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), TO_TIMESTAMP('2000-01-01 17:30:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 30, SYSDATE, NULL, '시설 이용 안내: 인천 미래 레저 파크 이용 시 안전수칙을 준수해 주세요.', '101', 'REG_REQUESTED', '010-0025-0025', 'Y');

INSERT INTO facility 
  (fct_id, prov_id, fct_name, fct_latitude, fct_longitude, fct_postal_code, fct_address, fct_detail_address, fct_open_time, fct_close_time, unit_usage_time, created_at, updated_at, fct_guide, cat_id, fct_state, fct_tel, is_open_on_holidays)
VALUES 
  (26, 25, '대전 프리미엄 스포츠 클럽', 36.3600, 127.4100, '06001', '대전광역시 서구 둔산대로 117', '가수원동 26-26', TO_TIMESTAMP('2000-01-01 07:30:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), TO_TIMESTAMP('2000-01-01 16:30:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 60, SYSDATE, NULL, '시설 이용 안내: 대전 프리미엄 스포츠 클럽 이용 시 안전수칙을 준수해 주세요.', '102', 'REG_REQUESTED', '010-0026-0026', 'N');

INSERT INTO facility 
  (fct_id, prov_id, fct_name, fct_latitude, fct_longitude, fct_postal_code, fct_address, fct_detail_address, fct_open_time, fct_close_time, unit_usage_time, created_at, updated_at, fct_guide, cat_id, fct_state, fct_tel, is_open_on_holidays)
VALUES 
  (27, 27, '광주 스포트라 레크 센터', 35.1400, 126.8300, '10120', '광주광역시 남구 봉선로 118', '봉선동 27-27', TO_TIMESTAMP('2000-01-01 08:00:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), TO_TIMESTAMP('2000-01-01 16:30:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 30, SYSDATE, NULL, '시설 이용 안내: 광주 스포트라 레크 센터 이용 시 안전수칙을 준수해 주세요.', '103', 'REG_REQUESTED', '010-0027-0027', 'Y');

INSERT INTO facility 
  (fct_id, prov_id, fct_name, fct_latitude, fct_longitude, fct_postal_code, fct_address, fct_detail_address, fct_open_time, fct_close_time, unit_usage_time, created_at, updated_at, fct_guide, cat_id, fct_state, fct_tel, is_open_on_holidays)
VALUES 
  (28, 29, '제주 해피 스포츠 아카데미', 37.5900, 126.9600, '20230', '제주특별자치도 서귀포시 외로 119', '서홍동 28-28', TO_TIMESTAMP('2000-01-01 09:00:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), TO_TIMESTAMP('2000-01-01 18:00:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 60, SYSDATE, NULL, '시설 이용 안내: 제주 해피 스포츠 아카데미 이용 시 안전수칙을 준수해 주세요.', '104', 'REG_REQUESTED', '010-0028-0028', 'N');

INSERT INTO facility 
  (fct_id, prov_id, fct_name, fct_latitude, fct_longitude, fct_postal_code, fct_address, fct_detail_address, fct_open_time, fct_close_time, unit_usage_time, created_at, updated_at, fct_guide, cat_id, fct_state, fct_tel, is_open_on_holidays)
VALUES 
  (29, 3, '서울 다함께 레저 센터', 35.2200, 129.0900, '03001', '서울특별시 구로구 디지털로 120', '구로동 29-29', TO_TIMESTAMP('2000-01-01 08:30:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), TO_TIMESTAMP('2000-01-01 17:30:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 30, SYSDATE, NULL, '시설 이용 안내: 서울 다함께 레저 센터 이용 시 안전수칙을 준수해 주세요.', '105', 'REG_REQUESTED', '010-0029-0029', 'Y');

INSERT INTO facility 
  (fct_id, prov_id, fct_name, fct_latitude, fct_longitude, fct_postal_code, fct_address, fct_detail_address, fct_open_time, fct_close_time, unit_usage_time, created_at, updated_at, fct_guide, cat_id, fct_state, fct_tel, is_open_on_holidays)
VALUES 
  (30, 5, '부산 열정 스포츠 클럽', 36.3700, 127.4200, '00001', '부산광역시 금정구 중앙로 121', '구서동 30-30', TO_TIMESTAMP('2000-01-01 07:30:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), TO_TIMESTAMP('2000-01-01 16:30:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 60, SYSDATE, NULL, '시설 이용 안내: 부산 열정 스포츠 클럽 이용 시 안전수칙을 준수해 주세요.', '106', 'REG_REQUESTED', '010-0030-0030', 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (1, 1, '시설1 존 A', 10000, 7, 'Y', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (2, 1, '시설1 존 B', 20000, 4, 'N', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (3, 2, '시설2 존 A', 30000, 5, 'N', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (4, 2, '시설2 존 B', 40000, 9, 'Y', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (5, 3, '시설3 존 A', 50000, 3, 'Y', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (6, 3, '시설3 존 B', 10000, 8, 'N', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (7, 4, '시설4 존 A', 20000, 6, 'N', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (8, 4, '시설4 존 B', 30000, 10, 'Y', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (9, 5, '시설5 존 A', 40000, 2, 'Y', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (10, 5, '시설5 존 B', 50000, 7, 'N', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (11, 6, '시설6 존 A', 10000, 4, 'Y', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (12, 6, '시설6 존 B', 30000, 5, 'N', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (13, 7, '시설7 존 A', 20000, 6, 'Y', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (14, 7, '시설7 존 B', 40000, 8, 'N', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (15, 8, '시설8 존 A', 50000, 9, 'N', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (16, 8, '시설8 존 B', 10000, 3, 'Y', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (17, 9, '시설9 존 A', 30000, 7, 'N', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (18, 9, '시설9 존 B', 20000, 2, 'Y', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (19, 10, '시설10 존 A', 40000, 5, 'Y', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (20, 10, '시설10 존 B', 50000, 6, 'N', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (21, 11, '시설11 존 A', 10000, 4, 'Y', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (22, 11, '시설11 존 B', 20000, 8, 'N', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (23, 12, '시설12 존 A', 30000, 7, 'Y', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (24, 12, '시설12 존 B', 40000, 3, 'N', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (25, 13, '시설13 존 A', 50000, 9, 'Y', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (26, 13, '시설13 존 B', 10000, 6, 'N', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (27, 14, '시설14 존 A', 20000, 5, 'N', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (28, 14, '시설14 존 B', 30000, 10, 'Y', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (29, 15, '시설15 존 A', 40000, 4, 'Y', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (30, 15, '시설15 존 B', 50000, 7, 'N', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (31, 16, '시설16 존 A', 10000, 8, 'Y', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (32, 16, '시설16 존 B', 30000, 5, 'N', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (33, 17, '시설17 존 A', 20000, 6, 'Y', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (34, 17, '시설17 존 B', 40000, 9, 'N', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (35, 18, '시설18 존 A', 50000, 3, 'Y', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (36, 18, '시설18 존 B', 10000, 4, 'N', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (37, 19, '시설19 존 A', 30000, 8, 'Y', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (38, 19, '시설19 존 B', 20000, 2, 'N', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (39, 20, '시설20 존 A', 40000, 7, 'Y', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (40, 20, '시설20 존 B', 50000, 10, 'N', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (41, 21, '시설21 존 A', 10000, 5, 'Y', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (42, 21, '시설21 존 B', 20000, 3, 'N', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (43, 22, '시설22 존 A', 30000, 8, 'Y', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (44, 22, '시설22 존 B', 40000, 4, 'N', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (45, 23, '시설23 존 A', 50000, 6, 'Y', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (46, 23, '시설23 존 B', 10000, 7, 'N', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (47, 24, '시설24 존 A', 20000, 5, 'Y', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (48, 24, '시설24 존 B', 30000, 9, 'N', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (49, 25, '시설25 존 A', 40000, 8, 'Y', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (50, 25, '시설25 존 B', 50000, 2, 'N', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (51, 26, '시설26 존 A', 10000, 7, 'Y', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (52, 26, '시설26 존 B', 30000, 6, 'N', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (53, 27, '시설27 존 A', 20000, 4, 'Y', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (54, 27, '시설27 존 B', 40000, 5, 'N', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (55, 28, '시설28 존 A', 50000, 8, 'Y', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (56, 28, '시설28 존 B', 10000, 3, 'N', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (57, 29, '시설29 존 A', 30000, 7, 'Y', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (58, 29, '시설29 존 B', 20000, 5, 'N', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (59, 30, '시설30 존 A', 40000, 6, 'Y', SYSDATE, NULL, 'N');

INSERT INTO zone (zone_id, fct_id, zone_name, hourly_rate, max_user_count, is_shared_zone, created_at, updated_at, is_del)
VALUES (60, 30, '시설30 존 B', 50000, 8, 'N', SYSDATE, NULL, 'N');

INSERT INTO additional_info (addinfo_id, fct_id, addinfo_desc)
VALUES (1, 1, '라켓 5000원');

INSERT INTO additional_info (addinfo_id, fct_id, addinfo_desc)
VALUES (2, 3, '샤워타월 1000원');

INSERT INTO additional_info (addinfo_id, fct_id, addinfo_desc)
VALUES (3, 5, '음료 2000원');

INSERT INTO additional_info (addinfo_id, fct_id, addinfo_desc)
VALUES (4, 7, '수건 1500원');

INSERT INTO additional_info (addinfo_id, fct_id, addinfo_desc)
VALUES (5, 9, '모자 3000원');

INSERT INTO additional_info (addinfo_id, fct_id, addinfo_desc)
VALUES (6, 11, '스포츠백 4000원');

INSERT INTO additional_info (addinfo_id, fct_id, addinfo_desc)
VALUES (7, 13, '물티슈 800원');

INSERT INTO additional_info (addinfo_id, fct_id, addinfo_desc)
VALUES (8, 15, '샌드위치 3500원');

INSERT INTO additional_info (addinfo_id, fct_id, addinfo_desc)
VALUES (9, 17, '간식 1200원');

INSERT INTO additional_info (addinfo_id, fct_id, addinfo_desc)
VALUES (10, 19, '에너지바 1500원');

INSERT INTO additional_info (addinfo_id, fct_id, addinfo_desc)
VALUES (11, 21, '핸드폰 충전기 10000원');

INSERT INTO additional_info (addinfo_id, fct_id, addinfo_desc)
VALUES (12, 23, '여분의 라켓 5000원');

INSERT INTO additional_info (addinfo_id, fct_id, addinfo_desc)
VALUES (13, 25, '탈의실 이용료 2000원');

INSERT INTO additional_info (addinfo_id, fct_id, addinfo_desc)
VALUES (14, 27, '음료수 자판기 3000원');

INSERT INTO additional_info (addinfo_id, fct_id, addinfo_desc)
VALUES (15, 29, '주차비 4000원');

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (1, 1, NULL, NULL, 1, 'M', 'http://localhost:8080/resources/files/mem_img.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (2, 2, NULL, NULL, 1, 'M', 'http://localhost:8080/resources/files/mem_img.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (3, 3, NULL, NULL, 1, 'M', 'http://localhost:8080/resources/files/mem_img.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (4, 4, NULL, NULL, 1, 'M', 'http://localhost:8080/resources/files/mem_img.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (5, 5, NULL, NULL, 1, 'M', 'http://localhost:8080/resources/files/mem_img.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (6, 6, NULL, NULL, 1, 'M', 'http://localhost:8080/resources/files/mem_img.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (7, 7, NULL, NULL, 1, 'M', 'http://localhost:8080/resources/files/mem_img.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (8, 8, NULL, NULL, 1, 'M', 'http://localhost:8080/resources/files/mem_img.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (9, 9, NULL, NULL, 1, 'M', 'http://localhost:8080/resources/files/mem_img.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (10, 10, NULL, NULL, 1, 'M', 'http://localhost:8080/resources/files/mem_img.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (11, 11, NULL, NULL, 1, 'M', 'http://localhost:8080/resources/files/mem_img.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (12, 12, NULL, NULL, 1, 'M', 'http://localhost:8080/resources/files/mem_img.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (13, 13, NULL, NULL, 1, 'M', 'http://localhost:8080/resources/files/mem_img.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (14, 14, NULL, NULL, 1, 'M', 'http://localhost:8080/resources/files/mem_img.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (15, 15, NULL, NULL, 1, 'M', 'http://localhost:8080/resources/files/mem_img.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (16, 16, NULL, NULL, 1, 'M', 'http://localhost:8080/resources/files/mem_img.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (17, 17, NULL, NULL, 1, 'M', 'http://localhost:8080/resources/files/mem_img.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (18, 18, NULL, NULL, 1, 'M', 'http://localhost:8080/resources/files/mem_img.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (19, 19, NULL, NULL, 1, 'M', 'http://localhost:8080/resources/files/mem_img.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (20, 20, NULL, NULL, 1, 'M', 'http://localhost:8080/resources/files/mem_img.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (21, 21, NULL, NULL, 1, 'M', 'http://localhost:8080/resources/files/mem_img.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (22, 22, NULL, NULL, 1, 'M', 'http://localhost:8080/resources/files/mem_img.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (23, 23, NULL, NULL, 1, 'M', 'http://localhost:8080/resources/files/mem_img.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (24, 24, NULL, NULL, 1, 'M', 'http://localhost:8080/resources/files/mem_img.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (25, 25, NULL, NULL, 1, 'M', 'http://localhost:8080/resources/files/mem_img.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (26, 26, NULL, NULL, 1, 'M', 'http://localhost:8080/resources/files/mem_img.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (27, 27, NULL, NULL, 1, 'M', 'http://localhost:8080/resources/files/mem_img.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (28, 28, NULL, NULL, 1, 'M', 'http://localhost:8080/resources/files/mem_img.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (29, 29, NULL, NULL, 1, 'M', 'http://localhost:8080/resources/files/mem_img.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (30, 30, NULL, NULL, 1, 'M', 'http://localhost:8080/resources/files/mem_img.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (31, NULL, 1, NULL, 1, 'F', 'http://localhost:8080/resources/files/fct_img_1.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (32, NULL, 2, NULL, 1, 'F', 'http://localhost:8080/resources/files/fct_img_2.webp', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (33, NULL, 3, NULL, 1, 'F', 'http://localhost:8080/resources/files/fct_img_3.webp', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (34, NULL, 4, NULL, 1, 'F', 'http://localhost:8080/resources/files/fct_img_4.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (35, NULL, 5, NULL, 1, 'F', 'http://localhost:8080/resources/files/sample_facility_1.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (36, NULL, 6, NULL, 1, 'F', 'http://localhost:8080/resources/files/fct_img_6.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (37, NULL, 7, NULL, 1, 'F', 'http://localhost:8080/resources/files/fct_img_7.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (38, NULL, 8, NULL, 1, 'F', 'http://localhost:8080/resources/files/fct_img_8.webp', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (39, NULL, 9, NULL, 1, 'F', 'http://localhost:8080/resources/files/fct_img_9.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (40, NULL, 10, NULL, 1, 'F', 'http://localhost:8080/resources/files/sample_facility_2.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (41, NULL, 11, NULL, 1, 'F', 'http://localhost:8080/resources/files/fct_img_11.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (42, NULL, 12, NULL, 1, 'F', 'http://localhost:8080/resources/files/fct_img_12.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (43, NULL, 13, NULL, 1, 'F', 'http://localhost:8080/resources/files/fct_img_13.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (44, NULL, 14, NULL, 1, 'F', 'http://localhost:8080/resources/files/fct_img_14.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (45, NULL, 15, NULL, 1, 'F', 'http://localhost:8080/resources/files/fct_img_15.webp', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (46, NULL, 16, NULL, 1, 'F', 'http://localhost:8080/resources/files/fct_img_16.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (47, NULL, 17, NULL, 1, 'F', 'http://localhost:8080/resources/files/fct_img_17.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (48, NULL, 18, NULL, 1, 'F', 'http://localhost:8080/resources/files/sample_facility_3.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (49, NULL, 19, NULL, 1, 'F', 'http://localhost:8080/resources/files/fct_img_19.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (50, NULL, 20, NULL, 1, 'F', 'http://localhost:8080/resources/files/fct_img_20.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (51, NULL, 21, NULL, 1, 'F', 'http://localhost:8080/resources/files/fct_img_21.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (52, NULL, 22, NULL, 1, 'F', 'http://localhost:8080/resources/files/sample_facility_4.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (53, NULL, 23, NULL, 1, 'F', 'http://localhost:8080/resources/files/fct_img_23.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (54, NULL, 24, NULL, 1, 'F', 'http://localhost:8080/resources/files/fct_img_24.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (55, NULL, 25, NULL, 1, 'F', 'http://localhost:8080/resources/files/fct_img_25.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (56, NULL, 26, NULL, 1, 'F', 'http://localhost:8080/resources/files/fct_img_26.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (57, NULL, 27, NULL, 1, 'F', 'http://localhost:8080/resources/files/fct_img_27.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (58, NULL, 28, NULL, 1, 'F', 'http://localhost:8080/resources/files/fct_img_28.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (59, NULL, 29, NULL, 1, 'F', 'http://localhost:8080/resources/files/fct_img_29.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (60, NULL, 30, NULL, 1, 'F', 'http://localhost:8080/resources/files/sample_facility_5.jpg', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (61, NULL, NULL, 1, 1, 'Z', 'http://localhost:8080/resources/files/zone1_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (62, NULL, NULL, 2, 1, 'Z', 'http://localhost:8080/resources/files/zone2_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (63, NULL, NULL, 3, 1, 'Z', 'http://localhost:8080/resources/files/zone1_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (64, NULL, NULL, 4, 1, 'Z', 'http://localhost:8080/resources/files/zone2_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (65, NULL, NULL, 5, 1, 'Z', 'http://localhost:8080/resources/files/zone1_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (66, NULL, NULL, 6, 1, 'Z', 'http://localhost:8080/resources/files/zone2_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (67, NULL, NULL, 7, 1, 'Z', 'http://localhost:8080/resources/files/zone1_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (68, NULL, NULL, 8, 1, 'Z', 'http://localhost:8080/resources/files/zone2_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (69, NULL, NULL, 9, 1, 'Z', 'http://localhost:8080/resources/files/zone1_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (70, NULL, NULL, 10, 1, 'Z', 'http://localhost:8080/resources/files/zone2_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (71, NULL, NULL, 11, 1, 'Z', 'http://localhost:8080/resources/files/zone1_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (72, NULL, NULL, 12, 1, 'Z', 'http://localhost:8080/resources/files/zone2_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (73, NULL, NULL, 13, 1, 'Z', 'http://localhost:8080/resources/files/zone1_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (74, NULL, NULL, 14, 1, 'Z', 'http://localhost:8080/resources/files/zone2_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (75, NULL, NULL, 15, 1, 'Z', 'http://localhost:8080/resources/files/zone1_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (76, NULL, NULL, 16, 1, 'Z', 'http://localhost:8080/resources/files/zone2_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (77, NULL, NULL, 17, 1, 'Z', 'http://localhost:8080/resources/files/zone1_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (78, NULL, NULL, 18, 1, 'Z', 'http://localhost:8080/resources/files/zone2_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (79, NULL, NULL, 19, 1, 'Z', 'http://localhost:8080/resources/files/zone1_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (80, NULL, NULL, 20, 1, 'Z', 'http://localhost:8080/resources/files/zone2_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (81, NULL, NULL, 21, 1, 'Z', 'http://localhost:8080/resources/files/zone1_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (82, NULL, NULL, 22, 1, 'Z', 'http://localhost:8080/resources/files/zone2_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (83, NULL, NULL, 23, 1, 'Z', 'http://localhost:8080/resources/files/zone1_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (84, NULL, NULL, 24, 1, 'Z', 'http://localhost:8080/resources/files/zone2_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (85, NULL, NULL, 25, 1, 'Z', 'http://localhost:8080/resources/files/zone1_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (86, NULL, NULL, 26, 1, 'Z', 'http://localhost:8080/resources/files/zone2_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (87, NULL, NULL, 27, 1, 'Z', 'http://localhost:8080/resources/files/zone1_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (88, NULL, NULL, 28, 1, 'Z', 'http://localhost:8080/resources/files/zone2_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (89, NULL, NULL, 29, 1, 'Z', 'http://localhost:8080/resources/files/zone1_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (90, NULL, NULL, 30, 1, 'Z', 'http://localhost:8080/resources/files/zone2_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (91, NULL, NULL, 31, 1, 'Z', 'http://localhost:8080/resources/files/zone1_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (92, NULL, NULL, 32, 1, 'Z', 'http://localhost:8080/resources/files/zone2_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (93, NULL, NULL, 33, 1, 'Z', 'http://localhost:8080/resources/files/zone1_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (94, NULL, NULL, 34, 1, 'Z', 'http://localhost:8080/resources/files/zone2_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (95, NULL, NULL, 35, 1, 'Z', 'http://localhost:8080/resources/files/zone1_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (96, NULL, NULL, 36, 1, 'Z', 'http://localhost:8080/resources/files/zone2_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (97, NULL, NULL, 37, 1, 'Z', 'http://localhost:8080/resources/files/zone1_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (98, NULL, NULL, 38, 1, 'Z', 'http://localhost:8080/resources/files/zone2_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (99, NULL, NULL, 39, 1, 'Z', 'http://localhost:8080/resources/files/zone1_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (100, NULL, NULL, 40, 1, 'Z', 'http://localhost:8080/resources/files/zone2_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (101, NULL, NULL, 41, 1, 'Z', 'http://localhost:8080/resources/files/zone1_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (102, NULL, NULL, 42, 1, 'Z', 'http://localhost:8080/resources/files/zone2_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (103, NULL, NULL, 43, 1, 'Z', 'http://localhost:8080/resources/files/zone1_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (104, NULL, NULL, 44, 1, 'Z', 'http://localhost:8080/resources/files/zone2_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (105, NULL, NULL, 45, 1, 'Z', 'http://localhost:8080/resources/files/zone1_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (106, NULL, NULL, 46, 1, 'Z', 'http://localhost:8080/resources/files/zone2_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (107, NULL, NULL, 47, 1, 'Z', 'http://localhost:8080/resources/files/zone1_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (108, NULL, NULL, 48, 1, 'Z', 'http://localhost:8080/resources/files/zone2_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (109, NULL, NULL, 49, 1, 'Z', 'http://localhost:8080/resources/files/zone1_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (110, NULL, NULL, 50, 1, 'Z', 'http://localhost:8080/resources/files/zone2_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (111, NULL, NULL, 51, 1, 'Z', 'http://localhost:8080/resources/files/zone1_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (112, NULL, NULL, 52, 1, 'Z', 'http://localhost:8080/resources/files/zone2_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (113, NULL, NULL, 53, 1, 'Z', 'http://localhost:8080/resources/files/zone1_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (114, NULL, NULL, 54, 1, 'Z', 'http://localhost:8080/resources/files/zone2_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (115, NULL, NULL, 55, 1, 'Z', 'http://localhost:8080/resources/files/zone1_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (116, NULL, NULL, 56, 1, 'Z', 'http://localhost:8080/resources/files/zone2_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (117, NULL, NULL, 57, 1, 'Z', 'http://localhost:8080/resources/files/zone1_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (118, NULL, NULL, 58, 1, 'Z', 'http://localhost:8080/resources/files/zone2_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (119, NULL, NULL, 59, 1, 'Z', 'http://localhost:8080/resources/files/zone1_img.png', SYSDATE, NULL);

INSERT INTO image (img_id, mem_id, fct_id, zone_id, img_display_order, img_attached_to, img_path, created_at, updated_at)
VALUES (120, NULL, NULL, 60, 1, 'Z', 'http://localhost:8080/resources/files/zone2_img.png', SYSDATE, NULL);
