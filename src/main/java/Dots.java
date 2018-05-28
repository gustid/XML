import java.util.*;
 
public class Dots {
protected List<Dot> dots;
public List<Dot> getDots(){
if( dots == null ) {
dots = new ArrayList<Dot>();
}
return this.dots;
}
public void setDots(List<Dot> l){
dots = l ;
}
}
