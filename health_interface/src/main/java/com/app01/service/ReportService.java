package com.app01.service;


import com.app01.entity.Result;

import java.util.List;
import java.util.Map;

public interface ReportService {

    Integer countUserByMonth(String date);

    List<Map> countByPackageName();

    Result getBusinessReportData() throws Exception;

}
