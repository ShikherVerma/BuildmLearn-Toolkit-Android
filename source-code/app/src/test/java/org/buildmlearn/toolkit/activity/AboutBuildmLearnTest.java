package org.buildmlearn.toolkit.activity;

import android.os.Build;
import android.widget.TextView;

import org.buildmlearn.toolkit.BuildConfig;
import org.buildmlearn.toolkit.R;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class AboutBuildmLearnTest {
    private AboutBuildmLearn aboutBuildmLearn;

    @Before
    public void setup() {
        aboutBuildmLearn = Robolectric.setupActivity(AboutBuildmLearn.class);
    }

    @Test
    public void validateTextViewContent() {
        TextView tvDescription = (TextView) aboutBuildmLearn.findViewById(R.id.description_tv);
        assertNotNull("TextView could not be found", tvDescription);
        assertTrue("TextView contains incorrect text",
          (aboutBuildmLearn.getString(R.string.about_para_1)).equals(tvDescription.getText().toString()));
    }
}