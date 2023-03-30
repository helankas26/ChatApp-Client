/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chatapp.client;

import com.chatapp.rmi.ChatRemote;
import com.chatapp.rmi.ChatSessionRemote;
import com.chatapp.rmi.MessageRemote;
import com.chatapp.rmi.SubscriptionRemote;
import com.chatapp.rmi.UserRemote;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author Helanka
 */
public class Client {
    private static Registry reg;
    private static ChatRemote chatRemote;
    private static ChatSessionRemote chatSessionRemote;
    private static MessageRemote messageRemote;
    private static SubscriptionRemote subscriptionRemote;
    private static UserRemote userRemote;
    
    private Client() {}
    
    // static block initialization for exception handling
    static {
        try {
            reg = LocateRegistry.getRegistry("localhost", 4000);
        } catch (RemoteException ex) {
            System.out.println("Exception in client....." + ex.getMessage());
        }
    }
    
    
    public static ChatRemote getChatRemote() {
        if (chatRemote == null) {
            synchronized (ChatRemote.class) {
                if (chatRemote == null) {
                    try {
                        chatRemote = (ChatRemote) reg.lookup("ChatService");
                    } catch (RemoteException | NotBoundException ex) {
                        System.out.println("Exception in getting ChatRemote.....\n" + ex.getMessage());
                    }
                }
            }
        }
        return chatRemote;
    }
    
    public static ChatSessionRemote getChatSessionRemote() {
        if (chatRemote == null) {
            synchronized (ChatSessionRemote.class) {
                if (chatRemote == null) {
                    try {
                        chatSessionRemote = (ChatSessionRemote) reg.lookup("ChatSessionService");
                    } catch (RemoteException | NotBoundException ex) {
                        System.out.println("Exception in getting ChatSessionRemote.....\n" + ex.getMessage());
                    }
                }
            }
        }
        return chatSessionRemote;
    }
    
    public static MessageRemote getMessageRemote() {
        if (messageRemote == null) {
            synchronized (MessageRemote.class) {
                if (messageRemote == null) {
                    try {
                        messageRemote = (MessageRemote) reg.lookup("MessageService");
                    } catch (RemoteException | NotBoundException ex) {
                        System.out.println("Exception in getting MessageRemote.....\n" + ex.getMessage());
                    }
                }
            }
        }
        return messageRemote;
    }
    
    public static SubscriptionRemote getSubscriptionRemote() {
        if (subscriptionRemote == null) {
            synchronized (SubscriptionRemote.class) {
                if (subscriptionRemote == null) {
                    try {
                        subscriptionRemote = (SubscriptionRemote) reg.lookup("SubscriptionService");
                    } catch (RemoteException | NotBoundException ex) {
                        System.out.println("Exception in getting SubscriptionRemote.....\n" + ex.getMessage());
                    }
                }
            }
        }
        return subscriptionRemote;
    }
    
    public static UserRemote getUserRemote() {
        if (userRemote == null) {
            synchronized (UserRemote.class) {
                if (userRemote == null) {
                    try {
                        userRemote = (UserRemote) reg.lookup("UserService");
                    } catch (RemoteException | NotBoundException ex) {
                        System.out.println("Exception in getting UserService.....\n" + ex.getMessage());
                    }
                }
            }
        }
        return userRemote;
    }
}
