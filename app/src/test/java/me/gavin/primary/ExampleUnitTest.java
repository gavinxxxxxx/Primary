package me.gavin.primary;

import org.junit.Test;

import java.lang.ref.WeakReference;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void reference() {
        String abc = new String("abc");
        WeakReference<String> abcWeakRef = new WeakReference<>(abc);
        m(abcWeakRef.get());
        String z = abcWeakRef.get();
        abc = null;
        System.out.println("before gc: " + abcWeakRef.get());
        System.gc();
        System.out.println("after gc: " + abcWeakRef.get());
    }

    private void m(String s) {
        String str = s;
        System.out.println("??? : " + str);
    }

}