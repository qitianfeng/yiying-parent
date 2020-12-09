package com.yiying.vod.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author zzf
 * @version 1.0
 * @date 2020/12/09 22:00
 */
public class OutPutErrorThread extends Thread {
    private InputStream is2;

    public OutPutErrorThread(InputStream is2) {
        this.is2 = is2;
    }

    @Override
    public void run() {

        BufferedReader br2 = new BufferedReader(
                new InputStreamReader(is2));
        try {
            String lineC = null;
            while ((lineC = br2.readLine()) != null) {
                if (lineC != null) {
                    //System.out.println(lineC);   //必须取走线程信息避免堵塞
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //关闭流
        finally {
            try {
                is2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
