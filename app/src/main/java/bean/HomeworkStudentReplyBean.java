package bean;

public class HomeworkStudentReplyBean {
    private String student_id,homework_id,stu_description,stu_homework_file;

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getHomework_id() {
        return homework_id;
    }

    public void setHomework_id(String homework_id) {
        this.homework_id = homework_id;
    }

    public String getStu_description() {
        return stu_description;
    }

    public void setStu_description(String stu_description) {
        this.stu_description = stu_description;
    }

    public String getStu_homework_file() {
        return stu_homework_file;
    }

    public void setStu_homework_file(String stu_homework_file) {
        this.stu_homework_file = stu_homework_file;
    }
}
