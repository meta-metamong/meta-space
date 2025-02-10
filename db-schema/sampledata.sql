INSERT INTO member (
    mem_id,
    email,
    mem_name,
    password,
    mem_phone,
    birth_date,
    gender,
    mem_postal_code,
    mem_address,
    mem_detail_address,
    role
) VALUES (
    21,
    'testprov@gmail.com',
    'testprov',
    '1q2w3e4r',
    '010-1234-1234',
    TO_DATE('1988-05-27', 'yyyy-MM-dd'),
    'M',
    '01234',
    'address',
    'detail addr',
    'ROLE_PROV'
);

INSERT INTO bank (
    bank_code,
    bank_name
) VALUES (
    '500',
    'Shina Bank'
);

INSERT INTO fct_provider (
    prov_id,
    biz_name,
    biz_reg_num
) VALUES (
    21,
    '363857357-462624',
    'BIZ REG NAME'
);

INSERT INTO category (
    cat_id,
    cat_name
) VALUES (
    '300',
    'Some category'
);

INSERT INTO facility (
    fct_id,
    cat_id,
    prov_id,
    fct_name,
    fct_postal_code,
    fct_address,
    fct_detail_address,
    fct_tel,
    fct_guide,
    is_open_on_holidays,
    fct_open_time,
    fct_close_time,
    unit_usage_time,
    fct_state,
    fct_latitude,
    fct_longitude
) VALUES (
    fct_pk_seq.NEXTVAL,
    '300',
    21,
    'FACILITY',
    '54321',
    'Somewhere',
    'Over the rainbow',
    '02-2612-2555',
    'How can do that',
    'Y',
    TO_DATE('08:00:00', 'HH24:MI:SS'),
    TO_DATE('18:00:00', 'HH24:MI:SS'),
    30,
    'REGISTERED',
    37.513513,
    129.415341 
);

INSERT INTO additional_info (
    addinfo_id,
    fct_id,
    addinfo_desc
) VALUES (
    addinfo_pk_seq.NEXTVAL,
    fct_pk_seq.CURRVAL,
    'Hello1'
);

INSERT INTO additional_info (
    addinfo_id,
    fct_id,
    addinfo_desc
) VALUES (
    addinfo_pk_seq.NEXTVAL,
    fct_pk_seq.CURRVAL,
    'Hello2'
);

INSERT INTO zone (
    zone_id,
    fct_id,
    zone_name,
    max_user_count,
    is_shared_zone,
    hourly_rate
) VALUES (
    zone_pk_seq.NEXTVAL,
    fct_pk_seq.CURRVAL,
    'ZONE1',
    50,
    'Y',
    5000
);

INSERT INTO zone (
    zone_id,
    fct_id,
    zone_name,
    max_user_count,
    is_shared_zone,
    hourly_rate
) VALUES (
    zone_pk_seq.NEXTVAL,
    fct_pk_seq.CURRVAL,
    'ZONE2',
    100,
    'N',
    50000
);

INSERT INTO image (
    img_id,
    fct_id,
    img_path,
    img_attached_to,
    img_display_order
) VALUES (
    img_pk_seq.NEXTVAL,
    fct_pk_seq.CURRVAL,
    'http://localhost:8080/resources/images/zone-image1.jpg',
    'F',
    1
);