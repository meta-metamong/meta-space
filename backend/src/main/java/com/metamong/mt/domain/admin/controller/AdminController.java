package com.metamong.mt.domain.admin.controller;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.WebSocketSession;

import com.metamong.mt.domain.admin.dto.response.MemberSearchResponseDto;
import com.metamong.mt.domain.admin.dto.response.ReportedMemberResponseDto;
import com.metamong.mt.domain.admin.service.AdminService;
import com.metamong.mt.global.apispec.BaseResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
    private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());
	
	private final AdminService adminService;
	
//  public void addSession(WebSocketSession session) {
//  sessions.add(session);
//}
//
//@GetMapping("/members/roleUserCount")
//public String getRoleUserCount() {
//  return memberService.view();
//}
//
//@GetMapping("/download/{filename}")
//public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
//	
//  String fileDirectory = "C:/Users/KOSA/Downloads/";
//  File file = new File(fileDirectory + filename);
//  
//  if (!file.exists()) {
//      return ResponseEntity.notFound().build();  // 파일이 없을 때 처리해놓깅
//  }
//
//  Resource resource = new FileSystemResource(file);
//
//  return ResponseEntity.ok()
//          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")  
//          .contentType(MediaType.parseMediaType("application/octet-stream"))  
//          .body(resource);
//}
	
//	public void removeSession(WebSocketSession session) {
//        sessions.remove(session);
//    }
//
//    public Set<WebSocketSession> getAllSessions() {
//        return sessions;
//    }
	
	@GetMapping("/searchMembers")
	public ResponseEntity<BaseResponse<List<MemberSearchResponseDto>>> searchMembers() {
	    return ResponseEntity.ok(BaseResponse.of(adminService.searchMembers(), HttpStatus.OK));
	}

    
    @GetMapping("/reportedMembers")
    public ResponseEntity<BaseResponse<List<ReportedMemberResponseDto>>> getReportedMembers() {
        return ResponseEntity.ok(BaseResponse.of(adminService.getReportedMembers(), HttpStatus.OK));
    }
    
    @PostMapping("/banMembers")
    public ResponseEntity<String> processReportBans(@RequestBody List<Long> reportedIds) {
        try {
        	adminService.processReportBans(reportedIds);
            return ResponseEntity.ok("정지 작업이 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("정지 작업 중 오류가 발생했습니다.");
        }
    }
    
    @PostMapping("/approval/registration")
    public ResponseEntity<String> approveFacilityRequest(@RequestParam Long provId) {
        try {
        	adminService.approveFacilityRequest(provId);
            return ResponseEntity.ok("등록 요청이 승인되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("승인처리가 실패되었습니다.");
        }
    }
    
    @PostMapping("/approval/deletion")
    public ResponseEntity<String> rejectionFacilityRequest(
            @RequestParam Long provId) {
        try {
        	adminService.rejectFacilityRequest(provId);
            return ResponseEntity.ok("등록 요청이 반려되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("반려처리가 실패되었습니다.");
        }
    }
    
    // 자정마다 반려된 시설 삭제
    
}
