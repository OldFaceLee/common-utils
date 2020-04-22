package com.ai.commonUtils.runtimeUtils;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RuntimeUtils {
    private static final String CMD = "cmd.exe /c ";
    private static final Logger logger = Logger.getLogger(RuntimeUtils.class);

    public static String executeCMD(String cmd) throws IOException, InterruptedException {
        StringBuilder sb = new StringBuilder();
        Process p = Runtime.getRuntime().exec(CMD+cmd);
        BufferedReader bf = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while((line=bf.readLine())!=null){
            sb.append(line+"\n");
        }
        logger.info("执行cmd命令: "+cmd+" ,返回的内容:\n"+sb.toString());
        Thread.sleep(1000);
        bf.close();
        return sb.toString();
    }

}
