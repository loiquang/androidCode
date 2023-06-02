package com.example.myapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private SignInClient oneTapClient;
    private BeginSignInRequest signUpRequest;

    CallbackManager callbackManager;

    Button btnGoogle;
    LoginButton btnFacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGoogle = findViewById(R.id.btnGoogle);
        btnFacebook = findViewById(R.id.btnFacebook);

        oneTapClient = Identity.getSignInClient(this);
        signUpRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest
                        .GoogleIdTokenRequestOptions
                        .builder()
                        .setSupported(true)
                        .setServerClientId(getString(R.string.default_web_client_id))
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();


        ActivityResultLauncher<IntentSenderRequest> starActivityForResult = registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                int x = 2;

                if (result.getResultCode() == Activity.RESULT_OK) {
                    try {
                        SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(result.getData());
                        String idToken = credential.getGoogleIdToken();
                        if (idToken != null) {
                            // Thông tin email
                            String email = credential.getId();
                            // Password
                            String name = credential.getDisplayName();
                            // Chúng ta có thể lấy ảnh của người dùng, và dùng thư viện glide để show ảnh lên UI
                            Uri avatar = credential.getProfilePictureUri();
                            Toast.makeText(MainActivity.this, "email: " + email + "\n" + "name: " + name, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e("TAG", "onFailure", e);
                    }
                }
            }
        });

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oneTapClient.beginSignIn(signUpRequest)
                        .addOnSuccessListener(MainActivity.this, new OnSuccessListener<BeginSignInResult>() {
                            @Override
                            public void onSuccess(BeginSignInResult beginSignInResult) {
                                try {
                                    IntentSenderRequest intentSenderRequest =
                                            new IntentSenderRequest.Builder(beginSignInResult.getPendingIntent().getIntentSender()).build();
                                    // Sử dụng ActivityResultLauncher ta vừa khởi tạo đến thực hiện đăng nhập
                                    starActivityForResult.launch(intentSenderRequest);
                                } catch (Exception e) {
                                    Log.e("TAG", "Couldn't start One tap UI: " + e.getLocalizedMessage());
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("TAG", "onFailure: " + e);
                            }
                        });
            }
        });

        callbackManager = CallbackManager.Factory.create();

        // Check người dùng đã đăng nhập trước đó hay chưa?
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if(isLoggedIn){
            getUserProfile(accessToken);
            Toast.makeText(this, "Đã đăng nhập", Toast.LENGTH_SHORT).show();
        }

        // Nút đăng nhập/Logout
        btnFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getUserProfile(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "Hủy đăng nhập", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(MainActivity.this, "Lỗi trong quá trình đăng nhập", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void getUserProfile(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String name = object.getString("name");
                    String email = object.getString("id");
                    String image = object.getJSONObject("picture").getJSONObject("data").getString("url");
                    Toast.makeText(MainActivity.this, name + " - " + email + " - " + image, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture.width(200)");
        request.setParameters(parameters);
        request.executeAsync();

    }
}