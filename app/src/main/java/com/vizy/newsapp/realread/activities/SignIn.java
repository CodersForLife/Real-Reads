package com.vizy.newsapp.realread.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.vizy.newsapp.realread.R;
import com.vizy.newsapp.realread.model.UserSession;

import java.util.Arrays;
import java.util.List;


public class SignIn extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    Button signIn, noAccount;
    EditText mobileNo, password;
    GoogleApiClient mGoogleApiClient;
    ProgressDialog mProgressDialog;
    private static final int RC_SIGN_IN = 9001;
    String TAG = "MainActivity-Google+SignIn";
    GoogleSignInAccount acct;
    LoginButton loginButton;
    CallbackManager callbackManager;
    RelativeLayout numberConfirmation, googleSignin, fbSignin;
    public static int APP_REQUEST_CODE = 99;
    TextView signinText2, signinText, fbText, googleText, mobilenoText;

    @Override
    protected void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_2);

        Typeface roboto = Typeface.createFromAsset(getAssets(), "fonts/Raleway-ExtraLight.ttf");
        signinText2=(TextView)findViewById(R.id.signin_text2);
        signinText=(TextView)findViewById(R.id.signin_text);
        googleText=(TextView)findViewById(R.id.google_text);
        fbText=(TextView)findViewById(R.id.fb_text);
        mobilenoText=(TextView)findViewById(R.id.mobile_no_text);
        signinText2.setTypeface(roboto);
        signinText.setTypeface(roboto);
        fbText.setTypeface(roboto);
        googleText.setTypeface(roboto);
        mobilenoText.setTypeface(roboto);
        googleSignin= (RelativeLayout) findViewById(R.id.google_signin);
        googleSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        final UserSession session = new UserSession(getApplicationContext());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso)
               // .requestScopes(new Scope(Scopes.PROFILE))
               // .requestScopes(new Scope(Scopes.PLUS_ME))
                .build();

        numberConfirmation= (RelativeLayout) findViewById(R.id.number_confirmation);
        AccountKit.initialize(getApplicationContext());
        FacebookSdk.sdkInitialize(getApplicationContext());
        numberConfirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoginPhone(numberConfirmation);
            }
        });

        final List<String> permissionNeeds = Arrays.asList("user_friends","user_photos","email");
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager= CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //AccessToken accessToken = loginResult.getAccessToken();
                AccessToken accessToken = AccountKit.getCurrentAccessToken();
                Profile profile = Profile.getCurrentProfile();
                if(profile!=null){
                    //support local session in app when login through FB
                    UserSession session = new UserSession(SignIn.this);
                    session.numberLoginSession(profile.getName(),profile.getLinkUri().toString());
                    Intent i =new Intent(SignIn.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
            @Override
            public void onCancel() {
            }
            @Override
            public void onError(FacebookException error) {
            }
        });

        fbSignin =(RelativeLayout) findViewById(R.id.facebookSignInBtn);
        fbSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(SignIn.this,permissionNeeds);
            }
        });

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            String toastMessage;
            if (loginResult.getError() != null) {
                toastMessage = loginResult.getError().getErrorType().getMessage();
                // showErrorActivity(loginResult.getError());
            } else if (loginResult.wasCancelled()) {
                toastMessage = "Login Cancelled";
            } else {
                if (loginResult.getAccessToken() != null) {
                    toastMessage = "Success:" + loginResult.getAccessToken().getAccountId();
                } else {
                    toastMessage = String.format(
                            "Success:%s...",
                            loginResult.getAuthorizationCode().substring(0,10));
                }
                Log.e(TAG,loginResult.getAuthorizationCode().substring(0,10));

               /* AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                    @Override
                    public void onSuccess(Account account) {
                        String accountKitId = account.getId();

                        // Get phone number
                        PhoneNumber phoneNumber = account.getPhoneNumber();
                        String phoneNumberString = phoneNumber.toString();

                        // Get email
                        String email = account.getEmail();
                        Log.e("fb det on number login",phoneNumberString+email+"Hey");
                    }

                    @Override
                    public void onError(AccountKitError accountKitError) {
                        Log.e("Error",accountKitError.toString());
                    }
                });*/
               // String number=loginResult();
                UserSession session = new UserSession(getApplicationContext());
                session.numberLoginSession("8587072927","piyush6348@gmail.com");

                startActivity(new Intent(SignIn.this,MainActivity.class));
                finish();
            }
            Toast.makeText(
                    this,
                    toastMessage,
                    Toast.LENGTH_LONG)
                    .show();
        }

    }

    private void handleSignInResult(GoogleSignInResult result) {

        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            Log.i("signin result Google",result.toString());
            acct = result.getSignInAccount();
            // mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    private void updateUI(boolean b) {
        if (b) {
            Log.e("Sucessfull", "Login");
            Log.i("google login result",acct.toString());
            String nam = acct.getDisplayName();
            String em = acct.getEmail();
//            String google_id=acct.getPhotoUrl().toString();
//            String userUrl=acct.getPhotoUrl().toString();
            String google_id=acct.getFamilyName();
            Log.e("googleId",google_id);

            UserSession session = new UserSession(this);
            session.googleLoginSession(nam, em,google_id);

            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            Log.e(TAG, " Google SignIn error");
        }
    }


    //@Override
    /*public void onClick(View view) {

        switch (view.getId()) {
            case R.id.google_signin:
                signIn();
                break;
        }
    }*/

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    public void onLoginPhone(final View view) {
        final Intent intent = new Intent(SignIn.this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.CODE); // or .ResponseType.TOKEN
        // ... perform additional configuration ...
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);
       /* UserSession session = new UserSession(SignIn.this);
        session.numberLoginSession();*/
    }
}
