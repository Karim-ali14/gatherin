package com.orwa.gatherin.rep

import com.orwa.gatherin.api.RetrofitService
import com.orwa.gatherin.di.NetworkModule
import com.orwa.gatherin.model.chat.ChatUsersListRes
import com.orwa.gatherin.model.chat.ReportReq
import com.orwa.gatherin.model.chat.TextAnswer
import com.orwa.gatherin.model.group.*
import com.orwa.gatherin.model.group.SectMembersRes
import com.orwa.gatherin.model.group_results.DepartmentResultsRes
import com.orwa.gatherin.model.group_results.SendResultsReq
import com.orwa.gatherin.model.notify.NotificationsRes
import com.orwa.gatherin.model.notify.SendNotifyReq
import com.orwa.gatherin.model.other.UploadFileRes
import com.orwa.gatherin.model.pack.PackageRes
import com.orwa.gatherin.model.pack.SaveReceiptReq
import com.orwa.gatherin.model.section.*
import com.orwa.gatherin.model.teacher_home.DepartmentsListRes
import com.orwa.gatherin.model.teacher_home.UserInfoRes

import com.skydoves.sandwich.ApiResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import javax.inject.Inject

class HomeRep @Inject constructor(
    @NetworkModule.Authenticated private val service: RetrofitService
) {

    suspend fun createSection(req: CreateSectionReq): ApiResponse<CreateSectionRes> {
        val res = service.createNewSection(req)
        return res
    }

    suspend fun createSection2(req: CreateSectionReq2): ApiResponse<CreateSectionRes> {
        val res = service.createNewSection2(req)
        return res
    }

    suspend fun getDepartmentsList(): ApiResponse<DepartmentsListRes> {
        val res = service.getDepartmentsList()
        return res
    }

    suspend fun getGroupsList(departmentId: Int): ApiResponse<GroupListRes> {
        val res = service.getGroupsList(departmentId)
        return res
    }

    suspend fun getAllGroups(): ApiResponse<ArrayList<AllGroupsResItem>> {
        val res = service.getAllGroups()
        return res
    }

    suspend fun deleteDepartment(id: Int): ApiResponse<ResponseBody> {
        val res = service.deleteSection(id)
        return res
    }

//    suspend fun getDepartmentMembers(departmentId: Int): ApiResponse<SectMembersRes> {
//        val res = service.getDepartmentsMembers(departmentId)
//        return res
//    }

//    suspend fun getDepartmentMembers2(departmentId: Int): ApiResponse<SectMembersRes> {
//        val res = service.getDepartmentsMembers2(departmentId)
//        return res
//    }

//    suspend fun getDepartmentMembers2(departmentId: Int): SectMembersPaginatedRes {
//        val res = service.getDepartmentsMembers2(departmentId)
//        return res
//    }

    suspend fun createGroup(req: CreateGroupReq): ApiResponse<CreateGroupRes> {
        val res = service.createNewGroup(req)
        return res
    }

    suspend fun updateGroup(req: UpdateGroupReq): ApiResponse<ResponseBody> {
        val res = service.updateGroup(req)
        return res
    }

    suspend fun deleteGroup(id: Int): ApiResponse<ResponseBody> {
        val res = service.deleteGroup(id)
        return res
    }

    suspend fun updateDepartment(id: Int, name: String, code: String): ApiResponse<ResponseBody> {
        val res = service.updateDepartment(id, name, code)
        return res
    }

    suspend fun getUserInfo(): ApiResponse<UserInfoRes> {
        val res = service.getUserInfo()
        return res
    }

    suspend fun getGroupDetails(groupId:Int): ApiResponse<GroupDetailsRes> {
        val res = service.getGroupDetails(groupId)
        return res
    }

    suspend fun setGroupLeader(groupId:Int, leaderId:Int): ApiResponse<ResponseBody> {
        val res = service.setGroupLeader(groupId, leaderId)
        return res
    }

    suspend fun addUserToDepartment(code:String): ApiResponse<ResponseBody> {
        val res = service.addUserToDepartment(code)
        return res
    }

    suspend fun sendResponseToMaster(req:SendResponseToMasterReq): ApiResponse<ResponseBody> {
        val res = service.sendResponseToMaster(req)
        return res
    }

    suspend fun uploadFile(file: MultipartBody.Part):ApiResponse<UploadFileRes>{
        val res = service.uploadPhoto(file)
        return res
    }

//    suspend fun uploadFile(file: RequestBody):ApiResponse<UploadFileRes>{
//        val res = service.uploadPhoto(file)
//        return res
//    }

    suspend fun sendQuestion(req: SendQuestionReq):ApiResponse<ResponseBody>{
        val res = service.sendQuestion(req)
        return res
    }

    suspend fun sendNotify(req: SendNotifyReq):ApiResponse<ResponseBody>{
        val res = service.sendNotifyReport(req)
        return res
    }

    suspend fun sendAnswer(req: TextAnswer):ApiResponse<ResponseBody>{
        val res = service.sendAnswer(req)
        return res
    }

    suspend fun getLeaderAnswer(groupId:Int):ApiResponse<LeaderAnswer>{
        val res = service.getLeaderAnswer(groupId)
        return res
    }

    suspend fun sendResults(req:SendResultsReq):ApiResponse<ResponseBody>{
        val res = service.sendResults(req)
        return res
    }

    suspend fun getDepartmentResults(deptId:Int):ApiResponse<DepartmentResultsRes>{
        val res = service.getDepartmentResults(deptId)
        return res
    }

    suspend fun getChatUsersList():ApiResponse<ChatUsersListRes>{
        val res = service.getChatUsersList()
        return res
    }

    suspend fun getNotifications():ApiResponse<NotificationsRes>{
        return service.getNotifications()
    }

    suspend fun logOut():ApiResponse<ResponseBody>{
        return service.logOut()
    }

    suspend fun deleteSectionMember(req:DeleteSectionMemberReq):ApiResponse<ResponseBody>{
        return service.deleteSectionMember(req)
    }

    suspend fun checkPackage():ApiResponse<PackageRes>{
        return service.checkPackage()
    }


    suspend fun changeMessagesStatus(otherId:Int):ApiResponse<ResponseBody>{
        return service.changeMessagesStatus(otherId)
    }

    suspend fun reportPersonGroup(req:ReportReq):ApiResponse<ResponseBody>{
        return service.sendReport(req)
    }


//    suspend fun setFirebaseUserToken(req:FirebaseUserTokenReq):ApiResponse<ResponseBody>{
//        val res = service.setFirebaseUserToken(req)
//        return res
//    }
}