package com.example.xiapengdi.woodoandroid

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

import com.google.android.gms.tasks.Task
import com.google.android.gms.common.api.ApiException
import kotlinx.android.synthetic.main.splash_activity.*

const val SIGN_IN = 1

class SplashActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        sign_in_button.visibility = View.VISIBLE

        test.visibility = View.VISIBLE
        sign_in_button.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, SIGN_IN)

            val acct = GoogleSignIn.getLastSignedInAccount(this)
            if (acct != null) {
                sign_in_button.visibility = View.VISIBLE
                test.text = "last time"
                test.visibility = View.VISIBLE
            }
        }

    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            sign_in_button.visibility = View.VISIBLE
            test.text ="success"
            test.visibility = View.VISIBLE
            //can only choose to login to continue to the fragments page
            //after successful login, goes to the Home fragment to view posts
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        } catch (e: ApiException) {
            sign_in_button.visibility = View.VISIBLE
            test.text ="fail"
            test.visibility = View.VISIBLE

        }

    }
}
