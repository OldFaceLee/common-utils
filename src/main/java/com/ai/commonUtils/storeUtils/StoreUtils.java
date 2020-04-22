package com.ai.commonUtils.storeUtils;

import org.apache.log4j.Logger;

import java.io.File;

public class StoreUtils {

    private static Logger log = Logger.getLogger(StoreUtils.class);

    /**
     * 判断盘符中剩余的空间容量大小
     * @param sizeUnit  MB 或者 GB
     * @param disk   C 盘 或者 其他盘符名称
     * @return
     */
    public static long freeSpace(String sizeUnit,String disk){
        File diskPartition = new File(disk+":");
        long totalCapacity = diskPartition.getTotalSpace();
        long freePartitionSpace = diskPartition.getFreeSpace();
        long usablePatitionSpace = diskPartition.getUsableSpace();
        System.out.println("**** Sizes in Mega Bytes ****\n");
        System.out.println("盘总容量(MB): " + totalCapacity / (1024*1024) + " MB");
        System.out.println("已使用容量(MB): " + usablePatitionSpace / (1024 *1024) + " MB");
        System.out.println("剩余容量(MB): " + freePartitionSpace / (1024 *1024) + " MB");
        System.out.println("\n**** Sizes in Giga Bytes ****\n");
        System.out.println("盘总容量(GB): " + totalCapacity / (1024*1024*1024) + " GB");
        System.out.println("已使用容量(GB): " + usablePatitionSpace / (1024 *1024*1024) + " GB");
        System.out.println("剩余容量(GB): " + freePartitionSpace / (1024 *1024*1024) + " GB");
        if(sizeUnit.equalsIgnoreCase("MB")){
            return freePartitionSpace / (1024 *1024);
        }
        return freePartitionSpace / (1024 *1024*1024);
    }


    public static void main(String[] args) {
        System.out.println(freeSpace("GB","D"));
    }
}
