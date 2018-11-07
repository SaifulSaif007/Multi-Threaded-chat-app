/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.net.Socket;
import java.util.*;
import java.io.*;


/**
 *
 * @author Olku
 */

public class ClientHandler implements Runnable{
    
    private String userName;
    private String passWord;
    private Socket socket;

    public ArrayList <ClientHandler> friendlist = new ArrayList<>();
    
    public ClientHandler (String userName, String passWord, Socket socket){
        this.userName = userName;
        this.passWord = passWord;
        this.socket = socket;
    }

 
    
    public ClientHandler OnlineUser(String userN){
        ClientHandler ch = null;
        for(int i=0;i<Server.onlineUserList.size();i++){
            if(Server.onlineUserList.get(i).getuserName().equals(userN)){
                ch = Server.onlineUserList.get(i);
                break;
            }
        }
                return ch;
        }
    
    public ClientHandler User_list(String userN){
        ClientHandler ch = null;
        for(int i=0;i<Server.onlineUserList.size();i++){
            if(Server.cList.get(i).getuserName().equals(userN)){
                ch = Server.cList.get(i);
                break;
            }
        }
         return ch;
    }
    
    public void AddFriend(String userN){
        try{
            ClientHandler ch = User_list(userN);
            DataOutputStream dos = new DataOutputStream(ch.getSocket().getOutputStream());
            dos.writeBytes("Friend Request from: " + this.getuserName()+ '\n');
            dos.writeBytes("accept/reject..?" + '\n');
        }
        catch(Exception ex){
         System.out.println(ex);   
        }
    }
    
    public void AcceptFriend(String userN){
        try{
            ClientHandler ch = User_list(userN);
            DataOutputStream dos = new DataOutputStream(ch.getSocket().getOutputStream());
            dos.writeBytes("Friend request Accepted by:" + this.getuserName() + '\n');
            
            DataOutputStream dos1 = new DataOutputStream(socket.getOutputStream());
            dos1.writeBytes("you are now friend with: " + ch.getuserName()+ '\n');
            
            friendlist.add(ch);
            ch.friendlist.add(this);
        }
        catch(Exception ex){
         System.out.println(ex);   
        }
    }
    
    public void R_Friend(String userN){
        try{
            ClientHandler ch = User_list(userN);
            DataOutputStream dos = new DataOutputStream(ch.getSocket().getOutputStream());
            dos.writeBytes("Friend request rejected by: " + this.getuserName() + '\n');
        }
        catch(Exception ex){
          System.out.println(ex);  
        }
    }
   
    
    
      public void Online_list(){
        try{
            DataOutputStream dos = new DataOutputStream(this.getSocket().getOutputStream());
            dos.writeBytes("Online User List......" + '\n');
             for(int i=0;i<Server.onlineUserList.size();i++){
                dos.writeBytes((i+1) + "." + Server.onlineUserList.get(i).getuserName()+'\n');
                //System.out.println(Server.onlineUserList.get(i).getuserName());
             }
             
        }catch(Exception ex){
            System.out.println(ex);
        }
    }

    private void Friendlist() {
        try{
            DataOutputStream dos = new DataOutputStream(this.getSocket().getOutputStream());
            dos.writeBytes("Your Friends are..." + '\n');
            for(int i=0;i<friendlist.size();i++){
                dos.writeBytes((i+1)+ ". " + friendlist.get(i).getuserName() + '\n');
            }
             
        }
        catch(Exception ex){
          System.out.println(ex);  
        }
    }

    private void UniCast(String userN, String msg) {
         try{
             ClientHandler ch = OnlineUser(userN);
               if(ch==null){
                     return;
                 }
             DataOutputStream dos = new DataOutputStream(ch.getSocket().getOutputStream());
             dos.writeBytes("msg from " + this.getuserName() + ":" + msg + '\n');
                 
             
         }
         catch(Exception ex){
           System.out.println(ex);  
         }
    }
    
    private void MultiCast(String msg[]){
       int L = msg.length;
       System.out.println(L + msg[0] + msg[1] + msg[2]);
       
       try{
           for(int i=1; i<=L-1;i++){
               ClientHandler ch = OnlineUser(msg[i]);
               if(ch==null){
                   return;
               }
               DataOutputStream dos = new DataOutputStream(ch.getSocket().getOutputStream());
               dos.writeBytes("msg from: " + this.getuserName() + ":" + msg[L-1] + '\n');
               
           }
           
       }
       catch(Exception ex){
           System.out.println(ex);
       }
    }
    
      
    public void BroadCast(String str){
        try{
           
            for(int i=0; i<Server.onlineUserList.size();i++){
                if(this==Server.onlineUserList.get(i)){
                }
                else
                {
                DataOutputStream dos = new DataOutputStream(Server.onlineUserList.get(i).getSocket().getOutputStream());
                dos.writeBytes("msg from: " + this.getuserName() + ":" + str + '\n');
                }
            }
        }
            catch(Exception ex){ 
               System.out.println(ex);
        }
    }
     public String getuserName(){
     return userName;
    }
    public void setuserName(String userName){
        this.userName = userName;
    }
    public String getpassWord(){
        return passWord;
    }
    public void setpassWord(String passWord){
        this.passWord = passWord;
    }
    
    public Socket getSocket(){
        return socket;
    }
    public void setSocket(Socket socket){
        this.socket = socket;
    }
    
    
     @Override
    public void run() {
        try{
            System.out.println("logged in.." + this.userName);
           //System.out.println(this.socket);
            InputStreamReader in = new InputStreamReader(socket.getInputStream());
            
            BufferedReader br = new BufferedReader(in);
           
            
            while(true){
                String info = br.readLine();
                String msg[] = info.split(":");
                
                
                if(msg[0].equals("logout")){
                    //System.out.println("out");
                    ClientHandler ch = OnlineUser(this.getuserName());
                    Server.onlineUserList.remove(ch);
                    break;
                }
                else if(msg[0].equals("list")){
                    Online_list();
                }
                else if(msg[0].equals("friend_list")){
                    Friendlist();
                }
               
                else if(msg[0].equals("add_friend")){
                    AddFriend(msg[1]);
                }
                else if(msg[0].equals("accept")){
                    AcceptFriend(msg[1]);
                }
                else if(msg[0].equals("reject")){
                    R_Friend(msg[1]);
                }
                if(msg[0].equals("msg")){
                    UniCast(msg[1], msg[2]);
                }
                 else if(msg[0].equals("multicast")){
                    MultiCast(msg);
                }
                else if(msg[0].equals("broadcast")){
                    BroadCast(msg[1]);
                }
                
            }
           //socket.close();
        }
        catch(Exception ex){
            
        }
    
    }
    
}
