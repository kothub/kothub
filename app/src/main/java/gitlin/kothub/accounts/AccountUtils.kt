package gitlin.kothub.accounts

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context


const val USER_PICTURE = "USER_PICTURE"
const val USER_EMAIL = "USER_EMAIL"
const val USER_LOGIN = "USER_LOGIN"
const val USER_NAME = "USER_NAME"


fun getAccount (context: Context): Account? {
    val am = AccountManager.get(context)
    if (am.accounts.size == 0) {
        return null
    }
    else {
        return am.accounts[0]
    }
}

fun getUserData (context: Context, key: String): String? {
    val account = getAccount(context)

    if (account == null) {
        return ""
    }
    else {
        return AccountManager.get(context).getUserData(account, key)
    }
}

fun getUserLogin (context: Context): String? = getUserData(context, USER_LOGIN)
fun getUserEmail (context: Context): String? = getUserData(context, USER_EMAIL)
fun getUserPicture (context: Context): String? = getUserData(context, USER_PICTURE)
fun getUserName (context: Context): String? = getUserData(context, USER_NAME)