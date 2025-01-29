CREATE TABLE member (
    user_id                      VARCHAR2(50)    NOT NULL,
    name                         VARCHAR2(100)   NOT NULL,
    password                     VARCHAR2(255)   NOT NULL,
    phone                        VARCHAR2(20),
    email                        VARCHAR2(100)   UNIQUE,  
    birth                        DATE,
    postal_code                  VARCHAR2(20),
    detail_address               VARCHAR2(255),
    address                      VARCHAR2(255),
    role                         VARCHAR2(50),
    refresh_token                VARCHAR2(255),
    business_name                VARCHAR2(100),
    business_registration_number VARCHAR2(50),
    
    CONSTRAINT pk_member PRIMARY KEY (user_id)  
);