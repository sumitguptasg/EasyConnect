package xyz.sumitgupta.easyconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class ViewProfile extends AppCompatActivity {

    TextView linkedin_url;
    TextView github_url;
    TextView instagram_url;
    TextView facebook_url;
    TextView web_url;
    ImageView user_img;
    TextView display_name;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        String id = getIntent().getStringExtra("id");

        user_img=findViewById(R.id.user_img);
        display_name=findViewById(R.id.displayname);
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



        db.collection("users").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
                        if(document.contains("photo_url"))
                        {
                            Picasso.get().load(document.getString("photo_url")).into(user_img);
                        }
                        if(document.contains("display_name"))
                        {
                            display_name.setText(document.getString("display_name"));
                        }


                    } else {
                        Log.d("Document fetch Fail", "No such document");
                        Toast.makeText(getApplicationContext(),"No User found with this QR Code!",Toast.LENGTH_SHORT).show();
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
                startActivity(i);
                break;
            }
            case R.id.github_button:{
                i.setData(Uri.parse(github_url.getText().toString()));
                startActivity(i);
                break;
            }
            case R.id.instagram_button:{
                i.setData(Uri.parse(instagram_url.getText().toString()));
                startActivity(i);
                break;
            }
            case R.id.facebook_button:{
                i.setData(Uri.parse(facebook_url.getText().toString()));
                startActivity(i);
                break;
            }
            case R.id.web_button:{
                i.setData(Uri.parse(web_url.getText().toString()));
                startActivity(i);
                break;
            }
        }
    }
}
