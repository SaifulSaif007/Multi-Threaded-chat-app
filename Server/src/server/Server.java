/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author Olku
 */
import java.io.*;
import java.net.*;
import java.util.*;


public class Server {

   public static ArrayList<ClientHandler> cList = new ArrayList();
   public static ArrayList<ClientHandler> onlineUserList = new ArrayList<ClientHandler>();
      
    public static void main(String[] args) {

    try{
        
        ServerSocket ss = new ServerSocket(9999);
        while(true){
            Socket socket = ss.accept();
            InputStreamReader input = new InputStreamReader(socket.getInputStream());
            BufferedReader in = new BufferedReader(input);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
 
            int flag = 0; 
            String s_msg;
            ClientHandler tempClient=null;
         
          while(flag!=1){  
             
            String info = in.readLine();
            String msg[] = info.split(":");
            System.out.println(info);
            
            for(int i=0;i<cList.size();i++){ 
            if(cList.get(i).getuserName().equals(msg[0]) && cList.get(i).getpassWord().equals(msg[1])){
                    tempClient=cList.get(i);
                        flag=1;
                        break;
            }
                }
           
               if(flag==1){
                      s_msg = "logged in";
                      onlineUserList.add(tempClient);
                      Thread th = new Thread(tempClient);
                      th.start();
                      dos.writeBytes(s_msg +'\n');
                    
                }
               else if(flag==0){
                     
                    ClientHandler client = new ClientHandler(msg[0],msg[1],socket); 
                    cList.add(client);
                    s_msg="user doesn't exists";
                    dos.writeBytes(s_msg+'\n');
                    
               }  
            }  
        }
    }
    catch(Exception ex){
        System.out.println(ex);
    }
   
      
    }
    
}
 

