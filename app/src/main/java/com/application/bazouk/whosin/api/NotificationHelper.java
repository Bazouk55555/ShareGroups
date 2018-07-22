package com.application.bazouk.whosin.api;

import com.application.bazouk.whosin.models.Notification;
import com.application.bazouk.whosin.models.UserGroup;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationHelper {

    private static final String COLLECTION_NAME = "notifications";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getNotificationCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<DocumentReference> createNotification(Notification notification) {
        Map<String, Object> notificationMap = new HashMap<>();
        notificationMap.put("id_group", notification.getIdGroup());
        notificationMap.put("username", notification.getUsername());
        notificationMap.put("name_of_the_group", notification.getNameOfTheGroup());
        return getNotificationCollection().add(notificationMap);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getNotification(String idGroup){
        return NotificationHelper.getNotificationCollection().document(idGroup).get();
    }

    // --- UPDATE ---

    public static Task<Void> updateNotification(String uid, List<String>usernames, Boolean isPresent, String nameOfTheGroup) {
        return NotificationHelper.getNotificationCollection().document(uid).update("username_array", usernames, "is_present",isPresent,"name", nameOfTheGroup);
    }

    // --- DELETE ---

    public static Task<Void> deleteUser(String uid) {
        return NotificationHelper.getNotificationCollection().document(uid).delete();
    }

}
