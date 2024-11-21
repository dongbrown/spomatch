package com.spomatch.dto.response;

import com.spomatch.common.paging.PagingDTO;
import com.spomatch.dto.ProgramDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class ProgramListResponseDTO {
    private List<ProgramDTO> programs;     // 강좌 목록
    private PagingDTO paging;              // 페이징 정보
    private Map<String, Object> filters;    // 필터 옵션들
}
