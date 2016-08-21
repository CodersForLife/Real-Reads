package com.vizy.newsapp.realread.activities;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.facebook.FacebookSdk;
import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.vizy.newsapp.realread.R;
import com.vizy.newsapp.realread.model.UserSession;


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
    RelativeLayout numberConfirmation, googleSignin;
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

        /*signIn = (Button) findViewById(R.id.sign_in);
        noAccount = (Button) findViewById(R.id.no_account);
        mobileNo = (EditText) findViewById(R.id.enter_mobile_no_login);
        password = (EditText) findViewById(R.id.enter_password_login);*/

//        noAccount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent toSignUp = new Intent(SignIn.this, SignUp.class);
//                startActivity(toSignUp);
//                finish();
//            }
//        });

//        findViewById(R.id.sign_in_button).setOnClickListener(this);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        /*SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());*/



        numberConfirmation= (RelativeLayout) findViewById(R.id.number_confirmation);
        AccountKit.initialize(getApplicationContext());
        FacebookSdk.sdkInitialize(getApplicationContext());
        numberConfirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoginPhone(numberConfirmation);
            }
        });

        callbackManager = CallbackManager.Factory.create();
        /*loginButton = (LoginButton) findViewById(R.id.fb_login);
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email"));*/
        // If using in a fragment
        //  loginButton.setActivi(this);
        // Other app specific specialization
        AccessToken accessToken = AccountKit.getCurrentAccessToken();

        if (accessToken != null) {
            //Handle Returning User
        } else {
            //Handle new or logged out user
        }
        // Callback registration
        /*loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code

                GraphRequest graphRequest=GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.e(TAG,response.toString());
                        try {
                            String id=response.getJSONObject().getString("id");
                            String email=response.getJSONObject().getString("email");
                            String fbname=response.getJSONObject().getString("first_name");

                            UserSession session = new UserSession(SignIn.this);
                            session.facebookLoginSession(fbname, email,id);
                            Log.e("fb login Result",email+" "+fbname);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,email,first_name,last_name");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();

                startActivity(new Intent(SignIn.this,MainActivity.class));
                finish();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });*/

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
                UserSession session = new UserSession(getApplicationContext());


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

            String nam = acct.getDisplayName();
            String em = acct.getEmail();
            String google_id=acct.getPhotoUrl().getAuthority().toString();
//            String userUrl=acct.getPhotoUrl().toString();

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
        UserSession session = new UserSession(SignIn.this);
        session.numberLoginSession();
    }
}
