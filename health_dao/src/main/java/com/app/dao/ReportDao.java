package com.app.dao;


import java.util.List;
import java.util.Map;

public interface ReportDao {
    Integer countUserByMonth(String date);

    List<Map> countByPackageName();
}
