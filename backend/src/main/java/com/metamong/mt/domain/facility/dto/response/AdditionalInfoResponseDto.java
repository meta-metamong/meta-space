package com.metamong.mt.domain.facility.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class AdditionalInfoResponseDto {
    private Long addinfoId;
    private String addinfoDesc;
}

/*
 * <resultMap type="com.metamong.mt.domain.facility.dto.response.FacilityResponseDto" id="facilityDto">
        <id property="fctId" column="fct_id" jdbcType="BIGINT" />
        <result property="fctName" column="fct_name" jdbcType="VARCHAR" />
        <result property="fctPostalCode" column="fct_postal_code" jdbcType="CHAR" />
        <result property="fctAddress" column="fct_address" jdbcType="VARCHAR" />
        <result property="fctDetailAddress" column="fct_detail_address" jdbcType="VARCHAR" />
        <result property="fctLatitude" column="fct_latitude" jdbcType="DOUBLE" />
        <result property="fctLongitude" column="fct_longitude" jdbcType="DOUBLE" />
        <result property="fctTel" column="fct_tel" jdbcType="VARCHAR" />
        <result property="catId" column="cat_id" jdbcType="CHAR" />
        <result property="catName" column="cat_name" jdbcType="VARCHAR" />
        <result property="fctGuide" column="fct_guide" jdbcType="VARCHAR" />
        <result property="isOpenOnHolidays" column="is_open_on_holidays" jdbcType="CHAR" />
        <result property="fctOpenTime" column="fct_open_time" jdbcType="DATE" />
        <result property="fctClosetime" column="fct_close_time" jdbcType="DATE" />
        <result property="fctState" column="fct_state" jdbcType="VARCHAR" />
        <collection property="fctImages" ofType="com.metamong.mt.domain.facility.dto.response.FacilityImageResponseDto">
            <result property="fctImgDisplayOrder" column="fct_img_display_order" jdbcType="INTEGER" />
            <result property="fctImgUrl" column="fct_img_path" jdbcType="VARCHAR" />
        </collection>
        <collection property="zones" ofType="com.metamong.mt.domain.facility.dto.response.ZoneResponseDto">
            <id property="zoneId" column="zone_id" jdbcType="BIGINT" />
            <result property="zoneName" column="zone_name" jdbcType="VARCHAR" />
            <result property="maxUserCount" column="max_user_count" jdbcType="INTEGER" />
            <result property="hourlyRate" column="hourly_rate" jdbcType="INTEGER" />
            <result property="is_shared_zone" column="is_shared_zone" jdbcType="CHAR" />
            <collection property="zoneImgs" ofType="com.metamong.mt.domain.facility.dto.response.ZoneImageResponseDto">
                <result property="zoneImgDisplayOrder" column="zone_img_display_order" jdbcType="INTEGER" />
                <result property="zoneImgUrl" column="zone_img_path" jdbcType="VARCHAR" />
            </collection>
        </collection>
        <collection property="additionalInfos" ofType="java.lang.String">
            <id property="addinfoId" column="addinfo_id" jdbcType="BIGINT" />
            <result property="addinfoDesc" column="addinfo_desc" jdbcType="VARCHAR" />
        </collection>
    </resultMap>
*/
