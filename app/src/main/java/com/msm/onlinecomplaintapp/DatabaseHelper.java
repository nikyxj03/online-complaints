package com.msm.onlinecomplaintapp;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.msm.onlinecomplaintapp.Interfaces.OnDataFetchListener;
import com.msm.onlinecomplaintapp.Interfaces.OnDataSFetchListener;
import com.msm.onlinecomplaintapp.Interfaces.OnDataUpdatedListener;
import com.msm.onlinecomplaintapp.Models.AdminRequests;
import com.msm.onlinecomplaintapp.Models.Complaint;
import com.msm.onlinecomplaintapp.Models.Users;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelper {

    private Context context;
    private static DatabaseHelper databaseHelper;
    private FirebaseFirestore database;

    public static final String COMPLAINT_DB_KEY="Complaints";
    public static final String USERS_DB_KEY = "Users";
    public static final String ADMIN_USER_DB_KEY = "AdminUserData";
    public static final String DEPT_USER_DB_KEY = "DepartmentUsers";
    public static final String DEPT_DB_KEY = "Departments";
    public static final String ADMIN_REQUESTS_DB_KEY = "AdminRequests";
    public static final String USER_SUPP_DB_KEY = "UserSupp";

    public static DatabaseHelper getInstance(Context context) {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(context);
        }
        return databaseHelper;
    }

    private DatabaseHelper(Context context) {
        this.context = context;
    }

    public void init() {
        database = FirebaseFirestore.getInstance();
    }

    public void getPublicComplaints(final OnDataFetchListener<Complaint> onDataFetchListener){
        database.collection(COMPLAINT_DB_KEY).whereEqualTo("mode","public").whereEqualTo("acm","0").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Complaint> complaintList=new ArrayList<>();
                for (QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                    complaintList.add(queryDocumentSnapshot.toObject(Complaint.class));
                }
                onDataFetchListener.onDataFetched(complaintList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onDataFetchListener.onDataFetched(null);
            }
        });
    }

    public void getPublicArchivedComplaints(final OnDataFetchListener<Complaint> onDataFetchListener){
        database.collection(COMPLAINT_DB_KEY).whereEqualTo("mode","public").whereEqualTo("acm","1" ).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Complaint> complaintList=new ArrayList<>();
                for (QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                    complaintList.add(queryDocumentSnapshot.toObject(Complaint.class));
                }
                onDataFetchListener.onDataFetched(complaintList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onDataFetchListener.onDataFetched(null);
            }
        });
    }

    public void getUserPrivateArchivedComplaints(String userId,final OnDataFetchListener<Complaint> onDataFetchListener){
        database.collection(COMPLAINT_DB_KEY).whereEqualTo("uid",userId ).whereEqualTo("acm","1").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Complaint> complaintList=new ArrayList<>();
                for (QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                    complaintList.add(queryDocumentSnapshot.toObject(Complaint.class));
                }
                onDataFetchListener.onDataFetched(complaintList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onDataFetchListener.onDataFetched(null);
            }
        });
    }

    public void getDepartmentPrivateArchivedComplaints(final String deptId, final OnDataFetchListener<Complaint> onDataFetchListener){
        database.collection(COMPLAINT_DB_KEY).whereEqualTo("acm","1").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Complaint> complaintList=new ArrayList<>();
                for (QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                    if(queryDocumentSnapshot.getData().get("dept").toString().contains(deptId)) {
                        complaintList.add(queryDocumentSnapshot.toObject(Complaint.class));
                    }
                }
                onDataFetchListener.onDataFetched(complaintList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onDataFetchListener.onDataFetched(null);
            }
        });
    }

    public void getDepartmentComplaints(final String deptId,final OnDataFetchListener<Complaint> onDataFetchListener){
        database.collection(COMPLAINT_DB_KEY).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Complaint> complaintList=new ArrayList<>();
                for (QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                    if(queryDocumentSnapshot.getData().get("dept").toString().contains(deptId)) {
                        complaintList.add(queryDocumentSnapshot.toObject(Complaint.class));
                    }
                }
                onDataFetchListener.onDataFetched(complaintList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onDataFetchListener.onDataFetched(null);
            }
        });
    }

    public void getUserComplaints(final String userId,final OnDataFetchListener<Complaint> onDataFetchListener){
        database.collection(COMPLAINT_DB_KEY).whereEqualTo("uid",userId ).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Complaint> complaintList=new ArrayList<>();
                for (QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                    complaintList.add(queryDocumentSnapshot.toObject(Complaint.class));
                }
                onDataFetchListener.onDataFetched(complaintList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onDataFetchListener.onDataFetched(null);
            }
        });
    }

    public void getAllComplaintsUA(final OnDataFetchListener<Complaint> onDataFetchListener){
        database.collection(COMPLAINT_DB_KEY).whereEqualTo("acm","0").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Complaint> complaintList=new ArrayList<>();
                for (QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                    complaintList.add(queryDocumentSnapshot.toObject(Complaint.class));
                }
                onDataFetchListener.onDataFetched(complaintList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onDataFetchListener.onDataFetched(null);
            }
        });
    }

    public void getAllComplaintsA(final OnDataFetchListener<Complaint> onDataFetchListener){
        database.collection(COMPLAINT_DB_KEY).whereEqualTo("acm","1").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Complaint> complaintList=new ArrayList<>();
                for (QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                    complaintList.add(queryDocumentSnapshot.toObject(Complaint.class));
                }
                onDataFetchListener.onDataFetched(complaintList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onDataFetchListener.onDataFetched(null);
            }
        });
    }

    public void getRequestsUR(final OnDataFetchListener<AdminRequests> onDataFetchListener){
        database.collection(ADMIN_REQUESTS_DB_KEY).whereEqualTo("status","0" ).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<AdminRequests> adminRequestsList=new ArrayList<>();
                for (QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                    adminRequestsList.add(queryDocumentSnapshot.toObject(AdminRequests.class));
                }
                onDataFetchListener.onDataFetched(adminRequestsList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onDataFetchListener.onDataFetched(null);
            }
        });
    }

    public void updateRequest(final AdminRequests adminRequests,final OnDataUpdatedListener onDataUpdatedListener){
        database.collection(ADMIN_REQUESTS_DB_KEY).document(adminRequests.getRid()).set(adminRequests.toMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                onDataUpdatedListener.onDataUploaded(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onDataUpdatedListener.onDataUploaded(false);
            }
        });
    }

    public void fetchUserData(final String userId, final OnDataSFetchListener<Users> onDataSFetchListener){
        database.collection(USERS_DB_KEY).document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                onDataSFetchListener.onDataSFetch(documentSnapshot.toObject(Users.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onDataSFetchListener.onDataSFetch(null);
            }
        });
    }

    public void updateUserData(final Users users,final OnDataUpdatedListener onDataUpdatedListener){
        database.collection(USERS_DB_KEY).document(users.getUid()).set(users.toMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                onDataUpdatedListener.onDataUploaded(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onDataUpdatedListener.onDataUploaded(false);
            }
        });
    }

    public void updateComplaint(final Complaint complaint,final OnDataUpdatedListener onDataUpdatedListener){
        database.collection(COMPLAINT_DB_KEY).document(complaint.getCid()).set(complaint.toMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                onDataUpdatedListener.onDataUploaded(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onDataUpdatedListener.onDataUploaded(false);
            }
        });
    }

    public void fetchComplaint(final String compId,final OnDataSFetchListener<Complaint> onDataSFetchListener){
        database.collection(COMPLAINT_DB_KEY).document(compId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                onDataSFetchListener.onDataSFetch(documentSnapshot.toObject(Complaint.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onDataSFetchListener.onDataSFetch(null);
            }
        });
    }

}
