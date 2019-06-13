package com.msm.onlinecomplaintapp.UserActivities;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import com.google.firebase.storage.UploadTask;
import com.msm.onlinecomplaintapp.MyImage;
import com.msm.onlinecomplaintapp.R;

public class newcomplaint extends AppCompatActivity {


    private static final int REQUEST_CODE_NEWCOMP=5;
    private static final int REQUEST_CODE_HOMEPAGE=11;
    private static final int REQUEST_CODE_MYCOMPLAINT=12;
    private static final int REQUEST_CODE_SETTINGS=13;
    private static final int REQUEST_CODE_MAIN=4;

    private Intent mainintent=new Intent();
    private Intent newcompintent=new Intent();
    private Intent mycomplaintintent=new Intent();
    private Intent settingsintent=new Intent();
    private Intent homeintent=new Intent();

    private EditText titleedit;
    private EditText descedit;
    private ImageView compimage;
    private RelativeLayout photolinear;
    private RadioGroup rgpm;
    private RadioButton publicrb;
    private RadioButton privaterb;
    private TextView depstext;
    private CheckBox cbrm;
    private LinearLayout cameratexturelinear;
    private LinearLayout compformlinear;
    private Button capimgbutton;
    private ImageView camlogo;
    private ListView depsellist;
    private Button depseldonebutton;

    private Dialog depdialog;
    private BottomSheetDialog botdialog;

    private Uri mCapturedImageURI;
    private ArrayList<MyImage> images;

    private Calendar cutica=Calendar.getInstance();

    private Intent camintent=new Intent();

    private HashMap<String,Object> tempmap1=new HashMap<>();
    private ArrayList<HashMap<String,Object>> complaintlistmap=new ArrayList<>();
    private ArrayList<HashMap<String,Object>> usercomplistmap=new ArrayList<>();
    private ArrayList<HashMap<String,Object>> usersupplistmap=new ArrayList<>();
    private ArrayList<HashMap<String,Object>> deptlistmap=new ArrayList<>();
    private ArrayList<String> compdeptlist=new ArrayList<>();

    private String cid;
    private int flag1=0;
    private int ciduf=0;
    private String cidu="";
    private String uid="";
    private int deptchflag=1;

    private FirebaseAuth vmauth=FirebaseAuth.getInstance();

    private FirebaseDatabase _database=FirebaseDatabase.getInstance();
    private DatabaseReference udfb=_database.getReference("userdata");
    private DatabaseReference cffb=_database.getReference("complaint");
    private DatabaseReference ucfb=_database.getReference("usercomp");
    private DatabaseReference usfb=_database.getReference("usersupp");
    private DatabaseReference dfb=_database.getReference("department");
    private DatabaseReference pcfb=_database.getReference("deptcomplaints");

    private FirebaseStorage vmfbs=FirebaseStorage.getInstance();
    private StorageReference compimgfbs=vmfbs.getReference();
    private StorageReference storageReference;

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        /*if (resultCode == RESULT_OK) {
            if(requestCode==9){
                String photoPath = Environment.getExternalStorageDirectory() + "/pic.jpg";
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                final Bitmap b = BitmapFactory.decodeFile(photoPath, options);
                compimage.setImageBitmap(b);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), b, "Title", null);
                Uri fbsimguri=Uri.parse(path);
                compimgfbs.putFile(fbsimguri);
            }
            if (requestCode == 10) {
                Uri selectedImage = data.getData();
                InputStream inputStream;
                try {
                    inputStream = getContentResolver().openInputStream(selectedImage);
                    Bitmap bmimage=BitmapFactory.decodeStream(inputStream);
                    compimage.setImageBitmap(bmimage);
                }catch (FileNotFoundException e){
                    e.printStackTrace();
                }
            }*/
        if (resultCode == RESULT_OK) {
            if (requestCode == 6) {
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Loading...");
                progressDialog.show();
                Bundle extras=data.getExtras();
                storageReference=compimgfbs.child("complaintimages/"+cid);
                Bitmap imagebitmap=(Bitmap)extras.get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                imagebitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getContentResolver(),imagebitmap, "compimage", null);
                Uri uri=Uri.parse(path);
                storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                cidu=uri.toString();
                                ciduf=1;
                                progressDialog.dismiss();
                                Toast.makeText(newcomplaint.this,"Uploaded Successfully",Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                });
                compimage.setImageBitmap(imagebitmap);
            }
            else if (requestCode == 7) {
                Uri selectedImage = data.getData();
                storageReference=compimgfbs.child("complaintimages/"+cid);
                storageReference.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        cidu=taskSnapshot.getUploadSessionUri().toString();
                        Toast.makeText(newcomplaint.this,"Uploaded Successfully",Toast.LENGTH_LONG).show();
                    }
                });
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                compimage.setImageBitmap(thumbnail);
            }
        }
    }
    //}

    @Override
    public void onBackPressed() {
        if(cidu.length()!=0){
            if(ciduf==1) {
                storageReference.delete();
            }
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.submitbuttonmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id==android.R.id.home){
            newcomplaint.this.finish();
        }

        if (id==R.id.csubmitbutton){
            if(!titleedit.getText().equals("") && !descedit.getText().equals("") && (publicrb.isChecked() || privaterb.isChecked())) {
                tempmap1=new HashMap<>();
                tempmap1.put("cid",cid);
                tempmap1.put("uid",uid);
                if (cidu.length() != 0) {
                    tempmap1.put("ciuri",cidu);
                }
                tempmap1.put("title", titleedit.getText().toString());
                tempmap1.put("desc", descedit.getText().toString());
                tempmap1.put("time", new SimpleDateFormat("HH:mm dd-MM-YYYY").format(cutica.getTime()));
                tempmap1.put("acm",0 );
                if (publicrb.isChecked()) {
                    tempmap1.put("mode","public");
                } else if (privaterb.isChecked()){
                    tempmap1.put("mode", "private");
                }
                tempmap1.put("supportno",0);
                if(cbrm.isChecked()){
                    tempmap1.put("amode","yes");
                }
                else{
                    tempmap1.put("amode","no");
                }
                String dept="";
                if(compdeptlist.size()>0){
                    dept=compdeptlist.get(0);
                    for(int i12=1;i12<compdeptlist.size();i12++){
                        dept=dept+","+compdeptlist.get(i12);
                    }
                }
                tempmap1.put("dept",dept);
                if(dept=="" && tempmap1.get("mode").toString().equals("private")){
                    Toast.makeText(newcomplaint.this, "A private complaint must have at least one department",Toast.LENGTH_LONG ).show();
                }
                else {
                    cffb.child(cid).updateChildren(tempmap1).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            newcomplaint.this.finish();
                        }
                    });
                }
            }
            else{
                Toast.makeText(newcomplaint.this,"All fields are required",Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newcomplaint);
        Initialize();
        InitializeLogic();
    }

    private void Initialize(){

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        titleedit=findViewById(R.id.titleedit);
        descedit=findViewById(R.id.descedit);
        compimage=findViewById(R.id.compimage);
        photolinear=findViewById(R.id.photolinear);
        rgpm=findViewById(R.id.rgpm);
        publicrb=findViewById(R.id.publicrb);
        privaterb=findViewById(R.id.privaterb);
        depstext=findViewById(R.id.depstext);
        cbrm=findViewById(R.id.cbrm);
        camlogo=findViewById(R.id.camlogo);
        compformlinear=findViewById(R.id.compformlinear);

        camlogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
                AlertDialog.Builder builder = new AlertDialog.Builder(newcomplaint.this);
                builder.setTitle("Add Photo!");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo"))
                        {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, 6);
                        }
                        else if (options[item].equals("Choose from Gallery"))
                        {
                            Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, 7);
                        }
                        else if (options[item].equals("Cancel")) {

                            dialog.dismiss();
                        }
                    }
                });
                if (ContextCompat.checkSelfPermission(newcomplaint.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(newcomplaint.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(newcomplaint.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
                }
                else{
                    builder.show();
                }
                //startActivityForResult(camintent,6);
                //Intent camintent = new   Intent(Intent.ACTION_PICK);
                //File f=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                //String fp=f.getPath();
                //Uri data=Uri.parse(fp);
                //camintent.setDataAndType(data,"image/*");
                //startActivityForResult(camintent,7);
            }
        });


        depstext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*ViewGroup.LayoutParams ddparam=compformlinear.getLayoutParams();
                depdialog=new Dialog(newcomplaint.this,ddparam.width);
                View view=depdialog.getLayoutInflater().inflate(R.layout.deplistlayout,null);
                depsellist=view.findViewById(R.id.depsellist);
                depsellist.setAdapter(new deptspinneradapter(deptlistmap));
                depseldonebutton=view.findViewById(R.id.depseldonebutton);
                depseldonebutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        depdialog.dismiss();
                    }
                });
                setListViewHeightBasedOnItems(depsellist);
                depdialog.setContentView(view);
                depdialog.show();*/
                deptcheckupdate();
                botdialog.show();
            }
        });

        ChildEventListener cffb_fl = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                final String _childKey = dataSnapshot.getKey();
                final HashMap<String, Object> _childValue = dataSnapshot.getValue(_ind);
                cffb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot _dataSnapshot) {
                        complaintlistmap = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                complaintlistmap.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        deptcheckupdate();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                final String _childKey = dataSnapshot.getKey();
                final HashMap<String, Object> _childValue = dataSnapshot.getValue(_ind);
                cffb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot _dataSnapshot) {
                        complaintlistmap = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                complaintlistmap.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        deptcheckupdate();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                final String _childKey = dataSnapshot.getKey();
                final HashMap<String, Object> _childValue = dataSnapshot.getValue(_ind);
                cffb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot _dataSnapshot) {
                        complaintlistmap = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                complaintlistmap.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        deptcheckupdate();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                final String _childKey = dataSnapshot.getKey();
                final HashMap<String, Object> _childValue = dataSnapshot.getValue(_ind);
                cffb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot _dataSnapshot) {
                        complaintlistmap = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                complaintlistmap.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        deptcheckupdate();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        cffb.addChildEventListener(cffb_fl);

        ChildEventListener usfb_fl = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                final String _childKey = dataSnapshot.getKey();
                final HashMap<String, Object> _childValue = dataSnapshot.getValue(_ind);
                usfb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot _dataSnapshot) {
                        usersupplistmap= new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                usersupplistmap.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        deptcheckupdate();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                final String _childKey = dataSnapshot.getKey();
                final HashMap<String, Object> _childValue = dataSnapshot.getValue(_ind);
                usfb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot _dataSnapshot) {
                        usersupplistmap = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                usersupplistmap.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        deptcheckupdate();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                final String _childKey = dataSnapshot.getKey();
                final HashMap<String, Object> _childValue = dataSnapshot.getValue(_ind);
                usfb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot _dataSnapshot) {
                        usersupplistmap = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                usersupplistmap.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        deptcheckupdate();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                final String _childKey = dataSnapshot.getKey();
                final HashMap<String, Object> _childValue = dataSnapshot.getValue(_ind);
                usfb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot _dataSnapshot) {
                        usersupplistmap = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                usersupplistmap.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        deptcheckupdate();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        usfb.addChildEventListener(usfb_fl);

        final ChildEventListener ucfb_fl = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                final String _childKey = dataSnapshot.getKey();
                final HashMap<String, Object> _childValue = dataSnapshot.getValue(_ind);
                ucfb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot _dataSnapshot) {
                        usercomplistmap = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                usercomplistmap.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        deptcheckupdate();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                final String _childKey = dataSnapshot.getKey();
                final HashMap<String, Object> _childValue = dataSnapshot.getValue(_ind);
                ucfb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot _dataSnapshot) {
                        usercomplistmap = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                usercomplistmap.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        deptcheckupdate();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                final String _childKey = dataSnapshot.getKey();
                final HashMap<String, Object> _childValue = dataSnapshot.getValue(_ind);
                ucfb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot _dataSnapshot) {
                        usercomplistmap = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                usercomplistmap.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        deptcheckupdate();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};

                ucfb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot _dataSnapshot) {
                        usercomplistmap = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                usercomplistmap.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        deptcheckupdate();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        ucfb.addChildEventListener(ucfb_fl);

        final ChildEventListener dfb_fl = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                final String _childKey = dataSnapshot.getKey();
                final HashMap<String, Object> _childValue = dataSnapshot.getValue(_ind);
                dfb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot _dataSnapshot) {
                        deptlistmap = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                deptlistmap.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        deptcheckupdate();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                final String _childKey = dataSnapshot.getKey();
                final HashMap<String, Object> _childValue = dataSnapshot.getValue(_ind);
                dfb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot _dataSnapshot) {
                        deptlistmap = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                deptlistmap.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        deptcheckupdate();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                final String _childKey = dataSnapshot.getKey();
                final HashMap<String, Object> _childValue = dataSnapshot.getValue(_ind);
                dfb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot _dataSnapshot) {
                        deptlistmap = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                deptlistmap.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        deptcheckupdate();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};

                ucfb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot _dataSnapshot) {
                        deptlistmap = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                deptlistmap.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        deptcheckupdate();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        dfb.addChildEventListener(dfb_fl);

    }

    private void InitializeLogic(){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        depstext.setText("None");

        cffb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                complaintlistmap = new ArrayList<>();
                try {
                    GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                    };
                    for (DataSnapshot _data : dataSnapshot.getChildren()) {
                        HashMap<String, Object> _map = _data.getValue(_ind);
                        complaintlistmap.add(_map);
                    }
                } catch (Exception _e) {
                    _e.printStackTrace();
                }
                ucfb.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        usercomplistmap= new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                usercomplistmap.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        usfb.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                usersupplistmap = new ArrayList<>();
                                try {
                                    GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                                    };
                                    for (DataSnapshot _data : dataSnapshot.getChildren()) {
                                        HashMap<String, Object> _map = _data.getValue(_ind);
                                        usersupplistmap.add(_map);
                                    }
                                } catch (Exception _e) {
                                    _e.printStackTrace();
                                }
                                dfb.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        deptlistmap = new ArrayList<>();
                                        try {
                                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                                            };
                                            for (DataSnapshot _data : dataSnapshot.getChildren()) {
                                                HashMap<String, Object> _map = _data.getValue(_ind);
                                                deptlistmap.add(_map);
                                            }
                                        } catch (Exception _e) {
                                            _e.printStackTrace();
                                        }

                                        do {
                                            flag1=0;
                                            cid=randomAlphaNumeric(7);
                                            for (int i1 = 0; i1 < complaintlistmap.size(); i1++) {
                                                if (complaintlistmap.get(i1).get("cid").toString().equals(cid)) {
                                                    flag1 = 1;
                                                }
                                            }
                                        }while(flag1==1);
                                        uid=vmauth.getCurrentUser().getUid();
                                        progressDialog.dismiss();
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    public class deptspinneradapter extends BaseAdapter {

        private int chflag=0;

        private ArrayList<HashMap<String,Object>> _data=new ArrayList<>();

        public deptspinneradapter(ArrayList<HashMap<String,Object>> _arr){
            _data=_arr;
        }

        @Override
        public int getCount() {
            return _data.size();
        }

        @Override
        public HashMap<String, Object> getItem(int _index) {
            return _data.get(_index);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater ds_inflater=(LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View ds_view=convertView;
            if(ds_view==null)
            {
                ds_view=ds_inflater.inflate(R.layout.depspinnerlayout,null);
            }
            final TextView deptnametext=ds_view.findViewById(R.id.deptnametext);
            final CheckBox depcheck=ds_view.findViewById(R.id.depcheck);
            deptnametext.setText(_data.get(position).get("name").toString());
            if(compdeptlist.contains(deptlistmap.get(position).get("did").toString())){
                depcheck.setChecked(true);
            }
            depcheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(depcheck.isChecked()){
                        if(!compdeptlist.contains(deptlistmap.get(position).get("did").toString())) {
                            compdeptlist.add(deptlistmap.get(position).get("did").toString());
                            if(compdeptlist.size()==1){
                                depstext.setText(deptlistmap.get(position).get("name").toString());
                                if(depstext.getText().length()>20){
                                    depstext.setText(depstext.getText().toString().substring(0,18)+"...");
                                }
                            }
                            else{
                                depstext.setText(depstext.getText()+" and "+String.valueOf(compdeptlist.size()-1)+" more");
                            }
                        }
                    }
                    else{
                        compdeptlist.remove(deptlistmap.get(position).get("did").toString());
                    }
                }
            });

            return ds_view;
        }
    }

    public static boolean setListViewHeightBasedOnItems(ListView listView)
    {
        ListAdapter listAdapter=listView.getAdapter();
        if(listAdapter!=null)
        {
            int numberOfItems =listAdapter.getCount();
            int th=0;
            for(int ip=0;ip<numberOfItems;ip++)
            {
                View item =listAdapter.getView(ip,null,listView);
                float px = 500*(listView.getResources().getDisplayMetrics().density);
                item.measure(View.MeasureSpec.makeMeasureSpec((int)px,View.MeasureSpec.AT_MOST),View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED));
                th=th+item.getMeasuredHeight();
            }
            int totalDividerHeight=listView.getDividerHeight()*(numberOfItems-1);
            int totalPadding=listView.getPaddingTop()+listView.getPaddingBottom();
            ViewGroup.LayoutParams params= listView.getLayoutParams();
            params.height=(int)(th*(1.1)+totalDividerHeight+totalPadding);
            listView.setLayoutParams(params);
            listView.requestLayout();
            return true;
        }
        else
            return false;
    }

    private void deptcheckupdate(){
        botdialog=new BottomSheetDialog(newcomplaint.this);
        View view=botdialog.getLayoutInflater().inflate(R.layout.deplistlayout,null);
        depsellist=view.findViewById(R.id.depsellist);
        depsellist.setAdapter(new deptspinneradapter(deptlistmap));
        setListViewHeightBasedOnItems(depsellist);
        botdialog.setContentView(view);
    }


}
