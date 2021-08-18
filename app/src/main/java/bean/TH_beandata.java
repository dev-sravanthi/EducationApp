package bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TH_beandata {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("posted_at_one")
    @Expose
    private String posted_at_one;
    @SerializedName("subject")
    @Expose
    private List<TH_SubjectList> subjectLists;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPosted_at_one() {
        return posted_at_one;
    }

    public void setPosted_at_one(String posted_at_one) {
        this.posted_at_one = posted_at_one;
    }

    public List<TH_SubjectList> getSubjectLists() {
        return subjectLists;
    }

    public void setSubjectLists(List<TH_SubjectList> subjectLists) {
        this.subjectLists = subjectLists;
    }
}
