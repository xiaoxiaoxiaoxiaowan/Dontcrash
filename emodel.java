import javax.swing.*;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Stack;




public class emodel extends Observable{
    public int end = 3000;
    public LinkedList<displayable> barriers = null;
    public JTabbedPane tab = null;
    public Stack<LinkedList<displayable>> undostack = null;
    public model game = null;

    public emodel(JTabbedPane container, model game){
        this.game = game;
        barriers = new LinkedList<>();
        undostack = new Stack<>();
        tab = container;
        eview view = new eview(tab, this);
        this.addObserver(view);
    }

    public void add(displayable e){
        barriers.add(e);
        setChanged();
        notifyObservers();
    }

    public void remove(displayable e){
        barriers.remove(e);
        setChanged();
        notifyObservers();
    }

    public void move(displayable e, int x, int y){
        e.x = x;
        e.y = y;
    }


    public void setend(int k){
        end = k;
        setChanged();
        notifyObservers();
    }

    public displayable locate(int x, int y){
        for(displayable e : barriers){
            if(e.in(x,y)) return e;
        }
        return null;
    }

    public displayable locatec(int x, int y){
        for(displayable e : barriers){
            int k = e.conner(x,y);
            if(k != -1) return e;
        }
        return null;
    }

    public void purge(){
        barriers = new LinkedList<>();
        end = 0;
    }


    public void trans() {
        game.restart();
        System.out.println(barriers);
        game.setlevel(barriers,end);
    }


}
