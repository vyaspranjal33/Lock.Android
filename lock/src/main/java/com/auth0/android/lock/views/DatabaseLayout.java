/*
 * DbLayout.java
 *
 * Copyright (c) 2016 Auth0 (http://auth0.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.auth0.android.lock.views;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.auth0.android.lock.Configuration;
import com.auth0.android.lock.R;
import com.squareup.otto.Bus;

public class DatabaseLayout extends RelativeLayout implements View.OnClickListener {
    private Bus bus;
    private Configuration configuration;

    private Button signUpBtn;
    private Button changePasswordBtn;
    private Button goBackBtn;
    private FrameLayout formContainer;


    public DatabaseLayout(Context context) {
        super(context);
    }

    public DatabaseLayout(Context context, Bus lockBus, Configuration configuration) {
        super(context);
        this.bus = lockBus;
        this.configuration = configuration;
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.com_auth0_lock_database_layout, this);
        formContainer = (FrameLayout) findViewById(R.id.com_auth0_lock_form_layout);
        signUpBtn = (Button) findViewById(R.id.com_auth0_lock_sign_up_btn);
        signUpBtn.setOnClickListener(this);
        changePasswordBtn = (Button) findViewById(R.id.com_auth0_lock_change_password_btn);
        changePasswordBtn.setOnClickListener(this);
        goBackBtn = (Button) findViewById(R.id.com_auth0_lock_back_btn);
        goBackBtn.setOnClickListener(this);

        showLoginForm();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.com_auth0_lock_sign_up_btn) {
            showSignUpForm();
        } else if (id == R.id.com_auth0_lock_change_password_btn) {
            showChangePasswordForm();
        } else if (id == R.id.com_auth0_lock_back_btn) {
            showLoginForm();
        }
    }

    private void showSignUpForm() {
        removePreviousForm();

        SignUpFormView signUpForm = new SignUpFormView(getContext(), this.bus, this.configuration);
        formContainer.addView(signUpForm);

        signUpBtn.setVisibility(View.GONE);
        changePasswordBtn.setVisibility(View.GONE);
        goBackBtn.setVisibility(View.VISIBLE);
    }

    private void showChangePasswordForm() {
        removePreviousForm();

        ChangePasswordFormView changePwdForm = new ChangePasswordFormView(getContext(), this.bus, this.configuration);
        formContainer.addView(changePwdForm);

        signUpBtn.setVisibility(View.GONE);
        changePasswordBtn.setVisibility(View.GONE);
        goBackBtn.setVisibility(View.VISIBLE);
    }

    private void showLoginForm() {
        removePreviousForm();

        LoginFormView loginForm = new LoginFormView(getContext(), this.bus, this.configuration);
        formContainer.addView(loginForm);

        changePasswordBtn.setVisibility(configuration.isChangePasswordEnabled() ? View.VISIBLE : View.GONE);
        signUpBtn.setVisibility(configuration.isSignUpEnabled() ? View.VISIBLE : View.GONE);
        goBackBtn.setVisibility(View.GONE);
    }


    private void removePreviousForm() {
        View existingForm = formContainer.getChildAt(0);
        if (existingForm != null) {
            formContainer.removeView(existingForm);
        }
    }

    /**
     * Should be called to update the form layout.
     *
     * @return true if it was consumed, false otherwise.
     */
    public boolean onBackPressed() {
        if (goBackBtn.getVisibility() == VISIBLE) {
            showLoginForm();
            return true;
        }
        return false;
    }

}