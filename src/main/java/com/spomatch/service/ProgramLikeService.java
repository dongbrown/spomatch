package com.spomatch.service;

import com.spomatch.dto.ProgramDTO;

import java.util.List;

public interface ProgramLikeService {
    boolean toggleLike(Long programId, Long memberId);
    void addRecentLike(Long programId);
    void updateTopLikedPrograms();
    List<ProgramDTO> getTopLikedPrograms();
    boolean checkLikeStatus(Long programId, Long memberId);
    List<ProgramDTO> getLikedPrograms(Long memberId);

    void unlike(Long programId, Long memberId);
}
