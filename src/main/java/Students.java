import java.util.*;
 
public class Students {
protected List<Student> students;
public List<Student> getStudents(){
if( students == null ) {
students = new ArrayList<Student>();
}
return this.students;
}
public void setStudents(List<Student> l){
students = l ;
}
}
