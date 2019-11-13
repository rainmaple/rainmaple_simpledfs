package cn.edu.ruc.adcourse;

import static cn.edu.ruc.adcourse.utils.CheckSumUtil.getCRC32Value;
import static org.junit.Assert.assertTrue;

import cn.edu.ruc.adcourse.utils.CheckSumUtil;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }


    @Test
    public void getCRC32ValueTest(){
        byte[] bs = {1,2,3,4,8,7,5,6};
        System.out.println(CheckSumUtil.getCRC32Value(bs).length);

    }
}
