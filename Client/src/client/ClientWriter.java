/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

 

/**
 *
 * @author Olku
 */

import java.io.*;
import java.net.*;
import java.util.*;

class ClientWriter implements Runnable {
    
    private OutputStream stream;

    public ClientWriter(OutputStream stream) {
       this.stream = stream;
    }

    @Override
    public void run() {
    
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      
      DataOutputStream dos = new DataOutputStream(stream);
      
      try{
          while(true){
              String msg = br.readLine();
              //System.out.println(msg);
              //System.out.println("ss");
              dos.writeBytes(msg + '\n');
              
          }    
      }
      
      catch(Exception ex){
          
      }
    }
    
      public OutputStream getStream(){
          return stream;
      }
    
      public void setStream(OutputStream stream){
          this.stream = stream;
      }
}
