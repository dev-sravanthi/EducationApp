package data.network;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import bean.AttendanceMonthWiseBean;
import bean.AttendanceOverallBean;
import bean.CircularFragBean;
import bean.ClassTestTodayBean;
import bean.FeePendingBean;
import bean.FeeStudentInvoiceBean;
import bean.ForgetVerificationBean;
import bean.HomeworkStudentReplyBean;
import bean.LibraryBorrowBook;
import bean.LibraryFineListBean;
import bean.LoginData;
import bean.PastHomeWorkBean;
import bean.ProfileDatabean;
import bean.ProfileEditBean;
import bean.ReligionBean;
import bean.SMSSpecificBean;
import bean.SMS_OverallBean;
import bean.StudentBean;
import bean.StudentDataBean_sample;
import bean.TodayHomeWork_bean;
import bean.TransportRouStoppingBean;
import bean.VoiceOverallBean;
import bean.VoiceSpecificBean;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("login")
    Call<LoginData> getLoginData(
            @Field("code")String code,
            @Field("password")String password
    );


    @GET("homework/today-homework")
    Call<TodayHomeWork_bean> getTodaysHomework(@Header("Authorization") String token,@Query("student_id") String student_id);


    @GET("homework/past-homework")
    Call<PastHomeWorkBean> getPastHomeWorkData(@Header("Authorization") String token,@Query("student_id") String student_id);


    @Headers({
            "Accept:application/json",
    })
    @GET("profile")
    Call<ProfileDatabean> getProfileData(@Header("Authorization") String token);

    @Headers({
            "Accept:application/json",
    })
    @GET("student-detail")
    Call<StudentDataBean_sample> getStudentDetails(@Header("Authorization") String token);

    @Headers({
            "Accept:application/json",
    })

    @GET("religion-list")
    Call<ReligionBean> getRelegionList(@Header("Authorization") String token);

    @Headers({
            "Accept:application/json",
    })
    @GET("profession-list")
    Call<ReligionBean> getProfessionList(@Header("Authorization") String token);

    @Headers({
            "Accept:application/json",
    })
    @GET("student-detail")
    Call<StudentBean> getStudentData(@Header("Authorization") String token);

    @Headers({
            "Accept:application/json",
    })
    @GET("attendance")
    Call<AttendanceOverallBean> getAttendanceOverall(@Header("Authorization") String token,@Query("student_id") String student_id);

    @Headers({
            "Accept:application/json",
    })
    @GET("attendance-detail")
    Call<AttendanceMonthWiseBean> getAttendanceMonthWise(@Header("Authorization") String token,@Query("student_id") String student_id
            ,@Query("month_list_id") String month_list_id);


    @Headers({
            "Accept:application/json",
    })
    @GET("fee-pending")
    Call<FeePendingBean> getFeePendingData(@Header("Authorization") String token,@Query("student_id") String student_id);

    @Headers({
            "Accept:application/json",
    })
    @GET("fee-invoice")
    Call<FeeStudentInvoiceBean> getFeeStudentInvoice(@Header("Authorization") String token,@Query("student_id") String student_id);


    @Headers({
            "Accept:application/json",
    })
    @GET("sms-overall")
    Call<SMS_OverallBean> getSMSOverall(@Header("Authorization") String token,@Query("student_id") String student_id);

    @Headers({
            "Accept:application/json",
    })
    @GET("sms-specific")
    Call<SMSSpecificBean> getSMSSpecific(@Header("Authorization") String token,@Query("student_id") String student_id);

    @Headers({
            "Accept:application/json",
    })
    @GET("voice-overall")
    Call<VoiceOverallBean> getVoiceOverall(@Header("Authorization") String token,@Query("student_id") String student_id);

    @Headers({
            "Accept:application/json",
    })
    @GET("voice-specific")
    Call<VoiceSpecificBean> getVoiceSpecific(@Header("Authorization") String token,@Query("student_id") String student_id);


    @FormUrlEncoded
    @POST("forgot-password-verification")
    Call<ForgetVerificationBean> getOTP(
            @Field("code")String code,
            @Field("phone_no")String phone_no
    );

    @FormUrlEncoded
    @POST("password-reset")
    Call<ForgetVerificationBean> setNewPassword(
            @Field("code")String code,
            @Field("phone_no")String phone_no,
            @Field("otp_code")String otp_code,
            @Field("new_password")String new_password
    );

    @FormUrlEncoded
    @POST("homework/homework-reply")
    Call<HomeworkStudentReplyBean> setHomeworkStudentReply(
            @Field("student_id")String student_id,
            @Field("homework_id")String homework_id,
            @Field("stu_description")String stu_description,
            @Field("stu_homework_file") File stu_homework_file
    );

    @Headers({
            "Accept:application/json",
    })
    @GET("circular")
    Call<CircularFragBean> getCircularData(@Header("Authorization") String token,@Query("student_id") String student_id);

    @Headers({
            "Accept:application/json",
    })
    @GET("event")
    Call<CircularFragBean> getEventsData(@Header("Authorization") String token,@Query("student_id") String student_id);

    @Headers({
            "Accept:application/json",
    })
    @GET("news")
    Call<CircularFragBean> getNewsData(@Header("Authorization") String token,@Query("student_id") String student_id);

    @Headers({
            "Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiMDM2NDk4MjYzYjE0MmZlNmM5OWY5MTk4MTJiYTIzZDQwYmYzNDBiMzdiNmU0YjdlN2Y4MzAwZWI1Mzg1ZDdmNDMyMjNhNjEyNDJlMjkzZjEiLCJpYXQiOjE2MTc4NzAyNjIsIm5iZiI6MTYxNzg3MDI2MiwiZXhwIjoxNjQ5NDA2MjYyLCJzdWIiOiIzIiwic2NvcGVzIjpbXX0.LmkwYhg-3ql3wqBiNH42ShnkYijgCo7zBIjRxblnrHTlfxdhhDk9B_5VUys2ot9CGgBWogxu1LiAFPS2UQfdQ6ieee3hluSPGS9wLLlwcUclBwymPkyW64iIPH9_F6OLQOq6qPhlcbDwOnJ2_vzH8zJqpss8y07iY40_GpRy1dEEPVn9afJroyxoPYSWv0Fl_-T2djkcv44YrNf_1R3D2BydOnvksKGuscgcOnQFdLrZnkNez1GWzb0xPC2MZU_7VGrUIW5co7_mYH-HAEo09iNAITH1nhZGjA2HGIG3h1HTcRCc3OJX3uc8hWpa7oGfWioPkKoA6MmIjAhyAmTxdi67Hyvc_rRS8BGlvzEh8W16DZRSYyUbzilhkrknIprEm4UPGywgP0D7pXwoYch5Fygz5Vxbp1LRp4wG71mWJ-WvR9Wo9lcGo75ZutT6sc7F0qJpQb2iTt5FtNZdvVmmHctP8OhyjTQ2gZsz5k5-K2O20pSwNK12zbDe5hxLJ2F50awra9jH0ZcaMvtR_NN1LstwUwsKlnhidSjRTHZXQZ6k7IW4H_bAkxHmySVZuHwVBJopextPoe_Cz21F8CrWFYi3c0-GiJGO-PNbYPjG8820r97epkh3aPzORyIW9zDyzibbazpvQFQJpeaY1egLmxC4fzrbL79PPp5eXYo4kfI",
            "Accept:application/json",
    })
    @GET("library/borrow-book?student_id=3")
    Call<LibraryBorrowBook> getLibraryBorrowBook();

    @Headers({
            "Accept:application/json",
    })
    @GET("class-test/today-class-test")
    Call<ClassTestTodayBean> getClassTestToday(@Header("Authorization") String token,@Query("student_id") String student_id);

    @Headers({
            "Accept:application/json",
    })
    @GET("class-test/past-class-test")
    Call<ClassTestTodayBean> getClassTestPast(@Header("Authorization") String token,@Query("student_id") String student_id);

    @Headers({
            "Accept:application/json",
    })
    @GET("transport")
    Call<TransportRouStoppingBean> getTransportRouteStoppingDetails(@Header("Authorization") String token,@Query("student_id") String student_id);


    @Headers({
            "Accept:application/json",
    })
    @GET("library/fine-list")
    Call<LibraryFineListBean> getLibraryFineList(@Header("Authorization") String token,@Query("student_id") String student_id);

    //----------------------------------------------
    //Post requests

    @FormUrlEncoded
    @POST("profile-father-mother-edit")
    Call<ProfileEditBean> setProfileFatherMotherEdit(
            @Field("father_name")String father_name,
            @Field("father_email")String father_email,
            @Field("father_office_address")String father_office_address,
            @Field("father_telephone_no")String father_telephone_no,
            @Field("father_phone_no")String father_phone_no,
            @Field("father_whats_app_no")String father_whats_app_no,
            @Field("father_religion_id")String father_religion_id,
            @Field("father_profession_id")String father_profession_id,
            @Field("father_photo")String father_photo,
            @Field("mother_name")String mother_name,
            @Field("mother_email")String mother_email,
            @Field("mother_office_address")String mother_office_address,
            @Field("mother_telephone_no")String mother_telephone_no,
            @Field("mother_phone_no")String mother_phone_no,
            @Field("mother_whats_app_no")String mother_whats_app_no,
            @Field("mother_religion_id")String mother_religion_id,
            @Field("mother_profession_id")String mother_profession_id,
            @Field("mother_photo")String mother_photo
    );

    @FormUrlEncoded
    @POST("profile-passport-edit")
    Call<ProfileEditBean> setProfilePassportEdit(
            @Field("passport_no")String passport_no,
            @Field("date_of_issue")String date_of_issue,
            @Field("date_of_expiry")String date_of_expiry,
            @Field("place_of_issue")String place_of_issue,
            @Field("civil_id_no")String civil_id_no,
            @Field("residence_permit_no")String residence_permit_no,
            @Field("date_of_res_permit_issue")String date_of_res_permit_issue,
            @Field("entered_country_date")String entered_country_date
    );


}
