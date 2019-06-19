package com.msm.onlinecomplaintapp.Others;

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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
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
import com.msm.onlinecomplaintapp.Models.AdminUser;
import com.msm.onlinecomplaintapp.Models.Complaint;
import com.msm.onlinecomplaintapp.Models.Departments;
import com.msm.onlinecomplaintapp.Models.DeptUsers;
import com.msm.onlinecomplaintapp.Models.Users;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

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

    public int sm=0;

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

    public void getPublicComplaintsSS(final OnDataFetchListener<Complaint> onDataFetchListener){
        database.collection(COMPLAINT_DB_KEY).whereEqualTo("mode","public").whereEqualTo("om","0").orderBy("supportno", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots!=null){
                    List<Complaint> complaintList=new ArrayList<>();
                    for (QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                        complaintList.add(queryDocumentSnapshot.toObject(Complaint.class));
                    }
                    onDataFetchListener.onDataFetched(complaintList);
                }
                else {
                    onDataFetchListener.onDataFetched(null);
                }
            }
        });
    }

    public void getPublicComplaintsST(final OnDataFetchListener<Complaint> onDataFetchListener){
        database.collection(COMPLAINT_DB_KEY).whereEqualTo("mode","public").whereEqualTo("om","0").orderBy("time", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots!=null){
                    List<Complaint> complaintList=new ArrayList<>();
                    for (QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                        complaintList.add(queryDocumentSnapshot.toObject(Complaint.class));
                    }
                    onDataFetchListener.onDataFetched(complaintList);
                }
                else {
                    onDataFetchListener.onDataFetched(null);
                }
            }
        });
    }

    public void getPublicArchivedComplaintsSS(final OnDataFetchListener<Complaint> onDataFetchListener){
        database.collection(COMPLAINT_DB_KEY).whereEqualTo("mode","public").whereEqualTo("om","1" ).orderBy("supportno", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots!=null){
                    List<Complaint> complaintList=new ArrayList<>();
                    for (QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                        complaintList.add(queryDocumentSnapshot.toObject(Complaint.class));
                    }
                    onDataFetchListener.onDataFetched(complaintList);
                }
                else {
                    onDataFetchListener.onDataFetched(null);
                }
            }
        });
    }

    public void getPublicArchivedComplaintsST(final OnDataFetchListener<Complaint> onDataFetchListener){
        database.collection(COMPLAINT_DB_KEY).whereEqualTo("mode","public").whereEqualTo("om","1" ).orderBy("time", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots!=null){
                    List<Complaint> complaintList=new ArrayList<>();
                    for (QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                        complaintList.add(queryDocumentSnapshot.toObject(Complaint.class));
                    }
                    onDataFetchListener.onDataFetched(complaintList);
                }
                else {
                    onDataFetchListener.onDataFetched(null);
                }
            }
        });
    }

    public void getUserPrivateOpenComplaintsSS(String userId,final OnDataFetchListener<Complaint> onDataFetchListener){
        database.collection(COMPLAINT_DB_KEY).whereEqualTo("uid",userId ).whereEqualTo("om","0").orderBy("supportno", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots!=null){
                    List<Complaint> complaintList=new ArrayList<>();
                    for (QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                        complaintList.add(queryDocumentSnapshot.toObject(Complaint.class));
                    }
                    onDataFetchListener.onDataFetched(complaintList);
                }
                else {
                    onDataFetchListener.onDataFetched(null);
                }
            }
        });
    }

    public void getUserPrivateOpenComplaintsST(String userId,final OnDataFetchListener<Complaint> onDataFetchListener){
        database.collection(COMPLAINT_DB_KEY).whereEqualTo("uid",userId ).whereEqualTo("om","0").orderBy("time", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots!=null){
                    List<Complaint> complaintList=new ArrayList<>();
                    for (QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                        complaintList.add(queryDocumentSnapshot.toObject(Complaint.class));
                    }
                    onDataFetchListener.onDataFetched(complaintList);
                }
                else {
                    onDataFetchListener.onDataFetched(null);
                }
            }
        });
    }

    public void getUserPrivateClosedComplaintsSS(String userId,final OnDataFetchListener<Complaint> onDataFetchListener){
        database.collection(COMPLAINT_DB_KEY).whereEqualTo("uid",userId ).whereEqualTo("om","1").orderBy("supportno", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots!=null){
                    List<Complaint> complaintList=new ArrayList<>();
                    for (QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                        complaintList.add(queryDocumentSnapshot.toObject(Complaint.class));
                    }
                    onDataFetchListener.onDataFetched(complaintList);
                }
                else {
                    onDataFetchListener.onDataFetched(null);
                }
            }
        });
    }

    public void getUserPrivateClosedComplaintsST(String userId,final OnDataFetchListener<Complaint> onDataFetchListener){
        database.collection(COMPLAINT_DB_KEY).whereEqualTo("uid",userId ).whereEqualTo("om","1").orderBy("time", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots!=null){
                    List<Complaint> complaintList=new ArrayList<>();
                    for (QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                        complaintList.add(queryDocumentSnapshot.toObject(Complaint.class));
                    }
                    onDataFetchListener.onDataFetched(complaintList);
                }
                else {
                    onDataFetchListener.onDataFetched(null);
                }
            }
        });
    }

    public void getDepartmentPrivateRegisteredComplaintsSS(final String deptId, final OnDataFetchListener<Complaint> onDataFetchListener){
        database.collection(COMPLAINT_DB_KEY).whereEqualTo("acm","0").orderBy("supportno", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots!=null){
                    List<Complaint> complaintList=new ArrayList<>();
                    for (QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                        if(queryDocumentSnapshot.getData().get("dept").toString().contains(deptId)) {
                            complaintList.add(queryDocumentSnapshot.toObject(Complaint.class));
                        }
                    }
                    onDataFetchListener.onDataFetched(complaintList);
                }
                else {
                    onDataFetchListener.onDataFetched(null);
                }
            }
        });
    }

    public void getDepartmentPrivateRegisteredComplaintsST(final String deptId, final OnDataFetchListener<Complaint> onDataFetchListener){
        database.collection(COMPLAINT_DB_KEY).whereEqualTo("acm","0").orderBy("time", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots!=null){
                    List<Complaint> complaintList=new ArrayList<>();
                    for (QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                        if(queryDocumentSnapshot.getData().get("dept").toString().contains(deptId)) {
                            complaintList.add(queryDocumentSnapshot.toObject(Complaint.class));
                        }
                    }
                    onDataFetchListener.onDataFetched(complaintList);
                }
                else {
                    onDataFetchListener.onDataFetched(null);
                }
            }
        });
    }

    public void getDepartmentPrivateWatchingComplaintsSS(final String deptId, final OnDataFetchListener<Complaint> onDataFetchListener){
        database.collection(COMPLAINT_DB_KEY).whereEqualTo("acm","1").orderBy("supportno", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots!=null){
                    List<Complaint> complaintList=new ArrayList<>();
                    for (QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                        if(queryDocumentSnapshot.getData().get("dept").toString().contains(deptId)) {
                            complaintList.add(queryDocumentSnapshot.toObject(Complaint.class));
                        }
                    }
                    onDataFetchListener.onDataFetched(complaintList);
                }
                else {
                    onDataFetchListener.onDataFetched(null);
                }
            }
        });
    }

    public void getDepartmentPrivateWatchingComplaintsST(final String deptId, final OnDataFetchListener<Complaint> onDataFetchListener){
        database.collection(COMPLAINT_DB_KEY).whereEqualTo("acm","1").orderBy("time", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots!=null){
                    List<Complaint> complaintList=new ArrayList<>();
                    for (QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                        if(queryDocumentSnapshot.getData().get("dept").toString().contains(deptId)) {
                            complaintList.add(queryDocumentSnapshot.toObject(Complaint.class));
                        }
                    }
                    onDataFetchListener.onDataFetched(complaintList);
                }
                else {
                    onDataFetchListener.onDataFetched(null);
                }
            }
        });
    }

    public void getDepartmentPrivateResolvedComplaintsSS(final String deptId, final OnDataFetchListener<Complaint> onDataFetchListener){
        database.collection(COMPLAINT_DB_KEY).whereEqualTo("acm","2").orderBy("supportno", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots!=null){
                    List<Complaint> complaintList=new ArrayList<>();
                    for (QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                        if(queryDocumentSnapshot.getData().get("dept").toString().contains(deptId)) {
                            complaintList.add(queryDocumentSnapshot.toObject(Complaint.class));
                        }
                    }
                    onDataFetchListener.onDataFetched(complaintList);
                }
                else {
                    onDataFetchListener.onDataFetched(null);
                }
            }
        });
    }

    public void getDepartmentPrivateResolvedComplaintsST(final String deptId, final OnDataFetchListener<Complaint> onDataFetchListener){
        database.collection(COMPLAINT_DB_KEY).whereEqualTo("acm","2").orderBy("time", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots!=null){
                    List<Complaint> complaintList=new ArrayList<>();
                    for (QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                        if(queryDocumentSnapshot.getData().get("dept").toString().contains(deptId)) {
                            complaintList.add(queryDocumentSnapshot.toObject(Complaint.class));
                        }
                    }
                    onDataFetchListener.onDataFetched(complaintList);
                }
                else {
                    onDataFetchListener.onDataFetched(null);
                }
            }
        });
    }

    public void getDepartmentPrivateIgnoredComplaintsSS(final String deptId, final OnDataFetchListener<Complaint> onDataFetchListener){
        database.collection(COMPLAINT_DB_KEY).whereEqualTo("acm","3").orderBy("supportno", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots!=null){
                    List<Complaint> complaintList=new ArrayList<>();
                    for (QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                        if(queryDocumentSnapshot.getData().get("dept").toString().contains(deptId)) {
                            complaintList.add(queryDocumentSnapshot.toObject(Complaint.class));
                        }
                    }
                    onDataFetchListener.onDataFetched(complaintList);
                }
                else {
                    onDataFetchListener.onDataFetched(null);
                }
            }
        });
    }

    public void getDepartmentPrivateIgnoredComplaintsST(final String deptId, final OnDataFetchListener<Complaint> onDataFetchListener){
        database.collection(COMPLAINT_DB_KEY).whereEqualTo("acm","3").orderBy("time", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots!=null){
                    List<Complaint> complaintList=new ArrayList<>();
                    for (QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                        if(queryDocumentSnapshot.getData().get("dept").toString().contains(deptId)) {
                            complaintList.add(queryDocumentSnapshot.toObject(Complaint.class));
                        }
                    }
                    onDataFetchListener.onDataFetched(complaintList);
                }
                else {
                    onDataFetchListener.onDataFetched(null);
                }
            }
        });
    }


    public void getUserComplaintsSS(final String userId,final OnDataFetchListener<Complaint> onDataFetchListener){
        database.collection(COMPLAINT_DB_KEY).whereEqualTo("uid",userId ).orderBy("supportno", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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

    public void getUserComplaintsST(final String userId,final OnDataFetchListener<Complaint> onDataFetchListener){
        database.collection(COMPLAINT_DB_KEY).whereEqualTo("uid",userId ).orderBy("time", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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

    public void getAllComplaintsUASS(final OnDataFetchListener<Complaint> onDataFetchListener){
        database.collection(COMPLAINT_DB_KEY).whereEqualTo("acm","0").orderBy("supportno", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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

    public void getAllComplaintsUAST(final OnDataFetchListener<Complaint> onDataFetchListener){
        database.collection(COMPLAINT_DB_KEY).whereEqualTo("acm","0").orderBy("time", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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

    public void getAllComplaintsASS(final OnDataFetchListener<Complaint> onDataFetchListener){
        database.collection(COMPLAINT_DB_KEY).whereEqualTo("acm","1").orderBy("supportno", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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

    public void getAllComplaintsAST(final OnDataFetchListener<Complaint> onDataFetchListener){
        database.collection(COMPLAINT_DB_KEY).whereEqualTo("acm","1").orderBy("time", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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
        database.collection(ADMIN_REQUESTS_DB_KEY).whereEqualTo("status","0" ).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots!=null){
                    List<AdminRequests> adminRequestsList=new ArrayList<>();
                    for (QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                        adminRequestsList.add(queryDocumentSnapshot.toObject(AdminRequests.class));
                    }
                    onDataFetchListener.onDataFetched(adminRequestsList);
                }
                else {
                    onDataFetchListener.onDataFetched(null);
                }
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

    public void fetchDeptUserData(final String userId, final OnDataSFetchListener<DeptUsers> onDataSFetchListener){
        database.collection(DEPT_USER_DB_KEY).document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                onDataSFetchListener.onDataSFetch(documentSnapshot.toObject(DeptUsers.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onDataSFetchListener.onDataSFetch(null);
            }
        });
    }

    public void fetchAdminUserData(final String userId, final OnDataSFetchListener<AdminUser> onDataSFetchListener){
        database.collection(ADMIN_USER_DB_KEY).document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                onDataSFetchListener.onDataSFetch(documentSnapshot.toObject(AdminUser.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onDataSFetchListener.onDataSFetch(null);
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

    public void getAllCIDs(final OnDataFetchListener<String> onDataFetchListener){
        final List<String> cidlist=new ArrayList<>();
        database.collection(COMPLAINT_DB_KEY).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                    cidlist.add(queryDocumentSnapshot.getId());
                }
                onDataFetchListener.onDataFetched(cidlist);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onDataFetchListener.onDataFetched(null);
            }
        });
    }

    public void getAllRIDs(final OnDataFetchListener<String> onDataFetchListener){
        final List<String> ridlist=new ArrayList<>();
        database.collection(ADMIN_REQUESTS_DB_KEY).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                    ridlist.add(queryDocumentSnapshot.getId());
                }
                onDataFetchListener.onDataFetched(ridlist);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onDataFetchListener.onDataFetched(null);
            }
        });
    }

    public void getUserEmails(final OnDataFetchListener<String> onDataFetchListener){
        final List<String> emaillist=new ArrayList<>();
        database.collection(USERS_DB_KEY).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                    emaillist.add(queryDocumentSnapshot.toObject(Users.class).getEmail());
                }
                onDataFetchListener.onDataFetched(emaillist);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onDataFetchListener.onDataFetched(null);
            }
        });
    }

    public void getDeptrEmails(final OnDataFetchListener<String> onDataFetchListener){
        final List<String> emaillist=new ArrayList<>();
        database.collection(USERS_DB_KEY).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                    emaillist.add(queryDocumentSnapshot.toObject(Users.class).getEmail());
                }
                onDataFetchListener.onDataFetched(emaillist);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onDataFetchListener.onDataFetched(null);
            }
        });
    }

    public void getAdminEmails(final OnDataFetchListener<String> onDataFetchListener){
        final List<String> emaillist=new ArrayList<>();
        database.collection(USERS_DB_KEY).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                    emaillist.add(queryDocumentSnapshot.toObject(Users.class).getEmail());
                }
                onDataFetchListener.onDataFetched(emaillist);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onDataFetchListener.onDataFetched(null);
            }
        });
    }

    public void getDepartmentsList(final OnDataFetchListener<Departments> onDataFetchListener){
        final List<Departments> deptlist=new ArrayList<>();
        database.collection(DEPT_DB_KEY).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots!=null){
                    for (QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                        deptlist.add(queryDocumentSnapshot.toObject(Departments.class));
                    }
                    onDataFetchListener.onDataFetched(deptlist);
                }
                else {
                    onDataFetchListener.onDataFetched(null);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onDataFetchListener.onDataFetched(null);
            }
        });
    }

}
