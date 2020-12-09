package com.yiying.vod.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author zzf
 * @version 1.0
 * @date 2020/12/09 21:56
 */
public class OutPutThread extends Thread {

    //获取进程的标准输入流
    InputStream is1 = null;

    public OutPutThread(InputStream is1) {
        this.is1 = is1;
    }

    @Override
    public void run() {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(is1));
        try {
            String lineB = null;
            while ((lineB = br.readLine()) != null) {
                if (lineB != null) {
                    //System.out.println(lineB);    //必须取走线程信息避免堵塞
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //关闭流
        finally {
            try {
                is1.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
