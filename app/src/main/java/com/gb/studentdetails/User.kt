package com.gb.studentdetails

import java.io.Serializable

data class User(val name: String, val email: String, val password: String, val uniqueId: String): Serializable{

}
