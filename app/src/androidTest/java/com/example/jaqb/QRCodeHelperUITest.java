package com.example.jaqb;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.jaqb.services.QRCodeHelper;

import org.junit.Test;

import static org.junit.Assert.*;

public class QRCodeHelperUITest {

    private Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    private QRCodeHelper qrCodeHelper = QRCodeHelper.newInstance(appContext);

    @Test
    public void TestNewInstance_ShouldReturnObjectOfClass() {
        assertTrue(qrCodeHelper instanceof QRCodeHelper);
    }

    @Test
    public void TestSetContent_ShouldSetTheValueOfQRCodeContent() {
        qrCodeHelper.setContent("test_content");
        assertTrue("test_content".contains(qrCodeHelper.getContent()));
    }
}