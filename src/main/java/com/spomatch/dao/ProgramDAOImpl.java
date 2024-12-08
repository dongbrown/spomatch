package com.spomatch.dao;

import com.spomatch.dto.ProgramDTO;
import com.spomatch.dto.request.ProgramSearchRequestDTO;
import com.spomatch.dto.response.ProgramListResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ProgramDAOImpl implements ProgramDAO {
    private final SqlSession sqlSession;
    private static final String NAMESPACE = "Program.";

    @Override
    public List<ProgramDTO> selectProgramList(ProgramSearchRequestDTO searchDTO) {
        return sqlSession.selectList(NAMESPACE + "selectProgramList", searchDTO);
    }

    //시설 개수로 변경!!
    @Override
    public int selectProgramCount(ProgramSearchRequestDTO searchDTO) {
        return sqlSession.selectOne(NAMESPACE + "selectFacilityCount", searchDTO);
    }

    @Override
    public ProgramDTO selectProgramDetail(Long programId) {
        return sqlSession.selectOne(NAMESPACE + "selectProgramDetail", programId);
    }

    @Override
    public void updateViewCount(Long programId) {
        sqlSession.update(NAMESPACE + "updateViewCount", programId);
    }

    @Override
    public boolean checkLikeStatus(Long programId, Long memberId) {
        Map<String, Object> params = Map.of(
                "programId", programId,
                "memberId", memberId
        );
        return sqlSession.selectOne(NAMESPACE + "checkLikeStatus", params);
    }

    @Override
    public void insertProgramLike(Long programId, Long memberId) {
        Map<String, Object> params = Map.of(
                "programId", programId,
                "memberId", memberId
        );
        sqlSession.insert(NAMESPACE + "insertProgramLike", params);
    }

    @Override
    public void deleteProgramLike(Long programId, Long memberId) {
        Map<String, Object> params = Map.of(
                "programId", programId,
                "memberId", memberId
        );
        sqlSession.delete(NAMESPACE + "deleteProgramLike", params);
    }

    @Override
    public List<ProgramDTO> selectLikedProgramList(Long memberId) {
        return sqlSession.selectList(NAMESPACE + "selectLikedProgramList", memberId);
    }

    @Override
    public Long selectLikeCount(Long programId) {
        return sqlSession.selectOne(NAMESPACE + "selectLikeCount", programId);
    }

    @Override
    public Integer selectRegisterCount(Long programId) {
        return sqlSession.selectOne(NAMESPACE + "selectRegisterCount", programId);
    }

    @Override
    public List<ProgramDTO> selectRecommendedPrograms(Long memberId, int limit) {
        Map<String, Object> params = Map.of(
                "memberId", memberId,
                "limit", limit
        );
        return sqlSession.selectList(NAMESPACE + "selectRecommendedPrograms", params);
    }

    @Override
    public List<String> selectCityList() {
        return sqlSession.selectList(NAMESPACE + "selectCityList");
    }

    @Override
    public List<String> selectDistrictList() {
        return sqlSession.selectList(NAMESPACE + "selectDistrictList");
    }

    @Override
    public List<String> selectFacilityTypeList() {
        return sqlSession.selectList(NAMESPACE + "selectFacilityTypeList");
    }

    @Override
    public List<String> selectProgramTypeList() {
        return sqlSession.selectList(NAMESPACE + "selectProgramTypeList");
    }

    @Override
    public List<String> selectTargetAgeList() {
        return sqlSession.selectList(NAMESPACE + "selectTargetAgeList");
    }
}