CREATE SEQUENCE mem_pk_seq        START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE noti_pk_seq       START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE fct_prov_pk_seq   START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE fct_pk_seq        START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE cat_pk_seq        START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE addinfo_pk_seq    START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE report_pk_seq     START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE rvt_pk_seq        START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE pay_pk_seq        START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE zone_pk_seq       START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE img_pk_seq        START WITH 1 INCREMENT BY 1;

CREATE TABLE member (
    mem_id                       NUMBER(4, 0)   NOT NULL,
    email                        VARCHAR2(50)   NOT NULL UNIQUE,
    mem_name                     VARCHAR2(30)   NOT NULL,
    password                     VARCHAR2(80)   NOT NULL,
    mem_phone                    VARCHAR2(15)   NOT NULL,
    birth_date                   DATE           NOT NULL,
    gender                       CHAR(1)        NOT NULL,
    mem_postal_code              CHAR(5)        NOT NULL,
    mem_address                  VARCHAR2(40)   NOT NULL,
    mem_detail_address           VARCHAR2(30)   NOT NULL,
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
    noti_id       NUMBER(4, 0),
    receiver_id   NUMBER(4, 0)   NOT NULL,
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
    prov_id            NUMBER(4, 0),
    biz_name           VARCHAR2(30) NOT NULL,
    biz_reg_num        VARCHAR2(30) NOT NULL,

    CONSTRAINT pk_fct_provider PRIMARY KEY (prov_id),
    CONSTRAINT fk_fct_provider_prov_id FOREIGN KEY (prov_id)
        REFERENCES member (mem_id)
);

CREATE TABLE account (
    prov_id            NUMBER(4, 0),
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
    report_id      NUMBER(4, 0),
    reporter_id    NUMBER(4, 0)     NOT NULL,
    reported_id    NUMBER(4, 0)     NOT NULL,
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
    fct_id                        NUMBER(4, 0),
    cat_id                        CHAR(3)        NOT NULL,
    prov_id                       NUMBER(4, 0)   NOT NULL,
    fct_name                      VARCHAR2(50)   NOT NULL,
    fct_postal_code               CHAR(5)        NOT NULL,
    fct_address                   VARCHAR2(40)   NOT NULL,
    fct_detail_address            VARCHAR2(30)   NOT NULL,
    fct_tel                       VARCHAR2(15)   NOT NULL,
    fct_guide                     VARCHAR2(1000) NOT NULL,
    is_open_on_holidays           CHAR(1)        NOT NULL,
    fct_open_time                 DATE           NOT NULL,
    fct_close_time                DATE           NOT NULL,
    unit_usage_time               NUMBER(4, 0)   NOT NULL,
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
    CONSTRAINT chk_fct_state CHECK (fct_state IN ('REG_REQUESTED', 'REGISTERED', 'DEL_REQUESTED', 'DEL_APPROVED', 'DEL_REJECTED')),
    CONSTRAINT fct_is_open_on_holidays_domain CHECK (is_open_on_holidays IN ('Y', 'N'))
);

CREATE TABLE additional_info (
    addinfo_id      NUMBER(4, 0),
    fct_id          NUMBER(4, 0) NOT NULL,
    addinfo_desc    VARCHAR2(500) NOT NULL,
 
    CONSTRAINT pk_addinfo PRIMARY KEY (addinfo_id),
    CONSTRAINT fk_addinfo_fct_id FOREIGN KEY (fct_id)
        REFERENCES facility (fct_id)
);

CREATE TABLE zone (
    zone_id            NUMBER(4, 0),
    fct_id             NUMBER(4, 0)  NOT NULL,
    zone_name          VARCHAR2(30)  NOT NULL,
    max_user_count     NUMBER(4, 0)  NOT NULL,
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
    img_id              NUMBER(4, 0),
    fct_id              NUMBER(4, 0),
    zone_id             NUMBER(4, 0),
    mem_id              NUMBER(4, 0),
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
    rvt_id                 NUMBER(4, 0),
    cons_id                NUMBER(4, 0)  NOT NULL,
    zone_id                NUMBER(4, 0)  NOT NULL,
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
    rvt_id                NUMBER(4, 0),
    pay_price             NUMBER(6, 0)  NOT NULL,
    pay_state             CHAR(1)       DEFAULT 'P',
    pay_method            VARCHAR2(10)  NOT NULL,
    pay_date              DATE          DEFAULT SYSDATE,
    cancel_date           DATE,
    refund_account        VARCHAR2(20),
    refund_bank_code      CHAR(3),
    refund_account_owner  VARCHAR2(30),

    CONSTRAINT pk_payment PRIMARY KEY (rvt_id),
    CONSTRAINT fk_payment_rvt_id FOREIGN KEY (rvt_id)
        REFERENCES reservation (rvt_id),
    CONSTRAINT payment_pay_state_domain CHECK (pay_state IN ('P', 'Q', 'R')),
    CONSTRAINT fk_payment_refund_bank_code FOREIGN KEY (refund_bank_code)
        REFERENCES bank (bank_code)
);