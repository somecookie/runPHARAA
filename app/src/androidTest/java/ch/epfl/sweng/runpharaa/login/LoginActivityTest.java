package ch.epfl.sweng.runpharaa.login;

import android.Manifest;
import android.net.Uri;
import android.os.Parcel;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.internal.firebase_auth.zzap;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.auth.UserInfo;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.runpharaa.Initializer.TestInitLocation;
import ch.epfl.sweng.runpharaa.R;
import ch.epfl.sweng.runpharaa.firebase.authentification.FirebaseAuth;
import ch.epfl.sweng.runpharaa.firebase.authentification.FirebaseAuthMock;
import ch.epfl.sweng.runpharaa.firebase.authentification.google.GoogleAuth;
import ch.epfl.sweng.runpharaa.user.User;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest extends TestInitLocation {

    @Rule
    public final ActivityTestRule<LoginActivity> mActivityRule =
            new ActivityTestRule<>(LoginActivity.class, false, false);

    @BeforeClass
    public static void setFakeUser() {
        GoogleAuth.isTest = true;
        FirebaseAuth.isTest = true;
    }

    @Test
    public void connectWithFakeFirebaseUser() {
        FirebaseAuthMock.setFakeFireBaseUser(Toto);
        mActivityRule.launchActivity(null);
        sleep(1000);
        assertEquals(User.instance.getName(), "Toto");
        assertEquals(User.instance.getUid(), "1");
    }

    @Test
    public void connectWithNullUserFails() {
        FirebaseAuthMock.setFakeFireBaseUser(null);
        mActivityRule.launchActivity(null);
        sleep(1000);
        ViewInteraction v = onView(withId(R.id.sign_in_button_google)).check(matches(isDisplayed()));
        FirebaseAuthMock.setFakeFireBaseUser(Toto);
        v.perform(click());
        sleep(10_000);
    }

    @AfterClass
    public static void unsetFakeUser() {
        GoogleAuth.isTest = false;
        FirebaseAuth.isTest = false;
    }

    private FirebaseUser Toto = new FirebaseUser() {
        @NonNull
        @Override
        public String getUid() {
            return "1";
        }

        @NonNull
        @Override
        public String getProviderId() {
            return "google";
        }

        @Override
        public boolean isAnonymous() {
            return false;
        }

        @Nullable
        @Override
        public List<String> getProviders() {
            return null;
        }

        @NonNull
        @Override
        public List<? extends UserInfo> getProviderData() {
            return new ArrayList<UserInfo>();
        }

        @NonNull
        @Override
        public FirebaseUser zza(@NonNull List<? extends UserInfo> list) {
            return this;
        }

        @Override
        public FirebaseUser zzp() {
            return null;
        }

        @NonNull
        @Override
        public FirebaseApp zzq() {
            return null;
        }

        @Nullable
        @Override
        public String getDisplayName() {
            return "Toto";
        }

        @Nullable
        @Override
        public Uri getPhotoUrl() {
            return Uri.parse("");
        }

        @Nullable
        @Override
        public String getEmail() {
            return "run.pharaa.sweng@gmail.com";
        }

        @Nullable
        @Override
        public String getPhoneNumber() {
            return "0788008080";
        }

        @NonNull
        @Override
        public zzap zzr() {
            return zzap.zzs("");
        }

        @Override
        public void zza(@NonNull zzap zzap) {

        }

        @NonNull
        @Override
        public String zzs() {
            return "zzs";
        }

        @NonNull
        @Override
        public String zzt() {
            return "zzt";
        }

        @Nullable
        @Override
        public FirebaseUserMetadata getMetadata() {
            return null;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

        }

        @Override
        public boolean isEmailVerified() {
            return true;
        }
    };
}
