package com.metamong.mt.domain.facility.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metamong.mt.domain.facility.dto.mapper.FacilityUpdateMapperDto;
import com.metamong.mt.domain.facility.dto.mapper.ZoneUpdateMapperDto;
import com.metamong.mt.domain.facility.dto.request.FacilityListRequestDto;
import com.metamong.mt.domain.facility.dto.request.FacilityRegistrationRequestDto;
import com.metamong.mt.domain.facility.dto.request.FacilityUpdateRequestDto;
import com.metamong.mt.domain.facility.dto.request.ImageRequestDto;
import com.metamong.mt.domain.facility.dto.request.ZoneRegistrationRequestDto;
import com.metamong.mt.domain.facility.dto.request.ZoneUpdateRequestDto;
import com.metamong.mt.domain.facility.dto.response.FacilityListItemResponseDto;
import com.metamong.mt.domain.facility.dto.response.FacilityListOfMemberResponseDto;
import com.metamong.mt.domain.facility.dto.response.FacilityListResponseDto;
import com.metamong.mt.domain.facility.dto.response.FacilityRegistrationResponseDto;
import com.metamong.mt.domain.facility.dto.response.FacilityResponseDto;
import com.metamong.mt.domain.facility.dto.response.FacilityUpdateResponseDto;
import com.metamong.mt.domain.facility.dto.response.ImageUploadUrlResponseDto;
import com.metamong.mt.domain.facility.dto.response.ZoneImageUploadUrlResponseDto;
import com.metamong.mt.domain.facility.exception.FacilityDeleteFailureException;
import com.metamong.mt.domain.facility.model.AdditionalInfo;
import com.metamong.mt.domain.facility.model.Facility;
import com.metamong.mt.domain.facility.model.FacilityImage;
import com.metamong.mt.domain.facility.model.Zone;
import com.metamong.mt.domain.facility.model.ZoneImage;
import com.metamong.mt.domain.facility.repository.jpa.AdditionalInfoRepository;
import com.metamong.mt.domain.facility.repository.jpa.FacilityRepository;
import com.metamong.mt.domain.facility.repository.jpa.ZoneRepository;
import com.metamong.mt.domain.facility.repository.mybatis.FacilityMapper;
import com.metamong.mt.domain.member.service.MemberService;
import com.metamong.mt.global.apispec.CommonListUpdateRequestDto;
import com.metamong.mt.global.apispec.CommonUpdateListItemRequestDto;
import com.metamong.mt.global.file.FileUploader;
import com.metamong.mt.global.file.FilenameResolver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DefaultFacilityService implements FacilityService {
    private final FacilityRepository facilityRepository;
    private final ZoneRepository zoneRepository;
    private final AdditionalInfoRepository additionalInfoRepository;
    private final FacilityMapper facilityMapper;
    private final FilenameResolver filenameResolver;
    private final FileUploader fileUploader;
    private final MemberService memberService;
    
    @Override
    public FacilityRegistrationResponseDto registerFacility(FacilityRegistrationRequestDto dto) {
        Facility facility = dto.toEntity();
        
        List<ImageUploadUrlResponseDto> fctImageUploadUrlResponseDtos = new ArrayList<>(dto.getImages().size());
        for (ImageRequestDto imageRequestDto : dto.getImages()) {
            String fctUuidFilename = this.filenameResolver.generateUuidFilename(imageRequestDto.getFileType());
            String fctUploadUrl = this.fileUploader.generateUploadUrl(fctUuidFilename);
            String fctFilePath = this.filenameResolver.resolveFileUrl(fctUuidFilename);
            facility.addFctImage(new FacilityImage(fctFilePath, imageRequestDto.getOrder(), facility));
            fctImageUploadUrlResponseDtos.add(new ImageUploadUrlResponseDto(imageRequestDto.getOrder(), fctUploadUrl));
        }
        Facility newFacility = this.facilityRepository.save(facility);
        
        List<ZoneImageUploadUrlResponseDto> zoneImageUploadUrls = new ArrayList<>();
        for (ZoneRegistrationRequestDto zoneDto : dto.getZones()) {
            Zone zone = zoneDto.toEntity(newFacility.getFctId());
            List<ImageUploadUrlResponseDto> uploadUrls = new ArrayList<>(zoneDto.getImages().size());
            for (ImageRequestDto zoneImage : zoneDto.getImages()) {
                String zoneUuidFilename = this.filenameResolver.generateUuidFilename(zoneImage.getFileType());
                String zoneUploadUrl = this.fileUploader.generateUploadUrl(zoneUuidFilename);
                String zoneFilePath = this.filenameResolver.resolveFileUrl(zoneUuidFilename);
                zone.addZoneImage(new ZoneImage(zoneFilePath, zoneImage.getOrder(), zone));
                uploadUrls.add(new ImageUploadUrlResponseDto(zoneImage.getOrder(), zoneUploadUrl));
            }
            this.zoneRepository.save(zone);
            zoneImageUploadUrls.add(new ZoneImageUploadUrlResponseDto(zoneDto.getZoneNo(), uploadUrls));
        }
        
        // TODO: don't flush here (Service should be pure)
        this.facilityRepository.flush();
        
        dto.getAddinfos().stream()
                .map((desc) -> AdditionalInfo.builder()
                        .fctId(newFacility.getFctId())
                        .addinfoDesc(desc)
                        .build())
                .forEach((addinfo) -> this.facilityMapper.saveAdditionalInfo(addinfo));
        
        return new FacilityRegistrationResponseDto(
                newFacility.getFctId(),
                fctImageUploadUrlResponseDtos,
                zoneImageUploadUrls
        );
    }
    
    @Override
    public FacilityResponseDto getFacility(Long facilityId) {
        return this.facilityMapper.findFacilityById(facilityId);
    }

    @Override
    public FacilityUpdateResponseDto updateFacility(Long fctId, FacilityUpdateRequestDto dto) {
        FacilityUpdateMapperDto facilityUpdateDto = FacilityUpdateMapperDto.of(fctId, dto);
        if (log.isDebugEnabled()) {
            log.debug("facilityUpdateDto={}", facilityUpdateDto);
        }
        this.facilityMapper.updateFacilityById(facilityUpdateDto);
        
        List<Long> generatedNewZoneIds = updateZones(fctId, dto.getZones());
        List<Long> generatedNewAddinfos = updateAddinfos(fctId, dto.getAddinfos());
        
        return new FacilityUpdateResponseDto(generatedNewZoneIds, generatedNewAddinfos);
    }
    
    private List<Long> updateZones(Long fctId, CommonListUpdateRequestDto<ZoneUpdateRequestDto, Long> zones) {
        // TODO: Zone delete
        
        for (CommonUpdateListItemRequestDto<ZoneUpdateRequestDto, Long> zone : zones.getUpdate()) {
            ZoneUpdateMapperDto zoneUpdateMapperDto = ZoneUpdateMapperDto.of(zone.getId(), zone.getTo());
            log.debug("zoneUpdateMapperDto={}", zoneUpdateMapperDto);
            this.facilityMapper.updateZoneById(zoneUpdateMapperDto);
        }
        
        return zones.getCreate().stream()
                .map((e) -> {
                    Zone newZone = Zone.builder()
                            .zoneName(e.getZoneName())
                            .maxUserCount(e.getMaxUserCount())
                            .isSharedZone(e.getIsSharedZone())
                            .hourlyRate(e.getHourlyRate())
                            .fctId(fctId)
                            .build();
                    newZone = this.zoneRepository.save(newZone);
                    return newZone.getZoneId();
                })
                .toList();
    }
    
    private List<Long> updateAddinfos(Long fctId, CommonListUpdateRequestDto<String, Long> addinfos) {
        if (addinfos.isDeleteAvailable()) {
            this.facilityMapper.deleteAdditionalInfosByIds(addinfos.getDelete());
        }
        
        for (CommonUpdateListItemRequestDto<String, Long> addinfoUpdate : addinfos.getUpdate()) {
            Optional<AdditionalInfo> find = this.additionalInfoRepository.findById(addinfoUpdate.getId());
            find.ifPresent((ai) -> ai.updateAddinfoDesc(addinfoUpdate.getTo()));
        }
        
        return addinfos.getCreate().stream()
                .map((e) -> {
                    AdditionalInfo additionalInfo = AdditionalInfo.builder()
                            .fctId(fctId)
                            .addinfoDesc(e)
                            .build();
                    additionalInfo = this.additionalInfoRepository.save(additionalInfo);
                    return additionalInfo.getAddinfoId();
                })
                .toList();
    }
    
    @Override
    public void deleteFacility(Long fctId, String password) {
        Facility facility = this.facilityRepository.findById(fctId)
                .orElseThrow(() -> new NoSuchElementException());
        
        if (!this.memberService.isValidPassword(facility.getProvId(), password)) {
            throw new FacilityDeleteFailureException("패스워드가 맞지 않습니다.");
        }
        
        facility.requestDelete();
    }
    
    @Override
    public FacilityListResponseDto getFacilities(FacilityListRequestDto dto) {
        List<FacilityListItemResponseDto> facilities =
                this.facilityMapper.findFacilitiesBySearchCondition(dto);
        int totalElementCount = this.facilityMapper.countBySearchCondition(dto);
        
        int page = dto.getPage();
        int pageSize = dto.getPageSize();
        int totalPageCount = totalElementCount / pageSize + (totalElementCount % pageSize == 0 ? 0 : 1);
        return FacilityListResponseDto.builder()
                .page(dto.getPage())
                .pageSize(dto.getPageSize())
                .totalElementCount(totalElementCount)
                .totalPageCount(totalPageCount)
                .isFirst(page == 1)
                .isLast(page == totalPageCount)
                .facilities(facilities)
                .build();
    }
    
    @Override
    public List<FacilityListOfMemberResponseDto> getFacilityOfMember(Long memId) {
        // TODO Auto-generated method stub
        return null;
    }
}
