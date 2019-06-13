package com.msm.onlinecomplaintapp.Others;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.msm.onlinecomplaintapp.Interfaces.OnDataFetchListener;
import com.msm.onlinecomplaintapp.Interfaces.OnDataSFetchListener;
import com.msm.onlinecomplaintapp.Interfaces.OnDataUpdatedListener;
import com.msm.onlinecomplaintapp.Models.AdminRequests;
import com.msm.onlinecomplaintapp.Models.AdminUser;
import com.msm.onlinecomplaintapp.Models.Complaint;
import com.msm.onlinecomplaintapp.Models.DeptUsers;
import com.msm.onlinecomplaintapp.Models.Users;

import java.util.ArrayList;
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
        database.collection(COMPLAINT_DB_KEY).whereEqualTo("mode","public").whereEqualTo("acm","0").addSnapshotListener(new EventListener<QuerySnapshot>() {
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

    public void getPublicArchivedComplaints(final OnDataFetchListener<Complaint> onDataFetchListener){
        database.collection(COMPLAINT_DB_KEY).whereEqualTo("mode","public").whereEqualTo("acm","1" ).addSnapshotListener(new EventListener<QuerySnapshot>() {
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

    public void getUserPrivateArchivedComplaints(String userId,final OnDataFetchListener<Complaint> onDataFetchListener){
        database.collection(COMPLAINT_DB_KEY).whereEqualTo("uid",userId ).whereEqualTo("acm","1").addSnapshotListener(new EventListener<QuerySnapshot>() {
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

    public void getDepartmentPrivateArchivedComplaints(final String deptId, final OnDataFetchListener<Complaint> onDataFetchListener){
        database.collection(COMPLAINT_DB_KEY).whereEqualTo("acm","1").addSnapshotListener(new EventListener<QuerySnapshot>() {
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

    public void getDepartmentComplaints(final String deptId,final OnDataFetchListener<Complaint> onDataFetchListener){
        database.collection(COMPLAINT_DB_KEY).addSnapshotListener(new EventListener<QuerySnapshot>() {
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

    public void getUserComplaints(final String userId,final OnDataFetchListener<Complaint> onDataFetchListener){
        database.collection(COMPLAINT_DB_KEY).whereEqualTo("uid",userId ).addSnapshotListener(new EventListener<QuerySnapshot>() {
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

    public void getAllComplaintsUA(final OnDataFetchListener<Complaint> onDataFetchListener){
        database.collection(COMPLAINT_DB_KEY).whereEqualTo("acm","0").addSnapshotListener(new EventListener<QuerySnapshot>() {
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

    public void getAllComplaintsA(final OnDataFetchListener<Complaint> onDataFetchListener){
        database.collection(COMPLAINT_DB_KEY).whereEqualTo("acm","1").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots!=null){
                    List<Complaint> complaintList = new ArrayList<>();
                    for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
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

    public void updateDeptUserData(final DeptUsers deptUsers,final OnDataUpdatedListener onDataUpdatedListener){
        database.collection(DEPT_USER_DB_KEY).document(deptUsers.getUid()).set(deptUsers.toMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    public void updateAdminUserData(final AdminUser adminUser,final OnDataUpdatedListener onDataUpdatedListener){
        database.collection(ADMIN_USER_DB_KEY).document(adminUser.getUid()).set(adminUser  .toMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        database.collection(COMPLAINT_DB_KEY).document(compId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot!=null){
                    onDataSFetchListener.onDataSFetch(documentSnapshot.toObject(Complaint.class));
                }
                else {
                    onDataSFetchListener.onDataSFetch(null);
                }
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

}
