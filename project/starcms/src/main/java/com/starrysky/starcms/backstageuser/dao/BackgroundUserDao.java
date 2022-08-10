package com.starrysky.starcms.backstageuser.dao;

import com.starrysky.starcms.entity.BackgroundUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName BackgroundUserDao
 * @Description
 * @Author adi
 * @Date 2022-07-26 17:24
 */
public interface BackgroundUserDao extends JpaRepository<BackgroundUser, Integer>, JpaSpecificationExecutor<BackgroundUser> {
    public BackgroundUser findByName(String name);

    public BackgroundUser findByNameAndState(String name, int state);

    public default Page<BackgroundUser> findDynamic(String name, String realName, Integer state, int pageNum, int pageSize){
        Specification<BackgroundUser> specification = (Specification<BackgroundUser>) (root, criteriaQuery, criteriaBuilder) -> {
            //用于暂时存放查询条件的集合
            List<Predicate> predicatesList = new ArrayList<>();
            if (!StringUtils.isEmpty(name)) {
                Predicate namePredicate = criteriaBuilder.like(root.get("name"), "%" + name + "%");
                predicatesList.add(namePredicate);
            }
            if (!StringUtils.isEmpty(realName)) {
                Predicate realNamePredicate = criteriaBuilder.like(root.get("realName"), "%" + realName + "%");
                predicatesList.add(realNamePredicate);
            }
            if(state != null) {
                Predicate statusPredicate = criteriaBuilder.equal(root.get("state"), state);
                predicatesList.add(statusPredicate);
            }
            Predicate[] predicates = new Predicate[predicatesList.size()];
            return criteriaBuilder.and(predicatesList.toArray(predicates));
        };
        return this.findAll(specification, PageRequest.of((pageNum - 1) , pageSize, Sort.Direction.DESC, "registTime"));
    }
}
