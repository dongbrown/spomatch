package com.spomatch.dao;

import com.spomatch.dto.ProgramDTO;
import com.spomatch.dto.request.ProgramSearchRequestDTO;
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

    @Override
    public int selectProgramCount(ProgramSearchRequestDTO searchDTO) {
        return sqlSession.selectOne(NAMESPACE + "selectProgramCount", searchDTO);
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
    public boolean checkLikeStatus(Long programId, String userId) {
        Map<String, Object> params = Map.of(
                "programId", programId,
                "userId", userId
        );
        Integer count = sqlSession.selectOne(NAMESPACE + "checkLikeStatus", params);
        return count != null && count > 0;
    }

    @Override
    public void insertProgramLike(Long programId, String userId) {
        Map<String, Object> params = Map.of(
                "programId", programId,
                "userId", userId
        );
        sqlSession.insert(NAMESPACE + "insertProgramLike", params);
    }

    @Override
    public void deleteProgramLike(Long programId, String userId) {
        Map<String, Object> params = Map.of(
                "programId", programId,
                "userId", userId
        );
        sqlSession.delete(NAMESPACE + "deleteProgramLike", params);
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
    public List<ProgramDTO> selectRecommendedPrograms(String userId, int limit) {
        Map<String, Object> params = Map.of(
                "userId", userId,
                "limit", limit
        );
        return sqlSession.selectList(NAMESPACE + "selectRecommendedPrograms", params);
    }

    @Override
    public List<String> selectRegionList() {
        return sqlSession.selectList(NAMESPACE + "selectRegionList");
    }

    @Override
    public List<String> selectSportTypeList() {
        return sqlSession.selectList(NAMESPACE + "selectSportTypeList");
    }

    @Override
    public List<String> selectAgeTypeList() {
        return sqlSession.selectList(NAMESPACE + "selectAgeTypeList");
    }
}
