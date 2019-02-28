package com.example.capstoneproject.ac.kmu.capstone.fragment


import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.capstoneproject.R
import com.example.capstoneproject.ac.kmu.capstone.activity.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import kr.co.bootpay.*
import kr.co.bootpay.enums.PG
import okhttp3.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.uiThread
import java.io.IOException


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class RechargeFragment : Fragment() {

    private lateinit var rechargeBtn: Button
    private lateinit var editRecharge: EditText
    private lateinit var textRecharge: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recharge, container, false)
        val ctrl = activity as HomeActivity

        BootpayAnalytics.init(ctrl, "5c612584396fa678c27587d1")

        with(view) {
            rechargeBtn = find(R.id.rechargeBtn)
            editRecharge = find(R.id.editRecharge)
            textRecharge = find(R.id.textRechagre)
        }

        editRecharge.apply {
            addTextChangedListener(EditRechargeListener())
        }

        rechargeBtn.setOnClickListener {
            val rechargeAlert = AlertDialog.Builder(ctrl)
            rechargeAlert.apply {
                setTitle("충전 알림")
                setMessage("충전 하시겠습니까?")
                setPositiveButton("충전", AlertBtnListener())
                setNegativeButton("취소", AlertBtnListener())
            }.show()
        }
        return view
    }

    inner class AlertBtnListener : DialogInterface.OnClickListener {
        override fun onClick(dialog: DialogInterface?, which: Int) {
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    Bootpay.init((activity as HomeActivity).fragmentManager)
                        .setApplicationId("5c612584396fa678c27587d1") // 해당 안드로이드 프로젝트의 어플리케이션 id 값
                        .setPG(PG.KAKAO) // 결제할 PG사
                        .setUserPhone(FirebaseAuth.getInstance().currentUser?.phoneNumber) // 구매자 전화번호
                        .setMethod("easy") // 결제 수단
                        .setName("지갑 충전") // 결제할 상품명
                        .setOrderId("1234") // 고유 주문번호로, 생성하신 값을 보내주셔야 합니다
                        .setPrice(editRecharge.text.toString().toInt()) // 결제할 금액
                        .isShowAgree(true)
                        .onConfirm(object : ConfirmListener {
                            override fun onConfirm(message: String?) {
                                /**
                                 * onConfrim의 구현부분은 반드시 있어야 된다고 전제하자
                                 * 이유는 잘 모르겠음
                                 * 없으니까 결제가 진행이 되지 않음
                                 */
                                if (true) Bootpay.confirm(message) // 재고가 있을 경우
                                else Bootpay.removePaymentWindow() // 재고가 없어 중간에 결제창을 닫고 싶을 경우
                                Log.d("confirm", message)
                            }
                        })
                        .onDone(object : DoneListener { // 결제완료시 호출, 아이템 지급 등 데이터 동기화 로직을 수행합니다
                            override fun onDone(message: String?) {
                                Log.d(":done", message)

                                val request = Request.Builder().url("http://13.125.170.17/CashRecharge.php").post(
                                    FormBody.Builder().apply {
                                        add("user_email", FirebaseAuth.getInstance().currentUser?.email)
                                        add("user_cash", editRecharge.text.toString())
                                    }.build()
                                ).build()

                                doAsync {
                                    OkHttpClient().newCall(request).enqueue(object : Callback {
                                        override fun onFailure(call: Call, e: IOException) {
                                        }

                                        override fun onResponse(call: Call, response: Response) {
                                            val result = response.body()?.string()?.toBoolean()

                                            uiThread {
                                                when (result) {
                                                    true -> {
                                                        toast("충전 완료")
                                                    }
                                                    false -> {
                                                        toast("충전 실패")
                                                    }
                                                }
                                            }

                                        }
                                    })
                                }
                            }
                        })
                        .onReady(object : ReadyListener { // 가상계좌 입금 계좌번호가 발급되면 호출되는 부분
                            override fun onReady(message: String?) {
                                Log.d("ready", message)
                            }
                        })
                        .onCancel(object : CancelListener { // 결제 취소시 호출되는 부분
                            override fun onCancel(message: String?) {
                                Log.d("cancle", message)
                            }
                        })
                        .onError(object : ErrorListener { // 에러가 났을때 호출되는 부분
                            override fun onError(message: String?) {
                                Log.d("error", message)
                            }
                        })
                        .onClose(object : CloseListener { // 결체창이 닫힐때 실행되는 부분
                            override fun onClose(message: String?) {
                                Log.d("close", "close")
                            }
                        })
                        .show()

                }
                DialogInterface.BUTTON_NEGATIVE -> {
                    toast("충전 취소")
                }
            }
        }
    }

    inner class EditRechargeListener : TextWatcher {

        private fun minRecharge(pay: Int, cal: (Int) -> Boolean) {
            when (cal(pay)) {
                true -> {
                    textRecharge.text = "충전 가능한 금액입니다"
                    textRecharge.setTextColor(Color.BLUE)
                }
                false -> {
                    textRecharge.text = "최소 충전 금액은 5000원입니다"
                    textRecharge.setTextColor(Color.RED)
                }
            }
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            s?.let {
                if (it.isBlank()) {
                    return
                }
                minRecharge(it.toString().toInt()) { it >= 5000 }
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            s?.let {
                if (it.isBlank()) {
                    return
                }
                minRecharge(it.toString().toInt()) { it >= 5000 }
            }
        }

        override fun afterTextChanged(s: Editable?) {
            s?.let {
                if (it.isBlank()) {
                    return
                }
                minRecharge(it.toString().toInt()) { it >= 5000 }
            }
        }
    }
}
