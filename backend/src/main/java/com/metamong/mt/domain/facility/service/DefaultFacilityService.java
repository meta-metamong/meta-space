package com.metamong.mt.domain.facility.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

import com.metamong.mt.domain.facility.dto.request.FacilityRegistrationRequestDto;
import com.metamong.mt.domain.facility.dto.request.ImageRequestDto;
import com.metamong.mt.domain.facility.dto.request.ZoneRegistrationRequestDto;
import com.metamong.mt.domain.facility.dto.response.FacilityRegistrationResponseDto;
import com.metamong.mt.domain.facility.dto.response.FacilityResponseDto;
import com.metamong.mt.domain.facility.dto.response.ImageUploadUrlResponseDto;
import com.metamong.mt.domain.facility.dto.response.ZoneImageUploadUrlResponseDto;
import com.metamong.mt.domain.facility.model.AdditionalInfo;
import com.metamong.mt.domain.facility.model.Facility;
import com.metamong.mt.domain.facility.model.FacilityImage;
import com.metamong.mt.domain.facility.model.Zone;
import com.metamong.mt.domain.facility.model.ZoneImage;
import com.metamong.mt.domain.facility.repository.jpa.FacilityRepository;
import com.metamong.mt.domain.facility.repository.jpa.ZoneRepository;
import com.metamong.mt.domain.facility.repository.mybatis.FacilityMapper;
import com.metamong.mt.global.file.FileUploader;
import com.metamong.mt.global.file.FilenameResolver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultFacilityService implements FacilityService {
    private final FacilityRepository facilityRepository;
    private final ZoneRepository zoneRepository;
    private final FacilityMapper facilityMapper;
    private final FilenameResolver filenameResolver;
    private final FileUploader fileUploader;
    
    @Override
    public FacilityRegistrationResponseDto registerFacility(FacilityRegistrationRequestDto dto) {
        Facility newFacility = dto.toEntity();
        
        List<ImageUploadUrlResponseDto> fctImageUploadUrlResponseDtos = new ArrayList<>(dto.getImages().size());
        for (ImageRequestDto imageRequestDto : dto.getImages()) {
            String fctUuidFilename = this.filenameResolver.generateUuidFilename(imageRequestDto.getFileType());
            String fctUploadUrl = this.fileUploader.generateUploadUrl(fctUuidFilename);
            String fctFilePath = this.filenameResolver.resolveFileUrl(fctUuidFilename);
            newFacility.addFctImage(new FacilityImage(fctFilePath, imageRequestDto.getOrder(), newFacility));
            fctImageUploadUrlResponseDtos.add(new ImageUploadUrlResponseDto(imageRequestDto.getOrder(), fctUploadUrl));
        }
        this.facilityRepository.save(newFacility);
        
        List<ZoneImageUploadUrlResponseDto> zoneImageUploadUrls = new ArrayList<>();
        for (ZoneRegistrationRequestDto zoneDto : dto.getZones()) {
            Zone zone = zoneDto.toEntity();
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
        
        dto.getAddinfos().stream()
                .map((desc) -> AdditionalInfo.builder()
                        .fctId(newFacility.getFctId())
                        .addinfoDesc(desc)
                        .build())
                .forEach((addinfo) -> this.facilityMapper.saveAdditionalInfo(addinfo));
        
        return new FacilityRegistrationResponseDto(
                newFacility.getFctId(),
                newFacility.getFctImages().stream()
                        .map((image) -> new ImageUploadUrlResponseDto(image.getImgDisplayOrder(), image.getImgPath()))
                        .toList(),
                zoneImageUploadUrls
        );
    }
    
    @Override
    public FacilityResponseDto getFacility(Long facilityId) {
        throw new UnsupportedOperationException();
    }
}
