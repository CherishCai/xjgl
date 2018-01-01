package cn.cherish.xjgl.xjgl.service;

import cn.cherish.xjgl.xjgl.common.enums.SubjectEnum;
import cn.cherish.xjgl.xjgl.dal.entity.Score;
import cn.cherish.xjgl.xjgl.dal.entity.Student;
import cn.cherish.xjgl.xjgl.dal.repository.IBaseDAO;
import cn.cherish.xjgl.xjgl.dal.repository.ScoreDAO;
import cn.cherish.xjgl.xjgl.dal.repository.StudentDAO;
import cn.cherish.xjgl.xjgl.util.ExcelUtil;
import cn.cherish.xjgl.xjgl.util.ObjectConvertUtil;
import cn.cherish.xjgl.xjgl.web.dto.ScoreDTO;
import cn.cherish.xjgl.xjgl.web.req.BasicSearchReq;
import cn.cherish.xjgl.xjgl.web.req.ScoreReq;
import cn.cherish.xjgl.xjgl.web.req.ScoreSearchReq;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ScoreService extends ABaseService<Score, Long> {

    private final ScoreDAO scoreDAO;
    private final StudentDAO studentDAO;

    @Autowired
    public ScoreService(ScoreDAO scoreDAO, StudentDAO studentDAO) {
        this.scoreDAO = scoreDAO;
        this.studentDAO = studentDAO;
    }

    @Override
    protected IBaseDAO<Score, Long> getEntityDAO() {
        return scoreDAO;
    }

    public ScoreDTO findOne(Long scoreId) {
        Score score = scoreDAO.findOne(scoreId);
        return score == null ? null : ObjectConvertUtil.objectCopy(new ScoreDTO(), score);
    }

    public List<Score> findBySno(String sno){
        return scoreDAO.findBySno(sno);
    }

    @Transactional
    public void delete(Long scoreId) {
        Score score = scoreDAO.findOne(scoreId);
        if (score == null) return;
        super.delete(scoreId);
    }

    public Page<ScoreDTO> findAll(BasicSearchReq basicSearchReq, ScoreSearchReq scoreSearchReq) {

        int pageNumber = basicSearchReq.getStartIndex() / basicSearchReq.getPageSize() + 1;
        PageRequest pageRequest = super.buildPageRequest(pageNumber, basicSearchReq.getPageSize());

        //除了分页条件没有特定搜索条件的，为了缓存count
        if (ObjectConvertUtil.objectFieldIsBlank(scoreSearchReq)){
            log.debug("没有特定搜索条件的");
            List<Score> scoreList = scoreDAO.listAllPaged(pageRequest);
            List<ScoreDTO> dtoList = scoreList.stream().map(this::getScoreDTO).collect(Collectors.toList());

            //为了计算总数使用缓存，加快搜索速度
            Long count = getCount();
            return new PageImpl<>(dtoList, pageRequest, count);
        }

        //有了其它搜索条件
        Page<Score> scorePage = super.findAllBySearchParams(
                buildSearchParams(scoreSearchReq), pageNumber, basicSearchReq.getPageSize());

        return scorePage.map(this::getScoreDTO);
    }

    @Transactional
    public void update(ScoreReq scoreReq) {
        Score score = this.findById(scoreReq.getId());
        if (score == null) return;

        ObjectConvertUtil.objectCopy(score, scoreReq);
        score.setModifiedTime(new Date());
        this.update(score);
    }

    /**
     * 管理员手动添加会员
     * @param scoreReq 会员信息
     */
    @Transactional
    public Score save(ScoreReq scoreReq) {
        Score score = new Score();
        ObjectConvertUtil.objectCopy(score, scoreReq);
        score.setCreatedTime(new Date());
        score.setModifiedTime(new Date());
        return save(score);
    }

    private ScoreDTO getScoreDTO(Score source) {
        ScoreDTO scoreDTO = new ScoreDTO();
        ObjectConvertUtil.objectCopy(scoreDTO, source);

        Student bySno = studentDAO.findBySno(source.getSno());
        scoreDTO.setSname(bySno.getNickname());
        scoreDTO.setSubjectStr(SubjectEnum.getDesc(scoreDTO.getSubject()));
        return scoreDTO;
    }

    @Transactional
    public boolean dealExcel(InputStream is) throws IOException {
        List<Map> maps = ExcelUtil.readExcel2003(is);

        maps.forEach(map -> {
            String sno = (String) map.get("学号");
            String subject = (String) map.get("学科");
            Float num = Float.valueOf((String) map.get("成绩"));

            SubjectEnum subjectEnum = SubjectEnum.fromDesc(subject);

            Student bySno = studentDAO.findBySno(sno);
            if (bySno != null) {

                Score bySnoAndSubject = scoreDAO.findBySnoAndSubject(sno, subjectEnum.getSeq());
                if (bySnoAndSubject != null) {
                    // update
                    bySnoAndSubject.setNum(num);
                    bySnoAndSubject.setModifiedTime(new Date());
                    this.update(bySnoAndSubject);
                } else {
                    // add
                    Score score = new Score();
                    score.setSno(sno);
                    score.setNum(num);
                    score.setSubject(subjectEnum.getSeq());
                    score.setCreatedTime(new Date());
                    score.setModifiedTime(new Date());

                    this.save(score);
                }

            }
        });
        return true;
    }


}
