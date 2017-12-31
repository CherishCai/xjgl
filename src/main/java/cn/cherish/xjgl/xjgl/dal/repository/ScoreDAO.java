package cn.cherish.xjgl.xjgl.dal.repository;

import cn.cherish.xjgl.xjgl.dal.entity.Score;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface ScoreDAO extends IBaseDAO<Score,Long> {

    List<Score> findBySno(String sno);

    @Query("SELECT sc FROM Score AS sc ")
    List<Score> listAllPaged(Pageable pageable);

}
