package org.buildmlearn.toolkit.activity;

import android.os.Build;

import org.buildmlearn.toolkit.BuildConfig;
import org.buildmlearn.toolkit.adapter.TemplateAdapter;
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
public class TemplateActivityTest{
    private TemplateActivity templateActivity;

    @Before
    public void setup() {
        templateActivity = Robolectric.setupActivity(TemplateActivity.class);
    }

    @Test
    public void validateTextViewContent() {
        TemplateAdapter listAdapter = (TemplateAdapter) templateActivity.mAdapter;
        assertNotNull("listAdapter could not be found", listAdapter);
        assertTrue("TextView contains non zero elements", listAdapter.getCount()>0);
    }
}
