package com.ai.commonUtils.readcase;

import com.ai.commonUtils.excelUtils.ExcelUtilsPoi;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author: lixuejun
 * @date: Create in 2020/3/24 上午11:07
 * @description:
 */
public class ReadCase {

    /**
     * 自动获取用例名称
     * @param filePath
     * @return
     */
    public List<String> autoReadCaseExcelSheetName(String filePath){
        return Optional.ofNullable(ExcelUtilsPoi.getSheetName(filePath)).orElse(Collections.emptyList());
    }

    public void generateCaseList(List<String> caseName){

    }
}
