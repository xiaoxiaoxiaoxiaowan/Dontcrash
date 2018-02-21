import java.awt.*;

public class displayable {
    public int x;
    public int y;
    public int length;
    public int width;

    displayable(int x, int y, int length, int width){
        this.x = x;
        this.y = y;
        this.length = length;
        this.width = width;
    }


    public void paint(Graphics g){
        g.setColor(Color.gray);
        g.fillRect(x,y,width,length);
    }

    public void paintfrom(Graphics g, int x){
        g.setColor(Color.gray);
        g.fillRect(this.x - x,y,width,length);
    }

    public boolean boom(int x, int y){
        Rectangle r1 = new Rectangle(this.x, this.y, this.width, this.length);
        Rectangle r2 = new Rectangle(x,y,50,50);
        return r1.intersects(r2);
    }

    public boolean in(int x, int y){
        Rectangle r = new Rectangle(this.x, this.y, this.width, this.length);
        return r.contains(x,y);
    }

    public int conner(int x, int y){
        Rectangle lu = new Rectangle(this.x - 15, this.y - 15, 30, 30);
        Rectangle lb = new Rectangle(this.x -15, this.y - 15 + this.length, 30,30);
        Rectangle ru = new Rectangle(this.x - 15 + this.width, this.y - 15, 30,30);
        Rectangle rb = new Rectangle(this.x - 15 + this.width, this.y - 15 + this.length,30,30);
        if(lu.contains(x,y)) return 0;
        if(lb.contains(x,y)) return 1;
        if(ru.contains(x,y)) return 2;
        if(rb.contains(x,y)) return 3;
        else return -1;
    }
}
