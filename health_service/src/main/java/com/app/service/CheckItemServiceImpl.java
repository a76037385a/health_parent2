package com.app.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.app.dao.CheckItemDao;
import com.app01.entity.PageResult;
import com.app01.entity.QueryPageBean;
import com.app01.pojo.CheckItem;
import com.app01.service.CheckItemService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service(interfaceClass = CheckItemService.class)
//@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    CheckItemDao checkItemDao;

    @Override
    public void add(CheckItem checkItem) {
        System.out.println(checkItem);
        checkItemDao.add(checkItem);
    }

    @Override
    public PageResult findPageByCondition(QueryPageBean queryPageBean) {
        if (!StringUtils.isEmpty(queryPageBean.getQueryString())){
            queryPageBean.setQueryString("%"+queryPageBean.getQueryString()+"%");
        }
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<CheckItem> page = checkItemDao.findPageByCondition(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void deleteCheckItemById(int id) {
        Long idcount = checkItemDao.findCountByCheckItemId(id);
        if (idcount > 0){
            throw new RuntimeException("检查已被引用");
        }
        checkItemDao.deleteById(id);
    }

    @Override
    public CheckItem findCheckItemById(int id) {

        return checkItemDao.findCheckItemById(id);
    }

    @Override
    public void editCheckItemById(CheckItem checkItem) {
        checkItemDao.updateCheckItem(checkItem);
    }
}
