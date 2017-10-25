/*The UI can adjust score factor, difficulty, speed factor, size of main area and square, and 
 When a new shape has no space to fall, i.e. existing shapes in “Main area” pile up to near the top, 
 the game terminates. */
package HW1;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.awt.Font;
import java.awt.Frame;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class finalTetris extends JPanel {
private static final long serialVersionUID = -8715353373678321308L;
float rWidth = 30.0F, rHeight = 25.0F;
static float pixelSize;
static float unit;   
static int centerY;
int maxY;
int maxX;
static Point pieceOrigin;
private Point ExamplePiece;
static int currentPiece;
int nextOnePiece;
private static int rotation;
private ArrayList<Integer> nextPieces = new ArrayList<Integer>();
private Color[][] well;
private boolean p=false;
private static boolean isFull=false;
private static int numClears=0;
private static int level=1;
private long score;
Parameter i=new Parameter();
void initgr() {  
	Dimension d = getSize(); 
	maxX = d.width - 1;
	maxY = d.height - 1; 
	pixelSize = Math.max(rWidth/maxX, rHeight/maxY);   
	unit=(float) (i.size*Math.min(maxX/rWidth, maxY/rHeight));
	centerY=maxY;
	}
static int iX(float x){return Math.round(x/pixelSize);} 
static int iY(float y){return Math.round(centerY - y/pixelSize);} 
float fx(int x){return x  * pixelSize;} 
float fy(int y){return (centerY - y) * pixelSize;}
int tr(float x){return Math.round(x);}
private final static Point[][][] Tetraminos = {
		// I-Piece
		{
		{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
		{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) },
		{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
		{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) }
		},
		// L-Piece
		{
		{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 0) },
		{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 2) },
		{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 2) },
		{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 0) }
		},
		// J-Piece
		{
		{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 2) },
		{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 2) },
		{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 0) },
		{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 0) }
		},
		// O-Piece
		{
		{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
		{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
		{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
		{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) }
		},
		// S-Piece
		{
		{ new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
		{ new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
		{ new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
		{ new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) }
		},
		// T-Piece
		{
		{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1) },
		{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
		{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(1, 2) },
		{ new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(1, 2) }
		},
		// Z-Piece
		{
		{ new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
		{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) },
		{ new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
		{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) }
		}
	};
	
	private final Color[] tetraminoColors = {
	Color.cyan, Color.red, Color.blue, Color.green, Color.yellow, Color.pink, Color.orange
	};
	// Creates a border around the well and initializes the dropping piece
	private void init() {
		well = new Color[12][24];
		for (int i = 0; i < 12; i++) {
			for (int j = 0; j < 24; j++) {
				well[i][j] = Color.WHITE;
		}
		}
		newPiece();
	}
	// Put a new, random piece into the dropping position
	public void newPiece() {
		pieceOrigin = new Point(5, 20);
		rotation = 0;
		if (nextPieces.isEmpty()) {
			Collections.addAll(nextPieces, 0, 1, 2, 3, 4, 5, 6);
			Collections.shuffle(nextPieces);
		}
		currentPiece = nextPieces.get(0);
		nextPieces.remove(0);
		if (!nextPieces.isEmpty()) nextOnePiece=nextPieces.get(0);
	}
	private boolean noSpace(int x, int y, int rotation){
		for (Point p : Tetraminos[currentPiece][rotation]) {
			if (p.y+y>20 && well[p.x + x][p.y + y-1] != Color.WHITE )  return true;
		}
		return false;
	} 
	private boolean collidesAt(int x, int y, int rotation) {
		for (Point p : Tetraminos[currentPiece][rotation]) {
			if (p.x+x<0 || p.x+x>11||p.y+y<3 || well[p.x + x][p.y + y-2] != Color.WHITE)  return true;
		}
		return false;
	}
	// Rotate the piece clockwise or counterclockwise
		public void rotate(int i) {
			int newRotation;
			if(rotation +i>=0) newRotation = (rotation +i) % 4;
			else newRotation=3;
			if(noSpace(pieceOrigin.x, pieceOrigin.y, newRotation)){
				isFull=true;
			}
			if (!collidesAt(pieceOrigin.x, pieceOrigin.y, newRotation)) {
				rotation = newRotation;
			}
			repaint();
		}
		
	// Move the piece left or right
	public void move(int i) {
		if(noSpace(pieceOrigin.x, pieceOrigin.y, rotation)){
			isFull=true;
		}
		if (!collidesAt(pieceOrigin.x + i, pieceOrigin.y, rotation)) {
			pieceOrigin.x += i;	
		}
		repaint();
	}
	
	// Drops the piece one line or fixes it to the well if it can't drop
	public void dropDown() {
		if(noSpace(pieceOrigin.x, pieceOrigin.y, rotation)){
			isFull=true;
		}
		if (!collidesAt(pieceOrigin.x, pieceOrigin.y + 1, rotation)) {
			pieceOrigin.y -= 1;
		} else {
			fixToWell();
		}	
		repaint();
	}
	
	// Make the dropping piece part of the well, so it is available for
	// collision detection.
	public void fixToWell() {
		for (Point p : Tetraminos[currentPiece][rotation]) {
			well[pieceOrigin.x + p.x][pieceOrigin.y + p.y] = tetraminoColors[currentPiece];
		}
		clearRows();
		newPiece();
	}
	public void deleteRow(int row) {
		for (int j = row+1; j <24; j++) {
			for (int i = 0; i < 12; i++) {
				well[i][j-1] = well[i][j];
			}
		}
	}
	public void clearRows() {
		boolean gap;
		for (int j = 0; j < 24; j++) {
			gap = false;
			for (int i = 0; i < 12; i++) {
				if (well[i][j] == Color.WHITE) {
					gap = true;
					break;
				}
			}
			if (!gap) {
				deleteRow(j);
				j -= 1;
				numClears += 1;
				level+=numClears/i.N;
				score+=level*i.M;
			}
		}
	}
	private void drawPiece(Graphics g){
		initgr();
		g.setColor(tetraminoColors[currentPiece]);
		for (Point p : Tetraminos[currentPiece][rotation]) {
			g.fillRect(iX(p.x + pieceOrigin.x), 
					   iY(p.y + pieceOrigin.y), 
					   tr(unit)-1, tr(unit)-1);
		}
		g.setColor(tetraminoColors[nextOnePiece]);
		for (Point p : Tetraminos[nextOnePiece][0]) {
			g.fillRect(iX(p.x + 16), 
					   iY(p.y + 20), 
					   tr(unit)-1, tr(unit)-1);
		}
	}
	private void drawPause(Graphics g){
		initgr();
		g.setColor(Color.WHITE);
		g.fillRect(iX(2)-5, iY(10)+5, 8*tr(unit)+10, 4*tr(unit)+10);
		g.setColor(Color.BLUE);
		g.drawRect(iX(2), iY(10), 8*tr(unit), 4*tr(unit));
		g.setColor(Color.BLACK);
		g.setFont(new Font("Courier New", 1,20)); 
		g.drawString("PAUSE", iX(5)-3, iY(8)-4);
	}
	private void transform(){
		currentPiece=(currentPiece+1)%7;
		repaint();
	}
	private void drawInfo(Graphics g){
		initgr();
		g.setColor(Color.BLACK);
		g.setFont(new Font("Courier New", 1,20)); 
		g.drawString("LEVEL:        "+level, iX(13), iY(13));
		g.drawString("LINES:        "+numClears, iX(13), iY(11));
		g.drawString("SCORES:       "+score, iX(13), iY(9));
	}
	private void drawExit(Graphics g){
		initgr();
		g.setColor(Color.WHITE);
		g.fillRect(iX(17), iY(4),3*tr(unit),2*tr(unit));
		g.setColor(Color.BLACK);
		g.drawRect(iX(17), iY(4),3*tr(unit),2*tr(unit));
		g.drawString("EXIT", iX(17)+tr(unit)/2, iY(3)+tr(unit)/2);
	}
	private void pause(int i){
		if(i==1) p=true;
		else p=false;
		repaint(); 
	}
	
	@Override 
	public void paintComponent(Graphics g)
	{
		initgr();
	// Paint the well
	g.setColor(Color.WHITE);
	g.fillRect(iX(0), iY(23), tr(unit)*12, tr(unit)*23);
	for (int i = 0; i < 12; i++) {
	for (int j = 1;j < 24; j++) {
	g.setColor(well[i][j]);
	g.fillRect(iX(i), iY(j), tr(unit)-1, tr(unit)-1);
	}
	}
	//Paint the next piece;
	g.setColor(Color.white);
	g.fillRect(iX(14), iY(22), tr(unit)*6,tr(unit)*4);
	//draw score
	drawInfo(g);
	// Draw the currently falling piece
	drawPiece(g);
	drawExit(g);
	if(p==true) drawPause(g);
	}
	
	public static void main(String[] args) {
		final finalTetris game = new finalTetris();
		JFrame f = new JFrame("finalTeris");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize((int)(26*26*game.i.size*game.i.size), (int)(26*26*game.i.size*game.i.size));
		f.setVisible(true);
		game.initgr();
		game.init();
		f.add(game);
		f.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				int x=e.getX();
				int y=e.getY();
				if(x>17*Math.round(game.unit) && x<20*Math.round(game.unit) && y>game.maxY-3*Math.round(game.unit) && y<game.maxY-0.5*Math.round(game.unit)){ 
					if(e.getButton()==MouseEvent.BUTTON1)  
					{
					 f.setVisible(false);
		             f.dispose();
	            	}
				}
				if(x>12*Math.round(game.unit)){
					if(e.getButton()==MouseEvent.BUTTON1) game.move(-1);
					if(e.getButton()==MouseEvent.BUTTON3) game.move(+1);
				}
			}
		});
		f.addMouseWheelListener(new MouseAdapter(){
			public void mouseWheelMoved(MouseWheelEvent e){
				if(e.getWheelRotation()==1){
					game.rotate(e.getWheelRotation());
				}
				if(e.getWheelRotation()==-1){
					game.rotate(e.getWheelRotation());
				}
			}
		});
		Thread t=new Thread() {
			@Override public void run() {
				while (true) {
					try {
						int sleep=(int)(1000*game.i.S/level);
						Thread.sleep(sleep);
						game.dropDown();
						f.setSize((int)(26*26*game.i.size*game.i.size), (int)(26*26*game.i.size*game.i.size));
						if(isFull==true){
							f.setVisible(false);
				             f.dispose();
						}
					} catch ( InterruptedException e ) {}
				}
			}
		};
		 t.start();
		f.addMouseMotionListener(new MouseAdapter(){
			public void mouseMoved(MouseEvent e){
				int x=e.getX();
				int y=e.getY();
				if(x>0 && x<13*Math.round(game.unit) && y>game.maxY-23*Math.round(game.unit)) 
					{t.suspend();
					game.pause(1);
					
					for(Point p:Tetraminos[currentPiece][rotation]){
						if(x>iX(p.x+pieceOrigin.x) && x<iX(p.x+pieceOrigin.x)+Math.round(game.unit)&&y>iY(p.y+pieceOrigin.y-2)&&y<iY(p.y+pieceOrigin.y-2)+Math.round(game.unit)){
							//System.out.print("DONE"+"x:"+x+"y:"+y);
							game.transform();
							game.score-=level*game.i.M;
							break;
						}
					}
					
					}
				else {
					t.resume();
					game.pause(0);}
			}
		});
		}
	}
class Parameter extends JFrame implements ItemListener{
	private static final long serialVersionUID = -8715353373678321300L;
    JComboBox jcb1,jcb2,jcb3, jcb4;  //下拉框  
    JPanel jp1, jp2, jp3, jp4;    //面板  
    JLabel jlb1, jlb2, jlb3, jlb4;  
    int M=1;
    int N=20;
    double S=1;
    float size=1;
    //构造函数  
    public Parameter(){  
    	
        jp1 = new JPanel();  
        jp2 = new JPanel(); 
        jp3 = new JPanel();
        jp4 = new JPanel();
        jlb1 = new JLabel("scoring factor：");  
        String str1[] = {"1","2","3", "4","5","6","7","8","9","10"};  
        jcb1 = new JComboBox(str1);
        jcb1.addItemListener(this);
        
        jlb2 = new JLabel("difficulty：");  
        String str2[] = {"20", "25", "30", "35", "40", "45", "50"};  
        jcb2 = new JComboBox(str2);
        jcb2.addItemListener(this);
        
        jlb3 = new JLabel("speed factor: ");
        String str3[] = {"0.1","0.2","0.3","0.4","0.5","0.6","0.7","0.8","0.9","1.0"};
        jcb3 = new JComboBox(str3);
        jcb3.addItemListener(this);
         
        jlb4 = new JLabel("windows mode: ");
        String str4[] = {"standard","enlarged"};
        jcb4 = new JComboBox(str4);
        jcb4.addItemListener(this); 
          
        jp1.add(jlb1);  
        jp1.add(jcb1);  
          
        jp2.add(jlb2);  
        jp2.add(jcb2);
        
        jp3.add(jlb3);
        jp3.add(jcb3);
        
        jp4.add(jlb4);
        jp4.add(jcb4);
        //int  = jcb1.getSelectedIndex()
        this.setLayout(new GridLayout(3, 1));  
          
        this.add(jp1);  
        this.add(jp2);
        this.add(jp3);
        this.add(jp4);
          
        this.setSize(300,300);  
        this.setBounds(650, 350, 200, 200);
        this.setTitle("Parameter Setting");  
        this.setVisible(true);  
        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
    }
    
    

	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		if (e.getStateChange() == ItemEvent.SELECTED) {
			if((JComboBox)e.getSource()==jcb1){
				this.M = jcb1.getSelectedIndex();
			}
			if((JComboBox)e.getSource()==jcb2){
				this.N = jcb2.getSelectedIndex()*5+20;
			}
			if((JComboBox)e.getSource()==jcb3){
				this.S = 0.1*(jcb3.getSelectedIndex()+1);
			}
			if((JComboBox)e.getSource()==jcb4){
				this.size = (float) (jcb4.getSelectedIndex()==0?1:1.2);
			}
		}
	}
}