import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.JFrame;

public class Janela  extends Component{

	QuadTree quadTree;
	 private ArrayList<Boid> boids;
	public static int largura = 800;
	public static int altura = largura;
	boolean modo_quadtree=false;
	
	public Janela() {
		JFrame frame = new JFrame();		
		frame.getContentPane().setBackground(Color.black);
		frame.setSize(largura, altura);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(this);

		quadTree = new QuadTree(null, 0, 0, largura, altura);
		 this.boids = new ArrayList<Boid>();
		 
		 
		Random r = new Random();
		for (int i = 0; i < 3000; i++) {
			int x =  r.nextInt(largura-50) + 50; 
			int y = r.nextInt(altura-50) + 50;
			Boid boid = new Boid(x, y, 2) ;
			if(modo_quadtree) {
			quadTree.AddBoid(boid);
			}
			else {
			boids.add(boid);
			}
		}
		
		
	}
    
    public void paint(Graphics g) {

    	long start = System.nanoTime();
		Graphics2D g2d = (Graphics2D) g;
		
		if(modo_quadtree) {
			quadTree.Update(g2d, Color.green);
		}
		else {
			for (int i = 0; i < this.boids.size(); i++)
	        {
	            Boid boid = this.boids.get(i);
	
	            for (int j = i + 1; j < this.boids.size(); j++)
	            {
	                boolean col = boid.Colision(this.boids.get(j));
	                if(col)
	                    break;
	            }
	
	            boid.Update(g2d, Color.green);
	        }
		}
		
		long finish = System.nanoTime();
		long timeElapsed = finish - start;
		System.out.println(timeElapsed);
		repaint(10000, 0, 0, largura, altura);

	}
}
