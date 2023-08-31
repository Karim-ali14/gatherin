package com.orwa.gatherin.api

import com.orwa.gatherin.model.about.AboutAppResItem
import com.orwa.gatherin.model.auth.*
import com.orwa.gatherin.model.chat.ChatUsersListRes
import com.orwa.gatherin.model.chat.ReportReq
import com.orwa.gatherin.model.chat.TextAnswer
import com.orwa.gatherin.model.fcm.FirebaseUserTokenReq
import com.orwa.gatherin.model.group.*
import com.orwa.gatherin.model.group.SectMembersRes
import com.orwa.gatherin.model.group_results.DepartmentResultsRes
import com.orwa.gatherin.model.group_results.SendResultsReq
import com.orwa.gatherin.model.notify.NotificationsRes
import com.orwa.gatherin.model.notify.NotificationsResPaginated
import com.orwa.gatherin.model.notify.SendNotifyReq
import com.orwa.gatherin.model.other.UploadFileRes
import com.orwa.gatherin.model.pack.PackageRes
import com.orwa.gatherin.model.pack.SaveReceiptReq
import com.orwa.gatherin.model.profile.PacksRes
import com.orwa.gatherin.model.section.*
import com.orwa.gatherin.model.teacher_home.DepartmentsListRes
import com.orwa.gatherin.model.teacher_home.UserInfoRes
import com.skydoves.sandwich.ApiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface RetrofitService {
    //    @GET("search")
//    suspend fun search(
//        @Header("Authorization") token: String,
//        @Query("page") page: Int,
//        @Query("query") query: String
//    ): RecipeSearchResponse
//
//    @GET("get")
//    suspend fun get(
//        @Header("Authorization") token: String,
//        @Query("id") id: Int
//    ): RecipeDto
    @POST("auth/signin")
    suspend fun signIn(@Body signInReq: SignInReq): ApiResponse<SignInRes>

    @POST("auth/signup")
    suspend fun signUp(@Body signUpReq: SignUpReq): ApiResponse<SignUpRes>

    @POST("auth/send/email")
    suspend fun sendVerifyCode(@Body req: VerifyCodeReq): ApiResponse<VerifyCodeRes>

    @POST("auth/forget/password")
    suspend fun sendForgotPwdVerifyCode(@Body req: VerifyCodeReq): ApiResponse<VerifyCodeRes>


    @POST("department/create")
    suspend fun createNewSection(@Body req: CreateSectionReq): ApiResponse<CreateSectionRes>

    @POST("department/create")
    suspend fun createNewSection2(@Body req: CreateSectionReq2): ApiResponse<CreateSectionRes>

    @GET("department/get/all")
    suspend fun getDepartmentsList(): ApiResponse<DepartmentsListRes>

    @GET("group/get/department/{id}")
    suspend fun getGroupsList(@Path("id") departmentId: Int): ApiResponse<GroupListRes>

    //get all groups for sendQuestionsFragment
    @GET("group/get/all")
    suspend fun getAllGroups(): ApiResponse<ArrayList<AllGroupsResItem>>


    @GET("group/get/{id}")
    suspend fun getGroupDetails(@Path("id") groupId: Int): ApiResponse<GroupDetailsRes>

    @FormUrlEncoded
    @POST("department/delete")
    suspend fun deleteSection(@Field("id") id: Int): ApiResponse<ResponseBody>

    @FormUrlEncoded
    @POST("group/delete")
    suspend fun deleteGroup(@Field("id") id: Int): ApiResponse<ResponseBody>

    @POST("group/create")
    suspend fun createNewGroup(@Body req: CreateGroupReq): ApiResponse<CreateGroupRes>


    @POST("group/update")
    suspend fun updateGroup(@Body req: UpdateGroupReq): ApiResponse<ResponseBody>


//    @GET("department/{id}/users/get/")
//    suspend fun getDepartmentsMembers(@Path("id") departmentId: Int): ApiResponse<SectMembersRes>

//    @GET("department/{id}/users/get/")
//    suspend fun getDepartmentsMembers2(@Path("id") departmentId: Int): ApiResponse<SectMembersRes>

    @GET("department/search/users")
    suspend fun getDepartmentsMembers2(@Query("departmentId") departmentId: Int, @Query("page") page: Int, @Query("text") text: String): SectMembersPaginatedRes

    @FormUrlEncoded
    @POST("department/update")
    suspend fun updateDepartment(
        @Field("id") id: Int,
        @Field("name") name: String,
        @Field("code") code: String
    ): ApiResponse<ResponseBody>

    @GET("/auth/user")
    suspend fun getUserInfo(): ApiResponse<UserInfoRes>


    @FormUrlEncoded
    @POST("/group/change/leader")
    suspend fun setGroupLeader(
        @Field("id") groupId: Int,
        @Field("leader_id") leaderId: Int
    ): ApiResponse<ResponseBody>

    /**
     * Get repos ordered by stars.
     */
    @GET("notification/get")
    suspend fun getNotifications(): ApiResponse<NotificationsRes>

    /**
     * Get repos ordered by stars.
     */
    @GET("notification/get/pagination")
    suspend fun getPaginatedNotifications(@Query("page") page: Int): NotificationsResPaginated


    @FormUrlEncoded
    @POST("department/user/add")
    suspend fun addUserToDepartment(@Field("code") code: String): ApiResponse<ResponseBody>

    @Multipart
    @POST("auth/update")
    suspend fun updateProfile(
        @Part file: MultipartBody.Part,
        @Part("fullName") fullName: RequestBody,
        @Part("phone") phone: RequestBody
    ): ApiResponse<UpdateProfileRes>

    @FormUrlEncoded
    @POST("auth/update")
    suspend fun updateProfile(
        @Field("fullName") fullName: String,
        @Field("phone") phone: String
    ): ApiResponse<UpdateProfileRes>

    /**
     * Get app about.
     */
    @GET("about")
    suspend fun getAboutApp(): ApiResponse<AboutAppResItem>

    /**
     * Get app policy.
     */
    @GET("privacy")
    suspend fun getAppPolicy(): ApiResponse<AboutAppResItem>


    /**
     * Get packages.
     */
    @GET("package/get/all")
    suspend fun getPackages(): ApiResponse<PacksRes>


    @FormUrlEncoded
    @POST("auth/update/info/password")
    suspend fun updatePwd(
        @Field("old_password") oldPwd: String,
        @Field("new_password") phone: String,
        @Field("passwordConfirmation") confirmPwd: String
    ): ApiResponse<ResponseBody>

    @POST("answer/send")
    suspend fun sendResponseToMaster(@Body req: SendResponseToMasterReq): ApiResponse<ResponseBody>

//    @Multipart
//    @POST("uploadFile")
//    suspend fun uploadPhoto(@Part("file") fileName:String, @Part file: MultipartBody.Part):ApiResponse<UploadFileRes>

//    @Multipart
//    @POST("uploadFile")
//    suspend fun uploadPhoto( @Part("file") file: RequestBody):ApiResponse<UploadFileRes>

    @Multipart
    @POST("uploadFile")
    suspend fun uploadPhoto(@Part file: MultipartBody.Part): ApiResponse<UploadFileRes>

    @POST("question/send")
    suspend fun sendQuestion(@Body req: SendQuestionReq): ApiResponse<ResponseBody>

    @POST("notification/send")
    suspend fun sendNotifyReport(@Body req: SendNotifyReq): ApiResponse<ResponseBody>

    //Used by group leader to send a response to the master
    @POST("answer/send")
    suspend fun sendAnswer(@Body req: TextAnswer): ApiResponse<ResponseBody>


    @GET("answer/get/{id}")
    suspend fun getLeaderAnswer(@Path("id") id: Int): ApiResponse<LeaderAnswer>

    //used by teacher
    @POST("result/create")
    suspend fun sendResults(@Body req: SendResultsReq): ApiResponse<ResponseBody>


    @GET("result/get/last/department/{id}")
    suspend fun getDepartmentResults(@Path("id") id: Int): ApiResponse<DepartmentResultsRes>


    @GET("/user/list")
    suspend fun getChatUsersList(): ApiResponse<ChatUsersListRes>

    //used by teacher
    @POST("/auth/firebase/set/token")
    suspend fun setFirebaseUserToken(@Body req: FirebaseUserTokenReq): ApiResponse<ResponseBody>

    @POST("/notification/change")
    suspend fun changeNotificationsState(): ApiResponse<ResponseBody>

    @POST("auth/signout")
    suspend fun logOut(): ApiResponse<ResponseBody>

    @POST("auth/update/password")
    suspend fun resetPassword(@Body req:ResetPasswordReq): ApiResponse<ResponseBody>

    @POST("department/user/remove")
    suspend fun deleteSectionMember(@Body req:DeleteSectionMemberReq): ApiResponse<ResponseBody>

    @POST("package/buy")
    @FormUrlEncoded
    suspend fun buyPackage(@Field("packageId") packId: Int): ApiResponse<ResponseBody>



    @POST("pay")
    @FormUrlEncoded
    suspend fun pay(@Field("receipt") token: String): ApiResponse<ResponseBody>

    @POST("iap/save/receipt")
    suspend fun saveReceipt(@Body req:SaveReceiptReq): ApiResponse<ResponseBody>


    @GET("isvalid")
    suspend fun checkPackage(): ApiResponse<PackageRes>

    @POST("private/chat/changeStatus")
    @FormUrlEncoded
    suspend fun changeMessagesStatus(@Field("other_id") other_id: Int): ApiResponse<ResponseBody>


    @POST("chatComplaint/create")
    suspend fun sendReport(@Body req:ReportReq): ApiResponse<ResponseBody>

}

