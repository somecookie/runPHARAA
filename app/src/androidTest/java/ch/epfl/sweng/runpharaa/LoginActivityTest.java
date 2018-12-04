package ch.epfl.sweng.runpharaa;


import android.app.Activity;
import android.net.Uri;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.internal.firebase_auth.zzap;
import com.google.android.gms.internal.firebase_auth.zzcz;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AdditionalUserInfo;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.auth.UserInfo;

import org.junit.After;
import org.junit.BeforeClass;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

import ch.epfl.sweng.runpharaa.Initializer.TestInitLocation;
import ch.epfl.sweng.runpharaa.login.firebase.FirebaseAuthenticationMock;
import ch.epfl.sweng.runpharaa.login.google.GoogleAuthentication;
import ch.epfl.sweng.runpharaa.login.google.GoogleAuthenticationMock;
import ch.epfl.sweng.runpharaa.location.FakeGpsService;
import ch.epfl.sweng.runpharaa.location.GpsService;

import ch.epfl.sweng.runpharaa.login.LoginActivity;
import ch.epfl.sweng.runpharaa.user.User;

import static android.os.SystemClock.sleep;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class LoginActivityTest extends TestInitLocation {

    @Rule
    public final ActivityTestRule<LoginActivity> mActivityRule =
            new ActivityTestRule<>(LoginActivity.class, false, false);

    @BeforeClass
    public static void setFakeUser() {
        FirebaseAuthenticationMock.setFakeTask(task);
        GpsService.initFakeGps(FakeGpsService.SAT);
        assertTrue(GoogleAuthentication.getInstance(getTargetContext()) instanceof GoogleAuthenticationMock);
    }

    @Test
    public void connectWithFakeFirebaseUser() {
        FirebaseAuthenticationMock.setFakeFireBaseUser(FakeUser);
        mActivityRule.launchActivity(null);
        sleep(3000);
        assertEquals(User.instance.getName(), "FakeUser");
        assertEquals(User.instance.getUid(), "1");
    }

    @Test
    public void connectWithNullUserFails() {
        FirebaseAuthenticationMock.setFakeFireBaseUser(null);
        mActivityRule.launchActivity(null);
        sleep(1000);
        ViewInteraction v = onView(withId(R.id.sign_in_button_google)).check(matches(isDisplayed()));
        FirebaseAuthenticationMock.setFakeFireBaseUser(FakeUser);
        v.perform(click());
        sleep(3000);
    }

    @After
    public void resetUser() {
        FirebaseAuthenticationMock.setFakeFireBaseUser(null);
    }

    // --- FAKE COMPONENTS WE NEED ---

    private static FirebaseUser FakeUser = new FirebaseUser() {
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
        public FirebaseUser zzce() {
            return null;
        }

        @NonNull
        @Override
        public FirebaseApp zzcc() {
            return null;
        }

        @Nullable
        @Override
        public String getDisplayName() {
            return "FakeUser";
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

        @Nullable
        @Override
        public String zzcf() {
            return null;
        }

        @NonNull
        @Override
        public zzcz zzcg() {
            return null;
        }

        @Override
        public void zza(@NonNull zzcz zzcz) {

        }

        @NonNull
        @Override
        public String zzch() {
            return null;
        }

        @NonNull
        @Override
        public String zzci() {
            return null;
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
    private static Task<AuthResult> task = new Task<AuthResult>() {
        @Override
        public boolean isComplete() {
            return true;
        }

        @Override
        public boolean isSuccessful() {
            return true;
        }

        @Override
        public boolean isCanceled() {
            return false;
        }

        @Nullable
        @Override
        public AuthResult getResult() {
            return null;
        }

        @Nullable
        @Override
        public <X extends Throwable> AuthResult getResult(@NonNull Class<X> aClass) throws X {
            return null;
        }

        @Nullable
        @Override
        public Exception getException() {
            return null;
        }

        @NonNull
        @Override
        public Task<AuthResult> addOnSuccessListener(@NonNull OnSuccessListener<? super AuthResult> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<AuthResult> addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener<? super AuthResult> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<AuthResult> addOnSuccessListener(@NonNull Activity activity, @NonNull OnSuccessListener<? super AuthResult> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<AuthResult> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<AuthResult> addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<AuthResult> addOnFailureListener(@NonNull Activity activity, @NonNull OnFailureListener onFailureListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<AuthResult> addOnCompleteListener(@NonNull Activity activity, @NonNull OnCompleteListener<AuthResult> onCompleteListener) {
            return Tasks.call(new Callable<AuthResult>() {
                @Override
                public AuthResult call() throws Exception {
                    return new AuthResult() {
                        @Override
                        public FirebaseUser getUser() {
                            return FakeUser;
                        }

                        @Override
                        public AdditionalUserInfo getAdditionalUserInfo() {
                            return null;
                        }

                        @Override
                        public int describeContents() {
                            return 0;
                        }

                        @Override
                        public void writeToParcel(Parcel dest, int flags) {

                        }
                    };
                }
            });
        }
    };
}
