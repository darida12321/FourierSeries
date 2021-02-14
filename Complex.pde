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

  Complex copy(){
    return new Complex(r, i);
  }

  Complex add(Complex o){
    return new Complex(r + o.r, i + o.i);
  }
  Complex mult(float s){
    return new Complex(r*s, i*s);
  }

  Complex mult(Complex o){
    return new Complex(r*o.r - i*o.i, r*o.i + i*o.r);
  }

  String toStr(){
    return "(" + str(r) + " + " + str(i) + "i)";
  }
}
