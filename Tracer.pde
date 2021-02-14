class Tracer{
  ArrayList<Complex> list;

  Tracer(Complex start, int size){
    list = new ArrayList<Complex>();
    for(int i = 0; i < size; i++){
      list.add(start.copy());
    }
  }

  void addPoint(Complex p){
    for(int i = list.size()-1; i > 0; i--){
      list.set(i, list.get(i-1));
    }
    list.set(0, p.copy());
  }

  void display(){

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
