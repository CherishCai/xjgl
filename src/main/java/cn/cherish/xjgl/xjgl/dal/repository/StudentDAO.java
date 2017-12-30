package cn.cherish.xjgl.xjgl.dal.repository;

import cn.cherish.xjgl.xjgl.dal.entity.Student;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentDAO extends IBaseDAO<Student,Long> {

    Student findBySno(String sno);

    @Query("SELECT stu FROM Student AS stu ")
    List<Student> listAllPaged(Pageable pageable);

}
