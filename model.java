
import javax.swing.*;
import java.util.*;
import java.util.Observable;
import java.util.Timer;


public class model extends Observable {
    int x = 150;
    int y = 50;

    java.util.Timer muser = new java.util.Timer();
    List<displayable> barriers = new LinkedList<>();
    java.util.Timer pullback = new java.util.Timer();

    int xspeed = 0;
    int yspeed = 0;
    int xspeed2 = 0;
    int yspeed2 = 0;
    int score = 0;
    int end = 6000;
    boolean p = true;
    boolean fail = false;
    boolean won = false;


    model(){
        for(int i = 0; i < 20; i++){
            barriers.add(new displayable(400* i + 800, 30, 300, 50));
            barriers.add(new displayable(400* i + 1000,200, 300,50));
        }

        muser.schedule(new TimerTask() {
            @Override
            public void run() {
                move();
            }
        },0,1000/60);


        pullback.schedule(new TimerTask() {
            @Override
            public void run() {
                if(p) return;
                score+= 5;
                if(x > end - 50){
                    won = true;
                    pa();
                }
                for(displayable e : barriers){
                    if(e.boom(x,y)){
                        stop();
                        return;
                    }
                    e.x -= 5;
                }
                x -= 5;
                end -= 5;
            }
        },0,1000/60);

    }

    public void move(){
        if(p) return;
        if (x + xspeed < 0) {
            x = 0;
            stop();
        }
        else x += xspeed;
        if (x + xspeed2 < 0){
            x = 0;
            stop();
        }
        else x += xspeed2;

        if (y + yspeed < 0) y = 0;
        else if(y + yspeed > 500) y = 500;
        else y+= yspeed;

        if (y + yspeed2 < 0) y = 0;
        else if(y + yspeed2 > 450) y = 450;
        else y+= yspeed2;
    }



    public void stop(){
        pullback.cancel();
        muser.cancel();
        this.fail = true;
        this.p = true;
        JOptionPane.showMessageDialog(null, "You lost! You scored " + score);
        setChanged();
        notifyObservers();
    }

    public void setlevel(LinkedList<displayable> barriers, int end){
        this.end = end;
        this.barriers = barriers;
        setChanged();
        notifyObservers();
    }

    public void restart(){
        this.fail = false;
        this.won = false;
        end = 3000;
        score = 0;
        x = 150;
        y = 50;
        xspeed = 0;
        yspeed = 0;
        xspeed2 = 0;
        yspeed2 = 0;
        p = true;
        this.barriers = new LinkedList<>();
        for(int i = 0; i < 20; i++){
            barriers.add(new displayable(400* i + 800, 30, 300, 50));
            barriers.add(new displayable(400* i + 1000,200, 300,50));
        }
        pullback.cancel();
        pullback.purge();
        muser.cancel();
        muser.purge();

        muser = new Timer();
        pullback = new Timer();


        muser.schedule(new TimerTask() {
            @Override
            public void run() {
                move();
            }
        },0,1000/60);


        pullback.schedule(new TimerTask() {
            @Override
            public void run() {
                if(p) return;
                score+=5;
                if(x > end){
                    won = true;
                    pa();
                }
                for(displayable e : barriers){
                    if(e.boom(x,y)){
                        stop();
                        return;
                    }
                    e.x -= 5;
                }
                x -= 5;
                end -=5;
            }
        },0,1000/60);
    }

    public void toggle(){
        this.p = !this.p;
        setChanged();
        notifyObservers();
    }

    public void pa(){
        this.p = true;
        setChanged();
        notifyObservers();
    }
    public void st(){
        this.p = false;
        setChanged();
        notifyObservers();
    }




}
