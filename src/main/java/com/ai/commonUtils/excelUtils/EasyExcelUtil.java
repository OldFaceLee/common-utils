/*
package com.ai.commonUtils.excelUtils;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import org.apache.poi.ss.formula.functions.T;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

*/
/**
 * @author: lixuejun
 * @date: Create in 2019/11/7 上午11:11
 * @description:
 *//*

public class EasyExcelUtil {

    public static class ExcelHeaderEntity extends BaseRowModel {

        public Integer getOrderNum() {
            return orderNum;
        }

        public void setOrderNum(Integer orderNum) {
            this.orderNum = orderNum;
        }

        public String getTestcaseName() {
            return testcaseName;
        }

        public void setTestcaseName(String testcaseName) {
            this.testcaseName = testcaseName;
        }

        @ExcelProperty(value = "序号",index = 0)
        private Integer orderNum;

        @ExcelProperty(value = "用例名称",index = 1)
        private String testcaseName;
    }

    public static void writeExcel(String filePath) {
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(new File(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ExcelWriter writer = new ExcelWriter(outputStream, ExcelTypeEnum.XLSX,true);
        Sheet sheet1 = new Sheet(1,1,ExcelHeaderEntity.class);
        sheet1.setSheetName("easyExcelSheet");

        List<ExcelHeaderEntity> data = new ArrayList<>();
        List<String> methodName = new ArrayList<>();
//        for(Method method : JdbcUtil.class.getDeclaredMethods()){
            methodName.add(method.getName());
        }
        for (int i = 0; i < methodName.size(); i++) {
            ExcelHeaderEntity items = new ExcelHeaderEntity();
            items.orderNum = i;
            items.testcaseName = methodName.get(i);
            data.add(items);
        }
        writer.write(data,sheet1);
        writer.finish();
    }

}
*/
