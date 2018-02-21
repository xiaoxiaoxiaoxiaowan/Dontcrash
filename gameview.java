import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import java.util.TimerTask;

class level implements Serializable {
    LinkedList<displayable> barriers = null;
    int end = 0;
}




public class gameview extends JPanel implements Observer {
    model bigmodel;
    JFrame frame;
    java.util.Timer refreshtimmer = new java.util.Timer();
    JButton rbutton;
    JButton pbutton;
    JButton hbutton;

    gameview(model game){
        frame = new JFrame("game");
        frame.setFocusable(true);
        frame.setVisible(true);
        frame.setSize(new Dimension(800,600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane();


        bigmodel = game;
        game.addObserver(this);
        this.setVisible(true);
        this.setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_W|| e.getKeyCode() == KeyEvent.VK_UP){
                    bigmodel.yspeed2 = -15;
                }
                if (e.getKeyCode() == KeyEvent.VK_S|| e.getKeyCode() == KeyEvent.VK_DOWN){
                    bigmodel.yspeed = 15;
                }
                if (e.getKeyCode() == KeyEvent.VK_A|| e.getKeyCode() == KeyEvent.VK_LEFT){
                    bigmodel.xspeed2 = -15;
                }
                if (e.getKeyCode() == KeyEvent.VK_D|| e.getKeyCode() == KeyEvent.VK_RIGHT){
                    bigmodel.xspeed = 15;
                }
                if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_P){
                    if(game.fail || game.won){
                        game.restart();
                    }else if(game.p){
                        game.st();
                    }else{
                        game.pa();
                    }
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP){
                    bigmodel.yspeed2 = 0;
                }
                if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN){
                    bigmodel.yspeed = 0;
                }
                if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT){
                    bigmodel.xspeed2 = 0;
                }
                if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT){
                    bigmodel.xspeed = 0;
                }
            }
        });
        this.setVisible(true);
        this.setFocusable(true);
        this.requestFocusInWindow();

        this.rbutton = new JButton("Restart");
        this.rbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bigmodel.restart();
            }
        });

        this.pbutton = new JButton("Start");
        this.pbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(game.fail || game.won){
                    game.restart();
                    return;
                }
                bigmodel.toggle();
                if(bigmodel.p) pbutton.setText("Start");
                else pbutton.setText("Pause");
            }
        });

        this.hbutton = new JButton("Help");
        this.hbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.pa();
                pbutton.setText("Start");
                JOptionPane.showMessageDialog(frame, "Press arrows to move the airplane\n" +
                        "Press WSAD to move the airplane\n" +
                        "Press p to pause\n" +
                        "Press s to start\n");

            }
        });




        JPanel controlpanel = new JPanel(new GridLayout(1,4));
        controlpanel.setVisible(true);
        controlpanel.add(pbutton);
        controlpanel.add(rbutton);
        controlpanel.add(hbutton);



        JPanel panelforgame = new JPanel(new BorderLayout());
        panelforgame.setVisible(true);
        panelforgame.setFocusable(true);
        panelforgame.add(this, BorderLayout.CENTER);
        panelforgame.add(controlpanel, BorderLayout.SOUTH);

        tabbedPane.add("Game", panelforgame);
        //eview!!!!!!!!!!!!!!!!!!!!
        emodel editor = new emodel(tabbedPane, bigmodel);
        //eview!!!!!!!!!!!!!!!!!!!!
        //tabbedPane.add("Editor", new JPanel());
        frame.add(tabbedPane);
        gameview that = this;
        this.refreshtimmer.schedule(new TimerTask() {
            @Override
            public void run() {
                that.repaint();
            }
        },0,1000/60);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.requestFocusInWindow();
        g.setFont(new Font("TimesRoman", Font.PLAIN, 18));
        g.setColor(Color.BLACK);
        g.drawString("Score: " + bigmodel.score, 0,20);
        for(displayable e : bigmodel.barriers){
            if(e.x > 1000) break;
            e.paint(g);
        }
        g.setColor(Color.blue);
        g.fillRect(bigmodel.x,bigmodel.y, 50,50);
        g.setColor(Color.BLACK);
        g.drawLine(0,500,2000,500);
        g.setColor(Color.lightGray);
        g.fillRect(bigmodel.end,0,1000,1000);

    }

    @Override
    public void update(Observable o, Object arg) {
        if(bigmodel.p) this.pbutton.setText("Start");
        else this.pbutton.setText("Pause");
        if(bigmodel.won){
            JOptionPane.showMessageDialog(null, "You won! You scored " + bigmodel.score);
        }
    }
}
