package com.starrysky.starcms.journal.service;

import com.starrysky.starcms.entity.BackgroundUser;
import com.starrysky.starcms.entity.Journal;
import com.starrysky.starcms.journal.dao.JournalDao;
import com.starrysky.starcms.util.Constant;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName JournalService
 * @Description
 * @Author adi
 * @Date 2022-08-18 19:55
 */
@Service
@Transactional
public class JournalService {
    @Resource
    private JournalDao journalDao;

    @Transactional(readOnly = true)
    public Page<Journal> list(String title, Integer state, int pageNum, int pageSize) throws Exception {
        return journalDao.findDynamic(title, state, pageNum, pageSize);
    }

    @Transactional(readOnly = true)
    public List<Journal> listNormal() {
        return journalDao.findByState(Constant.STATE_NORMAL);
    }

    @Transactional(readOnly = true)
    public boolean getByTitle(String title) {
        return this.journalDao.findByTitle(title) != null;
    }

    @Transactional(readOnly = true)
    public Journal getById(int id) {
        return this.journalDao.getOne(id);
    }

    public void add(Journal journal) {
        this.journalDao.save(journal);
    }

    public Journal edit(Journal journal) throws Exception {
        Journal dbJournal = this.journalDao.getOne(journal.getId());
//        dbJournal.setTitle(journal.getTitle());
        dbJournal.setBeginTime(journal.getBeginTime());
        dbJournal.setEndTime(journal.getEndTime());
        dbJournal.setPublisher(journal.getPublisher());
        dbJournal.setAddress(journal.getAddress());
        dbJournal.setPhone(journal.getPhone());
        dbJournal.setState(journal.getState());
        return this.journalDao.save(dbJournal);
    }

    public void delete(int id) {
        this.journalDao.deleteById(id);
    }

    @Transactional()
    public void deletes(String ids){
        String[] allId = ids.split(",");
        List<Journal> list = new ArrayList<>();
        for(String id : allId){
            if(id!=null && !id.equals("")) {
                Journal journal = new Journal();
                journal.setId(Integer.parseInt(id));
                list.add(journal);
            }
        }
        this.journalDao.deleteInBatch(list);
    }

}
