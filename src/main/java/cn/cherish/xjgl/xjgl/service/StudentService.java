package cn.cherish.xjgl.xjgl.service;

import cn.cherish.xjgl.xjgl.common.enums.ErrorCode;
import cn.cherish.xjgl.xjgl.common.exception.ServiceException;
import cn.cherish.xjgl.xjgl.dal.entity.Student;
import cn.cherish.xjgl.xjgl.dal.repository.IBaseDAO;
import cn.cherish.xjgl.xjgl.dal.repository.StudentDAO;
import cn.cherish.xjgl.xjgl.util.ObjectConvertUtil;
import cn.cherish.xjgl.xjgl.web.dto.StudentDTO;
import cn.cherish.xjgl.xjgl.web.req.BasicSearchReq;
import cn.cherish.xjgl.xjgl.web.req.StudentReq;
import cn.cherish.xjgl.xjgl.web.req.StudentSearchReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StudentService extends ABaseService<Student, Long> {

    private final StudentDAO studentDAO;

    @Autowired
    public StudentService(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    @Override
    protected IBaseDAO<Student, Long> getEntityDAO() {
        return studentDAO;
    }

    public StudentDTO findOne(Long studentId) {
        Student student = studentDAO.findOne(studentId);
        return student == null ? null : ObjectConvertUtil.objectCopy(new StudentDTO(), student);
    }

    public Student findBySno(String sno){
        return studentDAO.findBySno(sno);
    }

    public boolean exist(String sno) {
        return studentDAO.findBySno(sno) != null;
    }

    @Transactional
    public void delete(Long studentId) {
        Student student = studentDAO.findOne(studentId);
        if (student == null) return;
        super.delete(studentId);
    }

    public Page<StudentDTO> findAll(BasicSearchReq basicSearchReq, StudentSearchReq studentSearchReq) {

        int pageNumber = basicSearchReq.getStartIndex() / basicSearchReq.getPageSize() + 1;
        PageRequest pageRequest = super.buildPageRequest(pageNumber, basicSearchReq.getPageSize());

        //除了分页条件没有特定搜索条件的，为了缓存count
        if (ObjectConvertUtil.objectFieldIsBlank(studentSearchReq)){
            log.debug("没有特定搜索条件的");
            List<Student> studentList = studentDAO.listAllPaged(pageRequest);
            List<StudentDTO> CustomerDTOList = studentList.stream().map(source -> {
                StudentDTO studentDTO = new StudentDTO();
                ObjectConvertUtil.objectCopy(studentDTO, source);
                return studentDTO;
            }).collect(Collectors.toList());

            //为了计算总数使用缓存，加快搜索速度
            Long count = getCount();
            return new PageImpl<>(CustomerDTOList, pageRequest, count);
        }

        //有了其它搜索条件
        Page<Student> studentPage = super.findAllBySearchParams(
                buildSearchParams(studentSearchReq), pageNumber, basicSearchReq.getPageSize());

        return studentPage.map(source -> {
            StudentDTO studentDTO = new StudentDTO();
            ObjectConvertUtil.objectCopy(studentDTO, source);
            return studentDTO;
        });
    }

    @Transactional
    public void update(StudentReq studentReq) {
        Student student = this.findById(studentReq.getId());
        if (student == null) return;

        ObjectConvertUtil.objectCopy(student, studentReq);
        student.setModifiedTime(new Date());
        this.update(student);

    }

    /**
     * 管理员手动添加会员
     * @param studentReq 会员信息
     */
    @Transactional
    public Student save(StudentReq studentReq) {
        if (exist(studentReq.getSno())) {
            throw new ServiceException(ErrorCode.ERROR_CODE_400,"学号已存在");
        }

        Student student = new Student();
        ObjectConvertUtil.objectCopy(student, studentReq);
        student.setCreatedTime(new Date());
        student.setModifiedTime(new Date());
        return save(student);
    }

}
