import java.awt.Color;


public class Pixel {
	
	private int startX,startY,movedX,movedY,currentX,currentY;
	private Color color;
	private double angle,distance;
	private boolean moved = false;
	
		public Pixel(int x,int y, Color c){
			startX=x;
			startY=y;
			currentX=x;
			currentY=y;
			color=c;
		}
		
		public void moveTo(int x,int y){
			movedX=x;
			movedY=y;
			int distX=x-startX,distY=y-startY;
			distance=Math.sqrt(Math.pow(distX, 2)+Math.pow(distY, 2));
		}
		public void calcDist(){
			int distX=movedX-startX,distY=movedY-startY;
			distance=Math.sqrt(Math.pow(distX, 2)+Math.pow(distY, 2));
		}
		public void calcAngle(){
			int distX=movedX-startX,distY=movedY-startY;
			angle = Math.sin(distY/distX);
		}
		
		public boolean isInOrginalPlace(){
			return startX==currentX && startY==currentY;
		}
		
		public int getX(){
			return currentX;
		}
		public int getY(){
			return currentY;
		}
		
		public void step(Pixel[][] grid){
			
			boolean found=false;
			int x=0,y=0,diff=1,desieredX,desieredY;
			desieredX = (int) (Math.acos(angle)*distance/Mover.STEPS);
			desieredY = (int) (Math.asin(angle)*distance/Mover.STEPS);
			if(grid[desieredX][desieredY]==null){
				found=true;
				grid[desieredX][desieredY]=this;
			}
			else{
				int diffX=0,diffY=0;
				do{
					diffY=-1*diff;
					while(!found && diffY <= diff) //go through horizontal boarders
					{
						x=desieredX-diff; //set the lower boarder check point
						y=desieredY+diffY; //add the difference to Y
						if(x>=0 && y>=0 && !grid[x][y].hasMoved())
							found=true;
						else{ 
							x=desieredX+diff;
							if(x>=0 && y>=0 && !grid[x][y].hasMoved())
								found=true;
						}
						diffY++;
					}
					
					diffX=-1*diff;
					while(!found && diffX<=diff){
						x=desieredX+diffX;//add the difference to X
						y=desieredY+diff;//set the right boarder
						if(x>=0 && y>=0 && !grid[x][y].hasMoved())
							found=true;
						else{
							y=desieredY-diff;//set the left boarder
							if(x>=0 && y>=0 && !grid[x][y].hasMoved())
								found=true;
						}
						diffX++;
						
					}
					
					
				}while(!found && diff<Mover.MAX_MOVE);
			}
			
			int distX=currentX-startX,distY=currentY-startY;
			double distanceUnmoved=Math.sqrt(Math.pow(distX, 2)+Math.pow(distY, 2));
			distX=x-startX;distY=y-startY;
			double distanceMoved=Math.sqrt(Math.pow(distX, 2)+Math.pow(distY, 2));
			if(distanceUnmoved>distanceMoved)
				grid[x][y]=this;
			else
				grid[currentX][currentY]=this;
			moved=true;
		}
		public boolean hasMoved(){
			return moved;
		}
		public void reset(){
			moved=false;
		}
		public int getRGB(){
			return color.getRGB();
		}
		public void toggleMoved(){
			moved=!moved;
		}
}
