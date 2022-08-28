package com.starrysky.starcms.content.dao;

import com.starrysky.starcms.entity.Content;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ContentDao
 * @Description
 * @Author adi
 * @Date 2022-08-02 08:57
 */
public interface ContentDao extends JpaRepository<Content, Integer>, JpaSpecificationExecutor<Content> {
    public default Page<Content> findDynamic(String title, Boolean recommend, Integer status, Integer[] channelIds, Integer userId, String name, String realName, int pageNum, int pageSize) {
        Specification<Content> specification = (Specification<Content>) (root, criteriaQuery, criteriaBuilder) -> {
            //用于暂时存放查询条件的集合
            List<Predicate> predicatesList = new ArrayList<>();
            if (!StringUtils.isEmpty(title)) {
                Predicate namePredicate = criteriaBuilder.like(root.get("title"), "%" + title + "%");
                predicatesList.add(namePredicate);
            }
            if (recommend != null) {
                Predicate namePredicate = criteriaBuilder.equal(root.get("recommend"), recommend);
                predicatesList.add(namePredicate);
            }
            if (status != null && status > 0 && status <= 5) {
                Predicate namePredicate = criteriaBuilder.equal(root.get("status"), status);
                predicatesList.add(namePredicate);
            }
            if (channelIds != null) {
                if (channelIds.length > 1) {
                    CriteriaBuilder.In<Integer> in = criteriaBuilder.in(root.join("channel").get("id"));
                    for (Integer temp : channelIds) {
                        try {
                            in.value(temp);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    predicatesList.add(in);
                } else {
                    Predicate channelPredicate = criteriaBuilder.equal(root.join("channel").get("id"), channelIds[0]);
                    predicatesList.add(channelPredicate);
                }
            }
            if (userId != null) {
                Predicate userIdPredicate = criteriaBuilder.equal(root.join("user").get("id"), userId);
                predicatesList.add(userIdPredicate);
            }
            if (!StringUtils.isEmpty(name)) {
                Predicate realNamePredicate = criteriaBuilder.like(root.join("user").get("name"), "%" + name + "%");
                predicatesList.add(realNamePredicate);
            }
            if (!StringUtils.isEmpty(realName)) {
                Predicate realNamePredicate = criteriaBuilder.like(root.join("user").get("realName"), "%" + realName + "%");
                predicatesList.add(realNamePredicate);
            }
            Predicate[] predicates = new Predicate[predicatesList.size()];
            return criteriaBuilder.and(predicatesList.toArray(predicates));
        };
        return this.findAll(specification, PageRequest.of((pageNum - 1), pageSize, Sort.Direction.DESC, "addTime"));
    }

    @Query(value = "select DATE_FORMAT(c.add_time, '%Y-%m-%d') myaddtime, count(c.id) from ct_content c where c.user_id = ?1 and c.status = ?2 and c.add_time between ?3 and ?4 group by myaddtime", nativeQuery = true)
    public List<Object[]> countByUserIdAndYear(Integer userId, int status, String begin, String end);
}
