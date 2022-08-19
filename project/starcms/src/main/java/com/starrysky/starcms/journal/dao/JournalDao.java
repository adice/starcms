package com.starrysky.starcms.journal.dao;

import com.starrysky.starcms.entity.Journal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName JournalDao
 * @Description
 * @Author adi
 * @Date 2022-08-18 19:40
 */
public interface JournalDao extends JpaRepository<Journal, Integer>, JpaSpecificationExecutor<Journal> {
    public default Page<Journal> findDynamic(String title, Integer state, int pageNum, int pageSize){
        Specification<Journal> specification = (Specification<Journal>) (root, criteriaQuery, criteriaBuilder) -> {
            //用于暂时存放查询条件的集合
            List<Predicate> predicatesList = new ArrayList<>();
            if (!StringUtils.isEmpty(title)) {
                Predicate namePredicate = criteriaBuilder.like(root.get("title"), "%" + title + "%");
                predicatesList.add(namePredicate);
            }
            if(state != null) {
                Predicate statusPredicate = criteriaBuilder.equal(root.get("state"), state);
                predicatesList.add(statusPredicate);
            }
            Predicate[] predicates = new Predicate[predicatesList.size()];
            return criteriaBuilder.and(predicatesList.toArray(predicates));
        };
        return this.findAll(specification, PageRequest.of((pageNum - 1) , pageSize, Sort.Direction.DESC, "id"));
    }

    public Journal findByTitle(String title);

    public List<Journal> findByState(int state);
}
