package com.gb.studentdetails

class Post{
    var profileImageUri: String? = ""
    var username: String = ""
    var certificateName: String = ""
    var issuedBy: String = ""
    var postImageUri: String = ""
    var date: String = ""
    var prn: String = ""
    var dateLong: Long? = null

    constructor(
        profileImageUri: String?,
        username: String,
        certificateName: String,
        issuedBy: String,
        postImageUri: String,
        date: String,
        prn: String,
        dateLong: Long
    ) {
        this.profileImageUri = profileImageUri
        this.username = username
        this.certificateName = certificateName
        this.issuedBy = issuedBy
        this.postImageUri = postImageUri
        this.date = date
        this.prn = prn
        this.dateLong = dateLong
    }

    constructor()


}