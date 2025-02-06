package com.metamong.mt.domain.facility.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.metamong.mt.domain.facility.dto.request.FacilityRegistrationRequestDto;
import com.metamong.mt.domain.facility.dto.request.ImageRequestDto;
import com.metamong.mt.domain.facility.dto.request.ZoneRegistrationRequestDto;
import com.metamong.mt.domain.facility.dto.response.FacilityRegistrationResponseDto;
import com.metamong.mt.domain.facility.dto.response.ImageUploadUrlResponseDto;
import com.metamong.mt.domain.facility.model.AdditionalInfo;
import com.metamong.mt.domain.facility.model.Facility;
import com.metamong.mt.domain.facility.repository.jpa.FacilityRepository;
import com.metamong.mt.domain.facility.repository.mybatis.FacilityMapper;
import com.metamong.mt.global.file.FilenameResolver;
import com.metamong.mt.global.image.model.FacilityImage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultFacilityService implements FacilityService {
    private final FacilityRepository facilityRepository;
    private final FacilityMapper facilityMapper;
    private final FilenameResolver filenameResolver;
    
    @Override
    public FacilityRegistrationResponseDto registerFacility(FacilityRegistrationRequestDto dto) {
        Facility newFacility = dto.toEntity();
        this.facilityRepository.save(newFacility);
        
        Map<Integer, String> faciltiyFilePathByOrder = new HashMap<>();
        for (ImageRequestDto imageRequestDto : dto.getImages()) {
            faciltiyFilePathByOrder.put(imageRequestDto.getOrder(),
                    this.filenameResolver.generateUuidFilename(imageRequestDto.getFileType()));
        }
        
        for (Map.Entry<Integer, String> entry : faciltiyFilePathByOrder.entrySet()) {
            newFacility.addFctImage(new FacilityImage(entry.getValue(), entry.getKey(), newFacility));
        }
        
        dto.getZones().stream()
                .map(ZoneRegistrationRequestDto::toEntity)
                .forEach((zone) -> this.facilityMapper.saveZone(zone));
        
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
                List.of() // TODO: zone image upload url list
        );
    }
}
