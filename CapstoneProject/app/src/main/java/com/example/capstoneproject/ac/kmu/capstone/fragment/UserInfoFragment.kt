package com.example.capstoneproject.ac.kmu.capstone.fragment


import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.capstoneproject.R
import com.example.capstoneproject.ac.kmu.capstone.activity.HomeActivity
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.firebase.auth.FirebaseAuth
import okhttp3.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import org.jetbrains.anko.uiThread
import retrofit2.http.Body
import java.io.IOException


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class UserInfoFragment : Fragment() {

    private lateinit var userName: TextView
    private lateinit var userEmail: TextView
    private lateinit var userCash: TextView
    private lateinit var userBook: TextView
    private lateinit var userTrans: TextView
    private lateinit var userGetOffTime: TextView
    private lateinit var userIn: TextView
    private lateinit var userLastBusNo: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_info, container, false)
        val ctrl = activity as HomeActivity

        with(view) {
            userName = find(R.id.userName)
            userEmail = find(R.id.userEmail)
            userCash = find(R.id.userCash)
            userBook = find(R.id.userBook)
            userTrans = find(R.id.userTrans)
            userGetOffTime = find(R.id.userGetOffTime)
            userIn = find(R.id.userIn)
            userLastBusNo = find(R.id.userLastBusNo)
        }


        return view
    }

    override fun onResume() {
        super.onResume()

        doAsync {
            val request = Request.Builder().
                url("https://13.125.170.17/googleUserInfoSelect.php?user_email=${FirebaseAuth.getInstance().currentUser?.email}")
                .build()

            OkHttpClient().newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("userinfo","failed")

                }

                override fun onResponse(call: Call, response: Response) {
                    Log.d("userinfo","success")
                    val stream = response.body()?.byteStream()
                    val mapper = jacksonObjectMapper()
                    val result = mapper.readValue<ArrayList<UserInfo>>(stream!!)
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    uiThread {
                        userName.text = currentUser?.displayName
                        userEmail.text = currentUser?.email

                        result.forEach {
                            userCash.text = "${it.userCash}원"
                            userBook.text = it.userBook
                            userTrans.text = it.userTrans
                            userGetOffTime.text = it.userGetOffTime
                            userIn.text = it.userIn
                            userLastBusNo.text = it.userLastBusNo
                        }

                        userBook.apply {
                            isCustomEnabled {
                                when (it.toString().toBoolean()) {
                                    true -> {
                                        setBackgroundColor(Color.BLUE)
                                        text = "예약 중"
                                    }
                                    false -> {
                                        setBackgroundColor(Color.GRAY)
                                        text = "예약 없음"
                                    }
                                }
                            }
                        }

                        userTrans.apply {
                            isCustomEnabled {
                                when (it.toString().toBoolean()) {
                                    true -> {
                                        setBackgroundColor(Color.BLUE)
                                        text = "활성"
                                    }
                                    false -> {
                                        setBackgroundColor(Color.GRAY)
                                        text = "비활성"
                                    }
                                }
                            }
                        }

                        userIn.apply {
                            isCustomEnabled {
                                when (it.toString().toBoolean()) {
                                    true -> {
                                        setBackgroundColor(Color.BLUE)
                                        text = "탑승 중"
                                    }
                                    false -> {
                                        setBackgroundColor(Color.GRAY)
                                        text = "탑승 안함"
                                    }
                                }
                            }
                        }

                        userGetOffTime.apply {
                            isCustomEnabled {
                                it?.let {
                                    text = toString()
                                }
                            }
                        }

                        userLastBusNo.apply {
                            isCustomEnabled {
                                it?.let {
                                    text = toString()
                                }
                            }
                        }
                    }
                }
            })
        }
    }

    private fun TextView.isCustomEnabled(body: (strings: Any?) -> Unit) {
        when (this) {
            userBook -> {
                body(userBook.text)
            }
            userTrans -> {
                body(userTrans.text)
            }
            userIn -> {
                body(userIn.text)
            }
            userGetOffTime -> {
                body(userGetOffTime.text)
            }
            userLastBusNo -> {
                body(userLastBusNo.text)
            }
        }
    }
}


data class UserInfo(
    val userCash: String,
    val userBook: String,
    val userTrans: String,
    val userGetOffTime: String,
    val userIn: String,
    val userLastBusNo: String
)