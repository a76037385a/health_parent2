package com.app.service;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.app.dao.CheckGroupDao;
import com.app01.entity.PageResult;
import com.app01.entity.QueryPageBean;
import com.app01.pojo.CheckGroup;
import com.app01.pojo.CheckItem;
import com.app01.service.CheckGroupService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CheckGroupService.class)
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    CheckGroupDao checkGroupDao;



    @Override
    public List<CheckItem> getAllCheckitem() {
        return checkGroupDao.getAllCheckitem();
    }

    @Override
    @Transactional
    public void addCheckGroup(CheckGroup checkGroup, Integer[] checkGroupids) {
        checkGroupDao.addCheckGroup(checkGroup);
        setCheckGroupAndCheckItem(checkGroup.getId(),checkGroupids);
    }

    @Override
    @Transactional
    public void setCheckGroupAndCheckItem(int checkGroupId, Integer[] checkGroupids) {

        if (checkGroupids != null && checkGroupids.length>0){
            Map<String, Integer> map = new HashMap<>();
            map.put("checkgroup_id",checkGroupId);
            for (Integer checkItemid : checkGroupids) {
                //(#{checkgroup_id},#{checkitem_id})
                map.put("checkitem_id",checkItemid);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }

    }

    @Override
    public Map getCheckGroupById(int id) {
        Map<String, Object> map = new HashMap<>();
        map.put("checkGroup",checkGroupDao.getCheckGroupById(id));
        map.put("pickedCheckItems",checkGroupDao.getCheckItemsByCheckGroupID(id));
        return map;
    }

    @Override
    @Transactional
    public void updateCheckGroup(CheckGroup checkGroup, Integer[] pickedCheckItems)  {
        try {
            //删除全部原有检查组关联数据
            checkGroupDao.delCheckgroupAndcheckitemByCheckGroupId(checkGroup.getId());
            //新增用户选择检查组关联数据
            setCheckGroupAndCheckItem(checkGroup.getId(), pickedCheckItems);
            //更新检查组信息
            checkGroupDao.updateCheckGroup(checkGroup);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void delByCheckGroupId(int id) {
        try {
            checkGroupDao.delCheckgroupAndcheckitemByCheckGroupId(id);
            checkGroupDao.delCheckGroupById(id);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        if (queryPageBean.getQueryString() != null){
            queryPageBean.setQueryString("%"+queryPageBean.getQueryString()+"%");
        }
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<CheckGroup> page = checkGroupDao.findPage(queryPageBean.getQueryString());

        return new PageResult(page.getTotal(),page.getResult());
    }








}
