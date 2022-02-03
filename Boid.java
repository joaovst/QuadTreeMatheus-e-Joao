import java.awt.Color;
import java.awt.Graphics2D;

public class Boid
{
    public double x,y;
    public double radius, speed,speedX,speedY;



    Boid (int _x , int _y, int _radius){
        this.x = _x;
        this.y = _y;
        this.radius = _radius;
        this.speed = 0.05;
        this.speedX = this.speed;
        this.speedY = this.speed;
    }

    public boolean Check_Collision (Boid boid) {
    	double x;
    	double y;


        x = this.x - boid.x;
        y = this.y - boid.y;

        if (Math.sqrt((x*x) + (y*y)) < this.radius) {
            return true;
        } else {
            return false;
        }
    }

    public void Inverse(){
        if(Math.random() > 0.5)
            this.speedX =  -this.speedX;
        if(Math.random() > 0.5)
            this.speedY = -this.speedY;
    }

    public boolean Colision(Boid boid){
        if(this.Check_Collision(boid)){
            this.Inverse();
            boid.Inverse();
            return true;
        }
        return false;
    }

    public void Update (Graphics2D g2d, Color color) {

        if(this.x + this.radius > Janela.largura)
            this.speedX = -this.speed;
        else if(this.x - this.radius < 0)
            this.speedX = this.speed;

        if(this.y + this.radius > Janela.altura)
            this.speedY = -this.speed;
        else if(this.y - this.radius < 0)
            this.speedY = this.speed;

        this.x += this.speedX;
        this.y += this.speedY;


    	g2d.setColor(color);
		g2d.drawRect((int)y, (int)x, (int)radius, (int)radius);
    }
}
