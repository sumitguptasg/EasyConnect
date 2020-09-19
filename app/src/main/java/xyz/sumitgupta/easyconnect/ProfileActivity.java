package xyz.sumitgupta.easyconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    EditText linkedin_url;
    EditText github_url;
    EditText instagram_url;
    EditText facebook_url;
    EditText web_url;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseAuth mauth;
    FirebaseUser muser;
    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mauth=FirebaseAuth.getInstance();
        muser=mauth.getCurrentUser();
//        if (muser == null)
//            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
        ImageView user_img=findViewById(R.id.user_img);
        TextView display_name=findViewById(R.id.displayname);
        linkedin_url=findViewById(R.id.linkedin_text);
        github_url=findViewById(R.id.github_text);
        instagram_url=findViewById(R.id.instagram_text);
        facebook_url=findViewById(R.id.facebook_text);
        web_url=findViewById(R.id.web_text);
        Button linkedin_button=findViewById(R.id.linkedin_button);
        Button github_button=findViewById(R.id.github_button);
        Button instagram_button=findViewById(R.id.instagram_button);
        Button facebook_button=findViewById(R.id.facebook_button);
        Button web_button=findViewById(R.id.web_button);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Picasso.get().load(muser.getPhotoUrl().toString()).into(user_img);
        display_name.setText(muser.getDisplayName());

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.custom_layout,null);
        TextView mTitleTextView = mCustomView.findViewById(R.id.title_text);
        mTitleTextView.setText(R.string.app_name);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        ImageButton imageButton = mCustomView.findViewById(R.id.imageButton);
        imageButton.setImageResource(R.drawable.logout);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mauth.signOut();
            }
        });
        mauth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null) {
                    mGoogleSignInClient.signOut();
                    startActivity(new Intent(ProfileActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    ProfileActivity.this.finish();

                }
            }
        });

        DocumentReference docRef = db.collection("users").document(muser.getEmail());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Data fetch sucess", "DocumentSnapshot data: " + document.getData());
                        if(document.contains("linkedin"))
                        {
                            linkedin_url.setText(document.getString("linkedin"));
                        }
                        if(document.contains("github"))
                        {
                            github_url.setText(document.getString("github"));
                        }
                        if(document.contains("instagram"))
                        {
                            instagram_url.setText(document.getString("instagram"));
                        }
                        if(document.contains("facebook"))
                        {
                            facebook_url.setText(document.getString("facebook"));
                        }
                        if(document.contains("web"))
                        {
                            web_url.setText(document.getString("web"));
                        }


                    } else {
                        Log.d("Document fetch Fail", "No such document");
                    }
                } else {
                    Log.d("Task Failed", "get failed with ", task.getException());
                }
            }
        });

    }
    public void loadUrl(View view){
        Intent i = new Intent(Intent.ACTION_VIEW);
        switch(view.getId())
        {
            case R.id.linkedin_button: {
                i.setData(Uri.parse(linkedin_url.getText().toString()));
                try {
                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this,"It is not a link",Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.github_button:{
                i.setData(Uri.parse(github_url.getText().toString()));
                try {
                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this,"It is not a link",Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.instagram_button:{
                i.setData(Uri.parse(instagram_url.getText().toString()));
                try {
                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this,"It is not a link",Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.facebook_button:{
                i.setData(Uri.parse(facebook_url.getText().toString()));
                try {
                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this,"It is not a link",Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.web_button:{
                i.setData(Uri.parse(web_url.getText().toString()));
                try {
                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this,"It is not a link",Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    public void updateFields(View view){
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("linkedin", linkedin_url.getText().toString());
        user.put("github", github_url.getText().toString());
        user.put("instagram", instagram_url.getText().toString());
        user.put("facebook", facebook_url.getText().toString());
        user.put("web", web_url.getText().toString());
        user.put("display_name",muser.getDisplayName());
        user.put("photo_url",muser.getPhotoUrl().toString());

        // Add a new document with a generated ID
        db.collection("users").document(muser.getEmail())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Doc-Reference Success :", "DocumentSnapshot added with ID: " + "Successfully Written to DATABASE");
                        Toast.makeText(ProfileActivity.this, "Update Successful! :)", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Doc-Reference Failure :", "Error adding document to DATABASE", e);
                        Toast.makeText(ProfileActivity.this, "Update Not Successful :(", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
