package com.ai.commonUtils.annotationUtil;

import org.testng.annotations.Test;

/**
 * @author: lixuejun
 * @date: Create in 2020/3/24 上午11:20
 * @description:
 */
public class TestAnnotation {

    @Test(groups = "RRRR")
    public void t1(){

    }

    @Test(groups = {"R2","R1","H1"})
    public void t2(){

    }


    @Test(groups = {"R4","R1","H1"})
    public void t3(){

    }
}
