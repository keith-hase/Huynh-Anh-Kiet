/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package happyfrog2;

import com.sun.javafx.scene.SceneHelper;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 *
 * @author kei
 */
public class HappyFrog2 implements ActionListener, MouseListener, KeyListener {

    public static HappyFrog2 happyfrog;
    private static final int WIDTH = 900;
    private static final int HEIGHT = 600;
    private Renderer renderer;
    private Bar bar;
    private Rectangle frog;
    private int tick, motion;
    private ArrayList<Rectangle> columns;
    private boolean pause, gameover,pi;
    private int score;

    public HappyFrog2() {
        JFrame jFrame = new JFrame();
        Timer time = new Timer(20, this);
        renderer = new Renderer();
        bar = new Bar();
        frog = new Rectangle(WIDTH / 2, HEIGHT / 2 - 10, 40, 40);
        columns = new ArrayList<>();
        tick = 0;
        motion = 0;
        score = 0;
        pause = false;
        gameover = false;
        //-----------
        jFrame.setLayout(new BorderLayout());
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.getContentPane().add(bar, BorderLayout.PAGE_START);
        jFrame.getContentPane().add(renderer, BorderLayout.CENTER);
        jFrame.setTitle("happy frog");
        jFrame.setSize(WIDTH, HEIGHT);
        jFrame.setResizable(false);
        jFrame.addKeyListener(this);
        jFrame.addMouseListener(this);
        jFrame.setVisible(true);
        jFrame.setLocationRelativeTo(null);// thiet lap man hih ra giua
        addButtonAction();
        time.start();
    }

    public void addButtonAction() {
        bar.exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        //-------------
        bar.pause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (bar.pause.getText().equals("Pause")) {
                    bar.pause.setText("Resume");
                } else {
                    bar.pause.setText("Pause");
                }
                setPause();
            }
        });
        //-----------------
        bar.start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                score = 0;
                bar.setScore(score);
                pause = false;
                pi = false;
                columns.clear();
                motion = 0;
                frog.setLocation(WIDTH / 2, HEIGHT / 2 - 10);
                gameover = false;
            }
        });

    }

    public static void main(String[] args) {
        happyfrog = new HappyFrog2();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // bat dau cac hanh dong trong game 
        // set toc do di chuyen cua cac ong nuoc va frog trong game
        int speed = 5;
        if (!gameover) {// neu trong game chua over thi se xet cac button 
            if (!pause) {//neu nut chon pause chua kich hoat thi 
                bar.start.setEnabled(false);// nut start se chua dc mo 
            } else {
                bar.start.setEnabled(true);// neu da pause thi se mo nut start 
            }
            bar.pause.setEnabled(true);// thiet lap nut pause la enable san
        } else {// neu tro choi da ket thuc nut start se kich hoat de nguoi choi choiw man moi
            bar.start.setEnabled(true);
            bar.pause.setEnabled(false);
        }
        //-------------- thuc hien cac hanh dong trong game -----------------
        if (!pause) {
            tick++;
            for (int i = 0; i < columns.size(); i++) {
                if (columns.get(i).x + columns.get(i).y < 0) {
                    columns.remove(i);
                } else {
                    columns.get(i).x -= speed;
                }
            }
            //---------- thuc hien motion nhay nhay cua con ech va moi lan lick la motion se bay cao y = 2
            if (tick % 2 == 0 && motion < 20) {
                motion += 2;
            }
            if (tick % 70 == 0) {// thuc hien the hien khoan cach giua cac columns
                addColumns();
            }
            for (int i = 0; i < columns.size(); i++) {
                if (columns.get(i).intersects(frog)) {// intersects la ham phat hien va cham cua cac rectangle voi nhau tra ve gia tri boolean
                    gameover = true;
                }
            }
            if (frog.y <= 0 || frog.y + frog.height >= HEIGHT) {// thiet lap gameover khi frog bay thap hon man hiinh hoac lo man hinh
                gameover = true;
            }
            // thiet lap nhung tac vu say ra khi gameover
            if (gameover) {
                frog.x -= speed;
                // coc khong the di chuyen ngang dc nua
            } else {// truong hop chua gameover thi se bat dau he thong tinh diem
                for (int i = 0; i < columns.size(); i+=2) {
                    if (columns.get(i).x == frog.x) {// neu trong dong ngang ma tai truc do x cua dong va x cua frog bang nhau se dc 
                        score++;                           // tinh diem 
                        bar.setScore(score);
                        if (score == 5 || score == 20) {
                            pi = true;
                        }
                        else{
                            pi = false;
                        }
                    }
                }
            }
            if (frog.y <= 0) {
                frog.y = 0;
            }
            frog.y += motion;
        }
        renderer.repaint(0);
    }

    void addColumns() {
        Random rd = new Random();
        int t = rd.nextInt(400) - 200;
        columns.add(new Rectangle(WIDTH, 0, 130, 150 + t));// thiet lap cai tru dau tren se bat dau voi do 
        //rong tu dau man hinh ben phai va vi tri y se bat dau o goc tren man hinh do dai khoan cach cua se phu thuoc vao tru tren
        columns.add(new Rectangle(WIDTH, HEIGHT - 220 + t, 130, 200 - t));// cot duoi se bat dau o dau man hinh nhu cot tren
        // nhung vi tri dinh cua ong se duoc bat dau tu do cao cua man hinh tru di khoang cách từ dưới với dưới cùng là cao nhất và + t để set
        // do cao la ngau nhien thay vi dung 1 chỗ (ve do thi x y quay nguoc)
    }

    void paintColumns(Graphics g, Rectangle column) {
        g.setColor(Color.green.darker());
        g.fillRect(column.x, column.y, column.width, column.height);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
        if (!pause) {
            jump();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
       
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
         if (e.getKeyCode() == 27) {
       setPause();
        }

    }

    // thuc hien ve cac hoat hoa
    void repaint(Graphics g) {
        g.setColor(Color.cyan);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        for (Rectangle column : columns) {
            paintColumns(g, column);
        }
        // ve con coc hoac chim
        g.setColor(Color.GREEN);
        g.fillRect(frog.x, frog.y, 40, 40);
        g.setColor(Color.red);
        g.fillOval(frog.x + 30, frog.y + 20, 20, 10);
        g.setColor(Color.white);
        g.fillRect(frog.x + 5, frog.y + 10, 20, 25);
        g.setColor(Color.blue.brighter());
        g.fillOval(frog.x + 20, frog.y + 5, 10, 10);
        if (pause) {
            g.setColor(Color.blue);
            g.setFont(new Font("", Font.BOLD, 40));
            g.drawString("Pause", WIDTH / 2, HEIGHT / 2 - 40);
        }
        if (gameover) {
            g.setColor(Color.red);
            g.setFont(new Font("", Font.BOLD, 40));
            g.drawString("Game Over", WIDTH / 2, HEIGHT / 2 - 40);
        }
        if (pi) {
            g.setColor(Color.yellow);
            g.setFont(new Font("", Font.BOLD, 40));
            g.drawString("Congratulation you have reach the des point", WIDTH / 2, HEIGHT / 2 - 40);
        }
    }

    void jump() {
        if (!gameover && !pause) {
            if (motion >= 0) {
                motion = 0;
            }
            motion -= 10;
        }
    }

    public void setPause() {
        this.pause = !pause;
    }

}
