import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Fourier03_customFunc extends PApplet {


float rotTime = 5;
int n = 500;

Series series;
SegmentedF func = new SegmentedF();

public void setup(){
  
  frameRate(60);
}

public void draw(){
  if(series == null){ return; }

  series.update();

  background(200);
  pushMatrix();
  translate(300, 300);
  series.display();
  popMatrix();
}

public void mouseDragged() {
  if(series != null){ return; }
  func.addFunc(new LinearSegmentF(
    new Complex(pmouseX-300, pmouseY-300),
    new Complex(mouseX-300, mouseY-300)
  ));
  line(pmouseX, pmouseY, mouseX, mouseY);
}
public void keyPressed() {
  if(keyCode == 32 && series == null){
    series = new Series(n, func, 1/rotTime);
  }
}
class Complex{
  float r, i;

  Complex(float r, float i){
    this.r = r; this.i = i;
  }
  Complex(float x){
    this.r = cos(x*2*PI);
    this.i = sin(x*2*PI);
  }
  Complex(){
    this.r = 0; this.i = 0;
  }

  public Complex copy(){
    return new Complex(r, i);
  }

  public Complex add(Complex o){
    return new Complex(r + o.r, i + o.i);
  }
  public Complex mult(float s){
    return new Complex(r*s, i*s);
  }

  public Complex mult(Complex o){
    return new Complex(r*o.r - i*o.i, r*o.i + i*o.r);
  }

  public String toStr(){
    return "(" + str(r) + " + " + str(i) + "i)";
  }
}

interface Function{
  public Complex get(float t);
}

class LinearSegmentF implements Function{
  Complex p1, p2;

  LinearSegmentF(Complex p1, Complex p2){
    this.p1 = p1.copy();
    this.p2 = p2.copy();
  }

  public Complex get(float t){
    return p1.mult(1-t).add(p2.mult(t));
  }
}
class DiscontSegmentF implements Function{
  Complex p1, p2;

  DiscontSegmentF(Complex p1, Complex p2){
    this.p1 = p1.copy();
    this.p2 = p2.copy();
  }

  public Complex get(float t){
    return t<=0.5f ? p1.copy() : p2.copy();
  }
}

class SegmentedF implements Function{
  ArrayList<Function> functions;

  SegmentedF(){
    functions = new ArrayList<Function>();
  }

  public void addFunc(Function f){
    functions.add(f);
  }

  public Complex get(float t){
    if(functions.size() == 0){ return new Complex(0, 0); }
    t -= floor(t);

    int index = floor(t*functions.size());
    Function f = functions.get(index);
    float ft = t*functions.size() - floor(t*functions.size());

    return f.get(ft);
  }
}

// Fourier series
class Series{
  // Variables
  float t = 0;
  int n;
  Tracer tracer;
  Complex[] cValues;
  float dt = 0;

  // Constructor
  Series(int n, Function func, float rps){
    this.n = n;
    this.dt = rps/(60);

    cValues = new Complex[2*n+1];
    for(int i = 0; i < 2*n+1; i++){
      int f = i%2==0 ? i/2 : -(i+1)/2;
      cValues[i] = getC(f, n*2, func);
    }

    this.tracer = new Tracer(getEnd(), round(60/rps));
  }

  // Get the C for a given frequency
  public Complex getC(float f, int prec, Function func){
    Complex sum = new Complex(0, 0);
    for(int i = 0; i < prec; i++){
      float t = (float)i/(float)prec;
      Complex c = new Complex(f*t);
      sum = sum.add(c.mult(func.get(t)).mult(1/(float)prec));
    }
    return sum;
  }

  public Complex getEnd(){
    Complex end = new Complex();
    for(int i = 0; i < 2*n+1; i++){
      int f = i%2==0 ? i/2 : -(i+1)/2;
      end = end.add(new Complex(f*t).mult(cValues[i]));
    }
    return end;
  }

  // Update
  public void update(){
    t -= dt;
    tracer.addPoint(getEnd());
  }
  // Display
  public void display(){

    displayArrows();
    tracer.display();

    Complex end = getEnd();
    fill(0);
    ellipse(end.r, end.i, 10, 10);
  }
  public void displayArrows(){
    strokeWeight(1); stroke(0);
    noFill();
    Complex arrowBase = new Complex();
    for(int i = 0; i < 2*n+1; i++){
      int f = i%2==0 ? i/2 : -(i+1)/2;

      displayArrow(arrowBase, cValues[i], f);
      arrowBase = arrowBase.add(new Complex(f*t).mult(cValues[i]));
    }
  }
  public void displayArrow(Complex base, Complex c, int f){
    Complex tip = base.add(new Complex(f*t).mult(c));
    line(base.r, base.i, tip.r, tip.i);

    float r = sqrt(sq(c.r) + sq(c.i));
    ellipse(base.r, base.i, 2*r, 2*r);
  }
}
class Tracer{
  ArrayList<Complex> list;

  Tracer(Complex start, int size){
    list = new ArrayList<Complex>();
    for(int i = 0; i < size; i++){
      list.add(start.copy());
    }
  }

  public void addPoint(Complex p){
    for(int i = list.size()-1; i > 0; i--){
      list.set(i, list.get(i-1));
    }
    list.set(0, p.copy());
  }

  public void display(){

    strokeWeight(5);
    for(int i = 0; i < list.size()-1; i++){
      stroke(255, 0, 0, 255*(1-(float)i/(float)list.size()));
      Complex s = list.get(i);
      Complex e = list.get(i+1);
      if(sq(s.r-e.r) + sq(s.i-e.i) > sq(20)){
        point(s.r, s.i);
      }else{
        line(s.r, s.i, e.r, e.i);
      }
    }
    alpha(1);
  }
}
  public void settings() {  size(600, 600); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Fourier03_customFunc" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
