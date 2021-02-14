
interface Function{
  Complex get(float t);
}

class LinearSegmentF implements Function{
  Complex p1, p2;

  LinearSegmentF(Complex p1, Complex p2){
    this.p1 = p1.copy();
    this.p2 = p2.copy();
  }

  Complex get(float t){
    return p1.mult(1-t).add(p2.mult(t));
  }
}
class DiscontSegmentF implements Function{
  Complex p1, p2;

  DiscontSegmentF(Complex p1, Complex p2){
    this.p1 = p1.copy();
    this.p2 = p2.copy();
  }

  Complex get(float t){
    return t<=0.5 ? p1.copy() : p2.copy();
  }
}

class SegmentedF implements Function{
  ArrayList<Function> functions;

  SegmentedF(){
    functions = new ArrayList<Function>();
  }

  void addFunc(Function f){
    functions.add(f);
  }

  Complex get(float t){
    if(functions.size() == 0){ return new Complex(0, 0); }
    t -= floor(t);

    int index = floor(t*functions.size());
    Function f = functions.get(index);
    float ft = t*functions.size() - floor(t*functions.size());

    return f.get(ft);
  }
}
