import com.biqasoft.bpmn.safejs.ExecutorCodeService;
import com.biqasoft.bpmn.safejs.entity.ExecuteJsRequest;
import com.biqasoft.bpmn.safejs.entity.ExecuteJsResponse;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by Nikita on 17.08.2016.
 */
@Test
public class TestsExecution {

    @Test
    public void testPlus(){
        ExecutorCodeService executorCodeService = new ExecutorCodeService();

        ExecuteJsRequest request = new ExecuteJsRequest();
        request.setJsCode("function plusNumber (a,b){return a+b;}; plusNumber(5,78);");

        ExecuteJsResponse executeJsResponse = executorCodeService.executeCode(request);
        Assert.assertNotNull(executeJsResponse);
        Assert.assertNotNull(executeJsResponse.getResult());
        Assert.assertEquals(executeJsResponse.getResult(), 83);
//        Assert.assertEquals(executeJsResponse.getResult(), 83.0);
    }

}
