package ru.tstu.sapr;

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerFrame extends javax.swing.JFrame {
  ServerSocket server;
  Socket client;
  Sendler send = new Sendler();
  ServerThread sr[] = new ServerThread[2];
  int playerCount = 0;
  final int port = 3333;
  HashMap<String, String> mapStep = new HashMap<>();
  HashMap<String, String> mapName = new HashMap<>();
  ArrayList<Integer> xButtons = new ArrayList<>();
  ArrayList<Integer> oButtons = new ArrayList<>();
  boolean start = true;
  boolean work = true;;
  boolean run = true;
  int step = 1;
  
  public ServerFrame() {
    initComponents();
  }

  public class ServerThread implements Runnable{
    String sign;
    DataInputStream in;
    DataOutputStream out;  
    Integer[][] winCombo = {
      {1, 2, 3}, 
      {4, 5, 6}, 
      {7, 8, 9}, 
      {1, 4, 7}, 
      {2, 5, 8}, 
      {3, 6, 9}, 
      {1, 5, 9}, 
      {3, 5, 7}
    };
       
    ServerThread(InputStream is, OutputStream os){
      in = new DataInputStream(is);
      out = new DataOutputStream(os);
      new Thread(this).start();      
    }
       
    public boolean startGame(String name){      
      if (playerCount == 0) {
        sign = "X";
        mapStep.put(sign, name);
        mapName.put(name, sign);
        playerCount++;
        return true;
      }
      if (playerCount == 1) {
        sign = "O";
        mapStep.put(sign, name);
        mapName.put(name, sign);    
        for (ServerThread s: sr){        
          String strNames = (String)mapStep.get("X") + ":" + 
              (String)mapStep.get("O") + ":" + Integer.toString(step);         
          s.sendPlayer(strNames);    
          step *= -1;    
        }
        step *= -1;       
        return false;
      } 
      return false;
    }
    
    public void sendPlayer(String s){
      try {
        if (work){
          out.writeUTF(s);
          out.flush();
        }
      } catch(IOException e){
        e.printStackTrace();
      }
    }
    
    public void stepPlayers(ArrayList list, int numButton, String sign){ 
      boolean checkWin = false;
      list.add(numButton);
      if (list.size() >= 3) {
        for (Integer[] win: winCombo){
          Collection<Integer> wc = new ArrayList<Integer>();
          wc.addAll(Arrays.asList(win));
          if (list.containsAll(wc)){
            checkWin = true;
          }
        }
        if (checkWin){                
            createStr(numButton, "false", "1");                
            String nameWin = (String)mapStep.get(sign);
            work = false;
            send.sendMessage(nameWin, numButton, 1);          
        }
        else {         
          int lists_size = xButtons.size() + oButtons.size();
          if (!checkWin && lists_size >= 9){      
            createStr(numButton, "false", "0"); 
            work = false;
            send.sendMessage("Ничья", numButton, 0);            
          } else {
            createStr(numButton, "true", "-1");
          }
        }
      } else {
        createStr(numButton, "true", "-1");
      }     
    }
    
    public void createStr(int figure, String boolWork, String w){
      for (ServerThread s: sr){             
        String str = sign + ":" + Integer.toString(figure) + ":" 
              + Integer.toString(step) + ":" + boolWork + ":" + w;     
        s.sendPlayer(str);
        step *= -1;           
      }
      step *= -1;
    }

    @Override
    public void run(){
      try { 
        String line = null;        
        while (work){   
          line = in.readUTF();         
          if (start){           
            send.sendMessage(line, 0, -1);
            start = startGame(line);          
          } else{            
            String[] str = line.split(":");
            String name2 = str[0];
            int numButton = Integer.parseInt(str[1]);
            String key = (String)mapName.get(name2);          
            if (key.equals("X") && work){
              stepPlayers(xButtons, numButton, "X");              
              send.sendMessage(name2, numButton, -1);
            } else {
              stepPlayers(oButtons, numButton, "O");             
              send.sendMessage(name2, numButton, -1);
            }
          }
        }
      }catch(IOException e){       
      };
    }
  }
  
  class Sendler {
    public synchronized void sendMessage(String name, int numButton, int status){
      String str = null;
      if (start){
        str = "Игрок " + name + " подключен.\n";        
      } 
      if (!start && work) {
        str = "Ход игрока " + name + " : " + numButton + ". \n"; 
      }
      if (!start && !work){
        if (status == 1){
          str = "Ход игрока " + name + " : " + numButton + ". \n" +
                  "Выиграл игрок: " + name + ".";           
          } 
        if (status == 0){
          str = name;          
        }     
      }            
      jTextArea1.append(str);
    }
  }

  class PortListener implements Runnable{
    PortListener(){
      new Thread(this).start();
    }
    
    @Override
    public void run(){
      while(work){
        try {
          client = server.accept();
          InputStream in = client.getInputStream();
          OutputStream out = client.getOutputStream();
          sr[playerCount] = new ServerThread(in, out);
        } catch(IOException e){e.printStackTrace();};
      }
    }
    
  }
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jButton1 = new javax.swing.JButton();
    jScrollPane1 = new javax.swing.JScrollPane();
    jTextArea1 = new javax.swing.JTextArea();
    jLabel1 = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("Сервер");

    jButton1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
    jButton1.setText("Запустить сервер");
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton1ActionPerformed(evt);
      }
    });

    jTextArea1.setEditable(false);
    jTextArea1.setColumns(20);
    jTextArea1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    jTextArea1.setRows(5);
    jTextArea1.setSelectionColor(new java.awt.Color(0, 0, 0));
    jScrollPane1.setViewportView(jTextArea1);

    jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
    jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel1.setText("Сервер");

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 456, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(layout.createSequentialGroup()
            .addGap(138, 138, 138)
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(layout.createSequentialGroup()
            .addGap(119, 119, 119)
            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addContainerGap(22, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGap(19, 19, 19)
        .addComponent(jLabel1)
        .addGap(18, 18, 18)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(28, 28, 28)
        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(31, Short.MAX_VALUE))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    try {
      server = new ServerSocket(port, 2);
      new PortListener();
      jTextArea1.append("Сервер ожидает подключения.\n");            
    } catch(IOException e){e.printStackTrace();};  
  }//GEN-LAST:event_jButton1ActionPerformed

  public static void main(String args[]) {
    
    //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
    /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
     */
    try {
      for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          javax.swing.UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (ClassNotFoundException ex) {
      java.util.logging.Logger.getLogger(ServerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(ServerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(ServerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(ServerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>

     
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new ServerFrame().setVisible(true);
        
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton jButton1;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JTextArea jTextArea1;
  // End of variables declaration//GEN-END:variables
}
