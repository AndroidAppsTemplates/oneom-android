package com.iam.oneom;

import com.iam.oneom.util.Editor;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by iam on 21.05.17.
 */

public class EditorTest {


    @Test
    public void testSize() {
        assertEquals("1.0GB", Editor.size(1024L * 1024L * 1024L));
        System.out.println(Editor.size(1073741824L));
        assertEquals("1.0MB", Editor.size(1048576L));
        System.out.println(Editor.size(1048576L));
        assertEquals("1.56MB", Editor.size(1048576L + 1024L*567L));
        System.out.println(Editor.size(1048576L + 1024L*567L));
        assertEquals("864B", Editor.size(864L));
        System.out.println(Editor.size(864L));
    }

}
