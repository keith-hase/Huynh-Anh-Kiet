/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package happyfrog2;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

// trang giao dien button va diem so trong game
public class Bar extends JPanel{
    JButton start, pause, exit;
    JLabel score;
    
    public Bar(){
        init();
    } 
    
    public void init(){
        this.setSize(900,100);
        this.setLayout(new GridLayout(1, 4));
        start = new JButton("Start");
        pause = new JButton("Pause");
        exit = new JButton("Exit");
        score = new JLabel("Score: 0");
        
        this.add(start);
        this.add(pause);
        this.add(exit);
        this.add(score);
        
    }
    public void setScore(int scorex){
        this.score.setText("Score: " + scorex);
    }
}
