import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;
import java.util.Stack;


public class eview extends JPanel implements Observer  {
    JPanel bigcontain = null;
    emodel ed = null;
    int from = 0;
    displayable selected = null;
    displayable mem = null;
    Point position = null;
    int changedx = 0;
    int changedy = 0;
    Rectangle sb = null;
    boolean sc = false;
    Stack<command> redostack = new Stack<>();
    Stack<command> undostack = new Stack<>();
    JButton redo = null;
    JButton undo = null;
    int ox = 0;
    int oy = 0;
    boolean isdraged = false;


    class PopUp extends JPopupMenu {
        JMenuItem deleteItem;
        JMenuItem copy;
        JMenuItem cut;

        public PopUp(){
            deleteItem = new JMenuItem("Delete");
            deleteItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(selected == null) return;
                    else{
                        command newc = new deletecommand(ed,selected);
                        undostack.add(newc);
                        selected = null;
                    }
                }
            });
            add(deleteItem);

            copy = new JMenuItem("Copy");
            copy.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(selected == null) return;
                    else{
                        mem = new displayable(selected.x, selected.y, selected.length, selected.width);
                        System.out.println("mem changed");
                    }
                }
            });
            add(copy);

            cut = new JMenuItem("Cut");
            cut.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(selected == null) return;
                    else{
                        mem = new displayable(selected.x, selected.y, selected.length, selected.width);
                        command newc = new deletecommand(ed,selected);
                        System.out.println("deleted");
                        undostack.add(newc);
                        selected = null;
                    }
                }
            });
            add(cut);


        }
    }
    class diffPopUp extends JPopupMenu {
        JMenuItem paste;
        public diffPopUp(){
            paste = new JMenuItem("paste");
            paste.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(position == null) return;
                    if(mem == null) return;
                    else{
                        command newc = new addcommand(ed,new displayable(position.x,position.y, mem.length, mem.width));
                        undostack.add(newc);
                    }
                }
            });
            add(paste);
        }
    }



    public eview(JTabbedPane tab, emodel ed){

        this.setVisible(true);
        this.setFocusable(true);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                selected = ed.locate(e.getX() + from,e.getY());
                position = new Point(e.getX() + from, e.getY());
                if(sb.contains(e.getX(),e.getY())) sc = true;
                if(selected != null){
                    changedx = e.getX() + from - selected.x;
                    changedy = e.getY() + from - selected.y;
                }
                if(sc){
                    changedx = e.getX() - sb.x;
                    changedy = e.getY() - sb.y;
                }
                System.out.println("clicked");

                if(selected == null){
                    maybeShowdiffPopup(e);
                    return;
                }
                maybeShowPopup(e);
                System.out.println("keep from");
                ox = selected.x;
                oy = selected.y;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                selected = ed.locate(e.getX() + from,e.getY());
                sc = false;
                if(selected == null){
                    position = new Point(e.getX() + from, e.getY());
                    maybeShowdiffPopup(e);
                }else {
                    System.out.println("here!");
                    maybeShowPopup(e);
                }
                if(isdraged){
                    command newc = new movecommand(ed,selected,ox,oy);
                    undostack.add(newc);
                    System.out.println("keep to");
                }
                isdraged = false;
            }

            public void maybeShowPopup(MouseEvent e){
                if (e.isPopupTrigger()) {
                    PopUp menu = new PopUp();
                    menu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
            public void maybeShowdiffPopup(MouseEvent e){
                if (e.isPopupTrigger()) {
                    diffPopUp menu = new diffPopUp();
                    menu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        eview that = this;


        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                System.out.println(that.getSize());
                sb.width = (int) (that.getWidth() * ((float)that.getWidth()/ ed.end));
                that.repaint();
            }
        });




        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if(sc && that.getSize().getWidth() < ed.end){
                    sb.x = e.getX() - changedx;
                    if(sb.x < 0) sb.x = 0;
                    if(sb.x + sb.width > that.getWidth()) sb.x = that.getWidth() - sb.width;
                    repaint();
                    from = sb.x;
                    return;
                }

                if(selected == null) return;
                isdraged = true;
                ed.move(selected,e.getX() + from - changedx, e.getY() + from - changedy);

                repaint();
            }
        });


        bigcontain = new JPanel(new BorderLayout());
        bigcontain.setFocusable(true);
        bigcontain.setVisible(true);
        bigcontain.add(this,BorderLayout.CENTER);
        JPanel controlpanel = new JPanel(new GridLayout(8,1));
        controlpanel.setPreferredSize(new Dimension(100,500));
        controlpanel.setFocusable(true);
        controlpanel.setBackground(Color.gray);

        JButton addb = new JButton("Add Block");
        addb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dim = JOptionPane.showInputDialog(null,"Enter Dimission in from of width [space] height in pixels");
                System.out.println(dim);
                if (dim == null) return;
                String k[] = dim.split(" ");
                try{
                    if(k.length != 2) throw new Exception();
                    int w = Integer.parseInt(k[0]);
                    int h = Integer.parseInt(k[1]);
                    command newc = new addcommand(ed, new displayable(200,200,h,w));
                    undostack.add(newc);
                }catch(Exception eeee){
                    JOptionPane.showMessageDialog(null, "Wrong Format please retry");
                    this.actionPerformed(e);
                }
            }
        });
        controlpanel.add(addb);

        undo = new JButton("Undo");
        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(undostack.size() == 0) return;
                command toundo = undostack.pop();
                toundo.undo();
                redostack.add(toundo);
                that.repaint();
            }
        });
        controlpanel.add(undo);

        redo = new JButton("Redo");
        redo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(redostack.size() == 0)return;
                command toredo = redostack.pop();
                toredo.redo();
                undostack.add(toredo);
                repaint();
            }
        });
        controlpanel.add(redo);

        JButton load = new JButton("Load");
        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
            }
        });
        controlpanel.add(load);

        JButton save = new JButton("Save");
        controlpanel.add(save);


        JButton help = new JButton("Help");
        help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Right click on obstacle to copy/cut and delete\n" +
                        "Right click on white space to paste\n" +
                        "Clikc Dimension to change the world size\n" +
                        "move the grey block to move the view");
            }
        });
        controlpanel.add(help);

        JButton changewordsize = new JButton("Dimension");
        changewordsize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dim = JOptionPane.showInputDialog(null,"Enter world width in pixels");
                System.out.println(dim);
                if (dim == null) return;
                try{
                    int k = Integer.parseInt(dim);
                    if(k <= 0) throw new Exception();
                    ed.setend(k);
                }catch(Exception eee){
                    JOptionPane.showMessageDialog(null, "Wrong Format please retry");
                    this.actionPerformed(e);
                }

            }
        });
        controlpanel.add(changewordsize);


        JButton play = new JButton("Play!");
        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ed.trans();
                JOptionPane.showMessageDialog(null, "Please go to game tab to paly!");
            }
        });
        controlpanel.add(play);

        controlpanel.setVisible(true);
        bigcontain.add(controlpanel,BorderLayout.WEST);

        this.ed = ed;
        tab.add("Editor",bigcontain);


        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                System.out.println(that.getSize());
                System.out.println((int) ((float)that.getWidth() * (float)( (float)that.getWidth()/ ed.end)));
                sb = new Rectangle(0,that.getHeight() - 35, (int) (that.getWidth() * ((float)that.getWidth()/ ed.end)), 25);
                that.repaint();
            }
        });

    }


    @Override
    public void update(Observable o, Object arg) {
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if(undostack.size() == 0) undo.setEnabled(false);
        else undo.setEnabled(true);

        if(redostack.size() == 0) redo.setEnabled(false);
        else redo.setEnabled(true);
        super.paintComponent(g);
        for(displayable e: ed.barriers){
            e.paintfrom(g,from);
        }
        //System.out.println(this.getSize());

        if((int)this.getSize().getWidth() > ed.end) return;

        int bottom = ((int)this.getSize().getHeight()) - 25;
        g.setColor(Color.BLACK);
        g.drawLine(0,bottom, (int)this.getSize().getWidth(), bottom);
        g.setColor(Color.gray);
        sb.width = (int) (this.getWidth() * ((float)this.getWidth()/ ed.end));
        g.fillRoundRect(sb.x,sb.y,sb.width,sb.height,20,20);
        g.drawRoundRect(sb.x,sb.y,sb.width,sb.height,20,20);
    }
}
