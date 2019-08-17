package com.example.realestatemanager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.test.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.realestatemanager.controller.activities.MainActivity;
import com.example.realestatemanager.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.microedition.khronos.egl.EGLDisplay;

import static com.example.realestatemanager.utils.UtilsAddFormActivity.getIntFromEditText;
import static com.example.realestatemanager.utils.UtilsAddFormActivity.getLongFromEditText;
import static com.example.realestatemanager.utils.UtilsAddFormActivity.getStringFromEditText;
import static com.example.realestatemanager.utils.UtilsAddFormActivity.setDataValueIntToEditText;
import static com.example.realestatemanager.utils.UtilsAddFormActivity.setDataValueLongToEditText;
import static com.example.realestatemanager.utils.UtilsAddFormActivity.setDataValueTextToEditText;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.realestatemanager", appContext.getPackageName());
    }

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void checkInternetConnectionTest() {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) activityTestRule.getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
           assertTrue(Utils.isNetworkAvailable(activityTestRule.getActivity().getApplicationContext()));
        } else {
            assertFalse(Utils.isNetworkAvailable(activityTestRule.getActivity().getApplicationContext()));
        }
    }

    @Test
    public void getIntFromEditTextTest() {
        TextInputEditText editText = new TextInputEditText(activityTestRule.getActivity());
        editText.setText("5");
        assertEquals(5, getIntFromEditText(editText));
        editText.setText("Not informed yet");
        assertEquals(0, getIntFromEditText(editText));
    }

    @Test
    public void getLongFromEditTextTest() {
        TextInputEditText editText = new TextInputEditText(activityTestRule.getActivity());
        editText.setText("52");
        assertEquals(52, getLongFromEditText(editText));
        editText.setText("Not informed yet");
        assertEquals(0, getLongFromEditText(editText));
    }

    @Test
    public void getStringFromEditTextTest() {
        TextInputEditText editText = new TextInputEditText(activityTestRule.getActivity());
        editText.setText("london");
        assertEquals("london", getStringFromEditText(editText));
        editText.setText("");
        assertNull(getStringFromEditText(editText));
        editText.setText("Not informed yet");
        assertNull(getStringFromEditText(editText));
    }

    @Test
    public void setDataValueTextToEditTextTest() {
        TextInputEditText editText = new TextInputEditText(activityTestRule.getActivity());
        String value = "this is description for example";
        setDataValueTextToEditText(value, activityTestRule.getActivity().getApplicationContext(), editText);
        assertEquals(editText.getText().toString(), value);
        value = null;
        setDataValueTextToEditText(value, activityTestRule.getActivity().getApplicationContext(), editText);
        assertEquals("Not informed yet", editText.getText().toString());
    }

    @Test
    public void setDataValueLongToEditTextTest() {
        TextInputEditText editText = new TextInputEditText(activityTestRule.getActivity());
        long nbrOfRooms = 5;
        setDataValueLongToEditText(nbrOfRooms, activityTestRule.getActivity().getApplicationContext(), editText);
        assertEquals("5", editText.getText().toString());
        nbrOfRooms = 0;
        setDataValueLongToEditText(nbrOfRooms, activityTestRule.getActivity().getApplicationContext(), editText);
        assertEquals("Not informed yet", editText.getText().toString());
    }

    @Test
    public void setDataValueIntToEditTextTest() {
        TextInputEditText editText = new TextInputEditText(activityTestRule.getActivity());
        int nbrOfRooms = 5;
        setDataValueIntToEditText(nbrOfRooms, activityTestRule.getActivity().getApplicationContext(), editText);
        assertEquals("5", editText.getText().toString());
        nbrOfRooms = 0;
        setDataValueIntToEditText(nbrOfRooms, activityTestRule.getActivity().getApplicationContext(), editText);
        assertEquals("Not informed yet", editText.getText().toString());
    }
}
