package com.capstone.hanzo.bluebsystem


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_user_management.view.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.find
import org.jetbrains.anko.newTask
import org.jetbrains.anko.support.v4.intentFor


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class UserManagementFragment : Fragment() {

    private lateinit var profileNm: TextView
    private lateinit var profileEm: TextView
    private lateinit var profileBalT: TextView
    private lateinit var btnSetAlarm: Button
    private lateinit var btnLogout: Button
    private val user = FirebaseAuth.getInstance().currentUser
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_management, container, false)

        with(view){
            this@UserManagementFragment.profileNm = find(R.id.profileNm)
            this@UserManagementFragment.profileEm = find(R.id.profileEm)
            this@UserManagementFragment.profileBalT = find(R.id.profileBalT)
            this@UserManagementFragment.btnLogout = find(R.id.btnLogout)
            this@UserManagementFragment.btnSetAlarm = find(R.id.btnSetAlarm)
        }

        user?.let {
            profileNm.text = it.displayName
            profileEm.text = it.email
        }

        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(intentFor<LoginActivity>().clearTask().newTask().clearTop())
        }
        return view
    }

}
