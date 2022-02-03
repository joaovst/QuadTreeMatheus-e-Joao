



import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Comparator;

public class QuadTree
{
    private QuadTree root;
    private int x,y, max_boids;
    private int width,height;
    private ArrayList<Boid> boids;
    private boolean hasLeaf;
    private QuadTree northwest,northeast,southwest,southeast;

    QuadTree(QuadTree root, int x , int y, int width, int height){
        this.root = root;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.boids = new ArrayList<Boid>();

        this.hasLeaf = false;
        this.northwest = null;
        this.southwest = null;
        this.southeast = null;
        this.northeast = null;

        this.max_boids = 10;
    }

    public boolean IsInside(Boid boid){
        if(boid.x >= this.x
                & boid.x <= this.x + this.width)
        {
            if(boid.y >= this.y
                    & boid.y <= this.y + this.height)
            {
                return true;
            }
        }
        return false;
    }

    public void AddFromLeaf(Boid boid){
        if(!this.AddToLeaf(boid)){
        	if(this.root != null)
            this.root.AddFromLeaf(boid);
        }
    }

    public boolean AddToLeaf(Boid b)
    {
        if(this.northwest.IsInside(b)){
            this.northwest.AddBoid(b);
            return true;
        }

        if(this.southwest.IsInside(b)){
            this.southwest.AddBoid(b);
            return true;
        }

        if(this.southeast.IsInside(b)){
            this.southeast.AddBoid(b);
            return true;
        }

        if(this.northeast.IsInside(b)){
            this.northeast.AddBoid(b);
            return true;
        }

        return false;
    }

    public void Divide(Boid boid){
        this.hasLeaf = true;

        int w = this.width / 2;
        int h = this.height / 2;
        int x = this.x;
        int y = this.y;

        this.northwest = new QuadTree(this, x, y, w, h);
        y += h;
        this.southwest = new QuadTree(this, x, y, w, h);
        x += w;
        this.southeast = new QuadTree(this, x, y, w, h);
        y -= h;
        this.northeast = new QuadTree(this, x, y, w, h);

        for (int i = 0; i < this.boids.size(); i++) {
            Boid b = this.boids.get(i);
            this.AddToLeaf(b);
        }

        this.AddToLeaf(boid);

    }

    public void AddBoid(Boid boid){
        if(!this.hasLeaf){
            if(this.boids.size() >= this.max_boids  && this.width > 75){
                this.Divide(boid);
                this.boids = new ArrayList<Boid>();
                return;
            }
            this.boids.add(boid);
        }
        else{
            if(!this.AddToLeaf(boid)) {
            	System.out.println("Eita");
            	System.out.println(boid.x + "." + boid.y);
            }
        }
    }

    public void UpdateBoids(Graphics2D g2d ,Color color){
        if(this.root != null){
            for (int i = this.boids.size() - 1; i >=0 ; i--)
            {
                Boid boid = this.boids.get(i);
                if(!this.IsInside(boid)){
                    this.root.AddFromLeaf(boid);
                    this.boids.remove(i);
                }
            }
        }

        for (int i = 0; i < this.boids.size(); i++)
        {
            Boid boid = this.boids.get(i);

            for (int j = i + 1; j < this.boids.size(); j++)
            {
                boolean col = boid.Colision(this.boids.get(j));
                if(col)
                    break;
            }

            boid.Update(g2d, color);
        }
    }
    

    public int GetAmountBoids(){
        if(this.hasLeaf){
            int amount =
                    this.northwest.GetAmountBoids() +
                            this.southwest.GetAmountBoids() +
                            this.southeast.GetAmountBoids() +
                            this.northeast.GetAmountBoids();
            return amount;
        }
        else{
            return this.boids.size();
        }
    }

    public void Update (Graphics2D g2d, Color color)
    {
        if(this.hasLeaf){

            int amount =
                    this.northwest.GetAmountBoids() +
                            this.southwest.GetAmountBoids() +
                            this.southeast.GetAmountBoids() +
                            this.northeast.GetAmountBoids();

            if(amount > 0){
                this.northwest.Update(g2d, Color.red);
                this.southwest.Update(g2d, Color.green);
                this.southeast.Update(g2d, Color.blue);
                this.northeast.Update(g2d, Color.yellow);
            }
            else{
                this.hasLeaf = false;
            }

        }
        else{
        	g2d.setColor(color);
    		g2d.drawRect(y, x, width, height);
            this.UpdateBoids(g2d, color);
        }
    }
}