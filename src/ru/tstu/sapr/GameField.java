package ru.tstu.sapr;

import java.io.*;
import java.net.*;
import java.util.*;

public class GameField extends javax.swing.JFrame {
  Socket client;
  Client player;
  Client wr;
  List<Client> polym;
  HashMap<String, String> mapPlayers;
  boolean press1 = true, press2 = true, press3 = true, press4 = true,
          press5 = true, press6 = true,press7 = true, press8 = true,
          press9 = true;
  boolean gameRun = true;
  boolean start;
  boolean work;
  String namePlayer;    
  
  public GameField() {
    initComponents();
  }
  
  class Player extends Client implements Runnable{
    DataInputStream in;
    DataOutputStream out;
    String name;    
    
    
    Player(InputStream dis, OutputStream dos, String name) {
      in = new DataInputStream(dis);
      out = new DataOutputStream(dos);
      this.name = name;
      namePlayer = name;
      start = true;
      work = true;
      new Thread(this).start();
      this.step = 1;
    }            
    
    @Override
    public void sendStep(int numButton){
      try {
        out.writeUTF(name + ":" + Integer.toString(numButton));
        out.flush();
      } catch (IOException e){e.printStackTrace();};
    }
    
    public void startGame(String line){
      String[] names = line.split(":");
      String[] sign = {"X", "O"};
      mapPlayers.put(names[0], sign[0]);
      mapPlayers.put(names[1], sign[1]);
      step = Integer.parseInt(names[2]);
      start(names);
    }
    
    public void start(String[] s){
      nameX.setText(s[0]);
      nameO.setText(s[1]);
      stepPlayers.setText("Игрок X");
      start = false;
    }
    
    @Override
    public void send(){
      try{
        out.writeUTF(name);
        out.flush();
      } catch (IOException e) {e.printStackTrace();};   
    }   
    
    public void printStep(String s, int numButton){     
      switch (numButton){
        case (1) -> Field1.setText(s);
        case (2) -> Field2.setText(s);
        case (3) -> Field3.setText(s);
        case (4) -> Field4.setText(s);
        case (5) -> Field5.setText(s);
        case (6) -> Field6.setText(s);
        case (7) -> Field7.setText(s);
        case (8) -> Field8.setText(s);
        case (9) -> Field9.setText(s);        
      }
      if (s.equals("X")){
        stepPlayers.setText("Игрок O");
      } else {
        stepPlayers.setText("Игрок X");
      }
    }
    
    public void pressButton(int numButton){
      switch (numButton){
        case (1) -> press1 = false;
        case (2) -> press2 = false;
        case (3) -> press3 = false;
        case (4) -> press4 = false;
        case (5) -> press5 = false;
        case (6) -> press6 = false;
        case (7) -> press7 = false;
        case (8) -> press8 = false;
        case (9) -> press9 = false;        
      }
    }
    
    @Override
    public void run(){
      try {
          String line = null;         
          int isWin = -1;
          String sign = null;
          while(work) {  
            line = in.readUTF();
            if (start) {            
              startGame(line);
            } else {
              String[] inf = line.split(":"); 
              sign = inf[0];                      
              int numButton = Integer.parseInt(inf[1]);
              step = Integer.parseInt(inf[2]);
              work = Boolean.parseBoolean(inf[3]);
              isWin = Integer.parseInt(inf[4]);            
              if (!work){           
                if (isWin == 1){
                  String nameWin = null;
                  for (String key: mapPlayers.keySet()){
                    String s = (String)mapPlayers.get(key); 
                    if (s.equals(sign)){
                      nameWin = key;
                    }
                  }
                  result.setText("Выиграл игрок: " + nameWin);
                  in.close();
                  out.close();
                } else {
                  result.setText("Ничья!");
                  in.close();
                  out.close();  
                }            
              }               
              printStep(sign, numButton);
              pressButton(numButton);
            }
          }
          
        } catch(IOException e){           
        };
    }  
  }
  
  class Writer extends Client{    
    File file;
    
    Writer() {      
     file = new File("Step.txt");
      try {
        file.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    
    @Override
    public void sendStep(int step){      
      FileWriter fw = null;
      try {
          fw = new FileWriter(file, true);
          fw.write("Ход игрока " + namePlayer + ": " + step + "\n");
      } catch (IOException e) {
        e.printStackTrace();
      }finally{
        try {
          fw.write("----------------------\n");
          fw.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }     
  }   
 
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jSeparator1 = new javax.swing.JSeparator();
    jLabel1 = new javax.swing.JLabel();
    jSeparator3 = new javax.swing.JSeparator();
    name = new javax.swing.JTextField();
    jSeparator4 = new javax.swing.JSeparator();
    startButton = new javax.swing.JButton();
    jLabel2 = new javax.swing.JLabel();
    jLabel4 = new javax.swing.JLabel();
    nameO = new javax.swing.JLabel();
    jLabel6 = new javax.swing.JLabel();
    nameX = new javax.swing.JLabel();
    stepPlayers = new javax.swing.JLabel();
    result = new javax.swing.JTextField();
    Field1 = new javax.swing.JButton();
    Field2 = new javax.swing.JButton();
    Field3 = new javax.swing.JButton();
    Field5 = new javax.swing.JButton();
    Field6 = new javax.swing.JButton();
    Field4 = new javax.swing.JButton();
    Field7 = new javax.swing.JButton();
    Field8 = new javax.swing.JButton();
    Field9 = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("Крестики-нолики");
    setBounds(new java.awt.Rectangle(300, 300, 455, 525));
    setResizable(false);

    jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    jLabel1.setText("Введите своё имя:");

    jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);

    name.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

    startButton.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
    startButton.setText("Начать игру");
    startButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        startButtonActionPerformed(evt);
      }
    });

    jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    jLabel2.setText("Игрок X: ");

    jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    jLabel4.setText("Игрок O: ");

    nameO.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    nameO.setText("----");

    jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    jLabel6.setText("Ход:");

    nameX.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    nameX.setText("----");

    stepPlayers.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    stepPlayers.setText("----");

    result.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

    Field1.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
    Field1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        Field1ActionPerformed(evt);
      }
    });

    Field2.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
    Field2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        Field2ActionPerformed(evt);
      }
    });

    Field3.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
    Field3.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        Field3ActionPerformed(evt);
      }
    });

    Field5.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
    Field5.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        Field5ActionPerformed(evt);
      }
    });

    Field6.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
    Field6.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        Field6ActionPerformed(evt);
      }
    });

    Field4.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
    Field4.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        Field4ActionPerformed(evt);
      }
    });

    Field7.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
    Field7.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        Field7ActionPerformed(evt);
      }
    });

    Field8.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
    Field8.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        Field8ActionPerformed(evt);
      }
    });

    Field9.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
    Field9.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        Field9ActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addComponent(jSeparator4)
            .addContainerGap())
          .addGroup(layout.createSequentialGroup()
            .addComponent(jSeparator1)
            .addContainerGap())
          .addGroup(layout.createSequentialGroup()
            .addGap(23, 23, 23)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(name, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(26, 26, 26)
            .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stepPlayers, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nameX, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nameO, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGap(0, 0, Short.MAX_VALUE))
          .addGroup(layout.createSequentialGroup()
            .addGap(4, 4, 4)
            .addComponent(startButton, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(result)
            .addContainerGap())
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
              .addGroup(layout.createSequentialGroup()
                .addComponent(Field4, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Field5, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addGroup(layout.createSequentialGroup()
                .addComponent(Field1, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Field2, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(Field3, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(Field6, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
          .addGroup(layout.createSequentialGroup()
            .addComponent(Field7, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(Field8, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Field9, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap())))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(jLabel2)
              .addComponent(nameX, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(11, 11, 11)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(jLabel4)
              .addComponent(nameO, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGap(9, 9, 9)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(jLabel6)
              .addComponent(stepPlayers)))
          .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addGroup(layout.createSequentialGroup()
            .addGap(7, 7, 7)
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(name, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(Field1, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
          .addComponent(Field2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(Field3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addGap(10, 10, 10)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(Field5, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(Field4, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(Field6, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(Field7, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(Field8, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(Field9, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(18, 18, 18)
        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(result, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(startButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(25, 25, 25))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
    try {
      if (gameRun){
        String namePlayer = name.getText();
        InetAddress ip = InetAddress.getByName("127.000.000.001");
        int port = 3333;
        client = new Socket(ip, port);
        InputStream in = client.getInputStream();
        OutputStream out = client.getOutputStream();
        player = new Player(in, out, namePlayer);
        wr = new Writer();
        polym = Arrays.asList(player, wr);
        player.send();
        mapPlayers = new HashMap<>();
        gameRun = false;
      }
      else{
        client.close();
      }
    } catch (IOException e) {e.printStackTrace();};
  }//GEN-LAST:event_startButtonActionPerformed

  private void Field1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Field1ActionPerformed
    if (player.step == 1 && press1){
      for (Client cl: polym){
        cl.sendStep(1);
      }
    }
  }//GEN-LAST:event_Field1ActionPerformed

  private void Field2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Field2ActionPerformed
    if (player.step == 1 && press2){
      for (Client cl: polym){
        cl.sendStep(2);
      }
     }
  }//GEN-LAST:event_Field2ActionPerformed

  private void Field3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Field3ActionPerformed
    if (player.step == 1 && press3){
      for (Client cl: polym){
        cl.sendStep(3);
      }
    }    
  }//GEN-LAST:event_Field3ActionPerformed

  private void Field4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Field4ActionPerformed
    if (player.step == 1 && press4){
      for (Client cl: polym){
        cl.sendStep(4);
      }
    } 
  }//GEN-LAST:event_Field4ActionPerformed

  private void Field5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Field5ActionPerformed
    if (player.step == 1 && press5){
      for (Client cl: polym){
        cl.sendStep(5);
      }
    } 
  }//GEN-LAST:event_Field5ActionPerformed

  private void Field6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Field6ActionPerformed
    if (player.step == 1 && press6){
      for (Client cl: polym){
        cl.sendStep(6);
      }
    }     
  }//GEN-LAST:event_Field6ActionPerformed

  private void Field7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Field7ActionPerformed
    if (player.step == 1 && press7){
      for (Client cl: polym){
        cl.sendStep(7);
      }
    }
  }//GEN-LAST:event_Field7ActionPerformed

  private void Field8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Field8ActionPerformed
    if (player.step == 1 && press8){
      for (Client cl: polym){
        cl.sendStep(8);
      }
    }  
  }//GEN-LAST:event_Field8ActionPerformed

  private void Field9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Field9ActionPerformed
    if (player.step == 1 && press9){
      for (Client cl: polym){
        cl.sendStep(9);
      }
    } 
  }//GEN-LAST:event_Field9ActionPerformed

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
      java.util.logging.Logger.getLogger(GameField.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(GameField.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(GameField.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(GameField.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>

 
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new GameField().setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton Field1;
  private javax.swing.JButton Field2;
  private javax.swing.JButton Field3;
  private javax.swing.JButton Field4;
  private javax.swing.JButton Field5;
  private javax.swing.JButton Field6;
  private javax.swing.JButton Field7;
  private javax.swing.JButton Field8;
  private javax.swing.JButton Field9;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JSeparator jSeparator1;
  private javax.swing.JSeparator jSeparator3;
  private javax.swing.JSeparator jSeparator4;
  private javax.swing.JTextField name;
  private javax.swing.JLabel nameO;
  private javax.swing.JLabel nameX;
  private javax.swing.JTextField result;
  private javax.swing.JButton startButton;
  private javax.swing.JLabel stepPlayers;
  // End of variables declaration//GEN-END:variables
}
