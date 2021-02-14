
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
  Complex getC(float f, int prec, Function func){
    Complex sum = new Complex(0, 0);
    for(int i = 0; i < prec; i++){
      float t = (float)i/(float)prec;
      Complex c = new Complex(f*t);
      sum = sum.add(c.mult(func.get(t)).mult(1/(float)prec));
    }
    return sum;
  }

  Complex getEnd(){
    Complex end = new Complex();
    for(int i = 0; i < 2*n+1; i++){
      int f = i%2==0 ? i/2 : -(i+1)/2;
      end = end.add(new Complex(f*t).mult(cValues[i]));
    }
    return end;
  }

  // Update
  void update(){
    t -= dt;
    tracer.addPoint(getEnd());
  }
  // Display
  void display(){

    displayArrows();
    tracer.display();

    Complex end = getEnd();
    fill(0);
    ellipse(end.r, end.i, 10, 10);
  }
  void displayArrows(){
    strokeWeight(1); stroke(0);
    noFill();
    Complex arrowBase = new Complex();
    for(int i = 0; i < 2*n+1; i++){
      int f = i%2==0 ? i/2 : -(i+1)/2;

      displayArrow(arrowBase, cValues[i], f);
      arrowBase = arrowBase.add(new Complex(f*t).mult(cValues[i]));
    }
  }
  void displayArrow(Complex base, Complex c, int f){
    Complex tip = base.add(new Complex(f*t).mult(c));
    line(base.r, base.i, tip.r, tip.i);

    float r = sqrt(sq(c.r) + sq(c.i));
    ellipse(base.r, base.i, 2*r, 2*r);
  }
}
