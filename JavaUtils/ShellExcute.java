/**
 * Project Name:mda
 * File Name:ShellExcute.java
 * Package Name:com.calabar.commons.utils
 * Date:2017年11月17日下午6:28:02
 */
package com.calabar.commons.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * ClassName:ShellExcute <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * @author   Administrator 	 
 */
public class ShellExcute {
    public final static void process1(String cmdarray) { 
        try { 
            final Process p = Runtime.getRuntime().exec(cmdarray); 
            new Thread(new Runnable() { 

                public void run() { 
                    BufferedReader br = new BufferedReader( 
                            new InputStreamReader(p.getInputStream())); 
                    try { 
                        while (br.readLine() != null)  ; 
                        br.close(); 
                    } catch (IOException e) { 
                        e.printStackTrace(); 
                    } 
                } 
            }).start(); 
            BufferedReader br = null; 
            br = new BufferedReader(new InputStreamReader(p.getErrorStream())); 
            String line = null; 
            while ((line = br.readLine()) != null) { 
                System.out.println(line); 
            } 
            p.waitFor(); 
            br.close(); 
            p.destroy(); 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
    } 
}

