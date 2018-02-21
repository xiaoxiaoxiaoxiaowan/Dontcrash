import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class startview extends JFrame{
    model m;
    startview(model mo){
        m = mo;
        JFrame newone = new JFrame();
//        newone.setLayout(new BoxLayout(newone, BoxLayout.Y_AXIS));

        this.setFocusable(true);
        this.setSize(new Dimension(250,140));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);




        JLabel label1 = new JLabel("Welcome to the Game 'Airplane'");
        this.add(label1);


        JButton jbs = new JButton("Start");
        jbs.setPreferredSize(new Dimension(100, 40));
        this.add(jbs);

        JButton setting = new JButton("Setting");
        setting.setPreferredSize(new Dimension(100, 40));
        this.add(setting);

        JButton jbe = new JButton("Exit");
        jbe.setPreferredSize(new Dimension(100, 40));
        this.add(jbe);


        this.setVisible(true);
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        jbs.setAlignmentX(Component.CENTER_ALIGNMENT);
        jbe.setAlignmentX(Component.CENTER_ALIGNMENT);
        setting.setAlignmentX(Component.CENTER_ALIGNMENT);
        label1.setAlignmentX(Component.CENTER_ALIGNMENT);


        jbs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameview k = new gameview(m);
                newone.setVisible(false);
            }
        });


        jbe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });


    }
}
