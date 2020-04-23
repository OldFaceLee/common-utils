package com.ai.commonUtils.runtimeUtils;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RuntimeUtils {
    private static final Logger log = Logger.getLogger(RuntimeUtils.class);

    /**
     *本地执行命令
     */
    public static String exeCmd(String shell){
        StringBuffer stringBuffer = null;
        BufferedReader bufferedReader = null;
        Process process = null;
        String osName = System.getProperty("os.name");
        try {
            Runtime runtime = Runtime.getRuntime();
            if(osName.contains("win")){
                log.info("系统运行环境为："+osName);
                process = runtime.exec("cmd.exe /c "+shell);
            }else if (osName.contains("Mac")) {
                log.info("系统运行环境为："+osName);
                process = runtime.exec(shell);
            }else {
                log.info("系统运行环境为："+osName);
                process = runtime.exec(shell);
            }
            process.waitFor();
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
            stringBuffer = new StringBuffer();
            while(((line = bufferedReader.readLine()) != null)){
                stringBuffer.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                if(bufferedReader != null){
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stringBuffer.toString();
    }

    /**
     *远程执行linux命令
     */
    public static String execRemoteShell(String host,int port,String linuxUser,String LinusPwd,String shell){
        String result="";
        Session session =null;
        ChannelExec openChannel =null;
        try {
            JSch jsch=new JSch();
            session = jsch.getSession(linuxUser, host, port);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.setPassword(LinusPwd);
            session.connect();
            openChannel = (ChannelExec) session.openChannel("exec");
            openChannel.setCommand(shell);
            openChannel.connect();
            InputStream in = openChannel.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String buf = null;
            while ((buf = reader.readLine()) != null) {
                result+= new String(buf.getBytes("gbk"),"UTF-8")+" \n";
            }
        } catch (JSchException | IOException e) {
            result+=e.getMessage();
        }finally{
            if(openChannel!=null&&!openChannel.isClosed()){
                openChannel.disconnect();
            }
            if(session!=null&&session.isConnected()){
                session.disconnect();
            }
        }
        return result.trim();
    }

}
