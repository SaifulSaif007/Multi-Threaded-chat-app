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


public class Client {
    
    public static void main(String args[]){
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            Socket socket = new Socket();
            Socket soc = new Socket("localhost", 9999);
            String msg;
            
             
            
            while(true){
                System.out.println("username:password");
                msg = br.readLine();
                
                DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
                InputStreamReader input = new InputStreamReader(soc.getInputStream());
                BufferedReader in = new BufferedReader(input);
                
                dos.writeBytes(msg + '\n');
                String rec_msg = in.readLine();
                
                if(rec_msg.equalsIgnoreCase("logged in")){
                    System.out.println("login successful");
                   break;
                }
                else if(rec_msg.equalsIgnoreCase("user doesn't exists")){
                    System.out.println("regestration complete");
                    
                }
            }
               
          
           ClientWriter cw = new ClientWriter(soc.getOutputStream());
           Thread th1 = new Thread(cw);
           th1.start();
           
           ClientReader cr = new ClientReader(soc.getInputStream());
           Thread th = new Thread(cr);
           th.start();
        }
        catch(Exception ex){
            
        }
    }
    
}
