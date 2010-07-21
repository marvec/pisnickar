package org.marvec.pisnickar;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import org.jdesktop.application.LocalStorage;

/**
 *
 * @author marvec
 */
public class RegisterUtil {
    private final static String REGISTER_FILE = "registered_user";
    private LocalStorage ls;
    private boolean registered = false;
    private String user = null;

    public RegisterUtil(LocalStorage storage) {
        ls = storage;
        try {
            user = (String) storage.load(REGISTER_FILE);
            registered = user != null && !("".equals(user));
        } catch (Exception e) {
            user = null;
        }
    }

    public boolean isRegistered() {
        return registered;
    }

    public void save() {
        try {
            ls.save(user, REGISTER_FILE);
        } catch (IOException e) {
            // do nothing, registration is best effort
        }
    }

    public void register(String user) {
        this.user = user;
        login(true);
        save();
        registered = true;
    }

    public void login() {
        login(false);
    }
    
    private void login(boolean wait) {
        Thread t = new Thread(new URLRegistration(user));
        t.start();
        if (wait) {
            try {
                t.join();
            } catch (InterruptedException ie) {
                // do nothing
            }
        }
    }

    public String getUser() {
        return user;
    }

    private static class URLRegistration implements Runnable {
        final private static String REGISTER_URL = "http://www.marvec.org/songs/register.php?u=";
        private String user;

        public URLRegistration(String user) {
            this.user = user;
        }

        public void run() {
            try {
                HttpURLConnection c = (HttpURLConnection) (new URL(REGISTER_URL +
                        URLEncoder.encode(user, "UTF-8"))).openConnection();
                c.connect();
                c.getResponseCode();
                c.disconnect();
            } catch (Exception e) {
                // registration is best effort, there is not much we can do now
            }
        }

    }
}
