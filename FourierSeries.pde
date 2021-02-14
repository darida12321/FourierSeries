
float rotTime = 5;
int n = 300;

Series series;
SegmentedF func = new SegmentedF();

void setup(){
  size(600, 600);
  frameRate(60);
}

void draw(){
  if(series == null){ return; }

  series.update();

  background(200);
  pushMatrix();
  translate(300, 300);
  series.display();
  popMatrix();
}

void mouseDragged() {
  if(series != null){ return; }
  func.addFunc(new LinearSegmentF(
    new Complex(pmouseX-300, pmouseY-300),
    new Complex(mouseX-300, mouseY-300)
  ));
  line(pmouseX, pmouseY, mouseX, mouseY);
}
void keyPressed() {
  if(keyCode == 32 && series == null){
    series = new Series(n, func, 1/rotTime);
  }
}
