package com.application.bazouk.whosin.api;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ListView;

import com.application.bazouk.whosin.R;
import com.application.bazouk.whosin.models.User;
import com.application.bazouk.whosin.models.UserGroup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.firebase.ui.auth.AuthUI.TAG;

public class UserGroupHelper {

    private static final String COLLECTION_NAME = "users_groups";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<DocumentReference> createUserGroup(UserGroup userGroupToCreate) {
        Map<String, Object> userGroupMap = new HashMap<>();
        userGroupMap.put("usernames", userGroupToCreate.getUsernames());
        userGroupMap.put("names", userGroupToCreate.getNames());
        userGroupMap.put("is_present", userGroupToCreate.getIsPresent());
        userGroupMap.put("name_of_the_group", userGroupToCreate.getNameOfTheGroup());
        return getUsersCollection().add(userGroupMap);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getUserGroup(String idGroup){
        return UserGroupHelper.getUsersCollection().document(idGroup).get();
    }

    // --- UPDATE ---

    public static Task<Void> updateUserGroupPresence(String uid, List<Boolean> isPresent) {
        return UserGroupHelper.getUsersCollection().document(uid).update("is_present",isPresent);
    }

    public static void addMemberUserGroup(final String uid, final String username, final String name) {
       getUsersCollection().document(uid).get().
                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        List<String> usernames = ((List<String>)task.getResult().getData().get("usernames"));
                        usernames.add(username);
                        List<String> names = ((List<String>)task.getResult().getData().get("names"));
                        names.add(name);
                        List<Boolean> isPresent = ((List<Boolean>)task.getResult().getData().get("is_present"));
                        isPresent.add(true);
                        UserGroupHelper.getUsersCollection().document(uid).update("usernames",usernames,"names",names,"is_present",isPresent);
                    }
                });
    }

    // --- DELETE ---

    public static Task<Void> deleteUser(String uid) {
        return UserGroupHelper.getUsersCollection().document(uid).delete();
    }

}
