/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.InputStream;

/**
 *
 * @author Olku
 */

import java.io.*;
import java.net.*;

class ClientReader implements Runnable{

    private InputStream stream;
    
   public  ClientReader(InputStream stream) {
         this.stream = stream;
    }

    @Override
    public void run() {
        InputStreamReader in = new InputStreamReader(stream);
        BufferedReader br = new BufferedReader(in);
    
        while(true){
            try{
                String str = br.readLine();
                System.out.println(str);
                //System.out.println("ss");
            }
            catch(Exception ex){
             System.out.println(ex);
            }
        }
    }
    public  InputStream getStream(){
        return stream;
    }
    public void setStream(InputStream stream){
        this.stream = stream;
    }
}
