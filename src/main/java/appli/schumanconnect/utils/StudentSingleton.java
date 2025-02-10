package appli.schumanconnect.utils;

import appli.schumanconnect.model.Student;

public class StudentSingleton {
    private static StudentSingleton instance;
    private Student studentId;

    private StudentSingleton(){}

    public static StudentSingleton getInstance(){
        if (instance == null)
        {
            instance = new StudentSingleton();
        }
        return instance;
    }

    public void setStudentId(Student newStudentId){
        studentId = newStudentId;
    }

    public Student getStudentId(){
        return studentId;
    }
}
