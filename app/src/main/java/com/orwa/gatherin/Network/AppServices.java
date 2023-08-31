package com.orwa.gatherin.Network;

import com.orwa.gatherin.Models.DepartmentMembersModel.DepartmentMembersModel;
import com.orwa.gatherin.Models.GeneralResponse.GeneralResponse;
import com.orwa.gatherin.Models.GeneralResponse.GeneralResponse2;
import com.orwa.gatherin.Models.GroupAnswersModel.GroupsAnswersModel;
import com.orwa.gatherin.Models.GroupMembersModel.GroupStusentsModel;
import com.orwa.gatherin.Models.GroupsModel.GroupsModel;
import com.orwa.gatherin.Models.HomeTeacherModel.HomeTeacherModel;
import com.orwa.gatherin.Models.NotificationsModel.NotificationsModel;
import com.orwa.gatherin.Models.StudentsDepartmentsModel.StudentDepartmentsModel;
import com.orwa.gatherin.Models.SubscriptionPackagesModel.SubscriptionPlansModel;
import com.orwa.gatherin.Models.TeacherGroupsModel.TeacherGroupsModel;
import com.orwa.gatherin.Models.TeacherProfileModel.TeacherProfileModel;
import com.orwa.gatherin.Models.UserGroupsModel.UserGroupsModel;
import com.orwa.gatherin.Models.UserModelNew.UserModel;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AppServices {

    @FormUrlEncoded
    @POST(AppUrls.teacher_signUp)
    Observable<UserModel> teacherRegister(
            @Field("email") String email,
            @Field("password") String password,
            @Field("username") String username,
            @Field("subscription") String subscription
    );

    @FormUrlEncoded
    @POST(AppUrls.studentSignUp)
    Observable<UserModel> studentSignUp(
            @Field("email") String email,
            @Field("password") String password,
            @Field("username") String username
    );

    @FormUrlEncoded
    @POST(AppUrls.studentSignIn)
    Observable<UserModel> studentSignIn(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST(AppUrls.send_question)
    Observable<GeneralResponse> send_question(
            @Field("groups") String groups,
            @Field("year") String year,
            @Field("month") String month,
            @Field("day") String day,
            @Field("hour") String hour,
            @Field("minute") String minute,
            @Field("sender") String sender,
            @Field("questionBody") String questionBody,
            @Field("type") String type,
            @Field("choices") String choices,
            @Field("access_token") String access_token

    );

    @FormUrlEncoded
    @POST(AppUrls.send_question)
    Observable<GeneralResponse> send_questionEssay(
            @Field("groups") String groups,
            @Field("year") String year,
            @Field("month") String month,
            @Field("day") String day,
            @Field("hour") String hour,
            @Field("minute") String minute,
            @Field("sender") String sender,
            @Field("questionBody") String questionBody,
            @Field("type") String type,
            @Field("access_token") String access_token

    );

    @FormUrlEncoded
    @POST(AppUrls.sendNotificationToGroups)
    Observable<GeneralResponse> sendNotificationToGroups(
            @Field("groups") String groups,
            @Field("year") String year,
            @Field("month") String month,
            @Field("day") String day,
            @Field("hour") String hour,
            @Field("minute") String minute,
            @Field("text") String text,
            @Field("access_token") String access_token

    );

    @FormUrlEncoded
    @POST(AppUrls.teacher_signIn)
    Observable<UserModel> teacher_signin(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST(AppUrls.get_plans)
    Observable<SubscriptionPlansModel> subscriptionPackages(
            @Field("access_token") String access_token
    );

    @FormUrlEncoded
    @POST(AppUrls.update_teacher_plan)
    Observable<GeneralResponse> updateTeacherPlan(
            @Field("teacher_id") String teacher_id,
            @Field("plan_id") String plan_id,
            @Field("access_token") String access_token

    );

    @FormUrlEncoded
    @POST(AppUrls.add_department_to_teacher)
    Observable<GeneralResponse> addDepartment(
            @Field("teacher_id") String teacher_id,
            @Field("name") String name,
            @Field("code") String code,
            @Field("access_token") String access_token

    );

    @FormUrlEncoded
    @POST(AppUrls.update_department)
    Observable<GeneralResponse> updateDepartment(
            @Field("department_id") String teacher_id,
            @Field("name") String name,
            @Field("code") String code,
            @Field("access_token") String access_token

    );

    @FormUrlEncoded
    @POST(AppUrls.get_teacher)
    Observable<TeacherProfileModel> teacherProfile(
            @Field("teacher_id") String teacher_id,
            @Field("access_token") String access_token

    );

    @FormUrlEncoded
    @POST(AppUrls.update_teacher)
    Observable<GeneralResponse> updateTeacherProfile(
            @Field("teacher_id") String teacher_id,
            @Field("username") String username,
            @Field("email") String email,
            @Field("access_token") String access_token

    );

    @FormUrlEncoded
    @POST(AppUrls.update_teacher)
    Observable<GeneralResponse> updateTeacherPassword(
            @Field("teacher_id") String teacher_id,
            @Field("password") String password,
            @Field("access_token") String access_token

    );

    @FormUrlEncoded
    @POST(AppUrls.delete_department_from_teacher)
    Observable<GeneralResponse> delete_department_from_teacher(
            @Field("teacher_id") String teacher_id,
            @Field("department_id") String department_id,
            @Field("access_token") String access_token
    );

    @FormUrlEncoded
    @POST(AppUrls.get_teacher)
    Observable<HomeTeacherModel> teacherHome(
            @Field("teacher_id") String teacher_id,
            @Field("access_token") String access_token
    );

    @FormUrlEncoded
    @POST(AppUrls.get_department)
    Observable<HomeTeacherModel> getDepartmentGroup(
            @Field("department_id") String department_id,
            @Field("access_token") String access_token

    );

    @FormUrlEncoded
    @POST(AppUrls.getStudent)
    Observable<TeacherProfileModel> getStudent(
            @Field("student_id") String student_id,
            @Field("access_token") String access_token
    );

    @FormUrlEncoded
    @POST(AppUrls.getStudentDepartments)
    Observable<StudentDepartmentsModel> getStudentDepartments(
            @Field("student_id") String student_id,
            @Field("access_token") String access_token
    );

    @FormUrlEncoded
    @POST(AppUrls.getStudentGroups)
    Observable<UserGroupsModel> getStudentGroups(
            @Field("student_id") String student_id,
            @Field("access_token") String access_token
    );

    @FormUrlEncoded
    @POST(AppUrls.getGroupMembers)
    Observable<GroupStusentsModel> getGroupMembers(
            @Field("group_id") String student_id,
            @Field("access_token") String access_token
    );

    @FormUrlEncoded
    @POST(AppUrls.enrollStudentInDepartment)
    Observable<GeneralResponse> enrollStudentInDepartment(
            @Field("student_id") String student_id,
            @Field("access_token") String access_token,
            @Field("code") String code
    );

    @FormUrlEncoded
    @POST(AppUrls.updateStudent)
    Observable<GeneralResponse> updateStudent(
            @Field("student_id") String student_id,
            @Field("access_token") String access_token,
            @Field("username") String username,
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST(AppUrls.getStudentNotification)
    Observable<NotificationsModel> getStudentNotification(
            @Field("student_id") String student_id,
            @Field("access_token") String access_token
    );

    @FormUrlEncoded
    @POST(AppUrls.getTeacherNotification)
    Observable<NotificationsModel> getTeacherNotification(
            @Field("teacher_id") String teacher_id,
            @Field("access_token") String access_token
    );

    @FormUrlEncoded
    @POST(AppUrls.getTeacherGroups)
    Observable<GroupsModel> getTeacherGroups(
            @Field("teacher_id") String teacher_id,
            @Field("access_token") String access_token
    );

    @FormUrlEncoded
    @POST(AppUrls.sendQuestion)
    Observable<GeneralResponse> sendQuestionMCQ(
            @Field("groups") String groups,
            @Field("year") String year,
            @Field("month") String month,
            @Field("day") String day,
            @Field("hour") String hour,
            @Field("minute") String minute,
            @Field("sender") String sender,
            @Field("questionBody") String questionBody,
            @Field("type") String type,
            @Field("choices") String choices,
            @Field("access_token") String access_token
    );

    @FormUrlEncoded
    @POST(AppUrls.sendQuestion)
    Observable<GeneralResponse> sendQuestionESSAY(
            @Field("groups") String groups,
            @Field("year") String year,
            @Field("month") String month,
            @Field("day") String day,
            @Field("hour") String hour,
            @Field("minute") String minute,
            @Field("sender") String sender,
            @Field("questionBody") String questionBody,
            @Field("type") String type,
            @Field("access_token") String access_token
    );

    @FormUrlEncoded
    @POST(AppUrls.getSectionDetails)
    Observable<TeacherGroupsModel> getSectionDetails(
            @Field("department_id") String department_id,
            @Field("access_token") String access_token
    );

    @FormUrlEncoded
    @POST(AppUrls.deleteGroup)
    Observable<GeneralResponse> deleteGroup(
            @Field("group_id") String group_id,
            @Field("access_token") String access_token
    );

    @FormUrlEncoded
    @POST(AppUrls.set_leader)
    Observable<GeneralResponse> setLeader(
            @Field("group_id") String group_id,
            @Field("leader_id") String leader_id,
            @Field("access_token") String access_token
    );

    @FormUrlEncoded
    @POST(AppUrls.sendVerificationCode)
    Observable<GeneralResponse2> sendVerificationCode(
            @Field("email") String email,
            @Field("access_token") String access_token
    );

    @FormUrlEncoded
    @POST(AppUrls.addGroup)
    Observable<GeneralResponse> addGroup(
            @Field("department_id") String department_id,
            @Field("name") String name,
            @Field("access_token") String access_token,
            @Field("members") String members

    );

    @FormUrlEncoded
    @POST(AppUrls.update_group)
    Observable<GeneralResponse> updateGroup(
            @Field("group_id") String group_id,
            @Field("name") String name,
            @Field("access_token") String access_token,
            @Field("members") String members
    );

    @FormUrlEncoded
    @POST(AppUrls.check_adding_group)
    Observable<GeneralResponse> check_adding_group(
            @Field("department_id") String department_id,
            @Field("access_token") String access_token
    );

    @FormUrlEncoded
    @POST(AppUrls.get_department_members)
    Observable<DepartmentMembersModel> get_department_members(
            @Field("department_id") String department_id,
            @Field("access_token") String access_token
    );

    @FormUrlEncoded
    @POST(AppUrls.get_groups_answers)
    Observable<GroupsAnswersModel> getـgroupsـanswers(
            @Field("teacher_id") String teacher_id,
            @Field("access_token") String access_token
    );

}
