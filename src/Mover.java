import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;


public class Mover {

	public static int RANGE=10,STEPS=10,MAX_MOVE=10;
	private ArrayList<Pixel> pixels = new ArrayList<Pixel>();
	private Pixel[][] image;
	BufferedImage orgianalImage;
	Random rand =  new Random(324);
	private int step=0;
		
	public Mover(BufferedImage i){
		orgianalImage=i;
		organisePixels();
		randomisePixles();
		reset();
		run();
	}
	
	private void organisePixels(){
		int height = orgianalImage.getHeight();
		int width = orgianalImage.getWidth();
		image = new Pixel[width][height];
		for(int i=0;i<width-1;i++){
			for(int k=0;k<height-1;k++){
				
				Pixel p = new Pixel(i,k,new Color(orgianalImage.getRGB(i, k)));
				pixels.add(p);
				image[i][k]=p;
			}
		}
	}
	
	private void randomisePixles(){
		int done=0,tries=0,rangeExtention=0;
		for(int i=0;i<pixels.size();i++){
			Pixel p=pixels.get(i);
			int x=p.getX();
			int y = p.getY();
			int moveX=0,moveY=0;
			do{
				do{
					tries++;
					moveX=rand.nextInt(RANGE+rangeExtention)-(RANGE+rangeExtention)/2;
				}while(x+moveX<0 || x+moveX>=image.length);
				do{
					tries++;
					moveY=rand.nextInt(RANGE+rangeExtention)-(RANGE+rangeExtention)/2;
				}while(y+moveY<0 || y+moveY>=image[0].length);
				if(tries%10000==0)
					rangeExtention++;
				else if(tries>4000000){
					int j=0,k=0;
					while(j<image.length && image[j][k]!=null && image[j][k].hasMoved()){
						
						while(k<image[0].length && image[j][k]!=null && image[j][k].hasMoved()){
							k++;
						}
						k=0;
						j++;
					}
					x=j;
					y=k;
					moveX=0;
					moveY=0;
					if(j==image.length)
						return;
				}
				
			}while(image[x+moveX][y+moveY]!=null && image[x+moveX][y+moveY].hasMoved());
			//System.out.println(done+"/"+pixels.size()+" - "+tries);
			tries=0; rangeExtention=0;
			image[x+moveX][y+moveY]=p;
			p.moveTo(x+moveX, y+moveY);
			p.toggleMoved();
			done++;
			
		}
	}
	private void reset(){
		for(int i=0;i<pixels.size();i++){
			Pixel p=pixels.get(i);
			p.reset();
			}
	}
	
	private void saveImage(){
		BufferedImage buffImg =createImage();
		try {
			ImageIO.write(buffImg, "png", new File("save"+step+".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private BufferedImage createImage(){
		BufferedImage buffImg = new BufferedImage(image.length, image[0].length, 1);
		for(int i =0;i<image.length;i++){
			for(int k=0;k<image[0].length;k++){
				buffImg.setRGB(i, k, image[i][k].getRGB());
			}
		}
		return buffImg;
	}
	
	private void step(){
		int done=0;
		for(int i=0;i<pixels.size();i++){
			Pixel p=pixels.get(i);
			p.step(image);
			done++;
			System.out.println(done+"/"+pixels.size());
		}
		reset();
		saveImage();
		step++;
	}
	
	private void run(){
		for(int i=0;i<STEPS;i++){
			step();
		}
	}
	
	
	
	
	public static void main(String[] args){
		try {
			BufferedImage img = ImageIO.read(new File("C:\\eggs-full.jpg"));
			new Mover(img);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
